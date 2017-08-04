package in.pjitsol.zapnabit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;


/**
 * Created by websnoox android on 1/17/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    NotificationCompat.Builder notificationBuilder;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
               7);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
        sendNotification(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"));


    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody,String Title) {
        Intent intent = new Intent(this, BaseFragmentActivity.class);
        intent.putExtra(StaticConstants.NOTIFICATION_CALL,"1");
        intent.putExtra(StaticConstants.USER_TYPE, PrefHelper.
                getStoredString(getApplicationContext(),PrefHelper.PREF_FILE_NAME,StaticConstants.USER_TYPE));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int num = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(messageBody.contains("Ready")|| messageBody.contains("Good-To-Go")){
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(Title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody))
                    .setSound(
                            Uri.parse("android.resource://" + getPackageName()
                                    + "/" + R.raw.noti_sound))
                    .setContentIntent(pendingIntent);
        }
        else{
             notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(Title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody))
                    .setSound(
                            Uri.parse("android.resource://" + getPackageName()
                                    + "/" + R.raw.merchant_bell))
                    .setContentIntent(pendingIntent);
        }



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

   //  in.pjitsol.Interface.NotificationManager.getInstance().notifyPushListener();
    }


}
