package com.example.bruno.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.bruno.myapplication.Image;

/**
 * Created by BRUNO on 06/03/2017.
 */

public class Splash extends Activity {

    private List<Image> images = new ArrayList<Image>();

    private void updateImages() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {

            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Images.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Images.Media._ID);
            int path=  cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            do {
                //long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn);
                String thispath = cursor.getString(path);
                Log.v("var:", thisTitle);
                Log.v("path", thispath);
                Image that = new Image();
                that.addImage(thispath, thisTitle);
                images.add(that);

            } while (cursor.moveToNext());
        }
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        updateImages();
        Collections.shuffle(images);
        Image thisImage = images.get(0);
        File imgFile = new  File(thisImage.pathImage);


        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
            myImage.setImageBitmap(myBitmap);

        }
    }
}

