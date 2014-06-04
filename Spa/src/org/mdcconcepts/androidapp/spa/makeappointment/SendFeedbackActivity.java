package org.mdcconcepts.androidapp.spa.makeappointment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.mdcconcepts.androidapp.spa.R;
import org.mdcconcepts.androidapp.spa.serverhandler.JSONParser;
import org.mdcconcepts.androidapp.spa.util.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendFeedbackActivity extends Activity {

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// ids
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	String Therapist_Id;
	String Therapist_name;
	TextView textViewController_Therapist;
	Button buttonController_Feddback;
	EditText editTextController_Feddback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_send_feedback);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();

		Therapist_Id = bundle.getString("Therapist_Id");

		Therapist_name = bundle.getString("Therapist_name");

		textViewController_Therapist = (TextView) findViewById(R.id.textViewController_Therapist);

		editTextController_Feddback = (EditText) findViewById(R.id.editTextController_Feddback);

		textViewController_Therapist.setText(Therapist_name);

		buttonController_Feddback = (Button) findViewById(R.id.buttonController_Feddback);

		buttonController_Feddback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editTextController_Feddback.getText().toString().trim()
						.isEmpty()) {
					editTextController_Feddback
							.setError("Please Enter Feedback !");
				} else {
					new SendFeedback().execute();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_feedback, menu);
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

	class SendFeedback extends AsyncTask<String, String, String> {

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
			pDialog = new ProgressDialog(SendFeedbackActivity.this);
			pDialog.setMessage("Sending Feedback ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Uid", "" + Util.Uid));
				params.add(new BasicNameValuePair("Therapist_Id", Therapist_Id));
				params.add(new BasicNameValuePair("Feedback",
						editTextController_Feddback.getText().toString().trim()));
				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(
						Util.send_feedback, "POST", params);

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
				Toast.makeText(SendFeedbackActivity.this, file_url,
						Toast.LENGTH_LONG).show();
				if (success == 1) {
					finish();
				}
			}

		}

	}
}
