package barScheduling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimingLog {

    private static PrintWriter metricsWriter;
    private static String metricsFile= "experiments.csv";
    private static String summaryFile = "experiment_summary.csv";
    

    //Experiment Details
    private static long simDuration = 0;
    private static int currentPatronCount;
    private static long totalDrinkPrepTime = 0;
    private static String currentRunInfo;
    private static int totalNumberOfdrinks = 0;

    //Store all values for experiment summary
    private static List<Long> turnaroundList = new ArrayList<>();
    private static List<Long> waitingList = new ArrayList<>();
    private static List<Long> responseList = new ArrayList<>();

    private static final List<Long> patronFinishTimes = new ArrayList<>();
    private static final int WINDOW_SIZE = 500; // in ms



    public static void init(int patrons, int schedAlg, int q, int switchTime, long seed) throws IOException {
        metricsWriter = new PrintWriter(new FileWriter(metricsFile, true)); 

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
        currentRunInfo = String.format("%s,%s,%d,%d,%d", scheduler, qDisplay, patrons, switchTime, seed);
        totalNumberOfdrinks = patrons * 5; // Fixed value of 5 drinks ordered by each patron

        metricsWriter.println();
        metricsWriter.printf("# Run: Patrons=%d, Scheduler=%s, Quantum=%s, Switch=%d, Seed=%d%n", patrons, scheduler, qDisplay, switchTime, seed);
        metricsWriter.println("PatronID,ResponseTime,TotalWaitingTime,TurnaroundTime");
        metricsWriter.flush();
    }

    public static synchronized void logPatronMetrics(int patronId, long responseTime, long totalWaitingTime, long turnaroundTime) {
        metricsWriter.printf("%d,%d,%d,%d%n", patronId, responseTime, totalWaitingTime, turnaroundTime);
        metricsWriter.flush();

        responseList.add(responseTime);
        waitingList.add(totalWaitingTime);
        turnaroundList.add(turnaroundTime);
    }

    public static void recordSimulationDuration(long simulationDuration) {
        simDuration = simulationDuration;
    }

    public static void logDrinkPreparationTime(long prepDuration){
        totalDrinkPrepTime += prepDuration;
    }

    public static int getTotalDrinkPrepTime() {
        return (int) totalDrinkPrepTime; 
    }

    public static synchronized void logPatronFinishTime(long timestamp) {
        patronFinishTimes.add(timestamp);
    }

    //Write the throughput of patrons by time window
    private static void writeThroughputByWindow() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("throughput_windows.csv", true))) {
            writer.printf("# %s%n", currentRunInfo);
            if (patronFinishTimes.isEmpty()) return;
    
            Collections.sort(patronFinishTimes);
            long simStart = patronFinishTimes.get(0);
            long simEnd = patronFinishTimes.get(patronFinishTimes.size() - 1);
            long windowStart = simStart;
    
            int index = 0;
            while (windowStart <= simEnd) {
                long windowEnd = windowStart + WINDOW_SIZE;
                int count = 0;
    
                while (index < patronFinishTimes.size() && patronFinishTimes.get(index) < windowEnd) {
                    count++;
                    index++;
                }
    
                writer.printf("%d,%d\n", windowStart - simStart, count); // time since start, patrons completed
                windowStart = windowEnd;
            }
        }
    }
    

    public static void writeSummaryStats() throws IOException {
        try (PrintWriter summaryWriter = new PrintWriter(new FileWriter(summaryFile, true))) {
            File file = new File(summaryFile);
            if (file.length() == 0) {
                summaryWriter.println("Scheduler,Quantum,Patrons,SwitchTime,Seed," +
                        "AvgTAT,MedTAT,StdTAT," +
                        "AvgWait,MedWait,StdWait," +
                        "AvgResp,MedResp,StdResp," +
                        "CPUUtilization");
            }

            double cpuUtil = (simDuration > 0) ? (totalDrinkPrepTime * 100.0) / simDuration : 0;
            
            summaryWriter.printf("%s,%s,%s,%s,%.2f%n",
            currentRunInfo,
            formatStats(turnaroundList),
            formatStats(waitingList),
            formatStats(responseList),
            cpuUtil);
        }
    
    }

    private static String formatStats(List<Long> list) {
        if (list.isEmpty()) return "0,0,0";
        double avg = list.stream().mapToLong(Long::longValue).average().orElse(0);
        double std = Math.sqrt(list.stream().mapToDouble(v -> Math.pow(v - avg, 2)).sum() / list.size());
        long med = getMedian(list);
        return String.format("%.2f,%d,%.2f", avg, med, std);
    }

        private static long getMedian(List<Long> list) {
        List<Long> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        int mid = sorted.size() / 2;
        return (sorted.size() % 2 == 0) ? (sorted.get(mid - 1) + sorted.get(mid)) / 2 : sorted.get(mid);
    }


    public static void close() {
        if (metricsWriter != null) metricsWriter.close();
        try {
            writeSummaryStats();
            writeThroughputByWindow();
        } catch (IOException e) {
            System.err.println("Failed to write summary stats: " + e.getMessage());
        }
        responseList.clear();
        waitingList.clear();
        turnaroundList.clear();
        totalDrinkPrepTime = 0;
    }

    
}