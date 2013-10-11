package info.guardianproject.mrapp;

import info.guardianproject.mrapp.server.LoginActivity;

import java.util.Date;

import org.holoeverywhere.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class NavigationDrawerFragment extends Fragment {
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_drawer, container, false);
        
        initNavigationDrawerListeners(rootView);
        
        return rootView;
	}
	
	private void initNavigationDrawerListeners(View view)
	{	
		final Activity activity = (BaseActivity) getActivity();
		
        ImageButton btnDrawerQuickCaptureVideo = (ImageButton) findViewById(R.id.btnDrawerQuickCaptureVideo);
        ImageButton btnDrawerQuickCapturePhoto = (ImageButton) findViewById(R.id.btnDrawerQuickCapturePhoto);
        ImageButton btnDrawerQuickCaptureAudio = (ImageButton) findViewById(R.id.btnDrawerQuickCaptureAudio);
        
        Button btnDrawerHome = (Button) findViewById(R.id.btnDrawerHome);
        Button btnDrawerProjects = (Button) findViewById(R.id.btnDrawerProjects);
        Button btnDrawerLessons = (Button) findViewById(R.id.btnDrawerLessons);
        Button btnDrawerAccount = (Button) findViewById(R.id.btnDrawerAccount);
        Button btnDrawerSettings = (Button) findViewById(R.id.btnDrawerSettings);
        

       
        btnDrawerQuickCaptureVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	String dateNowStr = new Date().toLocaleString();
                
            	Intent intent = new Intent(activity, StoryNewActivity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	intent.putExtra("story_name", "Quick Story " + dateNowStr);
            	intent.putExtra("story_type", 0);
            	intent.putExtra("auto_capture", true);
                
                 activity.startActivity(intent);           
             }
        });
        
        btnDrawerQuickCapturePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	String dateNowStr = new Date().toLocaleString();
                
            	Intent intent = new Intent(activity, StoryNewActivity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	intent.putExtra("story_name", "Quick Story " + dateNowStr);
            	intent.putExtra("story_type", 2);
            	intent.putExtra("auto_capture", true);
                
                activity.startActivity(intent);           
             }
        });
        
        btnDrawerQuickCaptureAudio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	String dateNowStr = new Date().toLocaleString();
                
            	Intent intent = new Intent(activity, StoryNewActivity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            	intent.putExtra("story_name", "Quick Story " + dateNowStr);
            	intent.putExtra("story_type", 1);
            	intent.putExtra("auto_capture", true);
                
                activity.startActivity(intent);           
             }
        });
        
        btnDrawerHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {           	
				Intent i = new Intent(activity, HomeActivity.class);
				activity.startActivity(i);
            }
        });
        btnDrawerProjects.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {         	
            	Intent i = new Intent(activity, ProjectsActivity.class);
            	activity.startActivity(i);
            }
        });
        btnDrawerLessons.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, LessonsActivity.class);
                activity.startActivity(i);
            }
        });
        
        btnDrawerAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {      	
               Intent i = new Intent(activity, LoginActivity.class);
               activity.startActivity(i);
            }
        });
        
        btnDrawerSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(activity, SimplePreferences.class);
               activity.startActivity(i);
            }
        });
	}

}
