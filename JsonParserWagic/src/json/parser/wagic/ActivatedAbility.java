package json.parser.wagic;

import static json.parser.wagic.AutoEffects.numberMap;
import java.util.Map;
import java.util.LinkedHashMap;
import static json.parser.wagic.AutoEffects.processEffect;

/**
 * This class provides methods to determine and process activated abilities for Wagic cards.
 * It includes methods to identify activated abilities and format their costs and effects.
 * 
 * @author Eduardo_
 */
public class ActivatedAbility {

    /**
     * Determines if the card has an activated ability based on the oracle text.
     *
     * @param oracleBit The oracle text segment to check for activated abilities.
     * @param cardName  The name of the card.
     * @param type      The type of the card (e.g., Creature, Planeswalker).
     * @param subtype   The subtype of the card (e.g., Vehicle, Aura).
     * @return A string representing the activated ability, or an empty string if none is found.
     */
    static String DetermineActivatedAbility(String oracleBit, String cardName, String type, String subtype) {
        String[] activatedAbililty = oracleBit.split(":");
        if (activatedAbililty.length > 1) {
            return ActivatedAbililtyCost(activatedAbililty, cardName, type);
        } else {
            return "";
        }
    }

    /**
     * Processes the cost and effect of an activated ability.
     *
     * @param actAbil  An array containing the cost and effect of the activated ability.
     * @param cardName The name of the card.
     * @param type     The type of the card (e.g., Creature, Planeswalker).
     * @return A formatted string representing the activated ability cost and effect.
     */
    private static String ActivatedAbililtyCost(String[] actAbil, String cardName, String type) {
        String actAbilCost = actAbil[0];
        String actAbilEffect;

        // Define a map for cost replacements
        Map<String, String> costReplacements = new LinkedHashMap<>();
        costReplacements.put("{W/P}", "{p(W)}");
        costReplacements.put("{U/P}", "{p(U)}");
        costReplacements.put("{B/P}", "{p(B)}");
        costReplacements.put("{R/P}", "{p(R)}");
        costReplacements.put("{G/P}", "{p(G)}");
        costReplacements.put("Channel ", "autohand=");
        costReplacements.put("discard your hand" + cardName, "reject all(*|myhand)");
        costReplacements.put(", Discard " + cardName, "{discard}");
        costReplacements.put("Discard a card", "{D(*|myhand)}");
        costReplacements.put("Discard a land card", "{D(land|myhand)}");
        costReplacements.put("Discard three cards", "{D(*|myhand)}{D(*|myhand)}{D(*|myhand)}");
        costReplacements.put("Discard a creature card", "{D(creature|myhand)}");
        costReplacements.put("Exile " + cardName + " from your graveyard", "{E}");
        costReplacements.put("Exile " + cardName, "{E}");
        costReplacements.put("Forage", "{E(*|myGraveyard)}{E(*|myGraveyard)}{E(*|myGraveyard)}:\nauto={S(Food|myBattlefield)}");
        costReplacements.put("Sacrifice a creature with defender", "{S(creature[defender]|myBattlefield)}");
        costReplacements.put("Sacrifice " + cardName, "{S}");
        costReplacements.put("Sacrifice two creatures", "{S(creature|myBattlefield)}{S(creature|myBattlefield)}");
        costReplacements.put("Sacrifice an artifact creature", "{S(creature[artifact]|myBattlefield)}");
        costReplacements.put("Remove two oil counters from ", "{C(0/0,-2,oil)}");
        costReplacements.put("Remove three oil counters from ", "{C(0/0,-3,oil)}");
        costReplacements.put("Tap two untapped tokens you control", "{T(*[token]|mybattlefield)}{T(*[token]|mybattlefield)}");
        costReplacements.put("Pay ", "{L:");
        costReplacements.put(" life", "}");
        
        // Apply the replacements
        for (Map.Entry<String, String> entry : costReplacements.entrySet()) {
            actAbilCost = actAbilCost.replace(entry.getKey(), entry.getValue());
        }
        
        // Apply replacements for sacrifices and counters
        actAbilCost = actAbilCost.replace("/", "");
        actAbilCost = actAbilCost.replaceAll("Tap (an?|a) untapped ([a-zA-Z]+) you control", "{T(*[$2]|myBattlefield)}");        
        actAbilCost = actAbilCost.replaceAll("Sacrifice an ([a-zA-Z]+) or ([a-zA-Z]+)", "{S(*[$1;$2]|myBattlefield)}");        
        actAbilCost = actAbilCost.replaceAll("Sacrifice another ([a-zA-Z]+)", "{S(other $1|mybattlefield)}");        
        actAbilCost = actAbilCost.replaceAll("Sacrifice (an?|a) ([a-zA-Z]+)", "{S($1|myBattlefield)}");
        actAbilCost = actAbilCost.replaceAll("Remove (an?|a) ([a-zA-Z]+) counter from " + cardName, "{C(0/0,-1,$2)}");

        // Remove unnecessary characters
        actAbilCost = actAbilCost.replace(cardName, "");
        actAbilCost = actAbilCost.replace(", ", "");
        actAbilCost = actAbilCost.replace(" ", "");
        actAbilCost = actAbilCost.replace(".", "");

        // Special handling for Planeswalker loyalty costs
        if (type.contains("Planeswalker")) {
            actAbilCost = actAbilCost.replace("[", "");
            actAbilCost = actAbilCost.replace("]", "");
            actAbilCost = "{C(0/0," + actAbilCost + ",Loyalty)}";
        }

        actAbilCost = actAbilCost.concat(":");
        actAbilCost = "auto=" + actAbilCost;

        actAbilEffect = actAbil[1];
        actAbilEffect = processEffect(actAbilEffect, cardName);

        return actAbilCost + actAbilEffect;
    }
}
