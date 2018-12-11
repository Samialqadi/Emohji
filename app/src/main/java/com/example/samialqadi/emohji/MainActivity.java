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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Emohji:Main";
    private final String apiEndpoint = "https://westus.api.cognitive.microsoft.com/face/v1.0/detect?returnFaceId=true&returnFaceLandmarks=false&returnFaceAttributes=emotion";
    private final String subscriptionKey = "fd5fc95df0344e5c8b7f65f0ff5a483b";
    private RequestQueue queue;
    private String URL = "https://previews.123rf.com/images/deagreez/deagreez1608/deagreez160801143/61229745-portrait-of-handsome-cheerful-young-smiling-man-in-glasses.jpg";
    public static HashMap<String, Double> emotionMap = new HashMap<>();
    private String[] emotionArr = {"anger", "contempt", "disgust", "fear", "happiness", "neutral", "sadness", "surprise"};
    JSONObject emotion = new JSONObject();
    Button openFile;
    ImageView imageView;
    static Bitmap bitmap;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.queue = Volley.newRequestQueue(this);
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

        makeRequest();
        Log.e(TAG, "FINISHED");
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

    private void makeRequest() {
        Log.d(TAG, "TEST");
        try {
            Log.d(TAG, "HERE");
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("url", this.URL);

            CustomJsonArrayRequest jsonObjectRequest = new CustomJsonArrayRequest(
                    Request.Method.POST,
                    apiEndpoint,
                    jsonBody,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            try {
                                JSONObject obj = new JSONObject();
                                obj = response.getJSONObject(0);
                                JSONObject emotion = obj.getJSONObject("faceAttributes").getJSONObject("emotion");

                                for (String emotionType : emotionArr) {
                                    Log.e(TAG, emotionType + Double.parseDouble(emotion.get(emotionType).toString()));
                                    emotionMap.put(emotionType, Double.parseDouble(emotion.get(emotionType).toString()));
                                }


                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Log.d(TAG, "HERE2");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Ocp-Apim-Subscription-Key", subscriptionKey);
                    return params;
                }

                @Override
                protected Map<String, String> getParams() {
                    Log.d(TAG, "HERE3");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ReturnFaceId", "true");
                    params.put("returnFaceAttributes", "returnFaceAttributes=emotion");

                    return params;
                }

            };
            Log.d(TAG, "HERE4");
            this.queue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.d(TAG, "HERE5");
            e.printStackTrace();
        }
    }
}
