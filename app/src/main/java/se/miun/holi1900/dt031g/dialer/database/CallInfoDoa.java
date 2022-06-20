package se.miun.holi1900.dt031g.dialer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import se.miun.holi1900.dt031g.dialer.Util;

@Dao
public interface CallInfoDoa {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCallInfo(CallInfo...callInfos);

    @Query("SELECT * FROM " + Util.DATABASE_TABLE_NAME)
    LiveData<List<CallInfo>> getAllCallInfo();

    @Query("SELECT * FROM " + Util.DATABASE_TABLE_NAME)
    List<CallInfo> getListOfCallInfo();
    @Delete
    int deleteCallData(CallInfo... callInfos);

}
