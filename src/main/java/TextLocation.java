/**
 * Created by Ankit on 2016-05-13.
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
        int first
    }

}
