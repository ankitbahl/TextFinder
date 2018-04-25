import java.util.List;

/**
 * to run from console: java -cp TextFinder.jar Main
 */
public class Main {
    public static String[] _input;
    public static void main(String args[]) {
        List<Result> results = onLoad(
                new String[]{"The Atlantic Sun Conference Women’s Basketball Tournament is a postseason tournament that determines which team receives the conference's automatic bid into the NCAA Women's Division I Basketball Championship.",
                "The tournament was first held in 1986 by the New South Women's Athletic Conference, a women-only Division I conference. Following the 1990–91 basketball season, the NSWAC was absorbed by the Trans America Athletic Conference, with the TAAC incorporating all NSWAC statistics and records as its own. The conference changed its name to Atlantic Sun Conference in 2002, and rebranded itself as the ASUN Conference in 2016 (though the legal name is still \"Atlantic Sun\").\n",
                "For most of its history, the tournament was held at predetermined campus sites, a tradition which started with the inception of the women's tournament. From 2004-07, the tournament was played regularly at the Dothan Civic Center in Dothan, Alabama, though then-conference member Troy was the official host in 2004-05, and the city of Dothan was the host in 2006-07, after Troy had departed for the Sun Belt Conference. Starting in 2008, the tournament moved yet again, this time to Nashville, Tennessee, hosted by Lipscomb. In 2010, the tourney was moved to Macon, Georgia and was hosted by Mercer University through 2013. It then moved to Alico Arena on the campus of Florida Gulf Coast University near Fort Myers, Florida for two seasons. The 2016 tournament began a new era for the event, with all games being held at campus sites. Since then, all games have been hosted by the higher seed of the teams involved.\n"
                },
                new String[]{"200","tournament"}
                );
        for(Result r : results) {
            System.out.println(r.getIndex().getAbsoluteLocation());
            int charnum = r.getIndex().getCharNumber();
            System.out.println(_input[r.getIndex().getPageNumber()].substring(charnum - 20, charnum + 20));
        }
    }

    private static List<Result> onLoad(String[] input, String[] searchTerms) {
        while (true) {
            if(searchTerms.length < 2) {
                return null;
            }
            _input = input;
            //TODO allocate a thread for each word for efficiency
            List<WordTextLocation> wordTextLocations = Search.findWords(searchTerms);
            List<Result> results = Search.search(wordTextLocations);
            return results;
        }
    }

    static int getMaxPageChars(int pageNumber) {
        return _input[pageNumber].length();
    }
}
