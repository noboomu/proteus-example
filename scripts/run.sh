 #!/bin/bash

#  if [ -f './pid.file' ]; then
#     PID=`cat ./pid.file`
#     if [[ ! -z "$PID" ]]; then
#         echo "killing $PID"
#         pkill -KILL -P $PID
#         pkill -KILL -P $$
#     fi
#     rm pid.file
#  fi

# trap cleanup INT

# function cleanup {
#  echo "cleaning up"
#  if [ -f 'pid.file' ]; then
#     PID=`cat pid.file`
#     if [[ ! -z "$PID" ]]; then
#         echo "killing $PID"
#         pkill -KILL -P $PID

#     fi
#  fi
#  rm pid.file
#  pkill -KILL -P $$
# }

find src/main/java/ -name "*.java" | entr -r mvn compile process-classes exec:exec

# sleep 1
# echo -e "\n"