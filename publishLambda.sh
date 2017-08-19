#!/bin/bash

LAMBDA_NAME=bb-store
IMAGE_KEY=bb/lambda/store-0.1.jar
JAR_PATH=/mnt/c/dev/bb/store/target/scala-2.12/bb-store-assembly-0.1-SNAPSHOT.jar

main() {
    build
    if [[ $(publish) ]] 
        then updateLambda
    fi
}

build() {
    sbt store/assembly
    #should get paths and info back from sbt here
    #...
}

publish() {
    aws s3api put-object \
        --bucket $IMAGE_BUCKET \
        --key $IMAGE_KEY \
        --body $JAR_PATH
}

updateLambda() {
    aws lambda update-function-code \
        --function-name $LAMBDA_NAME \
        --s3-bucket $IMAGE_BUCKET \
        --s3-key $IMAGE_KEY
}

main

