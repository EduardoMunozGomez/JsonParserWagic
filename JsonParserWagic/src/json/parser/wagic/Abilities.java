package json.parser.wagic;

import java.util.Iterator;
import org.json.simple.JSONArray;

// @author Eduardo
public class Abilities {

    protected static String processKeywordsAbilities(JSONArray keywords, String oracleText) {
        String abilities = "";
        Iterator i = keywords.iterator();
        // take each value from the json array separately as a keyword
        while (i.hasNext()) {
            String keyword = (String) i.next();

            keyword = keyword.toLowerCase();

            switch (keyword) {
                case "flash":
                    abilities += "flash,";
                    break;
                case "defender":
                    abilities += "defender,";
                    break;
                case "flying":
                    abilities += "flying,";
                    break;
                case "intimidate":
                    abilities += "intimidate,";
                    break;
                case "first strike":
                    abilities += "first strike,";
                    break;
                case "double strike":
                    abilities += "double strike,";
                    break;
                case "deathtouch":
                    abilities += "deathtouch,";
                    break;
                case "hexproof":
                    abilities += "opponentshroud,";
                    break;
                case "menace":
                    abilities += "menace,";
                    break;
                case "indestructible":
                    abilities += "indestructible,";
                    break;
                case "vigilance":
                    abilities += "vigilance,";
                    break;
                case "reach":
                    abilities += "reach,";
                    break;
                case "trample":
                    abilities += "trample,";
                    break;
                case "lifelink":
                    abilities += "lifelink,";
                    break;
                case "haste":
                    abilities += "haste,";
                    break;
                case "islandwalk":
                    abilities += "islandwalk,";
                    break;
                case "swampwalk":
                    abilities += "swampwalk,";
                    break;
                case "mountainwalk":
                    abilities += "mountainwalk,";
                    break;
                case "forestwalk":
                    abilities += "forestwalk,";
                    break;
            }
        }

        abilities = processAbilities(oracleText, abilities);
        if (abilities.endsWith(",")) {
            abilities = abilities.substring(0, abilities.length() - 1);
        }
        return abilities;
    }

    protected static String processAbilities(String oracleText, String abilities) {

        oracleText = oracleText.toLowerCase();

        if (oracleText.contains("you have no maximum hand size")) {
            abilities += "nomaxhand,";
        }
        if (oracleText.contains("can't be countered")) {
            abilities += "nofizzle,";
        }
        if (oracleText.contains("can't be blocked.")) {
            abilities += "unblockable,";
        }
        if (oracleText.contains("can't be blocked by more than one creature")) {
            abilities += "oneblocker,";
        }
        if (oracleText.contains("can't block.")) {
            abilities += "cantblock,";
        }
        if (oracleText.contains("attacks each turn if able.") || oracleText.contains("attacks each combat if able.")) {
            abilities += "mustattack,";
        }
        if (oracleText.contains("power can't block it.")) {
            abilities += "strong,";
        }
        if (oracleText.contains("can block only creatures with flying.")) {
            abilities += "cloud,";
        }
        if (oracleText.contains("all creatures able to block ")) {
            abilities += "lure,";
        }
        if (oracleText.contains("counter on it for each color of mana spent to cast it")) {
            abilities += "sunburst,";
        }
        if (oracleText.contains("blocks each turn if able") || oracleText.contains("blocks each combat if able")) {
            abilities += "mustblock,";
        }
        if (oracleText.contains("players can't gain life")) {
            abilities += "nolifegain,";
        }
        if (oracleText.contains("opponents can't gain life")) {
            abilities += "nolifegainopponent,";
        }
        if (oracleText.contains("doesn't untap during your untap step")) {
            abilities += "doesnotuntap,";
        }
        if (oracleText.contains("protection from")) {
            abilities += "protection from,";
        }
        if (oracleText.contains("choose a background")) {
            abilities += "chooseabackground,";
        }
        if (oracleText.contains("modular")) {
            abilities += "modular\nmodular=";
        }
        if (abilities.endsWith(",")) {
            abilities = abilities.substring(0, abilities.length() - 1);
        }

        return abilities;
    }

    protected static String processEnchantmentAbilities(String oracleText) {
        String abilities = "";
        oracleText = oracleText.toLowerCase();

        if (oracleText.contains("flash")) {
            abilities += "flash";
        }

        return abilities;
    }

    protected static String processInstantOrSorceryAbilities(String oracleText) {
        String abilities = "";
        oracleText = oracleText.toLowerCase();

        if (oracleText.contains("can't be countered")) {
            abilities += "nofizzle";
        }

        return abilities;
    }

    protected static String processLandAbilities(String oracleText) {
        String abilities = "";
        oracleText = oracleText.toLowerCase();

        if (oracleText.contains("indestructible")) {
            abilities += "indestructible";
        }

        return abilities;
    }
}
