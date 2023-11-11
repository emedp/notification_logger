package emedp.notificationlogger.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notifications")
public class Notification {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo()
    private String appName;
    @ColumnInfo()
    private String time;
    @ColumnInfo()
    private String channel;
    @ColumnInfo()
    private String category;
    @ColumnInfo()
    private String title;
    @ColumnInfo()
    private String text;

    public Notification(String appName, String time, String channel, String category, String title, String text) {
        this.appName = appName;
        this.time = time;
        this.channel = channel;
        this.category = category;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public String getTime() {
        return time;
    }

    public String getChannel() {
        return channel;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
