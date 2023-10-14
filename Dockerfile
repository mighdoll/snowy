FROM openjdk:21

# Env variables
ENV SCALA_VERSION 3.3.1
ENV MILL_VERSION 0.11.5

RUN \
  curl -L -o /usr/local/bin/mill https://github.com/lihaoyi/mill/releases/download/$MILL_VERSION/$MILL_VERSION && \
  chmod +x /usr/local/bin/mill

WORKDIR snowy
ADD . .

RUN mill server.assembly

FROM openjdk:21
COPY --from=0 /snowy/out/server/assembly/dest/out.jar out.jar
EXPOSE 9000
CMD java -jar out.jar
