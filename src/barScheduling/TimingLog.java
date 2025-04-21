package barScheduling;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TimingLog {

    private static PrintWriter writer;
    private static String filename = "all_results.csv";

    public static void init(int patrons, int schedAlg, int q, int switchTime, long seed) throws IOException {
        writer = new PrintWriter(new FileWriter(filename, true)); //append to file

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

        writer.println();
        writer.printf("# Run: Patrons=%d, Scheduler=%s, Quantum=%s, Switch=%d, Seed=%d%n", patrons, scheduler, qDisplay, switchTime, seed);
        writer.println("PatronID,ResponseTime,TotalWaitingTime,TurnaroundTime");
        writer.flush();
    }

    public static synchronized void logPatronMetrics(int patronId, long responseTime, long totalWaitingTime, long turnaroundTime) {
        writer.printf("%d,%d,%d,%d%n", patronId, responseTime, totalWaitingTime, turnaroundTime);
        writer.flush();
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}