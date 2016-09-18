package com.carloskowalevicz.pokemon;
import android.os.Bundle;
import android.view.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginPaciente extends Activity {

    private static final String TAG = "stickynotes";
    private boolean mResumed = false;
    NfcAdapter mNfcAdapter;
    EditText tupass,id;
    public int codigo = 0;
    public String propiedadCodigo;
    public CheckBox check1,check2,check3,check4,check5,check6;
    public Button generarCodig;
    TextView text;
    TextView text2,text1;



    public String tipo,variables;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mNdefExchangeFilters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        setContentView(R.layout.activity_login_paciente);

        tupass = (EditText) findViewById(R.id.pass);
        tupass.addTextChangedListener(mTextWatcher);
        id = (EditText) findViewById(R.id.id);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (MalformedMimeTypeException e) { }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

        check1 =(CheckBox)findViewById(R.id.checkBox);
        check2 =(CheckBox)findViewById(R.id.checkBox2);
        check3 =(CheckBox)findViewById(R.id.checkBox3);
        check4 =(CheckBox)findViewById(R.id.checkBox4);
        check5 =(CheckBox)findViewById(R.id.checkBox5);
        check6 =(CheckBox)findViewById(R.id.checkBox6);

        text = (TextView)findViewById(R.id.textView2);
        text1 = (TextView)findViewById(R.id.textView3);
        text2 = (TextView)findViewById(R.id.textView4);

        tipo = "getData";
        String ids="";


        while (ServerManager.userId == null){

            //  alertDialog.setMessage();

            // alertDialog.show();
       }

        ids = ServerManager.userId.replace("null", "");
        ServerManager serverManager2 = new ServerManager(this);

        serverManager2.execute(tipo ,ids);

        while (serverManager2.var == null){


        }

        variables = serverManager2.var;
        variables = variables.replace("[", null);
        variables = variables.replace("]", null);
        variables = variables.replace("\"", null);
        variables = variables.trim();
        String[] variablesArray = variables.split(",");

        text.setText(variablesArray[0]);
        text1.setText(variablesArray[1]);
        text2.setText(variablesArray[2]);


    }

    public void onGen(View View) {

        codigo=10000001;

        if (check1.isChecked() == true) {
            codigo = codigo + 10;
        } else {
            codigo = codigo + 0;
        }
        if (check2.isChecked() == true) {
            codigo = codigo + 100;
        } else {
            codigo = codigo + 00;
        }
        if (check3.isChecked() == true) {
            codigo = codigo + 1000;
        } else {
            codigo = codigo + 000;
        }
        if (check4.isChecked() == true) {
            codigo = codigo + 10000;
        } else {
            codigo = codigo + 0000;
        }
        if (check5.isChecked() == true) {
            codigo = codigo + 100000;
        } else {
            codigo = codigo + 00000;
        }
        if (check6.isChecked() == true) {
            codigo = codigo + 1000000;
        } else {
            codigo = codigo + 000000;
        }

        this.propiedadCodigo = Integer.toString(codigo);

        tupass.setText(propiedadCodigo);



    }

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));
            setIntent(new Intent());
        }
        enableNdefExchangeMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
        mNfcAdapter.disableForegroundNdefPush(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {


        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            promptForContent(msgs[0]);
        }
    }
    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            if (mResumed) {
                mNfcAdapter.enableForegroundNdefPush(LoginPaciente.this, getPassAsNdef());
            }
        }
    };

    private void promptForContent(final NdefMessage msg) {
        new AlertDialog.Builder(this).setTitle("Replace current content?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String body = new String(msg.getRecords()[0].getPayload());
                        setNoteBody(body);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).show();
    }


    private void setNoteBody(String body) {
        Editable text = tupass.getText();
        text.clear();
        text.append(body);
    }



    private NdefMessage getPassAsNdef() {
        byte[] textBytes =tupass.getText().toString().getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
                textRecord
        });
    }


    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
                        msg
                };
            }
        } else {
            Log.d(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundNdefPush(LoginPaciente.this, getPassAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);
    }








}