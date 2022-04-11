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
        startActivity(new Intent(this, DialActivity.class));
    }

    /**Called when the Call List button is clicked*/
    public void displayCallList(View view) {
        startActivity(new Intent(this, CallListActivity.class));
    }

    /**Called when the Settings button is clicked*/
    public void displaySettings(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    /**Called when the Map button is clicked*/
    public void displayMap(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }

    /**Called when the About button is clicked*/
    public void openAboutDialog(View view){
        AboutDialog aboutDialog = new AboutDialog();
        aboutDialog.show(getSupportFragmentManager(), "about dialog");
    }

}