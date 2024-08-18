/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package json.parser.wagic;

import java.io.FileWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Eduardo_
 */
public class Tokens {

    static void obtainTokens(JSONArray cards, JSONArray tokens, JSONObject card, JSONObject identifiers, String primitiveCardName, String SET_CODE, FileWriter myWriterImages) {
        for (Object tkn : tokens) {
            JSONObject token = (JSONObject) tkn;
            JSONArray reverseRelated = (JSONArray) token.get("reverseRelated");
            for (Object related : reverseRelated) {
                for (Object o : cards) {
                    card = (JSONObject) o;
                    identifiers = (JSONObject) card.get("identifiers");

                    JSONObject tokenIdentifiers = (JSONObject) token.get("identifiers");
                    primitiveCardName = (String) card.get("faceName") != null ? (String) card.get("faceName") : (String) card.get("name");
                    if (primitiveCardName.equals(related.toString()) && !token.get("name").equals("Copy") && !token.get("name").equals("Energy Reserve") && !token.get("name").equals("Plot") && !token.get("name").equals("Poison Counter") && !token.get("name").equals("City's Blessing") && !token.get("name").equals("The Monarch") && identifiers.get("multiverseId") != null) {
                        CardDat.generateCSV(SET_CODE, identifiers.get("multiverseId") + "t", (String) tokenIdentifiers.get("scryfallId"), myWriterImages, "front/");
                    }
                }
            }
        }
    }
}
