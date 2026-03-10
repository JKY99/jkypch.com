/**
 * SpringSelector — 인터랙티브 Spring Framework 버전 & 의존성 선택 도구
 *
 * ┌─────────────────────────────────────────────────────────────────┐
 * │  ★ 핵심 구조:                                                   │
 * │  - 1차 기준: Spring Framework 버전                              │
 * │  - Spring Boot, Tomcat, Hibernate 등은 모두 "의존성"으로 취급     │
 * │                                                                 │
 * │  ★ 데이터 수정 가이드:                                           │
 * │  - FRAMEWORK_VERSIONS: Spring Framework 버전 추가/삭제/수정      │
 * │  - DEPENDENCIES: 의존성 추가/삭제/호환 범위 수정                  │
 * │  - CATEGORIES: 카테고리 탭 추가/삭제                              │
 * │  - 백엔드 서버 불필요 — 모든 데이터가 이 파일에 정의됨             │
 * │                                                                 │
 * │  ★ 양방향 호환성 잠금:                                           │
 * │  - Framework 버전 선택 → 비호환 의존성 disabled                   │
 * │  - 의존성 선택 → 비호환 Framework 버전 disabled                   │
 * └─────────────────────────────────────────────────────────────────┘
 */
import { useState, useMemo } from 'react'
import styles from './SpringBootSelector.module.css'

/* ================================================================
 *  ★ 데이터 정의 영역 — 여기만 수정하면 UI에 자동 반영됩니다
 * ================================================================ */

/**
 * Spring Framework 버전 목록 (1차 선택 기준)
 *
 * - version: 표시 버전 (major.minor 단위)
 * - jdkMin / jdkRecommended: JDK 요구사항
 * - jakartaEE: 대응하는 Jakarta/Java EE 버전
 * - ossEnd / commercialEnd: 지원 종료일
 * - status: 'current' | 'lts' | 'maintenance' | 'eol'
 * - notes: UI에 표시할 특이사항
 */
interface FrameworkVersion {
  version: string
  jdkMin: number
  jdkRecommended: number
  jakartaEE: string
  ossEnd: string
  commercialEnd: string
  status: 'current' | 'lts' | 'maintenance' | 'eol'
  notes?: string
}

const FRAMEWORK_VERSIONS: FrameworkVersion[] = [
  {
    version: '7.0',
    jdkMin: 17,
    jdkRecommended: 21,
    jakartaEE: 'Jakarta EE 11',
    ossEnd: '2026-12-31',
    commercialEnd: '2027-12-31',
    status: 'current',
    notes: 'Jakarta EE 11, AOT 강화, Jackson 3 지원',
  },
  {
    version: '6.2',
    jdkMin: 17,
    jdkRecommended: 21,
    jakartaEE: 'Jakarta EE 10',
    ossEnd: '2026-06-30',
    commercialEnd: '2032-06-30',
    status: 'lts',
    notes: 'Boot 3.5 LTS 기반 — 2032년까지 상용 지원',
  },
  {
    version: '6.1',
    jdkMin: 17,
    jdkRecommended: 21,
    jakartaEE: 'Jakarta EE 10',
    ossEnd: '2025-06-30',
    commercialEnd: '2026-06-30',
    status: 'eol',
    notes: 'OSS 지원 종료',
  },
  {
    version: '6.0',
    jdkMin: 17,
    jdkRecommended: 17,
    jakartaEE: 'Jakarta EE 9+',
    ossEnd: '2024-06-30',
    commercialEnd: '2025-12-31',
    status: 'eol',
    notes: 'javax → jakarta 전환 첫 버전',
  },
  {
    version: '5.3',
    jdkMin: 8,
    jdkRecommended: 17,
    jakartaEE: 'Java EE 8 (javax)',
    ossEnd: '2024-12-31',
    commercialEnd: '2029-06-30',
    status: 'eol',
    notes: 'Java 8 호환 마지막 — javax 네임스페이스',
  },
]

/**
 * 의존성 정의
 *
 * - id: 고유 식별자
 * - name: 표시 이름
 * - description: 짧은 설명
 * - group: 카테고리 (CATEGORIES의 id와 매칭)
 * - compatVersions: 호환되는 Framework major.minor 목록
 *   예: ['7.0', '6.2', '6.1'] → 이 버전들에서만 선택 가능
 *   'all' → 모든 Framework 버전과 호환
 * - versionNote: (선택) 버전별 참고 사항 (예: "Boot 4.0에서는 3.0 사용")
 * - gav: (선택) Maven groupId:artifactId (빌드 설정 생성용)
 */
interface Dependency {
  id: string
  name: string
  description: string
  group: string
  compatVersions: string[] | 'all'
  versionNote?: string
  gav?: string
}

const DEPENDENCIES: Dependency[] = [
  /* ================================================================
   *  ── Spring Boot (Framework 위에 올리는 선택사항) ──
   *  Boot 없이 순수 Spring Framework만 사용할 수도 있다
   * ================================================================ */
  { id: 'boot-4.0', name: 'Spring Boot 4.0', description: 'Auto-config, embedded server, opinionated defaults', group: 'boot', compatVersions: ['7.0'], gav: 'org.springframework.boot:spring-boot-starter:4.0.3' },
  { id: 'boot-3.5', name: 'Spring Boot 3.5 (LTS)', description: '2032년까지 상용 지원 — 가장 안정적', group: 'boot', compatVersions: ['6.2'], gav: 'org.springframework.boot:spring-boot-starter:3.5.11' },
  { id: 'boot-3.4', name: 'Spring Boot 3.4', description: 'OSS 지원 종료됨', group: 'boot', compatVersions: ['6.2'], gav: 'org.springframework.boot:spring-boot-starter:3.4.x' },
  { id: 'boot-3.3', name: 'Spring Boot 3.3', description: 'OSS 지원 종료', group: 'boot', compatVersions: ['6.1'], gav: 'org.springframework.boot:spring-boot-starter:3.3.x' },
  { id: 'boot-2.7', name: 'Spring Boot 2.7 (LTS)', description: 'Java 8 호환, 상용 2029년까지', group: 'boot', compatVersions: ['5.3'], gav: 'org.springframework.boot:spring-boot-starter:2.7.x' },

  /* ================================================================
   *  ── 웹 서버 (Servlet 컨테이너 / Reactive 런타임) ──
   *  Boot 사용 시 기본 내장되지만, 순수 Spring에서는 직접 선택
   * ================================================================ */
  { id: 'tomcat', name: 'Apache Tomcat', description: 'Servlet 컨테이너 (Boot 기본)', group: 'server', compatVersions: 'all', versionNote: 'Framework 7.0→Tomcat 11, 6.x→Tomcat 10.1, 5.3→Tomcat 9', gav: 'org.apache.tomcat.embed:tomcat-embed-core' },
  { id: 'jetty', name: 'Eclipse Jetty', description: '경량 Servlet 컨테이너', group: 'server', compatVersions: 'all', versionNote: 'Framework 7.0→Jetty 12, 6.x→Jetty 12/11, 5.3→Jetty 9/10', gav: 'org.eclipse.jetty:jetty-server' },
  { id: 'undertow', name: 'JBoss Undertow', description: 'Red Hat 경량 웹 서버', group: 'server', compatVersions: 'all', gav: 'io.undertow:undertow-core' },
  { id: 'netty', name: 'Netty', description: 'Reactive/비동기 서버 (WebFlux 기본)', group: 'server', compatVersions: 'all', versionNote: 'Framework 7.0→Netty 4.2, 6.x→Netty 4.1', gav: 'io.netty:netty-all' },

  /* ================================================================
   *  ── Spring 핵심 모듈 ──
   * ================================================================ */
  { id: 'spring-webmvc', name: 'Spring Web MVC', description: 'Servlet 기반 동기 웹 프레임워크', group: 'web', compatVersions: 'all', gav: 'org.springframework:spring-webmvc' },
  { id: 'spring-webflux', name: 'Spring WebFlux', description: 'Reactive 비동기 웹 프레임워크', group: 'web', compatVersions: 'all', gav: 'org.springframework:spring-webflux' },
  { id: 'spring-graphql', name: 'Spring for GraphQL', description: 'GraphQL API 서버', group: 'web', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springframework.graphql:spring-graphql' },
  { id: 'spring-websocket', name: 'Spring WebSocket', description: 'WebSocket 양방향 통신', group: 'web', compatVersions: 'all', gav: 'org.springframework:spring-websocket' },
  { id: 'spring-hateoas', name: 'Spring HATEOAS', description: 'Hypermedia REST 서비스', group: 'web', compatVersions: 'all', gav: 'org.springframework.hateoas:spring-hateoas' },
  { id: 'springdoc-openapi', name: 'SpringDoc OpenAPI', description: 'Swagger UI & API 문서 자동 생성', group: 'web', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springdoc:springdoc-openapi-starter-webmvc-ui' },

  /* ================================================================
   *  ── Security ──
   * ================================================================ */
  { id: 'spring-security', name: 'Spring Security', description: '인증/인가 프레임워크', group: 'security', compatVersions: 'all', versionNote: 'Framework 7.0→Security 7, 6.x→Security 6, 5.3→Security 5', gav: 'org.springframework.security:spring-security-web' },
  { id: 'oauth2-client', name: 'OAuth2 Client', description: 'OAuth2 / OpenID Connect 클라이언트', group: 'security', compatVersions: 'all', gav: 'org.springframework.security:spring-security-oauth2-client' },
  { id: 'oauth2-resource-server', name: 'OAuth2 Resource Server', description: 'JWT/Opaque 토큰 기반 리소스 서버', group: 'security', compatVersions: 'all', gav: 'org.springframework.security:spring-security-oauth2-resource-server' },
  { id: 'oauth2-auth-server', name: 'OAuth2 Authorization Server', description: 'OAuth2 인가 서버', group: 'security', compatVersions: ['7.0', '6.2', '6.1', '6.0'], gav: 'org.springframework.security:spring-security-oauth2-authorization-server' },

  /* ================================================================
   *  ── ORM / 데이터 접근 ──
   * ================================================================ */
  { id: 'spring-data-jpa', name: 'Spring Data JPA', description: 'JPA Repository 추상화', group: 'data', compatVersions: 'all', gav: 'org.springframework.data:spring-data-jpa' },
  { id: 'hibernate', name: 'Hibernate ORM', description: 'JPA 구현체', group: 'data', compatVersions: 'all', versionNote: 'Framework 7.0→Hibernate 7.2, 6.x→Hibernate 6.x, 5.3→Hibernate 5.6', gav: 'org.hibernate.orm:hibernate-core' },
  { id: 'spring-data-jdbc', name: 'Spring Data JDBC', description: '경량 JDBC 데이터 접근', group: 'data', compatVersions: 'all', gav: 'org.springframework.data:spring-data-jdbc' },
  { id: 'mybatis', name: 'MyBatis', description: 'SQL 매퍼 프레임워크', group: 'data', compatVersions: ['7.0', '6.2', '6.1'], versionNote: 'mybatis-spring-boot-starter 3.x (Boot 3+)', gav: 'org.mybatis.spring.boot:mybatis-spring-boot-starter' },
  { id: 'jooq', name: 'jOOQ', description: '타입 안전 SQL 빌더', group: 'data', compatVersions: 'all', gav: 'org.jooq:jooq' },
  { id: 'flyway', name: 'Flyway', description: 'SQL 마이그레이션 관리', group: 'data', compatVersions: 'all', gav: 'org.flywaydb:flyway-core' },
  { id: 'liquibase', name: 'Liquibase', description: 'DB 변경 관리 & 마이그레이션', group: 'data', compatVersions: 'all', gav: 'org.liquibase:liquibase-core' },

  /* ================================================================
   *  ── DB 드라이버 ──
   * ================================================================ */
  { id: 'postgresql', name: 'PostgreSQL', description: 'PostgreSQL JDBC 드라이버', group: 'driver', compatVersions: 'all', gav: 'org.postgresql:postgresql' },
  { id: 'mysql', name: 'MySQL', description: 'MySQL JDBC 드라이버', group: 'driver', compatVersions: 'all', gav: 'com.mysql:mysql-connector-j' },
  { id: 'oracle', name: 'Oracle', description: 'Oracle JDBC 드라이버', group: 'driver', compatVersions: 'all', gav: 'com.oracle.database.jdbc:ojdbc11' },
  { id: 'h2', name: 'H2 Database', description: '인메모리 & 임베디드 DB', group: 'driver', compatVersions: 'all', gav: 'com.h2database:h2' },
  { id: 'mariadb', name: 'MariaDB', description: 'MariaDB JDBC 드라이버', group: 'driver', compatVersions: 'all', gav: 'org.mariadb.jdbc:mariadb-java-client' },

  /* ================================================================
   *  ── NoSQL ──
   * ================================================================ */
  { id: 'spring-data-mongodb', name: 'Spring Data MongoDB', description: 'MongoDB 문서 데이터 접근', group: 'nosql', compatVersions: 'all', gav: 'org.springframework.data:spring-data-mongodb' },
  { id: 'spring-data-redis', name: 'Spring Data Redis', description: 'Redis 키-값 저장소', group: 'nosql', compatVersions: 'all', gav: 'org.springframework.data:spring-data-redis' },
  { id: 'spring-data-elasticsearch', name: 'Spring Data Elasticsearch', description: 'Elasticsearch 검색 엔진', group: 'nosql', compatVersions: 'all', gav: 'org.springframework.data:spring-data-elasticsearch' },
  { id: 'spring-data-neo4j', name: 'Spring Data Neo4j', description: 'Neo4j 그래프 DB', group: 'nosql', compatVersions: 'all', gav: 'org.springframework.data:spring-data-neo4j' },
  { id: 'spring-data-r2dbc', name: 'Spring Data R2DBC', description: 'Reactive 관계형 DB 접근', group: 'nosql', compatVersions: ['7.0', '6.2', '6.1', '6.0'], gav: 'org.springframework.data:spring-data-r2dbc' },

  /* ================================================================
   *  ── Serialization / JSON ──
   * ================================================================ */
  { id: 'jackson', name: 'Jackson', description: 'JSON 직렬화 (Spring 기본)', group: 'serialization', compatVersions: 'all', versionNote: 'Framework 7.0→Jackson 3.0 (패키지 변경), 6.x/5.3→Jackson 2.x', gav: 'com.fasterxml.jackson.core:jackson-databind' },
  { id: 'gson', name: 'Gson', description: 'Google JSON 라이브러리', group: 'serialization', compatVersions: 'all', gav: 'com.google.code.gson:gson' },
  { id: 'protobuf', name: 'Protocol Buffers', description: 'Google 바이너리 직렬화', group: 'serialization', compatVersions: 'all', gav: 'com.google.protobuf:protobuf-java' },

  /* ================================================================
   *  ── Messaging ──
   * ================================================================ */
  { id: 'spring-kafka', name: 'Spring for Kafka', description: 'Apache Kafka 메시징', group: 'messaging', compatVersions: 'all', gav: 'org.springframework.kafka:spring-kafka' },
  { id: 'spring-amqp', name: 'Spring for RabbitMQ', description: 'AMQP / RabbitMQ', group: 'messaging', compatVersions: 'all', gav: 'org.springframework.amqp:spring-rabbit' },
  { id: 'spring-pulsar', name: 'Spring for Pulsar', description: 'Apache Pulsar', group: 'messaging', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springframework.pulsar:spring-pulsar' },
  { id: 'spring-jms', name: 'Spring JMS (ActiveMQ)', description: 'JMS 메시징', group: 'messaging', compatVersions: 'all', gav: 'org.springframework:spring-jms' },

  /* ================================================================
   *  ── Observability / 운영 ──
   * ================================================================ */
  { id: 'micrometer', name: 'Micrometer Core', description: '메트릭 수집 추상화', group: 'ops', compatVersions: 'all', gav: 'io.micrometer:micrometer-core' },
  { id: 'micrometer-prometheus', name: 'Micrometer Prometheus', description: 'Prometheus 레지스트리', group: 'ops', compatVersions: 'all', gav: 'io.micrometer:micrometer-registry-prometheus' },
  { id: 'micrometer-tracing', name: 'Micrometer Tracing', description: '분산 추적', group: 'ops', compatVersions: ['7.0', '6.2', '6.1'], gav: 'io.micrometer:micrometer-tracing' },
  { id: 'opentelemetry', name: 'OpenTelemetry', description: '통합 관측성 (traces, metrics, logs)', group: 'ops', compatVersions: ['7.0'], versionNote: 'Boot 4.0에서 네이티브 통합', gav: 'io.opentelemetry:opentelemetry-api' },
  { id: 'boot-actuator', name: 'Spring Boot Actuator', description: '운영 엔드포인트 (Boot 필요)', group: 'ops', compatVersions: ['7.0', '6.2', '6.1', '6.0'], gav: 'org.springframework.boot:spring-boot-starter-actuator' },

  /* ================================================================
   *  ── Spring Cloud ──
   * ================================================================ */
  { id: 'cloud-config', name: 'Spring Cloud Config', description: '중앙 설정 서버/클라이언트', group: 'cloud', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springframework.cloud:spring-cloud-config-client' },
  { id: 'cloud-eureka', name: 'Eureka Discovery', description: '서비스 디스커버리', group: 'cloud', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client' },
  { id: 'cloud-gateway', name: 'Spring Cloud Gateway', description: 'API 게이트웨이 (WebFlux 기반)', group: 'cloud', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springframework.cloud:spring-cloud-starter-gateway' },
  { id: 'cloud-feign', name: 'OpenFeign', description: '선언적 REST 클라이언트', group: 'cloud', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springframework.cloud:spring-cloud-starter-openfeign' },
  { id: 'cloud-resilience4j', name: 'Resilience4J', description: '서킷 브레이커 & 내결함성', group: 'cloud', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j' },

  /* ================================================================
   *  ── 개발 & 테스트 도구 ──
   * ================================================================ */
  { id: 'lombok', name: 'Lombok', description: '보일러플레이트 코드 제거', group: 'devtools', compatVersions: 'all', gav: 'org.projectlombok:lombok' },
  { id: 'boot-devtools', name: 'Spring Boot DevTools', description: '빠른 재시작 & 라이브 리로드 (Boot 필요)', group: 'devtools', compatVersions: ['7.0', '6.2', '6.1', '6.0'], gav: 'org.springframework.boot:spring-boot-devtools' },
  { id: 'testcontainers', name: 'Testcontainers', description: '통합 테스트용 Docker 컨테이너', group: 'devtools', compatVersions: 'all', gav: 'org.testcontainers:testcontainers' },
  { id: 'boot-docker-compose', name: 'Docker Compose Support', description: 'Boot 개발 환경 Docker Compose 통합', group: 'devtools', compatVersions: ['7.0', '6.2', '6.1'], gav: 'org.springframework.boot:spring-boot-docker-compose' },
  { id: 'validation', name: 'Bean Validation', description: 'Hibernate Validator', group: 'devtools', compatVersions: 'all', gav: 'org.hibernate.validator:hibernate-validator' },
  { id: 'spring-batch', name: 'Spring Batch', description: '대용량 배치 처리', group: 'devtools', compatVersions: 'all', gav: 'org.springframework.batch:spring-batch-core' },
  { id: 'quartz', name: 'Quartz Scheduler', description: '스케줄링 프레임워크', group: 'devtools', compatVersions: 'all', gav: 'org.quartz-scheduler:quartz' },
  { id: 'spring-cache', name: 'Spring Cache', description: '캐시 추상화', group: 'devtools', compatVersions: 'all', gav: 'org.springframework:spring-context-support' },

  /* ================================================================
   *  ── AI (Spring AI, Framework 6.2+ 필요) ──
   * ================================================================ */
  { id: 'spring-ai-openai', name: 'Spring AI - OpenAI', description: 'OpenAI GPT 통합', group: 'ai', compatVersions: ['7.0', '6.2'], gav: 'org.springframework.ai:spring-ai-openai-spring-boot-starter' },
  { id: 'spring-ai-anthropic', name: 'Spring AI - Anthropic', description: 'Anthropic Claude 통합', group: 'ai', compatVersions: ['7.0', '6.2'], gav: 'org.springframework.ai:spring-ai-anthropic-spring-boot-starter' },
  { id: 'spring-ai-ollama', name: 'Spring AI - Ollama', description: 'Ollama 로컬 LLM 통합', group: 'ai', compatVersions: ['7.0', '6.2'], gav: 'org.springframework.ai:spring-ai-ollama-spring-boot-starter' },
]

/**
 * 카테고리 탭 정의
 * - id: DEPENDENCIES의 group과 매칭
 * - label: 탭에 표시될 텍스트
 */
const CATEGORIES = [
  { id: 'all', label: '전체' },
  { id: 'boot', label: 'Spring Boot' },
  { id: 'server', label: 'Web Server' },
  { id: 'web', label: 'Web' },
  { id: 'security', label: 'Security' },
  { id: 'data', label: 'ORM / Data' },
  { id: 'driver', label: 'DB Driver' },
  { id: 'nosql', label: 'NoSQL' },
  { id: 'serialization', label: 'JSON' },
  { id: 'messaging', label: 'Messaging' },
  { id: 'ops', label: 'Observability' },
  { id: 'cloud', label: 'Cloud' },
  { id: 'devtools', label: 'Dev & Test' },
  { id: 'ai', label: 'AI' },
]

/* ================================================================
 *  ★ 유틸리티 함수
 * ================================================================ */

/**
 * 의존성이 특정 Framework 버전과 호환되는지 판단
 * compatVersions가 'all'이면 모든 버전과 호환,
 * 배열이면 해당 버전 문자열이 포함되어야 호환
 */
function isCompatible(dep: Dependency, fwVersion: string): boolean {
  if (dep.compatVersions === 'all') return true
  return dep.compatVersions.includes(fwVersion)
}

/**
 * 선택한 의존성을 Maven/Gradle 설정으로 변환
 * gav(groupId:artifactId) 필드 기반
 */
function generateConfig(selectedIds: string[], format: 'maven' | 'gradle'): string {
  if (selectedIds.length === 0) return format === 'maven'
    ? '<!-- 의존성을 선택하세요 -->'
    : '// 의존성을 선택하세요'

  const deps = selectedIds
    .map(id => DEPENDENCIES.find(d => d.id === id))
    .filter((d): d is Dependency => !!d && !!d.gav)

  if (format === 'maven') {
    return deps.map(d => {
      const [groupId, artifactId] = d.gav!.split(':')
      return `<dependency>\n    <groupId>${groupId}</groupId>\n    <artifactId>${artifactId}</artifactId>\n</dependency>`
    }).join('\n\n')
  }

  return deps.map(d => `implementation '${d.gav}'`).join('\n')
}

/* ================================================================
 *  ★ 컴포넌트 본체
 * ================================================================ */

export default function SpringBootSelector() {
  /* ── 상태 관리 ── */
  const [selectedVersion, setSelectedVersion] = useState(FRAMEWORK_VERSIONS[0].version)
  const [selectedCategory, setSelectedCategory] = useState('all')
  const [selectedDeps, setSelectedDeps] = useState<Set<string>>(new Set())
  const [buildTool, setBuildTool] = useState<'maven' | 'gradle'>('maven')
  const [searchQuery, setSearchQuery] = useState('')

  /* ── 현재 선택된 Framework 버전 객체 ── */
  const currentVersion = FRAMEWORK_VERSIONS.find(v => v.version === selectedVersion)!

  /**
   * ── Framework 버전별 선택 가능 여부 (양방향 잠금) ──
   * 이미 선택된 의존성이 있으면, 모든 선택 의존성을 지원하는 버전만 선택 가능.
   */
  const disabledVersions = useMemo(() => {
    if (selectedDeps.size === 0) return new Set<string>()
    const disabled = new Set<string>()
    for (const v of FRAMEWORK_VERSIONS) {
      for (const depId of selectedDeps) {
        const dep = DEPENDENCIES.find(d => d.id === depId)
        if (dep && !isCompatible(dep, v.version)) {
          disabled.add(v.version)
          break
        }
      }
    }
    return disabled
  }, [selectedDeps])

  /* ── 필터링된 의존성 목록 (카테고리 + 검색어) ── */
  const filteredDeps = useMemo(() => {
    return DEPENDENCIES.filter(dep => {
      if (selectedCategory !== 'all' && dep.group !== selectedCategory) return false
      if (searchQuery) {
        const q = searchQuery.toLowerCase()
        return dep.name.toLowerCase().includes(q) || dep.description.toLowerCase().includes(q)
      }
      return true
    })
  }, [selectedCategory, searchQuery])

  /* ── 의존성 체크박스 토글 ── */
  function toggleDep(id: string) {
    setSelectedDeps(prev => {
      const next = new Set(prev)
      if (next.has(id)) next.delete(id)
      else next.add(id)
      return next
    })
  }

  /* ── 선택 전체 해제 ── */
  function clearAll() {
    setSelectedDeps(new Set())
  }

  /* ── 생성된 빌드 설정 텍스트 ── */
  const configText = useMemo(
    () => generateConfig([...selectedDeps], buildTool),
    [selectedDeps, buildTool]
  )

  return (
    <div className={styles.selector}>
      {/* ━━━ 헤더 ━━━ */}
      <div className={styles.selectorHeader}>
        <h2 className={styles.selectorTitle}>Spring Dependency Selector</h2>
        <p className={styles.selectorDesc}>
          Spring Framework 버전을 선택하고, 필요한 의존성을 체크하세요.
          Spring Boot, 웹 서버, ORM 등 모든 것이 선택사항입니다.
        </p>
      </div>

      {/* ━━━ Framework 버전 선택 영역 ━━━ */}
      <div className={styles.versionSection}>
        <h3 className={styles.subTitle}>Spring Framework Version</h3>
        <div className={styles.versionGrid}>
          {FRAMEWORK_VERSIONS.map(v => {
            const versionDisabled = disabledVersions.has(v.version)
            return (
              <button
                key={v.version}
                className={`${styles.versionCard} ${selectedVersion === v.version ? styles.versionActive : ''} ${v.status === 'eol' ? styles.versionEol : ''} ${versionDisabled ? styles.versionDisabled : ''}`}
                onClick={() => setSelectedVersion(v.version)}
                disabled={versionDisabled}
              >
                <div className={styles.versionTop}>
                  <span className={styles.versionNumber}>{v.version}</span>
                  {v.status === 'lts' && <span className={styles.ltsBadge}>LTS</span>}
                  {v.status === 'current' && <span className={styles.currentBadge}>Current</span>}
                  {v.status === 'eol' && <span className={styles.eolBadge}>EOL</span>}
                </div>
                <div className={styles.versionMeta}>
                  {v.jakartaEE} · JDK {v.jdkMin}+
                </div>
              </button>
            )
          })}
        </div>
      </div>

      {/* ━━━ 선택된 버전 상세 정보 ━━━ */}
      <div className={styles.versionDetail}>
        <div className={styles.detailGrid}>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>Jakarta / Java EE</span>
            <span className={styles.detailValue}>{currentVersion.jakartaEE}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>JDK</span>
            <span className={styles.detailValue}>{currentVersion.jdkMin}+ (권장 {currentVersion.jdkRecommended})</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>OSS 지원</span>
            <span className={styles.detailValue}>{currentVersion.ossEnd}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>상용 지원</span>
            <span className={styles.detailValue}>{currentVersion.commercialEnd}</span>
          </div>
        </div>
        {currentVersion.notes && (
          <p className={styles.detailNotes}>{currentVersion.notes}</p>
        )}
      </div>

      {/* ━━━ 의존성 선택 영역 ━━━ */}
      <div className={styles.depsSection}>
        <div className={styles.depsHeader}>
          <h3 className={styles.subTitle}>Dependencies</h3>
          <input
            className={styles.searchInput}
            type="text"
            placeholder="의존성 검색..."
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)}
          />
        </div>

        {/* 카테고리 탭 */}
        <div className={styles.categoryTabs}>
          {CATEGORIES.map(cat => (
            <button
              key={cat.id}
              className={`${styles.catTab} ${selectedCategory === cat.id ? styles.catTabActive : ''}`}
              onClick={() => setSelectedCategory(cat.id)}
            >
              {cat.label}
            </button>
          ))}
        </div>

        {/* 의존성 목록 */}
        <div className={styles.depsList}>
          {filteredDeps.map(dep => {
            const compatible = isCompatible(dep, selectedVersion)
            const checked = selectedDeps.has(dep.id)
            return (
              <label
                key={dep.id}
                className={`${styles.depItem} ${!compatible ? styles.depIncompatible : ''} ${checked ? styles.depChecked : ''}`}
              >
                <input
                  type="checkbox"
                  checked={checked}
                  onChange={() => toggleDep(dep.id)}
                  disabled={!compatible}
                  className={styles.depCheckbox}
                />
                <div className={styles.depInfo}>
                  <div className={styles.depNameRow}>
                    <span className={styles.depName}>{dep.name}</span>
                    {!compatible && <span className={styles.incompatBadge}>비호환</span>}
                  </div>
                  <span className={styles.depDesc}>{dep.description}</span>
                  {dep.versionNote && compatible && (
                    <span className={styles.depVersionNote}>{dep.versionNote}</span>
                  )}
                </div>
              </label>
            )
          })}
          {filteredDeps.length === 0 && (
            <p className={styles.emptyDeps}>검색 결과가 없습니다.</p>
          )}
        </div>
      </div>

      {/* ━━━ 선택 결과 & 빌드 설정 ━━━ */}
      {selectedDeps.size > 0 && (
        <div className={styles.resultSection}>
          <div className={styles.resultHeader}>
            <h3 className={styles.subTitle}>
              선택된 의존성 ({selectedDeps.size}개)
            </h3>
            <div className={styles.resultActions}>
              <div className={styles.buildToggle}>
                <button
                  className={`${styles.toggleBtn} ${buildTool === 'maven' ? styles.toggleActive : ''}`}
                  onClick={() => setBuildTool('maven')}
                >
                  Maven
                </button>
                <button
                  className={`${styles.toggleBtn} ${buildTool === 'gradle' ? styles.toggleActive : ''}`}
                  onClick={() => setBuildTool('gradle')}
                >
                  Gradle
                </button>
              </div>
              <button className={styles.clearBtn} onClick={clearAll}>전체 해제</button>
            </div>
          </div>

          {/* 선택된 의존성 태그 */}
          <div className={styles.selectedTags}>
            {[...selectedDeps].map(id => {
              const dep = DEPENDENCIES.find(d => d.id === id)
              return (
                <span
                  key={id}
                  className={styles.selectedTag}
                  onClick={() => toggleDep(id)}
                >
                  {dep?.name ?? id} ×
                </span>
              )
            })}
          </div>

          {/* 빌드 설정 코드 */}
          <pre className={styles.codeBlock}><code>{configText}</code></pre>
        </div>
      )}
    </div>
  )
}
