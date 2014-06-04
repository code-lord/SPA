package org.mdcconcepts.androidapp.spa.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service1 = new Intent(context, MyAlarmService.class);

		String Noti_title = intent.getExtras().getString("title");
		String Noti_message = intent.getExtras().getString("notes");
		int Appointment_Id = intent.getExtras().getInt("Appointment_Id");

		Log.d("Noti_title", Noti_title);
		Log.d("Noti_message", Noti_message);
		Log.d("Appointment_Id", "" + Appointment_Id);

		service1.putExtra("title", Noti_title);
		service1.putExtra("notes", Noti_message);
		service1.putExtra("Appointment_Id", Appointment_Id);

		context.startService(service1);

	}

}
