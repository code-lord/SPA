package org.mdcconcepts.androidapp.spa.signup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.mdcconcepts.androidapp.spa.R;
import org.mdcconcepts.androidapp.spa.customitems.CustomValidator;
import org.mdcconcepts.androidapp.spa.login.LoginActivity;
import org.mdcconcepts.androidapp.spa.serverhandler.JSONParser;
import org.mdcconcepts.androidapp.spa.util.Util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// ids
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	EditText Username;
	EditText Password;
	EditText Password_Conform;
	EditText Name;
	EditText Mobile;
	EditText Email;
	EditText Address;
	Button Sign_Up_ButtonController;
	Button AlreadyHaveAccount_ButtonController;
	EditText DOB;
	EditText Anniversary;

	public int whoCalls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		Username = (EditText) findViewById(R.id.Username);
		Password = (EditText) findViewById(R.id.Password);
		Password_Conform = (EditText) findViewById(R.id.Password_Conform);
		Name = (EditText) findViewById(R.id.Name);
		Mobile = (EditText) findViewById(R.id.Mobile);
		Email = (EditText) findViewById(R.id.Email);
		Address = (EditText) findViewById(R.id.Address);
		DOB = (EditText) findViewById(R.id.DOB);
		Anniversary = (EditText) findViewById(R.id.Anniversary);

		Sign_Up_ButtonController = (Button) findViewById(R.id.Sign_Up);

		Sign_Up_ButtonController.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (Username.getText().toString().trim().isEmpty()) {
					Username.setError("Please Enter Username !");
				} else if (Password.getText().toString().trim().isEmpty()) {
					Password.setError("Please Enter Password !");
				} else if (Password_Conform.getText().toString().trim()
						.isEmpty()) {
					Password_Conform
							.setError("Please Enter Conform Password !");
				} else if (Name.getText().toString().trim().isEmpty()) {
					Name.setError("Please Enter Name !");
				} else if (Mobile.getText().toString().trim().isEmpty()) {
					Mobile.setError("Please Enter Mobile !");
				} else if (Email.getText().toString().trim().isEmpty()) {
					Email.setError("Please Enter Email !");
				} else if (Address.getText().toString().trim().isEmpty()) {
					Address.setError("Please Enter Address !");
				} else if (DOB.getText().toString().trim().isEmpty()) {
					DOB.setError("Please Enter Date Of Birth !");
				} else if (!Password.getText().toString().trim()
						.equals(Password_Conform.getText().toString().trim())) {
					Password_Conform
							.setError("Please Password  Fields Doesn't Match!");
				} else if (!CustomValidator.isValidEmail(Email.getText()
						.toString().trim())) {
					Email.setError("Please Enter Correct Email Address !");
				} else if (!CustomValidator.isValidPhoneNumber(Mobile.getText()
						.toString().trim())) {
					Mobile.setError("Please Enter Correct Mobile !");
				} else {
					new CreateUser().execute();

				}

			}
		});

		AlreadyHaveAccount_ButtonController = (Button) findViewById(R.id.AlreadyHaveAccount);

		AlreadyHaveAccount_ButtonController
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(SignUpActivity.this,
								LoginActivity.class);
						finish();
						startActivity(myIntent);
					}
				});

		DOB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// On button click show datepicker dialog
				Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);
				System.out.println("the selected " + mDay);
				DatePickerDialog dialog = new DatePickerDialog(
						SignUpActivity.this, new mDateSetListener(), mYear,
						mMonth, mDay);
				whoCalls = 0;
				dialog.show();
			}
		});
		Anniversary.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// On button click show datepicker dialog
				Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR);
				int mMonth = c.get(Calendar.MONTH);
				int mDay = c.get(Calendar.DAY_OF_MONTH);
				System.out.println("the selected " + mDay);
				DatePickerDialog dialog = new DatePickerDialog(
						SignUpActivity.this, new mDateSetListener(), mYear,
						mMonth, mDay);
				whoCalls = 1;
				dialog.show();
			}
		});

	}

	class mDateSetListener implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			// getCalender();
			int mYear = year;
			int mMonth = monthOfYear;
			int mDay = dayOfMonth;
			if (whoCalls == 0) {
				DOB.setText(new StringBuilder()
						// Month is 0 based so add 1
						.append(mYear).append("-").append(mMonth + 1)
						.append("-").append(mDay).append(" "));
				DOB.setError(null);

			} else if (whoCalls == 1) {
				Anniversary.setText(new StringBuilder()
						// Month is 0 based so add 1
						.append(mYear).append("-").append(mMonth + 1)
						.append("-").append(mDay).append(" "));
			}

		}
	}

	class CreateUser extends AsyncTask<String, String, String> {

		// Progress Dialog
		private ProgressDialog pDialog;
		int success;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SignUpActivity.this);
			pDialog.setMessage("Creating User...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag

			String Username_Container = Username.getText().toString().trim();
			String Password_Container = Password.getText().toString().trim();
			String Name_Container = Name.getText().toString().trim();
			String Mobile_Container = Mobile.getText().toString().trim();
			String Email_Container = Email.getText().toString().trim();
			String Address_Container = Address.getText().toString().trim();

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Username",
						Username_Container));
				params.add(new BasicNameValuePair("Password",
						Password_Container));
				params.add(new BasicNameValuePair("Name", Name_Container));
				params.add(new BasicNameValuePair("Mobile", Mobile_Container));
				params.add(new BasicNameValuePair("Email", Email_Container));
				params.add(new BasicNameValuePair("Address", Address_Container));
				params.add(new BasicNameValuePair("IsUser_validate", "0"));
				params.add(new BasicNameValuePair("DOB", DOB.getText()
						.toString().trim()));
				params.add(new BasicNameValuePair("Anniversary", Anniversary
						.getText().toString().trim()));
				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(Util.Sign_Up_URL,
						"POST", params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {

					return json.getString(TAG_MESSAGE) + " , Please Login !";
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
				Toast.makeText(SignUpActivity.this, file_url, Toast.LENGTH_LONG)
						.show();
				if (success == 1) {
					Intent myIntent = new Intent(SignUpActivity.this,
							LoginActivity.class);
					finish();
					startActivity(myIntent);
				}
			}

		}

	}

}
