#!/usr/bin/env bash

# start.sh
# 배포할 신규 버전 spring boot 프로젝트를 stop.sh로 종료한 'profile'로 실행

###
# step2의 deploy.sh와 비슷하다.

# 다른점은 IDLE_PROFILE을 통해서 properties 파일을 가져오고(application-$IDLE_PROFILE.properties)
# active profile을 지정하는 것 뿐이다.

# 여기서도 IDLE_PROFILE을 사용하니 profile.sh을 가져와야 한다.
###

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3

PROJECT_NAME=freelec-springboot2-webservice

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY


echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

echo "> $JAR_NAME에 실행 권한 추가"

chmod +x $JAR_NAME

# 쉬고있는 profile 찾기: real1이 사용 중이면 real2가 쉬고있고, 반대면 real1이 쉬고있음
IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME를 profile=$IDLE_PROFILE 로 실행합니다."

nohup java -jar -Dspring.config.location=classpath:/application.properties,classpath:/application-$IDLE_PROFILE.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties -Dspring.profiles.active=$IDLE_PROFILE $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

