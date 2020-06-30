package com.example.kakaogalleryproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String sampleURL = "https://cdn-lostark.game.onstove.com/uploadfiles/tooltip/82fba916b31d449dbae5abe9c7f7fc86.png";
    private ProgressDialog progressDialog;

    private ImageView reloadButton;
    private String[] mariImageURL = new String[6];
    private ImageView[] mariImage = new ImageView[6];
    private TextView[] mariName = new TextView[6];
    private TextView[] mariPrice = new TextView[6];

    public Handler mHandler;
    public Message message;
    private int SHOW_IMAGE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if (msg.arg1 == SHOW_IMAGE) showImage(msg.arg2);
            }
        };

        mariImage[0] = (ImageView) findViewById(R.id.mari_image_0);
        mariImage[1] = (ImageView) findViewById(R.id.mari_image_1);
        mariImage[2] = (ImageView) findViewById(R.id.mari_image_2);
        mariImage[3] = (ImageView) findViewById(R.id.mari_image_3);
        mariImage[4] = (ImageView) findViewById(R.id.mari_image_4);
        mariImage[5] = (ImageView) findViewById(R.id.mari_image_5);

        mariName[0] = (TextView) findViewById(R.id.mari_name_0);
        mariName[1] = (TextView) findViewById(R.id.mari_name_1);
        mariName[2] = (TextView) findViewById(R.id.mari_name_2);
        mariName[3] = (TextView) findViewById(R.id.mari_name_3);
        mariName[4] = (TextView) findViewById(R.id.mari_name_4);
        mariName[5] = (TextView) findViewById(R.id.mari_name_5);

        mariPrice[0] = (TextView) findViewById(R.id.mari_price_0);
        mariPrice[1] = (TextView) findViewById(R.id.mari_price_1);
        mariPrice[2] = (TextView) findViewById(R.id.mari_price_2);
        mariPrice[3] = (TextView) findViewById(R.id.mari_price_3);
        mariPrice[4] = (TextView) findViewById(R.id.mari_price_4);
        mariPrice[5] = (TextView) findViewById(R.id.mari_price_5);

        reloadButton = (ImageView) findViewById(R.id.reload_button);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadImages();
            }
        });
        loadImages();
    }
    public void showImage(int i){
        Glide.with(getApplicationContext()).load(mariImageURL[i]).into(mariImage[i]);
    }

    // 웹에서 마리 정보 가져와 띄우기
    public void loadImages(){

        Thread parseThread = new Thread(){
            public void run(){
                String url = "https://www.gettyimagesgallery.com/collection/sasha";
                String imageSelector = ".jq-lazy";
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get(); // get doc
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                Elements images = doc.select(imageSelector); // get images

                for(int i=0;i<6;i++){//images.size();i++) {
                    mariImageURL[i] = images.get(i).attributes().get("data-src");
                    // 이미지 로딩
                    message = mHandler.obtainMessage();
                    message.arg1 = SHOW_IMAGE;
                    message.arg2 = i;
                    mHandler.sendMessage(message);
                }
            }
        };
        parseThread.start();
        try{parseThread.join(); } catch(InterruptedException e){}

    }
}
