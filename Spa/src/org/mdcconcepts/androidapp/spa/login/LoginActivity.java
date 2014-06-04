package org.mdcconcepts.androidapp.spa.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.mdcconcepts.androidapp.spa.MainActivity;
import org.mdcconcepts.androidapp.spa.R;
import org.mdcconcepts.androidapp.spa.customitems.ConnectionDetector;
import org.mdcconcepts.androidapp.spa.serverhandler.JSONParser;
import org.mdcconcepts.androidapp.spa.signup.SignUpActivity;
import org.mdcconcepts.androidapp.spa.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// ids
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	EditText Username;
	EditText Password;

	Button Login_ButtonController;
	Button GoToCreateAccount_ButtonController;

	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	int pallavi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());

		Username = (EditText) findViewById(R.id.Username_Login);
		Password = (EditText) findViewById(R.id.Password_Login);

		Login_ButtonController = (Button) findViewById(R.id.Login);

		Login_ButtonController.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (Username.getText().toString().trim().isEmpty()) {
					Username.setError("Please Enter Username !");
				} else if (Password.getText().toString().trim().isEmpty()) {
					Password.setError("Please Enter Password !");
				} else {

					isInternetPresent = cd.isConnectingToInternet();

					// check for Internet status
					if (isInternetPresent) {
						// Internet Connection is Present
						// make HTTP requests

						new LoginUser().execute();
						// showAlertDialog(
						// AndroidDetectInternetConnectionActivity.this,
						// "Internet Connection",
						// "You have internet connection", true);
					} else {
						// Internet connection is not present
						// Ask user to connect to Internet
						showAlertDialog(LoginActivity.this,
								"No Internet Connection",
								"You don't have internet connection.", false);
					}

				}

			}
		});

		GoToCreateAccount_ButtonController = (Button) findViewById(R.id.GoToRegister);

		GoToCreateAccount_ButtonController
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent myIntent = new Intent(LoginActivity.this,
								SignUpActivity.class);
						finish();
						startActivity(myIntent);

					}
				});
	}

	class LoginUser extends AsyncTask<String, String, String> {

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
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage("Attempting Login ... ");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag

			String Username_Container = Username.getText().toString();
			String Password_Container = Password.getText().toString();

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Username",
						Username_Container));
				params.add(new BasicNameValuePair("Password",
						Password_Container));

				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(Util.Login_URL,
						"POST", params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				Util.Uid = json.getInt("Uid");
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
		 **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG)
						.show();
				if (success == 1) {
					Intent myIntent = new Intent(LoginActivity.this,
							MainActivity.class);
					finish();
					startActivity(myIntent);
				}
			}

		}

	}

	/**
	 * 
	 * Function to display simple Alert Dialog
	 * 
	 * @param context
	 *            - application context
	 * @param title
	 *            - alert dialog title
	 * @param message
	 *            - alert message
	 * @param status
	 *            - success/failure (used to set icon)
	 */
	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting alert dialog icon
		alertDialog.setIcon((status) ? R.drawable.ic_launcher
				: R.drawable.ic_launcher);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
