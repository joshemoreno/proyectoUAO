package com.example.proyectouao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.proyectouao.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectouao.model._Shopping;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder>{
    ArrayList<_Shopping> products;
    Context context;
    DecimalFormat formater = new DecimalFormat("###,###.##");

    public ShoppingAdapter(ArrayList<_Shopping> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ShoppingAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ShoppingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ShoppingAdapter.ViewHolder holder, int position) {
        Integer count = Integer.parseInt(products.get(position).getCount());
        Integer price = Integer.parseInt(products.get(position).getPrice())*count;
        holder.tv_name.setText(products.get(position).getComboTitle());
        holder.tv_cant.setText("X "+count);
        holder.tv_price.setText("$" + formater.format(price));
    }

    @Override
    public int getItemCount() {
        return products.size();
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