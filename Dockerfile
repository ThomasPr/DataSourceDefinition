FROM azul/zulu-openjdk-alpine:8
USER root
ADD target/demo-microbundle.jar demo.jar
EXPOSE 8080
ENTRYPOINT java -jar demo.jar
