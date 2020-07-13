package com.example.gallaryapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.gallaryapplication.adapter.GallaryAdapter;
import com.example.gallaryapplication.model.ImageModel;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions();
        RecyclerView recyclerView = findViewById(R.id.galleryRecyclerView);
        recyclerView.setHasFixedSize(true);
        try {
        final GallaryAdapter adapter =  new GallaryAdapter(getListOfImages());;



        final GridLayoutManager  layoutManager = new GridLayoutManager(getApplicationContext(),4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? layoutManager.getSpanCount() : 1;
            }
        });


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    ArrayList<ImageModel> getListOfImages() throws FileNotFoundException {
        final String[] columns = {MediaStore.Images.Media._ID,MediaStore.Images.Media.DATE_TAKEN };
        final String orderBy = MediaStore.Images.Media._ID;
//Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
//Total number of images
        int count = cursor.getCount();
        Log.i("Count::::", count+"");
        count = 20;


//Create an array to store path to all the images
        ArrayList<ImageModel> arrPath = new ArrayList<>();
        String previousDate = "";

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            ImageModel imageModel=  new ImageModel();
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int dateColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
            String dateString = formatter.format(new Date(Long.parseLong((cursor.getString(dateColumnIndex)))));
            Log.i("date String",dateString);

            if (i==0) // initial state always start with date header
            {
                imageModel.setIsHeader(true);
                imageModel.setDateHeader(dateString);
                previousDate =dateString;
            }else
            if (!previousDate.equals(dateString))
            {
                imageModel.setIsHeader(true);
                imageModel.setDateHeader(dateString);
                previousDate =dateString;
            }
            else
                imageModel.setIsHeader(false);
            //Store the path of the image
            Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + cursor.getString(dataColumnIndex));
            Log.i("External Uri::::", uri+"");
            ParcelFileDescriptor parcelFileDescriptor=  getContentResolver().openFileDescriptor(uri, "r");
             Bitmap bitmap= BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.getFileDescriptor());
             if (imageModel.getIsHeader() && i!=0)
             {
                 Log.e("inside is::::::",i+"");
                 arrPath.add(imageModel);
                 ImageModel imageModel2=  new ImageModel();
                 imageModel2.setBitmap(bitmap);
                 imageModel2.setIsHeader(false);
                 arrPath.add(imageModel2);

             }else
             {
                 imageModel.setBitmap(bitmap);
                 arrPath.add(imageModel);
             }

        }
// The cursor should be freed up after use with close()
        cursor.close();
        return arrPath;
    }

    public  void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
