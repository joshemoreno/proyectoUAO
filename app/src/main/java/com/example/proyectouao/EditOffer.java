package com.example.proyectouao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.example.proyectouao.model._Side;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditOffer extends AppCompatActivity {

    public  static String START_DATE_DIALOG = "startDatePicker";
    public  static String END_DATE_DIALOG = "endDatePicker";
    public  static String START_TIME_DIALOG = "startTimepicker";
    public  static String END_TIME_DIALOG = "endTimePicker";
    public static EditText et_startDate;
    public static EditText et_startTime;
    public static EditText et_endDate;
    public static EditText et_endTime;

    private FirebaseFirestore db;
    private StorageReference mStorage;
    private ArrayList<_Side> list;
    private Spinner spinnerSide;
    private Button btnSave, btnCancel, btnUpload;
    private ImageView uploadImage;
    private Uri globalPath;
    private Task<Uri> imagePath;
    private String currentUser;
    private FirebaseAuth mAuth;
    private EditText et_comboName, et_comboDesc, et_amount, et_price;
    LoadinDialog loadinDialog = new LoadinDialog(EditOffer.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offer);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        CollectionReference reference = db.collection("sides");

        list = new ArrayList<_Side>();

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

        et_startDate = findViewById(R.id.et_startDate);
        et_startTime = findViewById(R.id.et_startTime);
        et_endDate = findViewById(R.id.et_endDate);
        et_endTime = findViewById(R.id.et_endTime);
        spinnerSide = findViewById(R.id.spinnerNameRestaurant);
        btnSave = findViewById(R.id.btn_save);
        btnUpload = findViewById(R.id.btn_upLoad);
        btnCancel = findViewById(R.id.btn_cancel);
        et_comboName = findViewById(R.id.et_comboName);
        et_comboDesc = findViewById(R.id.et_comboDesc);
        et_amount = findViewById(R.id.et_amount);
        uploadImage = findViewById(R.id.imageProduct);
        et_price = findViewById(R.id.et_price);

        reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                    _Side temp = document.toObject(_Side.class);
                    String id = document.getId();
                    temp.setId(id);
                    list.add(temp);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<_Side> optionsSides = new ArrayList<>();
                optionsSides.add(new _Side("0","Select..."));
                Integer setSpinner = 0;
                for (int i = 0; i < list.size(); i++) {
                    optionsSides.add(new _Side(list.get(i).getId(),list.get(i).getNameSide()));
                     String localId = list.get(i).getId();
                     if(localId.equals(selectSide)){
                        setSpinner = i+1;
                    }
                }
                ArrayAdapter<_Side> adapterSide = new ArrayAdapter<>(EditOffer.this,android.R.layout.simple_spinner_dropdown_item, optionsSides);
                spinnerSide.setAdapter(adapterSide);
                spinnerSide.setSelection(setSpinner);
            }
        });

        et_comboName.setText(name);
        et_comboDesc.setText(description);
        et_amount.setText(amount);
        et_price.setText(price);

        String[] start_Date = startDate.split(" ");
        String[] end_Date = endDate.split(" ");
        String startMonth ="";
        switch (start_Date[1]){
            case "Jan":
                startMonth = "01";
                break;
            case "Feb":
                startMonth = "02";
                break;
            case "Mar":
                startMonth = "03";
                break;
            case "Apr":
                startMonth = "04";
                break;
            case "May":
                startMonth = "05";
                break;
            case "Jun":
                startMonth = "06";
                break;
            case "Jul":
                startMonth = "07";
                break;
            case "Ago":
                startMonth = "08";
                break;
            case "Sep":
                startMonth = "09";
                break;
            case "Oct":
                startMonth = "10";
                break;
            case "Nov":
                startMonth = "11";
                break;
            case  "Dec":
                startMonth = "12";
                break;
        }
        String endMonth ="";
        switch (end_Date[1]){
            case "Jan":
                endMonth = "01";
                break;
            case "Feb":
                endMonth = "02";
                break;
            case "Mar":
                endMonth = "03";
                break;
            case "Apr":
                endMonth = "04";
                break;
            case "May":
                endMonth = "05";
                break;
            case "Jun":
                endMonth = "06";
                break;
            case "Jul":
                endMonth = "07";
                break;
            case "Ago":
                endMonth = "08";
                break;
            case "Sep":
                endMonth = "09";
                break;
            case "Oct":
                endMonth = "10";
                break;
            case "Nov":
                endMonth = "11";
                break;
            case  "Dec":
                endMonth = "12";
                break;
        }

        et_startTime.setText(start_Date[3]);
        et_startDate.setText(start_Date[2]+"/"+startMonth+"/"+start_Date[5]);
        et_endDate.setText(end_Date[2]+"/"+endMonth+"/"+end_Date[5]);
        et_endTime.setText(end_Date[3]);



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent act_goHome = new Intent(EditOffer.this, DashBoard.class);
                currentUser = mAuth.getCurrentUser().getEmail();
                act_goHome.putExtra("email",currentUser);
                startActivity(act_goHome);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer selectSide=0;
                String comboName = et_comboName.getText().toString().trim();
                String comboDesc = et_comboDesc.getText().toString().trim();
                String amount = et_amount.getText().toString().trim();
                String startDate = et_startDate.getText().toString().trim();
                String endDate = et_endDate.getText().toString().trim();
                String startTime = et_startTime.getText().toString().trim();
                String endTime = et_endTime.getText().toString().trim();
                selectSide = Integer.parseInt(String.valueOf(spinnerSide.getSelectedItemId()));
                String price = et_price.getText().toString().trim();
                Boolean response = validator(comboName,comboDesc,amount,startDate,endDate,startTime,endTime,price,selectSide);

                if (response){
                    Integer find = (selectSide-1);
                    _Side select = list.get(find);
                    loadinDialog.startLoading();
                    currentUser = mAuth.getCurrentUser().getEmail();
                    editOffer(currentUser,comboName,comboDesc,amount,startDate,endDate,startTime,endTime,price,select.getId(),url,id);
                }
            }
        });

    }

    private Boolean validator(String comboName, String comboDesc, String amount, String startDate, String endDate, String startTime,String endTime, String price, Integer selectSide) {
        Boolean response = true;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning);
        builder.setIcon(R.drawable.warning);
        builder.setPositiveButton(R.string.btn_ok,null);

        if(selectSide==0){
            builder.setMessage(R.string.msg_namePlace);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(comboName.isEmpty()){
            builder.setMessage(R.string.msg_comboName);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(comboDesc.isEmpty()){
            builder.setMessage(R.string.msg_comboDesc);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(amount.isEmpty()){
            builder.setMessage(R.string.msg_amount);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(Integer.parseInt(amount)<=0){
            builder.setMessage(R.string.msg_amount2);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(startDate.isEmpty()){
            builder.setMessage(R.string.msg_startDate);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(endDate.isEmpty()){
            builder.setMessage(R.string.msg_endDate);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(startTime.isEmpty()){
            builder.setMessage(R.string.msg_startTime);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(endTime.isEmpty()){
            builder.setMessage(R.string.msg_endTime);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if(price.isEmpty()){
            builder.setMessage(R.string.msg_endTime);
            AlertDialog dialog = builder.create();
            dialog.show();
            response = false;
        }else if (globalPath == null) {
            builder.setMessage(R.string.msg_imageProduct);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return response;
    }

    private void editOffer(String currentUser, String comboName, String comboDesc, String amount, String startDate, String endDate, String startTime, String endTime, String price, String selectSide,String url,String id) {
        String S_time[] =  startTime.split(" ");
        String E_time[] =  endTime.split(" ");


        String start_date_time = startDate + " " + S_time[0];
        String end_date_time = endDate+" "+E_time[0];


        SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            try {
                Date start_Date = dateParser.parse(start_date_time);
                Date end_Date = dateParser.parse(end_date_time);


                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)- 5);
                Date currentDate = cal.getTime();

                String status = "0";

                if(currentDate.after(start_Date)&&currentDate.before(end_Date)){
                    status = "1";
                }

                Map<String, Object> offer = new HashMap<>();
                offer.put("comboTitle", comboName);
                offer.put("comboDescription", comboDesc);
                offer.put("amount", amount);
                offer.put("startDate", start_Date.toString());
                offer.put("endDate", end_Date.toString());
                offer.put("status", status);
                offer.put("selectSide", selectSide);
                offer.put("price", price);

                db.collection("offers")
                        .document(id)
                        .update(offer)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditOffer.this);
                                builder.setTitle(R.string.msg_info);
                                builder.setMessage(R.string.msg_successOffer2);
                                builder.setIcon(R.drawable.info);
                                builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent act_goUser = new Intent(EditOffer.this, DashBoard.class);
                                        act_goUser.putExtra("email",currentUser);
                                        startActivity(act_goUser);
                                        finish();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }

    public void showStartDatePicker(View view) {
        DialogFragment newFragment = new EditOffer.StartDatePicker();
        newFragment.show(getSupportFragmentManager(), START_DATE_DIALOG);
    }

    public void showEndDatePicker(View view) {
        DialogFragment newFragment = new EditOffer.EndDatePicker();
        newFragment.show(getSupportFragmentManager(), END_DATE_DIALOG);
    }

    public void showStartTimePicker(View view) {
        DialogFragment newFragment = new EditOffer.StartTimePicker();
        newFragment.show(getSupportFragmentManager(), START_TIME_DIALOG);
    }

    public void showEndTimePicker(View view) {
        DialogFragment newFragment = new EditOffer.EndTimePicker();
        newFragment.show(getSupportFragmentManager(), END_TIME_DIALOG);
    }

    public static class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)- 5);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String startDate = DateFormat.getDateInstance().format(calendar.getTime());
            et_startDate.setText(startDate);
        }
    }

    public static class EndDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)- 5);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String endDate = DateFormat.getDateInstance().format(calendar.getTime());
            et_endDate.setText(endDate);
        }
    }

    public static class StartTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)- 5);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this , hour, minute, true);
        }
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.SECOND, 00);
            String startTime = DateFormat.getTimeInstance().format(calendar.getTime());
            et_startTime.setText(startTime);
        }
    }

    public static class EndTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR)- 5);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this , hour, minute, true);
        }
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.SECOND, 00);
            String endTime = DateFormat.getTimeInstance().format(calendar.getTime());
            et_endTime.setText(endTime);
        }
    }
}