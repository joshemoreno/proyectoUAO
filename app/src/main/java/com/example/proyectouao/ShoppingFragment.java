package com.example.proyectouao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.proyectouao.adapter.ShoppingAdapter;
import com.example.proyectouao.model._Shopping;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingFragment extends Fragment {

    private ArrayList<_Shopping> list;
    private ArrayList<String> ids;
    private FirebaseFirestore db;
    private RecyclerView rv_list2;
    private Button btn_buy;
    private ShoppingAdapter shoppingAdapter;
    private Integer total=0;
    private TextView tv_price;
    private LinearLayout linear_empty;
    private FirebaseAuth mAuth;
    DecimalFormat formater = new DecimalFormat("###,###.##");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        View root = inflater.inflate(R.layout.fragment_shopping, container, false);
        rv_list2 = root.findViewById(R.id.rv_list2);
        btn_buy = root.findViewById(R.id.btn_buy);
        tv_price = root.findViewById(R.id.item_price);
        linear_empty = root.findViewById(R.id.linear_empty);
        TextView tv_total = root.findViewById(R.id.itemsTotal);
        rv_list2.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        ids = new ArrayList<>();

        CollectionReference reference = db.collection("shopping");
        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                    _Shopping temp = document.toObject(_Shopping.class);
                    ids.add(temp.getId());
                    list.add(temp);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                shoppingAdapter = new ShoppingAdapter(list,getActivity());
                for (int i = 0; i<list.size();i++) {
                    total+= Integer.parseInt(list.get(i).getPrice())*Integer.parseInt(list.get(i).getCount());
                }
                tv_total.setText("Total: $" + formater.format(total));
                rv_list2.setAdapter(shoppingAdapter);

                if (list.size()==0){
                    btn_buy.setVisibility(View.GONE);
                    tv_total.setVisibility(View.GONE);
                    linear_empty.setVisibility(View.VISIBLE);
                }else{
                    btn_buy.setVisibility(View.VISIBLE);
                }

                btn_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < ids.size(); i++) {
                            int newVal = 0;
                            int cantIni = Integer.parseInt(list.get(i).getAmount());
                            int count = Integer.parseInt(list.get(i).getCount());
                            newVal = cantIni - count;
                            Map<String, Object> product = new HashMap<>();
                            product.put("amount", String.valueOf(newVal));
                            int finalI = i;
                            db.collection("offers")
                                .document(ids.get(i))
                                .update(product)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String currentUser = mAuth.getCurrentUser().getEmail();
                                        Map<String, Object> item = new HashMap<>();
                                        item.put("product", list.get(finalI).getComboTitle());
                                        item.put("count", String.valueOf(count));
                                        item.put("client", currentUser);
                                        db.collection("shoppingList")
                                                .add(item)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        db.collection("shopping")
                                                                .document(ids.get(finalI))
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                                        builder.setTitle(R.string.titleMap);
                                                                        builder.setIcon(R.drawable.info);
                                                                        builder.setMessage(R.string.msg_shopping);
                                                                        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                                Intent act_goUser = new Intent(getActivity(), DashBoard.class);
                                                                                act_goUser.putExtra("email",currentUser);
                                                                                startActivity(act_goUser);
                                                                            }
                                                                        });
                                                                        AlertDialog dialog = builder.create();
                                                                        dialog.show();
                                                                    }
                                                            });
                                                    }
                                            });
                                    }
                            });
                        }
                    }
                });
            }
        });
        return root;
    }
}