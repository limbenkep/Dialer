package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.content.Intent;
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

import java.net.URI;
import java.net.URLEncoder;

public class DisplayAreaView extends ConstraintLayout {
    private TextView textView;
    private ImageButton call_button;
    private Button delete_button;


    public DisplayAreaView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DisplayAreaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet){
        inflate(context, R.layout.view_display_area, this);

        textView = findViewById(R.id.display_area_textView);

        call_button = findViewById(R.id.display_area_call_button);
        call_button.setOnClickListener(view -> {
            //get phone number displayed on dial pad
            CharSequence phoneNumber = textView.getText();
            Log.d("Assignment5", "The phone number to be dialed is"+ ": " + phoneNumber);
            //make phone call
            dialPhoneNumber(context, phoneNumber);
        });

        delete_button = findViewById(R.id.display_area_delete_button);
        delete_button.setOnClickListener(view -> {
            //Delete the last digit of the number on the display area
            deleteOneNumber();
        });

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
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
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
}
