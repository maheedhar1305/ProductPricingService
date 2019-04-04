#!/bin/bash

set -e

SERVICE_VERSION="1.0.0"

JAVA_OPTS=${JAVA_OPTS:="-DVersion=$SERVICE_VERSION -XX:MaxRAMFraction=1 -XX:MaxRAM=`expr $(cat /sys/fs/cgroup/memory/memory.limit_in_bytes) / 10 \* 7`"}

exec java $JAVA_OPTS -jar ProductPricingService.jar
