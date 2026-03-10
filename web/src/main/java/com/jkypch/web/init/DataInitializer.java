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
            "Spring Boot 버전 선택 가이드 — 의존성 호환성과 LTS 전략",
            "Spring Initializr에서 제공하는 모든 GA 버전의 지원 기간, JDK 요구사항, 주요 라이브러리 버전, " +
            "Breaking Change, 보안 취약점까지 정리. 위의 인터랙티브 도구로 버전별 의존성 호환성을 직접 확인할 수 있다.",
            SPRING_BOOT_GUIDE_CONTENT,
            "2026-03-10",
            List.of("Spring Boot", "Java", "Spring Initializr", "LTS", "Dependencies")
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

    private static final String SPRING_BOOT_GUIDE_CONTENT = """
# Spring Boot 버전 선택 가이드

## 왜 버전 선택이 중요한가

Spring Boot 프로젝트를 시작할 때 가장 먼저 결정하는 것이 버전이다. 버전에 따라 사용 가능한 의존성, JDK 요구사항, 지원 기간이 모두 달라진다. 잘못된 선택은 프로젝트 중반에 강제 마이그레이션이나 보안 패치 부재로 이어질 수 있다.

이 글에서는 **start.spring.io에서 접근 가능한 모든 GA 버전**을 기준으로, 실무에서 버전을 선택할 때 고려해야 할 요소들을 정리한다.

---

## 현재 사용 가능한 버전 (2026년 3월 기준)

| 버전 | Spring Framework | 상태 | OSS 지원 | 상용 지원 |
|------|-----------------|------|---------|----------|
| **4.0.3** | 7.0.5 | Current GA | 2026-12 | 2027-12 |
| **3.5.11** | 6.2.16 | **LTS** | 2026-06 | **2032-06** |
| 3.4.x | 6.2.x | EOL (OSS) | 2025-12 | 2026-12 |
| 3.3.x | 6.1.x | EOL | 2025-06 | 2026-06 |
| 2.7.x | 5.3.x | EOL (LTS) | 2023-06 | 2029-06 |

> **LTS(Long-Term Support)**: VMware/Broadcom이 장기 상용 지원을 보장하는 버전. 3.5는 2032년까지, 2.7은 2029년까지 보안 패치를 받을 수 있다 (상용 라이선스 필요).

---

## JDK 요구사항

| Boot 버전 | 최소 JDK | 권장 JDK | 비고 |
|-----------|---------|---------|------|
| 4.0.x | 17 | 21 | Virtual Thread 기본 지원 |
| 3.5.x | 17 | 21 | Java 21 LTS와 최적 조합 |
| 3.3~3.4 | 17 | 21 | |
| 2.7.x | 8 | 17 | Java 8 호환이 필요한 레거시 환경 |

**권장 조합**: Boot 3.5 LTS + JDK 21 LTS — 양쪽 모두 장기 지원 버전이라 안정적이다.

---

## 주요 라이브러리 버전 비교

버전 선택 시 함께 따라오는 핵심 라이브러리의 메이저 버전이 달라진다. 특히 **Hibernate, Jackson, Spring Security**의 메이저 버전 차이는 마이그레이션 비용에 직접적으로 영향을 준다.

| 라이브러리 | Boot 4.0.3 | Boot 3.5.11 | Boot 2.7.x |
|-----------|-----------|------------|-----------|
| Hibernate | **7.2** | 6.6 | 5.6 |
| Jackson | **3.0** | 2.19 | 2.13 |
| Tomcat | **11.0** | 10.1 | 9.0 |
| Spring Security | **7.0** | 6.5 | 5.7 |
| Netty | 4.2 | 4.1 | 4.1 |
| Jakarta EE | **11** | 10 | Java EE 8 (javax) |

### 주목할 변화

- **Boot 4.0의 Jackson 3**: Jackson 2와 호환되지 않음. `ObjectMapper` 패키지가 `com.fasterxml.jackson` → `tools.jackson`으로 변경. 기존 커스텀 직렬화 코드가 있다면 수정 필요.
- **Boot 4.0의 Hibernate 7**: `SessionFactory` API 변경, `@Table` 기본 네이밍 전략 차이.
- **Boot 3.x의 Jakarta 전환**: `javax.*` → `jakarta.*` 네임스페이스. 2.7에서 3.x로의 마이그레이션에서 가장 큰 허들.

---

## Breaking Changes 요약

### Boot 4.0 (from 3.x)

- Spring Framework 7 / Spring Security 7
- Jackson 3 (Jackson 2는 deprecated 상태로 제공)
- Hibernate 7.1+ → 7.2
- Jakarta EE 11 (Servlet 6.1, Persistence 3.2)
- `management.tracing.enabled` → `management.tracing.export.enabled`

### Boot 3.5 (from 3.4)

- `TaskExecutor` 빈 이름: `taskExecutor` → `applicationTaskExecutor`
- `spring-boot-parent` 모듈 미발행
- Redis `spring.data.redis.database` — URL 설정 시 무시됨
- Boolean 프로퍼티 바인딩 강화
- Heapdump Actuator 기본값 `access=NONE`

### Boot 3.0 (from 2.7) — 역대 최대 변경

- Java 17 필수
- `javax.*` → `jakarta.*` 전면 전환
- Spring Security 6 (설정 방식 완전 변경)
- Hibernate 6 (HQL 파서 변경, 일부 쿼리 비호환)

---

## 보안 고려사항

### EOL 버전의 위험성

OSS 지원이 종료된 버전은 **CVE가 발견되어도 공개 패치가 제공되지 않는다**. 예를 들어:

- Boot 3.3 이하: OSS 지원 종료. 새로 발견되는 Spring Framework 취약점에 노출.
- Boot 2.7: OSS 종료(2023년). 상용 라이선스 없이는 패치 불가.

### Spring Security 버전별 주의점

- **Security 5.x** (Boot 2.7): `WebSecurityConfigurerAdapter` 기반 — deprecated이지만 동작.
- **Security 6.x** (Boot 3.x): Lambda DSL 기반 설정으로 전면 전환. `authorizeHttpRequests()` 사용.
- **Security 7.x** (Boot 4.0): 추가 강화. 일부 deprecated API 완전 제거.

### 의존성 호환성 확인의 중요성

서드파티 라이브러리가 아직 특정 Boot 버전을 지원하지 않을 수 있다:

- **Spring Cloud**: Boot 4.1.0-M2에서는 아직 미지원 (`<4.1.0.M1` 범위)
- **MyBatis**: Boot 4.1에서 미지원 (4.0.x까지만)
- **Google Cloud, Okta**: 3.5.x까지만 지원, 4.0에서 미지원

---

## 의존성 카테고리별 가이드

### Web

| 의존성 | 언제 사용 |
|--------|----------|
| **Spring Web** | 대부분의 웹 애플리케이션 (동기, Servlet 기반) |
| **WebFlux** | 비동기/Reactive 서버 (Netty 기반) |
| **SpringDoc OpenAPI** | Swagger UI & API 문서 자동 생성 (Boot 3.5+) |

### SQL

| 의존성 | 언제 사용 |
|--------|----------|
| **Spring Data JPA** | ORM이 필요한 대부분의 경우 |
| **MyBatis** | 복잡한 SQL 제어가 필요할 때 (Boot 3.5+) |
| **Flyway/Liquibase** | DB 스키마 버전 관리 |

### Observability

| 의존성 | 언제 사용 |
|--------|----------|
| **Actuator + Prometheus** | Prometheus 기반 모니터링 |
| **Distributed Tracing** | MSA 환경 요청 추적 |
| **OpenTelemetry** | 통합 관측성 (Boot 4.0+) |

### AI (Boot 3.5+)

Spring AI는 3.5부터 사용 가능하며, OpenAI, Anthropic Claude, Ollama 등 다양한 LLM 프로바이더를 지원한다. 아직 초기 단계이므로 API가 빈번하게 변경될 수 있다.

---

## 버전 선택 의사결정 플로우

1. **신규 프로젝트인가?**
   - Yes → Boot 3.5 LTS (안정성 우선) 또는 Boot 4.0 (최신 기능 필요 시)
   - No → 현재 버전의 지원 상태 확인

2. **장기 운영(3년+)이 필요한가?**
   - Yes → **Boot 3.5 LTS** (2032년까지 상용 지원)
   - No → Boot 4.0도 가능 (1년 후 다음 LTS로 마이그레이션 계획 필요)

3. **Java 8 환경을 벗어날 수 없는가?**
   - Yes → Boot 2.7 + 상용 라이선스 (또는 마이그레이션 계획 수립)
   - No → Boot 3.5+ 권장

4. **Spring Cloud / 서드파티 의존성이 핵심인가?**
   - Yes → 해당 의존성의 Boot 호환성 먼저 확인
   - No → Boot 버전 자유롭게 선택

---

## 정리

| 목적 | 권장 버전 | 이유 |
|------|----------|------|
| 신규 프로덕션 | **3.5 LTS** | 2032년까지 지원, JDK 21과 최적 조합 |
| 최신 기술 탐색 | **4.0** | Jackson 3, Hibernate 7, Spring Framework 7 |
| 레거시 유지보수 | **2.7 LTS** | Java 8 호환, 2029년까지 상용 지원 |
| 마이크로서비스 | **3.5 LTS** | Spring Cloud 완전 지원, 안정적 |

위의 **인터랙티브 도구**에서 버전을 선택하고 의존성을 체크해보면, 호환성과 빌드 설정을 바로 확인할 수 있다. 프로젝트 초기에 이 조합을 미리 검증하면 나중에 의존성 충돌로 고생하는 일을 줄일 수 있다.
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
