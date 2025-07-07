/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mavenproject4;

/**
 *
 * @author ASUS
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Mavenproject4 extends JFrame {


    private JTable visitTable;
    private DefaultTableModel tableModel;
    
    private JTextField nameField;
    private JTextField nimField;
    private JComboBox<String> studyProgramBox;
    private JComboBox<String> purposeBox;
    private JButton addButton;
    private JButton clearButton;
    private JButton editButton;
    private JButton deleteButton;
    
    private boolean actionColumnsAdded = false;

    public Mavenproject4() {
        setTitle("Library Visit Log");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        nameField = new JTextField();
        nimField = new JTextField();
        studyProgramBox = new JComboBox<>(new String[] {"Sistem dan Teknologi Informasi", "Bisnis Digital", "Kewirausahaan"});
        purposeBox = new JComboBox<>(new String[] {"Membaca", "Meminjam/Mengembalikan Buku", "Research", "Belajar"});
        addButton = new JButton("Add");
        clearButton = new JButton("Clear");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");


        inputPanel.setBorder(BorderFactory.createTitledBorder("Visit Entry Form"));
        inputPanel.add(new JLabel("NIM:"));
        inputPanel.add(nimField);
        inputPanel.add(new JLabel("Name Mahasiswa:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Program Studi:"));
        inputPanel.add(studyProgramBox);
        inputPanel.add(new JLabel("Tujuan Kunjungan:"));
        inputPanel.add(purposeBox);
        inputPanel.add(addButton);
        inputPanel.add(clearButton);
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);
        addButton.addActionListener(e -> {
            String nim = nimField.getText();
            String name = nameField.getText();
            String studyProgram = (String) studyProgramBox.getSelectedItem();
            String purpose = (String) purposeBox.getSelectedItem();

            if (!nim.isEmpty() && !name.isEmpty()) {
                tableModel.addRow(new Object[]{System.currentTimeMillis(), nim, name, studyProgram, purpose});
                nimField.setText("");
                nameField.setText("");
            }
        });
        clearButton.addActionListener(e -> {
            nimField.setText("");
            nameField.setText("");
            studyProgramBox.setSelectedIndex(0);
            purposeBox.setSelectedIndex(0);
        });
        editButton.addActionListener(e -> {
            int selectedRow = visitTable.getSelectedRow();
            if (selectedRow != -1) {
                nimField.setText((String) tableModel.getValueAt(selectedRow, 1));
                nameField.setText((String) tableModel.getValueAt(selectedRow, 2));
                studyProgramBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
                purposeBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
                tableModel.removeRow(selectedRow);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = visitTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            }
        });


        add(inputPanel, BorderLayout.NORTH);

        String[] columns = {"Waktu Kunjungan", "NIM", "Nama", "Program Studi", "Tujuan Kunjungan"};
        tableModel = new DefaultTableModel(columns, 0);
        visitTable = new JTable(tableModel);
        visitTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(visitTable);
        add(scrollPane, BorderLayout.CENTER);
        visitTable.setFillsViewportHeight(true);
        visitTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        setVisible(true);
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("control G"), "showActions");

        getRootPane().getActionMap().put("showActions", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!actionColumnsAdded) {
                    addActionColumns();
                    actionColumnsAdded = true;
                }
            }
        });
    }
    
    private void addActionColumns() {
        tableModel.addColumn("Action");

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("Action", i, tableModel.getColumnCount() - 2);
        }

        visitTable.getColumn("Action").setCellRenderer(new ButtonRenderer());

        visitTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mavenproject4::new);
    }
}
