#!/usr/bin/env bash

# health.sh
# 'start.sh'로 실행시킨 프로젝트가 정상적으로 실행됐는지 체크

###
# 엔진엑스와 연결되지 않은 포트로 스프링 부트가 잘 수행되었는지 체크한다.
# 잘 떳는지 확인되어야 Nginx 프록시 설정을 변경(switch_proxy) 한다.
# Nginx 프록시 설정 변경은 switch.sh에서 수행한다.
###



ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

# 쉬고있는 profile 찾기: real1이 사용 중이면 real2가 쉬고있고, 반대면 real1이 쉬고있음
IDLE_PORT=$(find_idle_port)

echo "> Headlth Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile "
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l)

  # $up_count >= 1 (real 문자열이 있는지 검증)
  if [ ${UP_COUNT} -ge 1 ]
  then
    echo "> Health check 성공"
    switch_proxy
    break
  else
    echo "> Health check의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
    echo "> Health check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health check 실패."
    echo "> Nginx에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done


