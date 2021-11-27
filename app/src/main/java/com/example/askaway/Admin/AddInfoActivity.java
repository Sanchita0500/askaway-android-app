package com.example.askaway.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.askaway.LoginActivity;
import com.example.askaway.R;

public class AddInfoActivity extends AppCompatActivity {

    private Button logoutButton;
    private TextView add_faculty,remove_faculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);

        logoutButton=(Button)findViewById(R.id.logout_button);
        add_faculty=(TextView)findViewById(R.id.add_faculty);
        remove_faculty=(TextView)findViewById(R.id.remove_faculty);

        add_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddInfoActivity.this,AddFacultyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        remove_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddInfoActivity.this,RemoveFacultyActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}