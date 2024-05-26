package libs;

import java.text.DecimalFormat;

public class ThreadToRun extends Thread{
    private final double timePerThread;
    private static double mutationProb;
    private static Integer populationSize;
    private static Integer citiesNumber;
    private static Integer[][] matrix;

    public ThreadToRun(
            double timePerThread,
            double mutProb,
            Integer populSize,
            Integer citSize,
            Integer[][] matrixReceive
    ) {
        this.timePerThread = timePerThread;
        mutationProb = mutProb;
        populationSize = populSize;
        citiesNumber = citSize;
        matrix = matrixReceive;
    }

    public void run() {
        System.out.println("Thread started");
        long startTime = System.nanoTime();
        long timeToExec = (long) (timePerThread * 1_000_000_000L);
        for (int i = 1; i <= 1_000_000_000; i++) {
            TSPSolver.mainTSPSolver(
                    (long) timePerThread,
                    mutationProb,
                    populationSize,
                    citiesNumber,
                    matrix
            );
            if ((System.nanoTime() - startTime) >= timeToExec) {
                break;
            }
        }

        setBestDistanceFinal(TSPSolver.getBestDistance());
        setBestPathFinal(TSPSolver.getBestPath());
        setIterationsFinal(TSPSolver.getIterations());
        setFormattedTimeFinal(TSPSolver.getTime());
        System.out.println(
                "Thread finished with distance: " + TSPSolver.getBestDistance() +
                " and time: " + new DecimalFormat("#0.000").format((double) TSPSolver.getTime() / 1_000_000_000)
        );
    }

    private static double bestDistanceFinal;
    private static int[] bestPathFinal;
    private static Integer iterationsFinal;
    private static long timeFinal;

    public static void setBestDistanceFinal(double bDistance) {
        bestDistanceFinal = bDistance;
    }

    public static double getBestDistanceFinal() {
        return bestDistanceFinal;
    }

    public static void setBestPathFinal(int[] bPath) {
        bestPathFinal = bPath;
    }

    public static int[] getBestPathFinal() {
        return bestPathFinal;
    }

    public static void setIterationsFinal(Integer it) {
        iterationsFinal = it;
    }

    public static Integer getIterationsFinal() {
        return iterationsFinal;
    }

    public static void setFormattedTimeFinal(long timeFinalInsert) {
        timeFinal = timeFinalInsert;
    }

    public static long getFormattedTimeFinal() {
        return timeFinal;
    }
}