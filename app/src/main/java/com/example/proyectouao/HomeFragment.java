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
import android.widget.TextView;

import com.example.proyectouao.adapter.HomeAdapter;
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
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private RecyclerView rv_list;
    private ArrayList<_Offer> list;
    private FloatingActionButton btnAdd;
    private FirebaseFirestore db;
    private HomeAdapter homeAdapter;
    private TextView tv_empty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        rv_list = root.findViewById(R.id.rv_list);
        tv_empty = root.findViewById(R.id.tv_empty);
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
                    Integer status = Integer.parseInt(temp.getStatus());
                    Integer count = Integer.parseInt(temp.getAmount());
                    Date startDate = new Date(temp.getStartDate());
                    Date endDate = new Date(temp.getEndDate());
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)- 5);
                    Date currentDate = cal.getTime();
                    if(status!=0){
                        if(count!=0){
                            if(currentDate.after(startDate)&&currentDate.before(endDate)){
                                temp.setId(id);
                                list.add(temp);
                            }
                        }else{
                            updateOffer(temp,id);
                        }
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(list.size()==0){
                    tv_empty.setVisibility(View.VISIBLE);
                }
                homeAdapter = new HomeAdapter(list,getActivity());
                homeAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent act_goDetail = new Intent(getActivity(), OfferDetail.class);
                        act_goDetail.putExtra("name",list.get(rv_list.getChildAdapterPosition(view)).getComboTitle());
                        act_goDetail.putExtra("description",list.get(rv_list.getChildAdapterPosition(view)).getComboDescription());
                        act_goDetail.putExtra("amount",list.get(rv_list.getChildAdapterPosition(view)).getAmount());
                        act_goDetail.putExtra("price",list.get(rv_list.getChildAdapterPosition(view)).getPrice());
                        act_goDetail.putExtra("startDate",list.get(rv_list.getChildAdapterPosition(view)).getStartDate());
                        act_goDetail.putExtra("endDate",list.get(rv_list.getChildAdapterPosition(view)).getEndDate());
                        act_goDetail.putExtra("url",list.get(rv_list.getChildAdapterPosition(view)).getUrl());
                        act_goDetail.putExtra("id",list.get(rv_list.getChildAdapterPosition(view)).getId());
                        act_goDetail.putExtra("selectSide",list.get(rv_list.getChildAdapterPosition(view)).getSelectSide());
                        act_goDetail.putExtra("status",list.get(rv_list.getChildAdapterPosition(view)).getStatus());
                        startActivity(act_goDetail);
                    }
                });
                rv_list.setAdapter(homeAdapter);
            }
        });

        return root;
    }

    private void updateOffer(_Offer offer, String id) {
        offer.setStatus("0");
        db.collection("offers")
                .document(id)
                .set(offer);
    }
}