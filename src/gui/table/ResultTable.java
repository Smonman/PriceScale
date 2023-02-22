package gui.table;

import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.List;

public class ResultTable extends AbstractTableModel {

    /**
     * This holds the data of the table.
     *
     * <p>Each list represents a <em>column</em> of the table.</p>
     */
    private final List<List<Object>> data;
    private final List<String> headers;

    public ResultTable() {
        this.data = new LinkedList<>();
        this.headers = new LinkedList<>();
    }

    /**
     * Adds a column to the table.
     *
     * <p>This adds the given column to the data table.</p>
     *
     * @param columnHeader the columnHeader of the column
     * @param columnData   the column to be added
     */
    public void addColumn(final String columnHeader,
                          final List<Object> columnData
    ) {
        headers.add(columnHeader);
        data.add(columnData);
        fireTableChanged(null);
    }

    /**
     * Adds all columns to the table.
     *
     * @param columnHeaders the headers of the given columns
     * @param columns       the columns to be added
     */
    public void addColumns(final List<String> columnHeaders,
                           final List<List<Object>> columns
    ) {
        headers.addAll(columnHeaders);
        data.addAll(columns);
        fireTableChanged(null);
    }

    @Override
    public int getRowCount() {
        return data.stream()
                   .map(List::size)
                   .reduce(0, Integer::max);
    }

    @Override
    public int getColumnCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= getRowCount() ||
            columnIndex >= getColumnCount() ||
            rowIndex < 0 ||
            columnIndex < 0) {
            return null;
        }
        return data.get(columnIndex).get(rowIndex);
    }

    @Override
    public String getColumnName(int column) {
        return headers.get(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
