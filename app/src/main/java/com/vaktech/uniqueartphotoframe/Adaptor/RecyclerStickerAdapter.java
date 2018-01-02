package com.vaktech.uniqueartphotoframe.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vaktech.uniqueartphotoframe.Activity.EditScreenActivity;
import com.vaktech.uniqueartphotoframe.R;

import java.util.ArrayList;

/**
 * Created by vaksys-android-52 on 8/8/17.
 */

public class RecyclerStickerAdapter extends RecyclerView.Adapter<RecyclerStickerAdapter.MyViewHolder> {
    Context editImage;
    Context context;
    private ArrayList<Integer> stickerImagelist;

    public RecyclerStickerAdapter(EditScreenActivity editScreenActivity, ArrayList<Integer> stickerImageList) {
        this.context=editImage;
        this.stickerImagelist=stickerImageList;
    }

    @Override
    public RecyclerStickerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewGroup group = (ViewGroup) layoutInflater.inflate(R.layout.recycler_sticker_adapter, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(group);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerStickerAdapter.MyViewHolder holder, int position) {
        final Integer name = stickerImagelist.get(position);
        holder.recyclerImgItem.setBackgroundResource(name);
    }

    @Override
    public int getItemCount() {
        return stickerImagelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView recyclerImgItem;
        private ItemClickListener mListener;

        public MyViewHolder(View itemView) {
            super(itemView);
            recyclerImgItem = (ImageView)itemView.findViewById(R.id.recycler_item_img);
            itemView.setOnClickListener(this);
        }

        public void setClickListener(ItemClickListener listener) {
            this.mListener = listener;
        }
        @Override
        public void onClick(View v) {

        }
    }
    public interface ItemClickListener {
        void onClickItem(int pos);
    }
}
