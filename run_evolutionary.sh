#!/bin/bash

export DECISION_TREE_HOME=$(pwd)
#echo $DECISION_TREE_HOME

cd src; make evorun$1$2
