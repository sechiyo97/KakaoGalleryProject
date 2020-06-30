package com.example.kakaogalleryproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    DisplayMetrics mMetrics;
    private ImageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_gallery);

        reloadImages(); // 이미지 불러오기
    }

    private void reloadImages(){  // 이미지 불러오기
        GridView gridview = (GridView) findViewById(R.id.ImgGridView);
        adapter = new ImageAdapter(this);
        gridview.setAdapter(adapter);

        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    public class ImageAdapter extends BaseAdapter {
        private ArrayList<String> thumbsDataList;
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
            thumbsDataList = new ArrayList<String>();
            getImages();
        }
        public void getImages(){
            Thread parseThread = new Thread() {
                public void run() {
                    String url = "https://www.gettyimagesgallery.com/collection/sasha";
                    String imageSelector = ".jq-lazy";
                    Document doc = null;
                    try {
                        doc = Jsoup.connect(url).get(); // get doc
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    Elements images = doc.select(imageSelector); // get images
                    for (int i=0;i<images.size();i++) thumbsDataList.add(images.get(i).attributes().get("data-src"));
                }
            };
            parseThread.start();
            try{parseThread.join(); } catch(InterruptedException e){}
        }

        public int getCount() {
            return thumbsDataList.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            int rowWidth = (mMetrics.widthPixels) / 3;

            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(rowWidth,rowWidth));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            Glide.with(mContext).load(thumbsDataList.get(position)).centerCrop().into(imageView); // thumbnail(0.5f) 뺐음

            return imageView;
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.fadein, R.anim.anim_slide_out_up);
    }
    public void gallery_exit(View v){
        finish();
    }

    protected void temp(View v){
        Toast.makeText(this,"Temporary Function",Toast.LENGTH_SHORT).show();
    }
}