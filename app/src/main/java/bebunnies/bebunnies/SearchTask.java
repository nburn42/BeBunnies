package bebunnies.bebunnies;

import android.os.AsyncTask;

/**
 * Created by justinyang on 10/18/14.
 */
public class SearchTask extends AsyncTask<Void, Void, SongSearch> {
    private static SongSearch searching;
    private static String id;
    public SearchTask(SongSearch x) {
        searching = x;
    }
    @Override
    protected SongSearch doInBackground(Void... voids) {
        try {
            searching.goodSongSearch(90);
            return searching;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
//        return searching;
    }

}