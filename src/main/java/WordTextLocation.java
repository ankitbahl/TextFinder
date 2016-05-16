import java.util.List;

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

    /**
     * returns in natural order
     */
    public List<Integer> getIndexList(int pageNum) {
        return _locations.getIndexList(pageNum);
    }

    /**
     * returns page list in order
     */
    public List<Integer> getPageList() {
        return _locations.getPageListInOrder();
    }

    public boolean hasResultsOnPage(int page) {
        return _locations.hasKey(page);
    }

}
