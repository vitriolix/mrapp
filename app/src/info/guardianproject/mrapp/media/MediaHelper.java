package info.guardianproject.mrapp.media;

import info.guardianproject.mrapp.AppConstants;
import info.guardianproject.mrapp.AppConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

import org.ffmpeg.android.MediaDesc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class MediaHelper implements MediaScannerConnectionClient {

	private Activity mActivity;
	private Handler mHandler;
	private MediaScannerConnection mScanner;
	
	private File mMediaFileTmp;
	private Uri mMediaUriTmp;
	
	public MediaHelper (Activity activity, Handler handler)
	{
		mActivity = activity;
		mHandler = handler;
	}
	
	public File captureVideo (File fileExternDir)
	{
		 ContentValues values = new ContentValues();
         values.put(MediaStore.Images.Media.TITLE, MediaConstants.CAMCORDER_TMP_FILE);
         values.put(MediaStore.Images.Media.DESCRIPTION,MediaConstants.CAMCORDER_TMP_FILE);
         
         mActivity.sendBroadcast(new Intent().setAction(AppConstants.Keys.Service.LOCK_LOGS));
         mMediaFileTmp = new File(fileExternDir, new Date().getTime() + '-' + MediaConstants.CAMCORDER_TMP_FILE);
         mMediaUriTmp = Uri.fromFile(mMediaFileTmp);
         
     	Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//      intent.putExtra( MediaStore.EXTRA_OUTPUT, mMediaUriTmp);

     	mActivity.startActivityForResult(intent, MediaConstants.CAMERA_RESULT);
         
         return mMediaFileTmp;
	}
	
	public File capturePhoto (File fileExternDir)
	{     
        ContentValues values = new ContentValues();
      
        values.put(MediaStore.Images.Media.TITLE, MediaConstants.CAMERA_TMP_FILE);      
        values.put(MediaStore.Images.Media.DESCRIPTION,MediaConstants.CAMERA_TMP_FILE);

        mMediaFileTmp = new File(fileExternDir, new Date().getTime() + '-' + MediaConstants.CAMERA_TMP_FILE);
    	
        mMediaUriTmp = Uri.fromFile(mMediaFileTmp);
        //uriCameraImage = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, mMediaUriTmp);
        
        mActivity.startActivityForResult(intent, MediaConstants.CAMERA_RESULT);
        
         return mMediaFileTmp;
	}
	
	
	public void openGalleryChooser (String mimeType)
    {
    	Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType(mimeType); //limit to specific mimetype
		mActivity.startActivityForResult(intent, MediaConstants.GALLERY_RESULT);
		
    }
	
	public void openFileChooser ()
    {
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("file/*"); 
		mActivity.startActivityForResult(intent, MediaConstants.FILE_RESULT);
		
    }
	
	 public void playMedia (File mediaFile, String mimeType) {
		 
		 if (mimeType == null)
			 mimeType = getMimeType(mediaFile.getAbsolutePath());
		 
		 playMedia(Uri.fromFile(mediaFile), mimeType);
	 }
	
	 public void playMedia (Uri uri, String mimeType) {
			
	    	Intent intent = new Intent(Intent.ACTION_VIEW);

	    	//String metaMime = mimeType.substring(0,mimeType.indexOf("/")) + "/*";
	    	intent.setDataAndType(uri, mimeType);

	   // 	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	   	 	mActivity.startActivity(intent);
	   	 	
	 }
	 
	 public void shareMedia (File mediaFile, String mimeType)
	 {		
		 if (mimeType == null)
			 mimeType = getMimeType(mediaFile.getAbsolutePath());
	 	
    	Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.setType(mimeType);
    	intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mediaFile));
    	mActivity.startActivityForResult(Intent.createChooser(intent, "Share Media"),0); 
	 }
 
	 
	 public MediaDesc handleIntentLaunch(Intent intent)
	 {
		 MediaDesc result = null;
		 Uri uriLaunch = null;
		 
		 if (intent != null)
		 {
			 uriLaunch = intent.getData();
			
			// If originalImageUri is null, we are likely coming from another app via "share"
			if (uriLaunch == null)
			{
				if (intent.hasExtra(Intent.EXTRA_STREAM)) 
				{
					uriLaunch = (Uri) mActivity.getIntent().getExtras().get(Intent.EXTRA_STREAM);
					
				}
				
			}
		 }
			
		if (uriLaunch != null)
		{
			result = pullMediaDescFromUri (uriLaunch);
			
			if (result == null)
			{
				File fileMedia = new File(uriLaunch.getPath());
				if (fileMedia.exists())
				{
					result = new MediaDesc();
					result.path = fileMedia.getAbsolutePath();
					result.mimeType = getMimeType(result.path);
					
				}
				
			}
		}
		
		return result;
	 }
	 
	 public MediaDesc pullMediaDescFromUri(Uri originalUri) {
		 
		 	MediaDesc result = null;
		 	
	    	String[] columnsToSelect = { MediaStore.Video.Media.DATA, MediaStore.Images.Media.MIME_TYPE };
	    	Cursor videoCursor = mActivity.getContentResolver().query(originalUri, columnsToSelect, null, null, null );
	    	if ( videoCursor != null && videoCursor.getCount() == 1 ) {
		        if (videoCursor.moveToFirst())
		        {
		        	
		        	int colIdx = videoCursor.getColumnIndex(MediaStore.Images.Media.DATA);
		        	if (colIdx != -1)
		        	{
		        		result = new MediaDesc();
		        		result.path = videoCursor.getString(colIdx);
		        		result.mimeType = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
		        	}
		        }
	    	}

	    	return result;
	    }
	 
	 public class MediaResult 
	 {
		 public String path;
		 public String mimeType;
	 }
	 
	 
	 @Override
	public void onMediaScannerConnected() {			 
		 mScanner.scanFile(mMediaFileTmp.getAbsolutePath(), null);	 
	}

	@Override
	public void onScanCompleted(String path, Uri uri) {
		
		mScanner.disconnect();
		//Log.d(MediaAppConstants.TAG, "new path: " + path + "\nnew uri for path: " + uri.toString());
		
		if (mHandler != null)
		{
		 Message msg = mHandler.obtainMessage(5);
	     msg.getData().putString("path", path);    
	     mHandler.sendMessage(msg);
		}

	}
	
	public String getMimeType (String path)
	{
		String result = null;
		String fileExtension = MimeTypeMap.getFileExtensionFromUrl(path);		
		result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
		
		if (result == null)
		{
			if (path.endsWith("wav"))
			{
				result = "audio/wav";
			}
			else if (path.endsWith("mp3"))
			{
				result = "audio/mpeg";
			}
			else if (path.endsWith("3gp"))
			{
				result = "audio/3gpp";
			}
			else if (path.endsWith("mp4"))
			{
				result = "video/mp4";
			}
			else if (path.endsWith("jpg"))
			{
				result = "image/jpeg";
			}
			else if (path.endsWith("png"))
			{
				result = "image/png";
			}
			//need to add more here, or build from map
		}
		
		return result;
	}
}
