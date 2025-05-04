#!/bin/bash

# Settings
PATRONS=80
SWITCHTIME=3

# Quantum to use for RR
QUANTUM=75

# List of seeds
SEEDS=(101 102 103 104 105 106 107 108 109 110)

# For each seed, run FCFS (0), SJF (1), and RR (2)
for seed in "${SEEDS[@]}"
do
    echo "Running experiments for seed=$seed"

    # FCFS
    make run ARGS="$PATRONS 0 $SWITCHTIME 0 $seed"

    # SJF
    make run ARGS="$PATRONS 1 $SWITCHTIME 0 $seed"

    # RR
    make run ARGS="$PATRONS 2 $SWITCHTIME $QUANTUM $seed"
done
