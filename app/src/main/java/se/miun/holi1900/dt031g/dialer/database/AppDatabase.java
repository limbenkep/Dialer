package se.miun.holi1900.dt031g.dialer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = CallInfo.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CallInfoDoa callsInfoDoa();
    private static AppDatabase INSTANCE;
    public static AppDatabase getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "calls_info")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    /**
     * Free resources
     */
    public static void destroyInstance(){INSTANCE = null;}

}
