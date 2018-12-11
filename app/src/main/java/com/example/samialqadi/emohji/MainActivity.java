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

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import android.util.Base64;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.webkit.URLUtil;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.AsyncTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Emohji:Main";
    private final String apiEndpoint = "https://westus.api.cognitive.microsoft.com/face/v1.0/detect?returnFaceId=true&returnFaceLandmarks=false&returnFaceAttributes=emotion";
    private final String subscriptionKey = "fd5fc95df0344e5c8b7f65f0ff5a483b";
    private RequestQueue queue;
    public static String URL = "https://img.purch.com/h/1000/aHR0cDovL3d3dy5saXZlc2NpZW5jZS5jb20vaW1hZ2VzL2kvMDAwLzA2OS85MDcvb3JpZ2luYWwvYW5ncnktZmFjZS5qcGVn";
    public static HashMap<String, Double> emotionMap = new HashMap<>();
    public static String[] emotionArr = {"anger", "contempt", "disgust", "fear", "happiness", "neutral", "sadness", "surprise"};
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

        openFile = findViewById(R.id.openFile);
        imageView = findViewById(R.id.userPhoto);

        openFile.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
//                //opens the photo gallery
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);


                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Input Image Link");
                alertDialog.setMessage("");

                final EditText input = new EditText(MainActivity.this);
                input.setText("Type Image URL");
                input.setTextIsSelectable(true);
                alertDialog.setView(input); // uncomment this line

                alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getBaseContext(), EmojiDisplay.class);

                        if (URLUtil.isValidUrl(input.getText().toString())) {
                            URL = input.getText().toString();
                        } else {
                            URL = "https://i0.wp.com/hashtag3r.com/wp-content/uploads/2018/08/Things-that-a-happy-person-does-not-do-it-at-all.jpg?fit=480%2C360&ssl=1";
                        }
                        makeRequest();
                        //new RetrieveURLBitmap().execute(URL);
                        startActivity(intent);

                    }
                });

                alertDialog.show();  //<-- See This!
            }
        });
        Log.e(TAG, "FINISHED");
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Intent intent = new Intent(this, EmojiDisplay.class);
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            try {
//                Uri pickedImage = data.getData();
//                InputStream imageStream = getContentResolver().openInputStream(pickedImage);
//                bitmap = BitmapFactory.decodeStream(imageStream);
//                makeRequest();
//                startActivity(intent);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void makeRequest() {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("url", URL);

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
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Ocp-Apim-Subscription-Key", subscriptionKey);
                    //params.put("Content-Type", "application/octet-stream");
                    return params;
                }

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    return params;
                }

            };
            this.queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private byte[] imageToByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
