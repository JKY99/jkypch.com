FROM jenkins/jenkins:lts-jdk21

USER root

# Docker CLI 설치 (호스트 docker.sock 경유)
RUN apt-get update -qq && \
    apt-get install -y --no-install-recommends \
        ca-certificates curl gnupg lsb-release && \
    install -m 0755 -d /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg \
        | gpg --dearmor -o /etc/apt/keyrings/docker.gpg && \
    chmod a+r /etc/apt/keyrings/docker.gpg && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
        https://download.docker.com/linux/debian $(lsb_release -cs) stable" \
        > /etc/apt/sources.list.d/docker.list && \
    apt-get update -qq && \
    apt-get install -y docker-ce-cli docker-compose-plugin gosu && \
    rm -rf /var/lib/apt/lists/*

# jenkins 유저를 docker 그룹에 추가 (GID는 entrypoint에서 런타임에 보정)
RUN groupadd -f docker && usermod -aG docker jenkins

# 동적 GID 매칭 엔트리포인트
COPY entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

USER jenkins

# 플러그인 설치
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# JCasC 설정
COPY casc/ /usr/share/jenkins/ref/casc/

# 파이프라인 init 스크립트 (최초 기동 시 deploy-web job 생성)
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/

# groupmod 실행을 위해 root로 기동 후 entrypoint에서 jenkins로 전환
USER root
ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
