package org.ragecastle.sunshine_udacity;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jahall on 10/31/15.
 */
public class MainFragment extends Fragment {

    private ArrayAdapter<String> forecastAdapter;
    private String result = "";

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

        forecastAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast);

        ListView listView;
        listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forecastAdapter);

        setHasOptionsMenu(true);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchWeatherTask fetch = new FetchWeatherTask();
            fetch.execute("11776", "metric", Integer.toString(7));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class FetchWeatherTask extends AsyncTask<String, Void, String[]>{
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();



        @Override
        protected String[] doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            InputStream inputStream = null;
            StringBuffer buffer = null;
            String result = null;

            try {
                final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";
                final String ZIP_PARAM = "zip";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";
                final String APIKEY = "2cc49234c210678f5634d620744e7202";

                Uri builder = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(ZIP_PARAM, params[0])
                        .appendQueryParameter(UNITS_PARAM, params[1])
                        .appendQueryParameter(DAYS_PARAM, params[2])
                        .appendQueryParameter(APPID_PARAM, APIKEY)
                        .build();

                URL url = new URL(builder.toString());

                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                inputStream = connection.getInputStream();
                buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                result = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
            } finally {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

            try{
                return DataParser.getForecast(result);
            } catch(JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if(result != null){
                forecastAdapter.clear();
                for(String dayForecast : result){
                    forecastAdapter.add(dayForecast);
                }
            }
        }
    }


}
