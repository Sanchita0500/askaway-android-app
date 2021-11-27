package com.example.askaway.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.askaway.LoginActivity;
import com.example.askaway.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddFacultyActivity extends AppCompatActivity {

    private Button createAccountButton;
    private EditText inputName,inputEmail,inputPassword,inputFacultyID,inputConfirmPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        createAccountButton=(Button) findViewById(R.id.faculty_register_button);
        inputName=(EditText) findViewById(R.id.register_faculty_name_input);
        inputEmail=(EditText) findViewById(R.id.faculty_register_email_input);
        inputPassword=(EditText) findViewById(R.id.faculty_register_password_input);
        inputFacultyID=(EditText) findViewById(R.id.register_facultyid_input);
        inputConfirmPassword=(EditText) findViewById(R.id.faculty_register_confirm_password_input);

        loadingBar=new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name=inputName.getText().toString();
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        String confirmPassword=inputConfirmPassword.getText().toString();
        String facultyID=inputFacultyID.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please write name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email ID", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword)){
            Toast.makeText(this, "Different passwords entered", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(facultyID)){
            Toast.makeText(this, "Please enter faculty ID", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Validate(name,email,password,facultyID);
        }
    }

    private void Validate(String name, String email, String password, String facultyID) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(facultyID).exists())){
                    HashMap<String,Object> userDataMap=new HashMap<>();
                    userDataMap.put("email",email);
                    userDataMap.put("password",password);
                    userDataMap.put("name",name);
                    userDataMap.put("id",facultyID);

                    RootRef.child("Users").child(facultyID).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        HashMap<String,Object> facultyDataMap = new HashMap<>();
                                        facultyDataMap.put("name",name);

                                        RootRef.child("Faculties").child(facultyID).updateChildren(facultyDataMap).addOnCompleteListener(new OnCompleteListener<Void>(){
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(AddFacultyActivity.this, "Congratulations! Account Created", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();

                                                    Intent intent=new Intent(AddFacultyActivity.this, AddInfoActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                    }
                                    else {
                                        Toast.makeText(AddFacultyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(AddFacultyActivity.this, "This "+facultyID+" already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent=new Intent(AddFacultyActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}