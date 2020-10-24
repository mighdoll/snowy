FROM openjdk:14

# Env variables
ENV SCALA_VERSION 2.12.12
ENV MILL_VERSION 0.8.0

RUN \
  curl -L -o /usr/local/bin/mill https://github.com/lihaoyi/mill/releases/download/$MILL_VERSION/$MILL_VERSION && \
  chmod +x /usr/local/bin/mill

WORKDIR snowy
ADD . .

RUN mill server.assembly

FROM openjdk:14
COPY --from=0 /snowy/out/server/assembly/dest/out.jar out.jar
EXPOSE 9000
CMD java -jar out.jar
