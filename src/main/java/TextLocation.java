/**
 * TODO use hashmap with page number as key in order to optimize searching ( fix search as well)
 */
public class TextLocation {
    private final int _pageNumber;
    private final int _charNumber;
    public TextLocation(int pageNumber, int charNumber) {
        _pageNumber = pageNumber;
        _charNumber = charNumber;
    }
    public int getCharDistance(TextLocation otherText) {
        int[] maxPageChars = Main.getMaxPageChars();
        if(maxPageChars == null) {
            throw new RuntimeException("textbook not loaded");
        }
        int firstPageNum = _pageNumber;
        int firstCharNum = _charNumber;
        int secondPageNum = otherText.getPageNumber();
        int secondCharNum = otherText.getCharNumber();
        if(firstPageNum > secondPageNum) {
            return getDistance(secondPageNum,firstPageNum,secondCharNum,firstCharNum,maxPageChars);
        } else if(secondPageNum > firstPageNum) {
            return getDistance(firstPageNum, secondPageNum, firstCharNum, secondCharNum,maxPageChars);
        } else {
            return Math.abs(firstCharNum-secondCharNum);
        }
    }
    private static int getDistance(int smallerPageNum, int biggerPageNum, int charForSmaller, int charForBigger, int[] maxPageChars) {
        int distance = maxPageChars[smallerPageNum] - charForSmaller;
        for(int i = smallerPageNum + 1; i< biggerPageNum; i++) {
            distance += maxPageChars[i];
        }
        distance+= charForBigger;
        return distance;
    }

    public int getPageNumber() {return _pageNumber;}
    public int getCharNumber() {return _charNumber;}
}
