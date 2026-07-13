package com.aniketjain.weatherapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aniketjain.weatherapp.R;
import com.aniketjain.weatherapp.update.UpdateUI;
import com.aniketjain.weatherapp.url.URL;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DayViewHolder> {
    private final Context context;

    public DaysAdapter(Context context) {
        this.context = context;
    }

    private String updated_at, min, max, pressure, wind_speed, humidity;
    private int condition;
    private long update_time;

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_item_layout, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        getDailyWeatherInfo(position + 1, holder);
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @SuppressLint("DefaultLocale")
    private void getDailyWeatherInfo(int i, DayViewHolder holder) {
        URL url = new URL();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.getLink(), null, response -> {
            try {
                // WeatherAPI free tier may not have 7 days. Check length to avoid crashes.
                if (i < response.getJSONObject("forecast").getJSONArray("forecastday").length()) {
                    JSONObject dayData = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(i);
                    JSONObject dayMetrics = dayData.getJSONObject("day");

                    update_time = dayData.getLong("date_epoch");
                    updated_at = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(new Date(update_time * 1000));

                    condition = dayMetrics.getJSONObject("condition").getInt("code");

                    min = String.format("%.0f", dayMetrics.getDouble("mintemp_c"));
                    max = String.format("%.0f", dayMetrics.getDouble("maxtemp_c"));

                    // WeatherAPI daily node doesn't provide pressure. Use N/A to keep UI intact.
                    pressure = "N/A";
                    wind_speed = dayMetrics.getString("maxwind_kph");
                    humidity = dayMetrics.getString("avghumidity");

                    updateUI(holder);
                    hideProgressBar(holder);
                } else {
                    // Hide the card if the API didn't return data for this day
                    holder.layout.setVisibility(View.GONE);
                    holder.progress.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                holder.layout.setVisibility(View.GONE);
                holder.progress.setVisibility(View.GONE);
            }
        }, null);
        requestQueue.add(jsonObjectRequest);
        Log.i("json_req", "Day " + i);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(DayViewHolder holder) {
        String day = UpdateUI.TranslateDay(updated_at, context);
        holder.dTime.setText(day);
        holder.temp_min.setText(min + "°C");
        holder.temp_max.setText(max + "°C");
        holder.pressure.setText(pressure.equals("N/A") ? pressure : pressure + " mb");
        holder.wind.setText(wind_speed + " km/h");
        holder.humidity.setText(humidity + "%");
        // We pass 1 for 'isDay' because future forecasts always use daytime icons
        holder.icon.setImageResource(
                context.getResources().getIdentifier(
                        UpdateUI.getIconID(condition, 1),
                        "drawable",
                        context.getPackageName()
                ));
    }

    private void hideProgressBar(DayViewHolder holder) {
        holder.progress.setVisibility(View.GONE);
        holder.layout.setVisibility(View.VISIBLE);
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        SpinKitView progress;
        RelativeLayout layout;
        TextView dTime, temp_min, temp_max, pressure, wind, humidity;
        ImageView icon;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.day_progress_bar);
            layout = itemView.findViewById(R.id.day_relative_layout);
            dTime = itemView.findViewById(R.id.day_time);
            temp_min = itemView.findViewById(R.id.day_min_temp);
            temp_max = itemView.findViewById(R.id.day_max_temp);
            pressure = itemView.findViewById(R.id.day_pressure);
            wind = itemView.findViewById(R.id.day_wind);
            humidity = itemView.findViewById(R.id.day_humidity);
            icon = itemView.findViewById(R.id.day_icon);
        }
    }
}