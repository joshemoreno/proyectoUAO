package com.example.proyectouao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectouao.model._Offer;
import com.example.proyectouao.model._Shopping;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OfferDetail extends AppCompatActivity {

    private TextView tvName, tvDescription, tvQuantity, tvPrice, tv_startDate, tv_endDate, tvLocation;
    private ImageView imageDetail, addCart;
    DecimalFormat formater = new DecimalFormat("###,###.##");
    private Activity mySelf;
    private int cant;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        _Shopping currentShopping=new _Shopping();

        mySelf = this;
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String amount = intent.getStringExtra("amount");
        String price = intent.getStringExtra("price");
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");
        String url = intent.getStringExtra("url");
        String id = intent.getStringExtra("id");
        String selectSide = intent.getStringExtra("selectSide");
        String status = intent.getStringExtra("status");

        currentShopping.setId(id);
        currentShopping.setComboTitle(name);
        currentShopping.setPrice(price);
        currentShopping.setAmount(amount);

        tvName= findViewById(R.id.tv_name);
        tvDescription= findViewById(R.id.tv_description);
        tvQuantity= findViewById(R.id.tv_quantity);
        tvPrice= findViewById(R.id.tv_price);
        imageDetail = findViewById(R.id.imageDetail);
        tv_startDate = findViewById(R.id.tv_startDate);
        tv_endDate = findViewById(R.id.tv_endDate);
        tvLocation = findViewById(R.id.tv_location);
        addCart = findViewById(R.id.addCart);

        String[] start_Date = startDate.split(" ");
        String[] end_Date = endDate.split(" ");

        String concatStartDate = "From: "+start_Date[2]+"-"+start_Date[1]+"-"+start_Date[5]+" at "+start_Date[3];
        String concatEndDate = "Until: "+end_Date[2]+"-"+end_Date[1]+"-"+end_Date[5]+" at "+end_Date[3];

        tvName.setText(name);
        tvDescription.setText(description);
        tvPrice.setText("$"+formater.format(Integer.parseInt(price)));
        tvQuantity.setText(amount);
        tv_startDate.setText(concatStartDate);
        tv_endDate.setText(concatEndDate);
        Glide.with(this).load(url).into(imageDetail);

        db.collection("shopping")
                .document(id)
                .get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String count = (String) documentSnapshot.getData().get("count");
                            currentShopping.setCount(count);
                        }else{
                            currentShopping.setCount(String.valueOf(0));
                        }
                    }
                });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map = new Intent(mySelf, MapActivity.class);
                map.putExtra("selectSide", selectSide);
                startActivity(map);
            }
        });

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cant = Integer.parseInt(currentShopping.getCount());
                cant++;
                currentShopping.setCount(String.valueOf(cant));
                addProductCart(currentShopping);
            }
        });

    }

    private void addProductCart(_Shopping product) {

        db.collection("shopping")
                .document(product.getId())
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OfferDetail.this.getBaseContext(), R.string.msg_addProduct, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}