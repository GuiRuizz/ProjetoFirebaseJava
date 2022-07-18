package com.example.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText edit_email,edit_senha;
    private Button bt_entrar;
    private ProgressBar Prg_Bar;
    String[] mensagens = {"Preencha todos os campos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();
        IniciarComponentes();

        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }
        });

        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edit_email.getText().toString();
                String senha = edit_senha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()){

                    Snackbar snackebar = Snackbar.make(view,mensagens[0],Snackbar.LENGTH_SHORT);
                    snackebar.setBackgroundTint(Color.WHITE);
                    snackebar.setTextColor(Color.BLACK);
                    snackebar.show();


                }else{

                    AutenticarUsuario(view);

                }

            }
        });

    }

    private void AutenticarUsuario (View view){

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    Prg_Bar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            TelaPrincipal();

                        }
                    },3000);

                }else {

                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e) {

                        erro = "Digite uma senha com no mínimo 6 caracteres";
                    }catch (FirebaseAuthUserCollisionException e) {

                        erro = "Este email já foi cadastrado";

                    }catch (FirebaseAuthInvalidCredentialsException e){

                        erro = "Email inválido";

                    }catch(Exception e){

                        erro = "Erro ao logar usuário";

                    }
                    Snackbar snackebar = Snackbar.make(view,erro,Snackbar.LENGTH_SHORT);
                    snackebar.setBackgroundTint(Color.WHITE);
                    snackebar.setTextColor(Color.BLACK);
                    snackebar.show();

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioatual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioatual != null){

            TelaPrincipal();

        }
    }

    private void TelaPrincipal(){

        Intent intent = new Intent(FormLogin.this, TelaPrincipal.class);
        startActivity(intent);
        finish();

    }

    private void IniciarComponentes(){

        text_tela_cadastro = findViewById(R.id.text_telacadastro);
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        Prg_Bar = findViewById(R.id.Prg_Bar);

    }
}