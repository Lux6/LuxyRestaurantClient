package mvila.cat.luxyrestaurantclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EspaiTreballador extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espai_treballador);

        getSupportActionBar().hide();
    }


    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {

            case R.id.btnCrearComanda:
                startActivity( new Intent( EspaiTreballador.this , CrearComanda.class));
                overridePendingTransition(R.anim.inleft, R.anim.outleft);
                break;
            case R.id.btnEditarComanda:
                startActivity( new Intent( EspaiTreballador.this, EditarComanda.class ));
                overridePendingTransition(R.anim.inleft, R.anim.outleft);
                break;
            case R.id.btnFinalitzarComanda:
                startActivity( new Intent( EspaiTreballador.this, FinalitzarComanda.class));
                overridePendingTransition(R.anim.inleft, R.anim.outleft);
                break;
            case R.id.btnBack_espai:
                finish();
                overridePendingTransition( R.anim.inright , R.anim.outright );
                break;
        }

    }
}
