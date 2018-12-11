package com.example.samialqadi.emohji;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.samialqadi.emohji.MainActivity.bitmap;
import static com.example.samialqadi.emohji.MainActivity.emotionMap;

public class EmojiDisplay extends AppCompatActivity {
    ImageView imageView;
    private static final String TAG = "Emohji:Main";
    private String max;

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


        Button EmojiButton = findViewById(R.id.generateEmoji);
        EmojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                max = "";
                double maxValue = 0;

                for (String key : emotionMap.keySet()) {
                    if (emotionMap.get(key) > maxValue) {
                        max = key;
                        maxValue = emotionMap.get(key);
                    }
                }

                TextView EmotionText = (TextView) findViewById(R.id.inputEmoji);
                EmotionText.append(max);
            }
        });

    }
}
