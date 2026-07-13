package com.aniketjain.weatherapp.update;

import android.content.Context;

import com.aniketjain.weatherapp.R;

public class UpdateUI {

    // Redesigned to map WeatherAPI condition codes to the existing drawable names
    public static String getIconID(int condition, int isDay) {
        if (condition == 1000) { // Sunny / Clear
            return isDay == 1 ? "clear_day" : "clear_night";
        } else if (condition == 1003) { // Partly cloudy
            return isDay == 1 ? "few_clouds_day" : "few_clouds_night";
        } else if (condition == 1006) { // Cloudy
            return "scattered_clouds";
        } else if (condition == 1009) { // Overcast
            return "broken_clouds";
        } else if (condition == 1030 || condition == 1135 || condition == 1148) { // Mist / Fog
            return "wind"; // Reusing the project's wind icon as a stand-in for poor visibility
        } else if (condition >= 1063 && condition <= 1207) { // Patchy rain, drizzle, light rain
            return "rain";
        } else if ((condition >= 1210 && condition <= 1264) || condition == 1114 || condition == 1117) { // Snow conditions
            return "snow";
        } else if ((condition >= 1273 && condition <= 1282) || condition == 1087) { // Thunder and lightning
            return "thunderstorm";
        }
        return "clear_day"; // Fallback safety
    }

    public static String TranslateDay(String dayToBeTranslated, Context context) {
        switch (dayToBeTranslated.trim()) {
            case "Monday":
                return context.getResources().getString(R.string.monday);
            case "Tuesday":
                return context.getResources().getString(R.string.tuesday);
            case "Wednesday":
                return context.getResources().getString(R.string.wednesday);
            case "Thursday":
                return context.getResources().getString(R.string.thursday);
            case "Friday":
                return context.getResources().getString(R.string.friday);
            case "Saturday":
                return context.getResources().getString(R.string.saturday);
            case "Sunday":
                return context.getResources().getString(R.string.sunday);
        }
        return dayToBeTranslated;
    }
}