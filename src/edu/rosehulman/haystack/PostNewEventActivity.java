package edu.rosehulman.haystack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
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
	private boolean mCorrectAddress;
	private Double mLat;
	private Double mLon;
	private static GregorianCalendar fromCal;
	private static GregorianCalendar toCal;
	private boolean clickedPost;
	private static final long COMMENT_SIZE = 0;
	public final static String BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_new_event);
		mCorrectAddress = false;
		clickedPost = false;

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

		address.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					(new CheckAddressTask()).execute(address.getEditableText()
							.toString());
				}
			}
		});

		postButton = (Button) findViewById(R.id.post_button);
		postButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clickedPost = true;
				(new CheckAddressTask()).execute(address.getEditableText()
						.toString());
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

	public void onFinishGeocoding() {
		clickedPost = false;
		if (!mCorrectAddress) {
			Toast.makeText(
					PostNewEventActivity.this,
					"Could not geocode address, please make sure the address is entered correctly.",
					Toast.LENGTH_LONG).show();
			return;
		} else if (fromCal == null || toCal == null) {
			Toast.makeText(PostNewEventActivity.this,
					"You must have a start and end time for the event.",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (fromCal.getTimeInMillis() >= toCal.getTimeInMillis()) {
			Toast.makeText(
					PostNewEventActivity.this,
					"The event ending time cannot be set before the starting time.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// Auto-generated method stub
		DbEvent dbevent = new DbEvent();
		dbevent.setAddress(address.getText().toString());
		dbevent.setCategory(mCategorySpinner.getSelectedItem().toString());
		dbevent.setDescription(description.getText().toString());
		dbevent.setTitle(title.getText().toString());
		dbevent.setCommentSize(COMMENT_SIZE);
		dbevent.setLat(mLat);
		dbevent.setLon(mLon);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dbevent.setFromDateTime(sdf.format(fromCal.getTime()));
		dbevent.setToDateTime(sdf.format(toCal.getTime()));

		(new InsertEventTask()).execute(dbevent);
		// need update
		finish();
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
			mButton.setText((month + 1) + "/" + day + "/" + year);
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

		}

	}

	class CheckAddressTask extends AsyncTask<String, Void, String> {

		private ProgressDialog myProgressDialog;

		@Override
		protected void onPreExecute() {
			myProgressDialog = new ProgressDialog(PostNewEventActivity.this);
			myProgressDialog.setMessage("Checking Address...");
			if (clickedPost) {
				myProgressDialog.show();
			}
			// set up a progress dialog notifying the user the task is running
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				HttpGet getMethod = new HttpGet(PostNewEventActivity.BASE_URL
						+ "?address=" + params[0].replace(' ', '+'));

				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(getMethod);
				int res = httpResponse.getStatusLine().getStatusCode();
				if (res == 200) {
					StringBuilder builder = new StringBuilder();
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(httpResponse.getEntity()
									.getContent()));
					for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
							.readLine()) {
						builder.append(s);
					}
					String resultString = builder.toString();
					return resultString;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			// Hide progress dialog.
			super.onPostExecute(result);
			JSONObject geolocation = null;
			try {
				geolocation = new JSONObject(result);
				parseGeolocationReturn(geolocation);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			myProgressDialog.dismiss();
			if (clickedPost) {
				onFinishGeocoding();
			}
		}

	}

	public void parseGeolocationReturn(JSONObject geoJSON) throws JSONException {
		if (geoJSON == null || !geoJSON.getString("status").equals("OK")) {
			return;
		}

		mCorrectAddress = true;
		JSONObject result = geoJSON.getJSONArray("results").getJSONObject(0);
		String formattedAddress = result.getString("formatted_address");
		address.setText(formattedAddress);
		JSONObject location = result.getJSONObject("geometry").getJSONObject(
				"location");
		mLat = location.getDouble("lat");
		mLon = location.getDouble("lng");
	}
}
