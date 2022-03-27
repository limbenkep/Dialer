package se.miun.holi1900.dt031g.dialer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**Called when the DIAL button is clicked*/
    public void displayKeyPad(View view) {
        Intent intent = new Intent(this, DialActivity.class);
        startActivity(intent);
    }

    /**Called when the Call List button is clicked*/
    public void displayCallList(View view) {
        Intent intent = new Intent(this, CallListActivity.class);
        startActivity(intent);
    }

    /**Called when the Settings button is clicked*/
    public void displaySettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}