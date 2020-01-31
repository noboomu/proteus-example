#!/bin/bash
  
set -e
  
git clone https://github.com/noboomu/proteus-example.git
cd proteus-example
mvn package exec:exec
