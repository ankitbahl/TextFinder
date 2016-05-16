/**
 * Created by cherryzard on 5/15/2016.
 */
public class Results implements Comparable<Results> {
    private final TextLocation _index;
    private final double _averageCharDistance;

    public Results(TextLocation index, double averageCharDistance) {
        _index = index;
        _averageCharDistance = averageCharDistance;
    }

    public Results(TextLocation indexLocation, TextLocation... allLocations) {
        int totalCharDistance = 0;
        TextLocation firstTextLocation = allLocations[0];
        for(int i = 1; i < allLocations.length; i++) {
            totalCharDistance += (firstTextLocation.getCharDistance(allLocations[i]));
        }
        double averageCharDistance = (double)totalCharDistance / (allLocations.length - 1);
        _index = indexLocation;
        _averageCharDistance = averageCharDistance;
    }

    public int compareTo(Results other) {
        return Double.valueOf(_averageCharDistance).compareTo(other._averageCharDistance);
    }

    public TextLocation getIndex() {return _index;}
    public double getAverageCharDistance() {return _averageCharDistance;}
}
