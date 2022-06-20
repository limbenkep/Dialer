package se.miun.holi1900.dt031g.dialer.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CallInfoRepository {
    private static final String TAG = "CallInfoRepository";
    private final CallInfoDoa callInfoDoa;
    private final Context context;

    public CallInfoRepository(Context context) {
        this.context = context;
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

    public void deleteAllCallRecords(){
        DeleteAllCallInfoAsyncTask deleteAllCallInfoAsyncTask = new DeleteAllCallInfoAsyncTask();
        deleteAllCallInfoAsyncTask.execute();
    }
    private class InsertCallInfoAsyncTask extends AsyncTask<CallInfo, Integer, Void>{
        @Override
        protected Void doInBackground(CallInfo... callInfos) {
                callInfoDoa.insertCallInfo(callInfos[0]);
                return null;
        }
    }

    private class DeleteAllCallInfoAsyncTask extends AsyncTask<Void, Integer, Integer>{

        @Override
        protected Integer doInBackground(Void... voids) {
            List<CallInfo> callData = callInfoDoa.getListOfCallInfo();
            int size = callData.size();
            int deleted = 0;
            if(size != 0){
                for(int i=0; i<size; i++){
                   deleted += callInfoDoa.deleteCallData(callData.get(i));
                }
            }

            return deleted ;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Toast.makeText(context, integer + " call records deleted.", Toast.LENGTH_SHORT).show();
        }
    }
}
