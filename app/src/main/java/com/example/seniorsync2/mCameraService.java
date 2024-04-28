package com.example.seniorsync2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Collections;
import java.util.Objects;
// Camera service class for managing camera operations
class mCameraService {
    private String cameraId; // ID of the camera device
    private final Activity activity; // Reference to the activity using this service
    private final Handler handler; // Handler for communicating with the UI thread
    private CameraDevice cameraDevice; // Camera device for capturing images
    private CameraCaptureSession previewSession; // Session for camera preview
    // Builder for configuring capture requests
    private CaptureRequest.Builder previewCaptureRequestBuilder;
    // Constructor initializing with activity and handler
    mCameraService(Activity _activity, Handler _handler) {
        activity = _activity;
        handler = _handler;
    }
    // Starts the camera preview on the provided surface
    void start(Surface previewSurface) {
        // Get the CameraManager service
        CameraManager cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            // Retrieve the camera ID of the first available camera
            cameraId = Objects.requireNonNull(cameraManager).getCameraIdList()[0];
        } catch (CameraAccessException | NullPointerException | ArrayIndexOutOfBoundsException e) {
            // Log and handle errors if camera access is not available
            Log.e("camera", "No access to camera", e);
            handler.sendMessage(Message.obtain(
                handler,
                mHeartBeatActivity.MESSAGE_CAMERA_NOT_AVAILABLE,
                "No access to camera...."));
        }

        try {
            // Check for camera permissions
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Log.println(Log.ERROR, "camera", "No permission to take photos");
                handler.sendMessage(Message.obtain(
                    handler,
                    mHeartBeatActivity.MESSAGE_CAMERA_NOT_AVAILABLE,
                    "No permission to take photos"));
                return;
            }

            // Exit if no camera ID is found
            if (cameraId == null) {
                return;
            };
            // Open the camera device
            Objects.requireNonNull(cameraManager).openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    // Create a new session for camera preview
                    CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            previewSession = session;
                            try {
                                // Set up the request to start a preview
                                previewCaptureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                previewCaptureRequestBuilder.addTarget(previewSurface); // this is previewSurface
                                previewCaptureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                                // Start a background thread for camera preview
                                HandlerThread thread = new HandlerThread("CameraPreview");
                                thread.start();
                                // Set the repeating request to update the preview
                                previewSession.setRepeatingRequest(previewCaptureRequestBuilder.build(), null, null);

                            } catch (CameraAccessException e) {
                                if (e.getMessage() != null) {
                                    Log.println(Log.ERROR, "camera", e.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            // Log failure in configuring the camera session
                            Log.println(Log.ERROR, "camera", "Session configuration failed");
                        }
                    };

                    try {
                        // Create the capture session for the camera
                        camera.createCaptureSession(Collections.singletonList(previewSurface), stateCallback, null); //1
                    } catch (CameraAccessException e) {
                        if (e.getMessage() != null) {
                            Log.println(Log.ERROR, "camera", e.getMessage());
                        }
                    }
                }
                // Handle camera disconnection.
                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    // Handle camera error.
                }
            }, null);
        } catch (CameraAccessException | SecurityException e) {
            if (e.getMessage() != null) {
                // Log and handle any security or access exceptions
                Log.println(Log.ERROR, "camera", e.getMessage());
                handler.sendMessage(Message.obtain(
                    handler,
                    mHeartBeatActivity.MESSAGE_CAMERA_NOT_AVAILABLE,
                    e.getMessage()));
            }
        }
    }
    // Stop the camera and release resources
    void stop() {
        try {
            cameraDevice.close(); // Close the camera device
        } catch (Exception e) {
            Log.println(Log.ERROR, "camera", "cannot close camera device" + e.getMessage());
        }
    }
}
