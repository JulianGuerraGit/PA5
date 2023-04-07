package util;

import java.util.*;

public class Normalizer {

    /**
     * Normalize is a method which normalizes a given cell and formats it according to the type of content in it.
     *
     * @param cell is the cell to be normalized and then returned formatted.
     * @return returns the formatted/normalized cell
     */
    private static String normalize(String cell) {
        if (cell.isEmpty())
            return "N/A";

        try {
            int intVal = Integer.parseInt(cell); // parses the cell as int and throws exception if not the correct format

            StringBuilder rv = new StringBuilder(String.valueOf(Math.abs(intVal)));   // stores the absolute value with no zeros

            for (int i = 0; i < 9 - (String.valueOf(Math.abs(intVal))).length(); i++)
                rv.insert(0, "0"); // adds a zero until length is 9

            rv.insert(0, (intVal >= 0 ? "+" : "-")); // adds + if the initial value was positive and - if it was negative

            return rv.toString();

        } catch (NumberFormatException exp) {

            try {
                //non-integer
                double doubleVal = Double.parseDouble(cell); // parses the cell as double and throws exception if not the correct format

                if (doubleVal > 100 || doubleVal < 0.01)
                    return String.format("%.2e", doubleVal); // if the double value is greater than 100 or less than 0.01 then it returns in exponential form with two decimal precision

                return String.format("%.2f", doubleVal); // else it returns the value in double format with two decimal precision

            } catch (NumberFormatException exp2) {

                //non-numerical
                if (cell.length() > 13)
                    return cell.substring(0, 9) + "..."; // if the string is longer than 13 characters it uses the first 10 characters and adds "..." to the end.

                return cell;    // else it returns the full string

            }
        }
    }

    /**
     * normalizeTable is the method that is called to normalize/format entire files. It is given two parameters, an
     * ArrayList of rows and a file format. First, it breaks down the rows into cells using the given file formats
     * delimiter, then it normalizes each cell in that row, then it merges the now normalized cells with the same
     * delimiter, and then it repeats for every other row.
     *
     * @param rows       is the ArrayList of rows from the target document.
     * @param fileFormat is the file type of the target file.
     * @return returns an ArrayList of normalized rows to be written back into the target file.
     */
    public static ArrayList<String> normalizeTable(ArrayList<String> rows, String fileFormat) {
        //Step 1: Break down a row into cells
        //Step 2: Call normalize to normalize each cell
        //Step 3: Merge normalized cells with the same separator that you used to break them down.
        //Step 4: Repeat for all rows.

        String[] cells;
        ArrayList<String> normalizedRows = new ArrayList<>();
        String splitSeparator = fileFormat.equalsIgnoreCase("txt") ? "\t" : ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"; // accounts for cells with double quotation enclosed commas
        String joinSeparator = fileFormat.equalsIgnoreCase("txt") ? "\t" : ",";

        for (String row : rows) {
            cells = row.split(splitSeparator); // splits row into cells using file format split separator

            for (int i = 0; i < cells.length; i++)
                cells[i] = normalize(cells[i]); // overwrites the cells with the normalized version

            normalizedRows.add(String.join(joinSeparator, cells)); // joins normalizedCells using the file format join separator and adds them to the rows ArrayList
        }
        return normalizedRows; // returns the ArrayList of row strings
    }
}
