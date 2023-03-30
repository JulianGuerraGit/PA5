package driver;
import util.Normalizer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Array;
import java.util.*;
public class Main {
    public static final String INPUT_PATH = System.getProperty("user.dir") + "/input/";
    public static final String OUTPUT_PATH = System.getProperty("user.dir") + "/output/";
    public static PrintWriter out;
    public static Scanner in;
    public static void convert(String inFormat, String outFormat){
        while(in.hasNextLine()){
            String line = in.nextLine();
            String[] cells = line.split(inFormat.equalsIgnoreCase("txt")?"\t": ",");
            /*
            if(inFormat.equalsIgnoreCase("txt")(
                cells = line.split("\t");
            else
                cells = lines.split(",");
             */
            //your code here. Use out.print or out.println, or out.printf
            for ( int i = 0; i < cells.length; i++ ){
                out.printf("%s", cells[i]);
                if( i < cells.length-1 )
                    out.print(outFormat.equalsIgnoreCase("txt")?"\t": ",");
            }
            out.println();
        }
    }
    public static ArrayList<String> readFile(FileInputStream inStream){
        Scanner inFile = new Scanner(inStream);
        ArrayList<String>rv = new ArrayList<>();
        while(inFile.hasNextLine())
            rv.add(inFile.nextLine());
        inFile.close();
        return rv;
    }
    public static void writeFile(ArrayList<String> rows, FileOutputStream outStream){
        PrintWriter outFile = new PrintWriter(outStream);
        for(String row: rows)
            outFile.println(row);
        outFile.close();
    }
    public static void main(String[] args){
        Scanner keyboard = new Scanner(System.in);
        boolean fileOpen = false;
        while(true){
            String command = keyboard.nextLine();
            try{
                if(command.equals("quit"))
                    break;
                else if (command.startsWith("convert")){
                    String[] tokens = command.split(" ");
                    if(tokens.length != 3)
                        throw new Exception("Illegal convert command. Usage: command file1 file2");
                    String inputFormat = tokens[1].substring(tokens[1].length() - 3);
                    String outputFormat = tokens[2].substring(tokens[2].length() - 3);
                    if(!inputFormat.equalsIgnoreCase("csv") && !inputFormat.equalsIgnoreCase("txt"))
                        throw new Exception("Illegal input format! Supported formats: txt and csv");
                    if(!outputFormat.equalsIgnoreCase("csv") && !outputFormat.equalsIgnoreCase("txt"))
                        throw new Exception("Illegal output format! Supported formats: txt and csv");
                    if(tokens[1].equalsIgnoreCase(tokens[2]))
                        throw new Exception("Source and destination files are the same for the convert command");
                    in = new Scanner(new FileInputStream(INPUT_PATH + tokens[1]));
                    out = new PrintWriter(new FileOutputStream(OUTPUT_PATH + tokens[2]));
                    fileOpen = true;
                    convert(inputFormat, outputFormat);

                }else if(command.startsWith("normalize")){
                    String[] tokens = command.split(" ");
                    if(tokens.length != 2)
                        throw new Exception("Illegal normalize command. Usage: normalize file");
                    String inputFormat = tokens[1].substring(tokens[1].length() - 3);
                    if(!inputFormat.equalsIgnoreCase("csv") && !inputFormat.equalsIgnoreCase("txt"))
                        throw new Exception("Illegal input format! Supported formats: txt and csv");
                    ArrayList<String> rows = readFile(new FileInputStream(INPUT_PATH + tokens[1]));
                    ArrayList<String> normalizedRows = Normalizer.normalizeTable(rows, inputFormat);
                    writeFile(normalizedRows, new FileOutputStream(INPUT_PATH + tokens[1]));

                }else
                    throw new Exception("Illegal command is entered!");
            }catch (Exception exp){
                System.out.println("Something went wrong: " + exp.getMessage() + "\nTry again!");
            }finally {
                if(fileOpen) {
                    in.close();
                    out.close();
                }
            }
        }
    }
}
