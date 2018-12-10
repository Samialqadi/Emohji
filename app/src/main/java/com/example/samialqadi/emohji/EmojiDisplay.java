package com.example.samialqadi.emohji;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import static com.example.samialqadi.emohji.MainActivity.bitmap;

public class EmojiDisplay extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_display);

        imageView = (ImageView) findViewById(R.id.userPhoto);

        //supposed to get the bitmap from the other activity
        Intent intent = getIntent();
        //supposed to set the bitmap received to the image view
        Log.d("Image", bitmap.toString());
        imageView.setImageBitmap(bitmap);
    }
}
