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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        ReportGenerator.fileName = filePrefix + "_report_" + dateFormat.format(System.currentTimeMillis()) + ".txt";
    }

    public static void addConvergedInfo(int distance, long time, List<Integer> path, int fitness, int generation) {
        convergedDistances.add(distance);
        convergedTimes.add((double) time / 1_000_000_000);
        convergedPaths.add(path);
        convergedFitnesses.add(fitness);
        convergedGenerations.add(generation);
    }

    private static void calculateAverageMaxMinConvergedTime() {
        ReportGenerator.averageConvergedTime = convergedTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        ReportGenerator.maxConvergedTime = convergedTimes.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        ReportGenerator.minConvergedTime = convergedTimes.stream().mapToDouble(Double::doubleValue).min().orElse(0);
    }

    private static String generateReportContentJson() {
        JsonObjectBuilder reportContent = Json.createObjectBuilder();
        reportContent.add("numberOfCities", ReportGenerator.citiesNumber);
        reportContent.add("quantityOfThreadsOrProcesses", ReportGenerator.threadsOrProcesses);
        reportContent.add("mutationProbability", ReportGenerator.mutationProb);
        reportContent.add("quantityOfExecutions", ReportGenerator.executions);

        JsonArrayBuilder entryMatrixArray = Json.createArrayBuilder();
        for (List<Integer> row : ReportGenerator.entryMatrix) {
            JsonArrayBuilder rowArray = Json.createArrayBuilder();
            for (Integer value : row) {
                rowArray.add(value);
            }
            entryMatrixArray.add(rowArray);
        }
        reportContent.add("entryMatrix", entryMatrixArray);

        JsonArrayBuilder convergencesArray = Json.createArrayBuilder();
        for (int i = 0; i < ReportGenerator.convergedDistances.size(); i++) {
            JsonObjectBuilder convergence = Json.createObjectBuilder();
            convergence.add("distance", ReportGenerator.convergedDistances.get(i));
            convergence.add("time", ReportGenerator.convergedTimes.get(i));
            convergence.add("path", ReportGenerator.convergedPaths.get(i).toString());
            convergence.add("fitness", ReportGenerator.convergedFitnesses.get(i));
            convergence.add("generations", ReportGenerator.convergedGenerations.get(i));
            convergencesArray.add(convergence);
        }
        reportContent.add("convergences", convergencesArray);

        reportContent.add("quantityOfOptimalSolutions", ReportGenerator.optimalSolutions);
        reportContent.add("averageConvergedTime", ReportGenerator.averageConvergedTime);
        reportContent.add("maxConvergedTime", ReportGenerator.maxConvergedTime);
        reportContent.add("minConvergedTime", ReportGenerator.minConvergedTime);

        return reportContent.build().toString();
    }

    public static void generateReport() {
        Path reports = Paths.get("reports");
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
            Files.write(Paths.get("reports/" + fileName), generateReportContent().getBytes());
            Files.write(Paths.get("reports/" + fileName.replace(".txt", ".json")), generateReportContentJson().getBytes());
        } catch (IOException e) {
            System.out.printf("Error to write the report file: %s\n", e.getMessage());
        }
    }

    private static String generateReportContent() {
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("Detailed Report of the TSP Algorithm using").append(ReportGenerator.filePrefix).append("\n\n");
        reportContent.append("Number of Cities: ").append(ReportGenerator.citiesNumber).append("\n");
        reportContent.append("Quantity of Threads/Processes: ").append(ReportGenerator.threadsOrProcesses).append("\n");
        reportContent.append("Mutation Probability: ").append(ReportGenerator.mutationProb).append("\n");
        reportContent.append("Quantity of Executions: ").append(ReportGenerator.executions).append("\n\n");
        reportContent.append("Entry Matrix:\n");
        for (List<Integer> row : ReportGenerator.entryMatrix){
            for (Integer value : row){
                reportContent.append(String.format("%02d | ", value));
            }
        }
        reportContent.append("\n");
        reportContent.append("TSP Results by Convergence:\n");
        for (int i = 0; i < ReportGenerator.convergedDistances.size(); i++) {
            reportContent.append("Convergence ").append(i + 1).append(":\n");
            reportContent.append("Distance: ").append(ReportGenerator.convergedDistances.get(i)).append("\n");
            reportContent.append("Time: ").append(ReportGenerator.convergedTimes.get(i)).append("\n");
            reportContent.append("Path: ").append(ReportGenerator.convergedPaths.get(i)).append("\n");
            reportContent.append("Fitness: ").append(ReportGenerator.convergedFitnesses.get(i)).append("\n");
            reportContent.append("Generations: ").append(ReportGenerator.convergedGenerations.get(i)).append("\n\n");
        }
        reportContent.append("Quantity of Optimal Solutions: ").append(ReportGenerator.optimalSolutions).append("\n\n");
        reportContent.append("Average Converged Time: ").append(ReportGenerator.averageConvergedTime).append("\n");
        reportContent.append("Max Converged Time: ").append(ReportGenerator.maxConvergedTime).append("\n");
        reportContent.append("Min Converged Time: ").append(ReportGenerator.minConvergedTime).append("\n\n");
        reportContent.append("End of Report");
        System.out.printf("Report generated in the reports folder with the name: %s\n", ReportGenerator.fileName);
        return reportContent.toString();
    }
}