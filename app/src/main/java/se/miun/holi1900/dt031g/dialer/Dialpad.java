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

public class Dialpad extends ConstraintLayout {


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

    }


}
