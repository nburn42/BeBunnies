package bebunnies.bebunnies;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

/**
 * @author: Manav Dutta
 * @version: 2.0
 * This is the class that pulls songs from EchoNest
 */
public class SongSearch {
    private EchoNestAPI en;
    private static String APIKey = "TSPV7YH3UINWQAH6T";

    /**
     * This constructs a new song search instance
     */
    public SongSearch() {
        en = new EchoNestAPI(APIKey);
    }

    /**
     * This does the whole searching. It searches for songs by tempo.
     * @param min: the min tempo we want
     * @param max: the max tempo we want
     * @param results: the results we obtain
     * @return the list of songs
     * @throws EchoNestException
     */
    public List<Song> searchSongsByTempo(int min, int max, int results)
            throws EchoNestException {
        SongParams p = new SongParams();
        p.setMinTempo(min);
        p.setMaxTempo(max);
        p.setResults(results);
        List<Song> songs = en.searchSongs(p);
        Collections.sort(songs, new Comparator<Song>() {
            public int compare(Song o1, Song o2) {
                 if (o1 == null || o2 == null) {
                     throw new NullPointerException();
                 }
                 try {
                         return (int) (o1.getTempo() - o2.getTempo());
                 }
                 catch (EchoNestException e) {
                         System.out.println(e.getMessage());
                  }
                return 0;
            }
        });
        for (Song song : songs) {
            System.out.printf("%.0f %s %s\n", song.getTempo(), song
                    .getArtistName(), song.getTitle());
        }
        return songs;
    }
    public static void main(String [] args) {
        SongSearch search = new SongSearch();
        try {
            List<Song> songs = search.searchSongsByTempo(50, 450, 20);
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
