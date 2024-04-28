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
// Main activity class for measuring heart rate using the camera
public class mHeartBeatActivity extends AppCompatActivity {
    public ArrayList<Integer> array; // Stores the red component values for pulse calculation
    // Message constants for Handler to manage UI updates or error handling
    public static final int MESSAGE_UPDATE_REALTIME = 1;
    public static final int MESSAGE_UPDATE_FINAL = 2;
    public static final int MESSAGE_CAMERA_NOT_AVAILABLE = 3;



    private boolean justShared = false;  // Flag to prevent re-sharing of the camera

    // Handler to process messages and run code on the UI thread
    @SuppressLint("HandlerLeak")
    private final Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_CAMERA_NOT_AVAILABLE) {
                Log.println(Log.WARN, "camera", msg.obj.toString());
            }
        }
    };
    // Method to determine if an index in an array represents a peak
    public boolean isPeak(double arr[], int n, double num, int i, int j)
    {
        if (i >= 0 && arr[i] >= num)
            return false;

        if (j < n && arr[j] >= num)
            return false;
        return true;
    }
    // Method to count the number of peaks in an array of doubles
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
    // Camera service object for handling camera functionality
    private final mCameraService cameraService = new mCameraService(this, mainHandler);

    @Override
    protected void onResume() {
        super.onResume();
        // Obtain the TextureView from the layout and prepare the camera
        TextureView cameraTextureView = findViewById(R.id.textureView);
        SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();
        // Start the camera if the TextureView is ready and the camera has not just been shared
        if ((previewSurfaceTexture != null) && !justShared) {
            Surface previewSurface = new Surface(previewSurfaceTexture);

            cameraService.start(previewSurface);
            measurePulse(cameraTextureView, cameraService);
        }
    }

    // Method to start measuring pulse based on the camera feed
    void measurePulse(TextureView textureView, mCameraService cameraService) {

        final int measurementInterval = 45; // Measurement interval in milliseconds
        final int measurementLength = 15000; // Total measurement duration in milliseconds
        final int clipLength = 3500; // Initial delay in milliseconds before starting measurement
        final int[] ticksPassed = {0}; // Counter for the number of ticks passed
        // Timer for controlling measurement intervals and duration
        CountDownTimer timer = new CountDownTimer(measurementLength, measurementInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (clipLength > (++ticksPassed[0] * measurementInterval)) return;
                // Run the measurement in a separate thread to not block the UI thread
                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    Bitmap newBitmap = Bitmap.createScaledBitmap(currentBitmap, 84, 84, false);
                    int pixelCount = newBitmap.getWidth() * newBitmap.getHeight();
                    int measurement = 0;
                    int[] pixels = new int[pixelCount];
                    // Extract the red component of each pixel and sum them up
                    newBitmap.getPixels(pixels, 0, newBitmap.getWidth(), 0, 0, newBitmap.getWidth(), newBitmap.getHeight());

                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        measurement += (pixels[pixelIndex] >> 16) & 0xff;
                    }

                    array.add(measurement);

                });
                thread.start();
            }

            @Override
            public void onFinish() {
                // Log the results and prepare for display in the next activity
                Log.i("helloarr", array.toString());

                double[] arr = new double[array.size()];

                for (int i = 0; i < array.size(); i++) {
                    arr[i] = array.get(i);
                }

                int peaks = printPeaksTroughs(arr, arr.length);
                Log.i("hellopeak", Integer.toString(peaks*2));
                // Stop the camera service
                if (cameraService != null)
                    cameraService.stop();
                // Start the results activity with the calculated heart rate
                Intent intent = new Intent(mHeartBeatActivity.this, mResultActivity.class);
                intent.putExtra("name", "Heart Rate");
                intent.putExtra("score", (peaks*2) < 50 ? -1 : peaks*2);
                intent.putExtra("normal", "60 - 100");
                startActivity(intent);
                finish();
            }
        };

        timer.start(); // Start the timer
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraService.stop(); // Stop the camera service when the activity is paused
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_heartbeat); // Set the content view from XML layout

        array = new ArrayList<>(); // Initialize the array to store pulse data
    }
}