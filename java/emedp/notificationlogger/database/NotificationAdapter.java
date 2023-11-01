package emedp.notificationlogger.database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import emedp.notificationlogger.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bindHolder(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_app;
        private final TextView tv_time;
        private final TextView tv_channel;
        private final TextView tv_category;
        private final TextView tv_title;
        private final TextView tv_text;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_app = itemView.findViewById(R.id.tv_app);
            tv_time = itemView.findViewById(R.id.tv_time);;
            tv_channel = itemView.findViewById(R.id.tv_channel);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_text = itemView.findViewById(R.id.tv_text);
        }

        void bindHolder (Notification notification) {
            tv_app.setText(notification.getAppName());
            tv_time.setText(notification.getTime());
            tv_channel.setText(notification.getChannel());
            tv_category.setText(notification.getCategory());
            tv_title.setText(notification.getTitle());
            tv_text.setText(notification.getText());
        }
    }
}
