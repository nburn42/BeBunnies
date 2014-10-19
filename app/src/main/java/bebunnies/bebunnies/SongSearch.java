package bebunnies.bebunnies;

import android.util.Log;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Song;
import com.echonest.api.v4.SongParams;
import com.echonest.api.v4.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * @ author: Manav Dutta
 * @ version: 2.0
 * This is the class that pulls songs from EchoNest
 */
public class SongSearch {
    private static String APIKey = "TSPV7YH3UINWQAH6T";
    private EchoNestAPI en;
    private List<Song> idsList;
    private String songName, artistName;


    /**
     * This constructs a new song search instance
     */
    public SongSearch() {
        en = new EchoNestAPI(APIKey);
        idsList = new ArrayList<Song>();
    }
    public String getSongName(){
        return songName;
    }
    public String getArtistName(){
        return artistName;
    }

    public void goodSongSearch(int tempo, float energy) throws EchoNestException {
        SongParams p = new SongParams();
        p.setResults(20);

        p.setMinTempo(tempo);
        p.setMaxTempo(tempo + 5);

        p.setMinEnergy(energy);
        p.setMaxEnergy(energy+.18f);

//        p.setMinDanceability(.4f);
        p.setMinSongHotttnesss(.3f);
//        p.setMinArtistFamiliarity(.4f);

        p.addIDSpace("spotify");
        p.includeAudioSummary();
        p.includeTracks();
        p.setLimit(true);
        List<Song> songs = en.searchSongs(p);
        List<Song> toReturn = new ArrayList<Song>();
        for (Song song : songs) {
            song.fetchBucket("audio_summary");
            double value = song.getDouble("audio_summary.valence");
            Track track = song.getTrack("spotify");
            if (value >= .4) {
                toReturn.add(song);
            }
        }
        idsList = toReturn;
    }


    public String returnForeignID() {
        int randomNumber = (int) (Math.floor(Math.random() * idsList.size()));
        Song removed = idsList.get(randomNumber);
        songName = idsList.get(randomNumber).getTitle();
        artistName = idsList.get(randomNumber).getArtistName();
        try {
            String id = removed.getTrack("spotify").getForeignID();
            Log.d(id, "Return Foreign");
            return id;
        } catch (Exception e) {
            String message = e.getMessage();
            return e.getMessage();
        }

    }

}
