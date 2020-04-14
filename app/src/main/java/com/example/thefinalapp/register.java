package com.example.thefinalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    public static final String TAG1 = "TAG";
    Spinner mySpinner,mySpinner2,type;
    Button button;
    EditText memail,mname,mpassword,mphonenumber,mrollnumber,mparentmob;
    TextView mloginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        memail=(EditText)findViewById(R.id.editText2);
        mname=(EditText)findViewById(R.id.editText3);
        mpassword=(EditText)findViewById(R.id.editText4);
        mphonenumber=(EditText)findViewById(R.id.editText5);
        mrollnumber=(EditText)findViewById(R.id.editText7);
        button=(Button)findViewById(R.id.button88);
        mySpinner=(Spinner) findViewById(R.id.spin);
        mySpinner2=(Spinner) findViewById(R.id.spin2);
        type=(Spinner)findViewById(R.id.spinner);
        mloginBtn=(TextView)findViewById(R.id.textView3);
        mparentmob=(EditText)findViewById(R.id.editText8);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();


        /*if(fAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }*/
        final String routes[]={"20K","14E"};
        final String k[]={"Balkampet","SR Nagar","Taruni","Mid Land"};
        final String e[]={"ESI","Jubilee Hills","Gnits"};
        final String t[]={"Driver","Student"};

        ArrayAdapter<String> myAdapter1=new ArrayAdapter<String>(register.this,
                android.R.layout.simple_spinner_dropdown_item,t);
        type.setAdapter(myAdapter1);



        ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(register.this,
                android.R.layout.simple_spinner_dropdown_item,routes);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelect=routes[position];
                //Toast.makeText(Main3Activity.this,"Selected Route :"+itemSelect,Toast.LENGTH_SHORT).show();
                if(position==0)
                {
                    ArrayAdapter<String> myAdapter1=new ArrayAdapter<String>(register.this,
                            android.R.layout.simple_spinner_dropdown_item,k);
                    mySpinner2.setAdapter(myAdapter1);
                }
                if(position==1)
                {
                    ArrayAdapter<String> myAdapter2=new ArrayAdapter<String>(register.this,
                            android.R.layout.simple_spinner_dropdown_item,e);
                    mySpinner2.setAdapter(myAdapter2);
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                final String email=memail.getText().toString().trim();
                final String password=mpassword.getText().toString().trim();
                final String name=mname.getText().toString().trim();
                final String rollno=mrollnumber.getText().toString();
                final String phno=mphonenumber.getText().toString();
                final String route = mySpinner.getSelectedItem().toString();
                final String stop=mySpinner2.getSelectedItem().toString();
                final String usertype = type.getSelectedItem().toString();
                final String parentno = mparentmob.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    memail.setError("Please enter your email");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mpassword.setError("Please enter the password");
                    return;
                }
                if(TextUtils.isEmpty(parentno))
                {
                    mpassword.setError("Please enter the parents number");
                    return;
                }
                if(password.length()<6)
                {
                    mpassword.setError("Password must be more than 6 characters");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(register.this,"user created",Toast.LENGTH_SHORT).show();
                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("users").document(userID);
                            Map<String,Object> user=new HashMap<>();
                            user.put("Roll Number",rollno);
                            user.put("Name",name);
                            user.put("Phone",phno);
                            user.put("Route",route);
                            user.put("Stop",stop);
                            user.put("Parents Phone",parentno);
                            user.put("Type",usertype);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"on Success..user profile is created for "+userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG1,"on Failure: "+e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else
                        {
                            Toast.makeText(register.this,"Error !!! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        };
                    }
                });

            }
        });



    }
}
