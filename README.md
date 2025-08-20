# CSC3002F Assignment 2 – Processes Scheduler

This project simulates a CPU scheduling system using the metaphor of a **barman** serving **patrons**. Each scheduling algorithm mimics how the CPU would process incoming jobs. The three algorithms implemented and compared are:

- First-Come-First-Serve (FCFS)
- Shortest Job First (SJF) – non-preemptive
- Round Robin (RR)

The program evaluates performance based on several operating system metrics and visualizes the results for analysis.

## Project Objectives

- Simulate FCFS, SJF (non-preemptive), and RR scheduling algorithms
- Evaluate performance using key metrics
- Compare algorithm efficiency and fairness
- Tune Round Robin using varying quantum values
- Demonstrate results across multiple experiment runs

## Key Metrics

| Metric           | Description                                                                 |
|------------------|-----------------------------------------------------------------------------|
| Response Time    | Time from patron’s first order to first drink received                      |
| Waiting Time     | Total time a patron waits before each drink is prepared                     |
| Turnaround Time  | Time from first order to last drink consumed                                |
| CPU Utilisation  | Time CPU (barman) spends actively working, relative to total time           |
| Throughput       | Number of patrons served per 2000ms window                                  |

## File Structure

```
.
├── Patron.java                 # Handles individual patron behaviour and timestamps
├── Barman.java                 # Simulates CPU behaviour including context switches
├── TimingLog.java              # Logs all metrics and writes to CSV files
├── run_experiments.sh         # Runs experiments across seeds and algorithms
├── q_experiments.sh           # Tests Round Robin with varying quantum values
├── Makefile                   # Automates compilation and execution
├── experiments.csv            # Logs metrics per patron per run
├── experiments_summary.csv    # Summarises averages, CPU utilisation, throughput
├── graphs/                    # Contains visual outputs of metric comparisons
```

---

## How to Compile and Run

This project uses a `Makefile` to simplify execution.

### Run a Single Simulation

```bash
make run ARGS="<num of patrons> <scheduling algorithm> <context switch time> <seed>"
```

### Example

```bash
make run ARGS="80 2 3 30"
```

Where:
- `num of patrons` = total number of patrons (e.g., 80)
- `scheduling algorithm`:
  - `0` = FCFS
  - `1` = SJF
  - `2` = Round Robin
- `context switch time` = time (in ms) added between switching patrons (e.g., 3)
- `seed` = random seed for reproducible job times

### Run All Algorithms Across Seeds

```bash
./run_experiments.sh
```

### Run Round Robin with Different Quantum Values

```bash
./q_experiments.sh
```

---

## Output Files

- `experiments.csv` – detailed metrics per patron (response, wait, turnaround time)
- `experiments_summary.csv` – average metrics + CPU utilisation + throughput per run
- `graphs/` – contains all generated plots from the experiments

---

## Performance Highlights

- **RR** offers the best fairness and balance across metrics
- **SJF** delivers shortest average turnaround/waiting time but risks starvation
- **FCFS** is predictable and stable but inefficient for long queues
- Optimal RR quantum value: `75 ms`
- Context switch time used: `3 ms`

## Author

Kavya Kaushik  
University of Cape Town – CSC3002F  
Student Number: KSHKAV001

## License

This project was developed for academic use in CSC3002F – Operating Systems.  
All content © 2025, for educational purposes only.


