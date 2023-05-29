package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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

                try ( BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    while ((line = bufferedReader.readLine()) != null) {
                        line = line.toLowerCase();
                        lineNumber++;
                        if (line.isEmpty()) {
                            continue;
                        }
                        if (line.startsWith("abilities=")) {
                            String subline = line.substring(10); // exclude "abilities=" from the string
                            String[] lineKeywords = subline.split("[,]");
                            boolean containsOnlyKeywords = false; // initialize to false
                            for (String keyword : lineKeywords) {
                                if (!Arrays.asList(Constants.KEYWORDS).contains(keyword.trim())) {
                                    containsOnlyKeywords = false;
                                    break;
                                } else {
                                    containsOnlyKeywords = true; // set to true if keyword is found
                                }
                            }
                            if (!containsOnlyKeywords) {
                                System.out.println("Line " + lineNumber + " does not contain valid keywords: " + subline);
                            }
                        }
                        if (line.startsWith("type=")) {
                            boolean containsType = false;
                            for (String type : Constants.VALID_TYPES) {
                                if (line.contains(type)) {
                                    containsType = true;
                                    break;
                                }
                            }
                            if (!containsType) {
                                System.out.println("Line " + lineNumber + " does not contain any of the types: " + line);
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
                                //System.out.println("Line " + lineNumber + " does not contain any of the targets: " + line);
                            }
                        }
                        boolean startsWithKeyword = false;
                        for (String startingkeyword : Constants.STARTING_KEYWORDS) {
                            if (line.startsWith(startingkeyword)) {
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
}
