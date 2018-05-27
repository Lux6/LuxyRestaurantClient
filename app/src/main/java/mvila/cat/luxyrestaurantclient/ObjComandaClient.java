package mvila.cat.luxyrestaurantclient;

import java.sql.ResultSet;

/**
 * Created by kitus on 16/5/2018.
 */

public class ObjComandaClient {

    private String strPrimerPlat = "";
    private String strSegonPalt = "";
    private String strPostres = "";
    private String strCafe = "";
    private String strBeguda = "";

    String strIdComanda = "0";

    private ConnexioBaseDades conBD;

    public ObjComandaClient( String strIdComanda ) {
        this.strIdComanda = strIdComanda;

        conBD = new ConnexioBaseDades();
        conBD.connectarDB();
    }

    public void enviarComanda() {
        try {
            if ( !strPrimerPlat.equalsIgnoreCase("") ) {
                int id = obtenirIDAliment( strPrimerPlat );
                String sQuery = "INSERT INTO comandaaliment(id_comanda, id_aliment, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
            if ( !strSegonPalt.equalsIgnoreCase("") ) {
                int id = obtenirIDAliment( strSegonPalt );
                String sQuery = "INSERT INTO comandaaliment(id_comanda, id_aliment, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
            if ( !strPostres.equalsIgnoreCase("") ) {
                int id = obtenirIDAliment( strPostres );
                String sQuery = "INSERT INTO comandaaliment(id_comanda, id_aliment, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
            if ( !strBeguda.equalsIgnoreCase("") ) {
                int id = obtenirIDBeguda( strBeguda );
                String sQuery = "INSERT INTO comanadabeguda(id_comanda, id_beguda, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
            if ( !strCafe.equalsIgnoreCase("") ) {
                int id = obtenirIDBeguda( strCafe );
                String sQuery = "INSERT INTO comanadabeguda(id_comanda, id_beguda, estat) VALUES ( " + strIdComanda + ", " + id + ", 'esperant');";
                conBD.updateDB( sQuery );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Obté l'id del plat seleccionat
     * @param strPlat
     * @return
     */
    private int obtenirIDAliment(String strPlat) {

        int idAliment = -1;

        String sQuery = "SELECT id_aliment FROM aliments WHERE nom = '" + strPlat + "'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if ( rs.next() ) {
                idAliment = rs.getInt( "id_aliment");
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idAliment;

    }

    /**
     * Obté l'id del plat seleccionat
     * @param strBeguda
     * @return
     */
    private int obtenirIDBeguda(String strBeguda) {

        int idBeguda = -1;

        String sQuery = "SELECT id_beguda FROM begudes WHERE nom = '" + strBeguda + "'";

        try {
            ResultSet rs = conBD.queryDB( sQuery );

            if ( rs.next() ) {
                idBeguda = rs.getInt( "id_aliment");
            }
            rs.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return idBeguda;

    }

    public String getStrPrimerPlat() {
        return strPrimerPlat;
    }

    public void setStrPrimerPlat(String strPrimerPlat) {
        this.strPrimerPlat = strPrimerPlat;
    }

    public String getStrSegonPalt() {
        return strSegonPalt;
    }

    public void setStrSegonPalt(String strSegonPalt) {
        this.strSegonPalt = strSegonPalt;
    }

    public String getStrPostres() {
        return strPostres;
    }

    public void setStrPostres(String strPostres) {
        this.strPostres = strPostres;
    }

    public String getStrCafe() {
        return strCafe;
    }

    public void setStrCafe(String strCafe) {
        this.strCafe = strCafe;
    }

    public String getStrBeguda() {
        return strBeguda;
    }

    public void setStrBeguda(String strBeguda) {
        this.strBeguda = strBeguda;
    }

}
