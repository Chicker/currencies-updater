FROM adoptopenjdk/openjdk11:latest

COPY currencies-updater/target/universal/stage/lib lib
COPY currencies-updater/target/universal/stage/bin bin

ENTRYPOINT [ "/bin/currencies-updater" ]