package mvila.cat.luxyrestaurantclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EditarComanda extends AppCompatActivity implements View.OnClickListener {

    private ConnexioBaseDades conBD = new ConnexioBaseDades();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_comanda);

        getSupportActionBar().hide();

        conBD.connectarDB();

        omplirSpinnerTaules();
    }

    /**
     * GENERA UN SPINNER DE LES TAULES DISPONIBLES PER CREAR UNA NOVA COMANDA
     */
    private void omplirSpinnerTaules() {

        //Crida Spinner de les ubicaions
        final Spinner spinTaules = findViewById ( R.id.spinTaules_edit );

        List listTaules = llistaTaules();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, listTaules);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        spinTaules.setAdapter( dataAdapter );

        spinTaules.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent , View view , int position , long id ) {
                        ( (TextView) parent.getChildAt( 0 ) ).setTextSize( 24 );
                        omplirListView( spinTaules.getItemAtPosition( position).toString() );
                    }

                    public void onNothingSelected( AdapterView<?> parent ) {}
                });
    }

    private void omplirListView(String strTaula) {

        ListView lvItemsComanda = findViewById( R.id.lvItemsComanda);
        List listItemsComanda = new ArrayList();
        llistaItemsAliments( strTaula , listItemsComanda );
        llistaItemsBegudes( strTaula , listItemsComanda );

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItemsComanda );

        lvItemsComanda.setAdapter(adaptador);

    }

    private void llistaItemsAliments(String strTaula, List listItemsComanda) {
        String sQuery = "SELECT nom FROM aliments AS a INNER JOIN comandaaliment AS ca ON a.id_aliment = ca.id_aliment " +
                "WHERE ca.id_comanda = ( SELECT id_comanda FROM comanda WHERE id_taula = ( SELECT id_taula FROM taula WHERE nom = '" + strTaula + "' limit 1) limit 1);";
        try {
            ResultSet rs = conBD.queryDB( sQuery );

            while ( rs.next() ) {
                listItemsComanda.add( rs.getString( "nom") );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private void llistaItemsBegudes(String strTaula, List listItemsComanda) {
        String sQuery = "SELECT nom FROM begudes AS b INNER JOIN comandabeguda AS cb ON b.id_beguda = cb.id_beguda " +
                "WHERE cb.id_comanda = ( SELECT id_comanda FROM comanda WHERE id_taula = ( SELECT id_taula FROM taula WHERE nom = '" + strTaula + "' limit 1) limit 1);";
        try {
            ResultSet rs = conBD.queryDB( sQuery );

            while ( rs.next() ) {
                listItemsComanda.add( rs.getString( "nom") );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
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

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {
            case R.id.btnBack_edit:
                finish();
                overridePendingTransition( R.anim.inright , R.anim.outright );
                break;
            case R.id.btnAfegirProducte:
                break;
        }

    }
}
