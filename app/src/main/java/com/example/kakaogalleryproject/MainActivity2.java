package com.example.kakaogalleryproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
    protected ArrayList<String> imageList = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Handler mHandler;
    private Message message;
    private int IMAGES_LOADED = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if (msg.arg1 == IMAGES_LOADED) showImages();
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a staggerd grid layout manager
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        loadImages();
    }
    public void showImages(){
        // specify an adapter (see also next example)
        mAdapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(mAdapter);
    }
    public void loadImages(){
        // get images from site
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
                for (int i=0;i<images.size();i++) imageList.add(images.get(i).attributes().get("data-src"));

                message = mHandler.obtainMessage();
                message.arg1 = IMAGES_LOADED;
                mHandler.sendMessage(message);
            }
        };
        parseThread.start();

        try{parseThread.join(); } catch(InterruptedException e){}

    }
}
