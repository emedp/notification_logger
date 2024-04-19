package emedp.notificationlogger.database;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import emedp.notificationlogger.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<MyNotification> notifications;

    public NotificationAdapter(List<MyNotification> notifications) {
        this.notifications = notifications;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh (List<MyNotification> newList) {
        notifications.clear();
        notifications.addAll(newList);
        notifyDataSetChanged();
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

    public static class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tv_app;
        private final TextView tv_time;
        private final TextView tv_channel_title;
        private final TextView tv_category_title;
        private final TextView tv_channel;
        private final TextView tv_category;
        private final TextView tv_title;
        private final TextView tv_text;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_app = itemView.findViewById(R.id.tv_app);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_channel_title = itemView.findViewById(R.id.tv_channel_title);
            tv_category_title = itemView.findViewById(R.id.tv_category_title);
            tv_channel = itemView.findViewById(R.id.tv_channel);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_text = itemView.findViewById(R.id.tv_text);
        }

        void bindHolder (MyNotification notification) {
            tv_app.setText(notification.getAppName());
            tv_time.setText(notification.getTime());
            tv_channel.setText(notification.getChannel());
            tv_category.setText(notification.getCategory());
            tv_title.setText(notification.getTitle());
            tv_text.setText(notification.getText());
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (tv_channel_title.getVisibility() == View.VISIBLE) {
                tv_channel_title.setVisibility(View.GONE);
                tv_category_title.setVisibility(View.GONE);
                tv_channel.setVisibility(View.GONE);
                tv_category.setVisibility(View.GONE);
            } else {
                tv_channel_title.setVisibility(View.VISIBLE);
                tv_category_title.setVisibility(View.VISIBLE);
                tv_channel.setVisibility(View.VISIBLE);
                tv_category.setVisibility(View.VISIBLE);
            }
        }
    }
}
