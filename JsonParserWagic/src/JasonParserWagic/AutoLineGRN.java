package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class AutoLineGRN {

    // other={convoke} name(Convoke)
    protected static String processOracleConvoke(String oracleText) {
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

    protected static String processOracleJumpStart(String oracleText, String mana) {
        String jumpStart = "";

        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "jump-start";
            if (oracleText.contains(incidence)) {
                jumpStart = "flashback=" + mana + "{D(*|myhand)}";
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return jumpStart;
    }

    protected static String processOracleMentor(String oracleText, String power) {
        String mentor = "";
        int powerAsNumber = Integer.parseInt(power);
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "mentor";
            if (oracleText.contains(incidence)) {
                mentor = "counter(1/1) target(other creature[attacking;power<="+ --powerAsNumber +"]|myBattlefield)";
            }
        } catch (Exception ex) {

        }
        return mentor;
    }

    protected static String processOracleSurveil(String oracleText) {
        String surveil = "";

        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "surveil ";
            if (oracleText.contains(incidence)) {
                String surveilNumber = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(incidence) + incidence.length() + 1);
                surveil = String.format("aicode=name(surveil) activate name(surveil) transforms((,newability[foreach(*[zpos<=%s]|mylibrary) moverandom(*[zpos<=%s]) from(mylibrary) to(mylibrary)])) ueot\n",
                        surveilNumber, surveilNumber);
                surveil += String.format("auto=name(surveil) reveal:%s optionone name(put in graveyard) target(<upto:%s>*|reveal) moveto(ownergraveyard) optiononeend optiontwo name(put in library) target(<%s>*|reveal) moveto(ownerlibrary) optiontwoend revealend",
                        surveilNumber, surveilNumber, surveilNumber);
            }
        } catch (Exception ex) {

        }
        return surveil;
    }

    // type:creature:myGraveyard
    // auto=foreach(creature|mygraveyard) 1/0
    // text=Target creature gets +X/+0 until end of turn, where X is the number of creature cards in your graveyard.
    protected static String processOracleUndergrowth(String oracleText) {
        String undergrowth = "";

        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("undergrowth")) {
                undergrowth = "auto=foreach(creature|mygraveyard)";
            }
        } catch (Exception ex) {

        }
        return undergrowth;
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
                riot = "auto=choice counter(1/1)\n"
                        + "auto=choice aslongas(creature|mybattlefield) haste >0";
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
                spectacle = "other=" + spectacleCost.trim() + " name(Spectacle) \notherrestriction=compare(oplifelost)~morethan~0";
            }
        } catch (Exception ex) {

        }
        return spectacle;
    }

    static String processOracleKicker(String oracleText, String cardName) {
        String kicker = "";
        String kickerCost;

        try {
            String incidence = "Kicker";
            if (oracleText.contains(incidence)) {
                kickerCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf(" "));
                kicker = "kicker=" + kickerCost.trim();
                //String ifKicked = "was kicked, ";
                //String ifKickedEffect = oracleText.substring(oracleText.indexOf(ifKicked)+ifKicked.length());
                
                //kicker += "\nauto=kicked" + AutoEffects.processEffect(ifKickedEffect, cardName);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return kicker;
    }
}
