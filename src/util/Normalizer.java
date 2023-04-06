package util;

import java.util.*;

public class Normalizer {
    private static String normalize(String cell) {
        if (cell.isEmpty())
            return "N/A";
        try {
            int ival = Integer.parseInt(cell); // parses the cell as int and throws exception if not the correct format

            String rv = "" + Math.abs(ival);   // stores the absolute value with no zeros

            for (int i = 0; i < 9 - ("" + Math.abs(ival)).length(); i++)
                rv = "0" + rv; // adds a zero until length is 10

            rv = (ival >= 0 ? "+" : "-") + rv; // adds a + if the initial value was positive and - if it was negative

            return rv;

        } catch (NumberFormatException exp) {

            try {
                //non-integer
                double dval = Double.parseDouble(cell); // parses the cell as double and throws exception if not the correct format

                if (dval > 100 || dval < 0.01)
                    return String.format("%.2e", dval); // if the double value is greater than 100 or less than 0.01 then it returns in exponential form with two decimal precision

                return String.format("%.2f", dval); // else it returns the value in double format with two decimal precision

            } catch (NumberFormatException exp2) {

                //non-numerical
                if (cell.length() > 13)
                    return cell.substring(0, 9) + "..."; // if the string is longer than 13 characters it uses the first 10 characters and adds "..." to the end.

                return cell;    // else it returns the full string

            }
        }
    }

    public static ArrayList<String> normalizeTable(ArrayList<String> rows, String fileFormat) {
        //Step 1: Break down a row into cells
        //Step 2: Call normalize to normalize each cell
        //Step 3: Merge normalized cells with the same separator that you used to break them down.
        //Step 4: Repeat for all rows.

        String separator = fileFormat.equalsIgnoreCase("txt") ? "\t" : ",";

        String[] cells;

        ArrayList<String> normalizedCells = new ArrayList<>();
        ArrayList<String> normalizedRows = new ArrayList<>();

        for (String row : rows) {
            cells = row.split(separator); // splits row into cells using file format separator

            for (String cell : cells) {
                normalizedCells.add(normalize(cell)); // normalizes the cells and adds them to normalizedCells
            }

            normalizedRows.add(String.join(separator, normalizedCells)); // joins normalizedCells using the file format separator
            normalizedCells.clear(); // clears normalized Cells array list in preparation for next row/
        }
        return normalizedRows; // returns the ArrayList of row strings
    }
}
