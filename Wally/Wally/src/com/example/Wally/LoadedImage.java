package com.example.Wally;

import android.graphics.Bitmap;

class LoadedImage {
    Bitmap mBitmap;

    LoadedImage(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }
}