import libs.AJE;
import libs.TSPSolver;

import java.util.Scanner;

public class Main {
    private static String fileName;
    private static Integer threadsNumber;
    private static Integer numberOfConvergences;
    private static Integer populationNumber;
    private static float mutationProb;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while (true) {
                String status = requestParams(scanner);
                switch (status) {
                    case "over", "start":
                        break label;
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String requestParams(Scanner scanner) {
        resetParams();
        System.out.print("\n(type 'exit' to leave) > ");

        String inputParams = scanner.nextLine();
        String[] params = inputParams.split("\\s+");

        if (params[0].equalsIgnoreCase("exit")) {
            System.out.println("Program Finished.");
            return "over";
        }

        if (params.length == 5 && Float.parseFloat(params[4]) <= 1) {
            fileName = params[0];
            threadsNumber = Integer.valueOf(params[1]);
            numberOfConvergences = Integer.valueOf(params[2]);
            populationNumber = Integer.valueOf(params[3]);
            mutationProb = Float.parseFloat(params[4]);

            startProgram();

            return "start";
        } else {
            System.out.println("> <fileName> <threadsNumber> <numberOfConvergences> <populationNumber> <mutationProb> (max: 1 (100%))");
            return "repeat";
        }
    }

    private static void resetParams() {
        AJE.resetAJE();
        TSPSolver.resetTSPSolver();
    }

    private static void showInfoInit() {
        System.out.println("\n--== General Informations ==--");
        System.out.println("File: " + fileName);
        System.out.println("Number of Threads: " + threadsNumber);
        System.out.println("Number of Convergences: " + numberOfConvergences);
        System.out.println("Population: " + populationNumber);
        System.out.println("Mutation Probability: ( " + (mutationProb * 100) + "% )");
    }

    private static void startProgram() {
        long startTime = System.nanoTime();
        showInfoInit();
        AJE.readFromFile(fileName);
        AJE.start(mutationProb, populationNumber, numberOfConvergences, threadsNumber, startTime);
        System.exit(0);
    }
}
