package com.example.askaway.Functionalities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.askaway.Objects.Prevalant;
import com.example.askaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

public class AskActivity extends AppCompatActivity {

    private Button ask_button;
    private CheckBox anonymous;
    private EditText question;
    private Spinner dropdown_list;
    private DatabaseReference FacultyRef,QueRef;
    private ProgressDialog loadingBar;
    private String Name,Question,saveCurrentDate,saveCurrentTime,productRandomKey,To;
    ArrayList<String> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        FacultyRef= FirebaseDatabase.getInstance().getReference().child("Faculties");
        QueRef= FirebaseDatabase.getInstance().getReference().child("Questions");
        loadingBar=new ProgressDialog(this);

        ask_button = (Button)findViewById(R.id.ask_button);
        anonymous = (CheckBox)findViewById(R.id.anonymous);
        question = (EditText)findViewById(R.id.question);
        dropdown_list=(Spinner)findViewById(R.id.dropdown_list);

        get(new MyCallBack() {
            @Override
            public void OnCallBack(ArrayList<String> list) {

                Collections.sort(list);

                String[] lists = new String[list.size() + 1];
                lists[0] = "Everyone";
                for (int i = 0; i < lists.length - 1; i++) {
                    lists[i + 1] = list.get(i);
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, lists);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dataAdapter.notifyDataSetChanged();
                dropdown_list.setAdapter(dataAdapter);
            }
        });

        dropdown_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                To = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(AskActivity.this, "Please choose professor's name from list", Toast.LENGTH_SHORT).show();
            }
        });

        ask_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateData();
            }
        });
    }


    private void ValidateData()
    {
        To = String.valueOf(dropdown_list.getSelectedItem());
        Name = Prevalant.currentOnlineUser.getName();
        Question = question.getText().toString();

        if(anonymous.isChecked())
            Name = "anonymous";
        if(To.equals(null))
            Toast.makeText(this, "Please select professor's name", Toast.LENGTH_SHORT).show();
        if(TextUtils.isEmpty(Question)){
            Toast.makeText(this, "Please type your question", Toast.LENGTH_SHORT).show();
        }
        else{
            StoreInfo();
        }
    }

    private void StoreInfo() {
        loadingBar.setTitle("Asking Question");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,YYYY");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey=saveCurrentDate + " " + saveCurrentTime;

        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("name",Name);
        productMap.put("question",Question);
        productMap.put("to",To);
        productMap.put("answer","");
        productMap.put("by","");

        QueRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loadingBar.dismiss();
                    Toast.makeText(AskActivity.this, "Question asked successfully", Toast.LENGTH_SHORT).show();

                }
                else{
                    loadingBar.dismiss();
                    String message=task.getException().toString();
                    Toast.makeText(AskActivity.this, "Error : "+ message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void get(MyCallBack callBack)
    {
        FacultyRef.keepSynced(true);
        FacultyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    String name="null";
                    if(ds.exists())
                        name = ds.child("name").getValue().toString();
                    arrayList.add(name);
                }

                callBack.OnCallBack(arrayList);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error",error.getMessage());
            }
        });

    }

    private interface MyCallBack{
        void OnCallBack(ArrayList<String> list);
    }

    public void goBack(View view){
        finish();
    }

}