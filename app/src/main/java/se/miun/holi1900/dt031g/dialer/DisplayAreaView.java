package se.miun.holi1900.dt031g.dialer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.net.URI;
import java.net.URLEncoder;

public class DisplayAreaView extends ConstraintLayout {
    private TextView textView;
    SharedPreferences sharedPreferences;


    public DisplayAreaView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DisplayAreaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.view_display_area, this);

        sharedPreferences = context.getSharedPreferences("dial_numbers", Context.MODE_PRIVATE);
        textView = findViewById(R.id.display_area_textView);

        ImageButton call_button = findViewById(R.id.display_area_call_button);
        call_button.setOnClickListener(view -> {
            //get phone number displayed on dial pad
            CharSequence phoneNumber = textView.getText();
            Log.d("Assignment5", "The phone number to be dialed is"+ ": " + phoneNumber);

            savePhoneNumber(phoneNumber.toString(), context);
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
     * @param text the text to be appended to the text displayed on the textView in the Display area.
     */
    public void appendText(String text){
        textView.append(text);
        invalidate();
        requestLayout();
    }


    /**creates an intent of type ACTION_DIAL which takes the user to the device's phone app,
     *provide phone number to the intent and start the call
     *
     * @param context of the view
     * @param phoneNumber  to be dialed
     */
    public void dialPhoneNumber(Context context, CharSequence phoneNumber) {
        if(phoneNumber.length()>0){
            Intent intent;

            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Make intent of with a call action
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumber));

            } else {
                // Make intent of with a dial action
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
            }
            //start the activity to make the phone call
            context.startActivity(intent);

        }
        else{
            Toast.makeText(context, "Enter Phone number", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Deletes the last digit of the pphone number displayed on the displaya area
     */
    public void deleteOneNumber(){
        CharSequence currentText = textView.getText();
        if(currentText.length()>0){
            textView.setText(currentText.subSequence(0, currentText.length()-1));
            invalidate();
            requestLayout();
        }
    }

    /**
     * Clears all numbers in the display area
     */
    public void clearNumber(){
        if(textView.getText().length()>0){
            textView.setText("");
            invalidate();
            requestLayout();
        }
    }

    /**
     * saves dialed number to shared preference in the setting to store dialed number is selected
     * @param phoneNumber dialed number
     * @param context context
     */
    private void savePhoneNumber(String phoneNumber, Context context){
        if(SettingsActivity.shouldStoreNumbers(context)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(phoneNumber, phoneNumber);
            editor.apply();
        }
    }
}
