package edu.rosehulman.haystack;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appspot.tombn_songm_haystack.haystack.Haystack;
import com.appspot.tombn_songm_haystack.haystack.model.DbEvent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

public class PostNewEventActivity extends Activity {

	private EditText title;
	private Button fromTimePicker;
	private Button fromDatePicker;
	private Button toTimePicker;
	private Button toDatePicker;
	private EditText address;
	private EditText description;
	private Button postButton;
	private Button cancelButton;
	private Spinner mCategorySpinner;
	private Haystack mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_new_event);

		title = (EditText) findViewById(R.id.edit_title);

		fromTimePicker = (Button) findViewById(R.id.from_time_button);
		fromTimePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog();
			}

		});
		// Calendar cal = Calendar.getInstance();
		// cal.getTime();
		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		//
		//
		// fromTimePicker.setText(sdf.format(cal.getTime()));

		fromDatePicker = (Button) findViewById(R.id.from_date_button);
		fromDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}

		});

		toTimePicker = (Button) findViewById(R.id.to_time_button);
		toTimePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog();
			}

		});

		toDatePicker = (Button) findViewById(R.id.to_date_button);
		toDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}

		});

		address = (EditText) findViewById(R.id.edit_address);
		description = (EditText) findViewById(R.id.edit_description);

		postButton = (Button) findViewById(R.id.post_button);
		postButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DbEvent dbevent = new DbEvent();
				dbevent.setAddress(address.getText().toString());
				// dbevent.getCategory()
				dbevent.setDescription(description.getText().toString());
				dbevent.setTitle(title.getText().toString());
				// dbevent.setFromDateTime(fromDateTime)
				// dbevent.setToDateTime(toDateTime)

				new InsertEventTask().execute(dbevent);
				// need update
				finish();
			}
		});

		cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mCategorySpinner = (Spinner) findViewById(R.id.post_category_spinner);

		ArrayAdapter<CharSequence> arraySpinnerAdapter = ArrayAdapter.createFromResource(this,
				R.array.category_spinner_array, android.R.layout.simple_spinner_item);

		arraySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mCategorySpinner.setAdapter(arraySpinnerAdapter);

		mService = new Haystack(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);

	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, final int hourOfDay, final int minute) {
			// Do something with the time chosen by the user

			Context context = super.getActivity();
			CharSequence text = "Current time is " + hourOfDay + ":" + minute;
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	private void showTimePickerDialog() {
		DialogFragment df = new TimePickerFragment();

		df.show(getFragmentManager(), "");

	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			Context context = super.getActivity();
			DateFormatSymbols date = new DateFormatSymbols();
			CharSequence text = "Current date is " + date.getMonths()[month] + " " + day + ", "
					+ year;
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	private void showDatePickerDialog() {
		// Auto-generated method stub
		DialogFragment df = new DatePickerFragment();
		df.show(getFragmentManager(), "");
	}

	class InsertEventTask extends AsyncTask<DbEvent, Void, DbEvent> {

		@Override
		protected DbEvent doInBackground(DbEvent... params) {
			// Auto-generated method stub
			DbEvent event = null;
			try {
				event = mService.dbevent().insert(params[0]).execute();
			} catch (IOException e) {

			}
			return event;
		}

		@Override
		protected void onPostExecute(DbEvent result) {
			super.onPostExecute(result);
			if (result == null) {
				Log.e(MainActivity.HS, "Failed inserting");
				return;
			}
		}

	}
}
