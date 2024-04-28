package com.example.seniorsync2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// Class definition for monitoring activity using camera data
public class mMonitorActivity extends AppCompatActivity {

    private SurfaceView preview = null; // View for displaying the camera preview
    private static SurfaceHolder previewHolder = null; // Holder for the camera preview
    private static Camera camera = null; // Camera object to interface with the Android camera hardware
    private static PowerManager.WakeLock wakeLock = null; // Wake lock to keep the screen on while monitoring
    public boolean complete = false; // Flag to check if monitoring is complete

    private static long startTime = 0; // To track the start time of monitoring
    private ProgressBar progress; // Progress bar to display progress visually
    public int ProgP = 0; // Progress percentage
    public int inc = 0; // Increment value for progress
    public int counter = 0; // Counter to track the number of frames processed

    public ArrayList<ArrayList<Double>> AvgList = new ArrayList<>(); // List to hold average values of measurements

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_monitor); // Setting the layout for the activity

        preview = findViewById(R.id.preview); // Getting the SurfaceView from the layout
        previewHolder = preview.getHolder(); // Getting the holder from the SurfaceView
        previewHolder.addCallback(surfaceCallback);  // Adding a callback to handle surface events
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // Setting the type of SurfaceHolder
        progress = findViewById(R.id.HRPB);  // Getting the ProgressBar from the layout
        progress.setProgress(0); // Setting initial progress to 0

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE); // Getting the PowerManager service
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, ":DoNotDimScreen"); // Creating a wake lock to keep the screen on
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig); // Handling configuration changes
    }

    @Override
    public void onResume() {
        super.onResume();
        wakeLock.acquire(); // Acquiring the wake lock
        camera = Camera.open(); // Opening the camera
        camera.setDisplayOrientation(90); // Setting the camera orientation
        startTime = System.currentTimeMillis(); // Storing the start time
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release(); // Releasing the wake lock
        camera.setPreviewCallback(null); // Removing the preview callback
        camera.stopPreview();  // Stopping the camera preview
        camera.release(); // Releasing the camera
        camera = null; // Nullifying the camera object
    }

    private final Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = cam.getParameters().getPreviewSize(); // Get the size of the preview frames
            if (size == null) throw new NullPointerException();

            if (!complete) { // Check if monitoring is not complete

                int width = size.width; // Get width from size
                int height = size.height; // Get height from size

                double[] avg = mImageProcessing.decode(data.clone(), height, width); // Process the image data

                if (avg[0] < 200) {
                    Toast.makeText(mMonitorActivity.this, "Retrying measurement", Toast.LENGTH_SHORT).show();
                } else {
                    // Store the processed averages
                    ArrayList<Double> avgArr = new ArrayList<>();
                    avgArr.add(avg[0]);
                    avgArr.add(avg[1]);
                    avgArr.add(avg[2]);
                    avgArr.add(avg[3]);
                    avgArr.add(avg[4]);
                    avgArr.add(avg[5]);

                    AvgList.add(avgArr); // Add to list of averages
                }

                long endTime = System.currentTimeMillis(); // Get the current time
                double totalTimeInSecs = (endTime - startTime) / 1000d; // Calculate the total time passed
                if (totalTimeInSecs >= 15) {
                    complete = true; // Mark completion if 15 seconds have passed
                    Log.i("helloavg", Integer.toString(AvgList.size())); // Log the number of averages computed

                    // send result data to the server
                    String url = "https://visara-api.herokuapp.com/"; // Define the server URL

                    try {
                        JSONArray arr = new JSONArray(); // Create a new JSON array
                        // Calculate average values for RGB and their standard deviations
                        double sumr = 0, sumg = 0, sumb = 0, sdr = 0, sdg = 0, sdb = 0;
                        for (ArrayList<Double> d : AvgList) {
                            sumr += d.get(0);
                            sdr += d.get(1);
                            sumg += d.get(2);
                            sdg += d.get(3);
                            sumb += d.get(4);
                            sdb += d.get(5);
                        }
                        // Put averaged values into the JSON array
                        arr.put((double) sumr / AvgList.size());
                        arr.put((double) sdr / AvgList.size());
                        arr.put((double) sumg / AvgList.size());
                        arr.put((double) sdg / AvgList.size());
                        arr.put((double) sumb / AvgList.size());
                        arr.put((double) sdb / AvgList.size());
                        // Create a JSON object to hold the data
                        JSONObject jsonParams = new JSONObject();
                        jsonParams.put("images", arr);
                        // Create a POST request with the JSON object
                        JsonObjectRequest request = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonParams,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            Intent intent = new Intent(mMonitorActivity.this, mResultActivity.class);
                                            intent.putExtra("name", "Blood Oxygen");
                                            intent.putExtra("score", response.getDouble("spo2") < 70 || response.getDouble("spo2") > 120 ? -1 : (int) response.getDouble("spo2"));
                                            intent.putExtra("normal", "92 - 99");
                                            startActivity(intent);
                                            finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                },

                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Log and handle any errors from the server response
                                        Log.i("helloerror", error.getMessage());
                                        // Handle the error

                                    }
                                }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                // Set headers for the request
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Content-Type", "application/json");
                                return params;
                            }
                        };
                        // Add the request to the Volley request queue
                        Volley.newRequestQueue(getApplicationContext()).
                                add(request);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                ++counter;

                ProgP = inc++ / 16;
                progress.setProgress(ProgP);

            }
        }
    };

    private final SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        // Handle the response from the server
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                // Create an intent to start the result activity
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            } catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // Handle surface changes, such as rotations
            Camera.Parameters parameters = camera.getParameters();// Get camera parameters
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH); // Set the flash mode

            Camera.Size size = getSmallestPreviewSize(width, height, parameters); // Get the smallest preview size that fits the surface
            camera.setParameters(parameters); // Set the modified parameters back to the camera
            camera.startPreview(); // Start the camera preview
        }


        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // Handle the surface being destroyed
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        // Method to get the smallest size for camera preview
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea < resultArea) result = size;
                }
            }
        }
        return result; // Return result
    }
}
