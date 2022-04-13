package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Dialpad extends ConstraintLayout {
    public Dialpad(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public Dialpad(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet){
        inflate(context, R.layout.dialpad, this);

        //Get reference for all the DialButtonView in the dial pad.
        DialButtonView dial_1 = findViewById(R.id.dial_1);
        DialButtonView dial_2 = findViewById(R.id.dial_2);
        DialButtonView dial_3 = findViewById(R.id.dial_3);
        DialButtonView dial_4 = findViewById(R.id.dial_4);
        DialButtonView dial_5 = findViewById(R.id.dial_5);
        DialButtonView dial_6 = findViewById(R.id.dial_6);
        DialButtonView dial_7 = findViewById(R.id.dial_7);
        DialButtonView dial_8 = findViewById(R.id.dial_8);
        DialButtonView dial_9 = findViewById(R.id.dial_9);
        DialButtonView dial_0 = findViewById(R.id.dial_0);
        DialButtonView dial_star = findViewById(R.id.dial_star);
        DialButtonView dial_hash_tag = findViewById(R.id.dial_hash_tag);

    }



}
