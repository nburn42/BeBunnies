package bebunnies.bebunnies;

import android.os.AsyncTask;

/**
 * Created by justinyang on 10/18/14.
 */
public class ReturnID extends AsyncTask<Void, Void, String> {
    private SongSearch searching;

    public ReturnID(SongSearch x) {
        searching = x;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String foreignID = searching.returnForeignID();
            return foreignID;
        } catch (Exception e) {
            String error = e.getMessage();
            return error;
        }
    }

    public void setSearch(SongSearch songSearch) {
        searching = songSearch;
    }


}
