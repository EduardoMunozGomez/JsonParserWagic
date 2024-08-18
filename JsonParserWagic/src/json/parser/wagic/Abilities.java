package json.parser.wagic;

// @author Eduardo
import java.util.Map;
import java.util.LinkedHashMap;
import org.json.simple.JSONArray;

public class Abilities {

    /**
     * Processes keyword abilities and custom abilities from the oracle text.
     * 
     * @param keywords   JSONArray of keywords.
     * @param oracleText The oracle text of the card.
     * @return A string of abilities separated by commas.
     */
    protected static String processKeywordsAbilities(JSONArray keywords, String oracleText) {
        StringBuilder abilities = new StringBuilder();

        // Process keyword abilities
        for (Object keyword : keywords) {
            String keywordString = ((String) keyword).toLowerCase();
            switch (keywordString) {
                case "flash":
                case "defender":
                case "flying":
                case "intimidate":
                case "first strike":
                case "double strike":
                case "deathtouch":
                case "hexproof":
                case "menace":
                case "indestructible":
                case "vigilance":
                case "reach":
                case "trample":
                case "lifelink":
                case "haste":
                case "islandwalk":
                case "swampwalk":
                case "mountainwalk":
                case "forestwalk":
                case "devoid":
                case "cycling":
                    abilities.append(keywordString).append(',');
                    break;
            }
        }

        // Process custom abilities
        abilities = new StringBuilder(processCustomAbilities(oracleText, abilities.toString()));
        
        // Remove trailing comma if exists
        if (abilities.length() > 0 && abilities.charAt(abilities.length() - 1) == ',') {
            abilities.setLength(abilities.length() - 1);
        }

        return abilities.toString();
    }

    /**
     * Processes custom abilities based on conditions in the oracle text.
     * 
     * @param oracleText The oracle text of the card.
     * @param abilities  The current string of abilities.
     * @return A string of abilities with custom abilities added.
     */
    protected static String processCustomAbilities(String oracleText, String abilities) {
        oracleText = oracleText.toLowerCase();

        // Define a map of conditions and abilities
        Map<String, String> abilityMap = new LinkedHashMap<>();
        abilityMap.put("you may look at the top card of your library any time", "showfromtoplibrary,");
        abilityMap.put("you have no maximum hand size", "nomaxhand,");
        abilityMap.put("can't be countered", "nofizzle,");
        abilityMap.put("can't be blocked.", "unblockable,");
        abilityMap.put("can't be blocked by more than one creature", "oneblocker,");
        abilityMap.put("toxic 1", "poisontoxic,");
        abilityMap.put("toxic 2", "poisontwotoxic,");
        abilityMap.put("toxic 3", "poisonthreetoxic,");
        abilityMap.put("can't block.", "cantblock,");
        abilityMap.put("attacks each turn if able.", "mustattack,");
        abilityMap.put("attacks each combat if able.", "mustattack,");
        abilityMap.put("power can't block it.", "strong,");
        abilityMap.put("can block only creatures with flying.", "cloud,");
        abilityMap.put("all creatures able to block ", "lure,");
        abilityMap.put("counter on it for each color of mana spent to cast it", "sunburst,");
        abilityMap.put("blocks each turn if able", "mustblock,");
        abilityMap.put("blocks each combat if able", "mustblock,");
        abilityMap.put("players can't gain life", "nolifegain,nolifegainopponent,");
        abilityMap.put("opponents can't gain life", "nolifegainopponent,");
        abilityMap.put("doesn't untap during your untap step", "doesnotuntap,");
        abilityMap.put("protection from", "protection from,");
        abilityMap.put("affinity for artifacts", "affinityartifacts,");
        abilityMap.put("choose a background", "chooseabackground,");
        abilityMap.put("modular", "modular\nmodular=");
        abilityMap.put("changeling", "changeling");
        abilityMap.put("partner", "partner");

        // Iterate over the map and add the corresponding abilities if conditions are met
        for (Map.Entry<String, String> entry : abilityMap.entrySet()) {
            if (oracleText.contains(entry.getKey())) {
                abilities += entry.getValue();
            }
        }

        // Remove trailing comma if exists
        abilities = abilities.replaceAll(",$", "");

        return abilities;
    }
}
