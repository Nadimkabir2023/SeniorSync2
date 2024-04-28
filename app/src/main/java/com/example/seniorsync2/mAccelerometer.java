package com.example.seniorsync2;
// Importing necessary Android and Java libraries.
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
// Defines mAccelerometer class that handles accelerometer data to compute respiratory rate
public class mAccelerometer extends AppCompatActivity implements SensorEventListener {
    // Manages sensor hardware
    SensorManager sensorManager;
    // List to store accelerometer z-axis values
    ArrayList<Float> zList = new ArrayList<>();
    // Flag to indicate readiness to capture data
    boolean ready = true;
    // onCreate method sets up the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_accelerometer); // Setting the UI layout from XML
        // Getting the system's SensorService and registering the accelerometer sensor
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        // Linking UI TextView component for displaying the timer
        TextView timer = findViewById(R.id.timer);
        // Setting up a countdown timer for data capture.
        new CountDownTimer(30000, 800) { // 30 seconds total, ticks every 0.8 seconds

            // Updates timer display and sets ready to true every tick
            public void onTick(long millisUntilFinished) {
                timer.setText(Long.toString(millisUntilFinished/1000) + "s");
                ready = true;
                // logic to set the EditText could go here
            }
            // Processes the data when timer finishes
            public void onFinish() {
                timer.setText("0s");
                ready = false;
                Log.i("helloz", zList.toString());  // Logging z-axis data
                double[] arr = new double[zList.size()]; // Converting ArrayList to array for processing

                for (int i = 0; i < zList.size(); i++) {
                    arr[i] = zList.get(i);
                }

                // Process the data to find respiratory rate
                double arr2[] = ComputeMovingAverage(arr, arr.length, 3); // Smoothing the data

                //double arr3[] = ComputeMovingAverage(arr2, arr2.length, 3);
                int peaks = printPeaksTroughs(arr2, arr2.length);
                Log.i("hellopeaks", Integer.toString(peaks*2));

                // Calculating the standard deviation of original data
                double sum = 0;
                for (int i = 0; i < arr.length; i++) {
                    sum += arr[i];
                }

                double avg = (double) sum/arr.length;

                double sum2 = 0;
                for (int i = 0; i < arr.length; i++) {
                    sum2 += Math.pow(arr[i] - avg, 2);
                }

                double var = (double) sum2/(arr.length - 1); // Logging standard deviation
                double sd = Math.sqrt(var);

                Log.i("hellosd", Arrays.toString(arr));
                Log.i("hellosd", Double.toString(sd));
                // Creating intent to start result display activity
                Intent intent = new Intent(mAccelerometer.this, mResultActivity.class);
                intent.putExtra("name", "Respiratory Rate");
                intent.putExtra("score",  sd < 0.035 ? -1 : peaks*2); // Passing respiratory rate as extra
                intent.putExtra("normal", "12 - 16"); // Normal range for reference
                startActivity(intent); // Starting the activity
                finish(); // Ending current activity
            }

        }.start(); // Starting the timer

    }
    // Handles changes in sensor data
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Checks for accelerometer data type and readiness to capture.
        if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            if (ready) {
                ready = false;// Resetting the flag to prevent multiple captures

                zList.add(sensorEvent.values[2]); // Adding z-axis value to list
            }
        }
    }
    // This method can be used if there is a need to respond to sensor accuracy changes
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    // Computes a moving average to smooth out the data series
    public double[] ComputeMovingAverage(double arr[], int N, int K)
    {
        double[] arr_new = new double[N];
        int i;
        float sum = 0;
        for (i = 0; i < K; i++) {
            sum += arr[i];  // Initial sum of the first 'windowSize' elements
            arr_new[i] = (sum / K);
        }
        for (i = K; i < N; i++) {
            sum -= arr[i - K]; // Sliding window sum calculation
            sum += arr[i];
            arr_new[i] = (sum / K); // Averaging over the window
        }
        return arr_new;
    }

    // Determines if an element is a peak
    public boolean isPeak(double arr[], int n, double num, int i, int j)
    {
        if (i >= 0 && arr[i] > num)
            return false;  // Not a peak if neighbors are greater

        if (j < n && arr[j] > num)
            return false;
        return true;
    }
    // Counts peaks and troughs in the data array
    public int printPeaksTroughs(double arr[], int n)
    {
        int count=0;
        //System.out.print("Peaks : ");

        for (int i = 0; i < n; i++)
        {
            if (isPeak(arr, n, arr[i], i - 1, i + 1))
            {
                count++; // Increment count for each peak
            }
        }
        return count;
    }
}