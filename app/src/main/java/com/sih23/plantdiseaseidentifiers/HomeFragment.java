package com.sih23.plantdiseaseidentifiers;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.sih23.plantdiseaseidentifiers.adapters.FruitAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private String LOG_TAG = HomeFragment.class.getSimpleName();

    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "af59eff54364d5fdb49d06155cb90244";
    public static String lat = "0";
    public static String lon = "0";
    private TextView weatherData;

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
            String[] coordinate = getArguments().getStringArray(PlantActivity.ARG_PARAM_COORDINATE);
            if (coordinate != null) {
                lon = coordinate[0];
                lat = coordinate[1];
            }
        } else {
            Log.d(LOG_TAG, "No arguments");
        }
        getCurrentData();
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

        weatherData = view.findViewById(R.id.textView6);
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

    void getCurrentData() {
        // Get coordinates of current location to show weather
        String[] coordinate;
        if (getArguments() != null) {
            coordinate = getArguments()
                    .getStringArray(PlantActivity.ARG_PARAM_COORDINATE);
            if (coordinate != null) {
                lon = coordinate[0];
                lat = coordinate[1];
                Toast.makeText(getContext(), "lon: " + lon + " lat: " + lat, Toast.LENGTH_SHORT).show();
            }
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.pressure;

                    weatherData.setText(stringBuilder);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                weatherData.setText(t.getMessage());
            }
        });
    }
}