package emedp.notificationlogger.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface NotificationDAO {

    // CRUD
    @Insert
    void insertNotification(Notification notification);

    @Query("DELETE FROM notifications")
    void deleteNotificationsTable();

    @Query("SELECT * FROM notifications ORDER BY id DESC")
    List<Notification> getAllNotifications();

    // WHERES
    @Query("SELECT * FROM notifications WHERE appName = (:appName) ORDER BY id DESC")
    List<Notification> selectNotificationsWhereAppName(String appName);

    // DISTINCT FIELDS
    @Query("SELECT DISTINCT appName FROM notifications ORDER BY appName")
    List<String> selectAppPackagesFromNotifications();

    @Query("SELECT DISTINCT category FROM notifications ORDER BY category")
    List<String> selectCategoriesFromNotifications();

}
