package com.example.iot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotActivity extends AppCompatActivity {
    private EditText correo;
    private Button forgotbtn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        correo = (EditText) findViewById(R.id.recuperar_txt);
        forgotbtn=(Button) findViewById(R.id.recuperar_btn);
        mAuth = FirebaseAuth.getInstance();

        forgotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correo.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Por favor ingrese un correo",Toast.LENGTH_SHORT).show();
                }
                else{
                    forgot();
                }
            }
        });

    }
    public void forgot(){
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(correo.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Se ha enviado un correo para reestablecer tu contraseña",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(forgotActivity.this,MainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"No se pudo enviar el correo",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}