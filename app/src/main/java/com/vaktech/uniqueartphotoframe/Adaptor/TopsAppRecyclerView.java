package com.vaktech.uniqueartphotoframe.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaktech.uniqueartphotoframe.Activity.AppHubActivity;
import com.vaktech.uniqueartphotoframe.R;
import com.vaktech.uniqueartphotoframe.Response.AppOnlineDialogResponse;

import java.util.List;

/**
 * Created by vaksys-android-52 on 3/8/17.
 */

public class TopsAppRecyclerView extends RecyclerView.Adapter<TopsAppRecyclerView.MyViewHolder> {

    Context context;
    List<AppOnlineDialogResponse.AppList> row;

    public TopsAppRecyclerView(AppHubActivity appHubActivity, List<AppOnlineDialogResponse.AppList> data) {
        this.context=appHubActivity;
        this.row=data;
    }

    @Override
    public TopsAppRecyclerView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_up,parent,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopsAppRecyclerView.MyViewHolder holder, final int position) {
        Glide.with(context).load(row.get(position).getImage()).into(holder.img1);
        holder.txt1.setText(row.get(position).getName());
        holder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });
        holder.lvup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (android.content.ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(row.get(position).getLink()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return row.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lvup;
        ImageView img1;
        TextView txt1;
        Button btn1;
        public MyViewHolder(View itemView) {
            super(itemView);
            lvup= (LinearLayout) itemView.findViewById(R.id.lvup);
            img1= (ImageView) itemView.findViewById(R.id.myimg1);
            txt1= (TextView) itemView.findViewById(R.id.mytxt1);
            btn1= (Button) itemView.findViewById(R.id.button);
        }
    }
}
