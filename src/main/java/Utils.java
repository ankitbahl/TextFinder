import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ankit on 2016-05-21.
 */
public class Utils {

    /**
     * assumes that it is page contains words
     * @param word
     * @param page
     * @return
     */
    public static List<Integer> getAllIndexOccurrencesInString(String word, String page) {
        return indexOccurenceFinderRecursive(word,page,0);
    }

    public static List<Integer> indexOccurenceFinderRecursive(String word, String page, int toAdd) {
        List<Integer> allIndexes = new ArrayList<>();
        int index = page.indexOf(word);
        allIndexes.add(index+toAdd);
        String rest = page.substring(index + word.length());
        if(rest.contains(word)) {
            List<Integer> indexList = indexOccurenceFinderRecursive(word,rest,toAdd + index + word.length());
            allIndexes.addAll(indexList);
        }
        return allIndexes;
    }

    public static String addSpacesInBetweenEachCharacter(String original) {
        return original.replaceAll(".(?=.)", "$0 ");
    }

    public static List<WordTextLocation> removeFirstElement(List<WordTextLocation> original) {
        return original.subList(1, original.size());
    }
}
