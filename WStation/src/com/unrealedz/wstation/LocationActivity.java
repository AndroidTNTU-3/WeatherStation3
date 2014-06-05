package com.unrealedz.wstation;

import com.unrealedz.wstation.bd.DaoCityDb;
import com.unrealedz.wstation.bd.DbHelper;
import com.unrealedz.wstation.entity.ForecastDayShort;

import android.R.drawable;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class LocationActivity extends Activity {
	
	ListView listView;
	SharedPreferences preferences;
	Cursor cursor;
	DaoCityDb cityDb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		listView = (ListView) findViewById(R.id.listViewLocation);
		listView.setOnItemClickListener( new ListListener());
		/*Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        String query = intent.getStringExtra(SearchManager.QUERY);
	        // At the end of doMySearch(), you can populate 
	        // a String Array as resultStringArray[] and set the Adapter
	        doMySearch(query);
	    }*/

	}
	


	private void doMySearch(String query) {
		cityDb = new DaoCityDb(this);
		cursor = cityDb.getLocation(query);

		String[] from = new String[] 
				{DbHelper.CityDb.COLUMN_NAME_NAME, DbHelper.CityDb.COLUMN_NAME_REGION, DbHelper.CityDb.COLUMN_NAME_COUNTRY};
		int [] to = new int[]
				{R.id.tvCityRowLocation, R.id.tvRegionRowLocation, R.id.tvCountryRowLocation};
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row_location, cursor, from, to);
		
		listView.setAdapter(adapter);
		adapter.swapCursor(cursor);
	}
	
	private class ListListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			cursor.moveToPosition(position);
			String cid = cursor.getString(cursor.getColumnIndex(DbHelper.CityDb.COLUMN_NAME_ID));
			Log.i("DEBUG", "city ID:" + cid);
			preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		    Editor ed = preferences.edit();
		    ed.putString("cityId", cid);
		    ed.commit();
    
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		
		 //Create the search view

	    final SearchView searchView = new SearchView(getActionBar().getThemedContext());
	    searchView.setQueryHint("Search location");
	    
	    menu.add(Menu.NONE,0,1,"Search")
        .setIcon(android.R.drawable.ic_menu_search)
        .setActionView(searchView)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	    
		final MenuItem searchMenuItem = menu.getItem(0);
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.length() > 0) {

				} else {
					// Do something when there's no input
				}
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String query) {

				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

				setProgressBarIndeterminateVisibility(true);

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						setProgressBarIndeterminateVisibility(false);
					}
				}, 2000);
				
				doMySearch(query);
				
		        if (searchMenuItem != null) {
		           searchMenuItem.collapseActionView();		//Collapse searchMenu after submit
		        }
			
				return false;
			}
		});

		return true;
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
	
	@Override
	  protected void onStop() {
	    super.onStop();
	    cityDb.closeDb();	
	  }

}
