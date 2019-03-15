package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class CardDat {

    //[card]
    //primitive=Abomination of Gudul 
    //id=386463
    //rarity=C
    //[/card]
    public static void generateCardDat(String primitive, Object id, String rarity) {

        switch (rarity) {
            case "common":
                rarity = "C";
                break;
            case "uncommon":
                rarity = "U";
                break;
            case "rare":
                rarity = "R";
                break;
            case "mythic":
                rarity = "M";
                break;
        }

        String[] basics = {"Plains", "Island", "Swamp", "Mountain", "Forest"};

        for (String basic : basics) {
            if (primitive.equals(basic)) {
                rarity = "L";
            }
        }

        System.out.println("[card]");
        System.out.println("primitive=" + primitive);
        System.out.println("id=" + id);
        System.out.println("rarity=" + rarity);
        System.out.println("[/card]");
    }
}
