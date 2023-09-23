package com.sih23.plantdiseaseidentifiers;

import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sih23.plantdiseaseidentifiers.databinding.FragmentMyPlantBinding;
import com.sih23.plantdiseaseidentifiers.utils.AppExecutors;
import com.sih23.plantdiseaseidentifiers.utils.ImageUtils;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPlantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPlantFragment extends Fragment {
    FragmentMyPlantBinding binding;
    private Bitmap bitmap;

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
                bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

                // TODO: Tensorflow lite ML model code here

                // Set text view with disease info from ML model output
                binding.diseaseDetailText.setText("Plant Disease Detail");
            }
        });

        return view;
    }


}