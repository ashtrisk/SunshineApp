package com.ashutosh.sunshinev2;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vostro-Daily on 4/14/2016.
 */
public class ViewHolder {       // A class to hold views within the view hierarchy of the listView recycled View.

    public final ImageView iconView;
    public final TextView maxTempView;
    public final TextView minTempView;
    public final TextView weatherView;
    public final TextView dateView;

    public ViewHolder(View view) {
        iconView = (ImageView)view.findViewById(R.id.img_weather_icon);
        maxTempView = (TextView)view.findViewById(R.id.max_temp);
        minTempView = (TextView)view.findViewById(R.id.min_temp);
        weatherView = (TextView)view.findViewById(R.id.weather);
        dateView = (TextView)view.findViewById(R.id.date);
    }
}
