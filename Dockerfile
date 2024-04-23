FROM java:openjdk-8u111-jdk-alpine

WORKDIR /app

COPY config.xml /app
COPY target/ModbusSimulator.jar /app
COPY target/lib /app/lib

CMD ["java", "-jar", "ModbusSimulator.jar"]