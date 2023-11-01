package emedp.notificationlogger.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
@androidx.room.Database(entities = {Notification.class}, exportSchema = false, version = 1)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract NotificationDAO notificationDAO();

    public static Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(), Database.class, "notifs_database")
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }
}
