/* Copyright (c) 2009, Nathan Freitas, Orbot / The Guardian Project - http://openideals.com/guardian */
/* See LICENSE for licensing information */

package info.guardianproject.mrapp;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;


public class SimplePreferences extends SherlockPreferenceActivity  {

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.simpleprefs);
		
		setResult(RESULT_OK);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	}
}
