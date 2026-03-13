package com.jkypch.web.init;

import com.jkypch.web.domain.Post;
import com.jkypch.web.repository.mongo.PostRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
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
            "nldd-fifth-generation",
            "자연어는 제5세대 개발 언어다 — 자연어 주도 개발",
            "기계어부터 자연어까지 추상화의 흐름. " +
            "LLM이 코드 작성마저 자동화한 지금, 개발자의 무기는 '의도를 정교하게 말하는 능력'이다.",
            NLDD_CONTENT,
            "2026-03-12",
            Instant.parse("2026-03-12T09:00:00Z"),
            List.of("Programming Paradigm", "LLM", "NLDD", "Natural Language", "AI", "Software Engineering")
        ));

        postRepository.save(new Post(
            "blog-architecture",
            "이 블로그의 아키텍처 — 서버 없이 맥북 한 대로 운영하기",
            "별도 서버 없이 로컬 맥에서 Docker Compose 하나로 이 블로그를 운영하는 방법. " +
            "Cloudflare Tunnel로 외부 노출, Jenkins Blue-Green 배포, Prometheus + Grafana 모니터링까지 전체 구조를 설명한다.",
            BLOG_ARCHITECTURE_CONTENT,
            "2026-03-11",
            Instant.parse("2026-03-11T09:00:00Z"),
            List.of("Architecture", "Docker", "Cloudflare", "Jenkins", "Blue-Green", "Spring Boot", "DevOps")
        ));

        postRepository.save(new Post(
            "spring-boot-version-guide",
            "Spring Framework 버전 선택 가이드 — 의존성 호환성과 LTS 전략",
            "Spring Framework를 기준으로 Boot, 웹 서버, ORM 등 모든 의존성의 호환성을 정리. " +
            "위의 인터랙티브 도구로 버전별 선택 가능한 조합을 직접 확인할 수 있다.",
            SPRING_GUIDE_CONTENT,
            "2026-03-10",
            Instant.parse("2026-03-10T03:00:00Z"),
            List.of("Spring Framework", "Spring Boot", "Java", "LTS", "Dependencies")
        ));

        postRepository.save(new Post(
            "java-web-architecture-evolution",
            "Java 웹 개발 아키텍처의 변천사 — Servlet부터 Spring Boot까지",
            "Servlet/JSP 시대의 구조적 한계, Spring IoC/AOP가 해결한 것, Spring MVC 등장, 그리고 Spring Boot까지 " +
            "\"문제 → 해결 → 발전\" 구조로 Java 웹 아키텍처의 진화를 설명한다.",
            JAVA_WEB_EVOLUTION_CONTENT,
            "2026-03-10",
            Instant.parse("2026-03-10T02:00:00Z"),
            List.of("Java", "Spring Framework", "Spring Boot", "Servlet", "Architecture", "Backend")
        ));

        postRepository.save(new Post(
            "prometheus-grafana-monitoring",
            "Prometheus + Grafana 모니터링 스택 구축 - Exporter란 무엇인가",
            "nginx, MongoDB, Redis를 Docker Compose로 운영할 때 Prometheus로 메트릭을 수집하고 Grafana로 시각화하는 과정. " +
            "Exporter가 왜 필요한지, 각 서비스가 Prometheus 포맷을 직접 제공하지 못하는 이유부터 실제 구성까지 다룬다.",
            CONTENT,
            "2026-03-06",
            Instant.parse("2026-03-06T09:00:00Z"),
            List.of("Docker", "Prometheus", "Grafana", "nginx", "MongoDB", "Redis", "Monitoring")
        ));
    }

    private static final String JAVA_WEB_EVOLUTION_CONTENT = """
# Java 웹 개발 아키텍처의 변천사

## 들어가며

레거시 시스템을 처음 마주했을 때 느끼는 당혹감은 대부분 "왜 이런 구조인가?"에서 온다. 현재의 Spring Boot 구조만 알고 있으면 이전 코드가 이상하게 보인다. 반대로 과거의 문제를 알면 현재의 설계가 왜 그렇게 됐는지 이해된다.

Servlet과 JSP만 존재하던 시절의 구조적 한계에서 출발해, Spring이 그 문제를 어떻게 해결했는지, 그리고 Spring MVC와 Spring Boot로 어떻게 발전했는지 순서대로 짚어본다.

---

## 1단계: Servlet + JSP + JDBC (2000년대 초반)

### 당시 아키텍처

<svg viewBox="0 0 720 420" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="s1-ar" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
  </defs>
  <rect width="720" height="420" rx="8" fill="#161616"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">Servlet / JSP 시대 아키텍처</text>

  <!-- Browser top -->
  <rect x="270" y="28" width="180" height="46" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="360" y="48" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="13" font-weight="600">Browser</text>
  <text x="360" y="65" text-anchor="middle" fill="#666" font-family="monospace" font-size="11">HTTP Request</text>

  <line x1="360" y1="74" x2="360" y2="94" stroke="#555" stroke-width="1.5" marker-end="url(#s1-ar)"/>

  <!-- Servlet Container dashed border -->
  <rect x="40" y="96" width="520" height="190" rx="8" fill="#0d1a0d" stroke="#2e5c2e" stroke-width="1.5" stroke-dasharray="6,3"/>
  <text x="60" y="113" fill="#3d7a3d" font-family="monospace" font-size="11">Servlet Container (Tomcat)</text>

  <!-- Servlet box — highlighted as problem -->
  <rect x="60" y="120" width="370" height="116" rx="6" fill="#2a1a0a" stroke="#f26207" stroke-width="1.5"/>
  <text x="245" y="143" text-anchor="middle" fill="#f26207" font-family="monospace" font-size="13" font-weight="600">HttpServlet</text>
  <text x="245" y="164" text-anchor="middle" fill="#999" font-family="monospace" font-size="11">① 요청 파싱</text>
  <text x="245" y="182" text-anchor="middle" fill="#999" font-family="monospace" font-size="11">② 비즈니스 로직</text>
  <text x="245" y="200" text-anchor="middle" fill="#999" font-family="monospace" font-size="11">③ JDBC / DB 접근</text>
  <text x="245" y="218" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">모든 책임이 하나의 클래스에 혼재</text>

  <!-- Arrow to DB -->
  <line x1="430" y1="178" x2="480" y2="178" stroke="#555" stroke-width="1.5" marker-end="url(#s1-ar)"/>
  <text x="455" y="170" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">JDBC</text>

  <!-- DB box -->
  <rect x="480" y="148" width="120" height="60" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="540" y="174" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="13">Database</text>
  <text x="540" y="192" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">매 요청마다 new Connection</text>

  <!-- Arrow Servlet → JSP -->
  <line x1="245" y1="236" x2="245" y2="260" stroke="#555" stroke-width="1.5" marker-end="url(#s1-ar)"/>
  <text x="262" y="252" fill="#555" font-family="monospace" font-size="10">forward</text>

  <!-- JSP box -->
  <rect x="80" y="262" width="330" height="56" rx="6" fill="#221e0a" stroke="#7a6a2a" stroke-width="1.5"/>
  <text x="245" y="284" text-anchor="middle" fill="#c8b040" font-family="monospace" font-size="13" font-weight="600">JSP</text>
  <text x="245" y="302" text-anchor="middle" fill="#888" font-family="monospace" font-size="11">HTML 마크업 + Java 코드 혼재</text>

  <!-- Arrow JSP → Browser bottom -->
  <line x1="245" y1="318" x2="245" y2="352" stroke="#555" stroke-width="1.5" marker-end="url(#s1-ar)"/>
  <text x="262" y="340" fill="#555" font-family="monospace" font-size="10">HTML Response</text>

  <!-- Browser bottom -->
  <rect x="155" y="354" width="180" height="46" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="245" y="374" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="13" font-weight="600">Browser</text>
  <text x="245" y="391" text-anchor="middle" fill="#666" font-family="monospace" font-size="11">화면 표시</text>
</svg>

### 당시 코드

```xml
<!-- web.xml — Servlet 등록 (당시 유일한 방법) -->
<servlet>
    <servlet-name>orderServlet</servlet-name>
    <servlet-class>com.example.OrderServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>orderServlet</servlet-name>
    <url-pattern>/order</url-pattern>
</servlet-mapping>
```

```java
// 주문 처리 Servlet — 모든 것이 하나의 클래스에
public class OrderServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) 요청 파라미터 파싱
        String userId = req.getParameter("userId");
        int itemId = Integer.parseInt(req.getParameter("itemId"));

        // 2) DB 연결 — 매 요청마다 직접 생성
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost/shop", "root", "password");

            // 3) 비즈니스 로직 (재고 확인, 주문 생성 등)
            PreparedStatement ps = conn.prepareStatement(
                "SELECT stock FROM items WHERE id = ?");
            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt("stock") > 0) {
                PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO orders (user_id, item_id) VALUES (?, ?)");
                ins.setString(1, userId);
                ins.setInt(2, itemId);
                ins.executeUpdate();
            }

            // 4) JSP로 포워드
            req.setAttribute("message", "주문 완료");
            req.getRequestDispatcher("/order-result.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
}
```

```jsp
<%-- order-result.jsp — 비즈니스 로직이 뒤섞인 뷰 --%>
<%
    // JSP 안에서 또 DB 조회
    Connection conn = DriverManager.getConnection(
        "jdbc:mysql://localhost/shop", "root", "password");
    PreparedStatement ps = conn.prepareStatement(
        "SELECT * FROM orders WHERE user_id = ?");
    ps.setString(1, request.getParameter("userId"));
    ResultSet rs = ps.executeQuery();
%>
<html>
<body>
  <h1>주문 내역</h1>
  <% while (rs.next()) { %>
    <p><%= rs.getString("item_name") %></p>
  <% } %>
</body>
</html>
```

### 구조적 문제점

**강한 결합(Tight Coupling)**
- 요청 파싱 / 비즈니스 로직 / DB 접근 / 뷰 렌더링이 하나의 클래스(또는 JSP)에 뒤섞임
- 기능 하나를 수정하면 연쇄 변경 발생

**트랜잭션 관리 불가**
- DB 연결을 매 요청마다 `new`로 생성 → 성능 문제
- 트랜잭션 커밋/롤백을 개발자가 일일이 try-finally로 처리
- 여러 DB 작업을 하나의 트랜잭션으로 묶기가 매우 어려움

**테스트 불가능**
- `HttpServletRequest`, `HttpServletResponse`에 직접 의존
- 비즈니스 로직을 단위 테스트하려면 웹 컨테이너 전체를 올려야 함

**중복 코드**
- DB 연결, 예외 처리, JSP 포워드 코드가 모든 Servlet에 반복

---

## 2단계: Spring Framework 등장 (2003–2004)

당시 Sun Microsystems가 제시한 "정석"은 **EJB(Enterprise JavaBeans)**였다. 트랜잭션 관리, 분산 객체, 보안 등 엔터프라이즈에 필요한 기능을 컨테이너가 제공하되, 그 대가로 복잡한 인터페이스 구현과 XML 배포 서술자를 요구했다. 작은 비즈니스 로직 하나를 위해 Home 인터페이스, Remote 인터페이스, 배포 서술자를 작성해야 하는 구조는 과도한 무게였다.

Rod Johnson이 저서 *Expert One-on-One J2EE Design and Development*에서 제시한 핵심 주장은 단순했다: **"EJB 없이도 엔터프라이즈 애플리케이션을 만들 수 있다."**

### 해결 방법: IoC 컨테이너

문제의 근원은 객체가 자신의 의존성을 직접 생성한다는 것이었다.

```java
// Before: 강한 결합
class OrderService {
    private OrderRepository repo = new OrderRepositoryImpl(); // 직접 생성
    private PaymentService pay = new PaymentServiceImpl();    // 직접 생성
}

// After: DI — 외부에서 주입
class OrderService {
    private final OrderRepository repo;
    private final PaymentService pay;

    // 생성자를 통해 주입받음 — 어떤 구현체인지 모름
    public OrderService(OrderRepository repo, PaymentService pay) {
        this.repo = repo;
        this.pay = pay;
    }
}
```

Spring IoC 컨테이너(ApplicationContext)가 객체의 생성과 연결을 담당한다. 개발자는 어떤 구현체를 쓸지 XML(또는 이후 Java Config)로 선언만 하면 된다.

### 해결 방법: AOP (Aspect-Oriented Programming)

트랜잭션, 로깅, 보안 같은 **횡단 관심사**를 비즈니스 코드에서 분리한다.

```java
// Before: 트랜잭션 코드가 비즈니스 코드를 오염
public void placeOrder(Order order) {
    Connection conn = dataSource.getConnection();
    conn.setAutoCommit(false);
    try {
        orderRepo.save(order);           // 실제 비즈니스
        inventoryRepo.reduce(order);     // 실제 비즈니스
        conn.commit();
    } catch (Exception e) {
        conn.rollback();
        throw e;
    } finally {
        conn.close();
    }
}

// After: @Transactional — AOP 프록시가 처리
@Transactional
public void placeOrder(Order order) {
    orderRepo.save(order);       // 비즈니스만
    inventoryRepo.reduce(order); // 비즈니스만
}
```

`@Transactional`이 붙은 메서드는 AOP 프록시로 감싸지고, 트랜잭션 시작/커밋/롤백은 프레임워크가 자동 처리한다.

### 초기 Spring 아키텍처

<svg viewBox="0 0 720 330" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="s2-ar" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
    <marker id="s2-di" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#3d8a3d"/></marker>
    <marker id="s2-aop" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#5a3a8a"/></marker>
  </defs>
  <rect width="720" height="330" rx="8" fill="#161616"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">초기 Spring IoC/AOP 아키텍처</text>
  <rect x="40" y="28" width="220" height="50" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="150" y="49" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12" font-weight="600">applicationContext.xml</text>
  <text x="150" y="67" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">bean 선언 — 구현체 명시</text>
  <line x1="260" y1="53" x2="310" y2="53" stroke="#555" stroke-width="1.5" marker-end="url(#s2-ar)"/>
  <rect x="310" y="28" width="380" height="276" rx="8" fill="#0a1a0a" stroke="#2e5c2e" stroke-width="1.5"/>
  <text x="500" y="50" text-anchor="middle" fill="#4a8a4a" font-family="monospace" font-size="12" font-weight="600">Spring IoC Container (ApplicationContext)</text>
  <rect x="330" y="68" width="160" height="44" rx="6" fill="#252525" stroke="#3a5a3a" stroke-width="1.5"/>
  <text x="410" y="87" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">OrderService</text>
  <text x="410" y="103" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">bean</text>
  <line x1="410" y1="112" x2="410" y2="136" stroke="#3d8a3d" stroke-width="1.5" marker-end="url(#s2-di)"/>
  <text x="424" y="130" fill="#3d6a3d" font-family="monospace" font-size="10">DI 주입</text>
  <rect x="330" y="138" width="160" height="44" rx="6" fill="#252525" stroke="#3a5a3a" stroke-width="1.5"/>
  <text x="410" y="157" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">OrderRepository</text>
  <text x="410" y="173" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">bean (JdbcTemplate)</text>
  <line x1="490" y1="160" x2="530" y2="160" stroke="#555" stroke-width="1.5" marker-end="url(#s2-ar)"/>
  <rect x="530" y="138" width="130" height="44" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="595" y="162" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">Database</text>
  <rect x="320" y="210" width="190" height="70" rx="6" fill="#1a1028" stroke="#5a3a8a" stroke-width="1.5"/>
  <text x="415" y="232" text-anchor="middle" fill="#a080d0" font-family="monospace" font-size="12">AOP Proxy</text>
  <text x="415" y="250" text-anchor="middle" fill="#7060a0" font-family="monospace" font-size="10">@Transactional</text>
  <text x="415" y="266" text-anchor="middle" fill="#504070" font-family="monospace" font-size="10">트랜잭션 시작/커밋/롤백 자동</text>
  <path d="M 320 245 C 280 245, 280 90, 330 90" fill="none" stroke="#5a3a8a" stroke-width="1.5" stroke-dasharray="4,3" marker-end="url(#s2-aop)"/>
  <text x="262" y="175" fill="#5a3a8a" font-family="monospace" font-size="10">wrap</text>
</svg>

이 시점의 Spring은 여전히 Servlet/JSP와 함께 사용됐다. MVC 레이어는 별도였다.

---

## 3단계: Spring MVC 등장 (Spring 2.0–2.5, 2006–2007)

### 해결한 문제: MVC 패턴의 체계화

Servlet 하나가 모든 것을 처리하는 대신, 진입점을 **단일 Servlet(DispatcherServlet)**으로 통일하고 역할을 분리했다.

<svg viewBox="0 0 720 460" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="s3-ar" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
    <marker id="s3-bl" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#4a6a9a"/></marker>
  </defs>
  <rect width="720" height="460" rx="8" fill="#161616"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">Spring MVC 아키텍처 — Front Controller 패턴</text>

  <!-- Browser -->
  <rect x="20" y="136" width="120" height="44" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="80" y="156" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12" font-weight="600">Browser</text>
  <text x="80" y="172" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">Request</text>

  <!-- Arrow Browser → DispatcherServlet -->
  <line x1="140" y1="158" x2="186" y2="158" stroke="#555" stroke-width="1.5" marker-end="url(#s3-ar)"/>

  <!-- DispatcherServlet -->
  <rect x="188" y="114" width="180" height="88" rx="6" fill="#0f1a2a" stroke="#4a7aaa" stroke-width="2"/>
  <text x="278" y="138" text-anchor="middle" fill="#6aaaee" font-family="monospace" font-size="12" font-weight="600">DispatcherServlet</text>
  <text x="278" y="156" text-anchor="middle" fill="#4a6a8a" font-family="monospace" font-size="10">단일 진입점 (Front Controller)</text>
  <text x="278" y="172" text-anchor="middle" fill="#4a6a8a" font-family="monospace" font-size="10">web.xml 에 "/" 매핑</text>
  <text x="278" y="188" text-anchor="middle" fill="#3a5a7a" font-family="monospace" font-size="10">요청 위임 · 응답 조합</text>

  <!-- HandlerMapping (top-right) -->
  <rect x="440" y="36" width="200" height="50" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="540" y="58" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">HandlerMapping</text>
  <text x="540" y="75" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">URL → @Controller 매핑</text>
  <line x1="368" y1="134" x2="440" y2="68" stroke="#4a6a9a" stroke-width="1.5" stroke-dasharray="4,3" marker-end="url(#s3-bl)"/>

  <!-- Controller -->
  <rect x="440" y="114" width="200" height="50" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="540" y="136" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">@Controller</text>
  <text x="540" y="153" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">요청 처리 · Service 호출</text>
  <line x1="368" y1="158" x2="440" y2="139" stroke="#4a6a9a" stroke-width="1.5" marker-end="url(#s3-bl)"/>

  <!-- Service (right, below Controller) -->
  <rect x="470" y="196" width="140" height="44" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="540" y="215" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">@Service</text>
  <text x="540" y="231" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">비즈니스 로직</text>
  <line x1="540" y1="164" x2="540" y2="196" stroke="#555" stroke-width="1.5" marker-end="url(#s3-ar)"/>

  <!-- Repository -->
  <rect x="470" y="274" width="140" height="44" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="540" y="293" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">@Repository</text>
  <text x="540" y="309" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">DB 접근</text>
  <line x1="540" y1="240" x2="540" y2="274" stroke="#555" stroke-width="1.5" marker-end="url(#s3-ar)"/>

  <!-- ModelAndView (below DispatcherServlet) -->
  <rect x="188" y="226" width="180" height="50" rx="6" fill="#1e1e10" stroke="#5a5a2a" stroke-width="1.5"/>
  <text x="278" y="248" text-anchor="middle" fill="#aaaa60" font-family="monospace" font-size="12">ModelAndView</text>
  <text x="278" y="265" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">데이터 + 뷰 이름 반환</text>
  <line x1="278" y1="202" x2="278" y2="226" stroke="#555" stroke-width="1.5" marker-end="url(#s3-ar)"/>

  <!-- ViewResolver (below ModelAndView, left side) -->
  <rect x="188" y="306" width="180" height="50" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="278" y="328" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">ViewResolver</text>
  <text x="278" y="345" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">뷰 이름 → JSP 경로 변환</text>
  <line x1="278" y1="276" x2="278" y2="306" stroke="#555" stroke-width="1.5" marker-end="url(#s3-ar)"/>

  <!-- JSP (below ViewResolver) -->
  <rect x="188" y="386" width="180" height="50" rx="6" fill="#221e0a" stroke="#7a6a2a" stroke-width="1.5"/>
  <text x="278" y="408" text-anchor="middle" fill="#c8b040" font-family="monospace" font-size="12" font-weight="600">JSP</text>
  <text x="278" y="425" text-anchor="middle" fill="#888" font-family="monospace" font-size="10">HTML 렌더링</text>
  <line x1="278" y1="356" x2="278" y2="386" stroke="#555" stroke-width="1.5" marker-end="url(#s3-ar)"/>

  <!-- Response back to Browser -->
  <line x1="188" y1="411" x2="80" y2="411" stroke="#3d7a3d" stroke-width="1.5"/>
  <line x1="80" y1="411" x2="80" y2="180" stroke="#3d7a3d" stroke-width="1.5" marker-end="url(#s3-ar)"/>
  <text x="134" y="404" text-anchor="middle" fill="#3d6a3d" font-family="monospace" font-size="9">HTML Response</text>
</svg>

### 당시 코드

```java
// Spring MVC Controller (2.x 시대 — 인터페이스 구현 방식)
public class OrderController implements Controller {

    private OrderService orderService;

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService; // XML에서 DI
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest req,
                                      HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        Order order = orderService.createOrder(userId);
        return new ModelAndView("orderResult", "order", order);
    }
}
```

```xml
<!-- applicationContext.xml -->
<bean id="orderController" class="com.example.OrderController">
    <property name="orderService" ref="orderService"/>
</bean>

<bean id="orderService" class="com.example.OrderServiceImpl">
    <property name="orderRepository" ref="orderRepository"/>
</bean>

<bean id="orderRepository" class="com.example.JdbcOrderRepository">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

### Spring 2.5 — 어노테이션 기반 전환

```java
// 인터페이스 강제 없음, 훨씬 간결
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String placeOrder(@RequestParam String userId, Model model) {
        Order order = orderService.createOrder(userId);
        model.addAttribute("order", order);
        return "orderResult"; // ViewResolver가 /WEB-INF/views/orderResult.jsp 찾음
    }
}
```

역할이 명확히 분리됐다: Controller는 요청만, Service는 로직만, Repository는 데이터만.

---

## 4단계: Spring 3.x–4.x — 현대 구조 완성 (2009–2016)

### Java Config — XML 탈출

```java
// XML 설정을 Java 코드로 대체
@Configuration
@ComponentScan("com.example")
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl("jdbc:mysql://localhost/shop");
        ds.setUsername("root");
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
```

### REST API 중심 전환

```java
// Spring 4.0+ — @RestController로 JSON 직접 반환
@RestController // @Controller + @ResponseBody (Spring 4.0에서 도입)
@RequestMapping("/api/orders")
public class OrderApi {

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderRequest req) {
        Order order = orderService.createOrder(req);
        return ResponseEntity.created(URI.create("/api/orders/" + order.getId()))
                             .body(OrderDto.from(order));
    }

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable Long id) {
        return OrderDto.from(orderService.findById(id));
    }
}
```

뷰(JSP)가 없어지고 JSON을 직접 반환한다. 프론트엔드와 백엔드가 분리된다.

<svg viewBox="0 0 720 300" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="s3b-ar" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
    <marker id="s3b-gn" markerWidth="8" markerHeight="6" refX="1" refY="3" orient="auto"><polygon points="8 0,0 3,8 6" fill="#3d7a3d"/></marker>
  </defs>
  <rect width="720" height="300" rx="8" fill="#161616"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">Spring 3.x–4.x — REST API 중심 전환</text>

  <!-- Frontend (SPA) -->
  <rect x="20" y="80" width="130" height="60" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="85" y="103" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12" font-weight="600">Frontend</text>
  <text x="85" y="120" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">SPA / 모바일 앱</text>
  <text x="85" y="134" text-anchor="middle" fill="#555" font-family="monospace" font-size="9">별도 프로젝트</text>

  <!-- Arrow Frontend → RestController -->
  <line x1="150" y1="100" x2="206" y2="100" stroke="#555" stroke-width="1.5" marker-end="url(#s3b-ar)"/>
  <text x="178" y="93" text-anchor="middle" fill="#555" font-family="monospace" font-size="9">JSON</text>

  <!-- @RestController -->
  <rect x="208" y="72" width="160" height="56" rx="6" fill="#0f1a2a" stroke="#4a7aaa" stroke-width="2"/>
  <text x="288" y="95" text-anchor="middle" fill="#6aaaee" font-family="monospace" font-size="12" font-weight="600">@RestController</text>
  <text x="288" y="115" text-anchor="middle" fill="#4a6a8a" font-family="monospace" font-size="10">@ResponseBody 내장</text>

  <!-- Arrow → Service -->
  <line x1="368" y1="100" x2="414" y2="100" stroke="#555" stroke-width="1.5" marker-end="url(#s3b-ar)"/>

  <!-- @Service -->
  <rect x="416" y="78" width="130" height="44" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="481" y="98" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">@Service</text>
  <text x="481" y="114" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">비즈니스 로직</text>

  <!-- Arrow → Repository -->
  <line x1="546" y1="100" x2="586" y2="100" stroke="#555" stroke-width="1.5" marker-end="url(#s3b-ar)"/>

  <!-- @Repository -->
  <rect x="588" y="78" width="110" height="44" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="643" y="98" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12">@Repository</text>
  <text x="643" y="114" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">DB 접근</text>

  <!-- JSON Response arrow back -->
  <line x1="288" y1="128" x2="288" y2="148" stroke="#3d7a3d" stroke-width="1.5"/>
  <line x1="288" y1="148" x2="85" y2="148" stroke="#3d7a3d" stroke-width="1.5" marker-end="url(#s3b-gn)"/>
  <text x="186" y="164" text-anchor="middle" fill="#3d6a3d" font-family="monospace" font-size="10">JSON (Jackson 직렬화)</text>

  <!-- 변화 비교 -->
  <rect x="40" y="196" width="290" height="80" rx="6" fill="#1a0a0a" stroke="#5a2a2a" stroke-width="1.5" stroke-dasharray="5,3"/>
  <text x="185" y="216" text-anchor="middle" fill="#aa5050" font-family="monospace" font-size="11" font-weight="600">Before (MVC)</text>
  <text x="185" y="236" text-anchor="middle" fill="#885050" font-family="monospace" font-size="10">Controller → ModelAndView → JSP 렌더링</text>
  <text x="185" y="256" text-anchor="middle" fill="#664040" font-family="monospace" font-size="10">서버가 HTML을 직접 렌더링</text>

  <rect x="390" y="196" width="290" height="80" rx="6" fill="#0a1a0a" stroke="#2e5c2e" stroke-width="1.5" stroke-dasharray="5,3"/>
  <text x="535" y="216" text-anchor="middle" fill="#4a8a4a" font-family="monospace" font-size="11" font-weight="600">After (REST)</text>
  <text x="535" y="236" text-anchor="middle" fill="#3a6a3a" font-family="monospace" font-size="10">@RestController → JSON 직접 반환</text>
  <text x="535" y="256" text-anchor="middle" fill="#2a4a2a" font-family="monospace" font-size="10">ViewResolver/JSP 불필요, 프론트 분리</text>
</svg>

### 이 시점의 여전한 문제

설정 파일이 줄었지만 여전히 많다. 새 프로젝트를 시작하려면:

- `web.xml` — DispatcherServlet 등록
- `Spring MVC 설정` — ViewResolver, MessageConverter 등
- `DataSource 설정` — 커넥션 풀
- `Transaction 설정` — @EnableTransactionManagement
- `Security 설정` — 별도 XML
- Maven/Gradle 의존성 버전 조합 — 직접 검증

기능 개발 전에 환경 구성에만 수 시간이 걸렸다.

---

## 5단계: Spring Boot — 설정보다 코드 (2014–)

### 핵심: "Convention over Configuration"

Spring Boot는 새로운 기술이 아니다. **Spring Framework를 "쓸 수 있는 상태"로 미리 구성해둔 것**이다.

```java
// 이게 전부다 — main 메서드 하나로 웹 서버가 뜬다
@SpringBootApplication
public class ShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}
```

```yaml
# application.yml — 나머지는 자동 설정
spring:
  datasource:
    url: jdbc:mysql://localhost/shop
    username: root
    password: secret
```

```xml
<!-- pom.xml — 버전 조합 걱정 없이 starter 하나 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### Auto-configuration 동작 원리

<svg viewBox="0 0 720 340" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="s4a-ar" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
    <marker id="s4a-gr" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#3d8a3d"/></marker>
  </defs>
  <rect width="720" height="340" rx="8" fill="#161616"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">Spring Boot — Auto-configuration 동작 원리</text>

  <!-- @SpringBootApplication -->
  <rect x="230" y="28" width="260" height="44" rx="6" fill="#1a0f28" stroke="#7a4aaa" stroke-width="2"/>
  <text x="360" y="47" text-anchor="middle" fill="#b080e0" font-family="monospace" font-size="13" font-weight="600">@SpringBootApplication</text>
  <text x="360" y="63" text-anchor="middle" fill="#7050a0" font-family="monospace" font-size="10">@EnableAutoConfiguration 포함</text>

  <line x1="360" y1="72" x2="360" y2="92" stroke="#7a4aaa" stroke-width="1.5" marker-end="url(#s4a-ar)"/>

  <!-- Auto-configuration imports -->
  <rect x="100" y="94" width="520" height="44" rx="6" fill="#1a1a0a" stroke="#6a6a2a" stroke-width="1.5"/>
  <text x="360" y="114" text-anchor="middle" fill="#aaaa50" font-family="monospace" font-size="12">AutoConfiguration.imports</text>
  <text x="360" y="130" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">조건 충족 시(@ConditionalOnClass / @ConditionalOnMissingBean)에만 활성화</text>

  <!-- Three auto-config boxes -->
  <line x1="220" y1="138" x2="180" y2="168" stroke="#555" stroke-width="1.5" marker-end="url(#s4a-ar)"/>
  <line x1="360" y1="138" x2="360" y2="168" stroke="#555" stroke-width="1.5" marker-end="url(#s4a-ar)"/>
  <line x1="500" y1="138" x2="540" y2="168" stroke="#555" stroke-width="1.5" marker-end="url(#s4a-ar)"/>

  <rect x="60" y="170" width="210" height="50" rx="6" fill="#252525" stroke="#3a5a3a" stroke-width="1.5"/>
  <text x="165" y="191" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="11">DataSourceAutoConfiguration</text>
  <text x="165" y="207" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">DataSource 빈 자동 생성</text>

  <rect x="290" y="170" width="150" height="50" rx="6" fill="#252525" stroke="#3a5a3a" stroke-width="1.5"/>
  <text x="365" y="191" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="11">JpaAutoConfiguration</text>
  <text x="365" y="207" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">EntityManager, Transaction</text>

  <rect x="460" y="170" width="210" height="50" rx="6" fill="#252525" stroke="#3a5a3a" stroke-width="1.5"/>
  <text x="565" y="191" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="11">DispatcherServletAutoConfig</text>
  <text x="565" y="207" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">DispatcherServlet 자동 등록</text>

  <!-- Override hint -->
  <rect x="160" y="256" width="400" height="60" rx="6" fill="#0a1a0a" stroke="#2e5c2e" stroke-width="1.5" stroke-dasharray="5,3"/>
  <text x="360" y="279" text-anchor="middle" fill="#4a8a4a" font-family="monospace" font-size="12">개발자 직접 정의 Bean</text>
  <text x="360" y="297" text-anchor="middle" fill="#3a6a3a" font-family="monospace" font-size="10">@ConditionalOnMissingBean — 자동 설정이 비켜감</text>
  <text x="360" y="313" text-anchor="middle" fill="#2a4a2a" font-family="monospace" font-size="10">필요한 부분만 오버라이드 가능</text>
</svg>

`@ConditionalOnMissingBean` 덕분에 개발자가 직접 빈을 정의하면 자동 설정이 비켜간다. 자동 설정을 사용하면서 필요한 부분만 오버라이드하는 방식이다.

### 현재 Spring Boot 아키텍처

<svg viewBox="0 0 720 430" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="s4b-ar" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
    <marker id="s4b-bl" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#4a7aaa"/></marker>
    <marker id="s4b-gn" markerWidth="8" markerHeight="6" refX="1" refY="3" orient="auto"><polygon points="8 0,0 3,8 6" fill="#3d7a3d"/></marker>
  </defs>
  <rect width="720" height="430" rx="8" fill="#161616"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">Spring Boot — HTTP 요청 처리 흐름</text>

  <!-- Browser -->
  <rect x="20" y="186" width="100" height="44" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="70" y="206" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12" font-weight="600">Browser</text>
  <text x="70" y="222" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">Request</text>
  <line x1="120" y1="208" x2="150" y2="208" stroke="#555" stroke-width="1.5" marker-end="url(#s4b-ar)"/>

  <!-- Embedded Tomcat -->
  <rect x="152" y="168" width="110" height="80" rx="6" fill="#1a1e28" stroke="#4a5a7a" stroke-width="1.5"/>
  <text x="207" y="194" text-anchor="middle" fill="#8090b0" font-family="monospace" font-size="11" font-weight="600">Embedded</text>
  <text x="207" y="210" text-anchor="middle" fill="#8090b0" font-family="monospace" font-size="11" font-weight="600">Tomcat</text>
  <text x="207" y="228" text-anchor="middle" fill="#505870" font-family="monospace" font-size="10">내장 서버</text>
  <line x1="262" y1="208" x2="286" y2="208" stroke="#555" stroke-width="1.5" marker-end="url(#s4b-ar)"/>

  <!-- Filter Chain -->
  <rect x="288" y="168" width="120" height="80" rx="6" fill="#221828" stroke="#6a3a8a" stroke-width="1.5"/>
  <text x="348" y="194" text-anchor="middle" fill="#9060b0" font-family="monospace" font-size="11" font-weight="600">Filter Chain</text>
  <text x="348" y="212" text-anchor="middle" fill="#604880" font-family="monospace" font-size="10">Security</text>
  <text x="348" y="228" text-anchor="middle" fill="#604880" font-family="monospace" font-size="10">CORS / Logging</text>
  <line x1="408" y1="208" x2="432" y2="208" stroke="#555" stroke-width="1.5" marker-end="url(#s4b-ar)"/>

  <!-- DispatcherServlet -->
  <rect x="434" y="148" width="140" height="120" rx="6" fill="#0f1a2a" stroke="#4a7aaa" stroke-width="2"/>
  <text x="504" y="175" text-anchor="middle" fill="#6aaaee" font-family="monospace" font-size="12" font-weight="600">Dispatcher</text>
  <text x="504" y="191" text-anchor="middle" fill="#6aaaee" font-family="monospace" font-size="12" font-weight="600">Servlet</text>
  <text x="504" y="212" text-anchor="middle" fill="#4a6a8a" font-family="monospace" font-size="10">HandlerMapping</text>
  <text x="504" y="228" text-anchor="middle" fill="#4a6a8a" font-family="monospace" font-size="10">HandlerAdapter</text>
  <text x="504" y="244" text-anchor="middle" fill="#4a6a8a" font-family="monospace" font-size="10">MessageConverter</text>

  <!-- Arrow DispatcherServlet → Controller -->
  <line x1="574" y1="208" x2="610" y2="208" stroke="#4a7aaa" stroke-width="1.5" marker-end="url(#s4b-bl)"/>

  <!-- @RestController -->
  <rect x="612" y="148" width="90" height="52" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="657" y="170" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="11" font-weight="600">@Rest</text>
  <text x="657" y="186" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="11" font-weight="600">Controller</text>

  <!-- @Transactional AOP -->
  <rect x="612" y="218" width="90" height="50" rx="6" fill="#1a1028" stroke="#5a3a8a" stroke-width="1.5"/>
  <text x="657" y="238" text-anchor="middle" fill="#a080d0" font-family="monospace" font-size="10">@Transactional</text>
  <text x="657" y="254" text-anchor="middle" fill="#7050a0" font-family="monospace" font-size="10">AOP Proxy</text>
  <line x1="657" y1="200" x2="657" y2="218" stroke="#5a3a8a" stroke-width="1.5" stroke-dasharray="3,2" marker-end="url(#s4b-ar)"/>

  <!-- ArgumentResolver -->
  <rect x="434" y="296" width="268" height="46" rx="6" fill="#1a1a10" stroke="#5a5a2a" stroke-width="1.5"/>
  <text x="568" y="316" text-anchor="middle" fill="#aaaa60" font-family="monospace" font-size="11">ArgumentResolver</text>
  <text x="568" y="332" text-anchor="middle" fill="#606030" font-family="monospace" font-size="9">@RequestBody / @PathVariable / @RequestParam</text>
  <line x1="504" y1="268" x2="504" y2="296" stroke="#555" stroke-width="1.5" stroke-dasharray="3,2" marker-end="url(#s4b-ar)"/>

  <!-- Response arrow: Controller right edge → down → left under all boxes → up to Browser bottom -->
  <line x1="702" y1="174" x2="702" y2="360" stroke="#3d7a3d" stroke-width="1.5"/>
  <line x1="702" y1="360" x2="20" y2="360" stroke="#3d7a3d" stroke-width="1.5"/>
  <line x1="20" y1="360" x2="20" y2="230" stroke="#3d7a3d" stroke-width="1.5" marker-end="url(#s4b-gn)"/>
  <text x="400" y="354" text-anchor="middle" fill="#3d6a3d" font-family="monospace" font-size="10">JSON Response (Jackson 직렬화)</text>

  <!-- Startup info at bottom -->
  <rect x="20" y="376" width="316" height="44" rx="6" fill="#0a1a0a" stroke="#2e5c2e" stroke-width="1.5"/>
  <text x="178" y="394" text-anchor="middle" fill="#4a8a4a" font-family="monospace" font-size="10" font-weight="600">SpringApplication.run() 시작 시</text>
  <text x="178" y="410" text-anchor="middle" fill="#3a6a3a" font-family="monospace" font-size="9">Tomcat 기동 · AppContext 구성 · Auto-config 적용</text>

  <rect x="346" y="376" width="354" height="44" rx="6" fill="#1a1028" stroke="#5a3a8a" stroke-width="1.5"/>
  <text x="523" y="394" text-anchor="middle" fill="#a080d0" font-family="monospace" font-size="10" font-weight="600">Auto-configuration</text>
  <text x="523" y="410" text-anchor="middle" fill="#7050a0" font-family="monospace" font-size="9">@ConditionalOnClass/MissingBean — 필요한 빈만 자동 생성</text>
</svg>

---

## 현재 구조가 왜 등장했는가

각 기술이 해결한 문제를 한 줄로 정리하면:

| 기술 | 해결한 핵심 문제 |
|------|----------------|
| Servlet/JSP | CGI 대비 Java 코드 재사용, 멀티스레드 처리 |
| Spring IoC/DI | 강한 결합 제거, 교체 가능한 구현체 |
| Spring AOP | 트랜잭션/보안 로직을 비즈니스에서 분리 |
| Spring MVC | 단일 진입점, 역할 분리 (Controller/Service/Repository) |
| Spring 3.x+ | XML 탈출, REST API, Java Config |
| Spring Boot | 설정 자동화, 내장 서버, 빠른 시작 |

---

## 레거시 코드를 이해해야 하는 이유

실무에서 마주치는 상황:

**`web.xml`이 있는 프로젝트**
- Spring Boot 이전 프로젝트. DispatcherServlet이 XML로 등록되어 있다.
- Filter, Listener 등록도 XML에 있다. 설정을 찾으려면 XML을 봐야 한다.

**`applicationContext.xml`이 있는 프로젝트**
- 빈 정의가 Java Config가 아닌 XML에 있다.
- `@Autowired`가 동작하지 않는 것처럼 보이면 XML에서 별도로 DI 설정이 되어 있는 경우다.

**`implements Controller` 또는 `extends HttpServlet`**
- 어노테이션이 아닌 인터페이스 기반 MVC. 메서드 시그니처가 다르다.

**트랜잭션이 동작하지 않는 것처럼 보이는 경우**
- `@Transactional`이 있어도 같은 클래스 내에서 self-invocation하면 AOP 프록시를 경유하지 않는다.
- 이것은 Spring의 AOP 프록시 구조를 알아야 이해할 수 있는 문제다.

Spring Boot가 모든 것을 자동으로 처리해주지만, 내부에서는 여전히 DispatcherServlet이 동작하고, AOP 프록시가 트랜잭션을 관리하고, IoC 컨테이너가 빈을 연결하고 있다. Boot는 그 위에 편의 레이어를 얹은 것이다.

레거시 코드를 유지보수하거나, 자동 설정이 왜 동작하지 않는지 디버깅하거나, 복잡한 트랜잭션 문제를 추적할 때 — 결국 아래 계층의 동작 방식을 알아야 한다.
""";

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

    private static final String BLOG_ARCHITECTURE_CONTENT = """
# 이 블로그의 아키텍처

## 들어가며

개인 블로그를 만들기로 했을 때 가장 먼저 부딪히는 문제는 "서버를 어디에 둘 것인가"이다. AWS EC2나 GCP를 쓰면 월 사용료가 발생하고, 저렴한 VPS는 스펙이 빈약하다.

이 블로그는 별도 서버 없이 개인 맥북에서 실행된다. Cloudflare Tunnel 덕분에 포트 포워딩이나 공인 IP 없이도 외부 접근이 가능하다. 어떤 구조로 만들어졌는지 순서대로 설명한다.

---

## 전체 구조

<svg viewBox="0 0 720 400" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="a1" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
    <marker id="a1g" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#3d7a3d"/></marker>
    <marker id="a1o" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#e8943a"/></marker>
  </defs>
  <rect width="720" height="400" rx="8" fill="#161616"/>
  <!-- local machine background -->
  <rect x="6" y="150" width="708" height="242" rx="6" fill="#0a140a" stroke="#2a3a2a" stroke-width="1" stroke-dasharray="6,4"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">전체 인프라 아키텍처</text>

  <!-- Browser -->
  <rect x="280" y="28" width="160" height="44" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="360" y="48" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="13" font-weight="600">Browser</text>
  <text x="360" y="64" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">사용자 (외부 인터넷)</text>

  <line x1="360" y1="72" x2="360" y2="88" stroke="#555" stroke-width="1.5" marker-end="url(#a1)"/>
  <text x="366" y="83" fill="#555" font-family="monospace" font-size="9">HTTPS</text>

  <!-- Cloudflare -->
  <rect x="110" y="90" width="500" height="50" rx="6" fill="#1e2a3a" stroke="#1a6abf" stroke-width="1.5"/>
  <text x="360" y="110" text-anchor="middle" fill="#4a9eff" font-family="monospace" font-size="13" font-weight="600">Cloudflare</text>
  <text x="360" y="128" text-anchor="middle" fill="#5588bb" font-family="monospace" font-size="10">DNS + DDoS 방어 + Tunnel (공인 IP 불필요)</text>

  <line x1="360" y1="140" x2="360" y2="156" stroke="#555" stroke-width="1.5" marker-end="url(#a1)"/>
  <text x="366" y="151" fill="#555" font-family="monospace" font-size="9">HTTP</text>

  <!-- nginx -->
  <rect x="210" y="158" width="300" height="50" rx="6" fill="#252525" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="360" y="178" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="13" font-weight="600">nginx : 80</text>
  <text x="360" y="196" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">리버스 프록시 · 유일한 외부 노출 포트</text>

  <!-- nginx to 4 services -->
  <line x1="240" y1="208" x2="98" y2="230" stroke="#555" stroke-width="1.2" marker-end="url(#a1)"/>
  <line x1="300" y1="208" x2="270" y2="230" stroke="#555" stroke-width="1.2" marker-end="url(#a1)"/>
  <line x1="430" y1="208" x2="455" y2="230" stroke="#555" stroke-width="1.2" marker-end="url(#a1)"/>
  <line x1="490" y1="208" x2="625" y2="230" stroke="#555" stroke-width="1.2" marker-end="url(#a1)"/>

  <!-- web-blue -->
  <rect x="16" y="230" width="164" height="58" rx="5" fill="#1e3a1e" stroke="#3d7a3d" stroke-width="1.5"/>
  <text x="98" y="250" text-anchor="middle" fill="#6dbf6d" font-family="monospace" font-size="12" font-weight="600">web-blue</text>
  <text x="98" y="266" text-anchor="middle" fill="#4d9f4d" font-family="monospace" font-size="10">Spring Boot 4</text>
  <text x="98" y="280" text-anchor="middle" fill="#3a7a3a" font-family="monospace" font-size="10">React SPA 서빙 포함</text>

  <!-- web-green -->
  <rect x="192" y="230" width="164" height="58" rx="5" fill="#1a2a1a" stroke="#2d5a2d" stroke-width="1.5" stroke-dasharray="5,3"/>
  <text x="274" y="250" text-anchor="middle" fill="#4a7a4a" font-family="monospace" font-size="12" font-weight="600">web-green</text>
  <text x="274" y="266" text-anchor="middle" fill="#3a6a3a" font-family="monospace" font-size="10">배포 시에만 기동</text>
  <text x="274" y="280" text-anchor="middle" fill="#2a5a2a" font-family="monospace" font-size="10">profiles: deploy</text>

  <!-- jenkins -->
  <rect x="368" y="230" width="164" height="58" rx="5" fill="#3a2a1a" stroke="#e8943a" stroke-width="1.5"/>
  <text x="450" y="250" text-anchor="middle" fill="#e8943a" font-family="monospace" font-size="12" font-weight="600">Jenkins</text>
  <text x="450" y="266" text-anchor="middle" fill="#b07030" font-family="monospace" font-size="10">CI/CD 파이프라인</text>
  <text x="450" y="280" text-anchor="middle" fill="#906020" font-family="monospace" font-size="10">Blue-Green 배포</text>
  <!-- jenkins → web-green deploy arrow -->
  <line x1="368" y1="255" x2="356" y2="255" stroke="#e8943a" stroke-width="1.2" stroke-dasharray="3,2" marker-end="url(#a1o)"/>
  <text x="362" y="249" text-anchor="middle" fill="#906020" font-family="monospace" font-size="8">배포</text>

  <!-- monitoring -->
  <rect x="544" y="230" width="160" height="58" rx="5" fill="#2a1e3a" stroke="#9b59b6" stroke-width="1.5"/>
  <text x="624" y="250" text-anchor="middle" fill="#b07ad0" font-family="monospace" font-size="12" font-weight="600">Prometheus</text>
  <text x="624" y="266" text-anchor="middle" fill="#906ab0" font-family="monospace" font-size="10">+ Grafana</text>
  <text x="624" y="280" text-anchor="middle" fill="#7050a0" font-family="monospace" font-size="10">메트릭 수집·시각화</text>

  <!-- data layer -->
  <text x="14" y="310" fill="#444" font-family="monospace" font-size="10">── 데이터 계층 ──────────────────────────────────────────────────────</text>

  <line x1="98" y1="288" x2="98" y2="326" stroke="#2a5a2a" stroke-width="1.2" marker-end="url(#a1g)"/>
  <line x1="274" y1="288" x2="274" y2="326" stroke="#2a5a2a" stroke-width="1.2" stroke-dasharray="4,3" marker-end="url(#a1g)"/>

  <!-- MongoDB -->
  <rect x="16" y="328" width="164" height="50" rx="5" fill="#1e1e1e" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="98" y="349" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12" font-weight="600">MongoDB</text>
  <text x="98" y="366" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">블로그 포스트</text>

  <!-- Redis -->
  <rect x="192" y="328" width="164" height="50" rx="5" fill="#1e1e1e" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="274" y="349" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12" font-weight="600">Redis</text>
  <text x="274" y="366" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">Refresh Token · 캐시</text>

  <!-- Oracle -->
  <rect x="368" y="328" width="164" height="50" rx="5" fill="#1e1e1e" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="450" y="349" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12" font-weight="600">Oracle XE</text>
  <text x="450" y="366" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">사용자 · 방문 통계</text>

  <!-- TSDB -->
  <rect x="544" y="328" width="160" height="50" rx="5" fill="#1e1e1e" stroke="#3a3a3a" stroke-width="1.5"/>
  <text x="624" y="349" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="12" font-weight="600">TSDB</text>
  <text x="624" y="366" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">시계열 메트릭 (15일)</text>

  <text x="694" y="387" text-anchor="end" fill="#333" font-family="monospace" font-size="9">로컬 맥북 — Docker Compose</text>
</svg>

nginx 하나만 포트 `80`을 외부에 노출하고, 나머지 서비스는 모두 `expose`(Docker 내부 전용)만 사용한다. 외부에서 보면 nginx가 전부다.

---

## Cloudflare Tunnel — 핵심 아이디어

일반적으로 집 네트워크에서 서비스를 외부에 노출하려면 공유기 포트 포워딩과 공인 IP가 필요하다. IP가 유동적이면 DDNS도 관리해야 한다. Cloudflare Tunnel은 이 과정을 전부 생략한다.

```
로컬 → cloudflared 데몬이 Cloudflare 엣지에 아웃바운드 연결 유지
외부 → Cloudflare 엣지 → 그 연결을 역방향으로 타고 로컬까지 도달
```

로컬 머신이 먼저 Cloudflare에 연결을 열어두기 때문에 인바운드 포트 개방이 전혀 없다. 공유기 설정 변경도, 공인 IP도 필요 없다.

---

## 애플리케이션 계층

Spring Boot가 REST API 서버와 React SPA 정적 파일 서빙을 동시에 담당한다. 별도의 정적 파일 서버 없이 단일 JAR 하나로 프론트엔드와 백엔드를 함께 배포한다.

```
GET /api/posts     → Spring Boot (PostController)
GET /api/auth      → Spring Boot (AuthController)
GET /*             → React SPA (index.html fallback)
```

### 데이터 접근 패턴

| 도메인 | 저장소 | 이유 |
|--------|--------|------|
| 블로그 포스트 | MongoDB | 스키마 변경이 잦고 마크다운 본문이 가변 길이 |
| Refresh Token | Redis | TTL 기반 자동 만료가 필요 |
| 사용자·방문 통계 | Oracle XE | 관계형 집계 쿼리 (JPA + MyBatis 혼용) |

---

## Blue-Green 무중단 배포

배포할 때 서비스를 내리지 않는다. 현재 트래픽을 받는 슬롯(blue)이 살아있는 상태에서 새 버전을 다른 슬롯(green)에 띄우고, nginx upstream만 바꿔서 전환한다.

<svg viewBox="0 0 720 320" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="a2" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
    <marker id="a2g" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#3d9e3d"/></marker>
  </defs>
  <rect width="720" height="320" rx="8" fill="#161616"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">Blue-Green 배포 흐름</text>

  <!-- Step 1 -->
  <rect x="16" y="36" width="124" height="80" rx="5" fill="#3a2a1a" stroke="#e8943a" stroke-width="1.5"/>
  <text x="78" y="57" text-anchor="middle" fill="#e8943a" font-family="monospace" font-size="11" font-weight="600">1. 빌드</text>
  <text x="78" y="74" text-anchor="middle" fill="#906020" font-family="monospace" font-size="9">Jenkins</text>
  <text x="78" y="89" text-anchor="middle" fill="#906020" font-family="monospace" font-size="9">docker build</text>
  <text x="78" y="104" text-anchor="middle" fill="#906020" font-family="monospace" font-size="9">jkypch-web:latest</text>
  <line x1="140" y1="76" x2="160" y2="76" stroke="#555" stroke-width="1.5" marker-end="url(#a2)"/>

  <!-- Step 2 -->
  <rect x="162" y="36" width="124" height="80" rx="5" fill="#1e2a1e" stroke="#3d7a3d" stroke-width="1.5"/>
  <text x="224" y="57" text-anchor="middle" fill="#5db05d" font-family="monospace" font-size="11" font-weight="600">2. green 기동</text>
  <text x="224" y="74" text-anchor="middle" fill="#3d8a3d" font-family="monospace" font-size="9">docker compose</text>
  <text x="224" y="89" text-anchor="middle" fill="#3d8a3d" font-family="monospace" font-size="9">--profile deploy</text>
  <text x="224" y="104" text-anchor="middle" fill="#3d8a3d" font-family="monospace" font-size="9">up -d web-green</text>
  <line x1="286" y1="76" x2="306" y2="76" stroke="#555" stroke-width="1.5" marker-end="url(#a2)"/>

  <!-- Step 3 -->
  <rect x="308" y="36" width="124" height="80" rx="5" fill="#1e1e2a" stroke="#4a6abf" stroke-width="1.5"/>
  <text x="370" y="57" text-anchor="middle" fill="#6a9aef" font-family="monospace" font-size="11" font-weight="600">3. 헬스체크</text>
  <text x="370" y="74" text-anchor="middle" fill="#4a7abf" font-family="monospace" font-size="9">GET /actuator</text>
  <text x="370" y="89" text-anchor="middle" fill="#4a7abf" font-family="monospace" font-size="9">/health</text>
  <text x="370" y="104" text-anchor="middle" fill="#4a7abf" font-family="monospace" font-size="9">200 대기 (60s)</text>
  <line x1="432" y1="76" x2="452" y2="76" stroke="#555" stroke-width="1.5" marker-end="url(#a2)"/>

  <!-- Step 4 -->
  <rect x="454" y="36" width="124" height="80" rx="5" fill="#252525" stroke="#aaaaaa" stroke-width="1.5"/>
  <text x="516" y="57" text-anchor="middle" fill="#cccccc" font-family="monospace" font-size="11" font-weight="600">4. nginx 전환</text>
  <text x="516" y="74" text-anchor="middle" fill="#888" font-family="monospace" font-size="9">web-active.conf</text>
  <text x="516" y="89" text-anchor="middle" fill="#888" font-family="monospace" font-size="9">upstream 수정</text>
  <text x="516" y="104" text-anchor="middle" fill="#888" font-family="monospace" font-size="9">nginx -s reload</text>
  <line x1="578" y1="76" x2="598" y2="76" stroke="#555" stroke-width="1.5" marker-end="url(#a2)"/>

  <!-- Step 5 -->
  <rect x="600" y="36" width="104" height="80" rx="5" fill="#2a1e1e" stroke="#c04040" stroke-width="1.5"/>
  <text x="652" y="57" text-anchor="middle" fill="#e06060" font-family="monospace" font-size="11" font-weight="600">5. blue 중지</text>
  <text x="652" y="78" text-anchor="middle" fill="#a04040" font-family="monospace" font-size="9">docker stop</text>
  <text x="652" y="93" text-anchor="middle" fill="#a04040" font-family="monospace" font-size="9">web-blue</text>

  <!-- Before / After -->
  <text x="14" y="148" fill="#444" font-family="monospace" font-size="10">── 전환 전후 ────────────────────────────────────────────────────────</text>

  <!-- Before -->
  <text x="150" y="170" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">전환 전</text>
  <rect x="50" y="178" width="110" height="34" rx="4" fill="#1e3a1e" stroke="#3d7a3d" stroke-width="1.5"/>
  <text x="105" y="200" text-anchor="middle" fill="#6dbf6d" font-family="monospace" font-size="11">web-blue ●</text>
  <rect x="170" y="178" width="110" height="34" rx="4" fill="#1a1a1a" stroke="#2a2a2a" stroke-width="1.5"/>
  <text x="225" y="200" text-anchor="middle" fill="#444" font-family="monospace" font-size="11">web-green ○</text>
  <rect x="76" y="222" width="58" height="22" rx="3" fill="#252525" stroke="#3a3a3a" stroke-width="1"/>
  <text x="105" y="237" text-anchor="middle" fill="#888" font-family="monospace" font-size="9">nginx →</text>
  <line x1="105" y1="212" x2="105" y2="222" stroke="#3d7a3d" stroke-width="1.5" marker-end="url(#a2g)"/>

  <!-- Arrow between -->
  <line x1="296" y1="200" x2="330" y2="200" stroke="#555" stroke-width="1.5" marker-end="url(#a2)"/>
  <text x="313" y="195" text-anchor="middle" fill="#555" font-family="monospace" font-size="9">reload</text>

  <!-- After -->
  <text x="460" y="170" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">전환 후</text>
  <rect x="350" y="178" width="110" height="34" rx="4" fill="#1a1a1a" stroke="#2a2a2a" stroke-width="1.5"/>
  <text x="405" y="200" text-anchor="middle" fill="#444" font-family="monospace" font-size="11">web-blue ○</text>
  <rect x="470" y="178" width="110" height="34" rx="4" fill="#1e3a1e" stroke="#3d7a3d" stroke-width="1.5"/>
  <text x="525" y="200" text-anchor="middle" fill="#6dbf6d" font-family="monospace" font-size="11">web-green ●</text>
  <rect x="496" y="222" width="58" height="22" rx="3" fill="#252525" stroke="#3a3a3a" stroke-width="1"/>
  <text x="525" y="237" text-anchor="middle" fill="#888" font-family="monospace" font-size="9">nginx →</text>
  <line x1="525" y1="212" x2="525" y2="222" stroke="#3d7a3d" stroke-width="1.5" marker-end="url(#a2g)"/>

  <!-- rollback -->
  <rect x="598" y="166" width="108" height="80" rx="4" fill="#1e1e1e" stroke="#555" stroke-width="1" stroke-dasharray="4,3"/>
  <text x="652" y="186" text-anchor="middle" fill="#666" font-family="monospace" font-size="9">헬스체크 실패 시</text>
  <text x="652" y="202" text-anchor="middle" fill="#e06060" font-family="monospace" font-size="10">자동 롤백</text>
  <text x="652" y="218" text-anchor="middle" fill="#555" font-family="monospace" font-size="9">green 중지</text>
  <text x="652" y="234" text-anchor="middle" fill="#555" font-family="monospace" font-size="9">blue 유지</text>

  <text x="360" y="282" text-anchor="middle" fill="#555" font-family="monospace" font-size="10">nginx reload는 기존 커넥션을 끊지 않는다 — 요청 단위 무중단 전환</text>
</svg>

핵심은 nginx `reload`의 동작 방식이다. `restart`와 달리 `reload`는 기존 커넥션을 유지한 채 새 설정으로 워커 프로세스를 교체한다. Jenkins는 `docker.sock`을 마운트받아 호스트 Docker를 직접 제어하기 때문에, 별도 빌드 에이전트 없이 Jenkins 컨테이너가 `docker build`와 `docker compose up`을 직접 실행한다.

---

## Dockerfile — 3단계 빌드

배포 이미지 크기를 줄이기 위해 빌드 도구를 런타임 이미지에 포함하지 않는다.

```dockerfile
# Stage 1: 프론트엔드 빌드
FROM node:22-alpine AS frontend-build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build          # → /app/dist

# Stage 2: 백엔드 빌드
FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline -q   # 의존성 레이어 캐시
COPY src/ src/
COPY --from=frontend-build /app/dist src/main/resources/static/
RUN mvn package -DskipTests -q     # → target/*.jar

# Stage 3: 런타임 (JRE만)
FROM eclipse-temurin:21-jre
RUN apt-get update -qq && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

최종 이미지에는 Node.js도, Maven도, JDK도 없다. JRE + JAR만 남는다.

---

## 모니터링

각 서비스 옆에 Exporter를 붙여 Prometheus가 메트릭을 수집하고 Grafana가 시각화한다.

| Exporter | 수집 대상 |
|----------|----------|
| `/actuator/prometheus` | Spring Boot (직접 제공) |
| nginx-prometheus-exporter | nginx 요청 수, 커넥션 수 |
| percona/mongodb_exporter | MongoDB 쿼리, 커넥션 |
| redis_exporter | Redis 명령 수, 메모리 |
| oracledb_exporter | Oracle 세션, 쿼리 |

Spring Boot만 Prometheus 포맷을 직접 지원한다. 나머지는 자체 포맷이 있거나 외부 스크랩을 지원하지 않아 Exporter가 중간 변환을 담당한다.

---

## 마치며

"서버가 없다"는 제약이 생각보다 많은 것을 강제했다. Cloudflare Tunnel을 써야 했고, 배포 중 서비스 중단을 허용할 수 없어 Blue-Green을 구현했고, 로컬 리소스 모니터링이 필요해 Prometheus를 붙였다.

제약이 설계를 만든다.
""";

    private static final String NLDD_CONTENT = """
<nav id="toc" style="background:#111;border:1px solid #2a2a3a;border-radius:8px;padding:1rem 1.5rem;margin:1.5rem 0">
<p style="margin:0 0 0.5rem;color:#888;font-size:0.9rem;font-weight:bold">목차</p>
<p style="margin:0 0 0.3rem;font-size:0.85rem"><a href="#s1" style="color:#6a9aef;text-decoration:none">1. 개발 언어의 발전 방향</a></p>
<p style="margin:0 0 0.3rem;font-size:0.85rem"><a href="#s2" style="color:#6a9aef;text-decoration:none">2. 개발 언어의 다섯 세대</a></p>
<p style="margin:0 0 0.3rem;font-size:0.85rem"><a href="#s5" style="color:#6a9aef;text-decoration:none">3. NLDD(Natural Language-Driven Development, 자연어 주도 개발)</a></p>
<p style="margin:0 0 0.3rem;font-size:0.85rem"><a href="#s7" style="color:#6a9aef;text-decoration:none">4. NLDD 실전 가이드</a></p>
<p style="margin:0;font-size:0.85rem"><a href="#s6" style="color:#6a9aef;text-decoration:none">5. 5세대 개발자의 필수 역량</a></p>
</nav>

---

<h2 id="s1"><a href="#toc">1. 개발 언어의 발전 방향 </a></h2>

1940년대에는 0과 1을 직접 타이핑했다. 80년이 지난 지금은 고급언어로 코드를 쓴다.
기계어에서 어셈블리로, 어셈블리에서 고급 언어로, 고급 언어에서 프레임워크로 발전해왔다.
개발의 역사는 "기계가 알아듣기 편한 말"에서 "인간이 말하기 편한 말"로 변화해왔다.

1. 이전 세대의 **귀찮고 실수 잦은 저수준 작업**이 드러난다
2. 새 도구가 그 작업을 **자동화**한다다
3. 개발자는 **더 중요한 문제**에 집중한다

26년 현재는 프레임워크에서 자연어로의 단계이다.
LLM의 등장과 함께 자연어로 의도를 전달하면 코드가 생성되는 시대가 열렸다. 이 글에서는 이 흐름을 **제5세대**로 정의하고 개발자로서 나아갈 방향을 이야기한다.

---

<h2 id="s2"><a href="#toc">2. 개발 언어의 다섯 세대</a></h2>

<svg viewBox="0 0 720 330" xmlns="http://www.w3.org/2000/svg" style="width:100%;max-width:720px;margin:1.5rem auto;display:block;border-radius:8px">
  <defs>
    <marker id="g-ar" markerWidth="8" markerHeight="6" refX="7" refY="3" orient="auto"><polygon points="0 0,8 3,0 6" fill="#555"/></marker>
  </defs>
  <rect width="720" height="330" rx="8" fill="#161616"/>
  <text x="14" y="18" fill="#555" font-family="monospace" font-size="11">프로그래밍 언어 세대 진화 — 추상화 수준의 확대</text>

  <line x1="24" y1="300" x2="24" y2="50" stroke="#333" stroke-width="1" marker-end="url(#g-ar)"/>
  <text x="16" y="180" fill="#444" font-family="monospace" font-size="9" transform="rotate(-90, 16, 180)">추상화 수준</text>

  <rect x="42" y="258" width="108" height="54" rx="5" fill="#1e1e1e" stroke="#555" stroke-width="1.5"/>
  <text x="96" y="275" text-anchor="middle" fill="#888" font-family="monospace" font-size="11" font-weight="600">제1세대</text>
  <text x="96" y="290" text-anchor="middle" fill="#666" font-family="monospace" font-size="10">기계어</text>
  <text x="96" y="303" text-anchor="middle" fill="#555" font-family="monospace" font-size="8">1940s~ · 이진수</text>

  <line x1="150" y1="280" x2="174" y2="240" stroke="#555" stroke-width="1.2" marker-end="url(#g-ar)"/>

  <rect x="174" y="206" width="108" height="54" rx="5" fill="#1a1e2a" stroke="#4a6a9a" stroke-width="1.5"/>
  <text x="228" y="223" text-anchor="middle" fill="#6a9aef" font-family="monospace" font-size="11" font-weight="600">제2세대</text>
  <text x="228" y="238" text-anchor="middle" fill="#5080bf" font-family="monospace" font-size="10">어셈블리어</text>
  <text x="228" y="251" text-anchor="middle" fill="#4060a0" font-family="monospace" font-size="8">1950s~ · 니모닉</text>

  <line x1="282" y1="228" x2="306" y2="188" stroke="#555" stroke-width="1.2" marker-end="url(#g-ar)"/>

  <rect x="306" y="154" width="108" height="54" rx="5" fill="#1a2528" stroke="#3a8a8a" stroke-width="1.5"/>
  <text x="360" y="171" text-anchor="middle" fill="#5aafaf" font-family="monospace" font-size="11" font-weight="600">제3세대</text>
  <text x="360" y="186" text-anchor="middle" fill="#408a8a" font-family="monospace" font-size="10">고급 언어</text>
  <text x="360" y="199" text-anchor="middle" fill="#307070" font-family="monospace" font-size="8">1960s~ · C / Java</text>

  <line x1="414" y1="176" x2="438" y2="136" stroke="#555" stroke-width="1.2" marker-end="url(#g-ar)"/>

  <rect x="438" y="102" width="108" height="54" rx="5" fill="#1a251a" stroke="#3d8a3d" stroke-width="1.5"/>
  <text x="492" y="119" text-anchor="middle" fill="#5daf5d" font-family="monospace" font-size="11" font-weight="600">제4세대</text>
  <text x="492" y="134" text-anchor="middle" fill="#3d8a3d" font-family="monospace" font-size="10">DSL·프레임워크</text>
  <text x="492" y="147" text-anchor="middle" fill="#2d6a2d" font-family="monospace" font-size="8">1990s~ · SQL / Spring</text>

  <line x1="546" y1="124" x2="570" y2="84" stroke="#555" stroke-width="1.2" marker-end="url(#g-ar)"/>

  <rect x="570" y="48" width="120" height="58" rx="5" fill="#2a1e0a" stroke="#e8943a" stroke-width="2"/>
  <text x="630" y="67" text-anchor="middle" fill="#e8943a" font-family="monospace" font-size="12" font-weight="600">제5세대</text>
  <text x="630" y="83" text-anchor="middle" fill="#c07a2a" font-family="monospace" font-size="10">자연어 (LLM)</text>
  <text x="630" y="97" text-anchor="middle" fill="#a06020" font-family="monospace" font-size="8">2020s~ · 의도 기반</text>

  <text x="360" y="322" text-anchor="middle" fill="#444" font-family="monospace" font-size="9">각 세대는 이전 세대의 저수준 복잡성을 은닉하고, 개발자를 의도에 더 가깝게 이동시킨다</text>
</svg>

| 세대 | 대표 언어 | 자동화 대상 | 개발자가 집중하는 것 |
|------|-----------|-------------|-------------------|
| 1세대 | 기계어 | 없음 | 전기 신호 제어 |
| 2세대 | 어셈블리 | 이진수 → 니모닉 | 레지스터·메모리 배치 |
| 3세대 | C, Java, Python | 하드웨어 세부사항 | 알고리즘·자료구조 |
| 4세대 | SQL, Spring, React | 반복 패턴·보일러플레이트 | 비즈니스 로직 |
| **5세대** | **자연어 (LLM)** | **코딩 행위 자체** | **의도·제약·검증** |

### 1세대: 기계어 (1940s~)

```
10110000 01100001    ; MOV AL, 97 — 숫자 97을 레지스터에 저장
```

### 2세대: 어셈블리어 (1950s~)

```
MOV AL, 61h       ; 97을 AL에 저장
ADD AL, 03h       ; AL에 3을 더함
```

### 3세대: 고급 언어 (1960s~)

```java
int x = 97;
x += 3;
```

### 4세대: DSL과 프레임워크 (1990s~)

```java
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    return userRepository.findById(id).orElseThrow();
}
```

### 5세대: 자연어 (2020s~)

```
"Spring Boot 기반으로 사용자 ID로 조회하는 REST API를 만들어줘."
```

---

<h2 id="s5"><a href="#toc">3. NLDD(Natural Language-Driven Development)</a></h2>

### 3계층 명세 모델

NLDD에서 자연어는 세 가지 계층으로 구성된다.

| 계층 | 핵심 질문 | 예시 |
|------|----------|------|
| **의도** (Intent) | 무엇을 만드는가? | "JWT 기반 인증 시스템" |
| **제약** (Constraint) | 어떤 조건에서? | "Access Token 30분, Refresh는 Redis" |
| **검증** (Verification) | 어떻게 확인하는가? | "만료 토큰 → 401, Refresh → 새 Access 발급" |

전통적 개발의 요구사항 명세서(SRS), 아키텍처 결정 기록(ADR), 테스트 케이스를 자연어 명세 하나로 통합시킨다.

### 프롬프트 vs NLDD 명세

**프롬프트:**
```
로그인 기능 만들어줘
```

**NLDD 명세:**
```
[의도] JWT 기반 인증 시스템을 구현한다.
  - 로그인 시 Access Token(30분)과 Refresh Token(7일) 발급
  - Access Token은 응답 헤더, Refresh Token은 HttpOnly 쿠키

[제약]
  - Refresh Token은 Redis, 사용자당 하나만 유효
  - 비밀번호는 BCrypt 해싱
  - 로그인 5회 실패 시 10분 잠금

[검증]
  - 만료 Access Token → 401
  - 유효한 Refresh Token으로 /api/auth/refresh → 새 Access 발급
  - 탈취된 Refresh Token(Redis에 없음) → 해당 사용자 전체 세션 무효화
```

---

<h2 id="s7"><a href="#toc">4. NLDD 실전 가이드</a> <button id="nldd-toggle-all" style="margin-left:0.5rem;font-size:0.7rem;padding:0.15rem 0.5rem;background:#1a1a2e;border:1px solid #2a2a3a;border-radius:4px;color:#6a9aef;cursor:pointer">모두 펼치기</button></h2>

Google Shell Style Guide처럼 실전 규칙을 정리했다. 각 규칙에 좋은 예/나쁜 예를 달았다.

<div id="nldd-guide-section">

<details>
<summary>규칙 1: 하나의 명세, 하나의 목적</summary>

**나쁜 예:**
```
로그인, 회원가입, 마이페이지, 비밀번호 찾기 만들어줘.
소셜 로그인도 지원하고 관리자 페이지도 필요해.
```

**좋은 예:**
```
[의도] JWT 기반 로그인 시스템을 구현한다.
  - 이메일/비밀번호로 로그인
  - Access Token(30분) + Refresh Token(7일) 발급
```

**실험 결과** — 동일 프로젝트에 "방문자 통계" 기능을 두 방식으로 요청:

| | 나쁜 예 (4기능 한 번에) | 좋은 예 (일별 조회 API 1개) |
|---|---|---|
| LLM이 추측한 횟수 | 12회 | 1회 |
| 생성 파일 수 | 15+개 | 3개 |
| 기존 코드 충돌 | VisitLog와 중복 시스템 생성 | 기존 DailyStatDto 재사용 |
| 예상 재작업률 | 60~80% | ~0% |

</details>

<details>
<summary>규칙 2: 의도를 먼저, 구현은 나중에</summary>

**나쁜 예:**
```
Redis에 키를 "session:{userId}" 형식으로 넣고
TTL 1800초 설정해서 세션 관리해줘.
StringRedisTemplate 써서 구현하고.
```

**좋은 예:**
```
[의도] 로그인 세션을 30분간 유지한다.
[제약] 저장소는 Redis. 사용자당 하나의 세션만 유효.
[검증] 30분 경과 후 요청 시 401 반환.
```

**실험 결과** — "조회수 중복 방지"를 두 방식으로 요청:

| | 나쁜 예 (구현 지시) | 좋은 예 (의도 전달) |
|---|---|---|
| 프롬프트 | StringRedisTemplate, RedisAtomicLong 지정 | "1시간 내 재방문 시 조회수 +1만" |
| 결과 | RedisAtomicLong 불필요, 기존 VisitLog 무시, 통합 불가 | SET NX EX 자연 선택, 기존 패턴과 정합 |
| 프로젝트 정합성 | 낮음 | 높음 |

</details>

<details>
<summary>규칙 3: 제약은 측정 가능하게 쓴다</summary>

**나쁜 예:**
```
빠르게 동작해야 하고 보안도 신경 써줘.
에러 처리도 잘 해줘.
```

**좋은 예:**
```
[제약]
  - P99 응답 시간 200ms 이하
  - 비밀번호는 BCrypt(cost 12) 해싱
  - 로그인 5회 실패 시 계정 10분 잠금
  - 모든 인증 실패 시 구조화 로그 출력
```

**실험 결과** — "포스트 검색"을 두 방식으로 요청:

| | 나쁜 예 (모호한 제약) | 좋은 예 (측정 가능한 제약) |
|---|---|---|
| 프롬프트 | "빠르게, 정확하게, 편하게" | "100ms 이하, text index, 20건 페이징" |
| 해석 가능 조합 | ~60가지 | 1가지 |
| 테스트 케이스 도출 | 불가능 | 3개 즉시 도출 |
| LLM 구현 | $regex + 페이지네이션 없음 (임의 판단) | text index + Pageable + 입력 검증 (명세 그대로) |

</details>

<details>
<summary>규칙 4: 기존 맥락을 전달한다</summary>

**나쁜 예:**
```
사용자 목록 API 만들어줘.
```

**좋은 예:**
```
[의도] 사용자 목록 조회 API를 추가한다.

[맥락]
  - CLAUDE.md 또는 프로젝트 README를 먼저 읽어라
  - 기존 패턴 참고: VisitLogController.java

[제약] 페이징 필수 (기본 20건, 최대 100건)
```

맥락은 사람이 기억에 의존해 직접 나열하면 틀리기 쉽다.
프로젝트 문서(CLAUDE.md, README)에 위임하고, 참고할 코드만 정확히 지정하라.

**실험 결과** — "사용자 목록 API"를 세 방식으로 요청:

| | 맥락 없음 | 수동 맥락 나열 | 문서 참조 지시 |
|---|---|---|---|
| 정답률 | 0/6 (0%) | 1/6 (17%) | 6/6 (100%) |
| 주요 오류 | DB, ORM, 패키지, 계층 전부 추측 | Service 레이어 없는데 생성, 잘못된 테이블명 참조 | 문서에서 확인 후 정확한 판단 |
| 재작업 | 전면 재작성 | 대부분 재작성 + 디버깅 난이도 상승 | 거의 없음 |

수동 맥락은 맥락 없는 것보다 오히려 위험하다 — 틀린 정보를 신뢰하고 구현하기 때문이다.

</details>

<details>
<summary>규칙 5: 부정 케이스를 명시한다</summary>

**나쁜 예:**
```
[검증] 로그인 시 토큰이 발급된다.
```

**좋은 예:**
```
[검증]
  - 올바른 이메일/비밀번호 → 200 + 토큰 발급
  - 존재하지 않는 이메일 → 401
  - 비밀번호 불일치 → 401 (이메일 존재 여부 노출 금지)
  - 5회 실패 후 → 423 Locked + 잠금 해제 시각 응답
  - 잠금 해제 후 정상 로그인 → 200
```

**실험 결과** — "포스트 삭제 API"를 두 방식으로 요청:

| | 나쁜 예 (happy path만) | 좋은 예 (부정 케이스 포함) |
|---|---|---|
| 검증 조건 수 | 1개 | 6개 |
| 구현 결과 | 6줄 `deleteById` + 항상 200 | 5개 분기의 방어적 구현 |
| 누락된 에러 처리 | 6가지 (404/400/401/멱등성/캐시 등) | 0가지 |
| 위험 | 없는 ID 삭제해도 200 반환, 캐시 불일치 | 모든 케이스 커버 |

</details>

<details>
<summary>규칙 6: 한 번에 완성하지 말고 대화한다</summary>

**나쁜 예:**
```
완벽한 인증 시스템 만들어줘. (한 번에 모든 걸 기대)
```

**좋은 예:**
```
1차: [의도] 이메일/비밀번호 로그인 → JWT 발급
  → 생성된 코드 검토 → 토큰 갱신 로직 누락 발견

2차: [의도] Refresh Token 갱신 엔드포인트 추가
     [제약] Redis에 저장, 재사용 시 전체 세션 무효화
  → 검토 → 보안 검증 추가 필요

3차: [검증] 탈취 시나리오 테스트 케이스 추가
     ...
```

**실험 결과** — "블로그 댓글 시스템"을 두 방식으로 요청:

| | 나쁜 예 (한 번에 전부) | 좋은 예 (3라운드 반복) |
|---|---|---|
| 요청 범위 | CRUD+대댓글+좋아요+신고+스팸+알림 | 1차: 기본 CRUD → 2차: 삭제 → 3차: 대댓글 |
| 동시 결정 수 | 7개 (DB, WebSocket/SSE, 스팸, 알림...) | 라운드당 0~1개 |
| 전체 정답 확률 | ~3% (0.6^7) | 라운드당 90%+ |
| 1개 오판 시 영향 | 30~40% 연쇄 재작업 | 해당 라운드만 수정 |

</details>

</div>

---

<h2 id="s6"><a href="#toc">5. 5세대 개발자의 필수 역량</a></h2>

### 추상화 능력

복잡한 시스템을 적절한 단위로 쪼개는 능력.

- 시스템을 독립 모듈로 분해
- 각 모듈의 책임과 인터페이스를 자연어로 정의
- 추상화 수준 간 경계를 식별

### 논리적 정교함

자연어에서 모호성을 제거하는 능력.

"빠르게 동작하게 제작하라" → "P99 응답 시간 200ms 이하"

"에러를 처리하라" → "타임아웃 시 3회 재시도, 지수 백오프, 최종 실패 시 Fallback 반환"

### 검증 능력

LLM이 작성한 코드를 판단하는 능력.

1. **정확성**: 명세대로 동작하는가?
2. **신뢰성**: 엣지 케이스에서도 견고한가?
3. **보안**: SQL 인젝션, 인증 우회 등 취약점은 없는가?
4. **성능**: 응답 속도와 리소스 사용이 기준 이내인가?
5. **유지보수성**: 코드 구조가 명확하고 수정이 용이한가?
6. **운영 가능성**: 로그·모니터링·롤백이 갖춰져 있는가?

""";
}
