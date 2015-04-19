#!/bin/sh -e
cd ..
export MAVEN_OPTS="-Djavax.net.ssl.trustStore=./certs/jssecacerts"
mvn install vertx:runMod