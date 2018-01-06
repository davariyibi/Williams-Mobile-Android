package com.daksoftwareproducts.kevin.wso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kevin on 6/20/2017.
 */

public class DayForecast {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public Weather weather = new Weather();
    public ForecastTemp forecastTemp = new ForecastTemp();
    public long timestamp;
    public String date;

    public class ForecastTemp {
        public float temp;
        public float temp_min;
        public float temp_max;
//        public String getHigh(){ return Double.toString(toFahrenheit(temp_max)); }
//        public String getLow(){ return Double.toString(toFahrenheit(temp_min)); }
        public String getTemp(float temp) {
            String tempStr = Double.toString(toFahrenInt(temp));
            return tempStr.substring(0, tempStr.indexOf("."));
        }
        public String getHigh(){
            return getTemp(temp_max);
        }
        public String getLow(){ return getTemp(temp_min); }
    }

    public double toFahrenheit( float temperature ){
        return ((temperature*9)/ 5) - 459.67;
    }

    public int toFahrenInt( float temperature ){
        return (int) (((temperature*9)/ 5) - 459.67);
    }

    public String getStringDate() {
        return sdf.format(new Date(timestamp));
    }

    public String dow() {
        return dow(this.timestamp);
    }

    public String dow(long tstamp) {
        Date date = new Date(tstamp);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
    }

    public String date() {
        return this.date.substring(5, 7) + "/" + this.date.substring(8, 10) + "/" + this.date.substring(0,4);
    }

    public String time() {
        String time = "";
        int hour = Integer.parseInt(this.date.substring(11, 13));
        if (hour == 0) {
            time = "12am";
        } else if (hour < 12) {
            time = hour + "am";
        } else if (hour == 12) {
            time = hour + "pm";
        } else {
            time = (hour - 12) + "pm";
        }
        return time;
    }

    public String descr() {
        String descr = this.weather.currentCondition.getDescr();
        return descr.substring(0,1).toUpperCase() + descr.substring(1);
    }

}
