import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by cherryzard on 5/16/2016.
 */
public class TextLocationMap {
    private Map<Integer,Integer> _locations = new HashMap<Integer, Integer>();
    public void addEntry(TextLocation textLocation) {

    }

    public List<Integer> getPageList() {
        List<Integer> pageList = new ArrayList<Integer>(_locations.keySet());
        pageList.sort(Comparator.naturalOrder());
        return pageList;
    }
}
