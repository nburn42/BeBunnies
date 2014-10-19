package bebunnies.bebunnies;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
        import com.echonest.api.v4.SongParams;
        import com.echonest.api.v4.Track;

        import java.util.ArrayList;
        import java.util.Comparator;
        import java.util.Collections;
        import java.util.List;
        import com.echonest.api.v4.Playlist;
        import com.echonest.api.v4.PlaylistParams;

/**
 * Created by manavdutta1 on 10/19/14.
 */
public class GenreSearch extends SongSearch{
    private EchoNestAPI en;
    private static String APIKey = "TSPV7YH3UINWQAH6T";

    public GenreSearch() {
        en = new EchoNestAPI(APIKey);
    }


    public List<Song> genreSearch(String genreOne) throws EchoNestException {
        PlaylistParams p = new PlaylistParams();
        p.setResults(80);
        p.setMinDanceability(.4f);
        p.setSongMinHotttnesss(.3f);
        p.setArtistMinFamiliarity(.4f);
        p.addIDSpace("spotify");
        p.includeAudioSummary();
        p.addGenre(genreOne);
        p.includeTracks();
        p.setLimit(true);
        List<Song> songs = en.searchSongs(p);
        List<Song> toReturn = new ArrayList<Song>();
        for(Song song : songs) {
            System.out.println(song);
            song.fetchBucket("audio_summary");
            double value = song.getDouble("audio_summary.valence");
            Track track = song.getTrack("spotify");
            if (value >= .4) {
                toReturn.add(song);
            }
        }
        return toReturn;
    }
    public static void main(String [] args) {
        GenreSearch search = new GenreSearch();
        try {
            List<Song> songs = search.genreSearch("");
            for (Song song : songs) {
                System.out.println(song);
                Track track = song.getTrack("spotify");
                System.out.println(song.getArtistName());
                System.out.println(song.getTitle());
                System.out.println(song.getTempo());
                song.fetchBucket("audio_summary");
                System.out.println(song.getDouble("audio_summary.valence"));
                System.out.println(track.getForeignID());
            }
        }
        catch(EchoNestException e) {
            System.out.println(e.getMessage());
        }

    }
}
