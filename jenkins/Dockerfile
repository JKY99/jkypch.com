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
    apt-get install -y docker-ce-cli docker-compose-plugin && \
    rm -rf /var/lib/apt/lists/*

# jenkins 유저를 docker 그룹에 추가
RUN groupadd -f docker && usermod -aG docker jenkins

USER jenkins

# 플러그인 설치
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# JCasC 설정
COPY casc/ /usr/share/jenkins/ref/casc/

# 파이프라인 init 스크립트 (최초 기동 시 deploy-web job 생성)
COPY init.groovy.d/ /usr/share/jenkins/ref/init.groovy.d/
