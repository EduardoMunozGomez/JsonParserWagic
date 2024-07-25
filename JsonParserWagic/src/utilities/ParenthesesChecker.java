package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class ParenthesesChecker {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Please provide a folder name as an argument.");
            return;
        }

        String folderName = args[0];
        File folder = new File(folderName);
        
        if (!folder.isDirectory()) {
            System.err.println("Provided path is not a directory.");
            return;
        }

        for (File file : folder.listFiles()) {
            if (!file.isFile()) {
                continue; // Skip directories or non-file items
            }

            String fileName = file.getAbsolutePath();
            System.out.println("Processing file: " + file.getName());

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                int lineNumber = 0;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    
                    if (line.startsWith("text=") || line.contains("this(cantargetcard(")) {
                        // Skip lines that should be ignored
                        continue;
                    }

                    Stack<Integer> parenStack = new Stack<>();
                    Stack<Integer> bracketStack = new Stack<>();
                    Stack<Integer> curlyStack = new Stack<>();
                    Stack<Integer> abilityStack = new Stack<>(); // Stack for abilities$
                    Stack<Integer> andStack = new Stack<>(); // Stack for and!(

                    boolean inAbilities = false; // To track if we are inside an abilities$ sequence
                    boolean inAnd = false; // To track if we are inside an and!( sequence

                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);

                        // Handle abilities$ and !$
                        if (i + 9 <= line.length() && line.substring(i, i + 9).equals("abilities$")) {
                            abilityStack.push(i);
                            inAbilities = true;
                            i += 8; // Skip past "abilities$"
                        } else if (inAbilities && i + 2 <= line.length() && line.substring(i, i + 2).equals("!$")) {
                            if (abilityStack.isEmpty()) {
                                System.out.println("Error: no opening abilities$ found for closing !$ at line " + lineNumber + ", position " + (i + 1));
                            } else {
                                abilityStack.pop();
                            }
                            inAbilities = false;
                            i += 1; // Skip past "!$"
                        }

                        // Handle and!( and )!
                        if (i + 4 <= line.length() && line.substring(i, i + 4).equals("and!(")) {
                            andStack.push(i);
                            inAnd = true;
                            i += 3; // Skip past "and!("
                        } else if (inAnd && i + 2 <= line.length() && line.substring(i, i + 2).equals(")!")) {
                            if (andStack.isEmpty()) {
                                System.out.println("Error: no opening and!( found for closing )! at line " + lineNumber + ", position " + (i + 1));
                            } else {
                                andStack.pop();
                            }
                            inAnd = false;
                            i += 1; // Skip past ")!"
                        }

                        // Handle parentheses, brackets, and curly braces
                        switch (c) {
                            case '(':
                                parenStack.push(i);
                                break;
                            case ')':
                                if (parenStack.isEmpty()) {
                                    System.out.println("Error: no opening parenthesis found for closing parenthesis at line " + lineNumber + ", position " + (i + 1));
                                } else {
                                    parenStack.pop();
                                }
                                break;
                            case '[':
                                bracketStack.push(i);
                                break;
                            case ']':
                                if (bracketStack.isEmpty()) {
                                    System.out.println("Error: no opening bracket found for closing bracket at line " + lineNumber + ", position " + (i + 1));
                                } else {
                                    bracketStack.pop();
                                }
                                break;
                            case '{':
                                curlyStack.push(i);
                                break;
                            case '}':
                                if (curlyStack.isEmpty()) {
                                    System.out.println("Error: no opening curly bracket found for closing curly bracket at line " + lineNumber + ", position " + (i + 1));
                                } else {
                                    curlyStack.pop();
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    // Report unmatched opening symbols
                    while (!parenStack.isEmpty()) {
                        int pos = parenStack.pop();
                        System.out.println("Error: opening parenthesis at line " + lineNumber + ", position " + (pos + 1) + " has no matching closing parenthesis");
                    }
                    while (!bracketStack.isEmpty()) {
                        int pos = bracketStack.pop();
                        System.out.println("Error: opening bracket at line " + lineNumber + ", position " + (pos + 1) + " has no matching closing bracket");
                    }
                    while (!curlyStack.isEmpty()) {
                        int pos = curlyStack.pop();
                        System.out.println("Error: opening curly bracket at line " + lineNumber + ", position " + (pos + 1) + " has no matching closing curly bracket");
                    }
                    while (!abilityStack.isEmpty()) {
                        int pos = abilityStack.pop();
                        System.out.println("Error: opening abilities$ at line " + lineNumber + ", position " + (pos + 1) + " has no matching closing !$");
                    }
                    while (!andStack.isEmpty()) {
                        int pos = andStack.pop();
                        System.out.println("Error: opening and!( at line " + lineNumber + ", position " + (pos + 1) + " has no matching closing )!");
                    }
                }
            }
        }
    }
}
