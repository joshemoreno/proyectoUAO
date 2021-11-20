package com.example.proyectouao.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectouao.R;
import com.example.proyectouao.model._Side;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SideAdapter extends RecyclerView.Adapter<SideAdapter.ViewHolder> implements  View.OnClickListener{
    ArrayList<_Side> sides;
    Context context;
    private View.OnClickListener listener;

    public SideAdapter(ArrayList<_Side> sides, Context context){
        this.sides = sides;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SideAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        v.setOnClickListener(this);
        return new SideAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SideAdapter.ViewHolder holder, int position){
        holder.nameSide.setText(sides.get(position).getNameSide());
        holder.description.setText(sides.get(position).getDescription());
        holder.image.setImageResource(R.drawable.location);
        holder.status.setText(">      ");
    }

    @Override
    public int getItemCount() {
        return sides.size();
    }

    public void setOnClickListener (View.OnClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameSide, description, status;
        private ImageView image;
        public ViewHolder(View v) {
            super(v);
            nameSide = v.findViewById(R.id.nameCart);
            description = v.findViewById(R.id.userRole);
            image = v.findViewById(R.id.imageCart);
            status = v.findViewById(R.id.statusUserCart);
        }
    }
}
