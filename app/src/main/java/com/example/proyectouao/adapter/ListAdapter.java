package com.example.proyectouao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectouao.R;
import com.example.proyectouao.model._ShoppingList;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    ArrayList<_ShoppingList> items;
    Context context;
    DecimalFormat formater = new DecimalFormat("###,###.##");

    public ListAdapter(ArrayList<_ShoppingList> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ListAdapter.ViewHolder holder, int position) {
        String count = items.get(position).getCount();
        holder.tv_name.setText(items.get(position).getClient());
        holder.tv_cant.setText("X "+count);
        holder.tv_price.setText(items.get(position).getProduct());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_cant, tv_price;

        public ViewHolder(View v) {
            super(v);
            tv_name = v.findViewById(R.id.item_name);
            tv_cant = v.findViewById(R.id.item_cant);
            tv_price = v.findViewById(R.id.item_price);
        }
    }
}
