package com.jkypch.web.init;

import com.jkypch.web.domain.Post;
import com.jkypch.web.repository.mongo.PostRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements ApplicationRunner {

    private final PostRepository postRepository;

    public DataInitializer(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (postRepository.count() > 0) return;

        postRepository.save(new Post(
            "Prometheus + Grafana 모니터링 스택 구축 - Exporter란 무엇인가",
            "nginx, MongoDB, Redis를 Docker Compose로 운영할 때 Prometheus로 메트릭을 수집하고 Grafana로 시각화하는 과정. " +
            "Exporter가 왜 필요한지, 각 서비스가 Prometheus 포맷을 직접 제공하지 못하는 이유부터 실제 구성까지 다룬다.",
            CONTENT,
            "2026-03-06",
            List.of("Docker", "Prometheus", "Grafana", "nginx", "MongoDB", "Redis", "Monitoring")
        ));
    }

    private static final String CONTENT = """
# Prometheus + Grafana 모니터링 스택 구축

## 배경

개인 서버에 nginx, MongoDB, Redis, Spring Boot를 Docker Compose로 운영하면서 각 서비스의 상태를 실시간으로 파악할 필요가 생겼다. 로그를 직접 보는 것보다 시각화된 대시보드가 훨씬 직관적이고, 이상 징후도 빠르게 포착할 수 있다.

선택한 스택은 **Prometheus + Grafana**다. Prometheus가 메트릭을 수집하고, Grafana가 이를 시각화한다.

---

## 전체 구성

```
[Spring Boot]  ──────────────────────────────────────┐
[nginx]  →  nginx-exporter  ─────────────────────────┤
[MongoDB]  →  mongodb-exporter  ─────────────────────┼─→  Prometheus  →  Grafana
[Redis]  →  redis-exporter  ─────────────────────────┘
```

Prometheus는 각 타겟에서 **주기적으로 메트릭을 pull**한다. 이 pull 방식이 push 방식과 다른 핵심이다. 서비스가 Prometheus에게 데이터를 보내는 게 아니라, Prometheus가 서비스에게 직접 가져간다.

---

## Exporter가 필요한 이유

nginx, MongoDB, Redis는 Prometheus가 생기기 훨씬 전부터 존재했던 소프트웨어다. 각자 자체적인 상태 조회 방식이 있었다.

| 서비스 | 원래 방식 |
|--------|-----------|
| nginx | HTTP `/nginx_status` (단순 텍스트) |
| MongoDB | `db.serverStatus()` (MongoDB 자체 프로토콜) |
| Redis | `INFO` 명령어 (Redis 자체 프로토콜) |

이 포맷을 Prometheus가 이해하는 포맷으로 변환해주는 것이 **Exporter**다. Exporter는 원본 서비스의 데이터를 읽어 `:PORT/metrics` 엔드포인트로 Prometheus 포맷을 노출한다.

---

## Spring Boot는 Exporter가 필요 없는 이유

Spring Boot는 **Micrometer**라는 메트릭 추상화 레이어를 내장하고 있다. `micrometer-registry-prometheus` 의존성 하나만 추가하면 `/actuator/prometheus` 엔드포인트를 직접 제공한다.

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
# application.yml
management:
  endpoints:
    web:
      exposure:
        include: health,info,prometheus
  metrics:
    tags:
      application: ${spring.application.name}
```

이렇게 하면 JVM 메모리, GC, 스레드, HTTP 요청 메트릭 등이 자동으로 수집된다.

---

## nginx Exporter 동작 원리

nginx의 `stub_status` 모듈이 아래와 같은 단순 텍스트를 반환한다.

```
Active connections: 2
server accepts handled requests
 10 10 25
Reading: 0 Writing: 1 Waiting: 1
```

`nginx/nginx-prometheus-exporter`(공식 exporter)가 이를 주기적으로 읽어 Prometheus 포맷으로 변환한다.

```
nginx_connections_active 2
nginx_connections_reading 0
nginx_connections_writing 1
nginx_connections_waiting 1
nginx_http_requests_total 25
```

먼저 nginx에서 `/nginx_status` 엔드포인트를 열어야 한다. 외부에는 노출하지 않고 Docker 내부 서브넷에서만 접근 가능하도록 제한했다.

```nginx
# nginx/conf.d/default.conf
location = /nginx_status {
    stub_status;
    access_log off;
    allow 172.0.0.0/8;
    allow 192.168.0.0/16;
    allow 10.0.0.0/8;
    deny all;
}
```

---

## Docker Compose 구성

```yaml
# docker-compose.yml (모니터링 관련 서비스)

prometheus:
  image: prom/prometheus:latest
  expose:
    - "9090"
  volumes:
    - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml:ro
    - prometheus_data:/prometheus
  command:
    - '--config.file=/etc/prometheus/prometheus.yml'
    - '--storage.tsdb.retention.time=15d'

grafana:
  image: grafana/grafana:latest
  expose:
    - "3000"
  volumes:
    - grafana_data:/var/lib/grafana
    - ./grafana/provisioning:/etc/grafana/provisioning:ro
  environment:
    - GF_SERVER_ROOT_URL=http://localhost/grafana
    - GF_SERVER_SERVE_FROM_SUB_PATH=true
    - GF_SECURITY_ADMIN_PASSWORD=${GRAFANA_ADMIN_PASSWORD:-admin}

nginx-exporter:
  image: nginx/nginx-prometheus-exporter:latest
  expose:
    - "9113"
  command:
    - '--nginx.scrape-uri=http://nginx:80/nginx_status'

mongodb-exporter:
  image: percona/mongodb_exporter:0.40
  expose:
    - "9216"
  command:
    - '--mongodb.uri=mongodb://mongodb:27017'
    - '--collect-all'
    - '--compatible-mode'

redis-exporter:
  image: oliver006/redis_exporter:latest
  expose:
    - "9121"
  environment:
    - REDIS_ADDR=redis:6379
```

`percona/mongodb_exporter:0.40`은 기본적으로 `mongodb_up` 메트릭만 수집한다. `--collect-all`로 전체 수집을 활성화하고, `--compatible-mode`로 기존 Grafana 대시보드와 호환되는 메트릭명을 사용한다.

---

## Prometheus 설정

```yaml
# prometheus/prometheus.yml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring-boot'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['nginx:80']  # blue-green 배포 시 nginx가 active 슬롯으로 라우팅

  - job_name: 'nginx'
    static_configs:
      - targets: ['nginx-exporter:9113']

  - job_name: 'mongodb'
    static_configs:
      - targets: ['mongodb-exporter:9216']

  - job_name: 'redis'
    static_configs:
      - targets: ['redis-exporter:9121']
```

Spring Boot 메트릭은 `nginx:80/actuator/prometheus`로 수집한다. blue-green 배포 환경에서 nginx가 항상 active 슬롯으로 라우팅하기 때문에, Prometheus 입장에서는 타겟 주소 변경 없이 항상 현재 active 컨테이너의 메트릭을 수집할 수 있다.

---

## Grafana 프로비저닝

Grafana는 파일 기반 프로비저닝을 지원한다. 컨테이너가 새로 뜰 때마다 데이터소스와 대시보드가 자동으로 구성된다.

```yaml
# grafana/provisioning/datasources/prometheus.yml
apiVersion: 1
datasources:
  - name: Prometheus
    type: prometheus
    url: http://prometheus:9090
    uid: PBFA97CFB590B2093
    isDefault: true
```

```yaml
# grafana/provisioning/dashboards/dashboards.yml
apiVersion: 1
providers:
  - name: 'default'
    type: file
    options:
      path: /etc/grafana/provisioning/dashboards
```

대시보드 JSON 파일을 `grafana/provisioning/dashboards/` 아래에 두면 Grafana가 자동으로 로드한다. 커뮤니티 대시보드를 사용할 경우 템플릿 변수의 `current` 값을 실제 환경에 맞게 설정해야 한다. 그렇지 않으면 변수가 비어 있어 모든 패널이 N/A를 표시한다.

```json
{
  "name": "instance",
  "type": "query",
  "query": "label_values(nginx_up, instance)",
  "current": {
    "selected": false,
    "text": "nginx-exporter:9113",
    "value": "nginx-exporter:9113"
  }
}
```

---

## 트러블슈팅

**mongodb-exporter가 `mongodb_up`만 노출하는 경우**

percona/mongodb_exporter v0.40은 기본적으로 최소한의 메트릭만 수집한다. `--collect-all` 플래그가 없으면 연결 확인만 한다.

**Grafana 대시보드 패널이 전부 N/A인 경우**

템플릿 변수 `current`가 비어 있으면 해당 변수에 의존하는 모든 패널이 N/A를 표시한다. 변수 쿼리(`label_values(...)`)가 결과를 반환해도 자동 선택이 안 되는 경우가 있어 명시적으로 기본값을 설정해야 한다.

**커뮤니티 대시보드의 메트릭명 불일치**

커뮤니티 대시보드는 특정 버전의 exporter를 기준으로 작성된다. exporter 버전이 다르면 메트릭명이 달라질 수 있다. 예를 들어 percona mongodb exporter v0.40에서는 `mongodb_metrics_document_total` 대신 `mongodb_mongod_metrics_document_total`을 사용한다.
""";
}
