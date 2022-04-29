package se.miun.holi1900.dt031g.dialer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class CallListActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private final ArrayList<String> savedPhoneNumbers = new ArrayList<>();
    private final  static  String phoneNumbersFile = "dial_numbers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);
        sharedPreferences = getSharedPreferences(phoneNumbersFile, Context.MODE_PRIVATE);
        //copy numbers from sharedPreference and store in the class arrayList
        getStorePhoneNumbers();

        //load adapter with list data and the layout sating the properties of each list item
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.activity_list_view, savedPhoneNumbers);

        //Pass adapter to listView to be displayed
        ListView listView = (ListView) findViewById(R.id.phone_number_list);
        listView.setAdapter(adapter);
    }

    /**
     * Called when the menu is opened for the first time. Inflates the menu resource to the menu
     * @param menu menu to inflate menu resource to
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_number_menu, menu);
        return true;
    }

    /**
     * Called when the user selects an item from the options menu
     * @param item menu item selected
     * @return successful implementation returns true while default implementation returns false
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if selected item is settings option start the setting activity
        if(item.getItemId()==R.id.delete_stored_numbers){
            boolean fileDeleted = deleteStoredPhoneNumbers(this, phoneNumbersFile);

            //redraw view after deleting list
            startActivity(getIntent());
            finish();
            Log.d("assignments", "Contact list deleted: " + fileDeleted);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Read stored numbers from the file dial_numbers in sharedPreference  to a Map.
     * Get the list os all the values from the map and store in the class arrayList
     */
    private void getStorePhoneNumbers(){
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue().toString();
            savedPhoneNumbers.add(value);
        }

    }

    /**
     * Deletes a file from sharedPreference
     * @param context the activity
     * @param fileName the name of the file to be deleted
     *
     * @return true if file is successfully deleted else false
     */
    public static boolean deleteStoredPhoneNumbers(Context context, String fileName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.deleteSharedPreferences(fileName);
        } else {
            context.getSharedPreferences(fileName, MODE_PRIVATE).edit().clear().apply();
            File dir = new File(context.getApplicationInfo().dataDir, "shared_prefs");
            return new File(dir, fileName + ".xml").delete();
        }
    }
}