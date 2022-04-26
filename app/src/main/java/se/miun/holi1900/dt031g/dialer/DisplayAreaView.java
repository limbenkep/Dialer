package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

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
        textView = findViewById(R.id.display_area_textView);
        call_button = findViewById(R.id.display_area_call_button);
        delete_button = findViewById(R.id.display_area_delete_button);
        // Apply the  dialed_numbers to DisplayAreaView
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet, R.styleable.DisplayAreaView, 0, 0);
        setText(a.getString(R.styleable.DisplayAreaView_dialed_numbers));
        a.recycle();

    }

    public void setText(String text){
        textView.setText(text);
    }
}
