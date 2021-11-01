package com.example.proyectouao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

public class OfferDetail extends AppCompatActivity {

    private TextView tvName, tvDescription, tvQuantity, tvPrice, tv_startDate, tv_endDate;
    private ImageView imageDetail;
    DecimalFormat formater = new DecimalFormat("###,###.##");
    private Activity mySelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

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

        Log.i("key",""+id);
        Log.i("key","entro");

        tvName= findViewById(R.id.tv_name);
        tvDescription= findViewById(R.id.tv_description);
        tvQuantity= findViewById(R.id.tv_quantity);
        tvPrice= findViewById(R.id.tv_price);
        imageDetail = findViewById(R.id.imageDetail);
        tv_startDate = findViewById(R.id.tv_startDate);
        tv_endDate = findViewById(R.id.tv_endDate);

        tvName.setText(name);
        tvDescription.setText(description);
        tvPrice.setText("$"+formater.format(Integer.parseInt(price)));
        tvQuantity.setText(amount);
        tv_startDate.setText(startDate);
        tv_endDate.setText(endDate);
        Glide.with(this).load(url).into(imageDetail);
    }
}