package com.the.players.club.firebaseauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "101";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        getToken();
        subscribeTopics();

    }

    private void getToken() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Tokens", "Refreshed token: " + refreshedToken);

       /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.e("Tokensss",  refreshedToken);
            }
        });*/
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a Channel
            CharSequence name = "firebaseNotification";
            String description = "This is the channel to recive notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        /*final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "101")
                .setSmallIcon(R.drawable.shopplus)
                .setContentTitle("IIId")
                .setContentText("dude")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) Math.floor(Math.random()), builder.build());

        Context ctx = this;

        String imgurl = "http://libroservices.com/images/loginlogo.png";
        ImageRequest imageRequest = new ImageRequest(imgurl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Assign the response to an ImageView
                        try {

                            if(response != null) {
                                NotificationCompat.BigPictureStyle big = new NotificationCompat.BigPictureStyle().bigPicture(response);
                                builder.setStyle(big);
                            }


                            //NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                            notificationManager.notify((int) Math.floor(Math.random()), builder.build());
                            Toast.makeText(ctx,"load" ,Toast.LENGTH_LONG).show();
                        } catch (Exception ex) {
                            Toast.makeText(ctx,ex.toString() ,Toast.LENGTH_LONG).show();
                        }

                    }
                }, 0, 0, null, Bitmap.Config.RGB_565,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(ctx,"err" ,Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(ctx);
        queue.add(imageRequest);

        Toast.makeText(ctx,"called" ,Toast.LENGTH_LONG).show();*/
    }

    private void subscribeTopics() {
        FirebaseMessaging.getInstance().subscribeToTopic("newslatter")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                      /*  String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);*/
                        //Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}