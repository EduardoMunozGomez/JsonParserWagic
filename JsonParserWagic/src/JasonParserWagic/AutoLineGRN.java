package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class AutoLineGRN {

    // other={convoke} name(Convoke)
    protected static String processOracleTextConvoke(String oracleText) {
        String convoke = "";

        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("convoke")) {
                convoke = "other={convoke} name(Convoke)";
            }
        } catch (Exception ex) {

        }
        return convoke;
    }

    //auto=if compare(restriction{assorcery}~morethan~0) then damage:3 all(creature,player) else damage:2 all(creature,player)
    static String processOracleAddendum(String oracleText) {
        String addendum = "";
        String incidence = "Addendum";
        if (oracleText.contains(incidence)) {

            addendum = "auto=if compare(restriction{assorcery}~morethan~0) then  else";

        }
        return addendum;
    }

    // other={convoke} name(Convoke)
    protected static String processOracleRiot(String oracleText) {
        String riot = "";

        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("riot")) {
                riot = "auto=choice counter(1/1,1)\n"
                        + "auto=choice haste";
            }
        } catch (Exception ex) {

        }
        return riot;
    }

    static String processOracleSpectacle(String oracleText) {
        String spectacle = "";
        String spectacleCost;

        try {
            String incidence = "Spectacle";
            if (oracleText.contains(incidence)) {
                spectacleCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf("("));
                spectacle = "other=" + spectacleCost.trim() + " name(Spectacle) restriction{compare(lifelost)~morethan~0}";
            }
        } catch (Exception ex) {

        }
        return spectacle;
    }
}
