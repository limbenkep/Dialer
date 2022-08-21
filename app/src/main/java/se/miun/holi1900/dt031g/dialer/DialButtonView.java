package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DialButtonView extends ConstraintLayout implements View.OnClickListener {
    private Animation zoom_in;
    private TextView titleView;
    private TextView messageView;

    /**
     * Interface definition for a callback to be invoked when the DialButtonView is
     * is clicked.
     */
    public interface OnClickedListener{
        void onClick(DialButtonView dialButtonView);
    }

    /**
     * Listener used to dispatch click events to.
     */
    private  DialButtonView.OnClickedListener listener;

    /**
     * Register a callback to be invoked when the button is clicked.
     *
     * @param listener The callback that will run when this button is clicked
     */
    public void setOnClickedListener(OnClickedListener listener){
        this.listener = listener;
    }

    /**
     * Constructor to use when creating a DialButtonView from code.
     * @param context The Context the DialButtonView is running in, through which it can access
     *                the current theme, resources, etc.
     */
    public DialButtonView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Constructor that is called when inflating a DialButtonView from XML. This is called when a
     * DialButtonView is being constructed from an XML file, supplying attributes that were specified in
     * the XML file.
     * @param context The Context the DialButtonView is running in, through which it can access
     *                the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view. This value may be null.
     */
    public DialButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    /**
     * Initializes the DialButtonView when it's created.
     * @param context The Context the DialButtonView is running in, through which it can access
     *                the current theme, resources, etc.
     * @param attributeSet The attributes of the XML tag that is inflating the view. This value may be null.
     */
    private void init(Context context, AttributeSet attributeSet) {

        inflate(context, R.layout.view_dial_button, this);

        //Get reference to the two textView in the button
        titleView = findViewById(R.id.titleView);
        messageView = findViewById(R.id.messageView);

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
        titleView.setText(first_letter);
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
        messageView.setText(shortMessage);
    }

    /**
     *
     * @return the title of the dial button
     */
    public String getTitle(){
        return (String) titleView.getText();
    }

    /**
     * overrides the onClick method of the OnClickListener class
     * on click the button animated with a zoomed in animation and plays sound that says the title of
     * the button
     * @param view view
     */
    @Override
    public void onClick(View view) {
        //When this button is clicked, call the dial button's  custom onClickedListener if one is set.
        if(listener!=null){
            listener.onClick(this);
        }
        //Get zoom in animation from resource, set visibility and start animation
        zoom_in = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.zoom_in);
        setVisibility(View.VISIBLE);
        startAnimation(zoom_in);
        // Get the an instance of the singleton class SoundPlayer
        // the reference to this instance should not be stored in this class
        // Call soundPlayer class method to play sound and pass the reference of this dial button
        SoundPlayer.getInstance(getContext()).playSound(this);
    }
}
