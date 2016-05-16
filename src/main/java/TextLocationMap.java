import java.util.*;

/**
 * Created by cherryzard on 5/16/2016.
 */
public class TextLocationMap {
    private Map<Integer,List<Integer>> _locations = new HashMap<>();

    /**
     * inserts a textLocation object into the map
     * when it inserts the index of character into the list, it inserts in order
     */
    public void addEntry(TextLocation textLocation) {
        if(!hasKey(textLocation.getPageNumber())) {
            List<Integer> entryList = new ArrayList<>();
            entryList.add(textLocation.getCharNumber());
            _locations.put(textLocation.getPageNumber(),entryList);
        } else {
            List<Integer> entryList = _locations.get(textLocation.getPageNumber());
            entryList.add(textLocation.getCharNumber());
            entryList.sort(Comparator.naturalOrder());
        }
    }

    /**
     * returns a list of pages with results in order
     */
    public List<Integer> getPageListInOrder() {
        List<Integer> pageList = new ArrayList<>(_locations.keySet());
        pageList.sort(Comparator.naturalOrder());
        return pageList;
    }

    /**
     * returns list of indexes for results on a certain page in order
     */
    public List<Integer> getIndexList(int pageNum) {
        return _locations.get(pageNum);
    }

    public boolean hasKey(int pageKey) {
        return _locations.containsKey(pageKey);
    }
}
