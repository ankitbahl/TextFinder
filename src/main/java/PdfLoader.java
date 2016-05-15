import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfLoader implements Runnable {
    private static Caller _caller;
    private static String FILE_LOCATION;
    private static final int FIRST_PAGE = Main.FIRST_PAGE;

    public PdfLoader(Caller caller, String fileLocation) {
        _caller = caller;
        FILE_LOCATION = fileLocation;
    }

    public void run() {
        try {
            PDDocument document = PDDocument.load(new File(FILE_LOCATION));
            Data data = getAllPages(document);
            _caller.onComplete(data._data1,data._data2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Data getAllPages(PDDocument document) throws IOException {
        PDFTextStripper reader = new PDFTextStripper();
        String[] textBook = new String[document.getNumberOfPages() - FIRST_PAGE + 1];
        int[] numChars = new int[document.getNumberOfPages() - FIRST_PAGE + 1];
        for(int i = FIRST_PAGE; i < textBook.length + FIRST_PAGE;i++ ) {
            reader.setStartPage(i);
            reader.setEndPage(i);
            String page = reader.getText(document).replaceAll("\r","").replaceAll("\n","").replaceAll("-","");
            textBook[i-FIRST_PAGE] = page;
            numChars[i-FIRST_PAGE] = page.length();
        }
        return new Data(textBook,numChars);
    }

    public interface Caller {
        void onComplete(String[] data1, int[] data2);
    }
    private static class Data {
        public final String[] _data1;
        public final int[] _data2;
        public Data(String[] data1, int[] data2) {
            _data1 = data1;
            _data2 = data2;
        }
    }
}
