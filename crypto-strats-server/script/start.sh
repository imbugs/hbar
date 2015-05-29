#!/bin/sh -e
cd ..
export MAVEN_OPTS="-Djavax.net.ssl.trustStore=./certs/jssecacerts -Dlog4j.configuration=file:./src/main/resources/log4j2.xml -Xmx8192m -XX:MaxPermSize=1024m"
mvn install vertx:runMod
