package org.ragecastle.sunshine_udacity;

import android.text.format.Time;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by jahall on 10/31/15.
 */
public class DataParser {

    public static String[] getForecast(String weatherData) throws JSONException{
        JSONObject jsonWeather = new JSONObject(weatherData);
        JSONArray weatherArray = jsonWeather.getJSONArray("list");
        String[] results;
        Time dayTime = new Time();
        dayTime.setToNow();

        int julianTime = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

        results = new String[weatherArray.length()];
        for(int i = 0;i<weatherArray.length();i++){
            String day;
            String description;
            String highAndLow;
            long dateTime;
            JSONObject dayForecast;
            double high;
            double low;

            dayForecast = weatherArray.getJSONObject(i);

            dateTime = dayTime.setJulianDay(julianTime + i);
            day = getReadableDateString(dateTime);

            JSONArray dayWeatherArray = dayForecast.getJSONArray("weather");
            JSONObject weather = dayWeatherArray.getJSONObject(0);
            description = weather.getString("description");

            JSONObject temp = dayForecast.getJSONObject("main");

            high = temp.getDouble("temp_max");
            low = temp.getDouble("temp_min");

            highAndLow = Math.round(high) + "/" + Math.round(low);

            results[i] = day + " - " + description + " - " + highAndLow;
        }

        return results;
    }

    private static String getReadableDateString(long dateTime) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd", Locale.US);
        return shortenedDateFormat.format(dateTime);    }

}
