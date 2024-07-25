package json.parser.wagic;

import static json.parser.wagic.AutoEffects.ProcessEffect;
import static json.parser.wagic.AutoEffects.numberMap;

/**
 *
 * @author Eduardo_
 */
public class ActivatedAbility {

    // Determine if the card has an activated ability
    static String DetermineActivatedAbility(String oracleBit, String cardName, String type, String subtype) {
        String[] activatedAbililty = oracleBit.split(":");
        if (activatedAbililty.length > 1) {
            return ActivatedAbililtyCost(activatedAbililty, cardName, type);
        } else {
            return "";
        }
    }

    // Activated ability cost
    private static String ActivatedAbililtyCost(String[] actAbil, String cardName, String type) {
        String actAbilCost;
        String actAbilEffect;

        numberMap.put("one", 1);
        numberMap.put("two", 2);
        numberMap.put("three", 3);
        numberMap.put("four", 4);
        numberMap.put("five", 5);
        numberMap.put("six", 6);
        numberMap.put("seven", 7);
        numberMap.put("eight", 8);
        numberMap.put("nine", 9);

        actAbilCost = actAbil[0];

        actAbilCost = actAbilCost.replace("{W/P}", "{p(W)}");
        actAbilCost = actAbilCost.replace("{U/P}", "{p(U)}");
        actAbilCost = actAbilCost.replace("{B/P}", "{p(B)}");
        actAbilCost = actAbilCost.replace("{R/P}", "{p(R)}");
        actAbilCost = actAbilCost.replace("{G/P}", "{p(G)}");

        actAbilCost = actAbilCost.replace("Channel ", "autohand=");
        actAbilCost = actAbilCost.replace("discard your hand" + cardName, "reject all(*|myhand)");
        actAbilCost = actAbilCost.replace(", Discard " + cardName, "{discard}");
        actAbilCost = actAbilCost.replace("Discard a card", "{D(*|myhand)}");
        actAbilCost = actAbilCost.replace("Discard a land card", "{D(land|myhand)}");
        actAbilCost = actAbilCost.replace("Discard three cards", "{D(*|myhand)}{D(*|myhand)}{D(*|myhand)}");
        actAbilCost = actAbilCost.replace("Discard a creature card", "{D(creature|myhand)}");
        actAbilCost = actAbilCost.replace("Exile " + cardName + " from your graveyard", "{E}");
        actAbilCost = actAbilCost.replace("Exile " + cardName, "{E}");
        actAbilCost = actAbilCost.replace("Forage", "{S(Food|myBattlefield)}");

        actAbilCost = actAbilCost.replace("Sacrifice a creature with defender", "{S(creature[defender]|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice " + cardName, "{S}");
        actAbilCost = actAbilCost.replace("Sacrifice two creatures", "{S(creature|myBattlefield)}{S(creature|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice an artifact or creature", "{S(*[artifact;creature]|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice an artifact or land", "{S(*[artifact;land]|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice an artifact creature", "{S(creature[artifact]|myBattlefield)}");
        actAbilCost = actAbilCost.replaceAll("Sacrifice another ([a-zA-Z]+)", "{S(other $1|mybattlefield)}");
        actAbilCost = actAbilCost.replaceAll("Sacrifice an ([a-zA-Z]+)", "{S($1|myBattlefield)}");
        actAbilCost = actAbilCost.replaceAll("Sacrifice a ([a-zA-Z]+)", "{S($1|myBattlefield)}");

        actAbilCost = actAbilCost.replaceAll("Remove (an?|a) ([a-zA-Z]+) counter from " + cardName, "{C(0/0,-1,$2)}");
        actAbilCost = actAbilCost.replace("Remove two oil counters from ", "{C(0/0,-2,oil)}");
        actAbilCost = actAbilCost.replace("Remove three oil counters from ", "{C(0/0,-3,oil)}");
        actAbilCost = actAbilCost.replace("Exile " + cardName + " from your graveyard", "{E}");
        actAbilCost = actAbilCost.replace("Pay ", "{L:");
        actAbilCost = actAbilCost.replace(" life", "}");
        actAbilCost = actAbilCost.replace(cardName, "");
        actAbilCost = actAbilCost.replace(", ", "");
        actAbilCost = actAbilCost.replace(" ", "");
        actAbilCost = actAbilCost.replace(".", "");
        actAbilCost = actAbilCost.replace("/", "");

        if (type.contains("Planeswalker")) {
            actAbilCost = actAbilCost.replace("[", "");
            actAbilCost = actAbilCost.replace("]", "");
            actAbilCost = "{C(0/0," + actAbilCost + ",Loyalty)}";
        }

        actAbilCost = actAbilCost.concat(":");
        actAbilCost = "auto=" + actAbilCost;

        actAbilEffect = actAbil[1];
        actAbilEffect = ProcessEffect(actAbilEffect, cardName);

        return actAbilCost + actAbilEffect;
    }
}
