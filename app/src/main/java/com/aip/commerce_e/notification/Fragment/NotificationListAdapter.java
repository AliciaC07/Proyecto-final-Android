package com.aip.commerce_e.notification.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.aip.commerce_e.R;
import com.aip.commerce_e.notification.Notifications;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AllArgsConstructor
public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder> {

    List<Notifications> notifications;
    private final NotificationInterface notificationInterface;

    @NonNull
    @NotNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_card, parent, false), notificationInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationViewHolder holder, int position) {
        Notifications notification = notifications.get(position);
        holder.contentTxt.setText(notification.getContent());
        holder.appTxt.setText(notification.getApp());
        holder.titleTxt.setText(notification.getTitle());
        holder.timeTxt.setText(notification.getTime());
    }

    @Override
    public int getItemCount() {
        if(notifications != null)
            return notifications.size();
        return 0;
    }

    public void setNotifications(List<Notifications> notifications){
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView timeTxt, appTxt, titleTxt, contentTxt;
        LinearLayout card;
        public NotificationViewHolder(@NonNull @NotNull View itemView, NotificationInterface notificationInterface) {
            super(itemView);
            timeTxt = itemView.findViewById(R.id.notification_time);
            appTxt = itemView.findViewById(R.id.notification_app);
            titleTxt = itemView.findViewById(R.id.notification_title);
            contentTxt = itemView.findViewById(R.id.notification_content);
            card = itemView.findViewById(R.id.notification_card);

            if (notificationInterface != null){
                card.setOnClickListener(view -> {
                    Integer pos = getBindingAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                        notificationInterface.navigateOnCLick(pos);
                });
            }
        }
    }
}
