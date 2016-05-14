import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final String FILE_LOCATION = "C:/pdf/greekmyths2.pdf";
    private static final String EXIT_KEYWORD = "exit";
    public static final int FIRST_PAGE = 25;
    private static AtomicBoolean _isLoading = new AtomicBoolean(true);
    private static String[] _textBook;
    private static int[] _maxPageChars;
    public static void main(String args[]) {
        init();
    }

    private static void onLoad() {
        while (true) {
            try {
                println("Enter number of terms: ");
                int numberOfTerms;
                try {
                    numberOfTerms = consoleReadInt();
                    if(numberOfTerms == 0) {
                        return;
                    }
                } catch (NumberFormatException e) {
                    println("Not a valid number");
                    continue;
                }
                println("Enter in each term and press enter");
                String[] words = new String[numberOfTerms];
                for(int i = 0; i < numberOfTerms; i++) {
                    String input = consoleRead();
                    if(input.equals(EXIT_KEYWORD)) {
                        return;
                    }
                    words[i] = input;
                }

                for (int i = 0; i < _textBook.length; i++) {
                    String page = _textBook[i];
                    if (page.toLowerCase().contains(input.toLowerCase())) {
                        int pageFound = i + FIRST_PAGE;
                        println(input + " found on page " + pageFound);
                    }
                }

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
    private static void init() {
        println("Loading pdf into memory...");
        Thread thread = new Thread(new PdfLoader(new PdfLoader.Caller() {
            public void onComplete(String[] data1,int[] data2 ) {
                _isLoading.set(false);
                _textBook = data1;
                _maxPageChars = data2;
            }
        }, FILE_LOCATION));
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(boolean b = false; _isLoading.get(); b = !b) {
            if(b) {
                println("..");
            } else {
                println(".");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        println("PDF Loaded");
        onLoad();

    }

    private static void print(String... s) {
        String base = "";
        for(String sk : s) {
            base += sk;
        }
        System.out.printf(base);
    }

    private static void println(String... s) {
        String base = "";
        for(String sk : s) {
            base += sk;
        }
        System.out.printf(base + "\n");
    }

    private static String consoleRead() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    private static int consoleReadInt() throws NumberFormatException{
        String stringInput = consoleRead();
        return Integer.parseInt(stringInput);
    }

    public static int[] getMaxPageChars() {
        return _maxPageChars;
    }

}
