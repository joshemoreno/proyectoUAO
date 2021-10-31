package com.example.proyectouao;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectouao.adapter.SideAdapter;
import com.example.proyectouao.adapter.UserAdapter;
import com.example.proyectouao.model._Side;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SideFragment extends Fragment {

    private RecyclerView rv_list;
    private ArrayList<_Side> list;
    private FloatingActionButton btnAdd;
    private FirebaseFirestore db;
    private SideAdapter sideAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_side, container, false);

        rv_list = root.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("sides");

        list = new ArrayList<>();

        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                    _Side temp = document.toObject(_Side.class);
                    String id = document.getId();
                    temp.setId(id);
                    list.add(temp);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                sideAdapter = new SideAdapter(list,getActivity());
                sideAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent act_goDetail = new Intent(getActivity(), MapActivity.class);
                        act_goDetail.putExtra("latitude",list.get(rv_list.getChildAdapterPosition(view)).getLatitude());
                        act_goDetail.putExtra("longitude",list.get(rv_list.getChildAdapterPosition(view)).getLongitude());
                        act_goDetail.putExtra("nameSide",list.get(rv_list.getChildAdapterPosition(view)).getNameSide());
                        act_goDetail.putExtra("description",list.get(rv_list.getChildAdapterPosition(view)).getDescription());
                        act_goDetail.putExtra("id",list.get(rv_list.getChildAdapterPosition(view)).getId());
                        startActivity(act_goDetail);
                    }
                });
                rv_list.setAdapter(sideAdapter);
            }
        });

        btnAdd = root.findViewById(R.id.btnAddSide);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act_goAdd = new Intent(getActivity(), MapActivity.class);
                startActivity(act_goAdd);
            }
        });
        return root;
    }
}