package org.mdcconcepts.androidapp.spa.notification;

import org.mdcconcepts.androidapp.spa.R;
import org.mdcconcepts.androidapp.spa.login.LoginActivity;
import org.mdcconcepts.androidapp.spa.util.Util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class MyAlarmService extends Service

{
	private String Noti_title;
	private String Noti_message;
	private int Appointment_Id;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		Noti_title = intent.getExtras().getString("title");
		Noti_message = intent.getExtras().getString("notes");
		Appointment_Id = intent.getExtras().getInt("Appointment_Id");

		Log.d("Noti_title", Noti_title);
		Log.d("Noti_message", Noti_message);
		Log.d("Appointment_Id", "" + Appointment_Id);

		showNotification();
		// mManager = (NotificationManager) this.getApplicationContext()
		// .getSystemService(
		// this.getApplicationContext().NOTIFICATION_SERVICE);
		// Intent intent1 = new Intent(this.getApplicationContext(),
		// NotificationReceiver.class);
		//
		// Notification notification = new Notification(R.drawable.ic_launcher,
		// "This is a test message!", System.currentTimeMillis());
		//
		// intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
		// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//
		// PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
		// this.getApplicationContext(), 0, intent1,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// notification.setLatestEventInfo(this.getApplicationContext(),
		// "AlarmManagerDemo", "This is a test message!",
		// pendingNotificationIntent);
		//
		// mManager.notify(0, notification);
	}

	public void showNotification() {

		// define sound URI, the sound to be played when there's a notification
		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		// intent triggered, you can add other intent for other actions
		Intent intent = new Intent(MyAlarmService.this, LoginActivity.class);

		Util.Appointment_Id = Appointment_Id;
		PendingIntent pIntent = PendingIntent.getActivity(MyAlarmService.this,
				0, intent, 0);

		// this is it, we'll build the notification!
		// in the addAction method, if you don't want any icon, just set the
		// first param to 0
		Notification mNotification = new Notification.Builder(this)

		.setContentTitle("New Post!")
				.setContentText("Here's an awesome update for you!")
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				.setSound(soundUri)

				.addAction(R.drawable.ic_launcher, "View", pIntent)
				.addAction(0, "Remind", pIntent)

				.build();

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// If you want to hide the notification after it was selected, do the
		// code below
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, mNotification);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}