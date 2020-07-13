package com.example.gallaryapplication.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gallaryapplication.R;
import com.example.gallaryapplication.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class GallaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ImageModel> imageList;
    public GallaryAdapter(ArrayList<ImageModel> imageList)
    {
        this.imageList =imageList;
    }
    public GallaryAdapter()
    {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {

            case 0:
                 view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_header_layout, parent, false);
                 return new GalleryViewHolder2(view);
            case 1:
                 view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_layout, parent, false);
                 return new GalleryViewHolder(view);
        }
        return  null;
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        Log.i("INside BindViewHolder",holder.getItemViewType()+"");
        if (holder.getItemViewType()==0)
        {

                GalleryViewHolder2 holder2 = (GalleryViewHolder2)holder;
                holder2.textView.setText(imageList.get(position).getDateHeader());
        }
        else
        {
            Bitmap bm = imageList.get(position).getBitmap();
            GalleryViewHolder holder1 = (GalleryViewHolder)holder;
            holder1.imgView.setImageBitmap(bm);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (imageList.get(position).getIsHeader())
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgView;
        public GalleryViewHolder(View view) {
            super(view);
            imgView =  view.findViewById(R.id.img);
        }

    }

    public class GalleryViewHolder2 extends RecyclerView.ViewHolder{
        private TextView textView;
        public GalleryViewHolder2(View view) {
            super(view);
            textView =  view.findViewById(R.id.tv_date);
        }

    }

    public boolean isHeader(int position) {
        return imageList.get(position).getIsHeader();
    }
}
