package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DialButtonView extends ConstraintLayout implements View.OnClickListener {
    public DialButtonView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DialButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {

        inflate(context, R.layout.view_dial_button, this);

        //Get reference to the two textView in the button
        TextView titleView = findViewById(R.id.titleView);
        TextView messageView = findViewById(R.id.messageView);

        setOnClickListener(this);

        // Apply the title text and the message text of the dial button
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet, R.styleable.DialButtonView, 0, 0);
        setTitle(a.getString(R.styleable.DialButtonView_title));
        setMessage(a.getString(R.styleable.DialButtonView_message));
        a.recycle();
    }

    /**
     * set a new button title
     * @param title the new title of the dial button
     */
    public void setTitle(String title) {
        String first_letter = "";
        if(title!=null && !title.isEmpty()){
            first_letter = title.substring(0, 1);
        }
        TextView textView = findViewById(R.id.titleView);
        textView.setText(first_letter);
        //super.invalidate();
    }

    /**
     * sets a new button message
     * @param message the new message of the dial button
     */
    public void setMessage(String message) {
        String shortMessage = "";
        if(message!=null){
            int messageLength = message.length();
            if(messageLength<4){
                shortMessage = message;
            }
            else{
                shortMessage = message.substring(0, 3);
            }
        }
        TextView textView = findViewById(R.id.messageView);
        textView.setText(shortMessage);
    }

    @Override
    public void onClick(View view) {
        // TODO: implementation
    }
}
