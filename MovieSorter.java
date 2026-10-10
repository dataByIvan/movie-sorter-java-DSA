    package Project1;
    import java.util.Comparator;

    public class MovieSorter {
    /*
    Part 2: Complete the MovieSorter.java file by implementing the five sorting algorithms. 
    In this section of the project you will be implementing sorting algorithms using what we have learned in class. 
    You will implement mergeSort, quickSort, and three additional custom sorting algorithms that are variants and hybrids of sorting methods discussed in class. 
    You will also leverage the provided SortMetrics.java method to track the performance of your sorting algorithms. 
    You will use the swapOrCopyCount variable, which tracks any time a Movie reference changes position, whether through an in-place swap or copying into/out of 
    an auxiliary array. You will also use the comparisonCount variable, which tracks every time your 
    code evaluates the order of two Movie objects. You will have to correctly increment the swapOrCopyCount and comparisonCount variable within each sort
    */  
    // =========================================================================
        // Standard Sorting Algorithm Implementations
    // =========================================================================

        //Convenience overload for Merge Sort using Natural Order.
        public static SortMetrics mergeSort(Movie[] movies) {
            return mergeSort(movies, Comparator.naturalOrder());
        }


        public static SortMetrics mergeSort(Movie[] movies, Comparator<Movie> comp) {
            SortMetrics metrics = new SortMetrics();
            long startTime = System.nanoTime();

            if (movies != null && movies.length > 1) {
                    mergeSortHelper(movies, 0, movies.length - 1, comp, metrics);
                }

            metrics.executionTimeNs = System.nanoTime() - startTime;
            return metrics;
        }

        // TODO: Implement Merge Sort (Divide & Conquer) from scratch within mergeSortHelper.
        private static void mergeSortHelper(Movie[] movies, int left, int right, Comparator<Movie> comp, SortMetrics metrics) {
        // HINT: Implement recursive divide-and-conquer logic here.
        // HINT: You may want to use additional helper methods to complete merge sort.
        Movie[] temp = new Movie[right-left +1];
        if(left>=right){
            return;
        }
        int mid = (left + right)/2;
        int l = left;
        int r = mid + 1;
        int starter =0;

        

        mergeSortHelper(movies, left, mid, comp, metrics);
        mergeSortHelper(movies, mid + 1, right, comp, metrics);

        // if(comp.compare(movies[l], movies[r]) < 0){
        //     temp[starter] = movies[l];
        //     l++;

        // }else{
        //     temp[starter] = movies[r];
        //     r++;

        // }

        // starter++;

        while (l <= mid && r <= right) {
            
            if (comp.compare(movies[l], movies[r]) <= 0) {

                temp[starter] = movies[l];
                l++;
                metrics.comparisonCount++;
                metrics.swapOrCopyCount++;
        } else {

            temp[starter] = movies[r];
            r++;
            metrics.swapOrCopyCount++;
            metrics.comparisonCount++;
        }


        starter++;

        
    }

        while(l<=mid){
            temp[starter] = movies[l];
            l++;
            starter++;
            metrics.swapOrCopyCount++;
        }

        while(r<=right){
            temp[starter] = movies[r];
            r++;
            starter++;
            metrics.swapOrCopyCount++;
        }
        //copy/paste into movies[]

        for(int i =0; i<temp.length; i++){
            movies[left+i] = temp[i];
            metrics.swapOrCopyCount++;
        } // left

        // for(int i = right; i<temp.length; i++){
        //     movies[right+i] = temp[i];
        // } 



        



        }

        //Convenience overload for QuickSort, to default to using Natural Order.
        public static SortMetrics quickSort(Movie[] movies) {
            return quickSort(movies, Comparator.naturalOrder());
        }

        public static SortMetrics quickSort(Movie[] movies, Comparator<Movie> comp) {
            SortMetrics metrics = new SortMetrics();
            long startTime = System.nanoTime();


            if (movies != null && movies.length > 1) {
                    quickSortHelper(movies, 0, movies.length - 1, comp, metrics);
                }

            metrics.executionTimeNs = System.nanoTime() - startTime;
            return metrics;
        }


        private static void quickSortHelper(Movie[] movies, int left, int
            right, Comparator<Movie> comp, SortMetrics metrics) {
            //TODO: complete this helper which perfoms the main recursive logic of quickSort
            // HINT: You may consider additional helper methods
            // int temp[] = new Movie[right - left +1]; // temp array

            if(right <= left){ // base case
                //metrics.comparisonCount++;
                return;
            }


            Movie pivot = movies[right]; // get last element
            int k = left - 1;

            for(int j =left; j<right; j++){
                metrics.comparisonCount++;
                if(comp.compare(movies[j], pivot) <= 0){
                    k++;
                    Movie temp = movies[k];
                    movies[k] = movies[j];
                    movies[j]= temp;
                    metrics.swapOrCopyCount++;
                    // metrics.comparisonCount++;
                }//swaps
            }

            Movie temp = movies[k+1];
            // pivot = movies[k+1];
            movies[k+1] = movies[right];
            movies[right] = temp;
            metrics.swapOrCopyCount++;

            int index = k+1;


            
            quickSortHelper(movies, left, index-1, comp, metrics);
            quickSortHelper(movies, index+1, right, comp, metrics); // recursively sort?
            // for(int i =0; i<temp.length; i++){ // partition
            //     if(movies[i] < pivot){
            //         temp[i] = movies[i];

            //     }else{

            //     }

            // }

            
        }

        private static void swap(Movie[] movies, int i, int j, SortMetrics metrics){
            //HINT: Implement this extra swap helper method to aid in your quick Sort implementation
        }
    // =========================================================================
    // Custom Sorting Algorithm Implementations
    // =========================================================================

    /**
        * SequenceStitcher (Custom Sort 1)
        *
        * Custom Sorting Algorithm Behavior Description:
        * Recursively divides the dataset in half using a divide-and-conquer approach.
        * When a sub-list reaches a small threshold length (e.g., <= 16 elements), it switches
        * to a localized adjacent-element swap pass with an early-exit check if a pass yields
        * zero swaps. The resulting internally sorted sub-lists are then merged back up
        * the call stack using an auxiliary buffer.
        *
        * Use Case:
        * In this project, we will use sequenceStitcher to sort movies by runtime including
        * many possible continous duplicate values. This method should cleanly handle
        * duplicates, maintaining their original ordering.
        *
        * @param movies Array of Movie objects to be sorted
        * @param comp   Comparator defining the sort order
            * @return SortMetrics containing timing, comparison, and swap counts
    */
        public static SortMetrics sequenceStitcher(Movie[] movies) {
            return sequenceStitcher(movies, Comparator.naturalOrder());
        }


        public static SortMetrics sequenceStitcher(Movie[] movies, Comparator<Movie> comp) {
                SortMetrics metrics = new SortMetrics();
                long startTime = System.nanoTime();

                if (movies != null && movies.length > 1) {
                    Movie[] temp = new Movie[movies.length];
                    sequenceStitcherHelper(movies, temp, 0, movies.length - 1, comp, metrics);
                }

                metrics.executionTimeNs = System.nanoTime() - startTime;
                return metrics;
            }

            private static void sequenceStitcherHelper(Movie[] movies, Movie[] temp, int left, int right,
                                                Comparator<Movie> comp, SortMetrics metrics) {
                // TODO: Implement recursive division logic with threshold cutoff (size <= 16).
                // HINT: Check for sub-array size threshold (e.g., <= 16).
                // HINT: If size <= 16, run an adjacent-swap pass (Bubble pass with early exit).
                // HINT: Otherwise, divide at mid and merge sub-lists back together using temp[].
                int size = right - left + 1;

                if(size <= 16){
                    //bubble
                    
                    for(int i =left; i< right; i++){
                        boolean swapped = false;
                        // metrics.comparisonCount++;
                        // metrics.swapOrCopyCount++;
                        for(int j = left ; j<right; j++){

                            metrics.comparisonCount++;
                            // metrics.swapOrCopyCount++;
                            
                            if(comp.compare(movies[j], movies[j+1]) > 0){
                                Movie temp1 = movies[j];
                                movies[j] = movies[j+1];
                                movies[j+1] = temp1;    
                                swapped = true;
                                metrics.swapOrCopyCount++;
                                // return;
                            }
                            }if(swapped == false){
                            break; //exit
                        }

                        }
                        return;
                    }
                    int mid = (left+right)/2;
                    sequenceStitcherHelper(movies, temp, left, mid, comp, metrics);
                    sequenceStitcherHelper(movies, temp, mid+1, right, comp, metrics); //recursion

                    //append to temp arr

                    int p1 = left;
                    int p2 = mid+1;
                    int p = left; // temp var

                    while(p1 <= mid && p2 <= right){
                        metrics.comparisonCount++;
                        if(comp.compare(movies[p1], movies[p2]) <= 0){

                            temp[p] = movies[p1];
                            // Movie temp2 = movies[p1];
                            // movies[p1] = movies[p2];
                            // movies[p2] = temp2;
                            p++;
                            p1++;
                        }else{
                            temp[p] = movies[p2];
                            p2++;   
                            p++;
                        }
                        metrics.swapOrCopyCount++;
                        
                    }

                    while(p1 <=mid){
                        temp[p] = movies[p1];
                        p1++;
                        p++;
                        metrics.swapOrCopyCount++;

                    }

                    while(p2 <=right){
                        temp[p] = movies[p2];
                        p2++;
                        p++;
                        metrics.swapOrCopyCount++;

                    }


                    for(int i =left; i <= right ; i++){
                        movies[i] = temp[i];
                        metrics.swapOrCopyCount++;
                    }

                }


                    //divide then 
                    //merge
                
            

        /** AdaptiveReelSort (Custom Sort 2)
        *
        * Custom Sorting Algorithm Behavior Description:
        * Recursively breaks down sub-arrays around a pivot until sub-arrays drop below
        * a specific threshold size (e.g., <= 16 elements). At that point, recursion halts.
        * Once high-level structural partitioning completes, a final linear pass runs across
        * the entire array to finalize full order.
        *
        * @param movies Array of Movie objects to be sorted
        * @param comp   Comparator defining the sort order
        * @return SortMetrics containing timing, comparison, and swap counts
        */

        /**
        * Convenience overload for AdaptiveReelSort using Natural Order.
        */
        public static SortMetrics adaptiveReelSort(Movie[] movies) {
        return adaptiveReelSort(movies, Comparator.naturalOrder());
        }

        public static SortMetrics adaptiveReelSort(Movie[] movies, Comparator<Movie> comp) {
            SortMetrics metrics = new SortMetrics();
            long startTime = System.nanoTime();


            if (movies != null && movies.length > 1) {
                // TODO: Step 1 - Call recursive partitioning helper with threshold cutoff (size <= 16)

                adaptiveReelSortHelper(movies, 0, movies.length -1, comp, metrics);

                // TODO: Step 2 - Call linear pass helper to finalize full array order
                finalizeOrder(movies, comp, metrics);
            }

            metrics.executionTimeNs = System.nanoTime() - startTime;
            return metrics;
        }

        private static void adaptiveReelSortHelper(Movie[] movies, int low, int high,
                                                    Comparator<Movie> comp, SortMetrics metrics) {

        // TODO: Implement recursive partitioning with threshold cutoff (size <= 16).
        // HINT: Halt recursion if range size <= 16, otherwise partition and recurse.
        // HINT: It may help to use additional helper functions below
        int size = high-low +1;
                                                        if(size <= 16){
                                                            return;
                                                        }

                                                        int splitter = partition(movies, low, high, comp, metrics);

                                                        adaptiveReelSortHelper(movies, low, splitter, comp, metrics);
                                                        adaptiveReelSortHelper(movies, splitter+1, high, comp, metrics);

                                                        //finalize helper next
        }

        private static int partition(Movie[] movies, int low, int high,
                                        Comparator<Movie> comp, SortMetrics metrics) {
            // HINT: It may help to implement this helper for adaptiveReelSort
            // TODO: Partition elements around pivot using two inward-moving pointers

            int l = low;
            int h = high;

            Movie pivot = movies[(low+high)/2];

            while(l <= h){
                while(comp.compare(movies[l], pivot) < 0){

                    l++;
                    metrics.comparisonCount++;
                }

                while(comp.compare(movies[h], pivot) > 0){
                    h--;
                    metrics.comparisonCount++;
                }

                if(l <= h){
                    Movie temp3 = movies[l];
                    movies[l] = movies[h];
                    movies[h] = temp3;
                    h--;
                    l++;
                    metrics.swapOrCopyCount++;

                }

            }


            return h; // Placeholder index
        }

        private static void finalizeOrder(Movie[] movies, Comparator<Movie> comp, SortMetrics metrics) {
            // HINT: It may help to implement this helper for adaptiveReelSort
            // TODO: Perform shifting/ordering pass over entire array (0 to N-1)

            for(int i =1; i< movies.length ; i++){
                Movie temp5 = movies[i];
                int prev = i-1;
                metrics.comparisonCount++;
                

                while(prev>=0 && comp.compare(temp5, movies[prev]) <0){
                    movies[prev + 1] = movies[prev];
                    prev--;
                    metrics.swapOrCopyCount++;
                    // metrics.comparisonCount++;
                }
                movies[prev + 1] = temp5;
                metrics.swapOrCopyCount++;



            }
        }


    /**
         * TopBoxOfficeRanker (Custom Sort 3)
         *
         * Custom Sorting Algorithm Behavior Description:
         * Phase 1: Executes K scanning passes over the dataset to locate extreme values
         * and swap them into the first K positions (indices 0 to K-1).
         *
         * Phase 2: Continues the scanning-and-swapping process across the remainder of
         * the dataset (indices K to N-1) until all remaining elements are fully ordered.
         *
         * Use Case:
         * Powers displays featuring top films based on overall box office rating or revenue.
         *
         * @param movies Array of Movie objects to be sorted
         * @param k      Number of top elements to extract and place at the front
         * @param comp   Comparator defining the sort order
         * @return SortMetrics containing execution time, comparison, and swap counts
         */

        public static SortMetrics topBoxOfficeRanker(Movie[] movies, int k) {
            return topBoxOfficeRanker(movies, k, Comparator.naturalOrder());
        }
        public static SortMetrics topBoxOfficeRanker(Movie[] movies, int k, Comparator<Movie> comp) {
            SortMetrics metrics = new SortMetrics();
            long startTime = System.nanoTime();

            if (movies != null && movies.length > 1) {
                metrics.comparisonCount++;
                // metrics.swapOrCopyCount++;
                int n = movies.length;
                int effectiveK = Math.min(Math.max(k, 0), n);

                int ind = n-1;

                // TODO: Phase 1 - Perform K scanning passes to locate and swap extreme elements into indices 0 to effective K - 1.

                for(int i = 0 ; i < effectiveK; i++){ // perform k scans from 0 to ek-1
                    int extreme =i; //extreme placeholder
                    // metrics.comparisonCount++;
                    for(int j = i + 1; j<n; j++){
                        if(comp.compare(movies[j], movies[extreme]) < 0){
                            // Movie temp4 = movies[j];
                            // movies[j] = movies[extreme];
                            // movies[extreme] = temp4;
                            extreme = j;
                            metrics.comparisonCount++;


                        }
                        // if(extreme != i){
                        //     Movie temp4 = movies[i];
                        //     movies[i] = movies[extreme];
                        //     movies[extreme] = temp4;
                        //     metrics.swapOrCopyCount++;

                        // }
                        // Movie temp4 = movies[i];
                        // movies[i] = movies[extreme];
                        // movies[extreme] = temp4;
                        // metrics.swapOrCopyCount++;
                    }
                    if(extreme != i){
                            Movie temp4 = movies[i];
                            movies[i] = movies[extreme];
                            movies[extreme] = temp4;
                            metrics.swapOrCopyCount++;

                        }
                }

                // TODO: Phase 2 - Continue scanning passes for indices effectiveK to n - 1 to finalize full array order.   

                for(int i = effectiveK; i< n-1; i++){
                                    int extreme =i; //extreme placeholder
                    // metrics.comparisonCount++;
                    for(int j = i + 1; j<n; j++){
                        metrics.comparisonCount++;
                        if(comp.compare(movies[j], movies[extreme]) < 0){
                            // Movie temp4 = movies[j];
                            // movies[j] = movies[extreme];
                            // movies[extreme] = temp4;
                            extreme = j;
                            // metrics.comparisonCount++;


                        }
                        // if(extreme != i){
                        //     Movie temp4 = movies[i];
                        //     movies[i] = movies[extreme];
                        //     movies[extreme] = temp4;
                        //     metrics.swapOrCopyCount++;

                        }
                        if(extreme != i){
                            Movie temp4 = movies[i];
                            movies[i] = movies[extreme];
                            movies[extreme] = temp4;
                            metrics.swapOrCopyCount++;
                        // Movie temp4 = movies[i];
                        // movies[i] = movies[extreme];
                        // movies[extreme] = temp4;
                        // metrics.swapOrCopyCount++;
                    }

                }
            }

            metrics.executionTimeNs = System.nanoTime() - startTime;
            return metrics;
        }

    }
