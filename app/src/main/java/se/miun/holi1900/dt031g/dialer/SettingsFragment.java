package se.miun.holi1900.dt031g.dialer;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.lang.invoke.CallSite;

public class SettingsFragment extends PreferenceFragmentCompat {
    private final static String pref_store_numbers = "store_number_key";
    private final static String pref_delete_numbers = "delete_number_key";
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference delete_numbers = findPreference("delete_number");
        if(delete_numbers!=null){
            delete_numbers.setOnPreferenceClickListener(preference -> CallListActivity.deleteStoredPhoneNumbers(getContext(), "dial_numbers"));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
        Preference savePreference = findPreference(pref_store_numbers);

        Preference deletePreference = findPreference(pref_delete_numbers);
    }
}