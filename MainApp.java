package Project1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;

public class MainApp {

    // Functional interface alias to represent any sort method: (Movie[], Comparator<Movie>) -> SortMetrics
    @FunctionalInterface
    public interface SortAlgorithm {
        SortMetrics sort(Movie[] movies, Comparator<Movie> comp);
    }

    /**
     * Helper method to run a dual test (small batch preview + full dataset metrics)
     * for any given sorting algorithm.
     */
    public static void runSortTest(String sortName, SortAlgorithm sorter, Movie[] masterDataset, Comparator<Movie> comp) {
        System.out.println("==================================================");
        System.out.println(" TESTING: " + sortName.toUpperCase());
        System.out.println("==================================================");

        // 1. SMALL BATCH PREVIEW (10 Items)
        int smallSize = 10;
        Movie[] smallBatch = Arrays.copyOf(masterDataset, Math.min(smallSize, masterDataset.length));

        System.out.println("--- Small Batch Preview (Before Sort) ---");
        for (Movie m : smallBatch) {
            System.out.println("  " + m);
        }

        // Run sort on small batch
        SortMetrics smallMetrics = sorter.sort(smallBatch, comp);

        System.out.println("\n--- Small Batch Preview (After Sort) ---");
        for (Movie m : smallBatch) {
            System.out.println("  " + m);
        }
        System.out.println();
        smallMetrics.printSummary(sortName + " (Small Batch)", smallBatch.length);

        // 2. FULL DATASET TEST
        System.out.println("\n--- Full Dataset Performance Test ---");
        Movie[] fullBatch = masterDataset.clone();

        SortMetrics fullMetrics = sorter.sort(fullBatch, comp);
        fullMetrics.printSummary(sortName + " (Full Dataset)", fullBatch.length);

        System.out.println("==================================================\n");
    }

    public static void main(String[] args) {
        String filePath = "movies.csv";
        Movie[] masterDataset = loadData(filePath);

        System.out.println("Loaded " + masterDataset.length + " movies.\n");

        // Set up custom comparators
        Comparator<Movie> ratingComp = Movie.ByRatingDesc();
        Comparator<Movie> yearComp = Movie.ByYearAsc();
        Comparator<Movie> runtimeComp = Movie.ByRuntimeAsc();
        Comparator<Movie> votesComp = Movie.ByVotesDesc();

        // PART 1: COMPARATOR VERIFICATION TEST
        System.out.println("--- Part 1: Testing ByRatingDesc Comparator ---");
        Movie[] sample = Arrays.copyOf(masterDataset, Math.min(5, masterDataset.length));
        Arrays.sort(sample, Movie.ByRatingDesc());
        for (Movie m : sample) {
            System.out.println(m.getPrimaryTitle() + " - Rating: " + m.getAverageRating());
        }
        System.out.println();

        // PART 2: RUNNING ALGORITHM TESTS VIA HELPERS
        runSortTest("Merge Sort", MovieSorter::mergeSort, masterDataset, ratingComp);
        runSortTest("Quick Sort", MovieSorter::quickSort, masterDataset, ratingComp);
        runSortTest("SequenceStitcher", MovieSorter::sequenceStitcher, masterDataset, runtimeComp);
        runSortTest("AdaptiveReelSort", MovieSorter::adaptiveReelSort, masterDataset, votesComp);

        // PART 3: SMART ROUTER TEST
        System.out.println("--- Part 3: Smart Router Selection Test ---");
        Movie[] routerBatch = masterDataset.clone();
        SortMetrics routerMetrics = SmartRouter.routeAndSort(routerBatch, ratingComp);
        routerMetrics.printSummary("Smart Router Selection", routerBatch.length);
    }

    private static Movie[] loadData(String filePath) {
        ArrayList<Movie> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header row

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length < 5) continue;

                try {
                    String title = values[0].trim();
                    int year = parseOrDefaultInt(values[1], 0);
                    int runtime = parseOrDefaultInt(values[2], 0);
                    double rating = parseOrDefaultDouble(values[3], 0.0);
                    int votes = parseOrDefaultInt(values[4], 0);

                    list.add(new Movie(title, year, rating, votes, runtime));
                } catch (Exception e) {
                    // Skip malformed record
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading dataset file: " + e.getMessage());
        }

        return list.toArray(new Movie[0]);
    }

    private static int parseOrDefaultInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static double parseOrDefaultDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
