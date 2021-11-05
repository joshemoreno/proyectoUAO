package com.example.proyectouao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OfferDetail extends AppCompatActivity {

    private TextView tvName, tvDescription, tvQuantity, tvPrice, tv_startDate, tv_endDate, tvLocation;
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
        String selectSide = intent.getStringExtra("selectSide");

        tvName= findViewById(R.id.tv_name);
        tvDescription= findViewById(R.id.tv_description);
        tvQuantity= findViewById(R.id.tv_quantity);
        tvPrice= findViewById(R.id.tv_price);
        imageDetail = findViewById(R.id.imageDetail);
        tv_startDate = findViewById(R.id.tv_startDate);
        tv_endDate = findViewById(R.id.tv_endDate);
        tvLocation = findViewById(R.id.tv_location);

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

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map = new Intent(mySelf, MapActivity.class);
                map.putExtra("selectSide", selectSide);
                startActivity(map);
            }
        });
    }
}