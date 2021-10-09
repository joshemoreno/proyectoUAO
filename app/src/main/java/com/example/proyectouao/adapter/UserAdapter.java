package com.example.proyectouao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectouao.R;
import com.example.proyectouao.model._User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> implements  View.OnClickListener{

    ArrayList<_User> users;
    Context context;
    private View.OnClickListener listener;

    public UserAdapter(ArrayList<_User> users, Context context){
        this.users = users;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position){

            holder.name.setText(users.get(position).getNameUser()+" "+users.get(position).getLastNameUser());
            String roleUser = "";
            switch (users.get(position).getSelectRole()){
                case "1":
                    roleUser = "Teacher";
                    break;
                case "2":
                    roleUser = "Student";
                    break;
            }
            holder.role.setText(roleUser);
            String statusUser = "";
            switch (users.get(position).getUserStatus()){
                case "1":
                    statusUser = "Active";
                    break;
                case "2":
                    statusUser = "Inactive";
                    break;
            }
            holder.status.setText(statusUser);
        }

    @Override
    public int getItemCount() {
        return users.size();
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
        private TextView name, role, status;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.nameCart);
            role = v.findViewById(R.id.userRole);
            status = v.findViewById(R.id.statusUserCart);
        }
    }

}
