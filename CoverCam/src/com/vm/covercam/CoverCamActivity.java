package com.vm.covercam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.vm.covercam.R;
import com.vm.covercam.constants.CFConstants;
import com.vm.covercam.gallery.BaseActivity;
import com.vm.covercam.gallery.ImageAdapter;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;

/**
 * @author Kucheryavaya Lyudmila
 */

public class CoverCamActivity extends BaseActivity {

	private RelativeLayout cameraPreviewFrame;
	private CoverCamView cameraView;
	private Button LCapture;
	private String name_photo = "cover camera.jpeg";
	private Integer[] pics = null;
	private TextView txtTop;

	@Override
	public void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.cover_camera);
		cameraView = new CoverCamView(this);
		cameraPreviewFrame = (RelativeLayout) findViewById(R.id.camera_preview1);
		txtTop = (TextView) findViewById(R.id.txtTop1);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CFConstants.getSizeCanvas("width"), CFConstants.getSizeCanvas("height"));
		txtTop.setId(2);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.BELOW,2);
		cameraPreviewFrame.setLayoutParams(params);
		LCapture = (Button) findViewById(R.id.btnCamera);
		LCapture.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				CoverCamView.getCamera().autoFocus(myAutoFocusCallback);
				cameraPreviewFrame.setDrawingCacheEnabled(true);
					Bitmap b = cameraPreviewFrame.getDrawingCache(true);
					Environment.getExternalStorageDirectory();
					File dir = new File(
							Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), this
							.getClass().getPackage().getName());
					dir.mkdirs();
					String p = dir + "/" + name_photo;
					try {

						b.compress(CompressFormat.PNG, 100, new FileOutputStream(p));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Toast.makeText(getApplicationContext(), "take photo!", Toast.LENGTH_SHORT).show();
					Log.i("path", p.toString());
					Uri uri = Uri.parse(p);
					Intent intent = new Intent(CoverCamActivity.this, PhotoViewActivity.class);
					intent.setData(uri);
					startActivity(intent);
				}
			
			});


		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		cameraPreviewFrame.addView(cameraView);

		Gallery ga = (Gallery)findViewById(R.id.gallery1);
		ga.setAdapter(new ImageAdapter(this));
		ga.setSelection(2);
		ga.setSpacing(0);
		ga.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				pics = CFConstants.getPics();
				
				if (pics[arg2] == pics[1]) {
					CFConstants.setColorFilter(1);
				}
				else
					if (pics[arg2] == pics[2]) {
						CFConstants.setColorFilter(2);
					}
					else 
						if (pics[arg2] == pics[3]) {
							CFConstants.setColorFilter(3);
						}
						else 
							if (pics[arg2] == pics[4]) {
								CFConstants.setColorFilter(4);
							}
							else {
								CFConstants.setColorFilter(0);
							}

			}

		});
		
		}

		AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

			@Override
			public void onAutoFocus(boolean arg0, Camera arg1) {
				// TODO Auto-generated method stub

			}};

			ShutterCallback myShutterCallback = new ShutterCallback(){

				@Override
				public void onShutter() {
					// TODO Auto-generated method stub

				}};

				PictureCallback myPictureCallback_RAW = new PictureCallback(){

					@Override
					public void onPictureTaken(byte[] arg0, Camera arg1) {
						
					}};

					PictureCallback myPictureCallback_JPG = new PictureCallback(){

						@Override
						public void onPictureTaken(byte[] arg0, Camera arg1) {
							Uri uriTarget = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());

							OutputStream imageFileOS;
							try {
								imageFileOS = getContentResolver().openOutputStream(uriTarget);
								imageFileOS.write(arg0);
								imageFileOS.flush();
								imageFileOS.close();

								Toast.makeText(CoverCamActivity.this, 
										"Image saved: " + uriTarget.toString(), 
										Toast.LENGTH_LONG).show();

							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							CoverCamView.getCamera().startPreview();
						}};

	}
