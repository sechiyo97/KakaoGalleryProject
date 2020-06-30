package com.example.kakaogalleryproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    protected ArrayList<String> imageList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = findViewById(R.id.progress_bar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a staggerd grid layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        // adapter
        mAdapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(mAdapter);
        DownloadTask();
    }
    Disposable downloadtask;
    void DownloadTask() {
        // onPreExecute
        mProgress.setVisibility(View.VISIBLE);

        downloadtask = Observable.fromCallable(() -> {

            // onBackground
            Document doc = Jsoup.connect("https://www.gettyimagesgallery.com/collection/sasha").timeout(3000).get();
            String imageSelector = ".jq-lazy";
            Elements images = doc.select(imageSelector); // get images
            for (int i=0;i<images.size();i++) imageList.add(images.get(i).attributes().get("data-src"));
            return false;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {

                    // onPostExecute
                    mAdapter.notifyDataSetChanged(); // dataset changed
                    mProgress.setVisibility(View.GONE);

                    downloadtask.dispose();
                });
    }
}
