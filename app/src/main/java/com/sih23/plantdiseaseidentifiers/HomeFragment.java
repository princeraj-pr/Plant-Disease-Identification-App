package com.sih23.plantdiseaseidentifiers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sih23.plantdiseaseidentifiers.adapters.FruitAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Open camera activity
        view.findViewById(R.id.camera_button).setOnClickListener(v -> {
            Intent cameraIntent = new Intent(getContext(), CameraActivity.class);
            startActivity(cameraIntent);
        });

        RecyclerView fruitView = view.findViewById(R.id.fruit_list);
        LinearLayoutManager fruitLayoutManager = new LinearLayoutManager(getContext());
        fruitLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        fruitView.setLayoutManager(fruitLayoutManager);
        FruitAdapter fruitAdapter = new FruitAdapter();
        fruitAdapter.submitList(getFruit());
        fruitView.setAdapter(fruitAdapter);
        return view;
    }

    public List<FruitEntry> getFruit() {
        List<FruitEntry> fruitEntries = new ArrayList<FruitEntry>();
        fruitEntries.add(new FruitEntry(1, R.drawable.ic_pineapple));
        fruitEntries.add(new FruitEntry(2, R.drawable.ic_apple));
        fruitEntries.add(new FruitEntry(3, R.drawable.ic_eggplant));
        fruitEntries.add(new FruitEntry(4, R.drawable.ic_potato));
        fruitEntries.add(new FruitEntry(5, R.drawable.ic_banana));
        fruitEntries.add(new FruitEntry(6, R.drawable.ic_pumpkin));
        fruitEntries.add(new FruitEntry(7, R.drawable.ic_grapes));
        fruitEntries.add(new FruitEntry(8, R.drawable.ic_tomato));
        fruitEntries.add(new FruitEntry(9, R.drawable.ic_orange));
        fruitEntries.add(new FruitEntry(10, R.drawable.ic_fruit));
        return fruitEntries;
    }
}