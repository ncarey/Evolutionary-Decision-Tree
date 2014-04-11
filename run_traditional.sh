#!/bin/bash

export DECISION_TREE_HOME=$(pwd)
#echo $DECISION_TREE_HOME

cd src; make run$1$2
