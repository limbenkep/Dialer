package se.miun.holi1900.dt031g.dialer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class DialActivity extends AppCompatActivity {

    private int call_permission_code = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial);
        // Initialize the singleton class SoundPlayer instance to make sure the sounds are completely
        // loaded and ready to be used by child activity DialButtonView.
        // Instantiating ensures that all 12 buttons do not need to instantiate
        SoundPlayer.getInstance(getApplicationContext());

        if (ContextCompat.checkSelfPermission(DialActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestCallPermission();
        }
    }

    /**
     * Called when the menu is opened for the first time. Inflates the menu resource to the menu
     * @param menu menu to inflate menu resource to
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dial_menu, menu);
        inflater.inflate(R.menu.download_voices_menu, menu);
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
        if(item.getItemId()==R.id.settings_option){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void requestCallPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("By granting the permission to make and manage phone calls, your " +
                            "call will be started immediately. If you deny the permission, you will" +
                            " have to explicitly initiate the call")
                    .setPositiveButton("Ok", (dialogInterface, i) -> ActivityCompat.requestPermissions(DialActivity.this,
                            new String[] {Manifest.permission.CALL_PHONE}, call_permission_code))
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, call_permission_code);
        }
    }

}