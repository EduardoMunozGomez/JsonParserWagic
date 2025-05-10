package utilities;

import java.io.*;

public class CardValidation {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Error: You must provide the folder name as an argument.");
            return;
        }

        String folderName = args[0];
        File folder = new File(folderName);

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Error: The provided folder does not exist or is not a valid directory.");
            return;
        }

        for (File file : folder.listFiles()) {
            if (!file.isFile()) {
                continue;
            }
            System.out.println("Processing file: " + file.getName());
            validateFile(file);
        }
    }

    private static void validateFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentCard = "";
            int lineNum = 0;
            boolean isValid = true;
            boolean hasTarget = false;
            boolean isCreature = false;
            boolean insideCard = false;
            boolean hasName = false;
            boolean hasType = false;

            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();

                if (line.startsWith("[card]")) {
                    if (insideCard) {
                        System.err.println("ERROR: Missing [/card] before new [card] at line " + lineNum);
                        isValid = false;
                    }
                    insideCard = true;
                    currentCard = "";
                    hasTarget = false;
                    isCreature = false;
                    hasName = false;
                    hasType = false;
                } else if (line.startsWith("[/card]")) {
                    if (!insideCard) {
                        System.err.println("ERROR: Unmatched [/card] at line " + lineNum);
                        isValid = false;
                    }
                    if (!hasName) {
                        System.err.println("ERROR: Missing name= for card ending at line " + lineNum);
                        isValid = false;
                    }
                    if (!hasType) {
                        System.err.println("ERROR: Missing type= for card ending at line " + lineNum);
                        isValid = false;
                    }
                    insideCard = false;
                } else if (line.startsWith("name=")) {
                    currentCard = line.substring(5);
                    hasName = true;
                } else if (line.startsWith("type=")) {
                    hasType = true;
                } else if (startsWithAny(line, "mana=", "other=", "kicker=", "flashback=", "buyback=", "retrace=", "bestow=")) {
                    isValid &= validateMana(line, currentCard, lineNum);
                } else {
                    isValid &= validateLine(line, currentCard, lineNum);
                    if (line.startsWith("target=")) {
                        hasTarget = true;
                    }
                    if (line.startsWith("type=") && line.matches(".*(Creature).*")) {
                        isCreature = true;
                    }
                    if (isCreature) {
                        isValid &= validateStats(line, currentCard, lineNum);
                    }
                }

                if (line.startsWith("subtype=Aura") && !currentCard.isEmpty() && !currentCard.endsWith("*")) {
                    if (!hasTarget) {
                        System.err.println("ERROR: Aura \"" + currentCard + "\" at line " + lineNum + " is missing target=.");
                        isValid = false;
                    }
                }
            }

            if (insideCard) {
                System.err.println("ERROR: Missing closing [/card] tag at end of file " + file.getName());
                isValid = false;
            }

            if (isValid) {
                System.out.println("All cards are valid in " + file.getName());
            }
        } catch (IOException e) {
            System.err.println("Validation error in " + file.getName() + ": " + e.getMessage());
        }
    }

    private static boolean startsWithAny(String line, String... prefixes) {
        for (String prefix : prefixes) {
            if (line.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private static boolean validateLine(String line, String currentCard, int lineNum) {
        boolean isValid = true;

        for (String keyword : Constants.STARTING_KEYWORDS) {
            String duplicate = keyword + "=" + keyword;
            if (line.startsWith(duplicate)  && !duplicate.equals("dredge=dredge")) {
                System.out.println("⚠️ Duplicado \"" + duplicate + "\" en la carta \"" + currentCard + "\" en la línea " + lineNum);
            }
        }
        if (line.startsWith("auto=") && (line.contains("if ") || line.contains("ifnot ")) && !line.contains("then")) {
            System.err.println("ERROR: if without else at line " + lineNum);
            isValid = false;
        }
        if (line.startsWith("auto=") && line.contains("@") && !line.contains(":")) {
            System.err.println("ERROR: Trigger condition missing semicolon at line " + lineNum);
            isValid = false;
        }
        if (line.contains("::")) {
            System.err.println("ERROR: :: at line " + lineNum);
            isValid = false;
        }
        if (line.contains(":)")) {
            System.err.println("ERROR: :) at line " + lineNum);
            isValid = false;
        }
        if (line.contains("((new")) {
            System.err.println("ERROR: ((new at line " + lineNum);
            isValid = false;
        }

        return isValid;
    }

    private static boolean validateStats(String line, String currentCard, int lineNum) {
        boolean isValid = true;

        if (line.startsWith("power=")) {
            String power = line.substring("power=".length()).trim();
            if (!power.matches("\\*|[-+]?\\d+\\+?\\*?")) {
                System.err.printf("ERROR: Invalid power value \"%s\" for \"%s\" at line %d%n", power, currentCard, lineNum);
                isValid = false;
            }
        }

        if (line.startsWith("toughness=")) {
            String toughness = line.substring("toughness=".length()).trim();
            if (!toughness.matches("\\*|[-+]?\\d+[-+]?\\*?|\\*\\+\\d+")) {
                System.err.printf("ERROR: Invalid toughness value \"%s\" for \"%s\" at line %d%n", toughness, currentCard, lineNum);
                isValid = false;
            }
        }

        return isValid;
    }

    private static boolean validateMana(String line, String currentCard, int lineNum) {
        int equalsIndex = line.indexOf('=');
        if (equalsIndex < 0) {
            System.err.println("ERROR: No '=' found in mana line for \"" + currentCard + "\" at line " + lineNum);
            return false;
        }

        String mana = line.substring(equalsIndex + 1).trim();

        // Separar un posible name(...) al final
        String baseMana = mana;
        String nameSuffixPattern = "( name\\(.*\\))$";
        if (mana.matches(".*" + nameSuffixPattern)) {
            baseMana = mana.replaceFirst(nameSuffixPattern, "");
        }

        // Expresión regular con todos los componentes permitidos
        if (!baseMana.matches("^("
                + "(\\{[0-9CWUBRGXEDi]+\\})*"
                + // básicos
                "(\\{L:\\d+\\})*"
                + // pago de vida
                "(\\{p\\([CWUBRG]\\)\\})*"
                + // phyrexiano
                "(\\{(S|E|D|T|H|X|s2g)\\(.*?\\)\\})*"
                + // efectos especiales
                "(\\{(convoke|delve|improvise|emerge)\\})*"
                + // keywords
                "(\\{k[a-zA-Z0-9]+\\})*"
                + // k-prefixed
                "(\\{X:[a-z]+\\})*"
                + // etiquetas X
                ")*"
                + "(multi(\\{[0-9CWUBRGXEDi]+\\})+)*"
                + // multi{...}{...}
                "$")) {
            System.err.println("ERROR: Invalid mana value \"" + mana + "\" for \"" + currentCard + "\" at line " + lineNum);
            return false;
        }

        return true;
    }

}
