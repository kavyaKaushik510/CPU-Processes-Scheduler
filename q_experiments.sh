#!/bin/bash

# Compile once
make

# Fixed parameters
numPatron=80
schedAlg=2
switchTime=3
seed=42

# Sweep over q values
for q in 10 20 30 40 50 75 100 125 150 175 200
do
  echo "===== Running with quantum = $q ====="
  make run ARGS="$numPatron $schedAlg $switchTime $q $seed"
done
