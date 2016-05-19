import java.util.List;

/**
 * Created by cherryzard on 5/15/2016.
 */
public class Results implements Comparable<Results> {
    private final TextLocation _index;
    private final double _standardDeviation;

    public Results(TextLocation index, double standardDeviation) {
        _index = index;
        _standardDeviation = standardDeviation;
    }

    public Results(TextLocation index, List<TextLocation> textLocations) {
        double totalSum = 0;
        for(TextLocation textLocation : textLocations) {
            totalSum += textLocation.getAbsoluteLocation();
        }
        int numTerms = textLocations.size();
        double average = totalSum/numTerms;
        double sumSquares = 0;
        for(TextLocation textLocation : textLocations) {
            sumSquares += Math.pow(textLocation.getAbsoluteLocation() - average,2);
        }
        double aveSquares = sumSquares / numTerms;
        _standardDeviation = Math.sqrt(aveSquares);
        _index = index;
    }

    public int compareTo(Results other) {
        return Double.valueOf(_standardDeviation).compareTo(other.getStandardDeviation());
    }

    public TextLocation getIndex() {return _index;}
    public double getStandardDeviation() {return _standardDeviation;}
}
