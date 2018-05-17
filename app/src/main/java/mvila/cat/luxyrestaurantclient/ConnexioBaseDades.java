package mvila.cat.luxyrestaurantclient;

import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnexioBaseDades extends Activity {

    private Connection conn = null;
    private Statement stmt = null;

    /**
     * CONNEXIÓ BASE DE DADES
     */
    public void connectarDB(){

        try {
            Class.forName( "org.postgresql.Driver" );
        } catch ( Exception e ) {
            Log.e( "DRIVER" , e.getMessage() );
        }

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy( policy );
        } catch ( Exception e ) {
            Log.e( "POLICY" , e.getMessage() );
        }

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://192.168.0.159:5432/LuxyRestaurant","postgres","root" );
            conn.setAutoCommit( false );
            stmt = conn.createStatement();
        } catch (SQLException e) {
            Log.e( "JDBC" , e.getMessage() );
            e.printStackTrace();
        }

    }

    /**
     * CONSULTA BASE DE DAES
     * @param sQuery
     */
    public ResultSet queryDB(String sQuery ){
        try {
            return stmt.executeQuery( sQuery );
        } catch (Exception e) {}
        return null;
    }

    /**
     * SCRIPTS BASE DE DADES
     * @param sQuery
     */
    public void updateDB(String sQuery ){
        try {
            stmt.executeUpdate( sQuery );
            conn.commit();
        } catch ( Exception e ) {}
    }

    /**
     * DESCONEXIÓ BASE DE DADES
     */
    public void desconnectarDB(){
        try {
            stmt.close();
            conn.close();
        } catch ( Exception e ) {}
    }

}
