
package com.vm.covercam;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import com.vm.covercam.R;
import com.vm.covercam.json.JSONParser;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Kucheryavaya Lyudmila
 */

public class PhotoViewActivity extends Activity {
	private static final String TAG = "PhotoViewActivity";

	private Uri uri;
	private ImageView photo;
	private ImageButton btnShare;
	private ImageButton btnBack;
	private Bitmap bm;
	private JSONParser jsonParser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_preview);
		uri = getIntent().getData();
		
		photo = (ImageView) findViewById(R.id.photo);
		photo.setImageURI(uri);
		
		btnBack = (ImageButton) findViewById(R.id.button_back);
		btnBack.setOnClickListener(onBackClickListener);
		
		btnShare = (ImageButton) findViewById(R.id.button_share);
		btnShare.setOnClickListener(onUploadClickListener);
		
		bm = BitmapFactory.decodeFile(uri.toString());
	}

	OnClickListener onUploadClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new UploadPhotoTask().execute();
		}
	};
	
	OnClickListener onBackClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};


	public void executeMultipartPost() throws Exception {
		try {
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 75, bos);
			byte[] data = bos.toByteArray();
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost("http://api.share.pho.to/v1/photosets");
			ByteArrayBody bab = new ByteArrayBody(data, "temp.jpg");
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("app_key", new StringBody("123456789"));
			reqEntity.addPart("images[src]", bab);
			reqEntity.addPart("images[src_type]", new StringBody("file"));
			reqEntity.addPart("images[caption]", new StringBody("Hello Pho.to"));
			reqEntity.addPart("comment_enabled", new StringBody("0"));
			postRequest.setEntity(reqEntity);
			Log.i("postRequest", postRequest.toString());
			HttpResponse response = httpClient.execute(postRequest);
			Log.i("response", response.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String sResponse;
			StringBuilder s = new StringBuilder();

			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			jsonParser = new JSONParser();
			String mstr = s.toString();
			String str = jsonParser.parseLinkImage(mstr);
			Log.i("strin", s.toString());
			Log.i("large", str);
			System.out.println("Response: " + s);
		} catch (Exception e) {
			Log.e("class", e.getLocalizedMessage());
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG, "Going back");
		photo = null;
		super.finish();
	}

	private class UploadPhotoTask extends AsyncTask<String, Void, Void> {

		private final int ID_My_Notification = 1;
		private NotificationManager mNotificationManager;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			 String ns = Context.NOTIFICATION_SERVICE;
			   mNotificationManager = (NotificationManager) getSystemService(ns);
			    
			   int icon = android.R.drawable.stat_sys_upload;
			   CharSequence tickerText = "Фото загружается";
			   long when = System.currentTimeMillis();
			 
			   Notification notification = new Notification(icon, tickerText, when);
			    
			   Context context = getApplicationContext();
			   CharSequence contentTitle = "My Notification";
			   CharSequence contentText = "Notification Bar!";
			   Intent notificationIntent = new Intent(getBaseContext(), PhotoViewActivity.class);
			   PendingIntent contentIntent = PendingIntent.getActivity(PhotoViewActivity.this, 0, notificationIntent, 0);
			   notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			   mNotificationManager.notify(ID_My_Notification, notification);
			   Toast.makeText(getApplicationContext(), "фотография отправляется!", Toast.LENGTH_SHORT).show();
			   
		}
		
		@Override
		protected Void doInBackground(String... params) {
			
			try {
				executeMultipartPost();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mNotificationManager.cancel(ID_My_Notification);
			Toast.makeText(getApplicationContext(), "фотография отправлена!", Toast.LENGTH_SHORT).show();
			   
		}
		
	}
	
}
