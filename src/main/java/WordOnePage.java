import java.util.List;

/**
 * Created by cherryzard on 5/18/2016.
 */
public class WordOnePage {
    private String _word;
    private int _page;
    private List<TextLocation> _indexesOnPage;
    public WordOnePage(String word, int page, List<TextLocation> indexesOnPage) {
        _word = word;
        _page = page;
        _indexesOnPage = indexesOnPage;
    }
    public String getWord() {return _word;}
    public int getPage() {return _page;}
    public List<TextLocation> getIndexesOnPage(){return _indexesOnPage;}

}
