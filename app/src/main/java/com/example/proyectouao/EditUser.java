package com.example.proyectouao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EditUser extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LoadinDialog loadinDialog = new LoadinDialog(EditUser.this);
    private FirebaseAuth mAuth;
    private Button btnSave, btnCancel;
    private Spinner spinnerRole, spinnerSite, spinnerStatus;
    private EditText et_nameUser, et_lastNameUser, et_facultyUser, et_program;
    private Activity mySelf;

    private String nameUser = "";
    private String lastNameUser = "";
    private String facultyUser = "";
    private String programEd = "";
    private String email = "";
    private String password = "";
    private Integer selectSite = 0;
    private Integer selectRole  = 0;
    private Integer selectStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mySelf = this;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String lastName = intent.getStringExtra("lastName");
        String faculty = intent.getStringExtra("faculty");
        String site = intent.getStringExtra("site");
        String program = intent.getStringExtra("program");
        String role = intent.getStringExtra("role");
        String status = intent.getStringExtra("status");
        String email = intent.getStringExtra("email");


        et_nameUser = findViewById(R.id.et_nameUser);
        et_lastNameUser = findViewById(R.id.et_lastNameUser);
        et_facultyUser = findViewById(R.id.et_facultyUser);
        et_program = findViewById(R.id.et_program);
        spinnerRole = findViewById(R.id.spinnerRole);
        spinnerSite = findViewById(R.id.spinnerSite);
        spinnerStatus = findViewById(R.id.spinnerStatus);

        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        String[]optionesRoles={"Select...","Teacher","Student"};
        ArrayAdapter<String> adapterRole = new ArrayAdapter<String>(mySelf,android.R.layout.simple_spinner_dropdown_item,optionesRoles);
        spinnerRole.setAdapter(adapterRole);

        String[]optionesSites={"Select...", "Principal", "San fernando", "Corregimiento el placer"};
        ArrayAdapter<String> adapterSites = new ArrayAdapter<String>(mySelf,android.R.layout.simple_spinner_dropdown_item,optionesSites);
        spinnerSite.setAdapter(adapterSites);

        String[]optionesStatus={"Select...", "Active", "Inactive"};
        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(mySelf,android.R.layout.simple_spinner_dropdown_item,optionesStatus);
        spinnerStatus.setAdapter(adapterStatus);

        et_nameUser.setText(name);
        et_lastNameUser.setText(lastName);
        et_facultyUser.setText(faculty);
        spinnerSite.setSelection(Integer.parseInt(site));
        et_program.setText(program);
        spinnerRole.setSelection(Integer.parseInt(role));
        spinnerStatus.setSelection(Integer.parseInt(status));

        String currentUser = mAuth.getCurrentUser().getEmail();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent act_goHome = new Intent(mySelf, DashBoard.class);
                act_goHome.putExtra("email",currentUser);
                startActivity(act_goHome);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameUser = et_nameUser.getText().toString().trim();
                lastNameUser = et_lastNameUser.getText().toString().trim();
                facultyUser = et_facultyUser.getText().toString().trim();
                programEd = et_program.getText().toString().trim();
                selectRole = Integer.parseInt(String.valueOf(spinnerRole.getSelectedItemId()));
                selectSite = Integer.parseInt(String.valueOf(spinnerSite.getSelectedItemId()));
                selectStatus = Integer.parseInt(String.valueOf(spinnerStatus.getSelectedItemId()));

                Boolean response = validator(email,nameUser,lastNameUser,facultyUser,programEd,selectRole,selectSite,selectStatus);

                if (response){
                    loadinDialog.startLoading();
                    editUser(currentUser,email,nameUser,lastNameUser,facultyUser,programEd,selectRole,selectSite,selectStatus);
                }
            }
        });

    }

    private Boolean validator(String email, String nameUser, String lastNameUser, String facultyUser, String program, Integer selectRole, Integer selectSite, Integer selectStatus) {
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
        }else if(selectStatus==0){
            builder.setMessage(R.string.msg_selectStatus);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }

        return response;
    }

    private void editUser(String currentUser,String email, String nameUser, String lastNameUser, String facultyUser, String programEd, Integer selectRole, Integer selectSite, Integer selectStatus) {
        Map<String, Object> user = new HashMap<>();
        user.put("nameUser", nameUser);
        user.put("lastNameUser", lastNameUser);
        user.put("facultyUser", facultyUser);
        user.put("program", programEd);
        user.put("email", email);
        user.put("selectRole", selectRole.toString());
        user.put("selectSite", selectSite.toString());
        user.put("userStatus", selectStatus.toString());

        db.collection("users").document(email)
                .update(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        loadinDialog.dismissDialog();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mySelf);
                        builder.setTitle(R.string.msg_info);
                        builder.setMessage(R.string.msg_successUser2);
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
    }
}