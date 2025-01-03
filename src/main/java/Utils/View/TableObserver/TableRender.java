package Utils.View.TableObserver;

import javax.swing.*;
import javax.swing.table.TableModel;

public class TableRender implements TableModelChanged {
    private static JTable theTable;
    private static TableModel model;

    public TableRender(JTable Table) {
        theTable=Table;
    }

    public void setModel(TableModel model) {
        TableRender.model = model;
        refreshTable(model);
    }

    @Override
    public void refreshTable(TableModel model) {
        theTable.setModel(model);
    }
}
