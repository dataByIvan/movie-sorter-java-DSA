package Project1;

import java.util.Comparator;

/**
 * Complete the implementation of the Movie class.
 *
 * To implement a sorting algorithm, we must have a basis to compare two items and determine
 * which is considered 'greater' or 'lesser'. This is straightforward when dealing with
 * integers as we have often done in class. However, when dealing with real-world data, we may
 * need to establish other means of comparison.
 *
 * For this project, you must implement the Comparable interface for the Movie class so that
 * by default they are sorted alphabetically by their title.
 *
 * You must also implement custom comparators for alternative sorting criteria.
 * 
 * 
 * 
 * 
 * Part 1: Complete the Movie.java skeleton file, which instantiates the Movie custom class and implements
 *  multiple Comparators. Comparators form the basis of your sorting algorithms.
 *  You will implement comparators using standard classes, anonymous classes, and lambda. 
 * You can test Part 1 using the provided MainApp.java, which specifically tests the ByRatingDesc comparator. 
 * You are encouraged to write your own tests to evaluate the other comparators. Study Tip: Part 1 provides
 *  excellent hands on practice in core concepts covered in Midterm 1, including object comparison and Java syntax.
 */
public class Movie implements Comparable<Movie> {
    private String primaryTitle;
    private int releaseYear;
    private double averageRating;
    private int numVotes;
    private int runTime;

    /**
     * REQUIREMENT 1: Complete the constructor for a Movie object.
     */
    public Movie(String primaryTitle, int releaseYear, double averageRating, int numVotes, int runTime) {
        // TODO: Complete the constructor to initialize all private fields
        this.primaryTitle = primaryTitle;
        this.releaseYear = releaseYear;
        this.averageRating = averageRating;
        this.numVotes = numVotes;
        this.runTime = runTime;
    }

    /**
     * REQUIREMENT 2: Implement the Comparable interface method.
     * Default behavior should sort movies alphabetically by primaryTitle.
     * If titles are identical, break the tie by release year (older first).
     */
    @Override
    public int compareTo(Movie other) {
        // TODO: Implement default comparison logic (Title ascending, break ties with Release Year ascending)
        int title = this.primaryTitle.compareTo(other.primaryTitle); // ascending order title

        if(title != 0){ // if not tie return title
            return title;
        }

        return Integer.compare(this.releaseYear, other.releaseYear);
    }

    /**
     * REQUIREMENT 3: Create custom Comparators for alternative sorting criteria.
     * These will be passed into your sorting algorithms.
     */

    /**
     * TODO: Implement ByRatingDesc() Comparator which
     * compares by averageRating descending (highest rating first).
     * Break ties using vote counts descending (highest votes first).
     *
     * Requirement: Implement using an Anonymous Inner Class.
     */
    public static Comparator<Movie> ByRatingDesc() {
        return new Comparator<Movie>() {
            @Override
            public int compare(Movie m1, Movie m2) {
                // TODO: Implement comparison logic using anonymous inner class syntax

                int avg = Double.compare(m2.averageRating,m1.averageRating);
                if(avg != 0){
                    return avg;
                }

                return Integer.compare(m2.numVotes, m1.numVotes);
            }
        };
    }

    /**
     * TODO: Implement ByYearAsc() Comparator which
     * compares by releaseYear ascending (oldest movies first).
     * Break ties alphabetically by title.
     *
     * Requirement: Implement using a Lambda Expression.
     */
    public static Comparator<Movie> ByYearAsc() {
        // TODO: Implement using a lambda expression

        // int year = integer.compareTo(m2.releaseYear, m1.releaseYear);

        // if(year != 0){
        //     return year;
        // }


        // return integer.compare(other.releaseYear, this.releaseYear);

        return (m1,m2) -> {
            int year = Integer.compare(m1.releaseYear, m2.releaseYear);

            if(year != 0){
                return year;
            }

            return m1.primaryTitle.compareTo(m2.primaryTitle);


        };
        
    }

    /**
     * TODO: Implement ByRuntimeAsc() Comparator which
     * compares movies by ascending runtime (shortest movies first).
     * Break ties alphabetically by title.
     */
    public static Comparator<Movie> ByRuntimeAsc() {
        // TODO: Implement comparison logic

        return (m1,m2) -> {
            int movieRunTime = Integer.compare(m1.runTime, m2.runTime);

            if(movieRunTime != 0){
                return movieRunTime;
            }

            return m1.primaryTitle.compareTo(m2.primaryTitle);


        };
    }

    /**
     * TODO: Implement ByVotesDesc() Comparator which
     * compares movies by number of votes descending (most votes first).
     * Break ties by average rating descending (highest rating first).
     */
    public static Comparator<Movie> ByVotesDesc() {
        return (m1,m2) -> {
            int votes = Integer.compare(m2.numVotes, m1.numVotes);

            if(votes != 0){
                return votes;
            }

            return Double.compare(m2.averageRating, m1.averageRating);


        };
    }

    // Standard Getters (No Setters allowed - Movie data is immutable once loaded)
    public String getPrimaryTitle() { return primaryTitle; }
    public int getReleaseYear() { return releaseYear; }
    public double getAverageRating() { return averageRating; }
    public int getNumVotes() { return numVotes; }
    public int getRuntimeMinutes() { return runTime; }

    @Override
    public String toString() {
        return String.format("%s (%d) - %d mins - Rating: %.1f [%d votes]",
                primaryTitle, releaseYear, runTime, averageRating, numVotes);
    }
}
