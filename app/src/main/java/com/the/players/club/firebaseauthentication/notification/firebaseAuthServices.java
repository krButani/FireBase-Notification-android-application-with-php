package com.the.players.club.firebaseauthentication.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.the.players.club.firebaseauthentication.MainActivity;
import com.the.players.club.firebaseauthentication.R;

public class firebaseAuthServices extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Create an explicit intent for an Activity in your app
        Log.e("Datac","Datasize "+ remoteMessage.getData().size());

        if(remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String desc = remoteMessage.getData().get("desc");
            String imgurl = remoteMessage.getData().get("imgurl");


            Intent intent = new Intent(this, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "101")
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setChannelId("101")
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder.setSmallIcon(R.drawable.shopplus);
                        builder.setColor(getResources().getColor(R.color.black));
                    } else {
                        builder.setSmallIcon(R.drawable.shopplus);
                    }


                // notificationId is a unique int for each notification that you must define

                Context ctx = this;
                //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                ImageRequest imageRequest = new ImageRequest(imgurl,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                // Assign the response to an ImageView
                                NotificationCompat.BigPictureStyle big = new NotificationCompat.BigPictureStyle().bigPicture(response);
                                builder.setStyle(big);


                                NotificationManager notificationManager = (NotificationManager)
                                        getSystemService(NOTIFICATION_SERVICE);

                                notificationManager.notify(10, builder.build());
                                Log.e("Datac","ddimgurl "+ imgurl);
                            }
                        }, 0, 0, null, Bitmap.Config.RGB_565,new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Datac","imgerror ");
                    }
                });
                //add request to queue
                RequestQueue queue = Volley.newRequestQueue(ctx);
                queue.add(imageRequest);



        }



    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
