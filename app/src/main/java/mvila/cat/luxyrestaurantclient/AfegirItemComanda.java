package mvila.cat.luxyrestaurantclient;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AfegirItemComanda extends AppCompatActivity implements View.OnClickListener{

    private String strOpcio;
    private ConnexioBaseDades conBD = new ConnexioBaseDades();
    private String strIdComanda;

    private final String[] strsOpcions = {"Primer" , "Segon" , "Postres" , "Beguda" , "Cafe"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir_item_comanda);

        getSupportActionBar().hide();

        strIdComanda = getIntent().getStringExtra( "id" );

        conBD.connectarDB();
        omplirLlistaOpcions();
    }

    private void omplirLlistaOpcions() {

        final ListView lvOpcions = findViewById( R.id.lvOpcionsPlats_Afegir );

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strsOpcions);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        lvOpcions.setAdapter( dataAdapter );

        lvOpcions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strOpcio = lvOpcions.getItemAtPosition( i ).toString();
                omplirLlistaPlats();
            }
        });

    }

    private void omplirLlistaPlats() {

        final ListView lvPlats = findViewById( R.id.lvPlats_Afegir );

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaPlats());
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        lvPlats.setAdapter( dataAdapter );

        lvPlats.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String strItem = lvPlats.getItemAtPosition(i).toString();

                dialogAfegirProducte( strItem );
            }
        });

    }

    private void dialogAfegirProducte(final String strItem ) {
        AlertDialog.Builder NewDialog = new AlertDialog.Builder( this );

        NewDialog
                .setTitle( "Afegir Producte" )
                .setMessage( "Producte: " + strItem + "\nComanda: " + strIdComanda )
                .setCancelable( true )

                .setPositiveButton( "Afegir" , new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialogo1 , int id ) {

                        afegirItem( strItem );
                    }
                })
                .setNegativeButton( "Cancel·lar" , new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialogo1 , int id ) {
                    }
                })
                .show();
    }

    private void afegirItem(String strItem) {

        if( strOpcio.equals( "Primer") || strOpcio.equals( "Segon") || strOpcio.equals( "Postres") ) {

            int idItem = obtenirIdAliment( strItem );
            afegirItemAliment( idItem );

        } else if ( strOpcio.equals( "Beguda") || strOpcio.equals( "Cafe") ){

            int idItem = obtenirIdBeguda( strItem );
            afegirItemBeguda( idItem );

        }

    }

    private void afegirItemBeguda(int idItem) {

        String sQuery = "INSERT INTO comandabeguda(id_comanda, id_beguda, estat) " +
                "VALUES (" + strIdComanda +", " + idItem + ", 'esperant');";
        System.out.println( sQuery );

        try {
            conBD.updateDB( sQuery );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    private void afegirItemAliment(int idItem) {

        String sQuery = "INSERT INTO comandaaliment(id_comanda, id_aliment, estat) " +
                "VALUES (" + strIdComanda + ", " + idItem + ", 'esperant');";
        System.out.println( sQuery );

        try {
            conBD.updateDB( sQuery );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private int obtenirIdBeguda(String strItem ) {
        int idBeguda = -1;

        String sQuery = "SELECT id_beguda FROM begudes WHERE nom = '" + strItem + "'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if ( rs.next() ) {
                idBeguda = rs.getInt( "id_beguda");
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idBeguda;
    }

    private int obtenirIdAliment(String strItem) {
        int idAliment = -1;

        String sQuery = "SELECT id_aliment FROM aliments WHERE nom = '" + strItem + "'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if ( rs.next() ) {
                idAliment = rs.getInt( "id_aliment");
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idAliment;
    }

    private List<String> llistaPlats() {

        List listPlats = new ArrayList();

        String sQuery = null;

        if( strOpcio.equals( "Primer") ) {

            sQuery = "SELECT nom FROM aliments WHERE ordre = 'Primer'";

        } else if ( strOpcio.equals( "Segon") ){

            sQuery = "SELECT nom FROM aliments WHERE ordre = 'Segon'";

        } else if ( strOpcio.equals( "Postres") ){

            sQuery = "SELECT nom FROM aliments WHERE ordre = 'Postres'";

        } else if ( strOpcio.equals( "Beguda") ){

            sQuery = "SELECT nom FROM begudes WHERE tipus = 'Beguda'";

        } else if ( strOpcio.equals( "Cafe") ){

            sQuery = "SELECT nom FROM begudes WHERE tipus = 'Cafe'";

        }

        try {
            ResultSet rs = conBD.queryDB( sQuery );
            while ( rs.next() ) {

                listPlats.add( rs.getString( "nom" ) );

            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return listPlats;
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {

            case R.id.btnBack_afegir:
                finish();
                break;
        }
    }
}
