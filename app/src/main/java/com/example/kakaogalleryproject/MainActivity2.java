package com.example.kakaogalleryproject;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a staggerd grid layout manager
        layoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        loadImages();
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
            }
        };
        parseThread.start();
        try{parseThread.join(); } catch(InterruptedException e){}

        // specify an adapter (see also next example)
        mAdapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(mAdapter);
    }
}
