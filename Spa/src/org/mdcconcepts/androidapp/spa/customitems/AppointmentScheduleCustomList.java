package org.mdcconcepts.androidapp.spa.customitems;

import java.util.ArrayList;

import org.mdcconcepts.androidapp.spa.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AppointmentScheduleCustomList extends ArrayAdapter<String> {

	private final Activity context;
	private final ArrayList<String> ArrayList_AllServiceList;
	private final ArrayList<String> ArrayList_AllTimeList;

	public AppointmentScheduleCustomList(Activity context,
			ArrayList<String> ArrayList_AllServiceList,
			ArrayList<String> ArrayList_AllTimeList) {

		super(context, R.layout.appointment_schedule_single_list,
				ArrayList_AllServiceList);

		this.context = context;
		this.ArrayList_AllServiceList = ArrayList_AllServiceList;
		this.ArrayList_AllTimeList = ArrayList_AllTimeList;

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(
				R.layout.appointment_schedule_single_list, null, true);

		TextView txtTitle = (TextView) rowView
				.findViewById(R.id.TextviewController_Services);

		TextView TextView_TimeDate = (TextView) rowView
				.findViewById(R.id.TextviewController_TIme);

		txtTitle.setText(ArrayList_AllServiceList.get(position));
		TextView_TimeDate.setText(ArrayList_AllTimeList.get(position));

		return rowView;
	}
}