package se.miun.holi1900.dt031g.dialer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import se.miun.holi1900.dt031g.dialer.database.CallInfo;
import se.miun.holi1900.dt031g.dialer.database.CallInfoRepository;

public class CallListActivity extends AppCompatActivity {
    private static final String TAG = "CallListActivity";

    protected RecyclerView.LayoutManager layoutManager;
    protected RecyclerView recyclerView;
    protected CallInfoAdapter callInfoAdapter;

    private SharedPreferences sharedPreferences;
    private final ArrayList<String> savedPhoneNumbers = new ArrayList<>();
    private final  static  String phoneNumbersFile = "dial_numbers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);

        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        TextView defaultTextView = findViewById(R.id.default_text_view);
        new CallInfoRepository(this).getAllCallInfo().observe(this, callData->{
            Collections.sort(callData);
            Collections.reverse(callData);

            Log.d(TAG, "onCreate: list size: " + callData.size());
            callInfoAdapter = new CallInfoAdapter(callData);
            if(callInfoAdapter.getItemCount() !=0){
                defaultTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(callInfoAdapter);

            }
            else{
                defaultTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                defaultTextView.setText(getString(R.string.empty_call_list_message));
            }
        });
    }


    /**
     * Called when the menu is opened for the first time. Inflates the menu resource to the menu
     * @param menu menu to inflate menu resource to
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_number_menu, menu);
        return true;
    }

    /**
     * Called when the user selects an item from the options menu
     * @param item menu item selected
     * @return successful implementation returns true while default implementation returns false
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //if selected item is settings option start the setting activity
        if(item.getItemId()==R.id.delete_stored_numbers){
            boolean fileDeleted = deleteStoredPhoneNumbers(this);

            //redraw view after deleting list
            startActivity(getIntent());
            finish();
            Log.d("assignments", "Contact list deleted: " + fileDeleted);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Deletes all call records from database
     * @param context the activity
     *
     * @return true
     */
    public static boolean deleteStoredPhoneNumbers(Context context){
        CallInfoRepository repo = new CallInfoRepository(context);
        repo.deleteAllCallRecords();
        return true;
    }
}