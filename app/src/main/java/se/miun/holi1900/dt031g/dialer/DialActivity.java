package se.miun.holi1900.dt031g.dialer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DialActivity extends AppCompatActivity {

    private final int CALL_PERMISSION_CODE = 1;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_ACCESS_CODE = 999;
    //private FusedLocationProviderClient fusedLocationClient;
    //private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial);
        // Initialize the singleton class SoundPlayer instance to make sure the sounds are completely
        // loaded and ready to be used by child activity DialButtonView.
        // Instantiating ensures that all 12 buttons do not need to instantiate
        SoundPlayer.getInstance(getApplicationContext());
        //fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(DialActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestCallPermission();
        }
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) ==
                PackageManager.PERMISSION_DENIED){
            requestLocationPermission();
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
        if(item.getItemId()==R.id.download_voices_option){
            startActivity(new Intent(this, DownloadActivity.class));
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
                            new String[] {Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE))
                    .create().show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
        }
    }

    /**
     * Request permission data if permission is not set
     */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(DialActivity.this, FINE_LOCATION)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            sendLocationRequest();
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            dialog.dismiss();
                            Toast.makeText(DialActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
        } else {
            sendLocationRequest();
        }
    }

    /**
     * Send request to get location permission
     */
    private void sendLocationRequest() {
        ActivityCompat
                .requestPermissions(DialActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_ACCESS_CODE);
    }

    /**
     * Get the device location
     */
    /*public void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            currentLocation = location;
                        }
                    });
        } else {
            currentLocation = null;
        }
    }

    public Location getLocation(){
        return currentLocation;
    }*/
}