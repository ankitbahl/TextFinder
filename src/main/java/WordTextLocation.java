/**
 * Created by Ankit on 2016-05-14.
 */
public class WordTextLocation {
    private String _word;
    private TextLocation[] _locations;
    public WordTextLocation(String word, TextLocation[] locations) {
        _word = word;
        _locations = locations;
    }
    public String getWord() {return _word;}
    public TextLocation[] getLocations(){return _locations;}
}
