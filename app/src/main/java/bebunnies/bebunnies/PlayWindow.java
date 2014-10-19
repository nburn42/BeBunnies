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

import java.util.Timer;
import java.util.TimerTask;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayWindow extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    public SongSearch songSearch;
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
    public TextView musicTest, currentBpmText, nextBpmText;
    private int currentSeekBar, currentBPM, nextBPM;
    private String songName, artistName;
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

        imageView.setOnClickListener(onClickListener);

        return rootView;
    }

    protected View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!stopped) {
                imageView.setImageResource(R.drawable.stopbutton);
                if (startTimerBool) {
                    try {
                        songSearch = new SongSearch();
                        songSearch = new SearchTask(songSearch, currentSeekBar, currentEnergy).execute().get();
                        returnID = new ReturnID(songSearch).execute().get();
                        returnID = "spotify" + returnID.substring(returnID.indexOf(":"), returnID.length());
                        musicTest.setText(songSearch.getSongName() + ", " + songSearch.getArtistName());
                        MainDrawer.mPlayer.play(returnID);
                    } catch (Exception e) {
                        Log.d(e.getMessage(), "Debug");
                    }
                    startTimerBool = false;
                } else {
                    try {
                        songSearch = new SongSearch();
                        songSearch = new SearchTask(songSearch, currentSeekBar, currentEnergy).execute().get();
                        returnID = new ReturnID(songSearch).execute().get();
                        returnID = "spotify" + returnID.substring(returnID.indexOf(":"), returnID.length());
                        currentBpmText.setText("Current BPM: " + currentSeekBar);
                        MainDrawer.mPlayer.play(returnID);
                        musicTest.setText(songSearch.getSongName() + ", " + songSearch.getArtistName());
                    } catch (Exception e) {
                        Log.d(e.getMessage(), "Debug");
                    }

                }
                stopped = true;
                startTimer();
            } else if (stopped & !startTimerBool) {
                imageView.setImageResource(R.drawable.playbutton);
                stopTimer();
                MainDrawer.mPlayer.pause();
                stopped = false;
            }
        }
    };
    protected SeekBar.OnSeekBarChangeListener onSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
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
    };
    protected SeekBar.OnSeekBarChangeListener onEnergyBarListener = new SeekBar.OnSeekBarChangeListener() {
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
    };

    @Override
    public void onResume() {
        super.onResume();

        seekBar.setOnSeekBarChangeListener(onSeekBarListener);
        energyBar.setOnSeekBarChangeListener(onEnergyBarListener);
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 10000, 10000);
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
                    returnID = new ReturnID(songSearch).execute().get();
                    musicTest.setText(songSearch.songName + ", " + songSearch.artistName);
                    returnID = "spotify" + returnID.substring(returnID.indexOf(":"), returnID.length());
                    currentBpmText.setText("Current BPM: " + currentSeekBar);
                    MainDrawer.mPlayer.play(returnID);
                } catch (Exception e) {
                    Log.d(e.getMessage(), "error");
                }
            }
        };
    }


    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

    }
}