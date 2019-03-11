package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class BasicOrReprint {

    protected static boolean isReprintOrBasic(String name) {
        String[] reprints = {
            "Plains", "Island", "Swamp", "Mountain", "Forest",
            "Concordia Pegasus",
            "Act of Treason",
            "Gift of Strength",
            "Mammoth Spider",
            "Root Snare",
            "Tower Defense",
            "Absorb",
            "Mortify",
            "Junktroller",
            "Scrabbling Claws",
            "Azorious Guildgate",
            "Blood Crypt",
            "Breeding Pool",
            "Gateway Plaza",
            "Godless Shrine",
            "Gruul Guildgate",
            "Hallowed Fountain",
            "Orzhov Guildgate",
            "Rakdos Guildgate",
            "Simic Guildgate",
            "Stomping Ground"
        };
        for (String reprint : reprints) {
            if (name.equals(reprint)) {
                return true;
            }
        }
        return false;
    }
}
