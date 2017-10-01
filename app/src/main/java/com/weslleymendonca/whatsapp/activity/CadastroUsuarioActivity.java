package com.weslleymendonca.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.weslleymendonca.whatsapp.R;
import com.weslleymendonca.whatsapp.config.ConfiguracaoFirebase;
import com.weslleymendonca.whatsapp.helper.Base64Custom;
import com.weslleymendonca.whatsapp.helper.Preferencias;
import com.weslleymendonca.whatsapp.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button   btCadastro;
    private Usuario  usuario;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);


        nome  = (EditText) findViewById(R.id.cadNome_id);
        email = (EditText) findViewById(R.id.cadEmail_id);
        senha = (EditText) findViewById(R.id.cadSenha_id);
        btCadastro = (Button) findViewById(R.id.btCadastrar_id);


        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario = new Usuario();

                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());

                if(usuario.getNome().isEmpty() || usuario.getEmail().isEmpty() || usuario.getSenha().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Você precisa preencher todos os campos acima.", Toast.LENGTH_LONG).show();
                }else {

                    cadastrarUsuario();
                }

            }
        });


    }

    public void cadastrarUsuario(){

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso.", Toast.LENGTH_LONG).show();

                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    //capturar o UID do usuário criado no firebase
                    //usuario.setId(usuarioFirebase.getUid());

                    //Codificar o email do usuário em base64 para usar como ID
                    String idUsuario = Base64Custom.codificaBase64(usuario.getEmail());
                    usuario.setId(idUsuario);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDados(idUsuario);

                    abrirLoginUsuario();

                }else{

                    String erroExcessao = "";

                    try{

                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){

                        erroExcessao = "Digite uma senha mais forte, contendo números e letras!";

                    }catch (FirebaseAuthInvalidCredentialsException e){

                        erroExcessao = "O E-mail digitado é inválido. Digite um E-mail válido!";

                    }catch (FirebaseAuthUserCollisionException e){

                        erroExcessao = "Esse E-mail já está sendo usando.";

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), "ERRO: "+ erroExcessao, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void abrirLoginUsuario(){

        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);

    }
}
