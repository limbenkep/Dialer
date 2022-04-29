package se.miun.holi1900.dt031g.dialer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //variable that keeps track of the state of the About page. Set to tru when the about button is clicked
    private static final String ABOUT_PAGE_STATE = "aboutPageOpened";
    //Tag for logging
    private static final String TAG= "assignment4";
    private boolean aboutPageOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context context = getApplicationContext();

        //Check if default voice has been copied to internal storage , if not copy.
        if(!Util.defaultVoiceExist(context)){
            try{
                boolean soundsCopied = Util.copyDefaultVoiceToInternalStorage(context);
            }catch (Exception e){
                Log.e(TAG, "Error copying default voice to internal storage "+ ": " + e);
            }

        }

        //get the reference to the about button that opens about activity on click
        Button about = findViewById(R.id.aboutButton);

        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of saved state (dice value)
            aboutPageOpened = savedInstanceState.getBoolean(ABOUT_PAGE_STATE); // Returns the value associated with the given key, or 0
            Log.d(TAG, "onCreate: we have a saved instance state, aboutPageOpened=" + aboutPageOpened);
        } else {
            Log.d(TAG, "onCreate: no saved instance state");
        }

    }

    /**
     * The onSaveInstanceState() method is called when an activity starts to stop unexpectedly,so the
     * activity can save state information (key-value pairs) to an instance state bundle. The state
     * can then be restored in onCreate or onRestoreInstanceState (the Bundle populated by this
     * method will be passed to both).
     *
     * @param outState Bundle in which to place your saved state (aboutPageOpened value).
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: about page has been opened in thisi life cycle=" + aboutPageOpened);

        //save the value of aboutPageOpened
        outState.putBoolean(ABOUT_PAGE_STATE, aboutPageOpened);

        // The superclass saves the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    /**
     * Instead of restoring the state during onCreate() you may choose to implement
     * onRestoreInstanceState(), which the system calls after the onStart() method. The system
     * calls onRestoreInstanceState() only if there is a saved state to restore, so you do not
     * need to check whether the Bundle is null.
     *
     * @param savedInstanceState the data most recently supplied in onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {

        //the superclass restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        //Restore value of for aboutPageState that tracks state of About activity
        aboutPageOpened= savedInstanceState.getBoolean(ABOUT_PAGE_STATE);
    }

    /**
     * Called when the DIAL button is clicked. Displays dialpad
     * @param view view
     */
    public void displayKeyPad(View view) {
        startActivity(new Intent(this, DialActivity.class));
    }

    /**Called when the Call List button is clicked*/
    public void displayCallList(View view) {
        startActivity(new Intent(this, CallListActivity.class));
    }

    /**Called when the Settings button is clicked*/
    public void displaySettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**Called when the Map button is clicked*/
    public void displayMap(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    /**
     * Called when the about button is clicked. Displays a dialog box with information on the
     * features of this application
     * @param view view
     */
    public void openAboutDialog(View view){
        if(!aboutPageOpened){
            aboutPageOpened = true;
            AboutDialog aboutDialog = new AboutDialog();
            aboutDialog.show(getSupportFragmentManager(), "about dialog");
        }else{
            Context context = getApplicationContext();
            //text to be displayed to user
            CharSequence text = "Dialog already seen!";
            //Duration the toast should remain on the screen
            int duration = Toast.LENGTH_SHORT;
            //The makeText() method returns a properly initialized Toast
            //object and show() method display toast
            Toast.makeText(context, text, duration).show();
        }
    }
}