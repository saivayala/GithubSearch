package com.sai8.githubsearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText ed;
    Button bt;
    String text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed=findViewById(R.id.etsearch);
        bt=findViewById(R.id.buttonsearch);
        if(checkInternetConnection()){

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text=ed.getText().toString();
                if(checkInternetConnection()){
                if(!text.isEmpty()){
                    Intent intent=new Intent(MainActivity.this,DisplayActivity.class);
                    intent.putExtra("searchkey",ed.getText().toString());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "enter value for search", Toast.LENGTH_SHORT).show();
                }
                }else {
                    alert();
                }

            }
        });
    }
    else {
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


}
