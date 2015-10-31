package org.ragecastle.sunshine_udacity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jahall on 10/31/15.
 */
public class MainFragment extends Fragment {

    public MainFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstanceState){
        View rootView = layoutInflater.inflate(R.layout.fragment_main, viewGroup, false);

        String[] forecastArray = {
          "Today - Sunny - 88/63",
                "Tomorrow - Cloudy - 70/50",
                "Monday - Weather - High/Low",
                "Tuesday - Weather - High/Low",
                "Wednesday - Weather - High/Low",
                "Thursday - Weather - High/Low",
                "Friday - Weather - High/Low",
        };

        List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));

        ArrayAdapter<String> forecastAdapter;
        forecastAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast);

        ListView listView;
        listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forecastAdapter);
        return rootView;
    }
}
