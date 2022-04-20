package se.miun.holi1900.dt031g.dialer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class DialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial);
        // Initialize the singleton class SoundPlayer instance to make sure the sounds are completely
        // loaded and ready to be used by child activity DialButtonView.
        // Instantiating ensures that all 12 buttons do not need to instantiate
        SoundPlayer.getInstance(getApplicationContext());
    }
}