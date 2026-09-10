# Native build — compiles to a standalone GraalVM/Mandrel binary (no JVM at runtime).
# For the JVM-based image, see Dockerfile.jvm.
FROM maven:3.9.8 AS maven

FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 AS build

COPY --from=maven /usr/share/maven /opt/maven
ENV PATH="/opt/maven/bin:${PATH}"

USER root
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
RUN mvn package -Dnative -DskipTests \
    -Dquarkus.native.native-image-xmx=6g \
    -Dquarkus.native.additional-build-args=-J-XX:ParallelGCThreads=1,-J-XX:CICompilerCount=2

FROM quay.io/quarkus/quarkus-micro-image:2.0

WORKDIR /work/

RUN chown 1001 /work && chmod "g+rwX" /work && chown 1001:root /work

COPY --from=build --chown=1001:root /usr/src/app/assembly/application-assembly/target/*-runner /work/application

EXPOSE 8080
USER 1001

ENTRYPOINT ["/work/application", "-Dquarkus.http.host=0.0.0.0"]
