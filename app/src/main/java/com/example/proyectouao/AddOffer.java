package com.example.proyectouao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.proyectouao.model._Offer;
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

import java.lang.reflect.Array;
import java.text.BreakIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddOffer extends AppCompatActivity {

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
    private Integer selectSide=0;
    private EditText et_comboName, et_comboDesc, et_amount, et_price;
    LoadinDialog loadinDialog = new LoadinDialog(AddOffer.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        CollectionReference reference = db.collection("sides");

        list = new ArrayList<_Side>();

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
                for (int i = 0; i < list.size(); i++) {
                    optionsSides.add(new _Side(list.get(i).getId(),list.get(i).getNameSide()));
                }
                ArrayAdapter<_Side> adapterSide = new ArrayAdapter<>(AddOffer.this,android.R.layout.simple_spinner_dropdown_item, optionsSides);
                spinnerSide.setAdapter(adapterSide);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent act_goHome = new Intent(AddOffer.this, DashBoard.class);
                currentUser = mAuth.getCurrentUser().getEmail();
                act_goHome.putExtra("email",currentUser);
                startActivity(act_goHome);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    createOffer(currentUser,comboName,comboDesc,amount,startDate,endDate,startTime,endTime,price,select.getId());
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImages();
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

    private void createOffer(String currentUser, String comboName, String comboDesc, String amount, String startDate, String endDate, String startTime, String endTime, String price, String selectSide) {
        StorageReference filepath = mStorage.child("photos").child(globalPath.getLastPathSegment());
        filepath.putFile(globalPath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagePath = taskSnapshot.getStorage().getDownloadUrl();
                while (!imagePath.isSuccessful());
                Uri downloadUri = imagePath.getResult();
                String S_time[] =  startTime.split(" ");
                String E_time[] =  endTime.split(" ");


                String start_date_time = startDate + " " + S_time[0];
                String end_date_time = endDate+" "+E_time[0];


                SimpleDateFormat dateParser = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                {
                    try {
                        Date start_Date = dateParser.parse(start_date_time);
                        Date end_Date = dateParser.parse(end_date_time);

                        Date currentDate = new Date();
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
                        offer.put("url", downloadUri.toString());
                        offer.put("selectSide", selectSide);
                        offer.put("price", price);

                        db.collection("offers")
                                .add(offer)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(AddOffer.this);
                                        builder.setTitle(R.string.msg_info);
                                        builder.setMessage(R.string.msg_successOffer);
                                        builder.setIcon(R.drawable.info);
                                        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent act_goUser = new Intent(AddOffer.this, DashBoard.class);
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
            }
        });
    }

    private void uploadImages(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Choose a application"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path = data.getData();
            uploadImage.setImageURI(path);
            globalPath = path;
        }
    }

    public void showStartDatePicker(View view) {
        DialogFragment newFragment = new StartDatePicker();
        newFragment.show(getSupportFragmentManager(), START_DATE_DIALOG);
    }

    public void showEndDatePicker(View view) {
        DialogFragment newFragment = new EndDatePicker();
        newFragment.show(getSupportFragmentManager(), END_DATE_DIALOG);
    }

    public void showStartTimePicker(View view) {
        DialogFragment newFragment = new StartTimePicker();
        newFragment.show(getSupportFragmentManager(), START_TIME_DIALOG);
    }

    public void showEndTimePicker(View view) {
        DialogFragment newFragment = new EndTimePicker();
        newFragment.show(getSupportFragmentManager(), END_TIME_DIALOG);
    }

    public static class StartDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
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
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this , hour, minute, true);
    }
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
            String startTime = DateFormat.getTimeInstance().format(calendar.getTime());
            et_startTime.setText(startTime);
        }
    }

    public static class EndTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this , hour, minute, true);
        }
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
            String endTime = DateFormat.getTimeInstance().format(calendar.getTime());
            et_endTime.setText(endTime);
        }
    }
}