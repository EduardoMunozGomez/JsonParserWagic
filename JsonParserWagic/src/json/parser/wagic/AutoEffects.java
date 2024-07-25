package json.parser.wagic;

// @author Eduardo
import java.util.HashMap;
import java.util.Map;

public class AutoEffects {

    static Map<String, Integer> numberMap = new HashMap<>();

    static String ProcessEffect(String oracleBit, String cardName) {
        String effect;
        effect = oracleBit;
        String manaOfAnyColor = "\nAdd{W}\n"
                + "auto={T}:Add{U}\n"
                + "auto={T}:Add{B}\n"
                + "auto={T}:Add{R}\n"
                + "auto={T}:Add{G}";
        effect = effect.toLowerCase();
        cardName = cardName.toLowerCase();

        if (effect.contains("offspring ")) {
            return "";
        }
        if (effect.contains("create ")) {
            return AutoLine.Create(effect);
        }
        if (effect.contains("scry ")) {
            AutoLine.scry(effect);
        }
        if (effect.contains("add ")) {
            return AutoLine.ManaAbility(oracleBit, "");
        }

        effect = effect.replaceAll("other ([a-zA-Z]+) you control", "lord(other $1|myBattlefield");
        effect = effect.replace("from your graveyard", "|mygraveyard)");
        effect = effect.replace("from your graveyard", "|mygraveyard)");
        effect = effect.replace("if you gained life this turn", "if compare(lifegain)~morethan~0 then ");
        effect = effect.replace("gift a card", "");
        effect = effect.replace("exile the top card of your library. you may play it this turn.", "_EXILETOP_");
        effect = effect.replace("exile the top card of your library. until the end of your next turn, you may play that card.", "_EXILEUENT_");
        effect = effect.replace("level 2", "name(Level 2) counter(0/0,1,Level) asSorcery this(variable{hascntlevel}=1)");
        effect = effect.replace("level 3", "name(Level 3) counter(0/0,1,Level) asSorcery this(variable{hascntlevel}=2)");
        effect = effect.replace("threshold ", "_THRESHOLD_");
        effect = effect.replace("connives", "_CONNIVES_");
        effect = effect.replace("top of your library", "moveTo(mylibrary)");
        effect = effect.replace("transform ", "flip(backside)");
        effect = effect.replace("from your graveyard the battlefield", "moveTo(battlefield) from(myGraveyard)");
        effect = effect.replace("exile the top card of your library. you may play it this turn", "__PLAY_TOP_FROM_EXILE__");
        effect = effect.replaceAll("ward \\{(\\d+)\\}", "_WARD$1_");
        effect = effect.replace("choose one or both -", "");
        effect = effect.replaceAll("amass (\\d+)", "_AMASS$1_");
        effect = effect.replace("until end of turn", "ueot");
        effect = effect.replace("until your next turn", "uynt");
        effect = effect.replace("activate only once each turn.", "limit:1");
        effect = effect.replace("activate only as a sorcery", "asSorcery");
        effect = effect.replace("as long as an opponent has three or more poison counters,", "this(variable{opponentpoisoncount}>2)");
        effect = effect.replace("has three or more poison counters", "this(variable{opponentpoisoncount}>2)");
        effect = effect.replace("enlist ", "_ENLIST_");
        effect = effect.replace("kicker ", "kicker=");
        effect = effect.replace("if it was kicked,", "if paid(kicker) then ");
        effect = effect.replace("proliferate", "_PROLIFERATE_");
        effect = effect.replace("exile this saga, then return it to the battlefield transformed under your control", "moveto(exile) and!( moveto(mybattlefield) and!( transforms((,newability[flip(backside)])) oneshot )! )!");

        effect = effect.replace("you win the game", "wingame");
        effect = effect.replace("this ability triggers only once each turn.", "turnlimited:");
        effect = effect.replace("only once each turn", "turnlimited:");
        effect = effect.replace("activate only during your turn", "myturnonly");
        effect = effect.replace("that was dealt damage this turn", "[damaged]");
        effect = effect.replace("+1/+1 counter", "counter(1/1)");
        effect = effect.replace("loyalty counter", "counter(0/0,1,Loyalty)");
        effect = effect.replace("on the bottom of its owner's library", "bottomoflibrary");
        effect = effect.replaceAll("creature with power ([0-9]+) or greater", "creature[power>=$1]");
        effect = effect.replaceAll("creature with power ([0-9]+) or less", "creature[power<=$1]");
        effect = effect.replaceAll("creature with toughness ([0-9]+) or greater", "creature[toughness>=$1]");
        effect = effect.replaceAll("creature with toughness ([0-9]+) or less", "creature[toughness<=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+) or more", "[manacost>=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+) or less", "[manacost<=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+)", "[manacost=$1]");

        effect = effect.replace("return target nonland permanent you don't control to its owner's hand", "target(-land|opponentBattlefield) moveTo(ownerHand)");
        effect = effect.replace("to its owner's hand", "moveto(ownerhand)");
        effect = effect.replace("historic", "artifact,*[legendary],enchantment[saga]");
        effect = effect.replace("return it to the battlefield tapped under its owner's control at the beginning of the next end step", "(blink)ueot");
        effect = effect.replace("creatures", "creature");
        effect = effect.replace("nonartifact", "-artifact");
        effect = effect.replace("noncreature", "-creature");
        effect = effect.replace("nonland", "-land");

        effect = effect.replace("activate this ability only once each turn", "limit:1");
        effect = effect.replace("sacrifice " + cardName, "{S}:");
        effect = effect.replace("sacrifice another creature", "{S(other creature|mybattlefield)}");
        effect = effect.replace("defending player controls", ")|opponentBattlefield)");
        effect = effect.replace("your opponents control", ")|opponentBattlefield)");
        effect = effect.replace("to any target", "target(creature,player)");
        effect = effect.replace("if you do, ", "");
        effect = effect.replace("return target creature an opponent controls to its owner's hand", "moveto(ownerHand) target(creature|opponentBattlefield)");
        effect = effect.replace("return target creature to its owner's hand", "moveto(ownerHand) target(creature)");
        effect = effect.replace("exile target nonland permanent an opponent controls until " + cardName + " leaves the battlefield.", "(blink)forsrc target(*[-land]|opponentbattlefield)");
        effect = effect.replace("you may search your library for a basic land card or gate card, reveal it, put it into your hand, then shuffle your library.", "target(land[basic;gate]|mylibrary) moveto(myhand)");
        effect = effect.replace("you may have it fight target creature you don't control", "may target(creature|opponentbattlefield) dynamicability<!powerstrike eachother!>");
        effect = effect.replace("activate this ability only any time you could cast a sorcery", "asSorcery");
        effect = effect.replace(cardName + " from your graveyard to your hand", "moveto(myhand) all(this)");
        effect = effect.replace("you may cast an instant or sorcery card from your hand without paying its mana cost", "may castcard(normal) notatarget(instant,sorcery|myhand)");
        effect = effect.replace(cardName + " enters the battlefield with a +1/+1 counter on it for each color of mana spent to cast it.", "counter(1/1,sunburst)");
        effect = effect.replace("as long as you've cast an instant or sorcery spell this turn, " + cardName, "auto=aslongas(instant,sorcery|mystack) >1");
        effect = effect.replace("legendary permanent that's an artifact, creature, or enchantment.", "artifact[legendary], creature[legendary], enchantment[legendary])");
        effect = effect.replace("each opponent discards a card", "ability$!name(discard) reject notatarget(*|myhand)!$ opponent");
        effect = effect.replace("each opponent loses 1 life", "life:-1 opponent");
        effect = effect.replace("to your hand", "moveto(ownerhand)");
        effect = effect.replace("whenever this creature attacks, ", "_ATTACKING_");
        effect = effect.replace("with an oil counter on it", "counter(0/0,1,oil)");
        effect = effect.replace("put a +1/+1 counter", "counter(1/1)");

        effect = effect.replaceAll("(\\d+) (.*?) counters on it", "(0/0,$1,$2)");
        effect = effect.replace("enters the battlefield under your control, ", "@movedTo(|myBattlefield):");
        effect = effect.replace("attacking creature without flying", "creature[attacking;-flying]");
        effect = effect.replace("can't be blocked this turn", ") unblockable");
        effect = effect.replace("can't be blocked", "unblockable");
        effect = effect.replace("can't block this turn", "cantblock");
        effect = effect.replace("another target ", "target(other ");
        effect = effect.replace("another ", "other ");
        effect = effect.replace("target ", "target(");
        effect = effect.replace("prevent the next", "prevent:");
        effect = effect.replace("to the battlefield.", "moveTo(mybattlefield)");
        effect = effect.replace("creature doesn't untap during its controller's next untap step", "freeze");

        effect = effect.replace("nonland permanent", "*[-land;-instant;-sorcery]");
        effect = effect.replace("permanents", "*[-instant;-sorcery]");
        effect = effect.replace("permanent", "*[-instant;-sorcery]");

        effect = effect.replace("you don't control", "|opponentBattlefield)");
        effect = effect.replace("creature you control ", "creature|myBattlefield");
        effect = effect.replace("each white creature", "(creature[white]");
        effect = effect.replace(" attacking creature with lesser power", "creature[attacking;power<=])");
        effect = effect.replace("nonland permanent an opponent controls", "*[-land]|opponentbattlefield)");
        effect = effect.replace("if you attacked with a creature this turn, ", "if raid then ");
        effect = effect.replace(" an opponent controls", "|opponentBattlefield)");
        effect = effect.replace(" you control", "|myBattlefield");
        effect = effect.replace("loses all abilities", "loseabilities");
        effect = effect.replace("discard a card", "ability$!name(discard) reject notatarget(*|myhand)!$");
        effect = effect.replace("discards a card", "ability$!name(discard) reject notatarget(*|myhand)!$");
        effect = effect.replace("discards two cards", "ability$!name(discard) reject notatarget(<2>*|myhand)!$");

        effect = effect.replace("sacrifices a creature", "ability$!sacrifice notatarget(creature|mybattlefield)!$ targetedplayer");
        effect = effect.replace("exile " + cardName, "\nexiledeath");
        effect = effect.replace(" card from your graveyard", "|mygraveyard)");
        effect = effect.replace("take an extra turn after this one", "turns:+1");
        // Deplete (Mill)
        effect = effect.replace("mills a card", "deplete:1");

        for (Map.Entry<String, Integer> entry : numberMap.entrySet()) {
            effect = effect.replace("with " + entry.getKey() + " oil counters on it", "counter(0/0," + entry.getValue() + ",oil)");
            effect = effect.replace("put " + entry.getKey() + " +1/+1 counters on ", "counter(1/1," + entry.getValue() + ")");
            effect = effect.replace("mills " + entry.getKey() + " cards", "deplete:" + entry.getValue());
            effect = effect.replace("mill " + entry.getKey() + " cards", "deplete:" + entry.getValue());
            effect = effect.replace("draw " + entry.getKey() + " cards", "draw:" + entry.getValue());
            effect = effect.replace("draws " + entry.getKey() + " cards", "draw:" + entry.getValue());
            effect = effect.replace("up to " + entry.getKey(), "<upto:" + entry.getValue() + ">");
        }
        effect = effect.replace("mill a card", "deplete:1");

        effect = effect.replaceAll("you gain ([0-9]+) life", "life:$1");
        effect = effect.replaceAll("you lose ([0-9]+) life", "life:-$1");
        // Draw Monsta Cardo!
        effect = effect.replace("draw a card", "draw:1");
        effect = effect.replace("for each creature ", "type:creature:");
        effect = effect.replace("each ", "all(");
        effect = effect.replace(cardName + " enters the battlefield tapped.", "auto=tap(noevent)");
        effect = effect.replace(cardName + " gains ", "");
        effect = effect.replace(cardName + " gets ", "");
        effect = effect.replace(cardName + " has ", "");
        effect = effect.replace(" then shuffle", "");
        effect = effect.replace(" target creature", "target(creature)");

        effect = effect.replaceAll("deals ([0-9]+|x) damage", "damage:$1");

        effect = effect.replace("if you do, ", ":");
        effect = effect.replace("when you do, ", ":");
        effect = effect.replace("you may pay", "pay(");
        effect = effect.replace("you may", "may");
        effect = effect.replace("add one mana of any color", manaOfAnyColor);

        effect = effect.replaceAll(", and that creature gains| and gains | and gain | and gets | and has | and you | and ", " && ");

        effect = effect.replace(" with a ", "");
        effect = effect.replace(" put a ", " ");
        effect = effect.replace("return ", "");
        effect = effect.replaceAll(" gains | gain | gets | get | has ", ") ");
        effect = effect.replaceAll("gains |gain |gets |get |has ", ") ");
        effect = effect.replace(", then ", " && ");

        effect = effect.replace(" it) ", "");
        effect = effect.replace(" it ", "");
        effect = effect.replace(" on ", "");
        effect = effect.replace(" or ", ",");
        effect = effect.replace(" to ", " ");

        effect = effect.replace("} {", "}{");
        effect = effect.replace(":|", ":");
        effect = effect.replace("+", "");
        effect = effect.replace(".", "");
        effect = effect.replace(cardName, "");

        effect = effect.trim();

        return effect;
    }
}
