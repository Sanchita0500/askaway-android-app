package com.example.askaway.Functionalities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashMap;

public class AnswerPage extends AppCompatActivity {

    private TextView que,asked_by;
    private EditText ans;
    private Button submit;
    private String question,answer,By;
    private ProgressDialog loadingBar;
    private DatabaseReference QueRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_page);

        asked_by=(TextView)findViewById(R.id.asked_by);
        que = (TextView)findViewById(R.id.que);
        ans = (EditText)findViewById(R.id.answer);
        submit = (Button)findViewById(R.id.answer_button);

        Intent intent = getIntent();
        question = intent.getStringExtra("que");
        By = intent.getStringExtra("asked_by");

        que.setText(question);
        asked_by.setText(By);

        QueRef= FirebaseDatabase.getInstance().getReference().child("Questions");
        loadingBar=new ProgressDialog(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateData();
            }
        });
    }

    private void ValidateData()
    {
        answer=ans.getText().toString();
        
        if(TextUtils.isEmpty(answer)){
            Toast.makeText(this, "Please type your answer", Toast.LENGTH_SHORT).show();
        }
        else{
            StoreInfo();
        }
    }
    
    public void StoreInfo()
    {

        loadingBar.setTitle("Answering");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        
        QueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(ds.exists()) {
                        String que=ds.child("question").getValue().toString();
                        String key = ds.getKey();
                        if(que.equals(question)) {
                            HashMap<String,Object> productMap=new HashMap<>();
                            productMap.put("answer",answer);
                            productMap.put("by", Prevalant.currentOnlineUser.getName());

                            QueRef.child(key).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        loadingBar.dismiss();
                                        Toast.makeText(AnswerPage.this, "Question answered successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        loadingBar.dismiss();
                                        String message=task.getException().toString();
                                        Toast.makeText(AnswerPage.this, "Error : "+ message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error",error.getMessage());
            }
        });
    }

    public void goBack(View view){
        finish();
    }
}