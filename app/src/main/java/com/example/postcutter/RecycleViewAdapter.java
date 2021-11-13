package com.example.postcutter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        picasso.load("file:" + imagePathArrayList.get(position)).into(holder.imageIV);
        holder.imageIV.setOnClickListener(e -> openPicture(position));
    }

    private void openPicture(int position) {
        Intent i = new Intent(context, ImageDetaylActivity.class);
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
