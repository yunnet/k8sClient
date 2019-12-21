#!/usr/bin/env bash
set -eu -o pipefail

#Build java backend micro-service
mvn clean install -DskipTests

#Start java backend micro-service
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
