#!/bin/bash

if [ -f 'pid.file' ]; then
    PID=`cat pid.file`
    if [[ ! -z "$PID" ]]; then
        echo "killing $PID"
        kill -- -$PID 
        pkill -KILL -P $PID
    fi
    echo "killing $PID 2"
    rm pid.file
fi


set -xe 
mvn exec:exec &
echo $! > pid.file
