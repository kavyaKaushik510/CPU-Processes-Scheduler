# Processes-Scheduler

# File structure

Graphs folder - contains all graphs
experiments.csv - contains metrics per patron for the algorithms
experiments_summary.csv - contains summary of the patron metrics for each run along with CPU utilisation and throughput. 

# Run command

make run ARGS="<num of patrons> <Scheduling algorithm> <switch time> <seed>

# Example command
make run ARGS="80 2 3 30"

# Shell scripts for experimentation

q_experiments.sh  # Run RR across different quantum values
run_experiments.sh # Run all three scheduling algorithms with the same seed over a range of different seeds. 

