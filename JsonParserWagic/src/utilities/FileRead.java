package utilities;

import java.io.*;

public class FileRead {
    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\Eduardo_\\Documents\\Wagic\\Res\\sets\\primitives\\new.txt");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("name=")) {
                        System.out.println(line.substring(5));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}