package com.weslleymendonca.whatsapp.fragmento;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.weslleymendonca.whatsapp.R;
import com.weslleymendonca.whatsapp.activity.ConversaActivity;
import com.weslleymendonca.whatsapp.adapter.ContatoAdapter;
import com.weslleymendonca.whatsapp.config.ConfiguracaoFirebase;
import com.weslleymendonca.whatsapp.helper.Preferencias;
import com.weslleymendonca.whatsapp.model.Contato;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragments extends android.support.v4.app.Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;

    public ContatosFragments() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        //iniciando o value event listener quando a activity for iniciada
        firebase.addValueEventListener(valueEventListenerContatos);
    }

    @Override
    public void onStop() {
        super.onStop();
        //finalizando o value event listener quando a activity for iniciada
        firebase.removeEventListener(valueEventListenerContatos);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Instanciar ArrayList de Contatos
        contatos = new ArrayList<>();


        //recuperar instancia do firebase
        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdUsuario();
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("contatos")
                .child(idUsuarioLogado);

        //Listener para recuperar contatos
        valueEventListenerContatos = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista de contatos
                contatos.clear();

                //Listar contatos
                for(DataSnapshot dados: dataSnapshot.getChildren()){
                    Contato contato = dados.getValue(Contato.class);
                    contatos.add(contato);

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos_fragments, container, false);

        //montando listView
        listView = (ListView) view.findViewById(R.id.lv_contatos);
        /*adapter  = new ArrayAdapter(getActivity(),
                R.layout.lista_contatos,
                contatos
        );*/
        //Instanciando o objeto do tipo ContatoAdapter
        adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                //recuperar dados a serem passados
                Contato contato = contatos.get(i);

                //enviar dados do contato clicado para conversaActivity
                intent.putExtra("nome", contato.getNomeContato());
                intent.putExtra("email", contato.getEmailContato());
                startActivity(intent);
            }
        });


        return view;
    }

}
