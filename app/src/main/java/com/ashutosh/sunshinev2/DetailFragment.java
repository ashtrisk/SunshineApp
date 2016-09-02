package com.ashutosh.sunshinev2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashutosh.sunshinev2.data.WeatherContract.WeatherEntry;

/**
 * Created by Vostro-Daily on 3/3/2016.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecastStr;

    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    private static final int DETAIL_LOADER = 0;     // id for detail loader
    private static final String[] FORECAST_COLUMNS = {
            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATE,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            WeatherEntry.COLUMN_HUMIDITY,
            WeatherEntry.COLUMN_WIND_SPEED,
            WeatherEntry.COLUMN_PRESSURE,
            WeatherEntry.COLUMN_WEATHER_ID      // weather id for fetching weather icon.
    };
    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_HUMIDITY = 5;
    private static final int COL_WIND_SPEED = 6;
    private static final int COL_PRESSURE = 7;
    private static final int COL_WEATHER_CONDITION_ID = 8;

    // Views to be used in Layout as member of DetailFragment to be used in onLoadFinished() callback
    ImageView mIconView;
    TextView mDayView;
    TextView mDateView;
    TextView mWeatherDescView;
    TextView mMaxTempView;
    TextView mMinTempView;
    TextView mHumidityView;
    TextView mWindView;
    TextView mPressureView;

    public DetailFragment(){
        // empty public constructor required by Android Runtime
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

//        Intent intent = getActivity().getIntent();
//        // Note : This inspection that intent has some extra or not is important
//        if(intent!=null && intent.hasExtra("WEATHER_DATA_EXTRA")) {
//            mForecastStr = intent.getStringExtra("WEATHER_DATA_EXTRA");
//            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastStr);
//        }
        mIconView = (ImageView)rootView.findViewById(R.id.img_weather_icon);
        mDayView = (TextView) rootView.findViewById(R.id.day);
        mDateView = (TextView) rootView.findViewById(R.id.date);
        mWeatherDescView = (TextView) rootView.findViewById(R.id.weather);
        mMaxTempView = (TextView) rootView.findViewById(R.id.max_temp);
        mMinTempView = (TextView) rootView.findViewById(R.id.min_temp);
        mHumidityView = (TextView) rootView.findViewById(R.id.humidity);
        mWindView = (TextView) rootView.findViewById(R.id.wind);
        mPressureView = (TextView) rootView.findViewById(R.id.pressure);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private Intent createShareForecastIntent(){
        //  create a share intent by setting SEND action, Flag, and share data type.
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);    // prevents the activity we sharing to from being placed onto the activity stack.
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);       // initialize loader
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();
        if(intent == null)
            return null;
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(getActivity(),
                intent.getData(),       // Uri for data
                FORECAST_COLUMNS,       // projection
                null,       // selection
                null,       // selection args
                null);      // sort order
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor dataCursor) {
        if (!dataCursor.moveToFirst()) { return; }

        String dayString = Utility.getDayName(getContext(), dataCursor.getLong(COL_WEATHER_DATE));

        String dateString = Utility.getFormattedMonthDay(getContext(),
                dataCursor.getLong(COL_WEATHER_DATE));

        String weatherDescription =
                dataCursor.getString(COL_WEATHER_DESC);

        boolean isMetric = Utility.isMetric(getActivity());

        String high = Utility.formatTemperature(getContext(),
                dataCursor.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

        String low = Utility.formatTemperature(getContext(),
                dataCursor.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

        String humidity = dataCursor.getString(COL_HUMIDITY);

        String windSpeed = dataCursor.getString(COL_WIND_SPEED);

        String pressure = dataCursor.getString(COL_PRESSURE);

        int weatherConditionId = dataCursor.getInt(COL_WEATHER_CONDITION_ID);

        View view = getView();

        mDayView.setText(dayString);
        mDateView.setText(dateString);
        mWeatherDescView.setText(weatherDescription);
        mMaxTempView.setText(high);
        mMinTempView.setText(low);
        mHumidityView.setText(humidity);
        mWindView.setText(windSpeed);
        mPressureView.setText(pressure);
        mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherConditionId));// get colored drawable for weather condition

        mForecast = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);
//        TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
//        detailTextView.setText(mForecast);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
        // nothing to do
    }
}
