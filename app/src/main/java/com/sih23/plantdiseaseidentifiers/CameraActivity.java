package com.sih23.plantdiseaseidentifiers;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.MeteringPointFactory;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.common.util.concurrent.ListenableFuture;
import com.sih23.plantdiseaseidentifiers.databinding.ActivityCameraBinding;
import com.sih23.plantdiseaseidentifiers.utils.ImageUtils;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {
    public static final String OPEN_MY_PLANT_FRAGMENT_PRAM = "showImage";
    private static final String TAG_LOG = CameraActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS =
            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    PreviewView previewView;
    private ActivityCameraBinding binding;
    private ImageCapture imageCapture = null;
    private CameraControl cameraControl = null;

    private CameraInfo cameraInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS);
        }

        binding.backButton.setOnClickListener(v -> finish());

        // Camera Preview
        previewView = binding.viewFinder;
        // Set up the listeners for take photo and video capture buttons
        binding.imageCaptureButton.setOnClickListener(v -> takePhoto());
    }

    private void takePhoto() {
        File file = new File(getExternalFilesDir(null), "sample" + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Show image review
                        ImageView previewImage = binding.captureImagePreview;
                        previewImage.setVisibility(View.VISIBLE);
                        Bitmap bitmap = ImageUtils.getCorrectOrientatedBitmap(outputFileResults
                                .getSavedUri()
                                .getPath()
                        );
                        previewImage.setImageBitmap(bitmap);
                        // Update camera UI
                        binding.flashButton.setVisibility(View.INVISIBLE);
                        binding.addPhotoButton.setIconResource(R.drawable.ic_cancel_24);
                        binding.imageCaptureButton.setIconResource(R.drawable.ic_select_24);

                        binding.imageCaptureButton.setOnClickListener(v -> {
                            Intent intent = new Intent(CameraActivity.this, PlantActivity.class);
                            intent.putExtra(OPEN_MY_PLANT_FRAGMENT_PRAM, 3);
                            startActivity(intent);
                            finish();
                        });
                        // Retake photo
                        binding.addPhotoButton.setOnClickListener(v -> {
                            previewImage.setVisibility(View.GONE);
                            binding.flashButton.setVisibility(View.VISIBLE);
                            binding.imageCaptureButton.setIconResource(R.drawable.ic_camera_shutter_24);
                            binding.addPhotoButton.setIconResource(R.drawable.ic_add_photo_24);
                            binding.imageCaptureButton.setOnClickListener(v1 -> takePhoto());
                        });
                        Toast.makeText(getBaseContext(), "Image Saved successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(@NonNull ImageCaptureException error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Failed to save: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                startCamera();
            }
        });
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    // Used to bind the lifecycle of cameras to the lifecycle owner
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                    Log.e(TAG_LOG, "Use case binding failed", e);
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        int aspectRatio = aspectRatio(previewView.getWidth(), previewView.getHeight());

        // Preview
        Preview preview = new Preview.Builder()
                .setTargetAspectRatio(aspectRatio)
                .setTargetRotation(Surface.ROTATION_0) // Use ROTATION_0 for portrait
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // Set Back camera for image capturing
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        imageCapture = new ImageCapture.Builder()
                //.setFlashMode(ImageCapture.FLASH_MODE_ON)
                .setTargetAspectRatio(aspectRatio)
                //.setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .setTargetRotation(Surface.ROTATION_0) // Use ROTATION_0 for portrait
                .build();

        // Unbind use cases before rebinding
        cameraProvider.unbindAll();
        // Bind use cases to camera
        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
        // For performing operations that affect all outputs.
        cameraControl = camera.getCameraControl();
        // Listen to tap events on the viewfinder and set them as focus regions
        setUpZoomAndTapToFocus();
        // Setup camera info
        cameraInfo = camera.getCameraInfo();
        // Enable Flash light Control by button
        binding.flashButton.setOnClickListener(new View.OnClickListener() {
            boolean state = true;

            @Override
            public void onClick(View v) {
                if (state) {
                    // Flash On
                    cameraControl.enableTorch(true);
                    binding.flashButton.setIconTintResource(R.color.yellow);
                    state = false;
                } else {
                    // Flash Off
                    cameraControl.enableTorch(false);
                    binding.flashButton.setIconTintResource(R.color.black);
                    state = true;
                }
            }
        });
    }

    private void setUpZoomAndTapToFocus() {
        // Listen to pinch gestures
        ScaleGestureDetector.SimpleOnScaleGestureListener listener =
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(@NonNull ScaleGestureDetector detector) {
                // Get the camera's current zoom ratio
                float currentZoomRatio = cameraInfo.getZoomState().getValue().getZoomRatio();
                // Get the pinch gesture's scaling factor
                float delta = detector.getScaleFactor();
                // Update the camera's zoom ratio. This is an asynchronous operation that returns
                // a ListenableFuture, allowing you to listen to when the operation completes.
                cameraControl.setZoomRatio(currentZoomRatio * delta);
                // Return true, as the event was handled
                return true;
            }
        };

        ScaleGestureDetector scaleGestureDetector =
                new ScaleGestureDetector(previewView.getContext(), listener);
        previewView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Attach the pinch gesture listener to the viewfinder
                scaleGestureDetector.onTouchEvent(event);
                if (event.getAction() != MotionEvent.ACTION_UP) {
                /* Original post returns false here, but in my experience this makes
                onTouch not being triggered for ACTION_UP event */
                    return true;
                }
                // Get the MeteringPointFactory from PreviewView
                MeteringPointFactory factory = previewView.getMeteringPointFactory();
                // Create a MeteringPoint from the tap coordinates
                MeteringPoint point = factory.createPoint(event.getX(), event.getY());
                // Create a MeteringAction from the MeteringPoint,
                // you can configure it to specify the metering mode
                FocusMeteringAction action = new FocusMeteringAction.Builder(point).build();
                cameraControl.startFocusAndMetering(action);
                // Animated the focus icon
                animateFocusRing(event.getX(), event.getY());
                return true;
            }
        });
    }

    private void animateFocusRing(float x, float y) {
        ImageView focusRing = binding.focusRing;

        // Move the focus ring so that its center is at the tap location (x, y)
        float width = focusRing.getWidth();
        float height = focusRing.getHeight();
        focusRing.setX(x - width / 2);
        focusRing.setY(y - height / 2);

        // Show focus ring
        focusRing.setVisibility(View.VISIBLE);
        focusRing.setAlpha(0.7F);

        // Animate the focus ring to disappear
        focusRing.animate()
                .setStartDelay(500)
                .setDuration(300)
                .alpha(0F)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        focusRing.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animation) {

                    }
                });
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}