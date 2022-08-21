package se.miun.holi1900.dt031g.dialer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.miun.holi1900.dt031g.dialer.database.CallInfo;
import se.miun.holi1900.dt031g.dialer.database.CallInfoRepository;

public class DisplayAreaView extends ConstraintLayout {
    private static final String TAG = "DisplayAreaView";
    private TextView textView;
    SharedPreferences sharedPreferences;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private double latitude;
    private double longitude;
    CharSequence phoneNumber;


    public DisplayAreaView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DisplayAreaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_display_area, this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        sharedPreferences = context.getSharedPreferences("dial_numbers", Context.MODE_PRIVATE);
        textView = findViewById(R.id.display_area_textView);

        ImageButton call_button = findViewById(R.id.display_area_call_button);
        call_button.setOnClickListener(view -> {
            //get phone number displayed on dial pad
            phoneNumber = textView.getText();
            Log.d("Assignment5", "The phone number to be dialed is" + ": " + phoneNumber);
            getDeviceLocation(context);

            //make phone call
            dialPhoneNumber(context, phoneNumber);
        });

        Button delete_button = findViewById(R.id.display_area_delete_button);

        //Set click listener to delete button which delete the last dialed number on click
        delete_button.setOnClickListener(view -> deleteOneNumber());

        //Set listener  for long click to delete button which delete all displayed numbers on long click
        delete_button.setOnLongClickListener(view -> {
            clearNumber();
            return true;
        });
    }



    /**
     * Appends text to the textView and redraw the Display area
     *
     * @param text the text to be appended to the text displayed on the textView in the Display area.
     */
    public void appendText(String text) {
        textView.append(text);
        invalidate();
        requestLayout();
    }


    /**
     * creates an intent of type ACTION_DIAL which takes the user to the device's phone app,
     * provide phone number to the intent and start the call
     *
     * @param context     of the view
     * @param phoneNumber to be dialed
     */
    public void dialPhoneNumber(Context context, CharSequence phoneNumber) {
        if (phoneNumber.length() > 0) {
            Intent intent;

            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Make intent of with a call action
                intent = new Intent(Intent.ACTION_CALL);

            } else {
                // Make intent of with a dial action
                intent = new Intent(Intent.ACTION_DIAL);
            }
            intent.setData(Uri.parse("tel:" + phoneNumber));
            //start the activity to make the phone call
            context.startActivity(intent);

        } else {
            Toast.makeText(context, "Enter Phone number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deletes the last digit of the phone number displayed on the display area
     */
    public void deleteOneNumber() {
        CharSequence currentText = textView.getText();
        if (currentText.length() > 0) {
            textView.setText(currentText.subSequence(0, currentText.length() - 1));
            invalidate();
            requestLayout();
        }
    }

    /**
     * Clears all numbers in the display area
     */
    public void clearNumber() {
        if (textView.getText().length() > 0) {
            textView.setText("");
            invalidate();
            requestLayout();
        }
    }

    /**
     * save call information to the database
     *
     * @param phoneNumber dialed number
     * @param date        date and time call was made
     * @param location    location call was made
     * @param context     context
     */
    private void saveCallInfo(String phoneNumber, String date, Location location, Context context) {
        CallInfoRepository repo = new CallInfoRepository(context);
        CallInfo callInfo = new CallInfo();
        callInfo.phoneNumber = phoneNumber;
        callInfo.date = date;
        callInfo.latitude = latitude;
        callInfo.longitude = longitude;
        repo.insertCallInfo(callInfo);

        Log.d(TAG, "saveCallInfo: latitude: " + latitude + " Longitude: " + longitude);
        Log.d(TAG, "saveCallInfo: call latitude: " + callInfo.latitude + " call Longitude: " + callInfo.longitude);
    }



    /**
     * Get the device location.
     * If location permission is granted the location is obtained and
     * the call information is saved
     * If location permission is not granted, the call information is saved without location
     */
    public void getDeviceLocation(Context context) {
        if (ContextCompat.checkSelfPermission(context, FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location !=null){
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.d(TAG, "onSuccess: Location latitude = "
                                + latitude + ", longitude = " + longitude);
                        saveCallInfo(phoneNumber.toString(), getCurrentDateAndTime(), currentLocation, context);
                    }
                }
            });
        } else {
            saveCallInfo(phoneNumber.toString(), getCurrentDateAndTime(), currentLocation, context);
        }
    }

    /**
     * Get the current date and time
     * @return date and time as a string in format "yyyy-MM-dd HH:mm:ss"
     */
    private String getCurrentDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(Calendar.getInstance().getTime());
    }
}