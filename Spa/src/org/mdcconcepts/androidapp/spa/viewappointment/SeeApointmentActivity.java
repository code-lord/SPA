package org.mdcconcepts.androidapp.spa.viewappointment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.mdcconcepts.androidapp.spa.R;
import org.mdcconcepts.androidapp.spa.makeappointment.SetTherapistActivity;
import org.mdcconcepts.androidapp.spa.notification.MyReceiver;
import org.mdcconcepts.androidapp.spa.serverhandler.JSONParser;
import org.mdcconcepts.androidapp.spa.util.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SeeApointmentActivity extends Activity {

	private PendingIntent pendingIntent;
	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// ids
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	private Calendar cal;
	private int day;
	private int month;
	private int year;

	private int hour;
	private int minute;
	private int AM_PM;
	private String Appointment_Id;
	private String Therapies_Id;
	boolean isTherapistset = true;
	String Therapist_Id;

	TextView UpdateAppointmentDate_TextViewController;
	TextView UpdateAppointmentTime_TextViewController;

	TextView textViewController_SpaName;
	TextView TextViewController_TherapyName;
	TextView TextViewController_TherapyTime;
	TextView UpdateTextViewController_Price;
	TextView UpdateTextViewController_YourTherapist;
	Button Date_ButtonController;
	Button Time_ButtonController;
	Button ButtonController_ChooseTherapist;
	Button buttonController_UpdateAppontment;
	Button buttonController_deleteAppontment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_see_apointment);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Appointment_Id = bundle.getString("Appointment_Id");
		Log.d("Appointment_Id", Appointment_Id);
		UpdateAppointmentDate_TextViewController = (TextView) findViewById(R.id.UpdateAppointmentDate_TextViewController);
		UpdateAppointmentTime_TextViewController = (TextView) findViewById(R.id.UpdateAppointmentTime_TextViewController);
		textViewController_SpaName = (TextView) findViewById(R.id.textViewController_SpaName);
		TextViewController_TherapyName = (TextView) findViewById(R.id.TextViewController_TherapyName);
		TextViewController_TherapyTime = (TextView) findViewById(R.id.TextViewController_TherapyTime);
		UpdateTextViewController_Price = (TextView) findViewById(R.id.UpdateTextViewController_Price);
		UpdateTextViewController_YourTherapist = (TextView) findViewById(R.id.UpdateTextViewController_YourTherapist);

		Date_ButtonController = (Button) findViewById(R.id.UpdateDate_ButtonController);
		ButtonController_ChooseTherapist = (Button) findViewById(R.id.UpdateButtonController_ChooseTherapist);
		Time_ButtonController = (Button) findViewById(R.id.UpdateTime_ButtonController);

		buttonController_UpdateAppontment = (Button) findViewById(R.id.buttonController_UpdateAppontment);
		buttonController_deleteAppontment = (Button) findViewById(R.id.buttonController_deleteAppontment);

		Date_ButtonController.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Date controller", "Click");
				final Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dpdFromDate = new DatePickerDialog(
						SeeApointmentActivity.this, datePickerListener, year,
						month, day);
				dpdFromDate.show();

			}
		});

		Time_ButtonController.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();
				hour = c.get(Calendar.HOUR_OF_DAY);
				minute = c.get(Calendar.MINUTE);

				TimePickerDialog dpdFromDate = new TimePickerDialog(
						SeeApointmentActivity.this, timePickerListener, hour,
						minute, false);
				dpdFromDate.show();

			}
		});
		buttonController_UpdateAppontment
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						if (isTherapistset) {

							new UpdateAnAppointment().execute();

						} else {
							Toast.makeText(SeeApointmentActivity.this,
									"Please Choose Therapist ... !",
									Toast.LENGTH_LONG).show();
							// Log.d("Price iD", ArrayList_AllIDForServiceList
							// .get(SpinnerController_TimeForServicelist
							// .getSelectedItemPosition()));

						}

					}
				});
		buttonController_deleteAppontment
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						new DeleteAnAppointment().execute();

					}
				});
		ButtonController_ChooseTherapist
				.setOnClickListener(new OnClickListener() {

					@SuppressLint("SimpleDateFormat")
					@Override
					public void onClick(View v) {

						Util.Spa_Name = textViewController_SpaName.getText()
								.toString();
						Calendar Appointment_TimeStamp = Calendar.getInstance();

						Appointment_TimeStamp.set(Calendar.DAY_OF_MONTH, day);
						Appointment_TimeStamp.set(Calendar.MONTH, month - 1);
						Appointment_TimeStamp.set(Calendar.YEAR, year);
						Appointment_TimeStamp.set(Calendar.HOUR, hour);
						Appointment_TimeStamp.set(Calendar.MINUTE, minute);
						Appointment_TimeStamp.set(Calendar.AM_PM, AM_PM);

						SimpleDateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Util.Appointment_Time = formatter
								.format(Appointment_TimeStamp.getTime());
						Log.d("Appointment_Time", "" + Util.Appointment_Time);

						Intent intentGetMessage = new Intent(
								SeeApointmentActivity.this,
								SetTherapistActivity.class);
						intentGetMessage.putExtra("Therapies_Id", Therapies_Id);
						startActivityForResult(intentGetMessage, 2);//

						isTherapistset = false;
						UpdateTextViewController_YourTherapist
								.setText("No Therapist Selected");
					}
				});

		new GetSingleAnAppointment().execute();
	}

	// Call Back method to get the Message form other Activity override the
	// method
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		// check if the request code is same as what is passed here it is 2
		if (requestCode == 2) {
			Log.d("Result For Return", data.getStringExtra("Therapist_Name"));
			Therapist_Id = data.getStringExtra("Therapist_Id");
			UpdateTextViewController_YourTherapist.setText(data
					.getStringExtra("Therapist_Name"));
			isTherapistset = true;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.see_apointment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// NavUtils.navigateUpFromSameTask(this);

			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class GetSingleAnAppointment extends AsyncTask<String, String, String> {

		// Progress Dialog
		private ProgressDialog pDialog;

		private String Spa_Name;
		private String Therapies_Name;
		private String Service_Time;
		private String Serevice_Price;
		private String Therapist_Name;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SeeApointmentActivity.this);
			pDialog.setMessage("Getting Appointment Details...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Appointment_Id", ""
						+ Appointment_Id));
				Log.d("Appointment_Id", "" + Appointment_Id);
				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(
						Util.Get_SingleAppointments_URL, "POST", params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {

					try {
						cal = Calendar.getInstance();
						DateFormat formatter = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date date;
						date = formatter.parse(json
								.getString("Appointment_DateTime"));
						Spa_Name = json.getString("Spa_Name");
						Therapies_Name = json.getString("Therapies_Name");
						Service_Time = json.getString("Service_Time");
						Serevice_Price = json.getString("Service_Price");
						Therapist_Name = json.getString("Therapist_Name");
						Therapies_Id = json.getString("Therapies_Id");
						Therapist_Id = json.getString("Therapist_Id");

						cal.setTime(date);

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					day = cal.get(Calendar.DAY_OF_MONTH);
					month = cal.get(Calendar.MONTH);
					year = cal.get(Calendar.YEAR);
					hour = cal.get(Calendar.HOUR);
					minute = cal.get(Calendar.MINUTE);
					AM_PM = cal.get(Calendar.AM_PM);
					month = month + 1;

					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(SeeApointmentActivity.this, file_url,
						Toast.LENGTH_LONG).show();
				String AM_PM_String[] = { "AM", "PM" };

				UpdateAppointmentDate_TextViewController.setText(day + " / "
						+ (month) + " / " + year);
				UpdateAppointmentTime_TextViewController.setText(hour + " : "
						+ (minute) + " : " + AM_PM_String[AM_PM]);
				textViewController_SpaName.setText(Spa_Name);
				TextViewController_TherapyName.setText(Therapies_Name);
				TextViewController_TherapyTime.setText(Service_Time);
				UpdateTextViewController_Price.setText(Serevice_Price);
				UpdateTextViewController_YourTherapist.setText(Therapist_Name);

			}

		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			day = selectedDay;
			month = selectedMonth + 1;
			year = selectedYear;
			UpdateAppointmentDate_TextViewController.setText(selectedDay
					+ " / " + (month) + " / " + selectedYear);
		}
	};
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			if (hour < 12) {

				AM_PM = 00;
				// set current time into textview
				UpdateAppointmentTime_TextViewController
						.setText(new StringBuilder().append(pad(hour))
								.append(":").append(pad(minute)).append(":")
								.append("AM"));

			} else {
				hour = hour - 12;
				if (hour == 0) {
					hour = 12;
				}
				// set current time into textview
				UpdateAppointmentTime_TextViewController
						.setText(new StringBuilder().append(pad(hour))
								.append(":").append(pad(minute)).append(":")
								.append("PM"));
				AM_PM = 01;
			}

		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	class UpdateAnAppointment extends AsyncTask<String, String, String> {

		// Progress Dialog
		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SeeApointmentActivity.this);
			pDialog.setMessage("Updating Appointment...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;

			String AM_PM_String[] = { "AM", "PM" };

			String Appointment_DateTime = year + "-" + month + "-" + day + " "
					+ hour + ":" + minute + " " + AM_PM_String[AM_PM];

			SimpleDateFormat Service_Time_format = new SimpleDateFormat(
					"HH:mm:ss");
			Date date = null;
			try {
				date = Service_Time_format.parse(TextViewController_TherapyTime
						.getText().toString());
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Calendar calendar = GregorianCalendar.getInstance(); //

			// a
			// new
			// calendar
			// instance
			calendar.setTime(date); // assigns calendar to given date
			int end_time_hour = calendar.get(Calendar.HOUR);
			int end_time_minit = calendar.get(Calendar.MINUTE);
			int hr;
			int min;
			hr = hour + end_time_hour;

			if (hr > 12) {
				hr = hr - 12;
				if (hr == 0) {
					hr = 12;
				}
				AM_PM = 1;
			}
			min = minute + end_time_minit;

			String Appointment_EndTime = year + "-" + month + "-" + day + " "
					+ hr + ":" + min + " " + AM_PM_String[AM_PM];
			Log.d("Date TIme", Appointment_DateTime);

			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Appointment_Id", ""
						+ Appointment_Id));
				params.add(new BasicNameValuePair("Appointment_DateTime",
						Appointment_DateTime));
				params.add(new BasicNameValuePair("Therapist_Id", Therapist_Id));
				params.add(new BasicNameValuePair("Appointment_EndTime",
						Appointment_EndTime));
				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(
						Util.UpdateAppointments_URL, "POST", params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {

					calendar = Calendar.getInstance();

					calendar.set(Calendar.MONTH, month - 1);
					calendar.set(Calendar.YEAR, year);
					calendar.set(Calendar.DAY_OF_MONTH, day);

					calendar.set(Calendar.HOUR_OF_DAY, hour);
					calendar.set(Calendar.MINUTE, minute);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.AM_PM, AM_PM);

					Intent myIntent = new Intent(SeeApointmentActivity.this,
							MyReceiver.class);

					myIntent.putExtra("title", "Appointment for spa");
					myIntent.putExtra("notes", "hour for appointment");
					myIntent.putExtra("Appointment_Id", Appointment_Id);

					pendingIntent = PendingIntent.getBroadcast(
							SeeApointmentActivity.this,
							Integer.parseInt(Appointment_Id), myIntent, 0);

					AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC,
							calendar.getTimeInMillis(), pendingIntent);
					Log.d("Calender seconds!", "" + calendar.getTimeInMillis());
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(SeeApointmentActivity.this, file_url,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	class DeleteAnAppointment extends AsyncTask<String, String, String> {

		// Progress Dialog
		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SeeApointmentActivity.this);
			pDialog.setMessage("Getting Appointment Details...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Appointment_Id", ""
						+ Appointment_Id));
				Log.d("Appointment_Id", "" + Appointment_Id);
				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(
						Util.DeleteAppointments_URL, "POST", params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {

					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(SeeApointmentActivity.this, file_url,
						Toast.LENGTH_LONG).show();
				finish();
			}

		}

	}

}
