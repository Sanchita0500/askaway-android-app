package com.example.askaway.Functionalities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.askaway.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailFeed extends AppCompatActivity {

    private TextView que,ans,ans_by;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        que = (TextView) findViewById(R.id.que_detail_feed);
        ans = (TextView) findViewById(R.id.answer_detail_feed);
        ans_by = (TextView) findViewById(R.id.answer_by_detail_feed);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        if(id.equals("manual")){
            String Que = intent.getStringExtra("que");
            String Ans = intent.getStringExtra("ans");
            String By = intent.getStringExtra("by");

            que.setText(Que);
            ans.setText(Ans);
            ans_by.setText(By);
        }

        else {
            mRef = FirebaseDatabase.getInstance().getReference().child("Questions").child(id);
            updateView();
        }
    }

    private void updateView() {
        mRef.child("question").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                que.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("answer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ans.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("by").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ans_by.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void goBack(View view){
        finish();
    }
}