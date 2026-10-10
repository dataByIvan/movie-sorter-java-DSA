package Project1;
public class SortMetrics {
    public long executionTimeNs;
    public long comparisonCount;
    public long swapOrCopyCount;

    public void printSummary(String algoName, int dataSize) {
        System.out.printf("[%s] Size: %d | Time: %.3f ms | Comparisons: %d | Swaps/Copies: %d\n",
                algoName, dataSize, executionTimeNs / 1_000_000.0, comparisonCount, swapOrCopyCount);
    }


}
