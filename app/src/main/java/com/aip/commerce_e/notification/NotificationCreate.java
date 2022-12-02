package com.aip.commerce_e.notification;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.aip.commerce_e.R;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationCreate {

    private String title;
    private String content;

    public NotificationCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NotificationCreate() {
    }

    public void notificationAdd(Context context, String CHANNEL_ID){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_shopping_cart_24)
                .setContentTitle(this.title)
                .setContentText(this.content)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(this.content))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        int notificationId = 10;
        notificationManager.notify(notificationId, builder.build());
    }
}
