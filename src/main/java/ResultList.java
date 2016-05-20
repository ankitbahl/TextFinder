import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Rita on 2016-05-20.
 */
public class ResultList {
    private final List<Result> _resultList;
    public ResultList() {
        _resultList = new ArrayList<>();
    }
    public void addResult(Result result) {
        _resultList.add(result);
    }
    public void addEntryToAllResults(TextLocation textLocation) {
        for(Result result : _resultList) {
            result.addResultPart(textLocation);
        }
    }
    public List<Result> getResultList() {
        _resultList.sort(Comparator.naturalOrder());
        return _resultList;
    }
}
