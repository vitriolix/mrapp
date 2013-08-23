package info.guardianproject.mrapp.ciphercam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import info.guardianproject.mrapp.R;
import info.guardianproject.mrapp.media.MediaConstants;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class CameraActivity extends Activity implements OnClickListener {
	private static final String TAG = "CameraActivity";
	CameraSurfaceView mPreviewSurfaceView;
	ImageButton mShutterButton;
	String mPath;
	PictureCallback mPictureCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		
		mPictureCallback = new PictureCallback() {
			
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				// TODO something with the image data
//				Debug.waitForDebugger();
				Log.d("foo", data.toString());
				File file = new File(mPath);
				file.getParentFile().mkdirs();
				FileOutputStream outStream = null;
				try {
					outStream = new FileOutputStream(file);
					outStream.write(data);
					outStream.close();
					Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}
				Log.d(TAG, "onPictureTaken - jpeg");

//				Handler handler = new Handler();
//				handler.post(new Runnable() {
//					public void run() {
//						finishActivity(MediaConstants.CAMERA_RESULT);
//					}
//				});
				runOnUiThread(new Runnable() {
					public void run() {
						setResult(RESULT_OK);
						finish();//(MediaConstants.CAMERA_RESULT);
					}
				});

				// Restart the preview and re-enable the shutter button so that we can take another picture
//				camera.startPreview();
//				shutterButton.setEnabled(true);				
			}
		};
		
		Object o = getIntent().getExtras().get(MediaStore.EXTRA_OUTPUT);
		if (o == null) {
			// FIXME create random temp file
			mPath = "fixme need real temp file";
		} else {
			mPath = ((Uri) o).toString().split("file://")[1];
		}
		Log.d(TAG, "mPath: " + mPath);

		// set up our preview surface
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		mPreviewSurfaceView = new CameraSurfaceView(this);
		preview.addView(mPreviewSurfaceView);

		// grab out shutter button so we can reference it later
		mShutterButton = (ImageButton) findViewById(R.id.shutter_button);
		mShutterButton.setOnClickListener(this);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_camera, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		takePicture();
	}

	private void takePicture() {
		mShutterButton.setEnabled(false);
		mPreviewSurfaceView.takePicture(mPictureCallback);
	}
}
