package com.example.kakaogalleryproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
  //  private Handler mHandler; // code piece which was first written for direct threading
   // private Message message;// code piece which was first written for direct threading
    private ProgressBar mProgress;
    private DownloadTask downloadTask;
    //private int START_LOADING = 1111;// code piece which was first written for direct threading
   // private int END_LOADING = 2222;// code piece which was first written for direct threading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // code piece which was first written for direct threading
       /* mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if (msg.arg1 == START_LOADING) startLoading();
                if (msg.arg1 == END_LOADING) endLoading();
            }
        }; */

        mProgress = findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a staggerd grid layout manager
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        mAdapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(mAdapter);

        downloadTask = new DownloadTask();
        downloadTask.execute();

        //loadImages(); // code piece which was first written for direct threading
    }
    // code piece which was first written for direct threading
    /*
    public void startLoading(){
        mProgress.setVisibility(View.VISIBLE);
    }
    public void endLoading(){
        mAdapter.notifyDataSetChanged(); // dataset changed
        mProgress.setVisibility(View.GONE);
    }*/
    private class DownloadTask extends AsyncTask<Object, String, ArrayList<String>> {
        @Override
        protected void onPreExecute(){
            mProgress.setVisibility(View.VISIBLE);
        }
        @Override
        protected ArrayList<String> doInBackground(Object... params){
            String url = "https://www.gettyimagesgallery.com/collection/sasha";
            String imageSelector = ".jq-lazy";
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get(); // get doc
                Elements images = doc.select(imageSelector); // get images
                for (int i=0;i<images.size();i++) imageList.add(images.get(i).attributes().get("data-src"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            return imageList;
        }
        @Override
        protected void onPostExecute(ArrayList<String> imageList){
            mAdapter.notifyDataSetChanged(); // dataset changed
            mProgress.setVisibility(View.GONE);
        }
    }

    // below code uses direct threading & messaging to get images
    /*
    public void loadImages(){
        // get images from site
        parseThread = new Thread(){
            public void run(){
                message = mHandler.obtainMessage();
                message.arg1 = START_LOADING;
                mHandler.sendMessage(message);

                String url = "https://www.gettyimagesgallery.com/collection/sasha";
                String imageSelector = ".jq-lazy";
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get(); // get doc
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                Elements images = doc.select(imageSelector); // get images
                for (int i=0;i<images.size();i++)  imageList.add(images.get(i).attributes().get("data-src"));

                message = mHandler.obtainMessage();
                message.arg1 = END_LOADING;
                mHandler.sendMessage(message);
            }
        };
        parseThread.start();

       // try{parseThread.join(); } catch(InterruptedException e){}

    }*/

    @Override
    public void onDestroy( ) {
        if (downloadTask.getStatus() == AsyncTask.Status.RUNNING) downloadTask.cancel(true);
        super.onDestroy();

    }
}
