package bebunnies.bebunnies;

import android.os.AsyncTask;

/**
 * Created by justinyang on 10/18/14.
 */
public class SearchTask extends AsyncTask<Void, Void, SongSearch> {
    private static SongSearch searching;
    private static String id;
    private static int tempo;
    private static float energy;

    public SearchTask(SongSearch x, int tempo, float energy) {
        searching = x;
        this.tempo = tempo;
        this.energy = energy;
    }

    @Override
    protected SongSearch doInBackground(Void... voids) {
        try {
            searching.goodSongSearch(tempo, energy);
            return searching;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
//        return searching;
    }

}