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
        _absoluteLocation = pageNumber + (double)charNumber/Main.getMaxPageChars(pageNumber);
    }

    public int getPageNumber() {return _pageNumber;}
    public int getCharNumber() {return _charNumber;}
    public double getAbsoluteLocation() {return _absoluteLocation;}
}
