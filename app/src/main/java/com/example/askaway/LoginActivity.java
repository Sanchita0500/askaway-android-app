package com.example.askaway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.askaway.Admin.AdminActivity;
import com.example.askaway.Objects.Prevalant;
import com.example.askaway.Objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText inputID,inputPassword;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private TextView signup;
    private ImageButton admin;

    private String parentDbName="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        admin = (ImageButton)findViewById(R.id.login_app_logo);
        loginButton=(Button) findViewById(R.id.login_button);
        inputPassword=(EditText) findViewById(R.id.login_password_input);
        inputID=(EditText) findViewById(R.id.login_id_input);
        signup=(TextView)findViewById(R.id.signup_link);


        loadingBar=new ProgressDialog(this);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void LoginUser() {
        String ID=inputID.getText().toString();
        String password=inputPassword.getText().toString();

        if(TextUtils.isEmpty(ID)){
            Toast.makeText(this, "Please enter your id", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(ID,password);

        }
    }

    private void AllowAccessToAccount(String ID, String password) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(ID).exists()){
                    Users usersData=snapshot.child(parentDbName).child(ID).getValue(Users.class);
                    if(usersData.getId().equals(ID)){
                        if(usersData.getPassword().equals(password)){

                            if(parentDbName.equals("Users")){
                                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalant.currentOnlineUser=usersData;
                                startActivity(intent);
                                finish();
                            }
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Account with ID "+ID+" does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}