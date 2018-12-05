public class FlipTable {

    private static final String ANSI_COLORS = "\u001B\\[[;\\d]*m";
    private static final String EMPTY = "(empty)";

    private final String[] headers;
    private final String[][] data;
    private final int columns;
    private final int[] columnWidths;
    private final int emptyWidth;

    FlipTable(String[] headers, String[][] data) {
        this.headers = headers;
        this.data = data;

        columns = headers.length;
        columnWidths = new int[columns];
        for (int row = -1; row < data.length; row++) {
            String[] rowData = (row == -1) ? headers : data[row]; // Hack to parse headers too.
            if (rowData.length != columns) {
                throw new IllegalArgumentException(
                        String.format("Row %s's %s columns != %s columns", row + 1, rowData.length, columns));
            }
            for (int column = 0; column < columns; column++) {
                if (rowData[column] == null) {
                    rowData[column] = "NULL";
                }
                for (String rowDataLine : rowData[column].split("\\n")) {
                    String rowDataWithoutColor = rowDataLine.replaceAll(ANSI_COLORS, "");
                    columnWidths[column] = Math.max(columnWidths[column], rowDataWithoutColor.length());
                }
            }
        }

        int emptyWidth = 4 * (columns - 1); // Account for column dividers and their spacing.
        for (int columnWidth : columnWidths) {
            emptyWidth += columnWidth;
        }
        this.emptyWidth = emptyWidth;

        if (emptyWidth < EMPTY.length()) { // Make sure we're wide enough for the empty text.
            columnWidths[columns - 1] += EMPTY.length() - emptyWidth;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        printDivider(builder, "╔═╤═╗");
        printData(builder, headers);
        if (data.length == 0) {
            printDivider(builder, "╠═╧═╣");
            //builder.append('║').append(pad(emptyWidth, EMPTY)).append("║\n");
            printDivider(builder, "╚═══╝");
        } else {
            for (int row = 0; row < data.length; row++) {
                printDivider(builder, row == 0 ? "╠═╪═╣" : "╟─┼─╢");
                printData(builder, data[row]);
            }
            printDivider(builder, "╚═╧═╝");
        }
        return builder.toString();
    }

    private void printDivider(StringBuilder out, String format) {
        for (int column = 0; column < columns; column++) {
            out.append(column == 0 ? format.charAt(0) : format.charAt(2));
            out.append(pad(columnWidths[column] + 2, "").replace(' ', format.charAt(1)));
        }
        out.append(format.charAt(4)).append('\n');
    }

    private void printData(StringBuilder out, String[] data) {
        for (int line = 0, lines = 1; line < lines; line++) {
            for (int column = 0; column < columns; column++) {
                out.append(column == 0 ? '║' : '│');
                String[] cellLines = data[column].split("\\n");
                lines = Math.max(lines, cellLines.length);
                String cellLine = line < cellLines.length ? cellLines[line] : "";
                out.append(pad(columnWidths[column], cellLine));
            }
            out.append("║\n");
        }
    }

    private static String pad(int width, String data) {
        return String.format(" %1$-" + width + "s ", data);
    }
}