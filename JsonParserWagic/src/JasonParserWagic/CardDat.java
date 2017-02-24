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
public class CardDat {

    //[card]
    //primitive=Abomination of Gudul 
    //id=386463
    //rarity=C
    //[/card]
    public static void generateCardDat(Object primitive, Object id, Object rare) {
        String rarity = (String) rare;

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
        System.out.println("primitive=" + (String) primitive);
        System.out.println("id=" + Long.toString((Long) id));
        System.out.println("rarity=" + rarity);
        System.out.println("[/card]");
    }
}
