package mvila.cat.luxyrestaurantclient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;

public class EspaiClient extends AppCompatActivity implements View.OnClickListener{

    private static boolean bOpinar = false;
    private static boolean bComanda = true;
    private boolean bTaulaEscollida = false;
    private String strTaula;

    private ConnexioBaseDades conBD = new ConnexioBaseDades();
    public static ObjComandaClient oComandaClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espai_client);

        getSupportActionBar().hide();

        conBD.connectarDB();

        if ( !bTaulaEscollida ) {
            bOpinar = false;
            bComanda = true;
            dialogEscollirTaula();
            oComandaClient = new ObjComandaClient();
        }

        comprovarOpcions();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(( findViewById( R.id.etTaulaSeleccionada_opcions)).getWindowToken(), 0);
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

/**************************************************************************************************/    /* Dialogs */

    private void dialogEscollirTaula() {
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

    private void dialogBack() {
        LayoutInflater inflater = ( LayoutInflater ) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        final View formElementsView = inflater.inflate( R.layout.activity_contrasenya_back, null , false );

        final EditText etContrasenyaBack = ( EditText ) formElementsView.findViewById(R.id.etContrasenyaBack);

        new AlertDialog.Builder(this).setView( formElementsView )
                .setCancelable(false)
                .setTitle( "Contrasenya" )
                .setMessage( "Introdueix la contrasenya per anar al menú" )
                .setNeutralButton( "Cancel·lar" , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {}
                })
                .setPositiveButton( "Continuar" , new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogo1, int id) {
                        comprovarConstrasenya( etContrasenyaBack , formElementsView );
                    }

                })
                .show();
    }

    private void dialogFinal() {
        AlertDialog.Builder NewDialog = new AlertDialog.Builder( this );

        NewDialog
                .setTitle( "Estimat Client" )
                .setMessage( "Estimat client, esperem que hagi gaudit del nostre menjar i esperem tornar-lo a veure'l molt aviat." )
                .setIcon( getDrawable(R.drawable.ic_restaurant))
                .setCancelable( true )

                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogBack();
                    }
                })
                .show();
    }

/**************************************************************************************************/    /* Comprovacions */

    private void comprovarOpcions() {

        if( !bOpinar && bComanda ) {
            ( (Button) findViewById(R.id.btnOpinarComanda )).setVisibility( View.INVISIBLE);
            ( (Button) findViewById(R.id.btnCrearComanda )).setVisibility( View.VISIBLE);
        } else if ( bOpinar && !bComanda ) {
            ( (Button) findViewById(R.id.btnOpinarComanda )).setVisibility( View.VISIBLE);
            ( (Button) findViewById(R.id.btnCrearComanda )).setVisibility( View.INVISIBLE);
        } else if ( !bOpinar && !bComanda ) {
            ( (Button) findViewById(R.id.btnOpinarComanda )).setVisibility( View.INVISIBLE);
            ( (Button) findViewById(R.id.btnCrearComanda )).setVisibility( View.INVISIBLE);
            dialogFinal();
        }
    }

    private void comprovarConstrasenya(EditText etContrasenya , View view) {
        try {

            String strContrasenya = etContrasenya.getText().toString();
            String strContrasenyaBack = obtenirContrasenya();

            if ( strContrasenya.equals( strContrasenyaBack )) { //Compara si el code es correcte

                finish();
                overridePendingTransition( R.anim.inright , R.anim.outright );

            } else {
                Toast.makeText(this, "Contrasenya incorrecta!", Toast.LENGTH_SHORT).show();
            }
        }catch ( Exception e ){
            e.printStackTrace();
        }
    }

/**************************************************************************************************/    /* Obtenir */

    private String obtenirContrasenya() {

        String strContrasenya = null;

        String sQuery = "SELECT contrasenya FROM usuaris WHERE usuari = 'back'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );
            if( rs.next() ) {
                strContrasenya = rs.getString( "contrasenya" );
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return strContrasenya;
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

/**************************************************************************************************/    /* Getters & Setters */

    public static boolean isbOpinar() {
        return bOpinar;
    }

    public static void setbOpinar(boolean bOpinar) {
        EspaiClient.bOpinar = bOpinar;
    }

    public static boolean isbComanda() {
        return bComanda;
    }

    public static void setbComanda(boolean bComanda) {
        EspaiClient.bComanda = bComanda;
    }

/**************************************************************************************************/

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {
            case R.id.btnCrearComanda:
                Intent intent = new Intent( EspaiClient.this, ComandaClient.class );
                intent.putExtra( "id" , String.valueOf( obtenirIDComanda() ) );
                startActivity( intent );
                overridePendingTransition(R.anim.inleft, R.anim.outleft);
                break;
            case R.id.btnOpinarComanda:
                Intent intent2 = new Intent( EspaiClient.this, OpinarComanda.class );
                intent2.putExtra( "id" , String.valueOf( obtenirIDComanda() ) );
                startActivity( intent2 );
                overridePendingTransition(R.anim.inleft, R.anim.outleft);
                break;
            case R.id.btnBack_opcions:
                dialogBack();
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        comprovarOpcions();
    }
}
