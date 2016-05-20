import java.util.ArrayList;
import java.util.List;

/**
 * Created by cherryzard on 5/15/2016.
 */
public class Result implements Comparable<Result> {
    private final TextLocation _index;
    private final List<TextLocation> _result;

    public Result(TextLocation index) {
        _index = index;
        _result = new ArrayList<>();
        _result.add(index);
    }

    public Result(TextLocation index, List<TextLocation> textLocations) {
        _index = index;
        _result = textLocations;
    }

    public int compareTo(Result other) {
        return Double.valueOf(getStandardDeviation()).compareTo(other.getStandardDeviation());
    }

    public void addResultPart(TextLocation textLocation) {
        _result.add(textLocation);
    }

    public TextLocation getIndex() {return _index;}
    public double getStandardDeviation() {
        double totalSum = 0;
        for(TextLocation textLocation : _result) {
            totalSum += textLocation.getAbsoluteLocation();
        }
        int numTerms = _result.size();
        double average = totalSum/numTerms;
        double sumSquares = 0;
        for(TextLocation textLocation : _result) {
            sumSquares += Math.pow(textLocation.getAbsoluteLocation() - average,2);
        }
        double aveSquares = sumSquares / numTerms;
        return Math.sqrt(aveSquares);
    }
}
