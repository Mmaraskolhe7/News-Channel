package com.manoj.newschannels;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

//Response of the onclick on Recyclerview Item

public class ReadMore extends AppCompatActivity {
public ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more);

        progressBar = findViewById(R.id.progress);
        swipeRefreshLayout = findViewById(R.id.swipe);


        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");
        final WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                         progressBar.setVisibility(View.VISIBLE);
                                         setTitle("Loading...");

                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         progressBar.setVisibility(View.GONE);
                                         setTitle(view.getTitle());
                                     }
                                 }

        );
        webView.loadUrl(url);

        //If refresh

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    webView.loadUrl(url);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },4000);
                }
            });

    }
}
