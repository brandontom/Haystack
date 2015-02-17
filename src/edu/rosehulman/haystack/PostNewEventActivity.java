package edu.rosehulman.haystack;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
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

import com.appspot.tombn_songm_haystack.haystack.model.DbEvent;

public class PostNewEventActivity extends Activity {

	private EditText title;
	private static Button fromTimePicker;
	private static Button fromDatePicker;
	private static Button toTimePicker;
	private static Button toDatePicker;
	private EditText address;
	private EditText description;
	private Button postButton;
	private Button cancelButton;
	private Spinner mCategorySpinner;
	private static GregorianCalendar fromCal;
	private static GregorianCalendar toCal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_new_event);

		fromCal = new GregorianCalendar();
		toCal = new GregorianCalendar();
		toCal.add(Calendar.HOUR_OF_DAY, 1);

		title = (EditText) findViewById(R.id.edit_title);

		fromTimePicker = (Button) findViewById(R.id.from_time_button);
		fromTimePicker
				.setText(Event.convertTime(fromCal.get(Calendar.HOUR_OF_DAY),
						fromCal.get(Calendar.MINUTE)));
		fromTimePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog(fromTimePicker, fromCal);
			}

		});
		// Calendar cal = Calendar.getInstance();
		// cal.getTime();
		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		//
		//
		// fromTimePicker.setText(sdf.format(cal.getTime()));

		fromDatePicker = (Button) findViewById(R.id.from_date_button);
		fromDatePicker
				.setText(1 + fromCal.get(Calendar.MONTH) + "/"
						+ fromCal.get(Calendar.DATE) + "/"
						+ fromCal.get(Calendar.YEAR));
		fromDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(fromDatePicker, fromCal);
			}

		});

		toTimePicker = (Button) findViewById(R.id.to_time_button);
		toTimePicker.setText(Event.convertTime(toCal.get(Calendar.HOUR_OF_DAY),
				toCal.get(Calendar.MINUTE)));
		toTimePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog(toTimePicker, toCal);
			}

		});

		toDatePicker = (Button) findViewById(R.id.to_date_button);
		toDatePicker.setText(1 + toCal.get(Calendar.MONTH) + "/"
				+ toCal.get(Calendar.DATE) + "/" + toCal.get(Calendar.YEAR));
		toDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(toDatePicker, toCal);
			}

		});

		address = (EditText) findViewById(R.id.edit_address);
		description = (EditText) findViewById(R.id.edit_description);

		postButton = (Button) findViewById(R.id.post_button);
		postButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fromCal.getTimeInMillis() >= toCal.getTimeInMillis()) {
					Toast.makeText(
							PostNewEventActivity.this,
							"End time must be set for after the start of the event.",
							Toast.LENGTH_SHORT).show();
					return;
				}
				// Auto-generated method stub
				DbEvent dbevent = new DbEvent();
				dbevent.setAddress(address.getText().toString());
				dbevent.setCategory(mCategorySpinner.getSelectedItem()
						.toString());

				dbevent.setDescription(description.getText().toString());
				dbevent.setTitle(title.getText().toString());

				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss.SSS");
				Log.d("MFDSKFL", "" + fromCal.getTime());
				dbevent.setFromDateTime(sdf.format(fromCal.getTime()));
				dbevent.setToDateTime(sdf.format(toCal.getTime()));

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

		ArrayAdapter<CharSequence> arraySpinnerAdapter = ArrayAdapter
				.createFromResource(this, R.array.category_spinner_array,
						android.R.layout.simple_spinner_item);

		arraySpinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mCategorySpinner.setAdapter(arraySpinnerAdapter);

	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		private Button mButton;
		private GregorianCalendar mCal;

		public TimePickerFragment(Button button, GregorianCalendar cal) {
			mCal = cal;
			mButton = button;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker

			int hour = mCal.get(Calendar.HOUR_OF_DAY);
			int minute = mCal.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		@Override
		public void onTimeSet(TimePicker view, final int hourOfDay,
				final int minute) {
			// Do something with the time chosen by the user
			mCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
			mCal.set(Calendar.MINUTE, minute);
			mButton.setText(Event.convertTime(hourOfDay, minute));

		}
	}

	private void showTimePickerDialog(Button button, GregorianCalendar cal) {
		DialogFragment df = new TimePickerFragment(button, cal);

		df.show(getFragmentManager(), "");

	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		private Button mButton;
		private GregorianCalendar mCal;

		public DatePickerFragment(Button button, GregorianCalendar cal) {
			mButton = button;
			mCal = cal;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = mCal.get(Calendar.YEAR);
			int month = mCal.get(Calendar.MONTH);
			int day = mCal.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user

			mCal.set(Calendar.YEAR, year);
			mCal.set(Calendar.MONTH, month);
			mCal.set(Calendar.DATE, day);
			mButton.setText((month+1) + "/" + day + "/" + year);
		}
	}

	private void showDatePickerDialog(Button button, GregorianCalendar cal) {
		// Auto-generated method stub
		DialogFragment df = new DatePickerFragment(button, cal);
		df.show(getFragmentManager(), "");
	}

	public static class InsertEventTask extends
			AsyncTask<DbEvent, Void, DbEvent> {

		@Override
		protected DbEvent doInBackground(DbEvent... params) {
			// Auto-generated method stub
			DbEvent event = null;
			try {
				event = MainActivity.mService.dbevent().insert(params[0])
						.execute();
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
			MainActivity.addDbEvent(result);
		}

	}
}
