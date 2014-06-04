package org.mdcconcepts.androidapp.spa.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.mdcconcepts.androidapp.spa.R;
import org.mdcconcepts.androidapp.spa.serverhandler.JSONParser;
import org.mdcconcepts.androidapp.spa.util.Util;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

	private View rootView;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	// ids
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	TextView Name;
	TextView Mobile;
	TextView Email;
	TextView Address;
	Button Sign_Up_ButtonController;
	Button AlreadyHaveAccount_ButtonController;
	TextView DOB;
	TextView Anniversary;

	public ProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater
				.inflate(R.layout.fragment_profile, container, false);

		Name = (TextView) rootView.findViewById(R.id.Name);
		Mobile = (TextView) rootView.findViewById(R.id.Mobile);
		Email = (TextView) rootView.findViewById(R.id.Email);
		Address = (TextView) rootView.findViewById(R.id.Address);
		DOB = (TextView) rootView.findViewById(R.id.DOB);
		Anniversary = (TextView) rootView.findViewById(R.id.Anniversary);
		new GetUserData().execute();
		return rootView;
	}

	class GetUserData extends AsyncTask<String, String, String> {

		// Progress Dialog
		private ProgressDialog pDialog;

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		String Name1;
		String Mobile1;
		String Email1;
		String Address1;
		String DOB1;
		String Anniversary1;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(rootView.getContext());
			pDialog.setMessage("Getting User Details...");
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
				params.add(new BasicNameValuePair("Uid", "" + Util.Uid));
				Log.d("Uid", "" + Util.Uid);
				Log.d("request!", "starting");

				// Posting user data to script
				JSONObject json = jsonParser.makeHttpRequest(
						Util.GetUserDetails, "POST", params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {

					Name1 = json.getString("Name");
					Mobile1 = json.getString("Mobile");
					Email1 = json.getString("Email");
					Address1 = json.getString("Address");
					DOB1 = json.getString("DOB");
					Anniversary1 = json.getString("Anniversary");

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
				Toast.makeText(rootView.getContext(), file_url,
						Toast.LENGTH_LONG).show();
				Name.setText(Name1);
				Mobile.setText(Mobile1);
				Email.setText(Email1);
				Address.setText(Address1);
				DOB.setText(DOB1);
				Anniversary.setText(Anniversary1);
			}

		}

	}
}