 #!/bin/bash

find src/main/java/ -name "*.java" | entr -r mvn compile exec:exec
