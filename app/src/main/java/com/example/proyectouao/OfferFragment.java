package com.example.proyectouao;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectouao.adapter.OfferAdapter;
import com.example.proyectouao.model._Offer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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