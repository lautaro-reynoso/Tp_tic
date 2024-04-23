import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hamming {

    // Calcula el número de bits de paridad necesarios para un número de bits de datos dado
    public static int calculateParityBits(int dataBits) {
        int m = 1;
        while (Math.pow(2, m) < dataBits + m + 1) {
            m++;
        }
        return m;
    }

    // Calcula la paridad para un conjunto de bits
    public static int calculateParity(List<Integer> data, int parityBitIndex) {
        int parity = 0;
        for (int i = parityBitIndex - 1; i < data.size(); i += (2 * parityBitIndex)) {
            for (int j = i; j < Math.min(i + parityBitIndex, data.size()); j++) {
                parity ^= data.get(j);
            }
        }
        return parity;
    }

    // Añade bits de paridad a los datos
    public static List<Integer> addParityBits(List<Integer> data) {
        int m = calculateParityBits(data.size());
        List<Integer> encodedData = new ArrayList<>();

        int dataIndex = 0;
        for (int i = 0; i < data.size() + m; i++) {
            if (isPowerOfTwo(i + 1)) {
                encodedData.add(0); // Inicializar bits de paridad en 0
            } else {
                encodedData.add(data.get(dataIndex++));
            }
        }

        for (int i = 0; i < m; i++) {
            int parityBitIndex = (int) Math.pow(2, i);
            encodedData.set(parityBitIndex - 1, calculateParity(encodedData, parityBitIndex));
        }

        return encodedData;
    }

    // Detecta y corrige errores
    // Detecta y corrige errores
// Detecta y corrige errores
public static List<Integer> detectAndCorrectErrors(List<Integer> receivedData) {
    int m = calculateParityBits(receivedData.size());
    List<Integer> correctedData = new ArrayList<>(receivedData);

    // Revisar y corregir los bits de paridad
    for (int i = 0; i < m; i++) {
        int parityBitIndex = (int) Math.pow(2, i);
        int calculatedParity = calculateParity(correctedData, parityBitIndex);
        int actualParity = correctedData.get(parityBitIndex - 1);
        if (calculatedParity != actualParity) {
            correctedData.set(parityBitIndex - 1, calculatedParity); // Corregir el bit de paridad
        }
    }

    // Eliminar los bits de paridad antes de devolver los datos corregidos
    List<Integer> dataWithoutParity = new ArrayList<>();
    for (int i = 0; i < correctedData.size(); i++) {
        if (!isPowerOfTwo(i + 1)) {
            dataWithoutParity.add(correctedData.get(i));
        }
    }

    return dataWithoutParity;
}

    // Verifica si un número es una potencia de 2
    public static boolean isPowerOfTwo(int n) {
        return (n & (n - 1)) == 0 && n != 0;
    }

    // Convierte una cadena de caracteres a una lista de bits (0 y 1)
    public static List<Integer> stringToBits(String str) {
        List<Integer> bits = new ArrayList<>();
        for (char c : str.toCharArray()) {
            String binary = Integer.toBinaryString(c);
            while (binary.length() < 8) {
                binary = "0" + binary; // Rellena con ceros a la izquierda para asegurar 8 bits
            }
            for (char bit : binary.toCharArray()) {
                bits.add(Character.getNumericValue(bit));
            }
        }
        return bits;
    }

    // Convierte una lista de bits (0 y 1) a una cadena de caracteres
    public static String bitsToString(List<Integer> bits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bits.size(); i += 8) {
            int end = Math.min(i + 8, bits.size());
            List<Integer> chunk = bits.subList(i, end);
            int asciiValue = 0;
            for (int j = 0; j < chunk.size(); j++) {
                asciiValue += chunk.get(j) << (7 - j);
            }
            sb.append((char) asciiValue);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            // Leer datos desde un archivo de texto
            BufferedReader reader = new BufferedReader(new FileReader("datos.txt"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            String text = sb.toString();

            System.out.println("Texto original:");
            System.out.println(text);

            // Convertir texto a una secuencia de bits
            List<Integer> originalData = stringToBits(text);

            System.out.println("Datos binarios:");
            for (int bit : originalData) {
                System.out.print(bit);
            }
            System.out.println();

            // Aplicar algoritmo de Hamming
            List<Integer> encodedData = addParityBits(originalData);

            System.out.println("Datos binarios con bits de paridad:");
            for (int bit : encodedData) {
                System.out.print(bit);
            }
            System.out.println();
            
            // Convertir datos binarios con bits de paridad a caracteres
            String encodedText = bitsToString(encodedData);
            System.out.println("Texto con bits de paridad:");
            System.out.println(encodedText);

            // Simular un error cambiando un bit
            encodedData.set(7, encodedData.get(7) ^ 1);

            System.out.println("Datos binarios recibidos con error:");
            for (int bit : encodedData) {
                System.out.print(bit);
            }
            System.out.println();
            
            // Convertir datos binarios recibidos con error a caracteres
            String receivedText = bitsToString(encodedData);
            System.out.println("Texto recibido con error:");
            System.out.println(receivedText);

            // Detectar y corregir errores
            List<Integer> correctedData = detectAndCorrectErrors(encodedData);

            System.out.println("Datos binarios corregidos:");
            for (int bit : correctedData) {
                System.out.print(bit);
            }
            System.out.println();
            
            // Convertir datos binarios corregidos a caracteres
            String correctedText = bitsToString(correctedData);
            System.out.println("Texto corregido:");
            System.out.println(correctedText);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}

