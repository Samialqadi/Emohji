package com.example.samialqadi.emohji;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Button openFile;
    ImageView imageView;
    static Bitmap bitmap;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openFile = (Button) findViewById(R.id.openFile);
        imageView = (ImageView) findViewById(R.id.userPhoto);

        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //opens the photo gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(this, EmojiDisplay.class);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri pickedImage = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(pickedImage);
                bitmap = BitmapFactory.decodeStream(imageStream);
                startActivity(intent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
