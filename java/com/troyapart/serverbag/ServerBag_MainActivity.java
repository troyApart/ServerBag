package com.troyApart.serverbag;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ServerBag_MainActivity extends Activity {

	private ServerBag_Database_Adapter sbDbAdapter;
	private String[] mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerItems = getResources().getStringArray(R.array.drawer_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        if (savedInstanceState == null) {
            selectItem(0);
        }
//		
//		mDrawerItems = new String[] { "Home", "Sales Report" };
//		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//		mDrawerList = (ListView) findViewById(R.id.left_drawer);
//
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActionBar()
//				.getThemedContext(), android.R.layout.simple_list_item_1,
//				mDrawerItems);
//
//		mDrawerList.setAdapter(adapter);
		// mDrawerList.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position,
		// long id) {
		// // TODO Auto-generated method stub
		// // selectItem(position);
		// }
		// });
//		final String[] fragments = {
//				"com.troyApart.serverbag.ServerBag_HomeFragment",
//				"com.troyApart.serverbag.ServerBag_SalesReportFragment" };
//		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					final int pos, long id) {
//				mDrawerLayout
//						.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//							@Override
//							public void onDrawerClosed(View drawerView) {
//								super.onDrawerClosed(drawerView);
//								FragmentTransaction fragTran = getSupportFragmentManager()
//										.beginTransaction();
//
//								fragTran.replace(R.id.content_frame, Fragment
//										.instantiate(getBaseContext(),
//												fragments[pos]));
//								fragTran.commit();
//							}
//						});
//			}
//		});

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		 mDrawerToggle = new ActionBarDrawerToggle(
		 this, /* host Activity */
		 mDrawerLayout, /* DrawerLayout object */
		 R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		 R.string.drawer_open, /* "open drawer" description */
		 R.string.drawer_close /* "close drawer" description */
		 ) {
		
		 /** Called when a drawer has settled in a completely closed state. */
		 public void onDrawerClosed(View view) {
		 super.onDrawerClosed(view);
		 getActionBar().setTitle(mTitle);
		
		 }
		
		 /** Called when a drawer has settled in a completely open state. */
		 public void onDrawerOpened(View drawerView) {
		 super.onDrawerOpened(drawerView);
		 getActionBar().setTitle("Menu");
		 }
		 };
		
		 // Set the drawer toggle as the DrawerListener
		 mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		 getActionBar().setDisplayHomeAsUpEnabled(true);
		 getActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.action_settings);
		item.setVisible(false);
		MenuItem item2 = menu.findItem(R.id.data_clear);
		item2.setVisible(false);
		MenuItem item3 = menu.findItem(R.id.email_report);
		item3.setVisible(false);
		return true;
	}

	 @Override
	 protected void onPostCreate(Bundle savedInstanceState) {
	 super.onPostCreate(savedInstanceState);
	 // Sync the toggle state after onRestoreInstanceState has occurred.
	 mDrawerToggle.syncState();
	 }
	
	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
	 super.onConfigurationChanged(newConfig);
	 mDrawerToggle.onConfigurationChanged(newConfig);
	 }
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	 // Pass the event to ActionBarDrawerToggle, if it returns
	 // true, then it has handled the app icon touch event
	 if (mDrawerToggle.onOptionsItemSelected(item)) {
	 return true;
	 }
	 // Handle your other action bar items...
	
	 return super.onOptionsItemSelected(item);
	 }

	// public void selectItem(int position) {
	// // switch(position){
	// // case 0:
	// // break;
	// // case 1:
	// // break;
	// // }
	// FragmentActivity fragment = new ServerBag_HomeActivity();
	//
	// // Insert the fragment by replacing any existing fragment
	// FragmentManager fragmentManager = getFragmentManager();
	// fragmentManager.beginTransaction()
	// .replace(R.id.content_frame, fragment)
	// .commit();
	//
	// // Highlight the selected item, update the title, and close the drawer
	// mDrawerList.setItemChecked(position, true);
	// setTitle(mDrawerItems[position]);
	// mDrawerLayout.closeDrawer(mDrawerList);
	// }
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		switch (position) {
			case 1:
	    	Fragment fragment1 = new ServerBag_ReportFragment();
		    Bundle args1 = new Bundle();
		    args1.putInt(ServerBag_ReportFragment.ARG_LIST_NUMBER, position);
		    fragment1.setArguments(args1);
		    
		    // Insert the fragment by replacing any existing fragment
		    FragmentManager fragmentManager1 = getFragmentManager();
		    fragmentManager1.beginTransaction()
		                   .replace(R.id.content_frame, fragment1)
		                   .commit();	
		    break;
//			case 2:
//				Fragment fragment2 = new ServerBag_ReportFragment();
//			    Bundle args2 = new Bundle();
//			    args2.putInt(ServerBag_ReportFragment.ARG_LIST_NUMBER, position);
//			    fragment2.setArguments(args2);
//			    
//			    // Insert the fragment by replacing any existing fragment
//			    FragmentManager fragmentManager2 = getFragmentManager();
//			    fragmentManager2.beginTransaction()
//			                   .replace(R.id.content_frame, fragment2)
//			                   .commit();	
//			    break;
			    default:
			    	Fragment fragment = new ServerBag_HomeFragment();
				    Bundle args = new Bundle();
				    args.putInt(ServerBag_HomeFragment.ARG_LIST_NUMBER, position);
				    fragment.setArguments(args);
				    
				    FragmentManager fragmentManager = getFragmentManager();
				    fragmentManager.beginTransaction()
				                   .replace(R.id.content_frame, fragment)
				                   .commit();
			    	break;
	    }
	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(mDrawerItems[position]);
	    mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    getActionBar().setTitle(mTitle);
	}
}
