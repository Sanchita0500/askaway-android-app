package com.example.askaway.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.askaway.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemoveFacultyActivity extends AppCompatActivity {

    private ListView faculty_list;
    private DatabaseReference UserRef,FacultyRef;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayList<String> IDList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_faculty);

        faculty_list=(ListView)findViewById(R.id.faculty_list);
        UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        FacultyRef= FirebaseDatabase.getInstance().getReference().child("Faculties");

        get(new MyCallBack() {
            @Override
            public void OnCallBack(ArrayList<String> list) {

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                faculty_list.setAdapter(dataAdapter);

            }
        });

        faculty_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String faculty = adapterView.getItemAtPosition(i).toString();
                String id = IDList.get(i);
                System.out.println("id : "+id);
                DeleteAccount(faculty,id);

            }
        });
    }

    private void DeleteAccount(String faculty, String id) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RemoveFacultyActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View DialogLayout = inflater.inflate(R.layout.delete_dialog, null);
        builder.setView(DialogLayout);

        Button delete = (Button) DialogLayout.findViewById(R.id.btn_del);
        Button cancel = (Button) DialogLayout.findViewById(R.id.btn_cancel_del);

        final android.app.AlertDialog del_dialog = builder.create();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query delQuery = UserRef.child(id);
                Query del1 = FacultyRef.child(id);

                delQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                        Toast.makeText(RemoveFacultyActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RemoveFacultyActivity.this, "error : " + error, Toast.LENGTH_SHORT).show();
                    }
                });

                del1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RemoveFacultyActivity.this, "error : " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                del_dialog.dismiss();
            }
        });

        del_dialog.show();
    }

    public void get(MyCallBack callBack)
    {
        FacultyRef.keepSynced(true);
        FacultyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                IDList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    String faculty,id;
                    if(ds.exists()) {
                        faculty = ds.child("name").getValue().toString();
                        id = ds.getRef().getKey();

                        arrayList.add(faculty);
                        IDList.add(id);
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