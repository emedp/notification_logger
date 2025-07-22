package emedpware.notificationlogger.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface NotificationDAO {

    // CRUD
    @Insert
    void insertNotification(MyNotification notification);

    @Query("DELETE FROM Notifications")
    void deleteNotificationsTable();

    @Query("SELECT * FROM Notifications ORDER BY id DESC")
    List<MyNotification> getAllNotifications();

    // WHERES
    @Query("SELECT * FROM Notifications WHERE appName = (:appName) ORDER BY id DESC")
    List<MyNotification> selectNotificationsWhereAppName(String appName);

    // DISTINCT FIELDS
    @Query("SELECT DISTINCT appName FROM Notifications ORDER BY appName")
    List<String> selectAppPackagesFromNotifications();

}
