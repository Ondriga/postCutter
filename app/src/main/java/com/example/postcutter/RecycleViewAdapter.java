package com.example.postcutter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postcutter.functions.ImageAction;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.RecyclerViewHolder> {
    private final Context context;
    private final List<String> imagePathArrayList;
    private final Picasso picasso;

    public RecycleViewAdapter(Context context, List<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
        picasso = Picasso.get();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        long id = ContentUris.parseId(ImageAction.getUriFromRealPath(context, imagePathArrayList.get(position)));
        ContentResolver cr = context.getContentResolver();
        Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND, null);
        holder.imageIV.setImageBitmap(miniThumb);
        holder.imageIV.setOnClickListener(e -> openPicture(position));
    }

    private void openPicture(int position) {
        Intent i = new Intent(context, ImageDetailActivity.class);
        i.putExtra("imagePath", imagePathArrayList.get(position));
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return imagePathArrayList.size();
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageIV;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.idIVImage);
        }
    }
}
