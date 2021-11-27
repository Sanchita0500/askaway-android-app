package com.example.askaway.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.askaway.R;

public class AdminActivity extends AppCompatActivity {

    private EditText username,password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        username=(EditText)findViewById(R.id.login_username);
        password=(EditText)findViewById(R.id.login_password);
        loginButton=(Button)findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {
        String loginUsername=username.getText().toString();
        String loginPassword=password.getText().toString();

        if(TextUtils.isEmpty(loginUsername)){
            Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(loginPassword)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else{
            //since we have used hardcoded username and password it is not authenticated so make changes in firebase storage rules {request.auth == null} to avoid error
            if(loginUsername.equals("admin") && loginPassword.equals("admin@125")){
                Intent intent=new Intent(AdminActivity.this, AddInfoActivity
                        .class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "Please Enter Correct Username and Password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}