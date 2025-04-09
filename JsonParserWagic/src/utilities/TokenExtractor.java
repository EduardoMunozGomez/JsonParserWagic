import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TokenExtractor {
    public static void main(String[] args) {
        String inputFile = "C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\entrada.txt";  // Name of the input file
        String outputFile = "C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\salida.txt";  // Name of the output file
        String match = "t;";  // String to search

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(match)) {
                    writer.write(line);
                    writer.newLine(); 
                }
            }
            
            System.out.println("Proceso completado. Revisa el archivo " + outputFile);
        } catch (IOException e) {
            System.err.println("Error al procesar los archivos: " + e.getMessage());
        }
    }
}
