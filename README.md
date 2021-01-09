# FireBase Notification on android application And send with php

How to send firebase notification using php and implement on android application

## Table of contents

* [Create Project](#create-project)
* [Setup Firebase Account](#setup-firebase-account)
* [Implement Firebase on android project](#implement-firebase-on-android-project)
* [Create PHP File to send Message](#create-php-file-to-send-message)

## Create Project

* First You need to create project in android studio.

## Setup Firebase Account

The following step to setup firebase account.

* 1. Goto `https://firebase.google.com/`
* 2. Login  and Goto Console
* 3. Now Create a new project ( Must remember enable google analytics )
* 4. Once Project is done Goto Project Setting
* 5. Find General Tab and goto last of the page.
* 6. Now Register your Applicatin and Download google-services.json File
* 7. Downloaded google-services.json put into app folder
* 8. Goto build.gradle (Project File) and implement following

```
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
        google()
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.1.1"
        classpath 'com.google.gms:google-services:4.3.3' // here version may be change check on firebase docs

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

* 9. Goto build.gradle (Project File) and implement following

```
    implementation 'com.google.firebase:firebase-messaging:20.1.0'
    implementation 'com.google.firebase:firebase-analytics'
    implementation 'com.android.volley:volley:1.1.0'

```
* 10. Now Your Project is Setup


## Implement Firebase on android project

* 1. First Goto Main Activity and get the token using following method

```
 private void getToken() {
    // This is use for send notification for single user.
    // this token is generate every application installation so keep this token on your database.
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.e("Tokens", "Refreshed token: " + refreshedToken);
}
```

* 2. Now Write method to create notification channel for 26+ API or greater than User

```
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
}
```

* 3. Now Write method to subscribe firebase channel to send multiple user notification

```
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
```

* 4. All 3 Mehtod Called in onCreate Method

```
private static final String CHANNEL_ID = "101";

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    createNotificationChannel();
    getToken();
    subscribeTopics();

}
```

* 5. Now create a Service Class FirebaseAuthServer ( You can Give your own name ) and implement the follwoing code

```
public class firebaseAuthServices extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Create an explicit intent for an Activity in your app
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

            ----------------------------------------------------------------------------------------------------------------------------------
            --- Below code is generate only text notification
            ----------------------------------------------------------------------------------------------------------------------------------
            /*NotificationManager notificationManager = (NotificationManager)
                                                getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(10, builder.build());*/

            ----------------------------------------------------------------------------------------------------------------------------------
            --- Below code is generate notification with image
            ----------------------------------------------------------------------------------------------------------------------------------
            // notificationId is a unique int for each notification that you must define
            Context ctx = this;
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

```

* 6. Change in AndroidManifest.xml file

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.the.players.club.firebaseauthentication">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:usesCleartextTraffic="true"
        android:theme="@style/Theme.FirebaseAuthentication">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".notification.firebaseAuthServices">
            <intent-filter>
                <action android:name="com.google.firebase.INSTANCE_ID_EVENT"/>
                <action android:name="com.google.firebase.MESSAGING_EVENT"/>

            </intent-filter>

        </service>
    </application>

</manifest>
```

## Create PHP File to send Message

```
<?php

//$to = "cckh-MwVSiCaYM9p-gE71v:APA91bGa45fxn...BK2"; // if you send message to single user require here token fetch from getToken
$to = "/topics/newslatter"; // or if you send multiple Message than wirte following /topic/(Your topic name whose given to subscribeTopics method)

push_notification_android($to,"Its Working");

function push_notification_android($device_id,$message){

    //API URL of FCM
    $url = 'https://fcm.googleapis.com/fcm/send';

    /*api_key available in:
    Firebase Console -> Project Settings -> CLOUD MESSAGING -> Server key*/
   $api_key = 'AAAAC3nPIBw:APA91bHf1rx...3zjHgc';

    // Fields are descibe Below you nee to send inside the data you can create you custom paramenter but notification only title and body
    $fields = array (
        'to' => $device_id,
        'data'=> array(
        		'title'=>"Notification Demo",
        		'desc'=> "Notification Demo With Image"
        		'imgurld'=>"https://cdn.pixabay.com/photo/2020/12/06/19/56/sunset-5809870__340.jpg"
        	),
        "priority"=> "high",
        /*'notification' => array(
        'title' => "dsec",
        'body' => $message
        )*/
    );

    //header includes Content type and api key
    $headers = array(
        'Content-Type:application/json',
        'Authorization:key='.$api_key
    );

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
    $result = curl_exec($ch);
    print_r($result);
    if ($result === FALSE) {
        die('FCM Send Error: ' . curl_error($ch));
    }
    curl_close($ch);
    return $result;
}
?>
```

