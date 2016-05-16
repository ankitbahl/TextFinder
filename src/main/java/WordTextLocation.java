import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 2016-05-14.
 */
public class WordTextLocation {
    private final String _word;
    private final TextLocationMap _textLocationMap;
    private final List<TextLocation> _textLocationList;
    public WordTextLocation(String word) {
        _word = word;
        _textLocationMap = new TextLocationMap();
        _textLocationList = new ArrayList<>();
    }

    public WordTextLocation(String word, List<TextLocation> textLocationList) {
        _word = word;
        _textLocationList = textLocationList;
        _textLocationMap = new TextLocationMap();
        for(TextLocation textLocation : textLocationList) {
            _textLocationMap.addEntry(textLocation);
        }
    }
    public String getWord() {return _word;}
    public TextLocationMap getLocations(){return _textLocationMap;}

    public void addEntry(TextLocation textLocation) {

        _textLocationMap.addEntry(textLocation);
        _textLocationList.add(textLocation);
    }

    /**
     * returns in natural order
     */
    public List<Integer> getIndexList(int pageNum) {
        return _textLocationMap.getIndexList(pageNum);
    }

    public List<TextLocation> getTextLocationList(int pageNum) {
        List<Integer> indexList = getIndexList(pageNum);
        List<TextLocation> textLocationList = new ArrayList<>();
        for(int index : indexList) {
            textLocationList.add(new TextLocation(pageNum,index));
        }
        return textLocationList;
    }

    /**
     * returns page list in order
     */
    public List<Integer> getPageList() {
        return _textLocationMap.getPageListInOrder();
    }

    public boolean hasResultsOnPage(int page) {
        return _textLocationMap.hasKey(page);
    }

    public List<TextLocation> getTextLocations() {return _textLocationList;}

}
