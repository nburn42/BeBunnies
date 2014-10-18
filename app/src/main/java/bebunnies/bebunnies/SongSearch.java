package bebunnies.bebunnies;

import com.echonest.api.v4.Artist;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;
import com.echonest.api.v4.YearsActive;
import java.util.List;
/**
 * Created by manavdutta1 on 10/18/14.
 */
public class SongSearch {
    private EchoNestAPI en;
    private static String APIKey = "TSPV7YH3UINWQAH6T";
    public SongSearch() {
        en = new EchoNestAPI(APIKey);
    }
    public List<Song> searchSongsByTempo(int results)
            throws EchoNestException {
        SongParams p = new SongParams();
        p.setMinTempo(0);
        p.setMaxTempo(100);
        p.setResults(results);
        List<Song> songs = en.searchSongs(p);
        for (Song song : songs) {
            System.out.printf("%.0f %s %s\n", song.getTempo(), song
                    .getArtistName(), song.getTitle());
        }
        return songs;
    }
    public static void main(String [] args) {
        SongSearch search = new SongSearch();
        try {
            List<Song> songs = search.searchSongsByTempo(45);
            for (Song song : songs) {
                System.out.printf("%.0f %s %s\n", song.getTempo(), song
                        .getArtistName(), song.getTitle());
            }
        }
        catch(EchoNestException e) {
            System.out.println(e.getMessage());
        }

    }
}
