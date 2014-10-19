package bebunnies.bebunnies;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlayWindow extends Fragment implements SensorEventListener {
    //private static final String ARG_SECTION_NUMBER = "section_number";
    private final int bufferSize = 1000;
    private double[] motionData = new double[bufferSize];
    private final float NOISE = (float) 2.0;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private ImageView imageView ;
    private boolean paused = false;
    private int motionDataIndex = 0;
    private long lastSyncTime = -1;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int newBufferLength = 10;
    private long[] lastTimes = new long[newBufferLength];

    //double bucket = 0;

    //Timer dataTimer;
    //TimerTask dataTimerTask;
    private double[] lastPeaks = new double[newBufferLength];
    private int lastPeakIndex = 0;
    private boolean up = true;
    private double currentMax = 0;
    private long currentPeakTime = 0;
    private double threshold = 0;
    private double rate = 100;
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

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!paused) {
                    MainDrawer.mPlayer.pause();
                    paused = true;
                } else {
                    MainDrawer.mPlayer.resume();
                    paused = false;
                }
            }
        });*/

        mSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        for (int i = 0; i < newBufferLength; i++) {
            lastTimes[i] = 0;
        }

        /*dataTimer = new Timer();
        dataTimerTask = new TimerTask() {
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY);

                motionData[motionDataIndex] = bucket;
                motionDataIndex += 1;



                if(motionDataIndex >= bufferSize) {
                    motionDataIndex = 0;
                    DoubleFFT_1D fft = new DoubleFFT_1D(bufferSize);

                    fft.realForward(motionData);

                    //in seconds
                    double sampleTime = 0.03 * bufferSize; // convert to seconds
                    // in samples per second
                    double dataFrequency = bufferSize/sampleTime;

                    //50 bpm min sample rate
                    //0.333 / sec
                    double minFreq = 0.3333;
                    int minSample = (int)(minFreq * (bufferSize/dataFrequency));

                    // 200 bpm max sample rate
                    // 3.33333 /sec
                    double maxFreq = 3.3333;
                    int maxSample = (int)(maxFreq * (bufferSize/dataFrequency)) + 1;

                    Log.d(bufferSize + "", "buffersize");
                    Log.d(sampleTime + "", "sampleTime");
                    Log.d(dataFrequency + "", "dataFrequency");
                    Log.d(minSample + "", "minSample");
                    Log.d(maxSample + "", "maxSample");

                    int maxSampleIndex = -1;
                    double maxSampleValue = -1;
                    for(int i = minSample; i <= maxSample; i++)
                    {
                        Log.d(motionData[i] + "", i + "");
                        if(Math.abs(motionData[i]) > maxSampleValue)
                        {
                            maxSampleValue = Math.abs(motionData[i]);
                            maxSampleIndex = i;
                        }
                    }

                    if(maxSampleIndex > 0)
                    {
                        double peakFrequencyLow = maxSampleIndex *(dataFrequency/bufferSize);
                        double peakFrequencyHigh = (maxSampleIndex + 1) *(dataFrequency/bufferSize);
                        Log.d(peakFrequencyLow + "", "peakFequency");
                        Log.d(peakFrequencyHigh + "", "peakFequency");
                        Log.d(maxSampleIndex + "", "maxSampleIndex");
                    }
                    else
                    {
                        Log.d("Failed to get good data", "onSensorChanged");
                    }

                }

            }
        };

        // 30 milisecond runs
        dataTimer.schedule(dataTimerTask, 30, 30);

        */

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    public void onResume() {
        super.onResume();
        //mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        super.onPause();
        //mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        //double abs = Math.pow((x*x) + (y*y) + (z*z), 0.5);
        double abs = y;//+z;
        //Math.abs(abs - SensorManager.GRAVITY_EARTH);

        if (abs > threshold || abs < -threshold) {
            if (abs > 0) {
                if (up) {
                    if (abs > currentMax) {
                        currentMax = abs;
                        currentPeakTime = System.currentTimeMillis();
                    }
                } else // reverse direction
                {
                    lastPeakIndex = (lastPeakIndex + 1) % newBufferLength;
                    lastTimes[lastPeakIndex] = currentPeakTime;
                    lastPeaks[lastPeakIndex] = currentMax;

                    currentMax = 0;
                    currentPeakTime = 0;
                    up = !up;

                    //calculate new rate
                    //drop 2 higest and lowest then average
                    ArrayList<Long> news = new ArrayList<Long>();
                    for (int i = 0; i < newBufferLength; i++) {
                        news.add(lastTimes[(lastPeakIndex - i + newBufferLength) % newBufferLength] - lastTimes[(lastPeakIndex - i - 1 + newBufferLength + newBufferLength) % newBufferLength]);
                    }


                    Collections.sort(news);

                    double total = newBufferLength - 5;
                    long average = 0;
                    for (int i = 2; i < newBufferLength - 2; i++) {
                        average += news.get(i) / total;
                    }

                    Log.d(average + "", "");

                    rate = average;
                    threshold = Math.abs(currentMax / 4.0);
                }
            } else {
                if (up) // reverse direction
                {
                    lastPeakIndex = (lastPeakIndex + 1) % newBufferLength;
                    lastTimes[lastPeakIndex] = currentPeakTime;
                    lastPeaks[lastPeakIndex] = currentMax;

                    currentMax = 0;
                    currentPeakTime = 0;
                    up = !up;

                    //calculate new rate
                    //drop 2 higest and lowest then average
                    ArrayList<Long> news = new ArrayList<Long>();
                    for (int i = 0; i < newBufferLength; i++) {
                        news.add(lastTimes[(lastPeakIndex - i + newBufferLength) % newBufferLength] - lastTimes[(lastPeakIndex - i - 1 + newBufferLength + newBufferLength) % newBufferLength]);
                    }

                    Collections.sort(news);

                    double total = newBufferLength - 5;
                    long average = 0;
                    for (int i = 2; i < newBufferLength - 2; i++) {
                        average += news.get(i) / total;
                    }
                    Log.d(average + "", "");

                    rate = average;
                    threshold = Math.abs(currentMax / 4.0);
                } else {
                    if (abs < currentMax) {
                        currentMax = abs;
                        currentPeakTime = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}