package se.miun.holi1900.dt031g.dialer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DialActivity extends AppCompatActivity {
    private static final String TAG = "DialActivity";

    private final int CALL_PERMISSION_CODE = 1;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_ACCESS_CODE = 999;
    //private LocationRequest locationRequest;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
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

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 1){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {

                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }else {
                Toast.makeText(DialActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(DialActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (isGPSEnabled()) {

                LocationServices.getFusedLocationProviderClient(DialActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                LocationServices.getFusedLocationProviderClient(DialActivity.this)
                                        .removeLocationUpdates(this);

                                if (locationResult != null && locationResult.getLocations().size() >0){

                                    int index = locationResult.getLocations().size() - 1;
                                    double latitude = locationResult.getLocations().get(index).getLatitude();
                                    double longitude = locationResult.getLocations().get(index).getLongitude();

                                    Log.d(TAG, "onLocationResult: latitude = "
                                            + latitude + ", longitude = " + longitude);
                                    //AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                }
                            }
                        }, Looper.getMainLooper());

            } else {
                turnOnGPS();
            }

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(DialActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(DialActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    public boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }*/


}