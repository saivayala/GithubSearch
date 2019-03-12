package com.sai8.githubsearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sai8.githubsearch.DisplayActivity;
import com.sai8.githubsearch.R;
import com.sai8.githubsearch.model.JobInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    Context context;
    List<JobInfo> jobInfos;
    public RecyclerAdapter(DisplayActivity displayActivity, List<JobInfo> jobInfos) {
        this.context=displayActivity;
        this.jobInfos=jobInfos;
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.row_design,viewGroup,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder myViewHolder, int i) {

        JobInfo model=jobInfos.get(i);

        myViewHolder.name.setText(model.getTitle());
       // Toast.makeText(context, ""+model.getTitle()+"\n"+model.getDes(), Toast.LENGTH_SHORT).show();
        myViewHolder.location.setText(model.getLocation());
        myViewHolder.link.setText(model.getUrl());
        Picasso.with(context).load(model.getImageurl()).placeholder(R.drawable.ic_launcher_foreground).into(myViewHolder.iv);


    }

    @Override
    public int getItemCount() {
        return jobInfos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,location,link;
        ImageView iv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.tvjob);
            location=itemView.findViewById(R.id.tvlocation);
            link=itemView.findViewById(R.id.tvapply);
            iv=itemView.findViewById(R.id.image);
        }
    }
}
