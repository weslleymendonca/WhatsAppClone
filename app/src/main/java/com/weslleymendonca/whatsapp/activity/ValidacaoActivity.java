package com.weslleymendonca.whatsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.weslleymendonca.whatsapp.R;
import com.weslleymendonca.whatsapp.helper.Preferencias;

import java.util.HashMap;

public class ValidacaoActivity extends AppCompatActivity {

    private EditText codigoValidacao;
    private Button btValidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacao);

        codigoValidacao = (EditText) findViewById(R.id.codValidacao_id);
        btValidar = (Button) findViewById(R.id.btValidar_id);

        //Adicionando máscara no campo de validacao
        SimpleMaskFormatter simpleMaskValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraValidacao = new MaskTextWatcher(codigoValidacao, simpleMaskValidacao);
        codigoValidacao.addTextChangedListener(mascaraValidacao);

        btValidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recuperar dados enviados da LoginActivity
                Preferencias preferencias = new Preferencias(ValidacaoActivity.this);
                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                String tokenGerado   = usuario.get("token");
                String tokenDigitado = codigoValidacao.getText().toString();

                if(tokenDigitado.equals(tokenGerado)){
                    Toast.makeText(getApplicationContext(), "Token validado", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Token não validado", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
