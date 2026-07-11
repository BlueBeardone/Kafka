FROM maven:3.9.8 AS build

WORKDIR /usr/src/app

COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY connectors/pom.xml connectors/pom.xml
COPY connectors/fischl/pom.xml connectors/fischl/pom.xml
COPY connectors/fischl/fischl-api/pom.xml connectors/fischl/fischl-api/pom.xml
COPY connectors/fischl/fischl-utilities/pom.xml connectors/fischl/fischl-utilities/pom.xml
COPY connectors/fischl/fischl-identity-impl/pom.xml connectors/fischl/fischl-identity-impl/pom.xml
COPY connectors/fischl/fischl-bastion-impl/pom.xml connectors/fischl/fischl-bastion-impl/pom.xml
COPY connectors/fischl/fischl-facilities-impl/pom.xml connectors/fischl/fischl-facilities-impl/pom.xml
COPY connectors/fischl/fischl-newspapers-impl/pom.xml connectors/fischl/fischl-newspapers-impl/pom.xml
COPY maurabastions/pom.xml maurabastions/pom.xml
COPY maurabastions/maurabastions-api/pom.xml maurabastions/maurabastions-api/pom.xml
COPY maurabastions/maurabastions-impl/pom.xml maurabastions/maurabastions-impl/pom.xml
COPY maurabastions/maurabastions-adapter/pom.xml maurabastions/maurabastions-adapter/pom.xml
COPY assembly/pom.xml assembly/pom.xml
COPY assembly/application-assembly/pom.xml assembly/application-assembly/pom.xml

RUN mvn dependency:go-offline -T 1C -q

COPY . .
RUN mvn install -T 1C -DskipTests

FROM registry.access.redhat.com/ubi8/openjdk-21:1.20

ENV LANGUAGE='en_US:en'

COPY --from=build --chown=185 /usr/src/app/assembly/application-assembly/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 /usr/src/app/assembly/application-assembly/target/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 /usr/src/app/assembly/application-assembly/target/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 /usr/src/app/assembly/application-assembly/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
