#!/bin/bash
set -x

scalapbc ./src/main/protobuf/v100.proto --scala_out=../scala
