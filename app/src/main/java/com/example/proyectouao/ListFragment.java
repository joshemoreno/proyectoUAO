package com.example.proyectouao;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.proyectouao.adapter.ListAdapter;
import com.example.proyectouao.model._Shopping;
import com.example.proyectouao.model._ShoppingList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListFragment extends Fragment {

    private ArrayList<_ShoppingList> list;
    private ArrayList<String> ids;
    private FirebaseFirestore db;
    private RecyclerView rv_list3;
    private Button btn_buy;
    private ListAdapter listAdapter;
    private Integer total=0;
    private TextView tv_price;
    private LinearLayout linear_empty;
    private FirebaseAuth mAuth;
    DecimalFormat formater = new DecimalFormat("###,###.##");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        View root = inflater.inflate(R.layout.fragment_list, container, false);
        rv_list3 = root.findViewById(R.id.rv_list3);
        rv_list3.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();

        CollectionReference reference = db.collection("shoppingList");
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                    _ShoppingList temp = document.toObject(_ShoppingList.class);
                    list.add(temp);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                listAdapter = new ListAdapter(list, getActivity());
                rv_list3.setAdapter(listAdapter);
            }
        });

        return root;
    }
}