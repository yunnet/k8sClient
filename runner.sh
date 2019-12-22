#!/usr/bin/env bash
set -e

MODE=$1
K8S_MASTER=$2

test() {
    #Test java backend micro-service
    mvn clean test
}

start() {
    export KUBERNETES_MASTER="$1"

    #Build java backend micro-service
    mvn clean install -DskipTests

    #Start java backend micro-service
    java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
}

case "$MODE" in
    test)
        test
        ;;
    start)
        if [[ -z $K8S_MASTER ]]
        then
              echo "Please provide K8S master url"
        else
              start $K8S_MASTER
        fi
        ;;
    *)
        echo "Use commands to explicitly specify upload environment: ./runner.sh [test|start]"
        ;;
esac
