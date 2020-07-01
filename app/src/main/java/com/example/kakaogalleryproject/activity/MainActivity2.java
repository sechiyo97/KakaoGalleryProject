package com.example.kakaogalleryproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kakaogalleryproject.R;
import com.example.kakaogalleryproject.adapter.ImageAdapter;
import com.example.kakaogalleryproject.adapter.ImgInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity2 extends AppCompatActivity {
    private ProgressBar mProgress;
    private Disposable downloadTask;

    private LinearLayout sortIndexBtn;
    private LinearLayout sortAtoZBtn;
    private LinearLayout sortDateBtn;
    private ImageView showListBtn;
    private ImageView showGridBtn;

    protected ArrayList<ImgInfo> imageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager llManager;
    private RecyclerView.LayoutManager glManager;

    private String BASE_URL = "https://www.gettyimagesgallery.com/collection/sasha";

    public void showSplash(){
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showSplash();

        setContentView(R.layout.activity_main);

        sortIndexBtn = findViewById(R.id.sort_index_btn);
        sortAtoZBtn = findViewById(R.id.sort_atoz_btn);
        sortDateBtn = findViewById(R.id.sort_date_btn);
        showListBtn = findViewById(R.id.show_list_btn);
        showGridBtn = findViewById(R.id.show_grid_btn);

        mProgress = findViewById(R.id.progress_bar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // use a staggerd grid layout manager
        glManager = new GridLayoutManager(this, 3);
        llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);

        // adapter
        mAdapter = new ImageAdapter(imageList);
        recyclerView.setAdapter(mAdapter);
        DownloadTask();
    }


    void DownloadTask() {
        // onPreExecute
        mProgress.setVisibility(View.VISIBLE);

        downloadTask = Observable.fromCallable(() -> {

            // onBackground
            Document doc = Jsoup.connect(BASE_URL).timeout(5000).get();
            String imageSelector = ".jq-lazy";
            Elements images = doc.select(imageSelector); // get images
            for (int i=0;i<images.size();i++) {
                Log.e(i+"", images.get(i)+"");
                Element image = images.get(i);
                String href = image.attributes().get("data-src");
                String date = getDateFromHref(href);
                String name = image.attributes().get("alt");
                ImgInfo imgInfo = new ImgInfo(i, href, date, name);
                imageList.add(imgInfo);
            }
            return false;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {

                    // onPostExecute
                    mAdapter.notifyDataSetChanged(); // dataset changed
                    mProgress.setVisibility(View.GONE);

                    downloadTask.dispose();

                    // set onClickListener after showing all images
                    sortIndexBtn.setOnClickListener(v->{
                        sortImgList(1);
                        sortIndexBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        sortAtoZBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        sortDateBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    });
                    sortAtoZBtn.setOnClickListener(v->{
                        sortImgList(2);
                        sortIndexBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        sortAtoZBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        sortDateBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    });
                    sortDateBtn.setOnClickListener(v->{
                        sortImgList(3);
                        sortIndexBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        sortAtoZBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        sortDateBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    });
                    showListBtn.setOnClickListener(v->{
                        recyclerView.setLayoutManager(llManager);
                        showListBtn.setAlpha(1.0f);
                        showGridBtn.setAlpha(0.2f);
                    });
                    showGridBtn.setOnClickListener(v->{
                        recyclerView.setLayoutManager(glManager);
                        showListBtn.setAlpha(0.2f);
                        showGridBtn.setAlpha(1.0f);
                    });

                });
    }

    private String getDateFromHref(String href){
        return href.substring(54,61);
    }

    void sortImgList(int method) {
        switch(method){
            case 1: // sort by index
                Collections.sort(imageList, (a, b) -> a.getOrgIdx() - b.getOrgIdx());
                break;
            case 2: // sort A to Z
                Collections.sort(imageList, (a, b) -> a.getName().compareTo(b.getName()));
                break;
            case 3: // sorte first by Date, and then A to Z
                Collections.sort(imageList, (a, b) -> {
                    int dateComp = b.getDate().compareTo(a.getDate());
                    int nameComp = a.getName().compareTo(b.getName());
                    if (dateComp!=0) return dateComp;
                    else return nameComp;
                });
                break;
        }
        mAdapter.notifyDataSetChanged();
    }
}
