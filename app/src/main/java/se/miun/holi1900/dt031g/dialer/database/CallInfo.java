package se.miun.holi1900.dt031g.dialer.database;

import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "calls_info_table")
public class CallInfo implements Comparable<CallInfo>{

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name="phoneNumber")
    public String phoneNumber;

    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name="latitude")
    public double latitude;

    @ColumnInfo(name="longitude")
    public double longitude;


    @Override
    public int compareTo(CallInfo o) {
        if(this.date==null || o.date == null){
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate, oDate;
        try {
            currentDate = sdf.parse(date);
            oDate = sdf.parse(o.date);
            return currentDate.compareTo(oDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
