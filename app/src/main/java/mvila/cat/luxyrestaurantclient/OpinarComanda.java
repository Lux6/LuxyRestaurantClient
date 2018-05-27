package mvila.cat.luxyrestaurantclient;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OpinarComanda extends AppCompatActivity implements View.OnClickListener{

    private String strIdComanda;
    private ConnexioBaseDades conBD = new ConnexioBaseDades();

    private List lsComanda = new ArrayList();
    private String strPlatActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinar_comanda);

        strIdComanda = getIntent().getStringExtra( "id" );

        conBD.connectarDB();

        listComanda();
        omplirLlistaComanda();
    }

    private void omplirLlistaComanda() {

        final ListView lvComanda = findViewById( R.id.lvComanda_opinions);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lsComanda);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        lvComanda.setAdapter( dataAdapter );

        lvComanda.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                strPlatActual = lvComanda.getItemAtPosition( i ).toString();
                omplirLlistaOpinions();
            }
        });

    }

    private void omplirLlistaOpinions() {

        final ListView lvOpinions = findViewById( R.id.lvOpinions);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOpinions() );
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        lvOpinions.setAdapter( dataAdapter );

        lvOpinions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    private List listOpinions() {

        int idPlat = obtenirIdPlat( strPlatActual );
        List listOpinions = new ArrayList();

        String sQuery = "SELECT puntuacio, opinio FROM opinioaliments WHERE id_aliment = " + idPlat;

        try {

            ResultSet rs = conBD.queryDB( sQuery );

            while ( rs.next() ) {
                listOpinions.add( rs.getString( "puntuacio" ) + " >> " + rs.getString( "opinio") );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return listOpinions;
    }

    private int obtenirIdPlat(String strPlat) {
        int idPlat = -1;

        String sQuery = "SELECT id_aliment FROM aliments WHERE nom = '" + strPlat + "';";
        try {

            ResultSet rs = conBD.queryDB( sQuery );

            if( rs.next() ) {
                idPlat = rs.getInt( "id_aliment" );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idPlat;
    }

    private void listComanda() {

        String sQuery = "SELECT DISTINCT(nom) FROM aliments AS a INNER JOIN comandaaliment AS ca ON a.id_aliment = ca.id_aliment WHERE ca.id_comanda =" + strIdComanda;

        System.out.println( sQuery );

        try {

            ResultSet rs = conBD.queryDB( sQuery );

            while ( rs.next() ) {
                lsComanda.add( rs.getString( "nom" ) );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {
            case R.id.btnBack_opinions:
                finish();
                break;
            case R.id.btnEnviarOpinio:
                comprovarValoracio();
                break;
        }
    }

    private void comprovarValoracio() {

        String strValoracio = ((EditText) findViewById( R.id.edValoració ) ).getText().toString();

        if( strValoracio.equalsIgnoreCase( null) || strValoracio.equalsIgnoreCase( "")) {
            dialogValoracioBuida();
        } else {
            dialogPuntuacio( strValoracio );
        }
    }

    private void dialogPuntuacio( final String strValoracio ) {
            AlertDialog.Builder NewDialog = new AlertDialog.Builder( this );

            final CharSequence[] csPuntuacio = {"1","2","3","4","5","6","7","8","9","10"};

            NewDialog
                    .setTitle( "Escull la Taula" )
                    .setCancelable( false )
                    .setNeutralButton("Cancel·lar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    })
                    .setItems(csPuntuacio, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            enviarOpinio(csPuntuacio[ i ].toString() , strValoracio);

                        }
                    })

                    .show();
    }

    private void enviarOpinio( String strPuntuacio, String strValoracio) {

        String sQuery = "INSERT INTO opinioaliments(id_aliment, puntuacio, opinio) " +
                "VALUES (" + obtenirIdPlat( strPlatActual ) + "," + strPuntuacio + ",'" + strValoracio + "');";

        try {
            conBD.updateDB( sQuery );
            lsComanda.remove( strPlatActual );
            omplirLlistaComanda();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private void dialogValoracioBuida() {
        AlertDialog.Builder NewDialog = new AlertDialog.Builder( this );

        NewDialog
                .setTitle( "No hi ha cap valoració" )
                .setCancelable( false )
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                })
                .show();
    }
}
