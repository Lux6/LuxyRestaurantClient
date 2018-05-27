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

        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {

            case R.id.btnEspaiClient:
                startActivity( new Intent( MainActivity.this , EspaiClient.class ) );
                overridePendingTransition(R.anim.inleft, R.anim.outleft);
                break;
            case R.id.btnEspaiTreballador:
                startActivity( new Intent( MainActivity.this , LogInTreballador.class ) );
                overridePendingTransition(R.anim.inleft, R.anim.outleft);
                break;

        }

    }
}
