package com.carloskowalevicz.pokemon;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class LoginDoctor2 extends AppCompatActivity {


    EditText tupass,EnombreDoc,EpassDoc,EidUsuario;
    String tipo,id,NombreDoc,PassDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor2);
        tipo = "codigDoc";

        EnombreDoc = ((EditText) findViewById(R.id.editText3));
        EpassDoc = ((EditText) findViewById(R.id.editText4));
        EidUsuario = ((EditText) findViewById(R.id.editText5));
    }


    public void OnClick(View View){

        NombreDoc = EnombreDoc.getText().toString();
        PassDoc = EpassDoc.getText().toString();
        id = EidUsuario.getText().toString();
    ServerManager serverManager3 = new ServerManager(this);
    serverManager3.execute(tipo,id,NombreDoc,PassDoc);
    String k = serverManager3.docIdResult;
    }






}
