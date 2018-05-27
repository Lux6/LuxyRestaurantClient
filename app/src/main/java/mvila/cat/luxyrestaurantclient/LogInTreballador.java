package mvila.cat.luxyrestaurantclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;

public class LogInTreballador extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_treballador);

        getSupportActionBar().hide();
    }


    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {
            case R.id.btnBack_login:
                finish();
                break;
            case R.id.btnLogIn:
                comprovarUsuari();
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        (( EditText) findViewById( R.id.etPassword)).setText( "" );
        (( EditText) findViewById( R.id.etPassword)).setHint( "Contrasenya" );

    }

    private void comprovarUsuari() {
        String strUsuari = ((EditText) findViewById(R.id.etUsuari)).getText().toString();

        buscarUsuariBD(strUsuari);
    }

    private void buscarUsuariBD(String strUsuari) {

        String strContrasenya = ( (EditText) findViewById( R.id.etPassword )).getText().toString();
        String sQuery = "SELECT contrasenya FROM usuaris WHERE usuari = '" + strUsuari + "'";

        ConnexioBaseDades conBD = new ConnexioBaseDades();

        try {

            conBD.connectarDB();

            ResultSet rs =  conBD.queryDB( sQuery );

            if ( rs.next() ) {

                if ( strContrasenya.equals( rs.getString( "contrasenya" ))) {
                    startActivity( new Intent( LogInTreballador.this, EspaiTreballador.class ));
                } else {
                    dialogErrorLogIn();
                }

            } else {
                dialogErrorLogIn();
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            conBD.desconnectarDB();
        }
    }

    private void dialogErrorLogIn() {
        try {
            AlertDialog.Builder NewDialog = new AlertDialog.Builder(this);

            NewDialog
                    .setTitle("Error")
                    .setMessage("Usuario i/o Conrasenya incorrectes")
                    .setCancelable(false)

                    .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            finish();
                            startActivity( getIntent() );
                        }
                    })
                    .setNeutralButton("Cancel·lar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            finish();
                        }
                    })
                    .show();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
