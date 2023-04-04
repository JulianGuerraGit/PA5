package util;
import java.util.*;

public class Normalizer {
    private static String normalize(String cell){
        if(cell.isEmpty())
            return "N/A";
        try{
            int ival = Integer.parseInt(cell);
            String rv = "" + Math.abs(ival);
            for( int i = 0; i < 9 - ("" + Math.abs(ival)).length(); i++)
                rv = "0" + rv;
            rv = (ival >= 0?"+": "-") + rv;
            return rv;
        }catch(NumberFormatException exp){
            try{
                //non-integer
                double dval = Double.parseDouble(cell);
                if(dval > 100 || dval < 0.01 )
                    return String.format("%.2e", dval);
                return String.format("%.2f", dval);
            }catch(NumberFormatException exp2){
                //non-numerical
                if(cell.length()>13)
                    return cell.substring(0,9) + "...";
                return cell;

            }
        }
    }
    public static ArrayList<String>normalizeTable(ArrayList<String> rows, String fileFormat){
        //Your code here:
        //Step 1: Break down every row into cells
        //Step 2: call normalize to normalize each cell
        //Step 3: merge normalized cells with the same separator that you used to break them down.
        String separator = fileFormat.equalsIgnoreCase("txt") ? "\t" : ",";
        String[] cells;
        ArrayList<String> normalizedRow = new ArrayList<>();
        ArrayList<String> normalizedRows = new ArrayList<>();
        for(String row: rows) {
            cells = row.split(separator);
            for (String cell: cells){
                normalizedRow.add(normalize(cell));
            }
            normalizedRows.add(String.join(separator,normalizedRow));
            normalizedRow.clear();
        }
        return normalizedRows;
    }
}
