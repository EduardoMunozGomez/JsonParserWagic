package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Keywords {

    public static void main(String[] args) {

        String folderName = args[0];
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {
            String fileName = file.getAbsolutePath();

            System.out.println("Processing file: " + file.getName());

            String line;
            int lineNumber = 0;

            try {
                FileReader fileReader = new FileReader(fileName);

                try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    while ((line = bufferedReader.readLine()) != null) {
                        line = line.toLowerCase();
                        lineNumber++;
                        if (line.isEmpty() || line.startsWith("#")) {
                            continue;
                        }

                        if (line.startsWith("abilities=")) {
                            String subline = line.substring(10);
                            String[] lineKeywords = subline.split("[,]");
                            boolean containsOnlyKeywords = true;

                            for (String keyword : lineKeywords) {
                                if (!Arrays.asList(Constants.KEYWORDS).contains(keyword.trim())) {
                                    containsOnlyKeywords = false;
                                    break;
                                }
                            }

                            if (!containsOnlyKeywords) {
                                System.out.println("Line " + lineNumber + " does not contain valid keywords: " + subline);
                            }
                        }

                        if (line.startsWith("type=")) {
                            String typesPart = line.substring("type=".length()).trim();
                            String[] types = typesPart.split("\\s+"); // separa por espacios
                            boolean allValid = true;

                            for (String type : types) {
                                if (!Arrays.asList(Constants.VALID_TYPES).contains(type)) {
                                    allValid = false;
                                    break;
                                }
                            }

                            if (!allValid) {
                                System.out.println("Line " + lineNumber + " contains invalid types: " + line);
                            }
                        }

                        if (line.startsWith("target=")) {
                            boolean containsTarget = false;
                            for (String validTarget : Constants.VALID_TARGETS) {
                                if (line.contains(validTarget)) {
                                    containsTarget = true;
                                    break;
                                }
                            }
                            if (!containsTarget) {
                                System.out.println("Line " + lineNumber + " does not contain any of the targets: " + line);
                            }
                        }

                        if (line.contains("|") && !line.startsWith("text=") && !line.contains("meld")) {
                            int pipeIndex = line.indexOf('|');
                            if (pipeIndex != -1) {
                                int closeParenIndex = line.indexOf(')', pipeIndex);
                                int closeBracketIndex = line.indexOf(']', pipeIndex);
                                int closeBraceIndex = line.indexOf('}', pipeIndex);

                                int endIndex = -1;
                                if (closeParenIndex != -1) {
                                    endIndex = closeParenIndex;
                                }
                                if (closeBracketIndex != -1 && (endIndex == -1 || closeBracketIndex < endIndex)) {
                                    endIndex = closeBracketIndex;
                                }
                                if (closeBraceIndex != -1 && (endIndex == -1 || closeBraceIndex < endIndex)) {
                                    endIndex = closeBraceIndex;
                                }

                                if (endIndex != -1) {
                                    String zonePart = line.substring(pipeIndex + 1, endIndex).trim();
                                    String[] zones = zonePart.split("[,^]");
                                    for (String zone : zones) {
                                        zone = zone.trim();
                                        boolean isValid = false;
                                        for (String validZone : Constants.VALID_ZONES) {
                                            if (zone.equals(validZone)) {
                                                isValid = true;
                                                break;
                                            }
                                        }
                                        if (!isValid) {
                                            System.out.println("Line " + lineNumber + " contains invalid zone (after |): '" + zone + "' in line: " + line);
                                        }
                                    }
                                }
                            }
                        }

                        if (line.contains("moveto(")) {
                            Pattern moveToPattern = Pattern.compile("moveto\\(([^)]+)\\)");
                            Matcher matcher = moveToPattern.matcher(line);

                            while (matcher.find()) {
                                String zone = matcher.group(1).trim();

                                boolean isValid = false;
                                for (String validZone : Constants.VALID_ZONES) {
                                    if (zone.equals(validZone)) {
                                        isValid = true;
                                        break;
                                    }
                                }
                                if (!isValid) {
                                    System.out.println("Line " + lineNumber + " contains invalid moveTo zone: '" + zone + "' in line: " + line);
                                }
                            }
                        }

                        if (line.contains("@")) {
                            boolean containsTrigger = false;
                            for (String validTrigger : Constants.VALID_TRIGGERS) {
                                if (line.contains(validTrigger)) {
                                    containsTrigger = true;
                                    break;
                                }
                            }
                            if (!containsTrigger) {
                                System.out.println("Line " + lineNumber + " does not contain any valid Trigger: " + line);
                            }
                        }

                        // ✅ Validación corregida: detectar keywords= fuera del inicio
                        //validateKeywordPosition(line, lineNumber);
                        // Validación original: debe iniciar con una keyword conocida
                        boolean startsWithKeyword = false;
                        for (String startingKeyword : Constants.STARTING_KEYWORDS) {
                            if (line.startsWith(startingKeyword)) {
                                startsWithKeyword = true;
                                break;
                            }
                        }
                        if (!startsWithKeyword) {
                            System.out.println("Line does not starts with keyword " + lineNumber + ": " + line);
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("Error reading file '" + fileName + "'");
            }
        }
    }

    private static void validateKeywordPosition(String line, int lineNumber) {
        if (line.startsWith("#")) {
            return; // ignorar comentarios
        }
        for (String startingKeyword : Constants.STARTING_KEYWORDS) {
            if (startingKeyword.equals("power") || startingKeyword.equals("toughness")) {
                continue; // excepción
            }

            String keywordWithEqual = startingKeyword.endsWith("=") ? startingKeyword : startingKeyword + "=";
            Pattern keywordPattern = Pattern.compile("\\b" + Pattern.quote(keywordWithEqual));
            Matcher matcher = keywordPattern.matcher(line);

            while (matcher.find()) {
                if (matcher.start() != 0) {
                    System.out.println("Line " + lineNumber + " has misplaced keyword '" + keywordWithEqual + "' not at the start: " + line);
                }
            }
        }
    }

}
