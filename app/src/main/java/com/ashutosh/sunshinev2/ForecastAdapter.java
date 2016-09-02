package com.ashutosh.sunshinev2;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vostro-Daily on 3/29/2016.
 */
public class ForecastAdapter extends CursorAdapter{

    final int VIEW_TYPE_TODAY = 0;
    final int VIEW_TYPE_FUTURE_DAY = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(mContext, high, isMetric) + "/" + Utility.formatTemperature(mContext, low, isMetric);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Cursor cursor) {
        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int pos = cursor.getPosition();
        int viewType = getItemViewType(pos);
        int layoutId;
        if(viewType == VIEW_TYPE_TODAY) {
            layoutId = R.layout.list_item_forecast_today;
        } else {      // if view type isn't Today view type
            layoutId = R.layout.list_item_forecast;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
//        TextView tv = (TextView)view;
//        tv.setText(convertCursorRowToUXFormat(cursor));
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        ImageView icon_img = viewHolder.iconView;
        // Read weather condition id from cursor
        int weatherCoditionId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);

        int iconDrawable;
        // get the viewtype so that different drawable can be set for todayView Type
        int viewType = getItemViewType(cursor.getPosition());

        if(viewType == VIEW_TYPE_TODAY){      // forecast today, colored icon
            iconDrawable = Utility.getArtResourceForWeatherCondition(weatherCoditionId);
        }else {
            iconDrawable = Utility.getIconResourceForWeatherCondition(weatherCoditionId);
        }

        icon_img.setImageResource(iconDrawable);

        TextView tv_date = viewHolder.dateView;//(TextView) view.findViewById(R.id.date);
        tv_date.setText(Utility.getFriendlyDayString(context, cursor.getLong(ForecastFragment.COL_WEATHER_DATE)));

        TextView tv_weather = viewHolder.weatherView;//(TextView) view.findViewById(R.id.weather);
        tv_weather.setText(cursor.getString(ForecastFragment.COL_WEATHER_DESC));

        boolean isMetric = Utility.isMetric(context);

        TextView tv_max_temp = viewHolder.maxTempView;//(TextView) view.findViewById(R.id.max_temp);
        tv_max_temp.setText(Utility.formatTemperature(context, cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP), isMetric));

        TextView tv_min_temp = viewHolder.minTempView;//(TextView) view.findViewById(R.id.min_temp);
        tv_min_temp.setText(Utility.formatTemperature(context, cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP), isMetric));

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }
}
