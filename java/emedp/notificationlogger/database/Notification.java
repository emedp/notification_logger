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
    @ColumnInfo()
    private String textLines;
    @ColumnInfo()
    private String subText;
    @ColumnInfo()
    private String bigText;

    public Notification(String appName, String time, String channel, String category,
                        String title, String text, String textLines, String subText, String bigText) {
        this.appName = appName;
        this.time = time;
        this.channel = channel;
        this.category = category;
        this.title = title;
        this.text = text;
        this.textLines = textLines;
        this.subText = subText;
        this.bigText = bigText;
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

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextLines() {
        return textLines;
    }

    public void setTextLines(String textLines) {
        this.textLines = textLines;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }
}
