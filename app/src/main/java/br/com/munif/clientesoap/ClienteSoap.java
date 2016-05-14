package br.com.munif.clientesoap;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class ClienteSoap extends AppCompatActivity implements View.OnClickListener{

    private EditText et;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_soap);
        et=(EditText)findViewById(R.id.editText);
        tv=(TextView)findViewById(R.id.textView);
        ((Button)findViewById(R.id.button)).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cliente_soap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        consultaCep(et.getText().toString());
    }

    private void consultaCep(String s) {
        new ConsultaCep().execute(s);
    }

    class ConsultaCep extends AsyncTask<String,String,String>{

        private static final String SOAP_ACTION = "https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeClientePort/consultaCEP";
        private static final String METHOD_NAME = "consultaCEP";
        private static final String NAMESPACE = "http://cliente.bean.master.sigep.bsb.correios.com.br/";
        private static final String URL = "https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente";

        @Override
        protected String doInBackground(String... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("cep", params[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            try {
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive result = (SoapPrimitive) envelope
                        .getResponse();
                publishProgress(result.toString());

            } catch (Exception e) {
                e.printStackTrace();
                publishProgress(e.toString());
            }
            return envelope.toString();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tv.setText(values[0]);
        }
    }

}
