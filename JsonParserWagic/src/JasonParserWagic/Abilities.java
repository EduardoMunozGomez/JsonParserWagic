/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class Abilities {

    protected static String processOracleText(String oracleText) {
        String abilities = "";
        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("flash")) {
                abilities += "flash, ";
            }
            if (oracleText.contains("can't be countered")) {
                abilities += "nofizzle, ";
            }
            if (oracleText.contains("defender")) {
                abilities += "defender, ";
            }
            if (oracleText.contains("flying")) {
                abilities += "flying, ";
            }
            if (oracleText.contains("intimidate")) {
                abilities += "intimidate, ";
            }
            if (oracleText.contains("first strike")) {
                abilities += "first strike, ";
            }
            if (oracleText.contains("double strike")) {
                abilities += "double strike, ";
            }
            if (oracleText.contains("deathtouch")) {
                abilities += "deathtouch, ";
            }
            if (oracleText.contains("haste")) {
                abilities += "haste, ";
            }
            if (oracleText.contains("hexproof")) {
                abilities += "opponentshroud, ";
            }
            if (oracleText.contains("can't be blocked.")) {
                abilities += "unblockable, ";
            }
            if (oracleText.contains("indestructible")) {
                abilities += "indestructible, ";
            }
            if (oracleText.contains("vigilance")) {
                abilities += "vigilance, ";
            }
            if (oracleText.contains("reach")) {
                abilities += "reach, ";
            }
            if (oracleText.contains("trample")) {
                abilities += "trample, ";
            }
            if (oracleText.contains("lifelink")) {
                abilities += "lifelink, ";
            }
            if (oracleText.contains("can't block.")) {
                abilities += "cantblock, ";
            }
            if (oracleText.contains("attacks each turn if able.")) {
                abilities += "mustattack, ";
            }
            if (oracleText.contains("islandwalk")) {
                abilities += "islandwalk, ";
            }
            if (oracleText.contains("swampwalk")) {
                abilities += "swampwalk, ";
            }
            if (oracleText.contains("mountainwalk")) {
                abilities += "mountainwalk, ";
            }
            if (oracleText.contains("forestwalk")) {
                abilities += "forestwalk, ";
            }
            if (oracleText.contains("protection from")) {
                abilities += "protection from, ";
            }
            if (oracleText.contains("power can't block it.")) {
                abilities += "strong, ";
            }
            if (oracleText.contains("can block only creatures with flying.")) {
                abilities += "cloud, ";
            }
        } catch (Exception ex) {

        }
        if (abilities.endsWith(", ")) {
            abilities = abilities.substring(0, abilities.length() - 2);
        }
        return abilities;
    }
}