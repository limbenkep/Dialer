package se.miun.holi1900.dt031g.dialer.database;

import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "calls_info_table")
public class CallInfo {

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
}
