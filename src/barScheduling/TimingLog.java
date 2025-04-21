package barScheduling;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TimingLog {

    private static PrintWriter metricsWriter;
    private static PrintWriter summaryWriter;
    private static String metricsFile= "experiments.csv";
    private static String summaryFile = "experiment_summary.csv";
    

    //Experiment Details
    private static long simDuration = 0;
    private static int currentPatronCount;


    public static void init(int patrons, int schedAlg, int q, int switchTime, long seed) throws IOException {
        metricsWriter = new PrintWriter(new FileWriter(metricsFile, true)); 
        summaryWriter = new PrintWriter(new FileWriter(summaryFile, true));

        String scheduler;
        switch (schedAlg) {
            case 0:
                scheduler = "FCFS";
                break;
            case 1:
                scheduler = "SJF";
                break;
            case 2:
                scheduler = "RR";
                break;
            default:
                scheduler = "Unknown";
                break;
        }



        String qDisplay = (schedAlg == 2) ? String.valueOf(q) : "N/A";

        metricsWriter.println();
        metricsWriter.printf("# Run: Patrons=%d, Scheduler=%s, Quantum=%s, Switch=%d, Seed=%d%n", patrons, scheduler, qDisplay, switchTime, seed);
        metricsWriter.println("PatronID,ResponseTime,TotalWaitingTime,TurnaroundTime");
        metricsWriter.flush();
    }

    public static synchronized void logPatronMetrics(int patronId, long responseTime, long totalWaitingTime, long turnaroundTime) {
        metricsWriter.printf("%d,%d,%d,%d%n", patronId, responseTime, totalWaitingTime, turnaroundTime);
        metricsWriter.flush();


    }

    public static void recordSimulationDuration(long simulationDuration) {
        simDuration = simulationDuration;
    }

    public static void close() {
        if (metricsWriter != null) {
            metricsWriter.close();
        }
    }
}