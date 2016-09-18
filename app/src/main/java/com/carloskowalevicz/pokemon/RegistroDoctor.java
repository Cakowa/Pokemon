package com.carloskowalevicz.pokemon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class RegistroDoctor extends AppCompatActivity {

    EditText username, password, ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_doctor);
        ip = (EditText) findViewById(R.id.ip);
        username = (EditText) findViewById(R.id.pusername);
        password = (EditText) findViewById(R.id.ppassword);

    }

    public void onregdoc(View view) {

        String susername = username.getText().toString();
        String spassword = password.getText().toString();
        String tipo = "registrodoc";
        String ips = ip.getText().toString();

        ServerManager serverManager = new ServerManager(this);
        serverManager.execute(tipo, susername, spassword, ips);
    }


}
