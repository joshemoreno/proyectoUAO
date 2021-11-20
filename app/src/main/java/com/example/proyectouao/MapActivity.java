package com.example.proyectouao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerView;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private FloatingActionButton btnSave;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoadinDialog loadinDialog = new LoadinDialog(MapActivity.this);
    private FirebaseAuth mAuth;
    private String currentUser;
    private Double latitudeG,longitudeG;
    private String nameSideG,descriptionG, idG;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        mAuth = FirebaseAuth.getInstance();


        mapView = findViewById(R.id.mapView);
        btnSave = findViewById(R.id.btnSaveSide);
        Intent intent = getIntent();
        String lat = intent.getStringExtra("latitude");
        String lon = intent.getStringExtra("longitude");
        String selectSide = intent.getStringExtra("selectSide");
        if(lat!=null||lon!=null){
            latitudeG = Double.parseDouble(lat);
            longitudeG = Double.parseDouble(lon);
            nameSideG = intent.getStringExtra("nameSide");
            descriptionG = intent.getStringExtra("description");
            idG = intent.getStringExtra("id");
        }else{
            latitudeG = 0.0;
            longitudeG = 0.0;
        }

        if (selectSide!=null){
            db.collection("sides").document(selectSide).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        btnSave.setVisibility(View.INVISIBLE);
                        Double localLat = Double.parseDouble((String) documentSnapshot.getData().get("latitude"));
                        Double localLong = Double.parseDouble((String) documentSnapshot.getData().get("longitude"));
                        String nameSide = (String) documentSnapshot.getData().get("nameSide");
                        String description = (String) documentSnapshot.getData().get("description");
                        mapView.onCreate(savedInstanceState);
                        mapView.getMapAsync(new OnMapReadyCallback() {

                            @Override
                            public void onMapReady(MapboxMap mapboxMap) {
                                CameraPosition position = new CameraPosition.Builder()
                                        .target(new LatLng(3.353802, -76.520742))
                                        .zoom(12)
                                        .tilt(60)
                                        .build();
                                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);

                                mapboxMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(localLat, localLong))
                                        .title(nameSide)
                                        .snippet(description)
                                );
                            }
                        });
                    }
                }
            });
        }else {
            if(latitudeG!=0.0||longitudeG!=0.0){
                mapView.onCreate(savedInstanceState);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(MapboxMap mapboxMap) {
                        btnSave.setImageResource(R.drawable.editar);
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(3.353802, -76.520742))
                                .zoom(12)
                                .tilt(60)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);

                        Marker marker = mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitudeG, longitudeG))
                                .title(nameSideG)
                                .snippet(descriptionG)
                        );

                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btnSave.setImageResource(R.drawable.save);

                                mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                                    Marker markerView;
                                    @Override
                                    public void onMapClick(@NonNull LatLng point) {

                                        Double latitude = point.getLatitude();
                                        Double longitude = point.getLongitude();

                                        if(latitude!=latitudeG || longitude!=longitudeG){
                                            mapboxMap.removeMarker(marker);
                                        }

                                        MarkerOptions newMarker = new MarkerOptions()
                                                .position(new LatLng(latitude,longitude));
//                                    if(markerView!=null){
//                                        mapboxMap.removeMarker(markerView);
//                                    }
                                        markerView = mapboxMap.addMarker(newMarker);
                                        btnSave.setVisibility(View.VISIBLE);

                                        btnSave.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                                                View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                                                Button btn_send = mView.findViewById(R.id.btn_okay);
                                                EditText et_name = mView.findViewById(R.id.et_name);
                                                EditText et_description = mView.findViewById(R.id.et_description);
                                                TextView tv_title = mView.findViewById(R.id.titleModal);
                                                tv_title.setText("Edit side");
                                                et_name.setText(nameSideG);
                                                et_description.setText(descriptionG);
                                                builder.setView(mView);
                                                AlertDialog alertDialog = builder.create();
                                                alertDialog.show();
                                                btn_send.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        String nameSide = et_name.getText().toString();
                                                        String description = et_description.getText().toString();

                                                        currentUser = mAuth.getCurrentUser().getEmail();
                                                        loadinDialog.startLoading();
                                                        createNewSide(idG, currentUser, nameSide, description, String.valueOf(latitude), String.valueOf(longitude));
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                                        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                                        Button btn_send = mView.findViewById(R.id.btn_okay);
                                        EditText et_name = mView.findViewById(R.id.et_name);
                                        EditText et_description = mView.findViewById(R.id.et_description);
                                        TextView tv_title = mView.findViewById(R.id.titleModal);
                                        tv_title.setText("Edit side");
                                        et_name.setText(nameSideG);
                                        et_description.setText(descriptionG);
                                        builder.setView(mView);
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                        btn_send.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String nameSide = et_name.getText().toString();
                                                String description = et_description.getText().toString();

                                                currentUser = mAuth.getCurrentUser().getEmail();
                                                loadinDialog.startLoading();
                                                createNewSide(idG, currentUser, nameSide, description, String.valueOf(latitudeG), String.valueOf(longitudeG));
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }else{
                mapView.onCreate(savedInstanceState);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.titleMap);
                builder.setIcon(R.drawable.info);
                builder.setPositiveButton(R.string.btn_ok,null);
                builder.setMessage(R.string.msg_starting);
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                        btnSave.setVisibility(View.INVISIBLE);
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(3.353802, -76.520742))
                                .zoom(12)
                                .tilt(60)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);

                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            Marker markerView;
                            @Override
                            public void onMapClick(@NonNull LatLng point) {

                                Double latitude = point.getLatitude();
                                Double longitude = point.getLongitude();

                                MarkerOptions newMarker = new MarkerOptions()
                                        .position(new LatLng(latitude,longitude));
                                if(markerView!=null){
                                    mapboxMap.removeMarker(markerView);
                                }
                                markerView = mapboxMap.addMarker(newMarker);
                                btnSave.setVisibility(View.VISIBLE);

                                btnSave.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                                        View mView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                                        Button btn_send = mView.findViewById(R.id.btn_okay);
                                        EditText et_name = mView.findViewById(R.id.et_name);
                                        EditText et_description = mView.findViewById(R.id.et_description);
                                        builder.setView(mView);
                                        final AlertDialog alertDialog = builder.create();
                                        btn_send.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String nameSide = et_name.getText().toString();
                                                String description = et_description.getText().toString();

                                                currentUser = mAuth.getCurrentUser().getEmail();
                                                loadinDialog.startLoading();
                                                createNewSide(null, currentUser, nameSide, description, String.valueOf(latitude), String.valueOf(longitude));
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }


    }

    public void createNewSide(String id, String currentUser, String name, String description, String latitude, String longitude){
        Map<String, Object> side = new HashMap<>();
        side.put("nameSide", name);
        side.put("latitude", latitude);
        side.put("longitude", longitude);
        side.put("description", description);

        if(id!=null){
            db.collection("sides")
                    .document(id)
                    .update(side)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadinDialog.dismissDialog();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                            builder.setTitle(R.string.msg_info);
                            builder.setMessage(R.string.msg_successSide2);
                            builder.setIcon(R.drawable.info);
                            builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent act_goUser = new Intent(MapActivity.this, DashBoard.class);
                                    act_goUser.putExtra("email",currentUser);
                                    startActivity(act_goUser);
                                    finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
        }else{
            db.collection("sides")
                .add(side)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                        builder.setTitle(R.string.msg_info);
                        builder.setMessage(R.string.msg_successSide);
                        builder.setIcon(R.drawable.info);
                        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent act_goUser = new Intent(MapActivity.this, DashBoard.class);
                                act_goUser.putExtra("email",currentUser);
                                startActivity(act_goUser);
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
            });
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}