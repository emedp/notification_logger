package emedp.notificationlogger.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface NotificationDAO {

    // CRUD
    @Insert
    void insertNotification(Notification notification);

    @Update
    void updateNotification(Notification notification);

    @Delete
    void deleteNotification(Notification notification);

    @Query("DELETE FROM notifications")
    void deleteNotificationsTable();

    @Query("SELECT * FROM notifications ORDER BY id")
    List<Notification> getAllNotifications();

    // WHERES

    @Query("SELECT * FROM notifications WHERE appName = (:appName)")
    List<Notification> selectNotificationsWhereAppName(String appName);

    @Query("SELECT * FROM notifications WHERE channel = (:channel)")
    List<Notification> selectNotificationsWhereChannel(String channel);

    @Query("SELECT * FROM notifications WHERE category = (:category)")
    List<Notification> selectNotificationsWhereCategory(String category);

    @Query("SELECT * FROM notifications WHERE title LIKE (:title)")
    List<Notification> selectNotificationsWhereTitle(String title);

    // DISTINCT FIELDS
    @Query("SELECT DISTINCT appName FROM notifications ORDER BY appName")
    List<String> selectNotificationsAllAppNames();

    @Query("SELECT DISTINCT category FROM notifications ORDER BY category")
    List<String> selectNotificationsAllCategories();

    @Query("SELECT DISTINCT channel FROM notifications ORDER BY channel")
    List<String> selectNotificationsAllChannels();

}
