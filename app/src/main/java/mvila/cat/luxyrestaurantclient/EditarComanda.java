package mvila.cat.luxyrestaurantclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        conBD.connectarDB();

        omplirSpinnerTaules();
    }

    /**
     * GENERA UN SPINNER DE LES TAULES DISPONIBLES PER CREAR UNA NOVA COMANDA
     */
    private void omplirSpinnerTaules() {

        //Crida Spinner de les ubicaions
        Spinner spinTaules = findViewById ( R.id.spinTaules_edit );

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
                break;
            case R.id.btnAfegirProducte:
                break;
        }

    }
}
