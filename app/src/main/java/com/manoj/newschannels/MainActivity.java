package com.manoj.newschannels;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private DrawerLayout drawerLayout;
    public ActionBarDrawerToggle toggle;
    String category;

    int count;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    public Adapter adapter;
    public List<Sources> sources = new ArrayList<>();
    List<Sources> rsources = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;

   ConnectivityManager connectivityManager;
    public static boolean flag;

    JSONresponse jsoNresponse;
    String result;
    public static final String API_KEY = "61d282d944884bc19cee1e41c9722564";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

       count =0;

       //to maintain the layout of Recyclerview for swipe refresh layout

        adapter = new Adapter(sources, getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout = findViewById(R.id.swipe1);

        //To enable drawerlayout and to access navigation slidebar

        drawerLayout = findViewById(R.id.main);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        //To chech network connection

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {

            flag = true;
        } else {

            flag = false;
            Toast.makeText(this, "You are Offline", Toast.LENGTH_SHORT).show();
        }

        //To get the respone from website by AsunkTask

        if (flag) {
        jsoNresponse = new JSONresponse();
        DownloadClass task = new DownloadClass();
        result = null;


            try {
                
                result = task.execute("https://newsapi.org/v2/sources?apiKey=" + API_KEY).get();

            } catch (ExecutionException e) {

                e.printStackTrace();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "oops! No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        convert();


//To refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                Toast.makeText(MainActivity.this, "Refreshing ......", Toast.LENGTH_SHORT).show();

                ConnectivityManager  connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
                    try {jsoNresponse = new JSONresponse();
                        DownloadClass task = new DownloadClass();
                        result = null;

                        result = task.execute("https://newsapi.org/v2/sources?apiKey=" + API_KEY).get();

                    } catch (ExecutionException e) {

                        e.printStackTrace();
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "You Are offline", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Swipe down to refresh", Toast.LENGTH_SHORT).show();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        convert();
                    }
                },4000);
            }
        });


     //To listen to on click on recyclerview
        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent intent = new Intent(getApplicationContext(),ReadMore.class);
                        if(count==0)//if no category is chosen
                        intent.putExtra("url",sources.get(position).getUrl());
                        else     intent.putExtra("url",rsources.get(position).getUrl());//if any category is chosen
                        startActivity(intent);
                    }
                });


    }

    //TO enable navigation view

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (toggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//If any navigation item selected .. For response

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        category = menuItem.getTitle().toString().toLowerCase();
        drawerLayout.closeDrawer(Gravity.START, false);
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        if(category.equals("all")){
            adapter = new Adapter(sources, getApplicationContext());
            count =0;
        }
        //If any category is Chosen
        else {

            count=1;
            rsources.clear();
            for (int i = 0; i < sources.size(); i++) {
                if (category.equals(sources.get(i).getCategory())) {
                    rsources.add(new Sources(sources.get(i).getName(), sources.get(i).getDescription(), sources.get(i).getUrl(), sources.get(i).getCategory(), sources.get(i).getLanguage()));
                }
            }

            adapter = new Adapter(rsources, getApplicationContext());

        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return false;
    }


    //To store the response from website into Recyclerview
    public void convert (){

        if (result != null) {
            sources.clear();
            sources = jsoNresponse.response(result);
            adapter = new Adapter(sources, getApplicationContext());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }




}
