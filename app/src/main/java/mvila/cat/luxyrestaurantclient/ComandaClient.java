package mvila.cat.luxyrestaurantclient;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                break;
            case R.id.btnSegonPlat:
                colorButton( view.getId() );
                omplirLlistPlats( "Segon" );
                break;
            case R.id.btnPostres:
                colorButton( view.getId() );
                omplirLlistPlats( "Postres" );
                break;
            case R.id.btnBegudes:
                colorButton( view.getId() );
                omplirLlistBegudes( "Beguda" );
                break;
            case R.id.btnCafes:
                colorButton( view.getId() );
                omplirLlistBegudes( "Cafe" );
                break;

        }

    }

    private void colorButton(int id) {

        String strSeleccionat = "#ff0099cc";
        String strStandard = "#ff00ddff";

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
                Toast.makeText(ComandaClient.this, strPlat, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void itemSeleccionat(String strPlat) {

        TextView tvPlat = findViewById( R.id.tvPlat );
        tvPlat.setText( strPlat );
    }

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
                        Toast.makeText(ComandaClient.this, strPlat, Toast.LENGTH_SHORT).show();
                        System.out.println( strPlat );
                    }

                    public void onNothingSelected( AdapterView<?> parent ) {}
                });
    }
}
