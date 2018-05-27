package mvila.cat.luxyrestaurantclient;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ComandaClient extends AppCompatActivity implements View.OnClickListener{

    private int iPlatSeleccionat = -1;
    private String strIdComanda;

    private ObjComandaClient oComandaClient;
    private ConnexioBaseDades conBD = new ConnexioBaseDades();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comanda_client);

        getSupportActionBar().hide();

        strIdComanda = getIntent().getStringExtra( "id" );

        oComandaClient = new ObjComandaClient( strIdComanda );
        conBD.connectarDB();
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {

            case R.id.btnPrimerPlat:
                iPlatSeleccionat = 0;
                colorButton( view.getId() );
                omplirLlistPlats( "Primer" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnSegonPlat:
                iPlatSeleccionat = 1;
                colorButton( view.getId() );
                omplirLlistPlats( "Segon" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnPostres:
                iPlatSeleccionat = 2;
                colorButton( view.getId() );
                omplirLlistPlats( "Postres" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnBegudes:
                iPlatSeleccionat = 3;
                colorButton( view.getId() );
                omplirLlistBegudes( "Beguda" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnCafes:
                iPlatSeleccionat = 4;
                colorButton( view.getId() );
                omplirLlistBegudes( "Cafe" );
                invisibleInformacioOpinions();
                break;
            case R.id.btnDemanar:
                afegirPlat();
                mostrarComanda();
                iPlatSeleccionat = -1;
                break;
            case R.id.btnComanda:
                mostrarComanda();
                iPlatSeleccionat = -1;
                break;
            case R.id.btnEnviar:
                dialogEnviarComanda();
                iPlatSeleccionat = -1;
                break;
            case R.id.btnBack_comanda:
                finish();
                overridePendingTransition( R.anim.inright , R.anim.outright );
                break;
        }

    }

    private void mostrarComanda() {

        String strComanda = "Primer Plat:\n\t\t\t\t" + oComandaClient.getStrPrimerPlat()
                + "\n\nSegon Plat:\n\t\t\t\t" + oComandaClient.getStrSegonPalt()
                + "\n\nPostres:\n\t\t\t\t" + oComandaClient.getStrPostres()
                + "\n\nBeguda:\n\t\t\t\t" + oComandaClient.getStrBeguda()
                + "\n\nCafe:\n\t\t\t\t" + oComandaClient.getStrCafe();

        AlertDialog.Builder NewDialog = new AlertDialog.Builder( this );

        NewDialog
                .setTitle( "Comanda" )
                .setMessage( strComanda )
                .setCancelable( false )

                .setNeutralButton( "Ok" , new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialogo1 , int id ) {
                    }
                })
                .show();
    }

    private void afegirPlat() {
        
        TextView tvPlat = findViewById( R.id.tvPlat );
        String strPlat = tvPlat.getText().toString();
        
        switch ( iPlatSeleccionat ) {
            
            case 0:
                oComandaClient.setStrPrimerPlat(strPlat);
                break;
            case 1:
                oComandaClient.setStrSegonPalt( strPlat );
                break;
            case 2:
                oComandaClient.setStrPostres( strPlat );
                break;
            case 3:
                oComandaClient.setStrBeguda( strPlat );
                break;
            case 4:
                oComandaClient.setStrCafe( strPlat );
                break;
                
        }
        
    }

    private void dialogEnviarComanda() {
        AlertDialog.Builder NewDialog = new AlertDialog.Builder( this );

        NewDialog
                .setTitle( "Enviar Comanda" )
                .setMessage( "Estas segur que vols enviar aquesta comanda? Una vegada enviada no es podra modifcar." )
                .setCancelable( false )

                .setPositiveButton( "Enviar" , new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialogo1 , int id ) {
                        progessEnviarComanda();
                    }
                })
                .setNegativeButton( "Cancel·lar" , new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialogo1 , int id ) {
                        Toast.makeText(ComandaClient.this, "Comanda cancl·lada", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void progessEnviarComanda() {

        progressDialog = new ProgressDialog(ComandaClient.this);
        progressDialog.setMessage("Sending..."); // Setting Message
        progressDialog.setTitle("Enviant Comanda..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    enviarComanda();
                    EspaiClient.setbOpinar( true );
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();

    }

    private void enviarComanda() {
        /*
        try {
            if ( !oComandaClient.getStrPrimerPlat().equalsIgnoreCase("") ) {
                int id = obtenirIDAliment( oComandaClient.getStrPrimerPlat() );
                String sQuery = "INSERT INTO comandaaliment(id_comanda, id_aliment, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
            if ( !oComandaClient.getStrSegonPalt().equalsIgnoreCase("") ) {
                int id = obtenirIDAliment( oComandaClient.getStrSegonPalt() );
                String sQuery = "INSERT INTO comandaaliment(id_comanda, id_aliment, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
            if ( !oComandaClient.getStrPostres().equalsIgnoreCase("") ) {
                int id = obtenirIDAliment( oComandaClient.getStrPostres() );
                String sQuery = "INSERT INTO comandaaliment(id_comanda, id_aliment, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
            if ( !oComandaClient.getStrBeguda().equalsIgnoreCase("") ) {
                int id = obtenirIDBeguda( oComandaClient.getStrBeguda() );
                String sQuery = "INSERT INTO comanadabeguda(id_comanda, id_beguda, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
            if ( !oComandaClient.getStrCafe().equalsIgnoreCase("") ) {
                int id = obtenirIDBeguda( oComandaClient.getStrCafe() );
                String sQuery = "INSERT INTO comanadabeguda(id_comanda, id_beguda, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        */
        oComandaClient.enviarComanda();
    }

    private void dialogComandaEnviada() {
        try {
            AlertDialog.Builder NewDialog = new AlertDialog.Builder(this);

            NewDialog
                    .setTitle("Comanda")
                    .setMessage("Comanda Enviada Correctament")
                    .setCancelable(false)

                    .setNeutralButton("Enviar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            finish();
                        }
                    })
                    .show();
        } catch ( Exception e ) {

        }
    }

    /**
     * Canvia el color del Botor seleccionat per saber quin plat està escollint.
     * @param id
     */
    private void colorButton(int id) {

        Drawable drawSeleccionat = getDrawable(R.drawable.background_blau_clar2);

        ( findViewById( R.id.btnPrimerPlat) ).setBackgroundColor(Color.TRANSPARENT );
        ( findViewById( R.id.btnSegonPlat) ).setBackgroundColor(Color.TRANSPARENT );
        ( findViewById( R.id.btnPostres) ).setBackgroundColor(Color.TRANSPARENT );
        ( findViewById( R.id.btnBegudes) ).setBackgroundColor(Color.TRANSPARENT );
        ( findViewById( R.id.btnCafes) ).setBackgroundColor(Color.TRANSPARENT );

        if( id == R.id.btnPrimerPlat ) {
            ( findViewById( R.id.btnPrimerPlat) ).setBackground( drawSeleccionat );
        } else if( id == R.id.btnSegonPlat ) {
            ( findViewById( R.id.btnSegonPlat) ).setBackground( drawSeleccionat );
        } else if( id == R.id.btnPostres ) {
            ( findViewById( R.id.btnPostres) ).setBackground( drawSeleccionat );
        } else if( id == R.id.btnBegudes ) {
            ( findViewById( R.id.btnBegudes) ).setBackground( drawSeleccionat );
        } else if( id == R.id.btnCafes ) {
            ( findViewById( R.id.btnCafes) ).setBackground( drawSeleccionat );
        }
    }

/**************************************************************************************************/

    /**
     * Visualitza la llista de plats
     * @param strOrdre
     */
    private void omplirLlistPlats(String strOrdre) {

        final ListView lvPlats = findViewById( R.id.lvPlats );

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaNoms( strOrdre , "aliments" ));
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
        int idPlat = obtenirIDAliment(strPlat);

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
        String sQuery = "SELECT opinio, puntuacio FROM opinioaliments WHERE id_aliment = " + idPlat;

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            while ( rs.next() ) {

                listOpinions.add( rs.getString( "opinio") + "\n\t-> " + rs.getInt( "puntuacio" ) );

            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return listOpinions;
    }

    /**
     * Obté l'id del plat seleccionat
     * @param strPlat
     * @return
     */
    private int obtenirIDAliment(String strPlat) {

        int idAliment = -1;

        String sQuery = "SELECT id_aliment FROM aliments WHERE nom = '" + strPlat + "'";

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

    /**
     * Obté l'id del plat seleccionat
     * @param strBeguda
     * @return
     */
    private int obtenirIDBeguda(String strBeguda) {

        int idBeguda = -1;

        String sQuery = "SELECT id_beguda FROM begudes WHERE nom = '" + strBeguda + "'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if ( rs.next() ) {
                idBeguda = rs.getInt( "id_aliment");
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idBeguda;

    }

    /**
     * Msotra la descripció del plat seleccionat.
     * @param strPlat
     */
    private void descripcioPlatSeleccionat(String strPlat) {

        TextView tvDescripcio = findViewById( R.id.tvDescripcio );
        String sQuery = "SELECT descripcio FROM aliments WHERE nom = '" + strPlat + "'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if ( rs.next() ) {
                tvDescripcio.setText( rs.getString( "descripcio" ));
            }
        } catch ( Exception e ) {
            e.printStackTrace();
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
     * @param strOrdre
     * @param from
     * @return
     */
    private List llistaNoms(String strOrdre, String from) {

        List<String> llistaPlats = new ArrayList<>();
        String sQuery = "SELECT nom FROM " + from + " WHERE ordre = '" + strOrdre + "'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );
            while ( rs.next() ) {
                llistaPlats.add( rs.getString( "nom" ) );
            }
            rs.close();

        } catch ( Exception e ) {
            e.printStackTrace();
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

    private void dialogEliminarPlat() {
        try {
            AlertDialog.Builder NewDialog = new AlertDialog.Builder(this);

            NewDialog
                    .setTitle("Eliminar")
                    .setMessage("Vols eliminar aquest plat de la teva comanda?")
                    .setCancelable(false)

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    })
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    })
                    .show();
        } catch ( Exception e ) {

        }
    }
}
