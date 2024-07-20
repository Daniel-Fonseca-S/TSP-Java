package libs;

import java.text.DecimalFormat;

public class ThreadToRun extends Thread {
    private static double mutationProb;
    private static Integer populationSize;
    private static Integer citiesNumber;
    private static Integer[][] matrix;
    private static double trueOptimalSolution;
    private static double bestDistanceFinal;
    private static int[] bestPathFinal;
    private static Integer iterationsFinal;
    private static long timeFinal;

    public ThreadToRun(
            double mutProb,
            Integer populSize,
            Integer citSize,
            Integer[][] matrixReceive,
            double trueOptimalSolutionReceive
    ) {
        mutationProb = mutProb;
        populationSize = populSize;
        citiesNumber = citSize;
        matrix = matrixReceive;
        trueOptimalSolution = trueOptimalSolutionReceive;
    }

    public static void resetTime() {
        timeFinal = Long.MAX_VALUE;
    }

    public static double getBestDistanceFinal() {
        return bestDistanceFinal;
    }

    public static void setBestDistanceFinal(double bDistance) {
        bestDistanceFinal = bDistance;
    }

    public static int[] getBestPathFinal() {
        return bestPathFinal;
    }

    public static void setBestPathFinal(int[] bPath) {
        bestPathFinal = bPath;
    }

    public static Integer getIterationsFinal() {
        return iterationsFinal;
    }

    public static void setIterationsFinal(Integer it) {
        iterationsFinal = it;
    }

    public static long getFormattedTimeFinal() {
        return timeFinal;
    }

    public static void setFormattedTimeFinal(long timeFinalInsert) {
        timeFinal = timeFinalInsert;
    }

    public void run() {
        System.out.println("Thread started");
        long startTime = System.nanoTime();
        for (int i = 1; i <= 1_000_000_000; i++) {
            TSPSolver.mainTSPSolver(
                    trueOptimalSolution,
                    mutationProb,
                    populationSize,
                    citiesNumber,
                    matrix
            );
            if (TSPSolver.getBestDistance() <= trueOptimalSolution) {
                break;
            }

            if (System.nanoTime() - startTime >= 900_000_000_000L) {
                if (!TSPSolver.solutionFound) {
                    TSPSolver.resetTSPSolver();
                    startTime = System.nanoTime();
                    System.err.println("Thread restarting due to timeout with no results");
                } else {
                    System.err.println("Thread timed out");
                    break;
                }
            }
        }
        long totalTime = System.nanoTime() - startTime;

        if (getFormattedTimeFinal() == 0 || getFormattedTimeFinal() >= totalTime) {
            setBestDistanceFinal(TSPSolver.getBestDistance());
            setBestPathFinal(TSPSolver.getBestPath());
            setFormattedTimeFinal(TSPSolver.getTime());
            setIterationsFinal(TSPSolver.getIterations());
        }
        System.out.println(
                "Thread finished with distance: " + TSPSolver.getBestDistance() +
                        " and time: " + new DecimalFormat("#0.000").format((double) totalTime / 1_000_000_000) +
                        " seconds" + " and iterations: " + TSPSolver.getIterations()
        );
    }
}