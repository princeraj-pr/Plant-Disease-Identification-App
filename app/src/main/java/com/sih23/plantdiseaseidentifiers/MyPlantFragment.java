package com.sih23.plantdiseaseidentifiers;

import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sih23.plantdiseaseidentifiers.databinding.FragmentMyPlantBinding;
import com.sih23.plantdiseaseidentifiers.ml.Model;
import com.sih23.plantdiseaseidentifiers.utils.AppExecutors;
import com.sih23.plantdiseaseidentifiers.utils.ImageUtils;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPlantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPlantFragment extends Fragment {
    FragmentMyPlantBinding binding;
    private Bitmap bitmap;

    String[] labels;
    int cnt = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyPlantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPlantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPlantFragment newInstance(String param1, String param2) {
        MyPlantFragment fragment = new MyPlantFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyPlantBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get image file from internal storage
        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getExternalFilesDir(null);
        File file = new File(directory, "sample" + ".jpg");

        // Show loading animation and disable button click
        binding.loadingAnimation.setVisibility(View.VISIBLE);
        binding.findDiseaseButton.setEnabled(false);
        AppExecutors.getInstance().diskIO().execute(() -> {
            // Load image bitmap from internal storage
            bitmap = ImageUtils.getCorrectOrientatedBitmap(file.getPath());
            AppExecutors.getInstance().mainThread().execute(() -> {
                // Update UI
                // Hide loading animation and enable button click
                binding.loadingAnimation.setVisibility(View.GONE);
                binding.findDiseaseButton.setEnabled(true);
                // Set image to preview image view
                binding.plantImage.setImageBitmap(bitmap);
            });
        });

        binding.findDiseaseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Check the expected input size and format
                int inputSize = 150; // Adjust if needed
                int numChannels = 3; // RGB

                // Resize the bitmap to match the expected input size and format
                bitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true);

                // TODO: Tensorflow lite ML model code here
                try {
                    Model model = Model.newInstance(getContext());

                    // Create inputs for inference
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, inputSize, inputSize, numChannels}, org.tensorflow.lite.DataType.FLOAT32);
                    Log.e("shape", bitmap.toString());
                    Log.e("shape", inputFeature0.getBuffer().toString());
                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());
                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Set text view with disease info from ML model output
                    binding.diseaseDetailText.setText(getMax(outputFeature0.getFloatArray()) + "");

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }
        });

        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        return view;
    }

    public int getMax(float[] arr) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[max]) max = i;
        }
        return max;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
                    binding.plantImage.setImageBitmap(bitmap);
                } catch (IOException e) {

                }
            }
        }
    }
}