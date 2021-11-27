package com.example.askaway.Functionalities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.askaway.Objects.Prevalant;
import com.example.askaway.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnswerActivity extends AppCompatActivity {

    private ListView que_list;
    private TextView display;
    private DatabaseReference QueRef;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> namesList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        display=(TextView)findViewById(R.id.display);
        que_list=(ListView)findViewById(R.id.que_list);
        QueRef= FirebaseDatabase.getInstance().getReference().child("Questions");

        get(new MyCallBack() {
            @Override
            public void OnCallBack(ArrayList<String> list) {

                if(list.isEmpty())
                    display.setVisibility(View.VISIBLE);
                else {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                    que_list.setAdapter(dataAdapter);
                }
            }
        });

        que_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String que = adapterView.getItemAtPosition(i).toString();
                String asked_by = namesList.get(i);
                Intent intent=new Intent(AnswerActivity.this, AnswerPage.class);
                intent.putExtra("que",que);
                intent.putExtra("asked_by",asked_by);
                startActivity(intent);
            }
        });

    }

    public void get(MyCallBack callBack)
    {
        QueRef.keepSynced(true);
        QueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                namesList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    String que,asked_by;
                    if(ds.exists()) {
                        String name=ds.child("to").getValue().toString();
                        String ans=ds.child("answer").getValue().toString();
                        if(ans.equals("")) {
                            if (name.equals(Prevalant.currentOnlineUser.getName()) || name.equals("Everyone")) {
                                que = ds.child("question").getValue().toString();
                                asked_by=ds.child("name").getValue().toString();
                                arrayList.add(que);
                                namesList.add(asked_by);
                            }
                        }
                    }
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