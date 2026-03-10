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
        postRepository.save(new Post(
            "spring-boot-version-guide",
            "Spring Framework 버전 선택 가이드 — 의존성 호환성과 LTS 전략",
            "Spring Framework를 기준으로 Boot, 웹 서버, ORM 등 모든 의존성의 호환성을 정리. " +
            "위의 인터랙티브 도구로 버전별 선택 가능한 조합을 직접 확인할 수 있다.",
            SPRING_GUIDE_CONTENT,
            "2026-03-10",
            List.of("Spring Framework", "Spring Boot", "Java", "LTS", "Dependencies")
        ));

        postRepository.save(new Post(
            "prometheus-grafana-monitoring",
            "Prometheus + Grafana 모니터링 스택 구축 - Exporter란 무엇인가",
            "nginx, MongoDB, Redis를 Docker Compose로 운영할 때 Prometheus로 메트릭을 수집하고 Grafana로 시각화하는 과정. " +
            "Exporter가 왜 필요한지, 각 서비스가 Prometheus 포맷을 직접 제공하지 못하는 이유부터 실제 구성까지 다룬다.",
            CONTENT,
            "2026-03-06",
            List.of("Docker", "Prometheus", "Grafana", "nginx", "MongoDB", "Redis", "Monitoring")
        ));
    }

    private static final String SPRING_GUIDE_CONTENT = """
# Spring Framework 버전 선택 가이드

## 왜 "Framework 기준"인가

Spring 생태계의 모든 것은 **Spring Framework** 위에 올라간다. Spring Boot는 편의를 위한 선택사항이고, Tomcat도 여러 웹 서버 중 하나다. 프로젝트의 기반을 결정하는 것은 Framework 버전이며, 나머지는 그 위에 조합하는 의존성이다.

이 글에서는 Spring Framework 버전을 기준으로, Boot·웹 서버·ORM·JSON 라이브러리 등 모든 의존성의 호환성과 선택 기준을 정리한다.

---

## Spring Framework 버전 현황 (2026년 3월)

| Framework | 출시 | Jakarta/Java EE | JDK | OSS 지원 | 상용 지원 | 대응 Boot |
|-----------|------|----------------|-----|---------|----------|----------|
| **7.0** | 2025-11 | Jakarta EE 11 | 17+ | 2027-06 | 2028-06 | Boot 4.0 |
| **6.2** | 2024-11 | Jakarta EE 10 | 17+ | 2026-06 | **2032-06** | Boot 3.4~3.5 (LTS) |
| 6.1 | 2023-11 | Jakarta EE 10 | 17+ | 2025-06 | 2026-06 | Boot 3.2~3.3 |
| 6.0 | 2022-11 | Jakarta EE 9+ | 17+ | 2024-06 | 2025-08 | Boot 3.0~3.1 |
| 5.3 | 2020-10 | Java EE 7-8 | 8+ | 2024-08 | 2029-06 | Boot 2.5~2.7 |
| 5.2 | 2019-09 | Java EE 7-8 | 8+ | 2021-12 | 2023-12 | Boot 2.2~2.4 |
| 5.1 | 2018-09 | Java EE 7-8 | 8+ | 2020-12 | 2022-12 | Boot 2.1 |
| 5.0 | 2017-09 | Java EE 7-8 | 8+ | 2020-12 | - | Boot 2.0 |
| 4.3 | 2016-06 | Java EE 6-7 | 6+ | 2020-12 | - | Boot 1.5 |
| 4.2 | 2015-07 | Java EE 6-7 | 6+ | 2019-12 | - | Boot 1.3~1.4 |
| 4.1 | 2014-09 | Java EE 6-7 | 6+ | 2018-12 | - | Boot 1.2 |
| 4.0 | 2013-12 | Java EE 6-7 | 6+ | 2017-12 | - | Boot 1.0~1.1 |
| 3.2 | 2012-12 | Java EE 5-6 | 5+ | 2016-12 | - | - |
| 3.1 | 2011-12 | Java EE 5-6 | 5+ | 2016-12 | - | - |
| 3.0 | 2009-12 | Java EE 5 | 5+ | 2013-12 | - | - |

> **핵심**: Framework 6.0부터 `javax.*` → `jakarta.*` 네임스페이스로 전환됨. 5.3 이하와 6.0 이상은 호환되지 않는 별개의 세계다. Spring Boot는 4.0(2013)부터 등장하며, 3.x 이전에는 Boot 없이 순수 Framework로만 개발했다.

---

## Spring Boot는 선택사항이다

Spring Boot 없이 순수 Spring Framework만으로 프로젝트를 구성할 수 있다. Boot가 제공하는 것:

| Boot가 해주는 것 | Boot 없이 직접 해야 하는 것 |
|----------------|--------------------------|
| 자동 설정 (Auto-configuration) | XML/Java Config 직접 작성 |
| 내장 웹 서버 (Tomcat 등) | WAR 배포 또는 서버 직접 임베딩 |
| Starter 의존성 묶음 | 개별 라이브러리 버전 직접 관리 |
| Actuator (운영 엔드포인트) | 모니터링 직접 구현 |
| application.yml 바인딩 | Environment 직접 처리 |

**Boot를 쓰지 않는 경우**: 기존 Servlet 컨테이너(Tomcat/WildFly)에 WAR 배포, 경량 라이브러리 프로젝트, 또는 Boot의 의견이 맞지 않는 특수 환경.

---

## 웹 서버 선택

Boot를 사용하면 기본적으로 Tomcat이 내장되지만, 이것도 선택이다.

| 서버 | 특징 | Framework 7.0 | 6.x | 5.3 |
|------|------|:---:|:---:|:---:|
| **Tomcat** | Servlet 컨테이너 (Boot 기본) | 11.0 | 10.1 | 9.0 |
| **Jetty** | 경량 Servlet 컨테이너 | 12 | 12/11 | 9/10 |
| **Undertow** | Red Hat 경량 서버 | 2.x | 2.x | 2.x |
| **Netty** | Reactive/비동기 (WebFlux 기본) | 4.2 | 4.1 | 4.1 |

- **Tomcat**: 가장 널리 사용, 문서/커뮤니티 풍부. 대부분의 경우 기본 선택.
- **Jetty**: 메모리 사용량이 적고 시작 빠름. 임베디드 환경에 유리.
- **Undertow**: 비동기 I/O 기반, WildFly의 코어.
- **Netty**: WebFlux(Reactive) 사용 시 선택. Servlet API를 사용하지 않음.

---

## ORM / 데이터 접근 계층

| 기술 | Framework 7.0 | 6.x | 5.3 | 비고 |
|------|:---:|:---:|:---:|------|
| **Hibernate** | 7.2 | 6.6 | 5.6 | JPA 구현체 (메이저 버전 차이 주의) |
| **Spring Data JPA** | 4.x | 3.x | 2.x | Repository 추상화 |
| **MyBatis** | 3.x | 3.x | — | SQL 매퍼 (Framework 6.1+) |
| **jOOQ** | 3.19 | 3.19 | 3.x | 타입 안전 SQL 빌더 |
| **Spring Data JDBC** | 4.x | 3.x | 2.x | 경량 JDBC |

### Hibernate 메이저 버전 간 차이

- **5.6 → 6.x**: HQL 파서 전면 교체, 일부 쿼리 비호환. `Type` 시스템 변경.
- **6.x → 7.x**: `SessionFactory` API 변경, `@Table` 네이밍 전략 차이.

---

## JSON 직렬화

| 라이브러리 | Framework 7.0 | 6.x | 5.3 |
|-----------|:---:|:---:|:---:|
| **Jackson** | **3.0** (패키지 변경) | 2.x | 2.x |
| **Gson** | 2.x | 2.x | 2.x |
| **Protobuf** | 4.x | 3.x/4.x | 3.x |

**주의**: Framework 7.0 + Jackson 3.0은 패키지가 `com.fasterxml.jackson` → `tools.jackson`으로 변경됨. 커스텀 직렬화 코드가 있다면 마이그레이션 필요.

---

## Spring Security

| Security 버전 | Framework | 설정 방식 | 비고 |
|:---:|:---:|------|------|
| **7.x** | 7.0 | Lambda DSL | deprecated API 완전 제거 |
| **6.x** | 6.0~6.2 | Lambda DSL | `authorizeHttpRequests()` |
| **5.x** | 5.3 | `WebSecurityConfigurerAdapter` | deprecated이지만 동작 |

6.0에서 설정 방식이 완전히 바뀌었으므로, 5.x → 6.x 마이그레이션 시 보안 설정 코드 전면 수정이 필요하다.

---

## Breaking Changes 요약

### Framework 7.0 (from 6.x)

- Jakarta EE 11 (Servlet 6.1, Persistence 3.2)
- Spring Security 7 (deprecated API 제거)
- Jackson 3 기본 지원 (Jackson 2는 deprecated)
- Hibernate 7.2 기본
- AOT(Ahead-of-Time) 컴파일 강화

### Framework 6.0 (from 5.3) — 최대 변경

- **`javax.*` → `jakarta.*`** 전면 전환
- Java 17 필수
- Spring Security 6 (설정 방식 변경)
- Hibernate 6 (HQL 비호환)
- 서블릿 5.0 → 6.0

---

## 보안 고려사항

### EOL 버전의 위험성

OSS 지원이 종료된 Framework/Boot 버전은 **새 CVE가 발견되어도 공개 패치가 나오지 않는다**.

- Framework 6.1 이하: OSS 종료. 상용 라이선스 없이는 보안 패치 불가.
- Framework 5.3: OSS 종료(2024년). Java 8 필수 환경이 아니면 6.2로 마이그레이션 필요.

### 서드파티 호환성 주의

- **Spring Cloud**: Framework 7.0에서는 아직 일부 모듈 미지원
- **MyBatis**: Framework 6.1 이상에서만 지원
- **Spring AI**: Framework 6.2 이상 필요

---

## 버전 선택 의사결정 플로우

1. **Java 8을 벗어날 수 없는가?**
   - Yes → Framework 5.3 + Boot 2.7 (상용 라이선스 권장)
   - No → 아래 계속

2. **장기 운영(3년+)이 필요한가?**
   - Yes → **Framework 6.2 + Boot 3.5 LTS** (2032년까지 상용 지원)
   - No → Framework 7.0 + Boot 4.0 가능

3. **Boot 없이 순수 Spring을 사용하는가?**
   - Yes → Framework 버전 선택 후 웹 서버/ORM 등 개별 선택
   - No → Boot 버전이 Framework 버전을 결정

4. **Spring Cloud / 서드파티가 핵심인가?**
   - Yes → 해당 라이브러리의 Framework 호환성 먼저 확인
   - No → 자유롭게 선택

---

## 정리

| 목적 | 권장 조합 | 이유 |
|------|----------|------|
| 신규 프로덕션 | **Framework 6.2 + Boot 3.5** | LTS, JDK 21, 2032년까지 지원 |
| 최신 기술 | **Framework 7.0 + Boot 4.0** | Jackson 3, Hibernate 7, Jakarta EE 11 |
| 레거시 유지보수 | **Framework 5.3 + Boot 2.7** | Java 8, 상용 2029년까지 |
| Boot 없이 경량 | **Framework 6.2** | 직접 Tomcat/Jetty 임베딩 |
| MSA | **Framework 6.2 + Boot 3.5** | Spring Cloud 완전 지원 |

위의 **인터랙티브 도구**에서 Framework 버전을 선택하고 필요한 의존성을 조합해보자. Boot를 넣을지, 어떤 웹 서버를 쓸지, ORM은 무엇으로 할지 — 모든 것이 선택이고, 호환성은 도구가 자동으로 확인해준다.
""";

    private static final String CONTENT = """
# Prometheus + Grafana 모니터링 스택 구축

## 배경

개인 서버에 nginx, MongoDB, Redis, Spring Boot를 Docker Compose로 운영하면서 각 서비스의 상태를 실시간으로 파악할 필요가 생겼다. 로그를 직접 보는 것보다 시각화된 대시보드가 훨씬 직관적이고, 이상 징후도 빠르게 포착할 수 있다.

선택한 스택은 **Prometheus + Grafana**다. Prometheus가 메트릭을 수집하고, Grafana가 이를 시각화한다.

---

## 전체 구성

![모니터링 흐름도](/images/monitoring-flow.svg)

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
