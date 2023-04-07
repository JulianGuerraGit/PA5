package driver;

import util.Normalizer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static final String INPUT_PATH = System.getProperty("user.dir") + "/input/";
    public static final String OUTPUT_PATH = System.getProperty("user.dir") + "/output/";
    public static PrintWriter out;
    public static Scanner in;

    /**
     * The convert method is the one that handles converting CSV to TXT and vice versa. It takes in the input and output
     * formats, then for each line in the input file it splits the rows into cells using the delimiter of the input format
     * (for csv files it splits using regex which accounts for commas enclosed in double quotations).
     * and then rejoins the cells with the output format/s delimiter and writes it in the output file.
     *
     * @param inFormat  is the format of the input file.
     * @param outFormat is the format of the output file.
     */
    public static void convert(String inFormat, String outFormat) {
        while (in.hasNextLine()) {
            String line = in.nextLine(); // splits file into lines
            String[] cells = line.split(inFormat.equalsIgnoreCase("txt") ? "\t" : ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); // splits lines into cells using input separator accounting for double quotation enclosed commas
            out.println(String.join(outFormat.equalsIgnoreCase("txt") ? "\t" : ",", cells)); // joins cells with output separator
        }
    }

    /**
     * The readFile method takes in a file input stream and opens the file and stores the rows as strings in an ArrayList,
     * then it returns the Arraylist.
     *
     * @param inStream is the file input stream.
     * @return returns the ArrayList of row strings.
     */
    public static ArrayList<String> readFile(FileInputStream inStream) {
        Scanner inFile = new Scanner(inStream);
        ArrayList<String> rv = new ArrayList<>();
        while (inFile.hasNextLine())
            rv.add(inFile.nextLine());
        inFile.close();
        return rv;
    }

    /**
     * The writeFile stream takes in two parameters, an ArrayList of rows and a file output stream. The print writer is
     * initialized with the file stream and then each item from the arraylist of rows is written to the file. After its
     * done it closes the file.
     *
     * @param rows      is the ArrayList of rows in string format.
     * @param outStream is the File output stream.
     */
    public static void writeFile(ArrayList<String> rows, FileOutputStream outStream) {
        PrintWriter outFile = new PrintWriter(outStream);
        for (String row : rows)
            outFile.println(row);
        outFile.close();
    }

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);

        while (true) {

            System.out.println("Enter Command: ");
            String command = keyboard.nextLine();   // scans for commands

            try {

                if (command.equals("quit"))
                    break;
                else if (command.startsWith("convert")) {

                    String[] tokens = command.split(" "); // splits command into argument tokens

                    if (tokens.length != 3) // verifies there is the correct number of tokens
                        throw new Exception("Illegal convert command. Usage: command fileName1 fileName2");

                    String inputFormat = tokens[1].substring(tokens[1].length() - 3);  // extracts the file extension from the file name
                    String outputFormat = tokens[2].substring(tokens[2].length() - 3); // extracts the file extension from the file name

                    if (!inputFormat.equalsIgnoreCase("csv") && !inputFormat.equalsIgnoreCase("txt")) // verifies file formats
                        throw new Exception("Illegal input format! Supported formats: txt and csv");

                    if (!outputFormat.equalsIgnoreCase("csv") && !outputFormat.equalsIgnoreCase("txt")) // verifies file formats
                        throw new Exception("Illegal output format! Supported formats: txt and csv");

                    if (tokens[1].equalsIgnoreCase(tokens[2])) // makes sure input and output files are not the same
                        throw new Exception("Source and destination files are the same for the convert command");

                    in = new Scanner(new FileInputStream(INPUT_PATH + tokens[1])); // creates input scanner

                    out = new PrintWriter(new FileOutputStream(OUTPUT_PATH + tokens[2])); // creates output printWriter

                    convert(inputFormat, outputFormat); // converts the file

                    // closes the files.
                    in.close();
                    out.close();

                } else if (command.startsWith("normalize")) {

                    String[] tokens = command.split(" "); // splits command into argument tokens

                    if (tokens.length != 2) // verifies there is the correct number of tokens
                        throw new Exception("Illegal normalize command. Usage: normalize fileName");

                    String inputFormat = tokens[1].substring(tokens[1].length() - 3);

                    if (!inputFormat.equalsIgnoreCase("csv") && !inputFormat.equalsIgnoreCase("txt")) // verifies file formats
                        throw new Exception("Illegal input format! Supported formats: txt and csv");

                    ArrayList<String> rows = readFile(new FileInputStream(INPUT_PATH + tokens[1])); // stores the file in an arraylist of rows

                    ArrayList<String> normalizedRows = Normalizer.normalizeTable(rows, inputFormat); // normalizes and stores normalized rows in and arraylist of rows

                    writeFile(normalizedRows, new FileOutputStream(INPUT_PATH + tokens[1])); // overwrites the normalized rows arraylist to the file

                } else
                    throw new Exception("Illegal command is entered!");

            } catch (Exception exp) {
                System.out.println("Something went wrong: " + exp.getMessage() + "\nTry again!");
            }
        }
    }
}
