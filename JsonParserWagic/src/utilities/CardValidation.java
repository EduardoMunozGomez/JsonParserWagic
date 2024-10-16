package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CardValidation {

    public static void main(String[] args) {
        String folderName = args[0];
        File folder = new File(folderName);

        for (File file : folder.listFiles()) {
            String fileName = file.getAbsolutePath();

            System.out.println("Processing file: " + file.getName());

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

                String line;
                String currentCard = "";
                int lineNum = 0;
                boolean isValid = true;
                boolean hasTarget = false;
                boolean isCreature = false;

                while ((line = reader.readLine()) != null) {
                    lineNum++;
                    line = line.trim();
                    if (line.startsWith("[card]")) {
                        currentCard = "";
                    } else if (line.startsWith("auto=auto")) {
                        System.out.println("auto=auto \"" + currentCard + "\" at line " + lineNum);
                    } else if (line.startsWith("auto=")) {
                        if ((line.contains("if ") || line.contains("ifnot ")) && !line.contains("then")) {
                            System.out.println("if without an else condition " + lineNum);
                        }
                        if (line.contains("@") && !line.contains(":")) {
                            System.out.println("Trigger condition without semicolon " + lineNum);
                        }
                    } else if (line.startsWith("name=")) {
                        currentCard = line.substring(5);
                        hasTarget = false;
                    } else if (line.startsWith("target=")) {
                        hasTarget = true;
                    } else if (line.startsWith("subtype=Aura") && !currentCard.isEmpty() && !currentCard.endsWith("*")) {
                        if (!hasTarget) {
                            System.out.println("ERROR: Aura card \"" + currentCard + "\" at line " + lineNum + " is missing a target line.");
                            isValid = false;
                        }
                    } else if (line.startsWith("type=") && (line.contains("Creature") || line.contains("Artifact") || line.contains("Planeswalker") || line.contains("Land") || line.contains("Battle")) && !currentCard.isEmpty() && !currentCard.endsWith("*")) {
                        if (hasTarget) {
                            System.out.println("ERROR: card \"" + currentCard + "\" at line " + lineNum + " is a target line.");
                            isValid = false;
                        }
                        isCreature = line.contains("Creature");
                    } else if (isCreature && line.startsWith("power=")) {
                        String power = line.substring(6).trim();
                        if (!power.matches("\\*|[-+]?\\d+\\+?\\*?")) {
                            System.out.println("ERROR: invalid power value \"" + power + "\" for creature card \"" + currentCard + "\" at line " + lineNum + ".");
                            isValid = false;
                        }
                    } else if (isCreature && line.startsWith("toughness=")) {
                        String toughness = line.substring(10).trim();
                        if (!toughness.matches("\\*|[-+]?\\d+[-+]?\\*?|\\*\\+\\d+")) {
                            System.out.println("ERROR: invalid toughness value \"" + toughness + "\" for creature card \"" + currentCard + "\" at line " + lineNum + ".");
                            isValid = false;
                        }
                    } else if (line.contains("::")) {
                        System.out.println("ERROR: :: " + lineNum + " line.");
                    } else if (line.contains(":)")) {
                        System.out.println("ERROR: :) " + lineNum + " line.");
                    } else if (line.contains("((new")) {
                        System.out.println("ERROR: ((new " + lineNum + " line.");
                    }
                }
                reader.close();
                if (isValid) {
                    System.out.println("All cards are valid!");
                }
            } catch (IOException e) {
                System.err.println("Error SyntaxCheck: " + e.getMessage());
            }
        }
    }
}
