package com.example.firebase.Notifications;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.firebase.DisplayEventActivity;
import com.example.firebase.DisplayTaskActivity;
import com.example.firebase.HomeActivity;
import com.example.firebase.R;
import com.example.firebase.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



public class MyAndroidFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyAndroidFCMService";
    String notificationBody ;
    String notificationTitle;
    String notificationData ;
    private static final int BROADCAST_NOTIFICATION_ID = 1;

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String dataType = remoteMessage.getData().get(getString(R.string.data_type));
        if(dataType.equals(getString(R.string.direct_message))){
            Log.d(TAG, "onMessageReceived: new incoming message.");
            String title = remoteMessage.getData().get(getString(R.string.data_title));
            String message = remoteMessage.getData().get(getString(R.string.data_message));
            String messageId = remoteMessage.getData().get(getString(R.string.data_message_id));
            String senderName = remoteMessage.getData().get("sender_name");
            if(remoteMessage.getData().get("profile_id") != null) {
                String senderId = remoteMessage.getData().get("profile_id");
                sendMessageNotification(title, message, messageId,senderName,senderId);
            }
            else if(remoteMessage.getData().get("sender_id") != null)
            {
                String Committee = remoteMessage.getData().get("Committee");
                String senderId = remoteMessage.getData().get("sender_id");
                sendCommitteeMessageNotification(title, message, messageId,senderName,Committee,senderId);
            }

        }
        else  {
            Log.d(TAG, "onMessageReceived: new incoming message.");
            String Topic = remoteMessage.getData().get("Topic");
            String Details = remoteMessage.getData().get("Details");
            String Date = remoteMessage.getData().get("Date");
            String Location = remoteMessage.getData().get("Location");
            String Deadline = remoteMessage.getData().get("Deadline");
            String EventID = remoteMessage.getData().get("EventID");
            String TaskID = remoteMessage.getData().get("TaskID");
            String Target = remoteMessage.getData().get("Target");
            String img = remoteMessage.getData().get("ImageUrl");
            sendAnyNotification(Topic,Details,Date,Location,Deadline,EventID,TaskID,Target,img , dataType);
        }

    }

    private void sendAnyNotification(String topic, String details, String date, String location, String deadline, String eventID, String taskID, String target, String img, String dataType) {
        int notificationId=0;
        if(taskID !=null) {//get the notification id
            notificationId= buildNotificationId(taskID);
        }

        //get the notification id
        else if(eventID!=null){
            notificationId = buildNotificationId(eventID);
        }


        Intent intent ;
        PendingIntent pendingIntent;
        switch (dataType){
            case "Committee_Event":
            case "General_Event":
                intent = new Intent(this, DisplayEventActivity.class);
                intent.putExtra("Topic",topic);
                intent.putExtra("Details",details);
                intent.putExtra("Date",date);
                intent.putExtra("Location",location);
                if(img !=null)
                    intent.putExtra("ImageUrl",img);
                else
                    intent.putExtra("ImageUrl","");
                intent.putExtra("Target",target);
                intent.putExtra("EventID",eventID);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                break;


            case "Committee_Task":
                intent = new Intent(this, DisplayTaskActivity.class);
                intent.putExtra("Topic",topic);
                intent.putExtra("Details",details);
                intent.putExtra("DeadLine",deadline);
                intent.putExtra("Target",target);
                intent.putExtra("TaskID",taskID);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + topic);
        }





        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(topic)
                        .setContentText("You have new Announcement To Show")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());


    }

    /**
     * Build a push notification for a chat message
     * @param title
     * @param message
     */
    private void sendMessageNotification(String title, String message, String messageId ,String senderName,String senderID){
        Log.d(TAG, "sendMessageNotification: Title: " + title);
        Log.d(TAG, "sendMessageNotification: notification body: " + message);
        Log.d(TAG, "sendMessageNotification: notification messageId: " + messageId);


        //get the notification id
        int notificationId = buildNotificationId(messageId);



        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("ProfileUID",senderID);
        intent.putExtra("chatType",0);
        intent.putExtra("CommitteeName","");
        intent.putExtra("ProfileUserName",senderName);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        if(!(senderID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
        }
    }


    private void sendCommitteeMessageNotification(String title, String message, String messageId ,String senderName,String Committee,String senderID ){
        Log.d(TAG, "sendMessageNotification: Title: " + title);
        Log.d(TAG, "sendMessageNotification: notification body: " + message);
        Log.d(TAG, "sendMessageNotification: notification messageId: " + messageId);


        //get the notification id
        int notificationId = buildNotificationId(messageId);



        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("ProfileUID","");
        if(Committee == "IEEECUSB"){
            intent.putExtra("chatType",2);
            intent.putExtra("CommitteeName","");
        }
        else {
            intent.putExtra("chatType", 1);
            intent.putExtra("CommitteeName",Committee);
        }
        intent.putExtra("ProfileUserName","");

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        if(!(senderID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
            notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
        }

    }


    private int buildNotificationId(String id){
        Log.d(TAG, "buildNotificationId: building a notification id.");

        int notificationId = 0;
        for(int i = 0; i < 9; i++){
            notificationId = notificationId + id.charAt(0);
        }
        Log.d(TAG, "buildNotificationId: id: " + id);
        Log.d(TAG, "buildNotificationId: notification id:" + notificationId);
        return notificationId;
    }

}

