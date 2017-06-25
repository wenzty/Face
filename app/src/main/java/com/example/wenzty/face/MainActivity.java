package com.example.wenzty.face;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.baidu.aip.face.AipFace;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static android.R.attr.bitmap;
import static android.R.attr.path;
import static android.provider.UserDictionary.Words.APP_ID;

//9543989  6vCICkarlFvWl8jZq0ypI8zU   9WsKNKG4PXAY6vks1QKbylG4QLzXrZyE
public class MainActivity extends AppCompatActivity {
    private Button btnFace;
    private ImageView imgBl;
    private AipFace client;
    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findviews();
        initFaceClient();
       initHandler();
        FaceClient();
    }

    private void initFaceClient() {
        AipFace client = new AipFace("9543989", "6vCICkarlFvWl8jZq0ypI8zU", "9WsKNKG4PXAY6vks1QKbylG4QLzXrZyE");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);



    }

    private void findviews() {
        imgBl = (ImageView) findViewById(R.id.img_bl);
        btnFace = (Button) findViewById(R.id.btn_face);
        btnFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FaceClient();
            }
        });
    }

    private void FaceClient() {

        final HashMap<String,String> paraMap = new HashMap<String, String>();
        paraMap.put("face_fields", "age,beauty,expression,faceshape,gender,glasses,landmark,race,qualities");
        new Thread(new Runnable() {
            @Override
            public void run() {
                imgBl.setDrawingCacheEnabled(true);
                Bitmap bmp = imgBl.getDrawingCache();
                final byte[] imgbyte = BitmapByte(bmp);
                JSONObject res = client.detect(imgbyte,paraMap);
                Log.e("MainAcitity", res.toString());

                Rect r = new Rect((int)(117/1.5f),(int)(127/1.5f),(int)((117+207)/1.5f),(int)((127+194)/1.5f));
                Message msg = Message.obtain();
                msg.obj = r;
                myHandler.sendMessage(msg);
            }
        }).start();

    }

    private byte[] BitmapByte(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    private void initHandler() {
        myHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Rect rect = (Rect) msg.obj;

                return  true;
            }
        });
    }
















}
