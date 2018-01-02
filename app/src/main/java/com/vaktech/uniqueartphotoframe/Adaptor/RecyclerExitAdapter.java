package com.vaktech.uniqueartphotoframe.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vaktech.uniqueartphotoframe.Activity.MainActivity;
import com.vaktech.uniqueartphotoframe.R;
import com.vaktech.uniqueartphotoframe.Response.AppOnlineDialogResponse;

import java.util.List;

/**
 * Created by vaksys-android-52 on 1/8/17.
 */

public class RecyclerExitAdapter extends RecyclerView.Adapter<RecyclerExitAdapter.MyviewHolder> {
    Context context;
    List<AppOnlineDialogResponse.AppList> row;

    public RecyclerExitAdapter(MainActivity mainActivity, List<AppOnlineDialogResponse.AppList> data) {
        this.context=mainActivity;
        this.row=data;
    }


    @Override
    public RecyclerExitAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleritem, parent, false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerExitAdapter.MyviewHolder holder, final int position) {

        Glide.with(context).load(row.get(position).getImage()).into(holder.appImages);
        holder.appsName.setText(row.get(position).getName());
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        animation.setDuration(500);
        holder.itemView.startAnimation(animation);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    public class MyviewHolder extends RecyclerView.ViewHolder {
        private final ImageView appImages;
        private final TextView appsName;
        public MyviewHolder(View itemView) {
            super(itemView);
            appImages = (ImageView) itemView.findViewById(R.id.apps_imgs_iv);
            appsName = (TextView) itemView.findViewById(R.id.apps_name_tv);
        }
    }
}
