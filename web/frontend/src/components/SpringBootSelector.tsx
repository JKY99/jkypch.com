/**
 * SpringBootSelector — 인터랙티브 Spring Boot 버전 & 의존성 선택 도구
 *
 * ┌─────────────────────────────────────────────────────────────────┐
 * │  이 컴포넌트는 블로그 포스트 상단에 삽입되어                       │
 * │  Spring Boot 버전별 의존성 호환성을 시각적으로 보여줍니다.          │
 * │                                                                 │
 * │  ★ 데이터 수정 가이드:                                           │
 * │  - SPRING_BOOT_VERSIONS: 버전 추가/삭제/수정                     │
 * │  - DEPENDENCIES: 의존성 추가/삭제/호환 버전 범위 수정              │
 * │  - CATEGORIES: 카테고리 탭 추가/삭제                              │
 * │  - 백엔드 서버 불필요 — 모든 데이터가 이 파일에 정의됨             │
 * └─────────────────────────────────────────────────────────────────┘
 */
import { useState, useMemo } from 'react'
import styles from './SpringBootSelector.module.css'

/* ================================================================
 *  ★ 데이터 정의 영역 — 여기만 수정하면 UI에 자동 반영됩니다
 * ================================================================ */

/**
 * Spring Boot 버전 목록
 * - version: 표시 버전
 * - lts: LTS 여부 (true면 LTS 뱃지 표시)
 * - springVersion: 대응하는 Spring Framework 버전
 * - jdkMin: 최소 JDK 버전
 * - jdkRecommended: 권장 JDK 버전
 * - ossEnd: OSS 지원 종료일
 * - commercialEnd: 상용 지원 종료일
 * - status: 'current' | 'lts' | 'eol' (표시용)
 * - notes: 특이사항 (선택)
 */
interface BootVersion {
  version: string
  lts: boolean
  springVersion: string
  jdkMin: number
  jdkRecommended: number
  ossEnd: string
  commercialEnd: string
  status: 'current' | 'lts' | 'eol'
  hibernateVersion: string
  jacksonVersion: string
  tomcatVersion: string
  securityVersion: string
  notes?: string
}

const SPRING_BOOT_VERSIONS: BootVersion[] = [
  /* ── 최신 GA 버전 ── */
  {
    version: '4.0.3',
    lts: false,
    springVersion: '7.0.5',
    jdkMin: 17,
    jdkRecommended: 21,
    ossEnd: '2026-12-31',
    commercialEnd: '2027-12-31',
    status: 'current',
    hibernateVersion: '7.2.4',
    jacksonVersion: '3.0.4',
    tomcatVersion: '11.0.18',
    securityVersion: '7.0.3',
    notes: 'Jackson 3, Hibernate 7, Jakarta EE 11',
  },
  /* ── LTS 버전 ── */
  {
    version: '3.5.11',
    lts: true,
    springVersion: '6.2.16',
    jdkMin: 17,
    jdkRecommended: 21,
    ossEnd: '2026-06-30',
    commercialEnd: '2032-06-30',
    status: 'lts',
    hibernateVersion: '6.6.42',
    jacksonVersion: '2.19.4',
    tomcatVersion: '10.1.52',
    securityVersion: '6.5.8',
    notes: '상용 지원 2032년까지 — 안정적 운영 환경에 권장',
  },
  /* ── 이전 버전 (EOL 포함) ── */
  {
    version: '3.4.x',
    lts: false,
    springVersion: '6.2.x',
    jdkMin: 17,
    jdkRecommended: 21,
    ossEnd: '2025-12-31',
    commercialEnd: '2026-12-31',
    status: 'eol',
    hibernateVersion: '6.6.x',
    jacksonVersion: '2.18.x',
    tomcatVersion: '10.1.x',
    securityVersion: '6.4.x',
    notes: 'OSS 지원 종료됨 — 3.5로 업그레이드 권장',
  },
  {
    version: '3.3.x',
    lts: false,
    springVersion: '6.1.x',
    jdkMin: 17,
    jdkRecommended: 21,
    ossEnd: '2025-06-30',
    commercialEnd: '2026-06-30',
    status: 'eol',
    hibernateVersion: '6.5.x',
    jacksonVersion: '2.17.x',
    tomcatVersion: '10.1.x',
    securityVersion: '6.3.x',
    notes: 'OSS/상용 모두 종료 임박',
  },
  {
    version: '2.7.x',
    lts: true,
    springVersion: '5.3.x',
    jdkMin: 8,
    jdkRecommended: 17,
    ossEnd: '2023-06-30',
    commercialEnd: '2029-06-30',
    status: 'eol',
    hibernateVersion: '5.6.x',
    jacksonVersion: '2.13.x',
    tomcatVersion: '9.0.x',
    securityVersion: '5.7.x',
    notes: 'LTS지만 OSS 종료 — javax 네임스페이스 (Jakarta 미적용)',
  },
]

/**
 * 의존성 정의
 * - id: 고유 식별자 (Spring Initializr ID와 동일)
 * - name: 표시 이름
 * - description: 짧은 설명
 * - group: 카테고리 (CATEGORIES의 id와 매칭)
 * - versionRange: 지원하는 Boot 버전 범위
 *   - 'all': 모든 버전
 *   - '>=3.5': 3.5 이상
 *   - '>=4.0': 4.0 이상
 *   - '3.x': 3.x 계열만
 *   - '<=3.5': 3.5 이하
 */
interface Dependency {
  id: string
  name: string
  description: string
  group: string
  versionRange: string
}

const DEPENDENCIES: Dependency[] = [
  /* ── Web ── */
  { id: 'web', name: 'Spring Web', description: 'Spring MVC를 사용한 RESTful 웹 애플리케이션', group: 'web', versionRange: 'all' },
  { id: 'webflux', name: 'Spring WebFlux', description: 'Reactive 웹 애플리케이션', group: 'web', versionRange: 'all' },
  { id: 'graphql', name: 'Spring for GraphQL', description: 'GraphQL API 지원', group: 'web', versionRange: 'all' },
  { id: 'websocket', name: 'WebSocket', description: '양방향 실시간 통신', group: 'web', versionRange: 'all' },
  { id: 'hateoas', name: 'Spring HATEOAS', description: 'HATEOAS 기반 RESTful 서비스', group: 'web', versionRange: 'all' },
  { id: 'springdoc-openapi', name: 'SpringDoc OpenAPI', description: 'Swagger UI & API 문서 자동 생성', group: 'web', versionRange: '>=3.5' },

  /* ── SQL ── */
  { id: 'data-jpa', name: 'Spring Data JPA', description: 'JPA를 사용한 SQL 데이터 접근', group: 'sql', versionRange: 'all' },
  { id: 'data-jdbc', name: 'Spring Data JDBC', description: '경량 JDBC 데이터 접근', group: 'sql', versionRange: 'all' },
  { id: 'mybatis', name: 'MyBatis', description: 'SQL 매퍼 프레임워크', group: 'sql', versionRange: '>=3.5' },
  { id: 'jooq', name: 'jOOQ', description: '타입 안전 SQL 빌더', group: 'sql', versionRange: 'all' },
  { id: 'flyway', name: 'Flyway', description: 'SQL 마이그레이션 관리', group: 'sql', versionRange: 'all' },
  { id: 'liquibase', name: 'Liquibase', description: 'DB 변경 관리 & 마이그레이션', group: 'sql', versionRange: 'all' },
  { id: 'postgresql', name: 'PostgreSQL', description: 'PostgreSQL JDBC 드라이버', group: 'sql', versionRange: 'all' },
  { id: 'mysql', name: 'MySQL', description: 'MySQL JDBC 드라이버', group: 'sql', versionRange: 'all' },
  { id: 'oracle', name: 'Oracle', description: 'Oracle JDBC 드라이버', group: 'sql', versionRange: 'all' },
  { id: 'h2', name: 'H2 Database', description: '인메모리 & 임베디드 DB (테스트용)', group: 'sql', versionRange: 'all' },

  /* ── NoSQL ── */
  { id: 'data-mongodb', name: 'Spring Data MongoDB', description: 'MongoDB 문서 데이터 접근', group: 'nosql', versionRange: 'all' },
  { id: 'data-redis', name: 'Spring Data Redis', description: 'Redis 키-값 저장소 접근', group: 'nosql', versionRange: 'all' },
  { id: 'data-elasticsearch', name: 'Spring Data Elasticsearch', description: 'Elasticsearch 검색 엔진', group: 'nosql', versionRange: 'all' },
  { id: 'data-neo4j', name: 'Spring Data Neo4j', description: 'Neo4j 그래프 데이터베이스', group: 'nosql', versionRange: 'all' },

  /* ── Security ── */
  { id: 'security', name: 'Spring Security', description: '인증/인가 보안 프레임워크', group: 'security', versionRange: 'all' },
  { id: 'oauth2-client', name: 'OAuth2 Client', description: 'OAuth2 / OpenID Connect 클라이언트', group: 'security', versionRange: 'all' },
  { id: 'oauth2-resource-server', name: 'OAuth2 Resource Server', description: 'JWT/Opaque 토큰 기반 리소스 서버', group: 'security', versionRange: 'all' },
  { id: 'oauth2-authorization-server', name: 'OAuth2 Auth Server', description: 'OAuth2 인가 서버', group: 'security', versionRange: 'all' },

  /* ── Messaging ── */
  { id: 'kafka', name: 'Spring for Kafka', description: 'Apache Kafka 메시징', group: 'messaging', versionRange: 'all' },
  { id: 'amqp', name: 'Spring for RabbitMQ', description: 'AMQP / RabbitMQ 메시징', group: 'messaging', versionRange: 'all' },
  { id: 'pulsar', name: 'Spring for Pulsar', description: 'Apache Pulsar 메시징', group: 'messaging', versionRange: 'all' },
  { id: 'artemis', name: 'ActiveMQ Artemis', description: 'JMS 메시징 (ActiveMQ)', group: 'messaging', versionRange: 'all' },

  /* ── Observability ── */
  { id: 'actuator', name: 'Spring Boot Actuator', description: '운영 모니터링 & 관리 엔드포인트', group: 'ops', versionRange: 'all' },
  { id: 'prometheus', name: 'Prometheus Metrics', description: 'Micrometer Prometheus 레지스트리', group: 'ops', versionRange: 'all' },
  { id: 'distributed-tracing', name: 'Distributed Tracing', description: 'Micrometer Tracing (분산 추적)', group: 'ops', versionRange: 'all' },
  { id: 'opentelemetry', name: 'OpenTelemetry', description: 'OpenTelemetry 통합 (Boot 4.0+)', group: 'ops', versionRange: '>=4.0' },

  /* ── Cloud ── */
  { id: 'cloud-config-client', name: 'Config Client', description: 'Spring Cloud Config 클라이언트', group: 'cloud', versionRange: '>=3.5' },
  { id: 'cloud-eureka', name: 'Eureka Discovery', description: 'Netflix Eureka 서비스 디스커버리', group: 'cloud', versionRange: '>=3.5' },
  { id: 'cloud-gateway', name: 'Spring Cloud Gateway', description: 'API 게이트웨이', group: 'cloud', versionRange: '>=3.5' },
  { id: 'cloud-feign', name: 'OpenFeign', description: '선언적 REST 클라이언트', group: 'cloud', versionRange: '>=3.5' },
  { id: 'cloud-resilience4j', name: 'Resilience4J', description: '서킷 브레이커 & 내결함성', group: 'cloud', versionRange: '>=3.5' },

  /* ── DevTools ── */
  { id: 'devtools', name: 'Spring Boot DevTools', description: '빠른 재시작 & 라이브 리로드', group: 'devtools', versionRange: 'all' },
  { id: 'lombok', name: 'Lombok', description: '보일러플레이트 코드 제거', group: 'devtools', versionRange: 'all' },
  { id: 'docker-compose', name: 'Docker Compose Support', description: 'Docker Compose 개발 환경 통합', group: 'devtools', versionRange: 'all' },
  { id: 'testcontainers', name: 'Testcontainers', description: '통합 테스트용 Docker 컨테이너', group: 'devtools', versionRange: 'all' },
  { id: 'validation', name: 'Validation', description: 'Bean Validation (Hibernate Validator)', group: 'devtools', versionRange: 'all' },
  { id: 'cache', name: 'Spring Cache', description: '캐시 추상화', group: 'devtools', versionRange: 'all' },
  { id: 'batch', name: 'Spring Batch', description: '대용량 배치 처리', group: 'devtools', versionRange: 'all' },
  { id: 'quartz', name: 'Quartz Scheduler', description: '스케줄링 프레임워크', group: 'devtools', versionRange: 'all' },

  /* ── AI (Boot 3.5+) ── */
  { id: 'spring-ai-openai', name: 'Spring AI - OpenAI', description: 'OpenAI GPT 통합', group: 'ai', versionRange: '>=3.5' },
  { id: 'spring-ai-anthropic', name: 'Spring AI - Anthropic', description: 'Anthropic Claude 통합', group: 'ai', versionRange: '>=3.5' },
  { id: 'spring-ai-ollama', name: 'Spring AI - Ollama', description: 'Ollama 로컬 LLM 통합', group: 'ai', versionRange: '>=3.5' },
]

/**
 * 카테고리 탭 정의
 * - id: DEPENDENCIES의 group과 매칭
 * - label: 탭에 표시될 텍스트
 */
interface Category {
  id: string
  label: string
}

const CATEGORIES: Category[] = [
  { id: 'all', label: '전체' },
  { id: 'web', label: 'Web' },
  { id: 'sql', label: 'SQL' },
  { id: 'nosql', label: 'NoSQL' },
  { id: 'security', label: 'Security' },
  { id: 'messaging', label: 'Messaging' },
  { id: 'ops', label: 'Observability' },
  { id: 'cloud', label: 'Cloud' },
  { id: 'devtools', label: 'Dev & I/O' },
  { id: 'ai', label: 'AI' },
]

/* ================================================================
 *  ★ 유틸리티 함수
 * ================================================================ */

/**
 * 의존성이 특정 Boot 버전과 호환되는지 판단
 * versionRange 규칙:
 *   'all'    → 모든 버전
 *   '>=3.5'  → 3.5 이상
 *   '>=4.0'  → 4.0 이상
 *   '3.x'    → 3.x 계열
 *   '<=3.5'  → 3.5 이하
 */
function isCompatible(dep: Dependency, bootVersion: string): boolean {
  const range = dep.versionRange
  if (range === 'all') return true

  /* 버전 문자열에서 major.minor 추출 (예: '4.0.3' → 4.0, '3.5.x' → 3.5) */
  const match = bootVersion.match(/^(\d+)\.(\d+)/)
  if (!match) return false
  const ver = parseFloat(`${match[1]}.${match[2]}`)

  if (range.startsWith('>=')) {
    const min = parseFloat(range.slice(2))
    return ver >= min
  }
  if (range.startsWith('<=')) {
    const max = parseFloat(range.slice(2))
    return ver <= max
  }
  if (range.endsWith('.x')) {
    const major = parseInt(range.split('.')[0])
    return parseInt(match[1]) === major
  }
  return true
}

/**
 * 선택한 의존성을 Maven/Gradle 설정으로 변환
 * (실제 groupId/artifactId는 간략화 — 참고용)
 */
function generateConfig(selected: string[], format: 'maven' | 'gradle'): string {
  if (selected.length === 0) return format === 'maven'
    ? '<!-- 의존성을 선택하세요 -->'
    : '// 의존성을 선택하세요'

  /* ID → Spring Initializr starter 매핑 (주요 항목) */
  const starterMap: Record<string, string> = {
    'web': 'spring-boot-starter-web',
    'webflux': 'spring-boot-starter-webflux',
    'data-jpa': 'spring-boot-starter-data-jpa',
    'data-jdbc': 'spring-boot-starter-data-jdbc',
    'data-mongodb': 'spring-boot-starter-data-mongodb',
    'data-redis': 'spring-boot-starter-data-redis',
    'data-elasticsearch': 'spring-boot-starter-data-elasticsearch',
    'data-neo4j': 'spring-boot-starter-data-neo4j',
    'security': 'spring-boot-starter-security',
    'oauth2-client': 'spring-boot-starter-oauth2-client',
    'oauth2-resource-server': 'spring-boot-starter-oauth2-resource-server',
    'actuator': 'spring-boot-starter-actuator',
    'websocket': 'spring-boot-starter-websocket',
    'hateoas': 'spring-boot-starter-hateoas',
    'batch': 'spring-boot-starter-batch',
    'cache': 'spring-boot-starter-cache',
    'validation': 'spring-boot-starter-validation',
    'kafka': 'spring-boot-starter-kafka' ,
    'amqp': 'spring-boot-starter-amqp',
    'quartz': 'spring-boot-starter-quartz',
    'devtools': 'spring-boot-devtools',
    'docker-compose': 'spring-boot-docker-compose',
  }

  if (format === 'maven') {
    return selected.map(id => {
      const artifact = starterMap[id] || `spring-boot-starter-${id}`
      return `<dependency>\n    <groupId>org.springframework.boot</groupId>\n    <artifactId>${artifact}</artifactId>\n</dependency>`
    }).join('\n\n')
  }

  return selected.map(id => {
    const artifact = starterMap[id] || `spring-boot-starter-${id}`
    return `implementation 'org.springframework.boot:${artifact}'`
  }).join('\n')
}

/* ================================================================
 *  ★ 컴포넌트 본체
 * ================================================================ */

export default function SpringBootSelector() {
  /* ── 상태 관리 ── */
  const [selectedVersion, setSelectedVersion] = useState(SPRING_BOOT_VERSIONS[0].version)
  const [selectedCategory, setSelectedCategory] = useState('all')
  const [selectedDeps, setSelectedDeps] = useState<Set<string>>(new Set())
  const [buildTool, setBuildTool] = useState<'maven' | 'gradle'>('maven')
  const [searchQuery, setSearchQuery] = useState('')

  /* ── 현재 선택된 Boot 버전 객체 ── */
  const currentVersion = SPRING_BOOT_VERSIONS.find(v => v.version === selectedVersion)!

  /* ── 필터링된 의존성 목록 (카테고리 + 검색어 + 호환성) ── */
  const filteredDeps = useMemo(() => {
    return DEPENDENCIES.filter(dep => {
      /* 카테고리 필터 */
      if (selectedCategory !== 'all' && dep.group !== selectedCategory) return false
      /* 검색어 필터 */
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

  /* ── 선택된 의존성 중 현재 버전과 비호환인 것 ── */
  const incompatibleDeps = useMemo(() => {
    return [...selectedDeps].filter(id => {
      const dep = DEPENDENCIES.find(d => d.id === id)
      return dep && !isCompatible(dep, selectedVersion)
    })
  }, [selectedDeps, selectedVersion])

  return (
    <div className={styles.selector}>
      {/* ━━━ 헤더 ━━━ */}
      <div className={styles.selectorHeader}>
        <h2 className={styles.selectorTitle}>Spring Boot Dependency Selector</h2>
        <p className={styles.selectorDesc}>
          버전을 선택하고, 필요한 의존성을 체크하세요. 호환성과 빌드 설정을 자동으로 확인합니다.
        </p>
      </div>

      {/* ━━━ 버전 선택 영역 ━━━ */}
      <div className={styles.versionSection}>
        <h3 className={styles.subTitle}>Spring Boot Version</h3>
        <div className={styles.versionGrid}>
          {SPRING_BOOT_VERSIONS.map(v => (
            <button
              key={v.version}
              className={`${styles.versionCard} ${selectedVersion === v.version ? styles.versionActive : ''} ${v.status === 'eol' ? styles.versionEol : ''}`}
              onClick={() => setSelectedVersion(v.version)}
            >
              <div className={styles.versionTop}>
                <span className={styles.versionNumber}>{v.version}</span>
                {v.lts && <span className={styles.ltsBadge}>LTS</span>}
                {v.status === 'eol' && <span className={styles.eolBadge}>EOL</span>}
              </div>
              <div className={styles.versionMeta}>
                Spring {v.springVersion} · JDK {v.jdkMin}+
              </div>
            </button>
          ))}
        </div>
      </div>

      {/* ━━━ 선택된 버전 상세 정보 ━━━ */}
      <div className={styles.versionDetail}>
        <div className={styles.detailGrid}>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>Spring Framework</span>
            <span className={styles.detailValue}>{currentVersion.springVersion}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>JDK</span>
            <span className={styles.detailValue}>{currentVersion.jdkMin}+ (권장 {currentVersion.jdkRecommended})</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>Hibernate</span>
            <span className={styles.detailValue}>{currentVersion.hibernateVersion}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>Jackson</span>
            <span className={styles.detailValue}>{currentVersion.jacksonVersion}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>Tomcat</span>
            <span className={styles.detailValue}>{currentVersion.tomcatVersion}</span>
          </div>
          <div className={styles.detailItem}>
            <span className={styles.detailLabel}>Spring Security</span>
            <span className={styles.detailValue}>{currentVersion.securityVersion}</span>
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
                  className={styles.depCheckbox}
                />
                <div className={styles.depInfo}>
                  <div className={styles.depNameRow}>
                    <span className={styles.depName}>{dep.name}</span>
                    {!compatible && <span className={styles.incompatBadge}>비호환</span>}
                  </div>
                  <span className={styles.depDesc}>{dep.description}</span>
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

          {/* 비호환 경고 */}
          {incompatibleDeps.length > 0 && (
            <div className={styles.warning}>
              Boot {selectedVersion}과 호환되지 않는 의존성: {' '}
              {incompatibleDeps.map(id => DEPENDENCIES.find(d => d.id === id)?.name).join(', ')}
            </div>
          )}

          {/* 선택된 의존성 태그 */}
          <div className={styles.selectedTags}>
            {[...selectedDeps].map(id => {
              const dep = DEPENDENCIES.find(d => d.id === id)
              const compat = dep ? isCompatible(dep, selectedVersion) : true
              return (
                <span
                  key={id}
                  className={`${styles.selectedTag} ${!compat ? styles.selectedTagWarn : ''}`}
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
