package mvila.cat.luxyrestaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {

            case R.id.btnCrearComanda:
                startActivity( new Intent( MainActivity.this , CrearComanda.class ) );
                break;
            case R.id.btnEditarComanda:
                startActivity( new Intent( MainActivity.this , EditarComanda.class ) );
                break;
            case R.id.btnComandaClient:
                startActivity( new Intent( MainActivity.this , ComandaClient.class ) );
                break;

        }

    }
}
