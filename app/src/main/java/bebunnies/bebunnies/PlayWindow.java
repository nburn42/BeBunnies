package bebunnies.bebunnies;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayWindow extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    SongSearch songSearch;
    String returnID;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private ImageView imageView;
    private SeekBar seekBar, energyBar;
    private boolean stopped = false, startTimerBool = true;
    private Timer timer;
    private TimerTask timerTask;
    private TextView musicTest, currentBpmText, nextBpmText;
    private int currentSeekBar, currentBPM, nextBPM;
    private float currentEnergy;

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
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.playbutton, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.imageView);

        seekBar = (SeekBar) rootView.findViewById(R.id.seekBar2);
        energyBar = (SeekBar) rootView.findViewById(R.id.seekBar);

        musicTest = (TextView) rootView.findViewById(R.id.textView3);
        currentBpmText = (TextView) rootView.findViewById(R.id.textView4);
        nextBpmText = (TextView) rootView.findViewById(R.id.textView5);


        seekBar.setMax(135);
        currentSeekBar = 123;
        currentBpmText.setText("Current BPM: " + currentSeekBar);

        energyBar.setMax(4);
        energyBar.setBottom(0);
        energyBar.setScrollX(3);
        energyBar.setScrollY(3);

        currentEnergy = .5f;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentSeekBar = i + 55;
                if (startTimerBool) {
                    currentBpmText.setText("Current BPM: " + currentSeekBar);
                }
                nextBpmText.setText("Next BPM: " + currentSeekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        energyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                currentEnergy = i * .2f;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
//                                             Toast.makeText(view.getParent().get,"Loading Song...",Toast.LENGTH_LONG).show();
                                             if (!stopped) {
                                                 imageView.setImageResource(R.drawable.stopbutton);
                                                 if (startTimerBool) {
                                                     try {
                                                         songSearch = new SongSearch();
                                                         songSearch = new SearchTask(songSearch, currentSeekBar, currentEnergy).execute().get();
                                                         returnID = new ReturnID(songSearch).execute().get();
                                                         returnID = "spotify" + returnID.substring(returnID.indexOf(":"), returnID.length());
                                                     } catch (Exception e) {
                                                         Log.d(e.getMessage(), "Debug");
                                                     }
                                                     musicTest.setText(songSearch.getSongName() + ", " + songSearch.getArtistName());
                                                     MainDrawer.mPlayer.play(returnID);

                                                     startTimerBool = false;
                                                 } else {
                                                     Toast.makeText(getActivity(),"Loading...",Toast.LENGTH_SHORT).show();
                                                     try {
                                                         songSearch = new SongSearch();
                                                         songSearch = new SearchTask(songSearch, currentSeekBar, currentEnergy).execute().get();
                                                         currentBpmText.setText("Current BPM: " + currentSeekBar);
                                                         returnID = new ReturnID(songSearch).execute().get();
                                                         returnID = "spotify" + returnID.substring(returnID.indexOf(":"), returnID.length());
                                                         Log.d(returnID, "Return ID");
                                                         musicTest.setText(songSearch.getSongName() + ", " + songSearch.getArtistName());
                                                         MainDrawer.mPlayer.play(returnID);
                                                     } catch (Exception e) {
                                                         Log.d("Debug", e.getMessage());
                                                     }
                                                 }
                                                 startTimer();
                                                 stopped = true;
                                             } else if (stopped & !startTimerBool) {
                                                 imageView.setImageResource(R.drawable.playbutton);
                                                 stopTimer();
                                                 MainDrawer.mPlayer.pause();
                                                 stopped = false;
                                             }
                                         }

                                     }

        );

        return rootView;
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 40000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    songSearch = new SongSearch();
                    songSearch = new SearchTask(songSearch, currentSeekBar, currentEnergy).execute().get();
                    currentBpmText.setText("Current BPM: " + currentSeekBar);
                    returnID = new ReturnID(songSearch).execute().get();
                    returnID = "spotify" + returnID.substring(returnID.indexOf(":"), returnID.length());
                    Log.d(returnID, "RunReturnID");
                    musicTest.setText(songSearch.getSongName() + ", " + songSearch.getArtistName());
                    MainDrawer.mPlayer.play(returnID);
                } catch (Exception e) {
                    Log.d("Debug", e.getMessage());
                }
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

    }
}