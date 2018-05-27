package mvila.cat.luxyrestaurantclient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;

public class EspaiClient extends AppCompatActivity implements View.OnClickListener{

    private static boolean bOpinar = false;
    private boolean bTaulaEscollida = false;
    private String strTaula;

    private ConnexioBaseDades conBD = new ConnexioBaseDades();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espai_client);

        getSupportActionBar().hide();

        conBD.connectarDB();

        if ( !bTaulaEscollida ) {
            dialogEScollirTaula();
        }

        comprovarOpinio();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(( findViewById( R.id.etTaulaSeleccionada_opcions)).getWindowToken(), 0);
    }

    private void comprovarOpinio() {

        if( !bOpinar ) {
           // ( (Button) findViewById(R.id.btnOpinarComanda )).setVisibility( View.INVISIBLE);
            ( (Button) findViewById(R.id.btnCrearComanda )).setVisibility( View.VISIBLE);
        } else {
            ( (Button) findViewById(R.id.btnOpinarComanda )).setVisibility( View.VISIBLE);
           // ( (Button) findViewById(R.id.btnCrearComanda )).setVisibility( View.INVISIBLE);
        }
    }

    private void dialogEScollirTaula() {
        AlertDialog.Builder NewDialog = new AlertDialog.Builder( this );

        final CharSequence[] csTaules = new CharSequence[ numeroTaules() ];

        llistaTaules( csTaules );

        NewDialog
                .setTitle( "Escull la Taula" )
                .setCancelable( false )
                .setItems(csTaules, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        strTaula = csTaules[ i ].toString();
                        ( (EditText) findViewById( R.id.etTaulaSeleccionada_opcions) ).setText( strTaula );

                    }
                })

                .show();
    }

    private int numeroTaules() {
        int iNumeroTaules = 0;
        String sQuery = "SELECT count(nom) AS num FROM taula AS t INNER JOIN comanda AS c ON t.id_taula = c.id_taula WHERE finalitzada = false;";

        try {
            ResultSet rs = conBD.queryDB( sQuery );
            if (rs.next()) {
                iNumeroTaules = rs.getInt( "num" );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return iNumeroTaules;
    }

    private void llistaTaules( CharSequence[] csTaules ) {
        String sQuery = "SELECT nom FROM taula AS t INNER JOIN comanda AS c ON t.id_taula = c.id_taula WHERE finalitzada = false;";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            for( int i = 0; i < csTaules.length; i++ ) {
                rs.next();
                csTaules[ i ] = rs.getString( "nom");
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    public static boolean isbOpinar() {
        return bOpinar;
    }

    public static void setbOpinar(boolean bOpinar) {
        EspaiClient.bOpinar = bOpinar;
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {
            case R.id.btnCrearComanda:
                Intent intent = new Intent( EspaiClient.this, ComandaClient.class );
                intent.putExtra( "id" , String.valueOf( obtenirIDComanda() ) );
                startActivity( intent );
                break;
            case R.id.btnOpinarComanda:
                Intent intent2 = new Intent( EspaiClient.this, OpinarComanda.class );
                intent2.putExtra( "id" , String.valueOf( obtenirIDComanda() ) );
                startActivity( intent2 );
            case R.id.btnBack_opcions:
                finish();
                break;
        }
    }

    private int obtenirIDComanda() {
        int idTaula = obtenirIDTaula();
        int id = -1;

        String sQuery = "SELECT id_comanda FROM comanda WHERE id_taula = " + idTaula +" AND finalitzada = FALSE;";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if( rs.next() ) {
                id =  rs.getInt( "id_comanda" );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return id;
    }

    private int obtenirIDTaula() {
        int id = -1;

        String sQuery = "SELECT id_taula FROM taula WHERE nom = '" + strTaula + "'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if( rs.next() ) {
                id =  rs.getInt( "id_taula" );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public void onResume(){
        super.onResume();

        comprovarOpinio();
    }
}
