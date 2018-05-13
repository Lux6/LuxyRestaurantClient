package mvila.cat.luxyrestaurantclient;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ComandaClient extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda_client);
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {

            case R.id.btnPrimerPlat:
                colorButton( view.getId() );
                omplirLlistPlats( "Primer" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnSegonPlat:
                colorButton( view.getId() );
                omplirLlistPlats( "Segon" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnPostres:
                colorButton( view.getId() );
                omplirLlistPlats( "Postres" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnBegudes:
                colorButton( view.getId() );
                omplirLlistBegudes( "Beguda" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnCafes:
                colorButton( view.getId() );
                omplirLlistBegudes( "Cafe" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnComanda:
                break;
            case R.id.btnEnviar:
                break;
        }

    }

    /**
     * Canvia el color del Botor seleccionat per saber quin plat està escollint.
     * @param id
     */
    private void colorButton(int id) {

        String strSeleccionat = "#ff0099cc"; //Blau fosc
        String strStandard = "#ff00ddff"; //Blau clar

        ( findViewById( R.id.btnPrimerPlat) ).setBackgroundColor(Color.parseColor(strStandard));
        ( findViewById( R.id.btnSegonPlat) ).setBackgroundColor(Color.parseColor(strStandard));
        ( findViewById( R.id.btnPostres) ).setBackgroundColor(Color.parseColor(strStandard));
        ( findViewById( R.id.btnBegudes) ).setBackgroundColor(Color.parseColor(strStandard));
        ( findViewById( R.id.btnCafes) ).setBackgroundColor(Color.parseColor(strStandard));

        if( id == R.id.btnPrimerPlat ) {
            ( findViewById( R.id.btnPrimerPlat) ).setBackgroundColor(Color.parseColor(strSeleccionat));
        } else if( id == R.id.btnSegonPlat ) {
            ( findViewById( R.id.btnSegonPlat) ).setBackgroundColor(Color.parseColor(strSeleccionat));
        } else if( id == R.id.btnPostres ) {
            ( findViewById( R.id.btnPostres) ).setBackgroundColor(Color.parseColor(strSeleccionat));
        } else if( id == R.id.btnBegudes ) {
            ( findViewById( R.id.btnBegudes) ).setBackgroundColor(Color.parseColor(strSeleccionat));
        } else if( id == R.id.btnCafes ) {
            ( findViewById( R.id.btnCafes) ).setBackgroundColor(Color.parseColor(strSeleccionat));
        }
    }

/**************************************************************************************************/

    /**
     * Visualitza la llista de plats
     * @param tipus
     */
    private void omplirLlistPlats(String tipus) {

        final ListView lvPlats = findViewById( R.id.lvPlats );

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaNoms( tipus , "aliments" ));
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        lvPlats.setAdapter( dataAdapter );

        lvPlats.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String strPlat = lvPlats.getItemAtPosition( i ).toString();
                itemSeleccionat( strPlat );
                visibleInformacio();
            }
        });

    }

    /**
     * Plat Sleccionat per visualitzar l'informació
     * @param strPlat
     */
    private void itemSeleccionat(String strPlat) {

        TextView tvPlat = findViewById( R.id.tvPlat );
        tvPlat.setText( strPlat );

        imatgePlatSeleccionat( strPlat );
        descripcioPlatSeleccionat( strPlat );
        opinioPlatSeleccionat( strPlat );
    }

    /**
     * Llista de les opinions del plat seleccionat
     * @param strPlat
     */
    private void opinioPlatSeleccionat(String strPlat) {
        int idPlat = obtenirID(strPlat);

        ListView lvOpinions = findViewById( R.id.lvOpinions );

        List listOpinions = llistaOpinions( idPlat );

        if( listOpinions.size() > 0 ) { //Comprova si te opinions i mostra o amaga la llista

            visibleOpinions();

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOpinions);
            dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

            lvOpinions.setAdapter(dataAdapter);

        } else {
            invisibleOpinions();
        }
    }

    /**
     * Genera una list de les opinions i valoracions del plat sleccionat
     * @param idPlat
     * @return
     */
    private List llistaOpinions(int idPlat) {
        List listOpinions = new ArrayList();
        ConnexioBaseDades conDB = new ConnexioBaseDades();
        String sQuery = "SELECT opinio, puntuacio FROM OpinioAliments WHERE id_aliment = " + idPlat;

        try {
            conDB.connectarDB();
            System.out.println( sQuery );
            ResultSet rs = conDB.queryDB( sQuery );

            while ( rs.next() ) {

                listOpinions.add( rs.getString( "opinio") + "\n\t-> " + rs.getInt( "puntuacio" ) );

            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            conDB.desconnectarDB();
        }

        return listOpinions;
    }

    /**
     * Obté l'id del plat seleccionat
     * @param strPlat
     * @return
     */
    private int obtenirID(String strPlat) {

        int idAliment = -1;

        ConnexioBaseDades conBD = new ConnexioBaseDades();
        String sQuery = "SELECT id_aliment FROM Aliments WHERE nom = '" + strPlat + "'";

        try {
            conBD.connectarDB();
            ResultSet rs = conBD.queryDB( sQuery );

            if ( rs.next() ) {
                idAliment = rs.getInt( "id_aliment");
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            conBD.desconnectarDB();
        }

        return idAliment;

    }

    /**
     * Msotra la descripció del plat seleccionat.
     * @param strPlat
     */
    private void descripcioPlatSeleccionat(String strPlat) {

        TextView tvDescripcio = findViewById( R.id.tvDescripcio );
        ConnexioBaseDades conDB = new ConnexioBaseDades();
        String sQuery = "SELECT descripcio FROM Aliments WHERE nom = '" + strPlat + "'";

        try {
            conDB.connectarDB();
            ResultSet rs = conDB.queryDB( sQuery );

            if ( rs.next() ) {
                tvDescripcio.setText( rs.getString( "descripcio" ));
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            conDB.desconnectarDB();
        }

    }

    /**
     * Mostra l'imatge del plat seleccionat
     * @param strPlat
     */
    private void imatgePlatSeleccionat(String strPlat) {

    }

    /**
     * Genera una llista d'items( plats / begudes ) depenent de l'opció escollida per l'usuari.
     * @param tipus
     * @param from
     * @return
     */
    private List llistaNoms(String tipus, String from) {

        List<String> llistaPlats = new ArrayList<>();
        ConnexioBaseDades conBD = new ConnexioBaseDades();
        String sQuery = "SELECT nom FROM " + from + " WHERE tipus = '" + tipus + "'";

        try {

            conBD.connectarDB();
            ResultSet rs;

            rs = conBD.queryDB( sQuery );
            while ( rs.next() ) {
                llistaPlats.add( rs.getString( "nom" ) );
            }
            rs.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            conBD.desconnectarDB();
        }

        return llistaPlats;
    }

    /**
     * Visualitza una llista amb les begudes.
     * @param tipus
     */
    private void omplirLlistBegudes(String tipus) {

        final ListView lvPlats = findViewById( R.id.lvPlats );

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaNoms( tipus , "begudes"));
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        lvPlats.setAdapter( dataAdapter );

        lvPlats.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent , View view , int position , long id ) {
                        ( (TextView) parent.getChildAt( 0 ) ).setTextSize( 24 );
                        String strPlat = lvPlats.getItemAtPosition( position ).toString();
                    }

                    public void onNothingSelected( AdapterView<?> parent ) {}
                });
    }

/**************************************************************************************************/    /* LinearPlats */

    /**
     * Amaga l'apartat d'informació i opinions dels plats
     */
    private void invisibleInformacioOpinions() {
        ( findViewById( R.id.scrollInformacio )).setVisibility( View.INVISIBLE );
        ( findViewById( R.id.lvOpinions ) ).setVisibility( View.INVISIBLE );
    }

    /**
     * Mostra l'apartat d'informació dels plats
     */
    private void visibleInformacio() {
        ( findViewById( R.id.scrollInformacio )).setVisibility( View.VISIBLE );
    }

    /**
     * Mostra l'apartat d'opinions dels plats
     */
    private void visibleOpinions() {
        ( findViewById( R.id.lvOpinions ) ).setVisibility( View.VISIBLE );
    }

    /**
     * Mostra l'apartat d'opinions del plats
     */
    private void invisibleOpinions() {
        ( findViewById( R.id.lvOpinions ) ).setVisibility( View.INVISIBLE );
    }
}
