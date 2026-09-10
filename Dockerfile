FROM maven:3.9.8 AS build

WORKDIR /usr/src/app

COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY Connectors/pom.xml Connectors/pom.xml
COPY Connectors/fischl/pom.xml Connectors/fischl/pom.xml
COPY Connectors/fischl/fischl-api/pom.xml Connectors/fischl/fischl-api/pom.xml
COPY Connectors/fischl/fischl-utilities/pom.xml Connectors/fischl/fischl-utilities/pom.xml
COPY Connectors/fischl/fischl-identity-impl/pom.xml Connectors/fischl/fischl-identity-impl/pom.xml
COPY Connectors/fischl/fischl-bastion-impl/pom.xml Connectors/fischl/fischl-bastion-impl/pom.xml
COPY Connectors/fischl/fischl-facilities-impl/pom.xml Connectors/fischl/fischl-facilities-impl/pom.xml
COPY Connectors/fischl/fischl-newspapers-impl/pom.xml Connectors/fischl/fischl-newspapers-impl/pom.xml
COPY Connectors/fischl/fischl-defender-impl/pom.xml Connectors/fischl/fischl-defender-impl/pom.xml
COPY Connectors/fischl/fischl-hireling-impl/pom.xml Connectors/fischl/fischl-hireling-impl/pom.xml
COPY MauraBastions/pom.xml MauraBastions/pom.xml
COPY MauraBastions/maurabastions-api/pom.xml MauraBastions/maurabastions-api/pom.xml
COPY MauraBastions/maurabastions-impl/pom.xml MauraBastions/maurabastions-impl/pom.xml
COPY MauraBastions/maurabastions-adapter/pom.xml MauraBastions/maurabastions-adapter/pom.xml
COPY Tsaritsa/pom.xml Tsaritsa/pom.xml
COPY Tsaritsa/tsaritsa-api/pom.xml Tsaritsa/tsaritsa-api/pom.xml
COPY Tsaritsa/tsaritsa-impl/pom.xml Tsaritsa/tsaritsa-impl/pom.xml
COPY Tsaritsa/tsaritsa-adapter/pom.xml Tsaritsa/tsaritsa-adapter/pom.xml
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
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -XX:MaxRAMPercentage=50 -XX:MaxMetaspaceSize=128m -XX:ReservedCodeCacheSize=64m -XX:+UseSerialGC -Xss1024k"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "/opt/jboss/container/java/run/run-java.sh" ]
