#!/bin/bash
export JAVA_OPTS="-Xmx512m -Dlogback.configurationFile=`pwd`/src/main/resources/logback-local.xml"
sbt universal:stage && ./target/universal/stage/bin/tapir
