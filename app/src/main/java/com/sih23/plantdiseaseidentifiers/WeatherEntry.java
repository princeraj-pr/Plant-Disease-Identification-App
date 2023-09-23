package com.sih23.plantdiseaseidentifiers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherEntry {
    private String city;
    private float date;
    private double temp;
    private long sunRiseTime;
    private long sunSetTime;
    private String skyInfo;
    private float humidity;

    public WeatherEntry(String city, float date, double temp, long sunRiseTime, long sunSetTime,
                        String skyInfo, float humidity) {
        this.city = city;
        this.date = date;
        this.temp = temp;
        this.sunRiseTime = sunRiseTime;
        this.sunSetTime = sunSetTime;
        this.skyInfo = skyInfo;
        this.humidity = humidity;
    }

    public String getCityNameAndFormatDate() {
        Date date = new Date((long) getDate() * 1000); // multiplying 1000 to get time in millisecond for Data object
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
        return getCity() + ", " + formatter.format(date);
    }

    public String getTempInCelsius() {
        int tempCelsius = (int) (getTemp() - 273.15);
        return String.valueOf(tempCelsius) + "°C";
    }

    public String getTempInFahrenheit() {
        int tempFahrenheit = (int) (((getTemp() - 273.15) * 1.8) + 32);
        return String.valueOf(tempFahrenheit) + "°F";
    }

    public String sunSetOrSunRiseTime() {
        double currentTime = System.currentTimeMillis();
        if (currentTime > 1000 * getSunRiseTime() && currentTime < 1000 * getSunSetTime()) {
            // Show sunset time
            Date date = new Date(getSunSetTime() * 1000); // multiplying 1000 to get time in millisecond for Data object
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
            return "Sunset " + formatter.format(date);
        } else {
            // show sunrise time
            Date date = new Date(getSunRiseTime() * 1000); // multiplying 1000 to get time in millisecond for Data object
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
            return "Sunrise " + formatter.format(date);
        }
    }

    public String getSkyDescription() {
        return "It is " + getSkyInfo() + " today.";
    }

    public String getHumidityInPercentage() {
        return  (int) getHumidity() + "%";
    }

    public String getFarmingAdvise() {
        // TODO : Show farming advise base on current weather
        // Temporary text
        return "Today would be a bad day for: Applying Pesticides.";
    }

    public String getCity() {
        return city;
    }

    public float getDate() {
        return date;
    }

    public double getTemp() {
        return temp;
    }

    public long getSunRiseTime() {
        return sunRiseTime;
    }

    public long getSunSetTime() {
        return sunSetTime;
    }

    public String getSkyInfo() {
        return skyInfo;
    }

    public float getHumidity() {
        return humidity;
    }
}
