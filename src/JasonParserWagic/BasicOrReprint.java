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
public class BasicOrReprint {

    protected static boolean isReprintOrBasic(String name) {
        String[] reprints
                = {"Battle Mastery",
                    "Death Wind",
                    "Dragon Fodder",
                    "Duress",
                    "Evolving Wilds",
                    "Explosive Vegetation",
                    "Gravepurge",
                    "Kindled Fury",
                    "Mind Rot",
                    "Naturalize",
                    "Negate",
                    "Pacifism",
                    "Spidersilk Net",
                    "Summit Prowler",
                    "Tormenting Voice",
                    "Ultimate Price"};

        String[] basics = {"Plains", "Island", "Swamp", "Mountain", "Forest"};

        for (String reprint : reprints) {
            if (name.equals(reprint)) {
                return true;
            }
        }

        for (String basic : basics) {
            if (name.equals(basic)) {
                return true;
            }
        }

        return false;
    }
}
