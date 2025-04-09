package json.parser.wagic;

// @author Eduardo
import java.util.HashMap;
import java.util.Map;

public class AutoEffects {

    static Map<String, Integer> numberMap = new HashMap<>();

    static String processEffect(String oracleBit, String cardName) {
        String effect;
        effect = oracleBit;

        effect = effect.toLowerCase();
        cardName = cardName.toLowerCase();

        numberMap.put("one", 1);
        numberMap.put("two", 2);
        numberMap.put("three", 3);
        numberMap.put("four", 4);
        numberMap.put("five", 5);
        numberMap.put("six", 6);
        numberMap.put("seven", 7);
        numberMap.put("eight", 8);
        numberMap.put("nine", 9);

        if (((effect.contains("offspring") || effect.contains("flashback")) || effect.contains("plot")) || effect.contains("impending")) {
            return "";
        }
        if (effect.contains("create")) {
            return AutoLine.Create(effect);
        }
        if (effect.contains("add ")) {
            return AutoLine.ManaAbility(oracleBit, "");
        }

        effect = effect.replaceAll("target creature with ([a-zA-Z]+)", "target(creature[$1]");
        effect = effect.replaceAll("loses all abilities", "loseabilities");

        effect = effect.replaceAll("search your library for (an?|a?|any?|up to) (.*?),", "target($2|myLibrary)");
        effect = effect.replaceAll("exile target (.*?) an opponent controls until " + cardName + " leaves the battlefield", "(blink)forsrc target(other *[$1]|battlefield)");
        effect = effect.replaceAll("exile target (.*?) an opponent controls until this enchantment leaves the battlefield", "(blink)forsrc target(other *[$1]|battlefield)");
        effect = effect.replaceAll("adapt ([0-9]+|x)", "_ADAPT$1_");
        effect = effect.replaceAll("non-([a-zA-Z]+)", "[-$1]");
        effect = effect.replaceAll("you may cast ([a-zA-Z]+) spells as though they had", "lord(*[$1]|myHand) asflash");
        effect = effect.replaceAll("another target ([a-zA-Z]+) ", "target(other $1");
        effect = effect.replaceAll("another target ([a-zA-Z]+).", "target(other $1)");
        effect = effect.replaceAll("target ([a-zA-Z]+) ", "target($1");
        effect = effect.replaceAll("target ([a-zA-Z]+).", "target($1)");
        effect = effect.replaceAll("\\b(plains|island|mountain|forest|swamp)cycling\\s*\\{(\\d+)\\}",
                "autohand={$2}{cycle}:name(Search $1) target($1|mylibrary) moveto(hand)");
        effect = effect.replace("if " + cardName + " is tapped, ", "sourceTapped:");
        effect = effect.replace("manifest dread", "_MANIFEST_DREAD_");
        effect = effect.replace("manifests dread", "_MANIFEST_DREAD_");
        effect = effect.replace("a deck can have any number of cards named", "");
        effect = effect.replace("this creature can attack this turn as though it didn't have defender", "canattack ueot");
        effect = effect.replace("named", "");
        effect = effect.replace("reveal it,", "");
        effect = effect.replace("onto the battlefield,", "moveTo(battlefield)");
        effect = effect.replace("onto the battlefield tapped,", "moveTo(battlefield) && tap(noevent)");
        effect = effect.replace("you may have this creature enter as a copy of any creature on the battlefield", "may copy notATarget(creature)");
        effect = effect.replace("discard a card. if you do, draw a card", "_DISCARD&DRAW_");
        effect = effect.replace("that was dealt damage", "[damaged]");
        effect = effect.replace("this creature's power is equal to the number of creatures you control", "anyzone=type:creature:mybattlefield/ cdaactive");
        effect = effect.replace("this creature's power and toughness is equal to the number of creatures you control", "anyzone=type:creature:mybattlefield/type:creature:mybattlefield cdaactive");

        effect = effect.replaceAll("you may cast (.*?) spells without paying their mana costs.", "lord($1|mycastingzone) zerocast");
        effect = effect.replaceAll("endures ([0-9]+|x)", "_ENDURE_($1)");
        effect = effect.replaceAll("mobilize ([0-9]+|x)", "_MOBILIZE_($1)");
        effect = effect.replaceAll("this ([a-zA-Z]+)", "");
        effect = effect.replaceAll("enters tapped unless you control (an?|a) ([a-zA-Z]+) or (an?|a) ([a-zA-Z]+)", "aslongas($2,$4|myBattlefield) tap(noevent) <1 oneshot");

        effect = effect.replaceAll("as an additional cost to cast this spell,", "");
        effect = effect.replaceAll("activated abilities of (.*?) can't be activated", "lord($1) noactivatedability");

        effect = effect.replace("that's one or more colors", "[white;blue;black;red;green]");
        effect = effect.replace("your opponents can't cast spells during your turn", "this(variable{controllerturn}) maxCast(*)0 opponent");
        effect = effect.replace("exile the top card of your library. you may play it until the end of your next turn.", "_IMPULSEUENT_");
        effect = effect.replace("if it was a creature, return it to the battlefield under its owner's control. it's an enchantment.", "all(trigger[to]) moveto(battlefield) and!(transforms((removetypes,newability[becomes(Enchantment)])) forever)! ");
        effect = effect.replace("if you would gain life, you gain that much life plus 1 instead.", "@lifeof(player) from(*[-lifefaker]|*):life:1 controller");
        effect = effect.replace("as long as", "aslongas(");
        effect = effect.replace("is in your opening hand, may begin the game withon the battlefield", "leyline");
        effect = effect.replace("if it was a creature, return it to the battlefield under its owner's control. It's an enchantment.", "");
        effect = effect.replace("during your turn,", "during my turn");
        effect = effect.replace("search your library for a basic land card, put it onto the battlefield tapped, then shuffle", "target(land[basic]|mylibrary) moveto(mybattlefield) && tap(noevent)");
        effect = effect.replace("return a creature card from your graveyard to the battlefield", "target(creature|mygraveyard) moveTo(battlefield)");
        effect = effect.replace("with a finality counter on it", "_FINALITY_COUNTER_");
        effect = effect.replace("you may play an additional land on each of your turns", "maxPlay(land)+1");
        effect = effect.replace("this creature", "");
        effect = effect.replace("if an opponent lost life this turn", "if compare(oplifelost)~morethan~0 then ");
        effect = effect.replace("you don't lose this mana as steps and phases end", "doesntempty");
        effect = effect.replace("draw a card, then discard a card", "_LOOT_");
        effect = effect.replaceAll("a copy of target ([a-zA-Z]+) you control", "target(*[$1]|mybattlefield) clone");
        effect = effect.replaceAll("if you control a ([a-zA-Z]+)", "aslongas(*[$1]|myBattlefield)");
        effect = effect.replaceAll("if that creature had power ([0-9]+|x) or less,", "if cantargetcard(*[creature<=$1]) then ");

        effect = effect.replaceAll("you may cast ([a-zA-Z]+) spells as though they had flash", "lord($1|myhand) asflash");
        effect = effect.replace("choose one -", "");

        effect = effect.replace("if " + cardName + " was cast from a graveyard", "if gravecast then ");
        effect = effect.replace("exile the top card of your library. until end of turn, you may play that card", "_IMPULSEDRAW_");
        effect = effect.replace("exile the top card of your library. until the end of your next turn, you may play that card", "_IMPULSEUENT_");
        effect = effect.replace("if the gift was promised,", "");
        effect = effect.replace(" if you cast it,", "if casted(this) then ");
        effect = effect.replace(" from among them and", "]|reveal)");
        effect = effect.replace("put it into your hand,", "moveTo(hand)");
        effect = effect.replaceAll("search your library for a ([a-zA-Z]+) card, reveal it", "target($1|myLibrary)");
        effect = effect.replaceAll("scry ([0-9]+|x)", "_SCRY$1_");
        effect = effect.replaceAll("surveil ([0-9]+|x)", "_SURVEIL$1_");
        effect = effect.replaceAll(". put the rest on the bottom of your library in a random order.", " optiononeend optiontwo name(bottom of library) all(*|reveal) bottomoflibrary optiontwoend revealend");
        effect = effect.replaceAll("other ([a-zA-Z]+) you control", "lord(other $1|myBattlefield");
        effect = effect.replace("from your graveyard", "|mygraveyard)");
        effect = effect.replace("if you gained life this turn", "if compare(lifegain)~morethan~0 then");
        effect = effect.replaceAll("gift a ([a-zA-Z]+)", "");
        effect = effect.replace("exile the top card of your library. you may play it this turn.", "_IMPULSEDRAW_");
        effect = effect.replace("exile the top card of your library. until the end of your next turn, you may play that card.", "_IMPULSEUENT_");
        effect = effect.replace("level 2", "name(Level 2) counter(0/0,1,Level) asSorcerythis(variable{hascntlevel}=1) ");
        effect = effect.replace("level 3", "name(Level 3) counter(0/0,1,Level) asSorcerythis(variable{hascntlevel}=2) ");
        effect = effect.replace("threshold ", "_THRESHOLD_");
        effect = effect.replace("as long as seven or more cards are in your graveyard", "");
        effect = effect.replace("connives", "_CONNIVES_");
        effect = effect.replace("top of your library", "moveTo(mylibrary)");
        effect = effect.replace("transform ", "flip(backside)");
        effect = effect.replace("from your graveyard the battlefield", "moveTo(battlefield) from(myGraveyard)");
        effect = effect.replaceAll("ward \\{(\\d+)\\}", "_WARD$1_");
        effect = effect.replace("choose one or both -", "");
        effect = effect.replaceAll("amass (\\d+)", "_AMASS$1_");
        effect = effect.replace("until end of turn", "ueot");
        effect = effect.replace("until your next turn", "uynt");
        effect = effect.replace("activate only once each turn.", "limit:1");
        effect = effect.replace("activate only as a sorcery", "asSorcery");
        effect = effect.replace("and only as a sorcery", "asSorcery");
        effect = effect.replace("as long as an opponent has three or more poison counters,", "this(variable{opponentpoisoncount}>2)");
        effect = effect.replace("has three or more poison counters", "this(variable{opponentpoisoncount}>2)");
        effect = effect.replace("enlist ", "_ENLIST_");
        effect = effect.replace("kicker ", "kicker=");
        effect = effect.replace("if it was kicked,", "if paid(kicker) then ");
        effect = effect.replace("proliferate", "_PROLIFERATE_");
        effect = effect.replace("exile this saga, then return it to the battlefield transformed under your control", "moveto(exile) and!( moveto(mybattlefield) and!( transforms((,newability[flip(backside)])) oneshot )! )!");

        effect = effect.replace("you win the game", "wingame");
        effect = effect.replace("this ability triggers only once each turn.", "turnlimited");
        effect = effect.replace("only once each turn", "turnlimited");
        effect = effect.replace("activate only during your turn", "myturnonly");
        effect = effect.replace("that was dealt damage this turn", "[damaged]");
        //effect = effect.replace("+1/+1 counter", "counter(1/1)");
        effect = effect.replace("loyalty counter", "counter(0/0,1,Loyalty)");
        effect = effect.replace("on the bottom of its owner's library", "bottomoflibrary");
        effect = effect.replaceAll("with power ([0-9]+|x) or greater", "creature[power>=$1]");
        effect = effect.replaceAll("with power ([0-9]+|x) or less", "creature[power<=$1]");
        effect = effect.replaceAll("with toughness ([0-9]+|x) or greater", "creature[toughness>=$1]");
        effect = effect.replaceAll("with toughness ([0-9]+|x) or less", "creature[toughness<=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+|x) or greater", "[manacost>=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+|x) or more", "[manacost>=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+|x) or less", "[manacost<=$1]");
        effect = effect.replaceAll("with mana value ([0-9]+|x)", "[manacost=$1]");

        effect = effect.replace("return target nonland permanent you don't control to its owner's hand", "target(-land|opponentBattlefield) moveTo(hand)");
        effect = effect.replace("to its owner's hand", "moveTo(hand)");
        effect = effect.replace("to their owners' hands", "moveTo(hand)");

        effect = effect.replace("return it to the battlefield tapped under its owner's control at the beginning of the next end step", "(blink)ueot");
        effect = effect.replace("creatures", "creature");
        effect = effect.replace("nonartifact", "-artifact");
        effect = effect.replace("noncreature", "-creature");
        effect = effect.replace("nonland", "-land");

        effect = effect.replace("activate this ability only once each turn", "limit:1");
        effect = effect.replace("sacrifice " + cardName, "{S}:");
        effect = effect.replace("sacrifice another creature", "{S(other creature|myBattlefield)}");
        effect = effect.replace("defending player controls", ")|opponentBattlefield)");
        effect = effect.replace("your opponents control", ")|opponentBattlefield)");
        effect = effect.replace("to any target", "target(anytarget)");
        effect = effect.replace("if you do, ", "");

        effect = effect.replace("return target creature an opponent controls to its owner's hand", "moveto(hand) target(creature|opponentBattlefield)");
        effect = effect.replace("return target creature to its owner's hand", "moveto(hand) target(creature)");
        effect = effect.replace("exile target nonland permanent an opponent controls until " + cardName + " leaves the battlefield.", "(blink)forsrc target(*[-land]|opponentbattlefield)");
        effect = effect.replace("you may have it fight target creature you don't control", "may target(creature|opponentbattlefield) dynamicability<!powerstrike eachother!>");
        effect = effect.replace("activate this ability only any time you could cast a sorcery", "asSorcery");
        effect = effect.replace(cardName + " from your graveyard to your hand", "moveTo(hand) all(this)");
        effect = effect.replace("you may cast an instant or sorcery card from your hand without paying its mana cost", "may castcard(normal) notatarget(instant,sorcery|myhand)");
        effect = effect.replace(cardName + " enters the battlefield with a +1/+1 counter on it for each color of mana spent to cast it.", "counter(1/1,sunburst)");
        effect = effect.replace("as long as you've cast an instant or sorcery spell this turn, " + cardName, "aslongas(instant,sorcery|mystack) >1");
        effect = effect.replace("legendary permanent that's an artifact, creature, or enchantment.", "artifact[legendary], creature[legendary], enchantment[legendary])");
        effect = effect.replace("each opponent discards a card", "ability$!name(discard) reject notatarget(*|myhand)!$ opponent");
        effect = effect.replace("each opponent loses 1 life", "life:-1 opponent");
        effect = effect.replace("to your hand", "moveTo(hand)");
        effect = effect.replace("whenever this creature attacks, ", "_ATTACKING_");
        effect = effect.replaceAll("with an (.*?) counter on it", "counter(0/0,1,$1)");
        effect = effect.replace("put a +1/+1 counter on it", "counter(1/1)");
        effect = effect.replace("a +1/+1 counter on it", "counter(1/1)");
        effect = effect.replace("put a +1/+1 counter on", "counter(1/1)");
        effect = effect.replaceAll("put (an?|a) (.*?) counter on", "counter(0/0,1,$2)");

        effect = effect.replaceAll("(\\d+) (.*?) counters on it", "(0/0,$1,$2)");
        effect = effect.replace("enters the battlefield under your control, ", "@movedTo(|myBattlefield):");
        effect = effect.replace("attacking creature without flying", "creature[attacking;-flying]");
        effect = effect.replace("can't be blocked this turn", ") unblockable");
        effect = effect.replace("can't be blocked", "unblockable");
        effect = effect.replace("can't block", "cantblock");
        effect = effect.replace("another target creature", "target(other creature");
        effect = effect.replace("another ", "other ");
        effect = effect.replace("target creature you control.", "target(creature|myBattlefield)");
        effect = effect.replace("target card", "target(*");
        effect = effect.replace("target ", "target(");
        effect = effect.replace("prevent the next", "prevent:");
        effect = effect.replace("to the battlefield.", "moveTo(battlefield)");
        effect = effect.replace("creature doesn't untap during its controller's next untap step", "freeze");

        effect = effect.replace("-land permanent", "*[-land;-instant;-sorcery]");
        effect = effect.replace("permanents", "*[-instant;-sorcery]");
        effect = effect.replace("permanent", "*[-instant;-sorcery]");
        effect = effect.replace("on the bottom of your library", "bottomoflibrary");

        effect = effect.replace("you don't control", "|opponentBattlefield)");
        effect = effect.replace("creature you control.", "creature|myBattlefield)");
        effect = effect.replace("creature you control ", "creature|myBattlefield");
        effect = effect.replace("each white creature", "(creature[white]");
        effect = effect.replace(" attacking creature with lesser power", "creature[attacking;power<=])");
        effect = effect.replace("nonland permanent an opponent controls", "*[-land]|opponentbattlefield)");
        effect = effect.replace("if you attacked with a creature this turn, ", "if raid then ");
        effect = effect.replace("an opponent controls", "|opponentBattlefield)");
        effect = effect.replace("you control", "|myBattlefield)");
        effect = effect.replace("loses all abilities", "loseabilities");
        effect = effect.replace("draw a card", "draw:1");
        effect = effect.replace("discard a card", "ability$!name(discard) reject notatarget(*|myhand)!$");
        effect = effect.replace("discards a card", "ability$!name(discard) reject notatarget(*|myhand)!$");
        effect = effect.replace("discards two cards", "ability$!name(discard) reject notatarget(<2>*|myhand)!$");

        effect = effect.replace("sacrifices a creature", "ability$!sacrifice notatarget(creature|mybattlefield)!$ targetedplayer");
        effect = effect.replace("exile " + cardName, "\nexiledeath");
        effect = effect.replace("card from your graveyard", "|mygraveyard)");
        effect = effect.replace("take an extra turn after this one", "turns:+1");
        // Deplete (Mill)
        effect = effect.replace("mills a card", "deplete:1");
        effect = effect.replace("up to", "may up to");

        for (Map.Entry<String, Integer> entry : numberMap.entrySet()) {
            effect = effect.replace("look at the top " + entry.getKey() + " cards of your library", "name(look) reveal:" + entry.getValue() + " optionone choice target(*[]|reveal");
            effect = effect.replaceAll("with " + entry.getKey() + " (.*?) counters on it", "counter(0/0," + entry.getValue() + ",$1)");
            effect = effect.replace("put " + entry.getKey() + " +1/+1 counters ", "counter(1/1," + entry.getValue() + ")");
            effect = effect.replace("mills " + entry.getKey() + " cards", "deplete:" + entry.getValue());
            effect = effect.replace("mill " + entry.getKey() + " cards", "deplete:" + entry.getValue());
            effect = effect.replace("draw " + entry.getKey() + " cards", "draw:" + entry.getValue());
            effect = effect.replace("draws " + entry.getKey() + " cards", "draw:" + entry.getValue());
            effect = effect.replace("up to " + entry.getKey(), "<upto:" + entry.getValue() + ">");
            effect = effect.replaceAll("put " + entry.getKey() + " (.*?) counters on", "counter(0/0," + entry.getValue() + ",$1)");
        }
        effect = effect.replace("mill a card", "deplete:1");

        effect = effect.replaceAll("you gain ([0-9]+|x) life", "life:$1");
        effect = effect.replaceAll("you lose ([0-9]+|x) life", "life:-$1");
        effect = effect.replaceAll("(loses?|lose) ([0-9]+|x) life", "life:-$2");

        // Draw Monsta Cardo!
        effect = effect.replace("draw a card", "draw:1");
        effect = effect.replace("draw that many cards", "draw:thatmuch");

        effect = effect.replace("exile ", "moveTo(exile)");
        effect = effect.replace("for each creature ", "type:creature:");
        effect = effect.replace("each ", "all(");
        effect = effect.replace(cardName + " enters the battlefield tapped.", "auto=tap(noevent)");
        effect = effect.replace(cardName + " gains ", "");
        effect = effect.replace(cardName + " gets ", "");
        effect = effect.replace(cardName + " has ", "");
        effect = effect.replace(" then shuffle", "");

        effect = effect.replaceAll("deals ([0-9]+|x) damage", "damage:$1");

        effect = effect.replace("if you do", ":");
        effect = effect.replace("when you do", ":");
        effect = effect.replace("you may pay ", "pay(");
        effect = effect.replace("you may", "may");

        effect = effect.replaceAll(", and that creature gains| and gains | and gain | and gets | and has | and you | and ", " && ");

        effect = effect.replace("without ", "-");
        effect = effect.replace(" with a ", "");
        effect = effect.replace("put a ", " ");
        effect = effect.replace("put ", " ");
        effect = effect.replace("return ", "");
        effect = effect.replaceAll(" gains | gain | gets | get ", "");
        effect = effect.replaceAll("gains |gain |gets |get ", "");
        effect = effect.replace(", then ", " && ");
        effect = effect.replace(" cards", "");
        effect = effect.replace(" card", "");
        effect = effect.replace("card ", "");

        effect = effect.replace("it) ", "");
        effect = effect.replace(" it ", "");
        effect = effect.replace("it ", "");
        effect = effect.replace(")on ", ") ");
        effect = effect.replace(" or ", ",");
        effect = effect.replace(" to ", " ");

        effect = effect.replace("} {", "}{");
        effect = effect.replace(":|", ":");
        effect = effect.replace("+", "");
        //effect = effect.replace("-", "");
        effect = effect.replace(".", "");
        effect = effect.replace(cardName, "");

        effect = effect.trim();

        return effect;
    }
}
