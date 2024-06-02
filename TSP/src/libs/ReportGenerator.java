package libs;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    private static String fileName;
    private static String filePrefix;
    private static int citiesNumber;
    private static List<List<Integer>> entryMatrix;
    private static final List<Integer> convergedDistances = new ArrayList<>();
    private static final List<Double> convergedTimes = new ArrayList<>();
    private static final List<List<Integer>> convergedPaths = new ArrayList<>();
    private static final List<Integer> convergedFitnesses = new ArrayList<>();
    private static final List<Integer> convergedGenerations = new ArrayList<>();
    private static int executions;
    private static int threadsOrProcesses;
    private static double mutationProb;
    private static int optimalSolutions;
    private static double averageConvergedTime;
    private static double maxConvergedTime;
    private static double minConvergedTime;

    public static void setGeneralInfo(String filePrefix, int citiesNumber, List<List<Integer>> entryMatrix, int executions, int threadsOrProcesses, double mutationProb, int optimalSolutions) {
        ReportGenerator.filePrefix = filePrefix;
        ReportGenerator.citiesNumber = citiesNumber;
        ReportGenerator.entryMatrix = entryMatrix;
        ReportGenerator.executions = executions;
        ReportGenerator.threadsOrProcesses = threadsOrProcesses;
        ReportGenerator.mutationProb = mutationProb;
        ReportGenerator.optimalSolutions = optimalSolutions;
        ReportGenerator.fileName = filePrefix + "_report_" + ".txt";
    }

    public static void addConvergedInfo(int distance, long time, List<Integer> path, int fitness, int generation) {
        convergedDistances.add(distance);
        convergedTimes.add((double) time / 1_000_000_000);
        convergedPaths.add(path);
        convergedFitnesses.add(fitness);
        convergedGenerations.add(generation);
    }

    private static void calculateAverageMaxMinConvergedTime() {
        averageConvergedTime = convergedTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        maxConvergedTime = convergedTimes.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        minConvergedTime = convergedTimes.stream().mapToDouble(Double::doubleValue).min().orElse(0);
    }

    private static String generateReportContentJson() {
        JsonObjectBuilder reportContent = Json.createObjectBuilder();
        reportContent.add("numberOfCities", citiesNumber);
        reportContent.add("quantityOfThreadsOrProcesses", threadsOrProcesses);
        reportContent.add("mutationProbability", mutationProb);
        reportContent.add("quantityOfExecutions", executions);

        JsonArrayBuilder entryMatrixArray = Json.createArrayBuilder();
        for (List<Integer> row : entryMatrix) {
            JsonArrayBuilder rowArray = Json.createArrayBuilder();
            for (Integer value : row) {
                rowArray.add(value);
            }
            entryMatrixArray.add(rowArray);
        }
        reportContent.add("entryMatrix", entryMatrixArray);

        JsonArrayBuilder convergencesArray = Json.createArrayBuilder();
        for (int i = 0; i < convergedDistances.size(); i++) {
            JsonObjectBuilder convergence = Json.createObjectBuilder();
            convergence.add("distance", convergedDistances.get(i));
            convergence.add("time", convergedTimes.get(i));
            JsonArrayBuilder pathArray = Json.createArrayBuilder();
            for (Integer value : convergedPaths.get(i)) {
                pathArray.add(value);
            }
            convergence.add("path", pathArray);
            convergence.add("fitness", convergedFitnesses.get(i));
            convergence.add("generations", convergedGenerations.get(i));
            convergencesArray.add(convergence);
        }
        reportContent.add("convergences", convergencesArray);

        reportContent.add("quantityOfOptimalSolutions", optimalSolutions);
        reportContent.add("averageConvergedTime", averageConvergedTime);
        reportContent.add("maxConvergedTime", maxConvergedTime);
        reportContent.add("minConvergedTime", minConvergedTime);
        reportContent.add("totalTimeOfThreadsOrProcesses", calculateTotalParallelTime());

        return reportContent.build().toString();
    }

    public static void generateReport() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String today = dateFormat.format(System.currentTimeMillis());
        Path reports = Paths.get("reports/" + today);
        if (Files.notExists(reports)) {
            try {
                Files.createDirectory(reports);
            } catch (IOException e) {
                System.out.printf("Error to create the reports folder: %s\n", e.getMessage());
            }
        } else if (fileName.isEmpty()) {
            System.out.println("Error: No data was set to generate the report.");
            return;
        }

        calculateAverageMaxMinConvergedTime();
        try {
            Files.write(Paths.get("reports/" + today + "/" + fileName), generateReportContent().getBytes());
            Files.write(Paths.get("reports/" + today + "/" + fileName.replace(".txt", ".json")), generateReportContentJson().getBytes());
        } catch (IOException e) {
            System.out.printf("Error to write the report file: %s\n", e.getMessage());
        }
    }

    private static String generateReportContent() {
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("Detailed Report of the TSP Algorithm using ").append(filePrefix).append("\n\n");
        reportContent.append("Number of Cities: ").append(citiesNumber).append("\n");
        reportContent.append("Quantity of Threads/Processes: ").append(threadsOrProcesses).append("\n");
        reportContent.append("Mutation Probability: ").append(mutationProb).append("\n");
        reportContent.append("Quantity of Executions: ").append(executions).append("\n");
        reportContent.append("Entry Matrix:\n");
        for (List<Integer> row : entryMatrix){
            for (Integer value : row){
                reportContent.append(String.format("%04d | ", value));
            }
            reportContent.append("\n");
        }
        reportContent.append("\n");
        reportContent.append("TSP Results by Convergence:\n");
        for (int i = 0; i < convergedDistances.size(); i++) {
            reportContent.append("Convergence ").append(i + 1).append(":\n");
            reportContent.append("Distance: ").append(convergedDistances.get(i)).append("\n");
            reportContent.append("Time: ").append(convergedTimes.get(i)).append("\n");
            reportContent.append("Path: ").append(convergedPaths.get(i)).append("\n");
            reportContent.append("Fitness: ").append(convergedFitnesses.get(i)).append("\n");
            reportContent.append("Generations: ").append(convergedGenerations.get(i)).append("\n\n");
        }
        reportContent.append("Quantity of Optimal Solutions: ").append(optimalSolutions).append("\n");
        reportContent.append("Average Converged Time: ").append(averageConvergedTime).append("\n");
        reportContent.append("Max Converged Time: ").append(maxConvergedTime).append("\n");
        reportContent.append("Min Converged Time: ").append(minConvergedTime).append("\n");
        reportContent.append("Total Time of Threads/Processes: ").append(calculateTotalParallelTime()).append("\n\n");
        reportContent.append("End of Report");
        System.out.printf("\nReport generated in the reports folder with the name: %s\n", fileName);
        return reportContent.toString();
    }

    public static double calculateTotalParallelTime() {
        double totalThreadTime = 0;
        for (double time : convergedTimes) {
            totalThreadTime += time;
        }
        return totalThreadTime;
    }
}