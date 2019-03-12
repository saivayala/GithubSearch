package com.sai8.githubsearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sai8.githubsearch.adapter.RecyclerAdapter;
import com.sai8.githubsearch.model.JobInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DisplayActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public static String linkurl;
    final int loderid = 1;
    RecyclerView rv;
    ProgressBar pb;
    TextView error, click;
    public static List<JobInfo> jobInfos;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        rv = findViewById(R.id.recyclerview);
        pb = findViewById(R.id.progresbar);
        error = findViewById(R.id.errortext);
        click = findViewById(R.id.clicktext);
        scrollView=findViewById(R.id.scrollview);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        if(checkInternetConnection()) {

            Intent intent = getIntent();
            String searchkey = intent.getStringExtra("searchkey");
            String baseurl = getString(R.string.baseurl);
            String endurl = getString(R.string.endurl);

            linkurl = baseurl + searchkey + endurl;
            if(savedInstanceState!=null){
                rv.setLayoutManager(new LinearLayoutManager(this));
                rv.setAdapter(new RecyclerAdapter(this, jobInfos));
                final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
                if (position != null) {
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(position[0], position[1]);
                        }
                    });
                }
            }
            else {
                jobInfos = new ArrayList<>();
                rv.setLayoutManager(new LinearLayoutManager(this));
                getSupportLoaderManager().initLoader(loderid, null, this);
                rv.setAdapter(new RecyclerAdapter(this, jobInfos));
            }
        }
        else{
            alert();
        }

    }
    public boolean checkInternetConnection() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    public  void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_signal_cellular_connected_no_internet_0_bar_black_24dp);
        builder.setTitle("Connection");
        builder.setMessage("please check internet connection");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("Scroll_position",new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {

                try {
                    Log.i("entered", "loadinbackground");
                    Log.i("urllink", linkurl);
                    URL url = new URL(linkurl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\A");
                    if (scanner.hasNext()) {
                        return scanner.next();
                    } else {
                        return null;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                pb.setVisibility(View.VISIBLE);
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        pb.setVisibility(View.GONE);
        if (s != null) {

            try {
                jobInfos.clear();
                Log.i("jsondata", s);
                Log.i("entered", "entered Loadfinished");
                JSONArray main = new JSONArray(s);
                if (main.length() > 0) {
                    for (int i = 0; i < main.length(); i++) {
                        JSONObject jobObject = main.getJSONObject(i);
                        String title = jobObject.getString("title");
                        Log.i("name", title);
                        String locattion = jobObject.getString("location");
                        String link = jobObject.getString("how_to_apply");
                        String des = jobObject.getString("description");
                        String imageurl=jobObject.getString("company_logo");

                        JobInfo model = new JobInfo(title, locattion, link, des,imageurl);

                        jobInfos.add(model);
                    }
                }else{
                    error.setVisibility(View.VISIBLE);
                    click.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rv.setAdapter(new RecyclerAdapter(this, jobInfos));

        } else {
            error.setVisibility(View.VISIBLE);
            click.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
