package mvila.cat.luxyrestaurantclient;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.PerformanceTestCase;
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
    private int idComanda;

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

        List listTaules = new ArrayList();
        llistaTaules(listTaules);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, listTaules);
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        spinTaules.setAdapter( dataAdapter );

        spinTaules.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent , View view , int position , long id ) {
                        ( (TextView) parent.getChildAt( 0 ) ).setTextSize( 24 );
                        omplirListPlats( spinTaules.getItemAtPosition( position ).toString() );
                    }

                    public void onNothingSelected( AdapterView<?> parent ) {}
                });
    }

    private void omplirListPlats(String strTaula) {

        final ListView lvItemsComanda = findViewById( R.id.lvItemsComanda);
        List listItemsComanda = new ArrayList();

        obtenirIdComanda( obtenirIdTaula( strTaula ) );

        llistaItemsAliments( listItemsComanda );
        llistaItemsBegudes( listItemsComanda );

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItemsComanda );

        lvItemsComanda.setAdapter(adaptador);

        lvItemsComanda.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String strItem = lvItemsComanda.getItemAtPosition( i ).toString();
                dialogEliminarItem( strItem);
            }
        });

    }

    private void recargarListView() {

        final ListView lvItemsComanda = findViewById( R.id.lvItemsComanda);
        List listItemsComanda = new ArrayList();

        llistaItemsAliments( listItemsComanda );
        llistaItemsBegudes( listItemsComanda );

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listItemsComanda );

        lvItemsComanda.setAdapter(adaptador);

        lvItemsComanda.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String strItem = lvItemsComanda.getItemAtPosition( i ).toString();
                dialogEliminarItem( strItem);
            }
        });

    }

    private void dialogEliminarItem(final String strItem) {

        try {
            AlertDialog.Builder NewDialog = new AlertDialog.Builder(this);

            NewDialog
                    .setTitle("Eliminar")
                    .setMessage("Vols eliminar el seguent plat de la comanda?\n\tPlat: " + strItem )
                    .setCancelable(false)

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                        }
                    })
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            elimnarItem( strItem );
                            recargarListView();
                        }
                    })
                    .show();
        } catch ( Exception e ) {

        }
    }

    private void elimnarItem(String strItem) {

        String sQuery = "DELETE FROM comandaaliment WHERE id_comanda = " + idComanda + " AND id = ( SELECT id FROM comandaaliment WHERE id_comanda = " + idComanda + " AND id_aliment = (SELECT id_aliment FROM aliments WHERE nom = '" + strItem + "') limit 1)";
        String sQuery1 = "DELETE FROM comandabeguda WHERE id_comanda = " + idComanda + " AND id = ( SELECT id FROM comandabeguda WHERE id_comanda = " + idComanda + " AND id_beguda = (SELECT id_beguda FROM begudes WHERE nom = '" + strItem + "') limit 1)";

        try {
            conBD.updateDB( sQuery );
            conBD.updateDB( sQuery1 );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    private void obtenirIdComanda( int idTaula ) {

        String sQuery = "SELECT id_comanda FROM comanda WHERE id_taula = " + idTaula +" AND finalitzada = FALSE";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if( rs.next() ) {
                idComanda = rs.getInt( "id_comanda" );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    private int obtenirIdTaula( String strTaula ) {

        int idTaula = -1;

        String sQuery = "SELECT id_taula FROM taula WHERE nom = '" + strTaula + "'";

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

    private void llistaItemsAliments(List listItemsComanda) {

        String sQuery = "SELECT nom FROM aliments AS a INNER JOIN comandaaliment AS ca ON a.id_aliment = ca.id_aliment " +
                "WHERE ca.id_comanda = " + idComanda + ";";
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

    private void llistaItemsBegudes(List listItemsComanda) {
        String sQuery = "SELECT nom FROM begudes AS b INNER JOIN comandabeguda AS cb ON b.id_beguda = cb.id_beguda " +
                "WHERE cb.id_comanda = " + idComanda + ";";
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
    private void llistaTaules( List listTaules) {

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

    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {
            case R.id.btnBack_edit:
                finish();
                overridePendingTransition( R.anim.inright , R.anim.outright );
                break;
            case R.id.btnAfegirProducte:
                Intent intent = new Intent( EditarComanda.this , AfegirItemComanda.class );
                intent.putExtra( "id" , String.valueOf( idComanda ) );
                startActivity( intent );
                overridePendingTransition(R.anim.inleft, R.anim.outleft);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        recargarListView();
    }
}
