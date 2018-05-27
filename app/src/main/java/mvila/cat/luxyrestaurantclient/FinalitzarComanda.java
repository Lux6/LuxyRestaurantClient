package mvila.cat.luxyrestaurantclient;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FinalitzarComanda extends AppCompatActivity implements View.OnClickListener{

    private ConnexioBaseDades conBD = new ConnexioBaseDades();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalitzar_comanda);

        getSupportActionBar().hide();

        conBD.connectarDB();

        omplirListComandes();
    }

    /**
     * GENERA UN SPINNER DE LES TAULES DISPONIBLES PER CREAR UNA NOVA COMANDA
     */
    private void omplirListComandes() {

        final ListView lvPlats = findViewById( R.id.lvComandes_finalitzar );

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaTaules());
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        lvPlats.setAdapter( dataAdapter );

        lvPlats.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String strPlat = lvPlats.getItemAtPosition( i ).toString();
                dialogFinalitzarComanda( strPlat );
            }
        });
    }

    /**
     * GENERA UNA LLISTA DE LES TAULES DISPONIBLES
     * @return
     */
    private List llistaTaules() {

        List listTaules = new ArrayList();
        String sQuery = "SELECT nom FROM taula AS t INNER JOIN comanda AS c ON t.id_taula = c.id_taula WHERE finalitzada = false;";
        try {
            ResultSet rs = conBD.queryDB( sQuery );

            while ( rs.next() ) {
                listTaules.add( rs.getString( "nom") );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return listTaules;

    }

    private void dialogFinalitzarComanda(final String strTaula ) {
        AlertDialog.Builder NewDialog = new AlertDialog.Builder( this );

        NewDialog
                .setTitle( "Finalitzar Comanda" )
                .setMessage( "Estas segur que vols finalitzar aquesta comanda? Una vegada finalitzada no es podràs revertir l'acció." )
                .setCancelable( false )

                .setPositiveButton( "Finalitzar" , new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialogo1 , int id ) {
                        finalitzarComanda( strTaula);
                        omplirListComandes();
                    }
                })
                .setNegativeButton( "Cancel·lar" , new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialogo1 , int id ) {
                    }
                })
                .show();
    }

    private void finalitzarComanda(String strTaula) {

        int idTaula = obtenirIdTaula( strTaula );
        String sQuery = "UPDATE comanda SET finalitzada = TRUE WHERE id_taula = " + idTaula;

        try {
            conBD.updateDB( sQuery );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    private int obtenirIdTaula(String strTaula) {

        int idTaula = -1;
        String sQuery = "SELECT id_taula FROM taula WHERE nom = '" + strTaula + "';";

        try {
            ResultSet rs = conBD.queryDB( sQuery );
            if ( rs.next() ) {
                idTaula = rs.getInt( "id_taula" );
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idTaula;
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {
            case R.id.btnBack_finalitzar:
                finish();
                overridePendingTransition( R.anim.inright , R.anim.outright );
                break;
        }

    }
}
