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
            case "Common":
                rarity = "C";
                break;
            case "Uncommon":
                rarity = "U";
                break;
            case "Rare":
                rarity = "R";
                break;
            case "Basic Land":
                rarity = "L";
                break;
            default:
                rarity = "M";
                break;
        }

        System.out.println("[card]");
        System.out.println("primitive=" + primitive.toString());
        System.out.println("id=" + id.toString());
        System.out.println("rarity=" + rarity);
        System.out.println("[/card]");
    }
}
