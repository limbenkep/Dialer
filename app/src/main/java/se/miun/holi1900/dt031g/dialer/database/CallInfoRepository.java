package se.miun.holi1900.dt031g.dialer.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CallInfoRepository {
    private static final String TAG = "CallInfoRepository";
    private final CallInfoDoa callInfoDoa;

    public CallInfoRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        callInfoDoa = db.callsInfoDoa();
    }

    public void insertCallInfo(CallInfo callInfo){
        InsertCallInfoAsyncTask insertTask = new InsertCallInfoAsyncTask();
        insertTask.execute(callInfo);
    }

    public LiveData<List<CallInfo>> getAllCallInfo(){
        return callInfoDoa.getAllCallInfo();
    }

    private class InsertCallInfoAsyncTask extends AsyncTask<CallInfo, Integer, Void>{
        @Override
        protected Void doInBackground(CallInfo... callInfos) {
                callInfoDoa.insertCallInfo(callInfos[0]);
                return null;
        }
    }
}
