FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar
ADD ${JAR_FILE} app.jar
#타임존 설정, 무작위 소스 지정, 프로파일 설정(application-dev.yml, 추후 생성)
ENTRYPOINT ["java", "-Duser.timezone=GMT+9", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=dev", "-jar", "/app.jar"]