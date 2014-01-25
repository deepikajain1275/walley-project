package com.example.Wally;

import java.io.IOException;

import org.apache.http.conn.ManagedClientConnection;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

public class LoadImagesFromSDCard extends
		AsyncTask<Object, LoadedImage, Object> {

	private Context context;

	public LoadImagesFromSDCard(Context context) {
		this.context = context;
	}

	@Override
	// take from internet code.
	protected Object doInBackground(Object... params) {

		Bitmap bitmap = null;
		Bitmap newBitmap = null;
		Uri uri = null;
		String[] projection = { MediaStore.Images.Thumbnails._ID };
		// Create the cursor pointing to the SDCard
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection,null, null, null);

		int columnIndex = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
		int size = cursor.getCount();
		// If size is 0, there are no images on the SD Card.
		if (size == 0) {
			// No Images available, post some message to the user
		}
		int imageID = 0;
		for (int i = 0; i < size; i++) {
			cursor.moveToPosition(i);
			imageID = cursor.getInt(columnIndex);
			uri = Uri.withAppendedPath(
					MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, ""
							+ imageID);
			try {
				bitmap = BitmapFactory.decodeStream(context
						.getContentResolver().openInputStream(uri));
				if (bitmap != null) {
					newBitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);

					bitmap.recycle();
					if (newBitmap != null) {
						publishProgress(new LoadedImage(newBitmap));
						

					}
				}
			} catch (IOException e) {
				// Error fetching image, try to recover
			}
		}
		cursor.close();
		return null;
	}

	public void onProgressUpdate(LoadedImage... value) {
		MainActivity.getInstance().addImage(value);
	}

	
}
