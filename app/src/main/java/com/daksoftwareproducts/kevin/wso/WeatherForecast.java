package com.daksoftwareproducts.kevin.wso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 6/20/2017.
 */

public class WeatherForecast {

    private List<DayForecast> daysForecast = new ArrayList<DayForecast>();

    public void addForecast(DayForecast forecast) {
        daysForecast.add(forecast);
        System.out.println("Add forecast ["+forecast+"]");
    }

    public List<DayForecast> get(){ return daysForecast; }

    public DayForecast getForecast(int dayNum) {
        return daysForecast.get(dayNum);
    }

}
