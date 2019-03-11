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
    public static void generateCardDat(Object primitive, Object id, Object rare) {
        String rarity = rare.toString();

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
            if (primitive.toString().equals(basic)) {
                rarity = "L";
            }
        }

        System.out.println("[card]");
        System.out.println("primitive=" + primitive.toString());
        System.out.println("id=" + id.toString());
        System.out.println("rarity=" + rarity);
        System.out.println("[/card]");
    }
}
