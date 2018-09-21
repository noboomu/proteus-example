#!/bin/bash
  
set -e
  
git clone git@github.com:noboomu/proteus-example.git
cd proteus-example
mvn package exec:exec