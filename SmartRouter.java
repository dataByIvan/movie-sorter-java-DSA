package Project1;

import java.util.Comparator;

/**
 * SmartRouter determines the most efficient sorting algorithm to deploy based on
 * dataset characteristics (e.g., size, pre-existing order) and intended use cases.
 */
public class SmartRouter {

    /**
     * Enum indicating the sorting goal/criterion for intent-aware routing.
     */
    private static final int SMALL_DATASET_THRESHOLD = 10;
    public enum SortIntent {
        GENERAL,
        RUNTIME_DUPLICATES, // Stable sort suited for continuous duplicates (SequenceStitcher)
        POPULARITY_VOTES,   // Quick partitioning with insertion cleanup (AdaptiveReelSort)
        TOP_K_RATINGS       // Extract top K elements before sorting the rest (TopBoxOfficeRanker)
    }

    /**
     * Default routing method based on dataset size and characteristics.
     * Defaults to GENERAL intent and k = 10.
     */
    public static SortMetrics routeAndSort(Movie[] movies, Comparator<Movie> comp) {
        return routeAndSort(movies, comp, SortIntent.GENERAL, 10);
    }

    /**
     * Intent-aware routing method to select the optimal algorithm based on data characteristics and use case.
     *
     * @param movies Array of movies to sort
     * @param comp   Comparator defining the sort order
     * @param intent High-level routing intent selected by the application
     * @param k      Number of top elements requested (used for TOP_K_RATINGS)
     * @return SortMetrics containing execution time, comparison counts, and swap/copy counts
     */
    public static SortMetrics routeAndSort(Movie[] movies, Comparator<Movie> comp, SortIntent intent, int k) {
        // TODO 1: Implement Intent-Based Routing using a switch statement on `intent`:
        // Select and execute the algorithm from MovieSorter that best satisfies each intent's requirements:
        // - SortIntent.RUNTIME_DUPLICATES : Must guarantee stability across identical keys.
        // - SortIntent.POPULARITY_VOTES   : Optimized for in-place, average-case fast partitioning.
        // - SortIntent.TOP_K_RATINGS      : Optimized to extract top K elements without sorting the whole array.
        // Remember to print a brief log message indicating which algorithm was selected and why.

        switch(intent){
            case RUNTIME_DUPLICATES:
                // use bubble, insertion, or merge --> stable
                System.out.println("Smart-Router: using sequenceSticher for stability: ");
                return MovieSorter.sequenceStitcher(movies, comp);

                // break;

            
            case POPULARITY_VOTES: // partition
            System.out.println("Smart-Router: using adaptiveReelSort for partitioning: ");
                return MovieSorter.adaptiveReelSort(movies, comp); //recursive

                // break;
            

            case TOP_K_RATINGS: //recursive
                System.out.println("Smart-Router: using topBoxOfficeRanker for recursion: ");
            
                return MovieSorter.topBoxOfficeRanker(movies, k, comp);

            case GENERAL:

                break;
            }

        

        // TODO 2: Implement Data-Driven Routing for SortIntent.GENERAL:
        // Evaluate the dataset properties dynamically to choose the fastest algorithm:
        // - Rule A: If the dataset is small (n <= SMALL_DATASET_THRESHOLD), delegate to an algorithm
        //           that avoids recursive function call overhead.
        // - Rule B: Inspect dataset order using checkIsNearlySorted(movies, comp). If true, delegate
        //           to an algorithm capable of taking advantage of existing order in O(N) time.
        // - Rule C: Default to your fastest general-purpose O(N log N) divide-and-conquer algorithm.
        int n = movies.length;
        // private static final int SMALL_DATASET_THRESHOLD = 16;

        if(n <= SMALL_DATASET_THRESHOLD){
            System.out.println("Smart-Router: using sequenceSticher for small threshold: ");
            return MovieSorter.sequenceStitcher(movies, comp); // no recursion
        }else if(checkIsNearlySorted(movies, comp)){
            System.out.println("Smart-Router: using adaptiveReelSort as list is nearly sorted: ");
            return MovieSorter.adaptiveReelSort(movies, comp);
        }else{
            System.out.println("Smart-Router: using quickSort for speed: ");
            return MovieSorter.quickSort(movies, comp);
        }

        // throw new UnsupportedOperationException("SmartRouter.routeAndSort() not yet fully implemented.");
    }

    /**
     * Helper method to sample the dataset and detect if the array is already mostly ordered.
     *
     * @param movies Array of movies to inspect
     * @param comp   Comparator defining expected sort order
     * @return true if inversion rate across sampled elements is below threshold (e.g., <= 10%), false otherwise
     */
    private static boolean checkIsNearlySorted(Movie[] movies, Comparator<Movie> comp) {
        // TODO 3: Implement sample scan inspection:
        // 1. If movies.length <= SMALL_DATASET_THRESHOLD, return false (too small to scan).
        // 2. Define a small sample size (e.g., scanning adjacent pairs in the first ~5% of the array).
        // 3. Count adjacent inversions using comp.compare().
        // 4. Return true if inverted pairs represent 10% or less of the sample.
        // metrics.comparisonCount++;
        int inversionCount = 0;
        

        if(movies.length <= SMALL_DATASET_THRESHOLD){
            return false; //#1
        }

        int smallSample = (int) (0.05 * movies.length); // #2

        // if(smallSample < 1){
        //     break;
        // }

        for(int i =0; i<smallSample;i++){
            // comp.compare(movies[i], movies[i+1]);
            // metrics.swapOrCopyCount++;

            if(comp.compare(movies[i], movies[i+1]) >0){
                inversionCount++;
            }
        }

        return inversionCount <= 0.1 * smallSample;  // 4. Return true if inverted pairs represent 10% or less of the sample.

        // throw new UnsupportedOperationException("SmartRouter.checkIsNearlySorted() not yet implemented.");
    }
}

