import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ankit on 2016-05-14.
 */
public class WordTextLocation {
    private final String _word;
    private final TextLocationMap _locations;
    public WordTextLocation(String word) {
        _word = word;
        _locations = new TextLocationMap();
    }
    public String getWord() {return _word;}
    public TextLocationMap getLocations(){return _locations;}
    public void addEntry(TextLocation textLocation) {
        _locations.addEntry(textLocation);
    }
}
