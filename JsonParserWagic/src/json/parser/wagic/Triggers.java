package json.parser.wagic;

/**
 *
 * @author Eduardo_
 */
public class Triggers {

    static String processTriggers(String oracleText, String cardName, String type, String subtype, String power) {

        String trigger = "";
        String triggerEffect;

        try {
            // Trigger conditions
            if ((oracleText.contains("Whenever")
                    || oracleText.contains("When")
                    || oracleText.contains("As " + cardName)
                    || oracleText.contains("As long as")
                    || oracleText.contains("At the beginning")
                    || oracleText.contains(cardName + " enters")
                    || oracleText.contains("Mentor")
                    || oracleText.contains("Raid ")
                    || oracleText.toLowerCase().contains("ward "))) {

                if (oracleText.contains("you cast")) {
                    return AutoLine.cast(oracleText, cardName);
                }
                trigger = trigger.replace("your second main phase, if " + cardName + " is tapped", "each my secondmain sourceUntapped:");

                // The comma limits the trigger
                if (oracleText.contains(",")) {
                    trigger = oracleText.substring(0, oracleText.indexOf(","));
                    triggerEffect = oracleText.substring(oracleText.indexOf(",") + 2);
                } else {
                    triggerEffect = oracleText.replace(cardName + " enters", "");
                }

                trigger = trigger.replace("Enrage - ", "");
                trigger = trigger.replace("Raid - ", "");
                trigger = trigger.replace("Valiant - ", "");
                trigger = trigger.replace("Threshold - ", "_THRESHOLD_");
                trigger = trigger.replace("Eerie - ", "_CONSTELLATION_\nauto=_EERIE_");

                trigger = trigger.replace("a creature you control deals combat damage to a player", "combatdamagefoeof(player) from(creature|mybattlefield):");
                trigger = trigger.replace("Whenever an enchantment you control enters and whenever you fully unlock a Room", "");
                trigger = trigger.replace("one or more +1/+1 counters are put on", "totalcounteradded(1/1) from(this):");
                trigger = trigger.replace("a creature you control enters", "movedto(creature|myBattlefield):");
                trigger = trigger.replace("a land you control enters", "_LANDFALL_");
                trigger = trigger.replace("a creature you control becomes blocked", "combat(blocked) source(creature|mybattlefield):");
                trigger = trigger.replace("you cast your second spell each turn", "@movedto(*|mystack) restriction{thisturn(*|mystack)~equalto~1}:");
                trigger = trigger.replace("As long as seven or more cards are in your graveyard", "");
                trigger = trigger.replaceAll(" attacks while you control a ([a-zA-Z]+)", "@combat(attacking) source(this) restriction{type($1|myBattlefield)~morethan~0}:");
                trigger = trigger.replaceAll("When this Class becomes level ([0-9]+)", "@counteradded(0/0.1.Level) from(this) restriction{compare(hascntlevel)~equalto~$1}:");
                trigger = trigger.replace("Whenever this creature becomes the target of a spell or ability you control for the first time each turn", "_VALIANT_");
                trigger = trigger.replace("Whenever " + cardName + " becomes the target of a spell or ability you control for the first time each turn", "_VALIANT_");
                trigger = trigger.replace(cardName + " deals combat damage to a player", "combatdamaged(player) from(this):");
                trigger = trigger.replace("this creature deals combat damage to a player", "combatdamaged(player) from(this):");
                trigger = trigger.replaceAll(" or another ([a-zA-Z]+) you control enters", "@movedTo(other $1|myBattlefield):");
                trigger = trigger.replaceAll("another ([a-zA-Z]+) you control enters", "movedTo(other $1|myBattlefield):");
                trigger = trigger.replaceAll("with ([0-9]+) counters on it", "counter(1/1,$1)");

                trigger = trigger.replace("you put one or more +1/+1 counters on a creature you control", "totalcounteradded(1/1) from(creature|mybattlefield):");
                trigger = trigger.replace("as long as it is your turn", "this(variable{controllerturn}>0)");
                trigger = trigger.replace("As long as you've lost life this turn", "aslongas(variable{oplifelost}>0) ");
                trigger = trigger.replace(cardName + " enters tapped", "tap(noevent)");
                trigger = trigger.replace("When " + cardName + " enters", "");
                trigger = trigger.replace("When this creature enters", "");
                trigger = trigger.replace("Whenever " + cardName + " enters ", "");
                trigger = trigger.replace("Whenever " + cardName + " attacks", "_ATTACKING_");
                trigger = trigger.replace("Whenever " + cardName, "");
                trigger = trigger.replace(cardName + " enters", "");
                trigger = trigger.replace("and when you sacrifice it", "\nauto=@sacrificed(this):");
                trigger = trigger.replace("a player casts their second spell each turn", "movedto(*|mystack) restriction{thisturn(*|mystack)~equalto~1}:\nauto=@movedto(*|opponentStack) restriction{thisturn(*|opponentStack)~equalto~1}:\"");
                trigger = trigger.replace("another creature you control enters", "movedto(other creature|myBattlefield):");
                trigger = trigger.replace("you gain life", "lifeof(player) from(*[-lifefaker]|*):");
                trigger = trigger.replace("you may pay ", "pay(");
                trigger = trigger.replace(". If you do ", "):");
                trigger = trigger.replace("you draw a card", "drawof(player):");

                trigger = trigger.replace("Whenever you draw your second card each turn", "_SECOND_DRAW_");
                trigger = trigger.replace("you sacrifice an artifact or creature", "sacrificed(*[creature;artifact]|myBattlefield):");
                trigger = trigger.replaceAll("you sacrifice (an?|a) ([a-zA-Z]+)", "sacrificed($2|myBattlefield):");
                trigger = trigger.replace(cardName + " becomes tapped", "tapped(this):");
                // Moved to battlefield
                trigger = trigger.replace("one or more tokens you control enter", "movedTo(*[token]|myBattlefield):");
                trigger = trigger.replaceAll("one or more ([a-zA-Z]+) you control enter", "movedTo($1|myBattlefield):");
                trigger = trigger.replaceAll("a ([a-zA-Z]+) enters the battlefield under your control", "movedTo($1|myBattlefield):");
                trigger = trigger.replaceAll("another ([a-zA-Z]+) enters under your control", "movedTo(other $1|myBattlefield):");
                trigger = trigger.replaceAll("a ([a-zA-Z]+) enters under an opponent's control", "movedTo($1|opponentBattlefield):");
                trigger = trigger.replaceAll("a ([a-zA-Z]+) enters", "movedTo($1|battlefield):");

                trigger = trigger.replaceAll("a ([a-zA-Z]+) you control attacks", "combat(attacking) source($1|myBattlefield):");
                trigger = trigger.replace("or dies", "\nauto=_DIES_");
                trigger = trigger.replace("or attacks", "\nauto=_ATTACKING_");
                trigger = trigger.replace("Whenever this creature attacks", "_ATTACKING_");
                trigger = trigger.replace(cardName + " and at least two other creatures attack", "_ATTACKING_restriction{type(other creature[attacking]|myBattlefield)~morethan~1}:");
                trigger = trigger.replace("Whenever " + cardName + " deals combat damage to a player", "combatdamaged(player) from(this):");
                trigger = trigger.replace("a Samurai or Warrior you control attacks alone", "combat(attackedalone) source(*[Samurai;Warrior]|myBattlefield):all(trigger[to])");

                trigger = trigger.replace("When " + cardName + " dies", "_DIES_");
                trigger = trigger.replace("Whenever " + cardName + " dies", "_DIES_");
                trigger = trigger.replace("When this creature dies", "_DIES_");
                trigger = trigger.replaceAll("([a-zA-Z]+) is put into a graveyard from the battlefield", "movedto($1|graveyard) from(battlefield):");
                trigger = trigger.replace("a creature dies", "movedTo(creature|graveyard) from(battlefield):");
                trigger = trigger.replace("another creature dies", "movedTo(other creature|graveyard) from(battlefield):");
                trigger = trigger.replace("another creature or planeswalker you control dies", "movedTo(other *[creature;planeswalker]|graveyard) from(myBattlefield):");
                trigger = trigger.replace("a creature you control dies", "movedTo(creature|graveyard) from(mybattlefield):");
                trigger = trigger.replace("a creature an opponent controls dies", "movedTo(creature|graveyard) from(opponentbattlefield):");
                trigger = trigger.replace("a creature or planeswalker you control dies", "movedTo(creature,planeswalker|graveyard) from(mybattlefield):");
                trigger = trigger.replace(" dies", "movedTo(|graveyard) from(battlefield):");
                // Phases
                trigger = trigger.replace("At the beginning of ", "@");
                trigger = trigger.replace("your upkeep", "each my upkeep:");
                trigger = trigger.replace("your draw step", "each my draw:");
                trigger = trigger.replace("your firt main phase", "each my firstmain:");
                trigger = trigger.replace("Survival - ", "");
                trigger = trigger.replace("your second main phase", "each my secondmain:");
                trigger = trigger.replace("combat on your turn", "each my combatbegins:");
                trigger = trigger.replace("your end step", "each my endofturn:");
                trigger = trigger.replace("each end step", "each endofturn:");
                trigger = trigger.replace("your next end step", "my endofturn once");

                trigger = trigger.replace("If you attacked with a creature this turn", "if raid then ");
                trigger = trigger.replace("if it was kicked", "kicked ");
                trigger = trigger.replace("is turned face up", "autofaceup=");

                trigger = trigger.replace(cardName + " is dealt damage", "damaged(this):");
                trigger = trigger.replace("with three or more creatures", "restriction{type(creature[attacking]|myBattlefield)~morethan~2}:");

                trigger = trigger.replace("mana value", "manacost");
                trigger = trigger.replace("becomes the target of a spell or ability an opponent controls", "targeted(this|mybattlefield) from(*|opponentbattlefield,opponenthand,opponentstack,opponentgraveyard,opponentexile,opponentlibrary):");
                trigger = trigger.replace(cardName + " becomes the target of a spell or ability", "targeted(this):");
                trigger = trigger.replace("Whenever another", "@other");
                trigger = trigger.replace("Whenever you attack", "@each my blockers:");
                trigger = trigger.replace("Whenever you ", "@");
                trigger = trigger.replace("Whenever a ", "@");
                trigger = trigger.replace("Whenever ", "@");
                trigger = trigger.replace("When ", "@");
                trigger = trigger.replace(cardName, "");

                triggerEffect = AutoEffects.processEffect(triggerEffect, cardName);
                trigger = "auto=" + trigger + triggerEffect;

            }
        } catch (Exception e) {
        }
        return trigger;
    }
}
