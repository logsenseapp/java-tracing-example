FROM maven

COPY . /src
WORKDIR /src
RUN mkdir -p /build

RUN mvn install -Pdockerization
RUN mvn assembly:single -Pdockerization