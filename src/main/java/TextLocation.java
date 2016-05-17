/**
 *
 */
public class TextLocation {
    private final int _pageNumber;
    private final int _charNumber;
    private final double _absoluteLocation;
    public TextLocation(int pageNumber, int charNumber) {
        _pageNumber = pageNumber;
        _charNumber = charNumber;
        int[] maxPageChars = getMaxPageChars();
        _absoluteLocation = pageNumber + (double)charNumber/maxPageChars[pageNumber];
    }
    public double getAbsoluteDistance(TextLocation otherText) {
        return Math.abs(this.getAbsoluteLocation() - otherText.getAbsoluteLocation());
    }

    public int getPageNumber() {return _pageNumber;}
    public int getCharNumber() {return _charNumber;}
    public double getAbsoluteLocation() {return _absoluteLocation;}
    public int[] getMaxPageChars() {
        int[] maxPageChars = Main.getMaxPageChars();
        if(maxPageChars == null) {
            throw new RuntimeException("Textbook not loaded");
        }
        return maxPageChars;
    }
}
