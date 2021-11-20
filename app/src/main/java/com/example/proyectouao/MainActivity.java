package com.example.proyectouao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText et_email, et_password;
    private Button logIn;
    private Activity mySelf;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    LoadinDialog loadinDialog = new LoadinDialog(MainActivity.this);

    //variables
    private String email="";
    private String password="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        mySelf = this;
        et_email = findViewById(R.id.eTemail);
        et_password = findViewById(R.id.eTpassword);
        logIn = findViewById(R.id.btnLogin);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            Intent act_goHome = new Intent(mySelf,DashBoard.class);
            act_goHome.putExtra("email",email);
            startActivity(act_goHome);
            finish();
        } else {
            reload();
        }

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();

                AlertDialog.Builder builder = new AlertDialog.Builder(mySelf);
                builder.setTitle(R.string.warning);
                builder.setIcon(R.drawable.warning);
                builder.setPositiveButton(R.string.btn_ok,null);

                if (email.isEmpty()){
                    builder.setMessage(R.string.msg_email);
                    androidx.appcompat.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (password.isEmpty()){
                    builder.setMessage(R.string.msg_password);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    loadinDialog.startLoading();
                    logIn(email,password);
                }

            }
        });

    }

    private void logIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mySelf);
                        builder.setTitle(R.string.warning);
                        builder.setIcon(R.drawable.warning);
                        builder.setPositiveButton(R.string.btn_ok,null);

                        if (task.isSuccessful()) {
                            db.collection("users").document(email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        String status = (String) documentSnapshot.getData().get("userStatus");
                                        if(Integer.parseInt(status)==1){
                                            loadinDialog.dismissDialog();
                                            finish();
                                            Intent act_goHome = new Intent(mySelf,DashBoard.class);
                                            act_goHome.putExtra("email",email);
                                            startActivity(act_goHome);
                                        }else{
                                            loadinDialog.dismissDialog();
                                            builder.setMessage(R.string.msg_disabled);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            FirebaseAuth.getInstance().signOut();
                                            et_email = findViewById(R.id.eTemail);
                                            et_password = findViewById(R.id.eTpassword);
                                            et_email.setText("");
                                            et_password.setText("");
                                        }
                                    }
                                }
                            });

                        } else if(!task.isSuccessful()){
                            loadinDialog.dismissDialog();
                            String errorCode = task.getException().getMessage();
                            builder.setMessage(errorCode);
                            AlertDialog dialog3 = builder.create();
                            dialog3.show();
                        }
                    }
                });

    }

    private void reload() {

    }
}