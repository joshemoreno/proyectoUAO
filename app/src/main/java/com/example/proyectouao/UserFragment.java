package com.example.proyectouao;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectouao.adapter.UserAdapter;
import com.example.proyectouao.model._User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserFragment extends Fragment {

    private RecyclerView rv_list;
    private ArrayList<_User> list;
    private FloatingActionButton btnAdd;
    private FirebaseFirestore db;
    private UserAdapter userAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user,container,false);
        rv_list = root.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("users");

        list = new ArrayList<>();

        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                    _User temp = document.toObject(_User.class);
                    Integer role = Integer.parseInt(temp.getSelectRole());
                    if(role!=0){
                        list.add(temp);
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                userAdapter = new UserAdapter(list,getActivity());
                userAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent act_goEdit = new Intent(getActivity(), EditUser.class);
                        act_goEdit.putExtra("name",list.get(rv_list.getChildAdapterPosition(view)).getNameUser());
                        act_goEdit.putExtra("lastName",list.get(rv_list.getChildAdapterPosition(view)).getLastNameUser());
                        act_goEdit.putExtra("faculty",list.get(rv_list.getChildAdapterPosition(view)).getFacultyUser());
                        act_goEdit.putExtra("site",list.get(rv_list.getChildAdapterPosition(view)).getSelectSite());
                        act_goEdit.putExtra("program",list.get(rv_list.getChildAdapterPosition(view)).getProgram());
                        act_goEdit.putExtra("role",list.get(rv_list.getChildAdapterPosition(view)).getSelectRole());
                        act_goEdit.putExtra("status",list.get(rv_list.getChildAdapterPosition(view)).getUserStatus());
                        act_goEdit.putExtra("email",list.get(rv_list.getChildAdapterPosition(view)).getEmail());
                        startActivity(act_goEdit);
                    }
                });
                rv_list.setAdapter(userAdapter);
            }
        });

        btnAdd = root.findViewById(R.id.btnAddProduct);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act_goAdd = new Intent(getActivity(), AddUser.class);
                startActivity(act_goAdd);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}