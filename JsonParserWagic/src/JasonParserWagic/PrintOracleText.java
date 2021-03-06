package JasonParserWagic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Eduardo
 */
public class PrintOracleText {

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
            check(AutoEffects.processOracleBit(oracleBit, cardName, type, subtype));
            check(AutoLine.processOracleTextManaAbility(oracleBit, subtype));
            check(AutoLineGRN.processOracleUndergrowth(oracleBit));
            check(AutoLineGRN.processOracleKicker(oracleBit, cardName));
            check(AutoLine.processOracleTextCast(oracleBit, cardName));
            check(AutoLine.processOracleTextCombatDamage(oracleBit));

            check(AutoLine.processOracleTextOppCasts(oracleBit));
            check(AutoLine.processOracleTextWeak(oracleBit));
            check(AutoLine.processOracleDiscarded(oracleBit));
            check(AutoLine.processOracleTextTakeControl(oracleBit));
            check(AutoLine.processOracleScry(oracleBit));
            check(AutoLine.processOracleCantBeBlockedBy(oracleBit));
            check(AutoLine.processOracleLord(oracleBit, type));
            check(AutoLine.processOracleTriggers(oracleBit, cardName, type, subtype, power));
            check(AutoLine.processAsLongAs(oracleBit, cardName));
            check(AutoLine.processForEach(oracleBit, type));
            check(AutoLineGRN.processOracleConvoke(oracleBit));
            check(AutoLineGRN.processOracleSurveil(oracleBit));
            check(AutoLineGRN.processOracleAddendum(oracleBit));
            check(AutoLineGRN.processOracleRiot(oracleBit));
            check(AutoLineGRN.processOracleSpectacle(oracleBit));
            check(AutoLineGRN.processOracleAscend(oracleBit));
            check(AutoLineGRN.processOracleAmass(oracleBit));
            check(AutoLineGRN.processOraclePartner(oracleBit));
            check(AutoLineGRN.processOracleProliferate(oracleBit));
            
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
            // SAGA
            if (subtype.contains("Saga")) {
                check(AutoLine.processOracleEpicSaga(oracleBit, subtype));
                String epicSaga = "";
                //= AutoLine.processOracleREPLACERAuraEquipBonus(oracleBit, subtype);
                if (!epicSaga.isEmpty()) {
                    check(epicSaga);
                }
            }
        }
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
