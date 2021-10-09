package com.example.proyectouao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoadinDialog loadinDialog = new LoadinDialog(AddUser.this);
    private FirebaseAuth mAuth;
    private Button btnSave, btnCancel;
    private Spinner spinnerRole, spinnerSite;
    private EditText et_nameUser, et_lastNameUser, et_facultyUser, et_program, et_email, et_password;
    private Integer selectRole=0;
    private Integer selectSite=0;
    private Activity mySelf;
    private String currentUser;

    private String nameUser = "";
    private String lastNameUser = "";
    private String facultyUser = "";
    private String program = "";
    private String email = "";
    private String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        mySelf = this;
        mAuth = FirebaseAuth.getInstance();

        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerSite = findViewById(R.id.spinnerSite);
        et_nameUser = findViewById(R.id.et_nameUser);
        et_lastNameUser = findViewById(R.id.et_lastNameUser);
        et_facultyUser = findViewById(R.id.et_facultyUser);
        et_program = findViewById(R.id.et_program);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        String[]optionesRoles={"Select...","Teacher","Student"};
        ArrayAdapter<String> adapterRole = new ArrayAdapter<String>(mySelf,android.R.layout.simple_spinner_dropdown_item,optionesRoles);
        spinnerRole.setAdapter(adapterRole);

        String[]optionesSites={"Select...", "Principal", "San fernando", "Corregimiento el placer"};
        ArrayAdapter<String> adapterSites = new ArrayAdapter<String>(mySelf,android.R.layout.simple_spinner_dropdown_item,optionesSites);
        spinnerSite.setAdapter(adapterSites);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent act_goHome = new Intent(mySelf, UserFragment.class);
                startActivity(act_goHome);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameUser = et_nameUser.getText().toString().trim();
                lastNameUser = et_lastNameUser.getText().toString().trim();
                facultyUser = et_facultyUser.getText().toString().trim();
                program = et_program.getText().toString().trim();
                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();
                selectRole = Integer.parseInt(String.valueOf(spinnerRole.getSelectedItemId()));
                selectSite = Integer.parseInt(String.valueOf(spinnerSite.getSelectedItemId()));

                Boolean response = validator(nameUser,lastNameUser,facultyUser,program,email,password,selectRole,selectSite);

                if (response){
                    loadinDialog.startLoading();
                    currentUser = mAuth.getCurrentUser().getEmail();
                    createUser(currentUser,nameUser,lastNameUser,facultyUser,program,email,password,selectRole,selectSite);
                }
            }
        });

    }

    private Boolean validator(String nameUser, String lastNameUser, String facultyUser, String program, String email, String password, Integer selectRole, Integer selectSite) {
        Boolean response = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(mySelf);
        builder.setTitle(R.string.warning);
        builder.setIcon(R.drawable.warning);
        builder.setPositiveButton(R.string.btn_ok,null);

        if(nameUser.isEmpty()){
            builder.setMessage(R.string.msg_nameUser);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(lastNameUser.isEmpty()){
            builder.setMessage(R.string.msg_lastNameUser);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(facultyUser.isEmpty()){
            builder.setMessage(R.string.msg_facultyUser);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(program.isEmpty()){
            builder.setMessage(R.string.msg_programUser);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(email.isEmpty()){
            builder.setMessage(R.string.msg_email);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(password.isEmpty()){
            builder.setMessage(R.string.msg_password);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if (password.length()<6){
            builder.setMessage(R.string.msg_password2);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else if(selectRole==0){
            builder.setMessage(R.string.msg_selectRole);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(selectSite==0){
            builder.setMessage(R.string.msg_selectSite);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }

        return response;
    }

    private void createUser(String currentUser, String nameUser, String lastNameUser, String facultyUser, String program, String email, String password, Integer selectRole, Integer selectSite){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mySelf, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mySelf);
                        builder.setTitle(R.string.warning);
                        builder.setIcon(R.drawable.warning);
                        builder.setPositiveButton(R.string.btn_ok,null);

                        if (task.isSuccessful()) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("nameUser", nameUser);
                            user.put("lastNameUser", lastNameUser);
                            user.put("facultyUser", facultyUser);
                            user.put("program", program);
                            user.put("email", email);
                            user.put("selectRole", selectRole.toString());
                            user.put("selectSite", selectSite.toString());
                            user.put("userStatus", "1");

                            db.collection("users")
                                    .document(email)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            loadinDialog.dismissDialog();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(mySelf);
                                            builder.setTitle(R.string.msg_info);
                                            builder.setMessage(R.string.msg_successUser);
                                            builder.setIcon(R.drawable.info);
                                            builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent act_goUser = new Intent(mySelf, DashBoard.class);
                                                    act_goUser.putExtra("email",currentUser);
                                                    startActivity(act_goUser);
                                                    finish();
                                                }
                                            });
                                            AlertDialog dialog = builder.create();
                                            dialog.show();

                                        }
                                    });
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            loadinDialog.dismissDialog();
                            builder.setMessage(R.string.msg_exist);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            loadinDialog.dismissDialog();
                            builder.setMessage(R.string.msg_error);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
    }
}