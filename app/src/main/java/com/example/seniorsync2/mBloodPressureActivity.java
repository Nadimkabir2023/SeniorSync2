package com.example.seniorsync2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
// The main class definition that handles blood pressure measurement using camera data
public class mBloodPressureActivity extends AppCompatActivity {
    // ArrayList to store pixel intensity data
    public ArrayList<Integer> array;
    // Constant identifiers for different types of messages handled by the handler
    public static final int MESSAGE_UPDATE_REALTIME = 1;
    public static final int MESSAGE_UPDATE_FINAL = 2;
    public static final int MESSAGE_CAMERA_NOT_AVAILABLE = 3;
    // Boolean to avoid re-sharing camera data
    private boolean justShared = false;
    // Handler to manage UI updates from background threads
    @SuppressLint("HandlerLeak")
    private final Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            // Handles camera availability errors
            if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                Log.println(Log.WARN, "camera", msg.obj.toString());
            }
        }
    };

    // Method to determine if a value in an array is a peak
    public boolean isPeak(double arr[], int n, double num, int i, int j)
    {
        if (i >= 0 && arr[i] >= num)
            return false;

        if (j < n && arr[j] >= num)
            return false;
        return true;
    }
    // Method to count the number of peaks in the data array
    public int printPeaksTroughs(double arr[], int n)
    {
        int count=0;

        for (int i = 0; i < n; i++)
        {
            if (isPeak(arr, n, arr[i], i - 1, i + 1))
            {
                count++;
            }
        }
        return count;
    }
    // Method to compute the Ejection Time (ET) from peak data
    public double getET(double arr[], int n, int peaks)
    {
        double[][] arr1 = new double[peaks][2];
        int j = 0;

        for (int i = 0; i < n; i++)
        {
            if (isPeak(arr, n, arr[i], i - 1, i + 1))
            {
                arr1[j++] = new double[]{arr[i], i*50};
            }
        }

        int mid1 = arr1.length/2;
        int mid2 = arr1.length/2 + 1;

        double et;
        if (arr1[mid1][0] > arr1[mid2][0]) {
             et = arr1[mid1][1] - arr1[mid1 - 1][1];
        } else {
            et = arr1[mid2][1] - arr1[mid1][1];
        }

        return et;
    }
    // Camera service object for handling camera operations
    private final mCameraService cameraService = new mCameraService(this, mainHandler);

    @Override
    protected void onResume() {
        super.onResume();
        // Sets up the camera preview surface
        TextureView cameraTextureView = findViewById(R.id.textureView);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();

        if ((previewSurfaceTexture != null) && !justShared) {
            Surface previewSurface = new Surface(previewSurfaceTexture);

            cameraService.start(previewSurface);
            measurePulse(cameraTextureView, cameraService);
        }
    }
    // Method to measure the pulse by analyzing camera data
    void measurePulse(TextureView textureView, mCameraService cameraService) {
        final int measurementInterval = 45; // Time between measurements in ms
        final int measurementLength = 15000; // Total duration of measurements in ms
        final int clipLength = 3500; // Initial period to ignore in ms
        final int[] ticksPassed = {0}; // Counter for the number of ticks passed
        // Timer to manage periodic data capture
        CountDownTimer timer = new CountDownTimer(measurementLength, measurementInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (clipLength > (++ticksPassed[0] * measurementInterval)) return;
                // Captures camera data, computes red component average, and stores it
                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    Bitmap newBitmap = Bitmap.createScaledBitmap(currentBitmap, 84, 84, false);
                    int pixelCount = newBitmap.getWidth() * newBitmap.getHeight();
                    int measurement = 0;
                    int[] pixels = new int[pixelCount];

                    newBitmap.getPixels(pixels, 0, newBitmap.getWidth(), 0, 0, newBitmap.getWidth(), newBitmap.getHeight());

                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        measurement += (pixels[pixelIndex] >> 16) & 0xff; // Extracts the red component
                    }

                    array.add(measurement);  // Adds the measurement to the array

                });
                thread.start();  // Starts the thread
            }

            @Override
            public void onFinish() {
                // Logs array data and processes it to compute blood pressure
                Log.i("helloarr", array.toString());

                double[] arr = new double[array.size()];

                for (int i = 0; i < array.size(); i++) {
                    arr[i] = array.get(i);
                }

                int peaks = printPeaksTroughs(arr, arr.length);
                int hr = peaks*2; // Calculates heart rate
                Log.i("hellopeak", Integer.toString(peaks*2));

                double et = getET(arr, arr.length, peaks);  // Calculates ET
                Log.i("helloet", Double.toString(et));
                // Calculates blood pressure related metrics
                int gender = 1;
                double W = 75;
                double H = 6;
                int age = 20;

                double bsa = 0.007184*Math.pow(W, 0.425)*Math.pow(H, 0.725); // Body surface area formula
                Log.i("hellobsa", Double.toString(bsa));
                double sv = -6.6 + 0.25*(et - 35) - 0.62*hr + 40.4*bsa - 0.51*age; // Stroke volume formula
                Log.i("hellosv", Double.toString(sv));
                double pp = sv/((0.013*W - 0.007*age - 0.004*hr) + 1.307); // Pulse pressure formula
                Log.i("hellopp", Double.toString(pp));

                double sp = (93.33 + 1.5*pp); // Systolic pressure formula
                Log.i("hellosp", Double.toString(sp));
                double dp = (93.33 - pp/3); // Diastolic pressure formula
                Log.i("hellodp", Double.toString(dp));

                if (cameraService != null)
                    cameraService.stop(); // Stops the camera service.

                // Prepares intent to start result activity and passes blood pressure data.
                Intent intent = new Intent(mBloodPressureActivity.this, mResultActivity.class);
                intent.putExtra("name", "Blood Pressure");
                intent.putExtra("score", Integer.toString((int) sp) + "/" + Integer.toString((int) dp));
                intent.putExtra("normal", "90/60 - 120/80");
                startActivity(intent); // Starts the result activity
                finish(); // Finishes the current activity
            }
        };

        timer.start(); // Starts the timer
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraService.stop(); // Stops the camera service to release resources
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_heartbeat);  // Sets the layout for this activity

        array = new ArrayList<>(); // Initializes the array to store measurement data
    }
}