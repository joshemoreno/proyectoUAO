package com.example.proyectouao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Activity mySelf;
    private ActionBarDrawerToggle toggle;
    private String user, rol;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        mySelf = this;
        db = FirebaseFirestore.getInstance();
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.app_bar_menu);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.navName);
        db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    user = (String) documentSnapshot.getData().get("name");
                    rol= (String) documentSnapshot.getData().get("rol");
                    textView.setText(user);
                }
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.content, new HomeFragment()).commit();
        setTitle(R.string.menu_home);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(mySelf,
                                            mDrawerLayout,
                                            toolbar,
                                            R.string.drawer_open,
                                            R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectItemNav(item);
        return true;
    }

    private void selectItemNav(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (item.getItemId()){
            case R.id.nav_home:
                ft.replace(R.id.content, new HomeFragment()).commit();
                break;
            case R.id.nav_user:
                ft.replace(R.id.content, new UserFragment()).commit();
                break;
            case R.id.nav_side:
                ft.replace(R.id.content, new SideFragment()).commit();
                break;
            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(mySelf);
                builder.setTitle(R.string.logOut);
                builder.setMessage(R.string.sure);
                builder.setIcon(R.drawable.warning);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        FirebaseAuth.getInstance().signOut();
                        Intent act_goMain = new Intent(mySelf, MainActivity.class);
                        startActivity(act_goMain);
                    }
                });
                builder.setNegativeButton(R.string.no,null);

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        if(item.getItemId() != R.id.nav_logout){
            setTitle(item.getTitle());
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}