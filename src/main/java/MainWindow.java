import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class MainWindow {
    private JTable table1;
    private JButton reloadButton;
    private JComboBox comboBox1;
    private JPanel MainPanel;
    private JButton loadTableButton;
    private JCheckBox onlyViewsBox1;
    private JButton addNewRecordButton;
    private JButton editRecordButton;
    private DbUtils dbUtils;

    private JFrame dialogFrame;

    private ActionListener closeListener;

    public MainWindow() {

        try {
            dbUtils = new DbUtils();
            populateComboBox();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LoadTableListener loadTableListener = new LoadTableListener();
        LoadTablesListener loadTablesListener = new LoadTablesListener();
        AddNewRecordListener addNewRecordListener = new AddNewRecordListener();
        EditRecordListener editRecordListener = new EditRecordListener();
        closeListener = new CloseDialogListener();
        loadTableButton.addActionListener(loadTableListener);
        reloadButton.addActionListener(loadTablesListener);
        addNewRecordButton.addActionListener(addNewRecordListener);
        editRecordButton.addActionListener(editRecordListener);
    }

    private void populateComboBox() {
        try {
            var ls = dbUtils.getDbList(onlyViewsBox1.isSelected());
            comboBox1.setModel(new DefaultComboBoxModel<String>(ls.toArray(new String[0])));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private class LoadTableListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                ResultSet rs = DbUtils.getTable(String.valueOf(comboBox1.getSelectedItem()));
                table1.setModel(buildTableModel(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class CloseDialogListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            dialogFrame.dispose();
        }
    }

    private class LoadTablesListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            populateComboBox();
        }
    }


    private class AddNewRecordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            dialogFrame = new JFrame("AddDialog");
            dialogFrame.setContentPane(new AddRecrodDialog(dbUtils, closeListener).MainPanel);
            dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialogFrame.setLocationRelativeTo(null);
            dialogFrame.pack();
            dialogFrame.setVisible(true);
        }
    }

    private class EditRecordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt) {
            dialogFrame = new JFrame("EditDialog");
            dialogFrame.setContentPane(new EditRecordDialog(dbUtils,closeListener).MainPanel);
            dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            dialogFrame.setLocationRelativeTo(null);
            dialogFrame.pack();
            dialogFrame.setVisible(true);
        }
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 400));
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}


