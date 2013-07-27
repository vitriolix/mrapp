package info.guardianproject.mrapp.server;

import info.guardianproject.mrapp.AppConstants;
import info.guardianproject.mrapp.StoryMakerApp;

//import java.io.File;
import info.guardianproject.iocipher.File;
import info.guardianproject.iocipher.FileInputStream;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import net.bican.wordpress.Comment;
import net.bican.wordpress.CustomField;
import net.bican.wordpress.MediaObject;
import net.bican.wordpress.Page;
import net.bican.wordpress.Wordpress;
import redstone.xmlrpc.XmlRpcArray;
import redstone.xmlrpc.XmlRpcClient;
import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcStruct;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ServerManager {

	private Wordpress mWordpress;	
	private String mServerUrl;
	private Context mContext;
	
	private final static String PATH_XMLRPC = "/xmlrpc.php";
	private final static String PATH_REGISTER = "/wp-login.php?action=register";
	private final static String PATH_LOGIN = "/wp-admin";
	
	public final static String CUSTOM_FIELD_MEDIUM = "medium"; //Text, Audio, Photo, Video
	
	public final static String CUSTOM_FIELD_MEDIUM_TEXT = "Text";
	public final static String CUSTOM_FIELD_MEDIUM_AUDIO = "Audio";
	public final static String CUSTOM_FIELD_MEDIUM_PHOTO = "Photo";
	public final static String CUSTOM_FIELD_MEDIUM_VIDEO = "Video";
	
	public final static String CUSTOM_FIELD_MEDIA_HOST = "media_value"; //youtube or soundcloud
	public final static String CUSTOM_FIELD_MEDIA_HOST_YOUTUBE = "youtube"; //youtube or soundcloud
	public final static String CUSTOM_FIELD_MEDIA_HOST_SOUNDCLOUD = "soundcloud"; //youtube or soundcloud

	private SharedPreferences mSettings;
	
	public ServerManager (Context context)
	{
		this(context, StoryMakerApp.initServerUrls(context));
		
	}
	
	public ServerManager (Context context, String serverUrl)
	{
		mContext = context;
		mServerUrl = serverUrl;
		
		mSettings = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
	       
	}
	
	public void setContext (Context context)
	{
		mContext = context;
	}
	
	//if the user hasn't registered with the user, show the login screen
    public boolean hasCreds ()
    {
       
        String user = mSettings.getString("user","");
        
        return (user != null && user.length() > 0);
        
    }
    
    private void connect () throws MalformedURLException, XmlRpcFault
    {
    	if (mWordpress == null)
    	{
           String user = mSettings.getString("user","");
           String pass = mSettings.getString("pass", "");
         
           if (user != null && user.length() > 0)
        	   connect (user, pass);
    	}
    }
	
	public void connect (String username, String password) throws MalformedURLException, XmlRpcFault
	{
		XmlRpcClient.setContext(mContext);
		

	    boolean useTor = mSettings.getBoolean("pusetor", false);
	    
		if (useTor)
		{
			XmlRpcClient.setProxy(true, "SOCKS", AppConstants.TOR_PROXY_HOST, AppConstants.TOR_PROXY_PORT);
		}
		else
		{
			XmlRpcClient.setProxy(false, null, null, -1);

		}
		
		Log.d(AppConstants.TAG,"Logging into Wordpress: " + username + '@' + mServerUrl + PATH_XMLRPC);
		mWordpress = new Wordpress(username, password, mServerUrl + PATH_XMLRPC);	
		
		mWordpress.getRecentPosts(1); //need to do a test to force authentication
	}
	
	public String getPostUrl (String postId) throws XmlRpcFault, MalformedURLException
	{
		connect();
		Page post = mWordpress.getPost(Integer.parseInt(postId));
		return post.getPermaLink();
		
	}
	
	public Page getPost (String postId) throws XmlRpcFault, MalformedURLException
	{
		connect();
		Page post = mWordpress.getPost(Integer.parseInt(postId));
		return post;
		
	}
	
	public List<Page> getRecentPosts (int num) throws XmlRpcFault, MalformedURLException
	{
		connect();
		List<Page> rPosts = mWordpress.getRecentPosts(num);
		return rPosts;
	}
	
	public List<Comment> getComments (Page page) throws XmlRpcFault, MalformedURLException
	{
		connect();
		return mWordpress.getComments(null, page.getPostid(), null, null);
	}

	public String post (String title, String body, String[] cats, String medium, String mediaService, String mediaGuid) throws XmlRpcFault, MalformedURLException
	{
		return post (title, body, cats, medium, mediaService, mediaGuid, null, null);
	}
	
	public String addMedia (String mimeType, File file) throws XmlRpcFault, MalformedURLException
	{
		connect();
		
		MediaObject mObj = null;
		if (file != null) {

			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				try {
					mObj = mWordpress.newMediaObject(mimeType, fis, file.getName(), file.length(), false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return mObj.getUrl();
	}
	
	public String post (String title, String body, String[] catstrings, String medium, String mediaService, String mediaGuid, String mimeType, File file) throws XmlRpcFault, MalformedURLException
	{
		connect();
		
		MediaObject mObj = null;
		
		if (file != null)
			mObj = mWordpress.newMediaObject(mimeType, file, false);
		
		Page page = new Page ();
		page.setTitle(title);
		
		StringBuffer sbBody = new StringBuffer();
		sbBody.append(body);
		
		if (mObj != null)
		{
			sbBody.append("\n\n");
			sbBody.append(mObj.getUrl());
		}
		
		page.setDescription(sbBody.toString());
		
		if (catstrings != null && catstrings.length > 0)
		{
			XmlRpcArray cats = new XmlRpcArray();
			for (String catstr : catstrings)
				cats.add(catstr);
			page.setCategories(cats);
		}
		
		XmlRpcArray custom_fields = new XmlRpcArray();

		
		if (medium != null)
		{

			XmlRpcStruct struct = new XmlRpcStruct();
			struct.put("key","medium");
			struct.put("value",medium);			
			custom_fields.add(struct);

		}

		if (mediaService != null)
		{
			
			
			XmlRpcStruct struct = new XmlRpcStruct();
			struct.put("key","media_value");
			struct.put("value",mediaService);
			custom_fields.add(struct);

		}
		
		if (mediaGuid != null)
		{
			
			XmlRpcStruct struct = new XmlRpcStruct();
			struct.put("key","media_guid");
			struct.put("value",mediaGuid);
			custom_fields.add(struct);

		}
		
		

		page.setCustom_fields(custom_fields);
		
		boolean publish = true; //let's push it out!
		String postId = mWordpress.newPost(page, publish);
		
		
		return postId;
	}
	
	public void createAccount (Activity activity)
	{
		//open web view here to reg form
		Intent intent = new Intent(mContext,WebViewActivity.class);
		intent.putExtra("title", "New Account");
		intent.putExtra("url", mServerUrl + PATH_REGISTER);
		
		activity.startActivity(intent);
	}
	
	public void showPost (String title, String url)
	{		
		Intent intent = new Intent(mContext,WebViewActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		
		mContext.startActivity(intent);
	}
	
}
