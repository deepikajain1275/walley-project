package com.example.Wally;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class Myservice extends Service {

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("", "stop");
		if (timer != null) {
			timer.cancel();
			timer = null;
		} else {
			Log.e("TRACK_RECORDING_SERVICE", "Timer already null.");
		}
		// al.removeAll(al);

	}

	public static ArrayList al = new ArrayList();
	Timer timer = new Timer();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();
		//Alarmmgr = (AlarmManager) getSystemService(ALARM_SERVICE);

	}

	public void setwallpaper(ArrayList al) {
		Object ar[] = al.toArray();
		Log.i("", "" + ar.length);
		ContentResolver cr = MainActivity.getInstance().getContentResolver();
		Uri uri = Uri.parse(ar[0].toString());
		al.remove(0);
		al.add(ar[0]);
		Bitmap mBitmap = null;
		try {
			mBitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		WallpaperManager myWallpaperManager = WallpaperManager
				.getInstance(getApplicationContext());
		try {
			myWallpaperManager.setBitmap(mBitmap);

		} catch (IOException e) {

		}
	}

	public AlarmManager Alarmmgr;
	public PendingIntent myPendingIntent;

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i("called ", "service");
		
	}

	public int onStartCommand(Intent intent, int flags, int startId) {

		TimerTask Task = new TimerTask() {
			@Override
			public void run() {
				setwallpaper(al);
			}
		};
		 try{

		// schedule the task to run starting now and then every hour...
		timer.schedule(Task, 0, Integer.parseInt(TimerClass.time));
		}
		 catch(NumberFormatException e)
		 {Log.i("not number","interval value is not number");
			 
		 }
		 

		return super.onStartCommand(intent, flags, startId);

	}

}
