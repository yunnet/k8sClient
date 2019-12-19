#!/usr/bin/env bash
set -eu -o pipefail

#source _AWS/credentials/aws_env_params
#echo "Exported AWS access token:" $AWS_TOKEN

#Build java backend micro-service
mvn clean install

#Start java backend micro-service
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
