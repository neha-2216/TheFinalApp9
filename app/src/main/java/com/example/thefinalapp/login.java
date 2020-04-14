package com.example.thefinalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    EditText lemail,lpassword;
    Button loginbtn;
    Spinner type11;
    TextView registerbtn;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        type11=(Spinner)findViewById(R.id.spinner2);
        lemail=(EditText)findViewById(R.id.editText);
        lpassword=(EditText)findViewById(R.id.editText6);
        registerbtn=(TextView)findViewById(R.id.textView5);
        loginbtn=(Button)findViewById(R.id.button2);
        fAuth=FirebaseAuth.getInstance();

        final String t11[]={"Driver","Student"};

        ArrayAdapter<String> myAdapter11=new ArrayAdapter<String>(login.this,
                android.R.layout.simple_spinner_dropdown_item,t11);
        type11.setAdapter(myAdapter11);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final String email=lemail.getText().toString().trim();
                final String password=lpassword.getText().toString().trim();
                final String item=type11.getSelectedItem().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    lemail.setError("Please enter your email");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    lpassword.setError("Please enter the password");
                    return;
                }
                if(password.length()<6)
                {
                    lpassword.setError("Password must be more than 6 characters");
                    return;
                }

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(login.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                            if(password.equals("driver") && item.equals("Driver"))
                            {
                                startActivity(new Intent(getApplicationContext(),bus.class));
                            }
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(login.this,"Error !!! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

        });


    }
}
