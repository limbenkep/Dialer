package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DialButtonView extends ConstraintLayout {
    public DialButtonView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DialButtonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_dial_button, this);
    }

    public void setTitle(String title) {
        TextView textView = findViewById(R.id.titleView);
        textView.setText(title);
        super.invalidate();
    }

    public void setMessage(String message) {
        TextView textView = findViewById(R.id.messageView);
        textView.setText(message);
    }

}
