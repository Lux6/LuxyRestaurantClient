package mvila.cat.luxyrestaurantclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AfegirItemComanda extends AppCompatActivity implements View.OnClickListener{

    private String strTaula;
    private String strProducteNou;
    private ConnexioBaseDades conBD = new ConnexioBaseDades();

    private final String[] strsOpcions = {"Primer" , "Segon" , "Postres" , "Beguda" , "Cafe"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir_item_comanda);

        getSupportActionBar().hide();

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

                findViewById( R.id.btnAfegirProducte_afegir ).setVisibility( View.INVISIBLE );
                String strOpcio = lvOpcions.getItemAtPosition( i ).toString();
                omplirLlistaPlats( strOpcio );
            }
        });

    }

    private void omplirLlistaPlats(String strOpcio) {

        final ListView lvPlats = findViewById( R.id.lvPlats_Afegir );

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, llistaPlats( strOpcio));
        dataAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);

        lvPlats.setAdapter( dataAdapter );

        lvPlats.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                strProducteNou = lvPlats.getItemAtPosition( i ).toString();
                findViewById( R.id.btnAfegirProducte_afegir ).setVisibility( View.VISIBLE );
            }
        });

    }

    private List<String> llistaPlats( String strOpcio ) {

        List listPlats = new ArrayList();
        int idComanda = obtenirIdComanda();

        String sQuery = "SELECT c.id_comanda FROM comanda AS c INNER JOIN taula AS t ON c.id_taula = t.id_taula WHERE t.nom = '" + strTaula + "' AND c.finalitzada = FALSE;";

        try {
            ResultSet rs = conBD.queryDB( sQuery );
            while ( rs.next() ) {

                listPlats.add( rs.getString( "opinio") + "\n\t-> " + rs.getInt( "puntuacio" ) );

            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return listPlats;
    }

    private int obtenirIdComanda() {
        String sQuery = "SELECT c.id_comanda FROM comanda AS c INNER JOIN taula AS t ON c.id_taula = t.id_taula WHERE t.nom = '" + strTaula + "' AND c.finalitzada = FALSE;";
        int idComanda = -1;

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if ( rs.next() ) {
                idComanda = rs.getInt( "c.id_comanda" );
            }

            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idComanda;
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {

            case R.id.btnBack_afegir:
                finish();
                break;
            case R.id.btnAfegirProducte_afegir:
                Toast.makeText(this, "Add item", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
