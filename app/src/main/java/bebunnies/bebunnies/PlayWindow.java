package bebunnies.bebunnies;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayWindow extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    SongSearch songSearch ;
    String returnID;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private ImageView imageView;
    private boolean stopped = false, ranOnce = false;

    public PlayWindow() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlayWindow newInstance() {
        PlayWindow fragment = new PlayWindow();
        Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.playbutton, container, false);
        imageView = (ImageView) rootView.findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             //if (!ranOnce) {
                                                 try {
                                                     songSearch = new SongSearch();
                                                     songSearch = new SearchTask(songSearch).execute().get();
                                                     returnID = new ReturnID(songSearch).execute().get();
                                                     Log.d(returnID, "returnID");
                                                     returnID.replaceAll("spotify-CA","spotify");
                                                     MainDrawer.mPlayer.play(returnID);
                                                     ranOnce = true;
                                                 } catch (Exception e) {
                                                     Log.d(e.toString(),"FDF");
                                                 }
                                             /*} else {
                                                 if (!stopped) {
                                                     try {
//                                                         songSearch.removeFirst();
//                                                         ReturnID.setSearch(songSearch);
                                                         int index = returnID.indexOf("-");
                                                         int index1 = returnID.indexOf(":");
                                                         returnID = returnID.substring(0, index) + returnID.substring(index1, returnID.length());
                                                         MainDrawer.mPlayer.play(returnID);
                                                     } catch (Exception e) {
                                                         Log.d("Debug", e.getMessage());
                                                     }
                                                     imageView.setImageResource(R.drawable.stopbutton);
                                                     stopped = true;
                                                 } else if (stopped) {
                                                     imageView.setImageResource(R.drawable.playbutton);
                                                     MainDrawer.mPlayer.pause();
                                                     stopped = false;
                                                 }
                                             }*/
                                         }
                                     }

        );

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }
}