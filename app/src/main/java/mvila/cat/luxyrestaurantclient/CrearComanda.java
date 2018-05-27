package mvila.cat.luxyrestaurantclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CrearComanda extends AppCompatActivity implements View.OnClickListener{

    private ConnexioBaseDades conBD = new ConnexioBaseDades();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_comanda);

        getSupportActionBar().hide();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        conBD.connectarDB();

        omplirSpinnerTaules();
    }

    /**
     * GENERA UN SPINNER DE LES TAULES DISPONIBLES PER CREAR UNA NOVA COMANDA
     */
    private void omplirSpinnerTaules() {

        //Crida Spinner de les ubicaions
        Spinner spinTaules = findViewById ( R.id.spinTaules );

        List listTaules = llistaTaules();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, listTaules);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        spinTaules.setAdapter( dataAdapter );

        spinTaules.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent , View view , int position , long id ) {
                        ( (TextView) parent.getChildAt( 0 ) ).setTextSize( 24 );
                    }

                    public void onNothingSelected( AdapterView<?> parent ) {}
                });
    }

    /**
     * GENERA UNA LLISTA DE LES TAULES DISPONIBLES
     * @return
     */
    private List llistaTaules() {

        List listTaules = new ArrayList();
        String sQuery = "SELECT nom FROM taula WHERE NOT id_taula IN ( SELECT id_taula FROM comanda WHERE finalitzada = false);";
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

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {

            case R.id.btnGenerarComanda:
                try {
                    generarComanda();
                    mostrarDadesComanda();
                    omplirSpinnerTaules();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnBack_crear:
                finish();
                overridePendingTransition( R.anim.inright , R.anim.outright );
                break;
        }

    }

    /**
     * MOSTRA LA TAULA I EL ID DE LA NOVA COMANDA
     */
    private void mostrarDadesComanda() {

        //mostra la Taula de la nova comanda
        ( (TextView) findViewById( R.id.etTaulaSeleccionada) ).setText(
                ((Spinner) findViewById( R.id.spinTaules)).getSelectedItem().toString()
        );

        //mostra el id de la nova comanda
        ( (TextView) findViewById( R.id.etComandaGenerada)).setText(
                String.valueOf( cercarComandaGenerada() )
        );
    }

    /**
     * CERCA EL ID DE LA NOVA COMANDA
     * @return
     */
    private int cercarComandaGenerada() {

        int idComanda = -1;
        String sQuery = "SELECT id_comanda FROM comanda ORDER BY id_comanda DESC limit 1";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if( rs.next() ) {
                idComanda = rs.getInt( "id_comanda" );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idComanda;
    }

    /**
     * GENERA UNA NOVA COMANDA
     */
    private void generarComanda() {

        int idNovaComanda = cercarUltimaID();
        String strTaula = ((Spinner) findViewById( R.id.spinTaules)).getSelectedItem().toString();
        String sQuery = "INSERT INTO comanda(id_comanda, dia, id_taula, finalitzada) " +
                "VALUES ( " + idNovaComanda + ", now(), (SELECT id_taula FROM taula WHERE nom = '" + strTaula + "'), false);";

        try {
            conBD.updateDB( sQuery );
        } catch ( Exception e) {
            e.printStackTrace();
        }

    }

    private int cercarUltimaID() {

        int idNovaComanda = -1;
        String sQuery = "SELECT id_comanda FROM comanda ORDER BY id_comanda DESC limit 1";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if( rs.next() ) {
                idNovaComanda = rs.getInt( "id_comanda" );
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        System.out.println( idNovaComanda++ );
        return idNovaComanda++;
    }
}
