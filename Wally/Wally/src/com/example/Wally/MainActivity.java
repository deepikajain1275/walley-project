package com.example.Wally;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static MainActivity instance;
	public static Uri imageid;

	public static MainActivity getInstance() {
		return instance;
	}

	public static void setInstance(MainActivity instance) {
		MainActivity.instance = instance;
	}

	private GridView sdcardImages;

	private ImageAdapter imageAdapter;

	/**
	 * Display used for getting the width of the screen.
	 **/
	private Display display;
	ImageView imgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainActivity.setInstance(this);
		display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();

		imgView = (ImageView) findViewById(R.id.imgView);
		setupViews();
		loadImages();
		sdcardImages.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Log.e("MyApp", "get onItem Click position= " + position);
				imageid = ImageUri(position);
				storeid(imageid);
				return true;
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		menu.add(1, 1, 0, "Start Walley");
		menu.add(1, 2, 0, "Stop Walley");
		menu.add(1, 3, 0, "Back");

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * Intent i = new Intent(); i.setClassName( "com.example.cursor",
		 * "com.example.cursor.myservice" ); bindService( i, null,
		 * Context.BIND_AUTO_CREATE);
		 */
		Intent i = new Intent(getApplicationContext(), Myservice.class);

		switch (item.getItemId()) {

		case 1:

			Intent intent = new Intent(this, TimerClass.class);
			startActivity(intent);

			return true;
		case 2:
			Toast.makeText(this, "Stop Walley", Toast.LENGTH_SHORT).show();

			MainActivity.getInstance().stopService(i);
			// stopService(i);
			return true;
		case 3:
			imgView.setImageBitmap(null);
			return true;

		}
		return super.onOptionsItemSelected(item);

	}

	public Uri ImageUri(int position) {
		int columnIndex = 0;
		Uri uri = null;

		String[] projection = { MediaStore.Images.Thumbnails._ID };// MediaStore.Images.Media.DATA
																	// };
		if (MainActivity.getInstance() != null) {
			ContentResolver cr = MainActivity.getInstance()
					.getContentResolver();

			Cursor cursor = cr.query(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
					projection, null, null, null);

			if (cursor != null) {
				columnIndex = cursor
				// .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
				cursor.moveToPosition(position);
				String imagePath = cursor.getString(columnIndex);
				uri = Uri.withAppendedPath(
						MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
								+ imagePath);
				imageid = uri;
				try {

					cursor.close();
					projection = null;
				} catch (Exception e) {

				}
			}
		}
		return imageid;

	}

	private void setupViews() {
		// TODO Auto-generated method stub
		sdcardImages = (GridView) findViewById(R.id.sdcard);
		sdcardImages.setNumColumns(display.getWidth() / 95);
		imageAdapter = new ImageAdapter(this);
		sdcardImages.setAdapter(imageAdapter);

		sdcardImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub

				ContentResolver cr = MainActivity.getInstance()
						.getContentResolver();
				Uri uri = ImageUri(position);

				try {
					Bitmap bitmap;
					try {
						bitmap = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
						imgView.setImageBitmap(bitmap);

					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.i("called", "called");
					// Log.i("path", imagePath);
					Log.i("id", "" + MediaStore.Images.Media._ID);
					Log.i("", "" + MediaStore.Images.Thumbnails._ID);

					// Intent intent = new
					// Intent(MainActivity.getInstance(),
					// ImageView.class);
					// startActivity(intent);
				} finally {

				}

			}

		});
		imgView.setOnClickListener(instance);

	}

	protected void onDestroy() {
		super.onDestroy();

	}

	private void loadImages() {
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			new LoadImagesFromSDCard(this).execute();
		} else {
			final LoadedImage[] photos = (LoadedImage[]) data;
			if (photos.length == 0) {
				new LoadImagesFromSDCard(this).execute();
			}
			for (LoadedImage photo : photos) {
				addImage(photo);
			}
		}

	}

	public void addImage(LoadedImage... value) {
		for (LoadedImage image : value) {
			imageAdapter.addPhoto(image);
			imageAdapter.notifyDataSetChanged();
		}
	}

	public void storeid(Uri id) {

		Myservice.al.add(id);
		Log.i("size", "" + Myservice.al.size());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		storeid(imageid);
		imgView.setImageBitmap(null);

	}

}
