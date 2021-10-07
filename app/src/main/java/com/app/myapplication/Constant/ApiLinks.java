package com.app.myapplication.Constant;

public class ApiLinks {
    public static String root_url = "https://api.postalpincode.in/";
    public static String pin_code_end_point = root_url+"pincode/";

    public static String weather_root_url = "https://api.weatherapi.com/v1/current.json?";
    public static String getWeather_key = "key=35c9f92ac5bf4df0811144140212307";
    public static String getWeather_end_point = weather_root_url+getWeather_key+"&aqi=no&q=";
}
