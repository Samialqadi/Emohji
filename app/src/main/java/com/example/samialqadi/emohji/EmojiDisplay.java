package com.example.samialqadi.emohji;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;


import static com.example.samialqadi.emohji.MainActivity.bitmap;
import static com.example.samialqadi.emohji.MainActivity.emotionMap;
import static com.example.samialqadi.emohji.MainActivity.URL;

public class EmojiDisplay extends AppCompatActivity {
    ImageView imageView;
    private static final String TAG = "Emohji:Main";
    private String max;
    public static String[] emotionArr = {"anger", "contempt", "disgust", "fear", "happiness", "neutral", "sadness", "surprise"};
    public static String[] emojis = {"\uD83D\uDE21", "\uD83D\uDE27", "\uD83D\uDE16", "\uD83D\uDE31", "\uD83D\uDE00", "\uD83D\uDCA9", "\uD83D\uDE22", "\uD83E\uDD2F"};
    public static String[] ascii = {"•`_´•", "( ͡° ʖ̯ ͡°)", "(´～ヾ )", "(°Ω°)/", "٩(^‿^)۶", "(｡◕‿‿◕｡)", "(︶︹︶)", "(๑•́ ヮ •̀๑)" };

    private HashMap<String, String> emojiMap = new HashMap<>();
    private HashMap<String, String> asciiMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emoji_display);
        //new RetrieveURLBitmap().execute(URL);

        int index = 0;
        for (String emotion : emotionArr) {
            emojiMap.put(emotion, emojis[index]);
            asciiMap.put(emotion, ascii[index]);
            index++;
        }


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

                TextView EmotionText = findViewById(R.id.inputEmoji);
                EmotionText.append(max);

                TextView EmojiText = findViewById(R.id.hereEmod);
                EmojiText.append(emojiMap.get(max));

                TextView AsciiText = findViewById(R.id.inputAsc);
                AsciiText.append(asciiMap.get(max));
            }
        });

    }

    class RetrieveURLBitmap extends AsyncTask<String, String, String> {
        Bitmap myBitmap;
        ImageView imgView;

        @Override
        protected String doInBackground(String[] src) {
            setContentView(R.layout.activity_emoji_display);
            imgView = findViewById(R.id.userPhoto);
            try {
                URL url = new URL(src[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String x) {
            if (myBitmap != null && imgView != null) {
                imgView.setImageBitmap(bitmap);
            }


        }
    }
}


