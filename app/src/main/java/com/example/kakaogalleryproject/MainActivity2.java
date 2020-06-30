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
    private Handler mHandler;
    private Message message;
    private ProgressBar mProgress;
    private Thread parseThread;
    private int START_LOADING = 1111;
    private int END_LOADING = 2222;

    private Long start_t;
    private Long prev_t, cur_t;
    public void timeFactorTest(int lineNum){
        cur_t = System.currentTimeMillis();
        Log.e("TIME FACTOR TESTING", "line " +lineNum + ": " + (cur_t-prev_t));
        prev_t = cur_t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /** init for reconstruct part **/
        start_t = System.currentTimeMillis();
        prev_t = start_t;

        // code piece which was first written for direct threading
       /* mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if (msg.arg1 == START_LOADING) startLoading();
                if (msg.arg1 == END_LOADING) endLoading();
            }
        }; */

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a staggerd grid layout manager
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        mAdapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(mAdapter);

        new DownloadTask().execute("https://www.gettyimagesgallery.com/collection/sasha");

        //loadImages(); // code piece which was first written for direct threading
    }
    public void startLoading(){
        mProgress.setVisibility(View.VISIBLE);
    }
    public void endLoading(){
        mAdapter.notifyDataSetChanged(); // dataset changed
        mProgress.setVisibility(View.GONE);
    }
    private class DownloadTask extends AsyncTask<String, String, ArrayList<String>> {
        @Override
        protected void onPreExecute(){
            mProgress.setVisibility(View.VISIBLE);
        }
        @Override
        protected ArrayList<String> doInBackground(String... params){
            String url = params[0];
            String imageSelector = ".jq-lazy";
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get(); // get doc
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            Elements images = doc.select(imageSelector); // get images
            for (int i=0;i<images.size();i++) imageList.add(images.get(i).attributes().get("data-src"));
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
        super.onDestroy( );
    }
}
