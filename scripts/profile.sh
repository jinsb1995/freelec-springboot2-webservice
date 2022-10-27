#!/usr/bin/env bash

# profile.sh
# 앞선 4개 스크립트 파일에서 공용으로 사용할 'profile'과 port 체크 로직

# 쉬고있는 profile 찾기: real1이 사용 중이면 real2가 쉬고있고, 반대면 real1이 쉬고있음
function find_idle_profile() {

  # 현재 Nginx가 바라보고 있는 스프링 부트가 정상적으로 수행중인지 확인한다.
  # 응답값을 HttpStatus로 받는다.
  # 정상이면 200, 오류가 발생한다면 400~503 사이로 발생하니 400이상은 모두 예외로 보고
  # real2를 현재 profile로 사용한다.
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

  if [ ${RESPONSE_CODE} -ge 400 ]   # 400 보다 크면 (즉, 40x / 50x 에러 모두 포함)
  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=real1
  fi


  # IDLE_PROFILE은 Nginx와 연결되지 않는 profile이다.
  # 스프링 부트 프로젝트를 이 profile로 연결하기 위해 반환한다.
 if [ ${CURRENT_PROFILE} == real1 ]
  then
    IDLE_PROFILE=real2
  else
    IDLE_PROFILE=real1
  fi

  # 쉘스크립트는 값을 반환하는 기능이 없다. = return 이 없다.
  # 그래서 제일 마지막 줄에 echo로 결과를 출력 후, 클라이언트에서 그 값을 잡아서 사용한다. $($(find_idle_profile))
  # 중간에 echo를 사용해선 안된다.
  echo "${IDLE_PROFILE}"
}



# 쉬고 있는 profile의 port 찾기
function find_idle_port() {

  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == real1 ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}
