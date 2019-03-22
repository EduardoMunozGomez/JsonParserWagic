package JasonParserWagic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Eduardo
 */
public class OracleText {

    static void parseOracleText(String oracleText, String cardName, String type, String subtype, String power, String manaCost) {
        String abilities = "";
        String[] oracleSplit;
        // Remove remainder text
        Matcher textBetweenPar = Pattern.compile("\\((.*?)\\)").matcher(oracleText);
        while (textBetweenPar.find()) {
            oracleText = oracleText.replace(textBetweenPar.group(1), "");
        }
        oracleText = oracleText.replace("()", "");
        // Remove comma from Legendaries
        if (cardName.contains(",")) {
            Matcher legedName = Pattern.compile(cardName).matcher(oracleText);
            while (legedName.find()) {
                oracleText = oracleText.replace(legedName.group(), cardName.replace(",", ""));
            }
        }
        // Keywords (abilities=)
        if (type.contains("Creature")) {
            abilities = Abilities.processAbilities(oracleText);
        }
        if (type.contains("Enchantment")) {
            abilities = Abilities.processEnchantmentAbilities(oracleText);
        }
        if (type.contains("Instant") || type.contains("Sorcery")) {
            abilities = Abilities.processInstantOrSorceryAbilities(oracleText);
        }
        if (type.contains("Land")) {
            abilities = Abilities.processLandAbilities(oracleText);
        }
        if (!abilities.isEmpty()) {
            System.out.println("abilities=" + abilities.trim());
        }
        // Auto lines
        oracleSplit = oracleText.split("\n");
        for (String oracleBit : oracleSplit) {
            // Evergreen mechanics
            //String activatedAbility = AutoLine.processOracleActivatedAbilityCost(oracleBit, cardName, type, subtype);
            check(AutoEffects.processOracleBit(oracleBit, cardName, type, subtype));
            check(AutoLine.processOracleTextManaAbility(oracleBit, subtype));
            //check(AutoLine.processOracleTextETB(oracleBit, cardName, type, subtype));
            check(AutoLineGRN.processOracleUndergrowth(oracleBit));
            check(AutoLineGRN.processOracleKicker(oracleBit));

            check(AutoLine.processOracleTextCast(oracleBit));
            check(AutoLine.processOracleTextCombatDamage(oracleBit));
            //check(AutoLine.processOracleTextFirebreting(oracleBit, cardName));
            check(AutoLine.processOracleTextOppCasts(oracleBit));
            //check(AutoLine.processOracleTextThisDies(oracleBit, cardName));
            //check(AutoLine.processOracleTextTargeted(oracleBit, cardName));
            //check(AutoLine.processOracleTextUpkeep(oracleBit));
            check(AutoLine.processOracleTextWeak(oracleBit));
            //check(AutoLine.processOracleTextAttacks(oracleBit, cardName, power));
            check(AutoLine.processOracleDiscarded(oracleBit));
            check(AutoLine.processOracleTextTakeControl(oracleBit));
            check(AutoLine.processOracleScry(oracleBit));
            check(AutoLine.processOracleCantBeBlockedBy(oracleBit));
            check(AutoLine.processOracleLord(oracleBit, type));
            check(AutoLine.processOracleTriggers(oracleBit, cardName, type, subtype, power));
            check(AutoLine.processAsLongAs(oracleBit, type));
            check(AutoLine.processForEach(oracleBit, type));
            //check(AutoLineGRN.processOracleConvoke(oracleBit));
            //check(AutoLineGRN.processOracleSurveil(oracleBit));
            //check(AutoLineGRN.processOracleAddendum(oracleBit));
            //check(AutoLineGRN.processOracleRiot(oracleBit));
            //check(AutoLineGRN.processOracleSpectacle(oracleBit));
            //INSTANT, SORCERY
            if (type.contains("Instant") || type.contains("Sorcery")) {
                check(AutoLine.processOracleTextMyTarget(oracleBit, "InstantOrSorcery", subtype));
                check(AutoLine.processOracleTextExileDestroyDamage(oracleBit, type));
                check(AutoLine.processOracleREPLACERAuraEquipBonus(oracleBit, "InstantOrSorcery"));
                check(AutoLine.processOracleCreate(oracleBit), true);
                check(AutoLine.processOracleGainLife(oracleBit), true);
                check(AutoLine.processOracleDraw(oracleBit));
                check(AutoLine.processOraclePutA(oracleBit, type), true);
                check(AutoLine.processOracleChooseOneOrBoth(oracleBit));
                //check(AutoLine.processOracleInstantSorcery(oracleBit));
                //check(AutoLine.processOracleExileDeath(oracleBit), true);

                check(AutoLineGRN.processOracleJumpStart(oracleBit, manaCost));
            }
            // AURA
            if (subtype.contains("Aura")) {
                check(AutoLine.processOracleTextMyTarget(oracleBit, subtype, subtype));
                String auraEquipBonus = AutoLine.processOracleREPLACERAuraEquipBonus(oracleBit, subtype);
                if (!auraEquipBonus.isEmpty()) {
                    check(auraEquipBonus);
                }
            }
            // EQUIPMENT
            if (subtype.contains("Equipment")) {
                check(AutoLine.processOracleTextAuraEquipBonus(oracleBit, "Equipment"));
                check(AutoLine.processOracleTextEquipCost(oracleBit));
            }
        }
    }

    public OracleText() {
    }

    static void check(String outcome, boolean... needAuto) {
        if (outcome.length() > 0) {
            outcome += "\n";
            if (needAuto.length > 0) {
                outcome = "auto=" + outcome;
            }
        }

        System.out.print(outcome);
    }

}
