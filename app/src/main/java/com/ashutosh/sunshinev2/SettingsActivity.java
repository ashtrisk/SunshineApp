package com.ashutosh.sunshinev2;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);

        if(savedInstanceState == null){
            // android.R.id.content gives the rootView of the activity
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFragment()).commit();
        }
    }


    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        public SettingsFragment(){
            // Required public empty constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
//            setHasOptionsMenu(true);
            super.onCreate(savedInstanceState);
            // done to remove default values in shared preferences file
//            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().remove(getString(R.string.pref_units_key)).commit();

            // should be called after super.onCreate(..)
            addPreferencesFromResource(R.xml.pref_general);

            // bind the summary to the preference
            bindPreferenceToSummary(findPreference(getString(R.string.pref_location_key)));
            bindPreferenceToSummary(findPreference(getString(R.string.pref_units_key)));
        }

        public void bindPreferenceToSummary(Preference pref){
            pref.setOnPreferenceChangeListener(this);       // register a listener to watch for value change

            // call the listener immediately for default value (current value) of the preference too.
            onPreferenceChange(pref, PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(pref.getKey(), ""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String value = newValue.toString();
            if (preference instanceof ListPreference) {
                // For list preference, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(value);
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            } else {
                // For other pref_general, set the summary to the value's simple string representation.
                preference.setSummary(value);
            }
            return true;
        }
    }
}
