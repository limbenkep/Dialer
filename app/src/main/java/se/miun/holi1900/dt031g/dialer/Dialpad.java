package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;
import java.util.Map;

public class Dialpad extends ConstraintLayout implements DialButtonView.OnClickedListener{
    DialButtonView button_1, button_2, button_3, button_4, button_5, button_6, button_7, button_8, button_9;
    DialButtonView button_0, button_pound, button_star;
    DisplayAreaView displayArea;


    public Dialpad(@NonNull Context context) {
        super(context);
        init(context);
    }

    public Dialpad(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.dialpad, this);

        button_0 = findViewById(R.id.dial_0);
        button_1 = findViewById(R.id.dial_1);
        button_2 = findViewById(R.id.dial_2);
        button_3 = findViewById(R.id.dial_3);
        button_4 = findViewById(R.id.dial_4);
        button_5 = findViewById(R.id.dial_5);
        button_6 = findViewById(R.id.dial_6);
        button_7 = findViewById(R.id.dial_7);
        button_8 = findViewById(R.id.dial_8);
        button_9 = findViewById(R.id.dial_9);
        button_pound= findViewById(R.id.dial_hash_tag);
        button_star= findViewById(R.id.dial_star);

        displayArea = findViewById(R.id.dialpad_display_area);

        button_0.setOnClickedListener(this);
        button_1.setOnClickedListener(this);
        button_2.setOnClickedListener(this);
        button_3.setOnClickedListener(this);
        button_4.setOnClickedListener(this);
        button_5.setOnClickedListener(this);
        button_6.setOnClickedListener(this);
        button_7.setOnClickedListener(this);
        button_8.setOnClickedListener(this);
        button_9.setOnClickedListener(this);
        button_0.setOnClickedListener(this);
        button_pound.setOnClickedListener(this);
        button_star.setOnClickedListener(this);
    }

    /**
     * overrides onClick method for the DialButtonView custom onClickedListener
     * Adds the title of a button to the display area
     * @param dialButtonView that is clicked
     */
    @Override
    public void onClick(DialButtonView dialButtonView) {
        displayArea.appendText(dialButtonView.getTitle());
    }

}
