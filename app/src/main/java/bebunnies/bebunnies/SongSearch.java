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
 * @ author: Manav Dutta
 * @ version: 2.0
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
    public List<Song> goodSongSearch(int tempo) throws EchoNestException {
        SongParams p = new SongParams();
        p.setMinTempo(tempo);
        p.setMaxTempo(tempo + .99f);
        p.setResults(80);
        p.setMinDanceability(.4f);
        p.setMinArtistHotttnesss(.6f);
        p.setMinSongHotttnesss(.6f);
        p.setMinArtistFamiliarity(.6f);
        p.setArtistActiveDuring(2007, 2014);
        p.includeAudioSummary();
        List<Song> songs = en.searchSongs(p);
        List<Song> toReturn = new ArrayList<Song>();
        for(Song song : songs) {
           song.fetchBucket("audio_summary");
           double value = song.getDouble("audio_summary.valence");
           if (value >= .4) {
              toReturn.add(song);
           }
        }
        return toReturn;
    }
    public Playlist returnPlayList(int results) {
           PlaylistParams params = new PlaylistParams();
           params.setMinTempo(100);
           params.setResults(results);
           params.setMinDanceability(.4f);
           params.includeAudioSummary();
           params.setSongMinHotttnesss(.5f);
           params.setArtistMinFamiliarity(.3f);
           try {
               Playlist playlist = en.createStaticPlaylist(params);
               return playlist;
           }
           catch(EchoNestException e) {
               System.out.println(e.getMessage());
           }
           return new Playlist(new ArrayList<Song>());
    }
    public static void main(String [] args) {
        SongSearch search = new SongSearch();
        try {
            List<Song> songs = search.goodSongSearch(100);
            System.out.println(songs.size());
            for (Song song : songs) {
                    song.fetchBucket("audio_summary");
                    Track track = song.getTrack("spotify-WW");
                    System.out.println(song.getArtistName());
                    System.out.println(song.getTitle());
                    System.out.println(song.getDouble("audio_summary.valence"));
            }
        }
        catch(EchoNestException e) {
            System.out.println(e.getMessage());
        }

    }
}
