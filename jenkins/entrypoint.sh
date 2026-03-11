#!/bin/bash
set -e

# 마운트된 docker.sock의 실제 GID를 읽어 컨테이너 내 docker 그룹 GID에 맞춤.
# docker.sock GID는 호스트(macOS Docker Desktop 포함)마다 다를 수 있으므로
# 빌드 타임에 고정하지 않고 런타임에 동적으로 처리한다.
if [ -S /var/run/docker.sock ]; then
    SOCK_GID=$(stat -c '%g' /var/run/docker.sock)
    # GID가 이미 다른 그룹에 사용 중인지 확인 (macOS Docker Desktop: GID=0 = root)
    if getent group "$SOCK_GID" > /dev/null 2>&1; then
        SOCK_GROUP=$(getent group "$SOCK_GID" | cut -d: -f1)
        usermod -aG "$SOCK_GROUP" jenkins
    else
        groupmod -g "$SOCK_GID" docker
    fi
fi

# root → jenkins 유저로 전환 후 Jenkins 기동
exec gosu jenkins /usr/local/bin/jenkins.sh "$@"
