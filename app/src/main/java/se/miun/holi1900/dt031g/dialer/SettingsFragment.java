package se.miun.holi1900.dt031g.dialer;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;

public class SettingsFragment extends PreferenceFragmentCompat {
    private final static String pref_store_numbers = "store_number_key";
    private final static String pref_delete_numbers = "delete_number_key";
    private ListPreference voicesPref;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference delete_numbers = findPreference("delete_number");
        voicesPref = findPreference(getString(R.string.voices_key));
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        if(delete_numbers!=null){
            delete_numbers.setOnPreferenceClickListener(preference ->
                    CallListActivity.deleteStoredPhoneNumbers(getContext(), "dial_numbers"));
        }
        if(voicesPref != null){
            updateVoicesPreference();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Overrides onResume method. Updates llstPreference values on resume.
     */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
        updateVoicesPreference();

    }

    /**
     * gets the name of the folders in voices folder containing voices for the dial buttons
     * @return an array of names of voice folders
     */
    private String[] getVoicesNames() {

        File fileDir= new File(requireContext().getFilesDir(), getText(R.string.voices_directory).toString());
        if (fileDir.isDirectory() && fileDir.listFiles() !=null) {
            File[] filesList = fileDir.listFiles();
            int arrayLength = filesList.length;
            String[] voices = new String[arrayLength];
            for (int i= 0; i<arrayLength; i++) {
                voices[i] = filesList[i].getName();
            }
            return voices;
        }
        return null;
    }

    /**
     * sets values of listPreference shown
     */
    private void updateVoicesPreference(){
        String[] voices = getVoicesNames();
        if(voices != null){
            voicesPref.setEntries(voices);
            voicesPref.setEntryValues(voices);
        }
    }

}