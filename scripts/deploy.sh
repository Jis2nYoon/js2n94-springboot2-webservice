#! /bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=js2n94-springboot2-webservice

echo "> Build 파일 복사"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인"

CURRENT_PID=$(pgrep -fla js2n94-springboot2-webservice | grep java | awk '{print $1}')

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

if [  -z  "$CURRENT_PID"  ];  then
    echo  "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo  "> kill -15  $CURRENT_PID"
    sudo kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포 시작"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name : $JAR_NAME"

echo ">  $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup sudo java -jar \
        -Dspring.config.location=classpath:/application.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties,classpath:/application-real.properties \
        -Dspring.profiles.active=real \
        -Dspring.config.name=application-real.properties \
        $JAR_NAME > $REPOSITORY/nohup.out Application 2>&1 &

#nohup 실행 시 CodeDeploy는 무한 대기합니다.
# 이 이슈를 해결하기 위해 nohup.out파일을 표준 입출력용으로 별도로 사용합니다.
# 이렇게 하지 않으면 nohup.out 파일이 생기지 않고, CodeDeploy로그에 표준 입출력이 출력됩니다.
# nohup이 끝나기 전까지 CodeDeploy도 끝나지 않으니 꼭 이렇게 해야만 합ㄴ다.
