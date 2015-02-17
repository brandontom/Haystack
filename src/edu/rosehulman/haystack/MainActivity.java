package edu.rosehulman.haystack;

import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.appspot.tombn_songm_haystack.haystack.Haystack;
import com.appspot.tombn_songm_haystack.haystack.Haystack.Dbevent.List;
import com.appspot.tombn_songm_haystack.haystack.model.DbEvent;
import com.appspot.tombn_songm_haystack.haystack.model.DbEventCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;

public class MainActivity extends Activity implements SideSwipeFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private SideSwipeFragment mNavigationDrawerFragment;

	public static final String KEY_EVENT_ID = "KEY_EVENT_ID";

	public static final String MQ = "MQ";

	public static final String HS = "HS";

	public static ArrayList<Event> mEvents = new ArrayList<Event>();

	private ListView mListView;
	private Spinner mSortSpinner;
	private Spinner mTimeSpinner;
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	public static Haystack mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mService = new Haystack(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);

		mNavigationDrawerFragment = (SideSwipeFragment) getFragmentManager().findFragmentById(
				R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		mListView = (ListView) findViewById(R.id.main_activity_list_view);

		mSortSpinner = (Spinner) findViewById(R.id.sort_spinner);
		mTimeSpinner = (Spinner) findViewById(R.id.time_spinner);

		setUpSpinners();
		updateEvents();
	}

	private void setUpSpinners() {
		ArrayAdapter<CharSequence> arraySpinnerAdapter = ArrayAdapter.createFromResource(this,
				R.array.sort_spinner_array, android.R.layout.simple_spinner_item);

		arraySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mSortSpinner.setAdapter(arraySpinnerAdapter);

		ArrayAdapter<CharSequence> timeSpinnerAdapter = ArrayAdapter.createFromResource(this,
				R.array.time_spinner_array, android.R.layout.simple_spinner_item);

		timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mTimeSpinner.setAdapter(timeSpinnerAdapter);

	}

	private void setUpListView(java.util.List<DbEvent> list) {
		mEvents = new ArrayList<Event>();
		for (DbEvent event : list) {
			addDbEvent(event);
			// TODO: parse dates from database
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
			// sdf.parse(string)
		}

		final RowViewAdapter adapter = new RowViewAdapter(this, mEvents);

		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent eventIntent = new Intent(MainActivity.this, EventActivity.class);
				eventIntent.putExtra(KEY_EVENT_ID, position);
				startActivity(eventIntent);
			}
		});
	}

	public static void addDbEvent(DbEvent event) {
		Event temp = new Event(event.getTitle(), event.getAddress(), event.getToDateTime(),
				event.getFromDateTime(), event.getEntityKey(), event.getDescription(),
				event.getLastTouchDateTime(), event.getComments());
		mEvents.add(temp);
	}

	private void updateEvents() {
		(new QueryForEventsTask()).execute();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		String[] categories = getResources().getStringArray(R.array.category_spinner_array);
		if (number == 1) {
			mTitle = getString(R.string.all);
		} else {
			mTitle = categories[number - 2];
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

	class QueryForEventsTask extends AsyncTask<Void, Void, DbEventCollection> {
		private ProgressDialog myProgressDialog;

		@Override
		protected void onPreExecute() {// set up a progress dialog notifying the
										// user the task is running
			myProgressDialog = new ProgressDialog(MainActivity.this);
			myProgressDialog.setMessage("Getting Events...");
			myProgressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected DbEventCollection doInBackground(Void... params) {
			DbEventCollection quotes = null;
			try {
				List query = mService.dbevent().list();
				query.setOrder("-last_touch_date_time");
				query.setLimit(50L);
				quotes = query.execute();
			} catch (IOException e) {
				Log.e(HS, "Failed loading " + e);
			}
			return quotes;
		}

		@Override
		protected void onPostExecute(DbEventCollection result) {
			super.onPostExecute(result);

			if (result == null || result.getItems() == null) {
				Log.e(HS, "Result is null. Failed loading.");
				return;
			}
			// result.getItems() could be null
			setUpListView(result.getItems());
			myProgressDialog.dismiss();
		}

	}

}
