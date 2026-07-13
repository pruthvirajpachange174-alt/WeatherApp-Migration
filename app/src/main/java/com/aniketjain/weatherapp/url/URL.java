package com.aniketjain.weatherapp.url;

import com.aniketjain.weatherapp.location.LocationCord;

public class URL {

    private String link;
    private static String city_url;

    public URL() {

        link =
                "https://api.weatherapi.com/v1/forecast.json?key="
                        + LocationCord.API_KEY
                        + "&q="
                        + LocationCord.lat
                        + ","
                        + LocationCord.lon
                        + "&days=7&aqi=no&alerts=no";
    }

    public String getLink() {
        return link;
    }

    public static void setCity_url(String cityName) {

        city_url =
                "https://api.weatherapi.com/v1/forecast.json?key="
                        + LocationCord.API_KEY
                        + "&q="
                        + cityName
                        + "&days=7&aqi=no&alerts=no";
    }

    public static String getCity_url() {
        return city_url;
    }

}