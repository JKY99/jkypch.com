# jkypch.com

개인 서버 (jkypch.com) 소스코드

## 인프라 구성

- **DNS**: Cloudflare DNS (`jkypch.com`)
- **터널링**: Cloudflare Tunnel → 로컬 nginx:80
- **Reverse Proxy**: nginx (앞단, 확장 가능)
- **WAS**: Spring Boot 4.0.3 / JDK 25 (포트 8080, 내부)
- **Frontend**: React (Vite, Spring Boot 정적 파일로 서빙)

## 서비스 구성 (현재)

| 서비스 | 설명 |
|--------|------|
| nginx  | 리버스 프록시, 로드밸런서 |
| web    | Spring Boot WAS (jkypch.com 메인) |
| mongodb | MongoDB |
| redis  | Redis |

> Oracle은 라이선스 이슈로 별도 구성 필요 (application.yml 참고)

## 실행

```bash
cp .env.example .env
# .env 값 수정 후

docker compose up -d
```

## 확장 방법

새 서비스 추가 시 `docker-compose.yml`에 서비스 정의 후,
`nginx/conf.d/` 에 라우팅 설정 파일 추가.

```
nginx/conf.d/
├── default.conf   # jkypch.com → web:8080
├── jenkins.conf   # /jenkins → jenkins:8080 (예시)
└── ...
```

## 개발 환경 (프론트 핫리로드)

```bash
cd web/frontend
npm install
npm run dev   # Vite dev server :5173, API proxy → localhost:8080
```

Spring Boot 별도 실행:
```bash
cd web
./gradlew bootRun
```
