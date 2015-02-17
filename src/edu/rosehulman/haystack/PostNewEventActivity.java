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
				showTimePickerDialog(fromTimePicker);
			}

		});
		// Calendar cal = Calendar.getInstance();
		// cal.getTime();
		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		//
		//
		// fromTimePicker.setText(sdf.format(cal.getTime()));

		fromDatePicker = (Button) findViewById(R.id.from_date_button);
		fromDatePicker.setText(1+fromCal.get(Calendar.MONTH) + "/"
				+ fromCal.get(Calendar.DATE) + "/"
				+ fromCal.get(Calendar.YEAR));
		fromDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(fromDatePicker);
			}

		});

		toTimePicker = (Button) findViewById(R.id.to_time_button);
		toTimePicker.setText(Event.convertTime(toCal.get(Calendar.HOUR_OF_DAY),
				toCal.get(Calendar.MINUTE)));
		toTimePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerDialog(toTimePicker);
			}

		});

		toDatePicker = (Button) findViewById(R.id.to_date_button);
		toDatePicker.setText(1+toCal.get(Calendar.MONTH) + "/"
				+ toCal.get(Calendar.DATE) + "/" + toCal.get(Calendar.YEAR));
		toDatePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerDialog(toDatePicker);
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
							"End time must be set for date after start of event."
									+ fromCal.getTime()
									+ toCal.getTimeInMillis(),
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

		public TimePickerFragment(Button button) {
			//
			mButton = button;
		}

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

		@Override
		public void onTimeSet(TimePicker view, final int hourOfDay,
				final int minute) {
			// Do something with the time chosen by the user

			if (mButton.getId() == R.id.from_time_button) {
				fromCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				fromCal.set(Calendar.MINUTE, minute);

			} else if (mButton.getId() == R.id.to_time_button) {
				toCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				toCal.set(Calendar.MINUTE, minute);

			} else {
				Log.d("MIN", "timepicker id is wrong ");
			}
			mButton.setText(Event.convertTime(hourOfDay, minute));

		}
	}

	private void showTimePickerDialog(Button button) {
		DialogFragment df = new TimePickerFragment(button);

		df.show(getFragmentManager(), "");

	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		private Button mButton;

		public DatePickerFragment(Button button) {
			//
			mButton = button;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH+1);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user

			if (mButton.getId() == R.id.from_date_button) {
				fromCal.set(Calendar.YEAR, year);
				fromCal.set(Calendar.MONTH, month);
				fromCal.set(Calendar.DATE, day);

			} else if (mButton.getId() == R.id.to_date_button) {
				toCal.set(Calendar.YEAR, year);
				toCal.set(Calendar.MONTH, month);
				toCal.set(Calendar.DATE, day);
			} else {
				Log.d("MIN", "datepicker id is wrong");
			}
			mButton.setText(month + "/" + day + "/" + year);
		}
	}

	private void showDatePickerDialog(Button button) {
		// Auto-generated method stub
		DialogFragment df = new DatePickerFragment(button);
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
