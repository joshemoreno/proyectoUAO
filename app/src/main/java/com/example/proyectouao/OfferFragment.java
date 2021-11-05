package com.example.proyectouao;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectouao.adapter.OfferAdapter;
import com.example.proyectouao.adapter.SideAdapter;
import com.example.proyectouao.model._Offer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OfferFragment extends Fragment {

    private RecyclerView rv_list;
    private ArrayList<_Offer> list;
    private FloatingActionButton btnAdd;
    private FirebaseFirestore db;
    private OfferAdapter offerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offer, container, false);

        rv_list = root.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("offers");

        list = new ArrayList<>();

        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                    _Offer temp = document.toObject(_Offer.class);
                    String id = document.getId();
                    temp.setId(id);
                    list.add(temp);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                offerAdapter = new OfferAdapter(list,getActivity());
                offerAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent act_goDetail = new Intent(getActivity(), EditOffer.class);
                        act_goDetail.putExtra("name",list.get(rv_list.getChildAdapterPosition(view)).getComboTitle());
                        act_goDetail.putExtra("description",list.get(rv_list.getChildAdapterPosition(view)).getComboDescription());
                        act_goDetail.putExtra("amount",list.get(rv_list.getChildAdapterPosition(view)).getAmount());
                        act_goDetail.putExtra("price",list.get(rv_list.getChildAdapterPosition(view)).getPrice());
                        act_goDetail.putExtra("startDate",list.get(rv_list.getChildAdapterPosition(view)).getStartDate());
                        act_goDetail.putExtra("endDate",list.get(rv_list.getChildAdapterPosition(view)).getEndDate());
                        act_goDetail.putExtra("url",list.get(rv_list.getChildAdapterPosition(view)).getUrl());
                        act_goDetail.putExtra("id",list.get(rv_list.getChildAdapterPosition(view)).getId());
                        act_goDetail.putExtra("selectSide",list.get(rv_list.getChildAdapterPosition(view)).getSelectSide());
                        startActivity(act_goDetail);
                    }
                });
                rv_list.setAdapter(offerAdapter);
            }
        });


        btnAdd = root.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent act_goAdd = new Intent(getActivity(), AddOffer.class);
                startActivity(act_goAdd);
            }
        });

        return root;
    }
}