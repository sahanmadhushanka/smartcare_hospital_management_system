package com.hospitalmanagement.smartcare.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DoctorManagementFrame extends JFrame {

    // Form Input Fields
    private JTextField txtDoctorId;
    private JTextField txtDoctorName;
    private JComboBox<String> cmbSpecialization;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtRoomNo;
    private JComboBox<String> cmbAvailableDays;
    private JTextField txtTimeSlot;

    // Buttons
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JButton btnSearch;

    // Table & Model
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public DoctorManagementFrame() {
        setTitle("SmartCare HMS - Doctor Management Module");
        setSize(1100, 680);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 10, 26)); // Dark Theme Header
        headerPanel.setPreferredSize(new Dimension(1100, 42));

        JLabel lblTitle = new JLabel("  Doctor Management System", JLabel.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.WEST);

        // 2. Main Content Split Panel (Left: Form Inputs | Right: Table & Search)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(380); // Form Panel Width
        splitPane.setDividerSize(5);

        // --- LEFT PANEL: Form Inputs ---
        JPanel leftFormPanel = createFormPanel();

        // --- RIGHT PANEL: Search & Table ---
        JPanel rightTablePanel = createTablePanel();

        splitPane.setLeftComponent(leftFormPanel);
        splitPane.setRightComponent(rightTablePanel);

        add(headerPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // Populate Initial Sample Data
        loadSampleData();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 1), " Doctor Details Form "),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Fields Labels & Inputs
        txtDoctorId = new JTextField();
        txtDoctorId.setEditable(false); // Auto-generated ID
        txtDoctorId.setText("DOC-101");
        txtDoctorId.setBackground(new Color(236, 240, 241));

        txtDoctorName = new JTextField();

        String[] specializations = {
                "Cardiologist", "Pediatrician", "General Physician",
                "Neurologist", "Dermatologist", "Orthopedic Surgeon", "ENT Specialist"
        };
        cmbSpecialization = new JComboBox<>(specializations);

        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtRoomNo = new JTextField();

        String[] daysOptions = {
                "Mon / Wed", "Tue / Thu", "Fri / Sat", "Daily", "Weekend Only"
        };
        cmbAvailableDays = new JComboBox<>(daysOptions);

        txtTimeSlot = new JTextField("09:00 AM - 01:00 PM");

        int row = 0;
        addFormRow(panel, gbc, "Doctor ID:", txtDoctorId, row++);
        addFormRow(panel, gbc, "Doctor Name:", txtDoctorName, row++);
        addFormRow(panel, gbc, "Specialization:", cmbSpecialization, row++);
        addFormRow(panel, gbc, "Contact No:", txtPhone, row++);
        addFormRow(panel, gbc, "Email Address:", txtEmail, row++);
        addFormRow(panel, gbc, "Room No:", txtRoomNo, row++);
        addFormRow(panel, gbc, "Available Days:", cmbAvailableDays, row++);
        addFormRow(panel, gbc, "Time Slot:", txtTimeSlot, row++);

        // Action Buttons Panel
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        btnPanel.setOpaque(false);

        btnAdd = new JButton("Add Doctor");
        btnAdd.setBackground(new Color(39, 174, 96)); // Green
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 12));

        btnUpdate = new JButton("Update");
        btnUpdate.setBackground(new Color(41, 128, 185)); // Blue
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFont(new Font("Arial", Font.BOLD, 12));

        btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(192, 57, 43)); // Red
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("Arial", Font.BOLD, 12));

        btnClear = new JButton("Clear / Reset");
        btnClear.setBackground(new Color(127, 140, 141)); // Gray
        btnClear.setForeground(Color.WHITE);
        btnClear.setFont(new Font("Arial", Font.BOLD, 12));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        panel.add(btnPanel, gbc);

        // Button Event Listeners
        btnAdd.addActionListener(e -> addDoctorAction());
        btnUpdate.addActionListener(e -> updateDoctorAction());
        btnDelete.addActionListener(e -> deleteDoctorAction());
        btnClear.addActionListener(e -> clearFormFields());

        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        component.setPreferredSize(new Dimension(180, 26));
        panel.add(component, gbc);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Search Bar
        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        searchBarPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Search Doctor:");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 12));

        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(220, 28));

        btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(80, 28));
        btnSearch.setBackground(new Color(52, 73, 94));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.addActionListener(e -> searchDoctorAction());

        JButton btnRefresh = new JButton("Refresh List");
        btnRefresh.setPreferredSize(new Dimension(105, 28));
        btnRefresh.setBackground(new Color(41, 128, 185));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadSampleData();
        });

        searchBarPanel.add(lblSearch);
        searchBarPanel.add(txtSearch);
        searchBarPanel.add(btnSearch);
        searchBarPanel.add(btnRefresh);

        // JTable Setup
        String[] columns = {"ID", "Name", "Specialization", "Contact", "Room", "Schedule"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table rows non-editable directly
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setRowHeight(28);
        doctorTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        doctorTable.getTableHeader().setBackground(new Color(220, 221, 225));
        doctorTable.setFont(new Font("Arial", Font.PLAIN, 12));

        // Click Row to Auto-Fill Form
        doctorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow != -1) {
                    txtDoctorId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    txtDoctorName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    cmbSpecialization.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
                    txtPhone.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    txtRoomNo.setText(tableModel.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(doctorTable);

        panel.add(searchBarPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        return panel;
    }

    // --- LOGIC METHODS ---

    private void loadSampleData() {
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"DOC-101", "Dr. Nimal Perera", "Cardiologist", "0771234567", "Room 01", "Mon/Wed (09:00-01:00)"});
        tableModel.addRow(new Object[]{"DOC-102", "Dr. Sunethra Silva", "Pediatrician", "0719876543", "Room 02", "Tue/Thu (04:00-08:00)"});
        tableModel.addRow(new Object[]{"DOC-103", "Dr. Kasun Fernando", "General Physician", "0755554433", "Room 05", "Daily (08:00-12:00)"});
        tableModel.addRow(new Object[]{"DOC-104", "Dr. Chathuri Wickrama", "Dermatologist", "0762223344", "Room 03", "Fri/Sat (02:00-06:00)"});
    }

    private void addDoctorAction() {
        String name = txtDoctorName.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Doctor Name and Contact Number!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = "DOC-" + (100 + tableModel.getRowCount() + 1);
        String spec = cmbSpecialization.getSelectedItem().toString();
        String room = txtRoomNo.getText().trim();
        String schedule = cmbAvailableDays.getSelectedItem().toString() + " (" + txtTimeSlot.getText().trim() + ")";

        tableModel.addRow(new Object[]{id, name, spec, phone, room, schedule});
        JOptionPane.showMessageDialog(this, "New Doctor Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFormFields();
    }

    private void updateDoctorAction() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Doctor from the table to Update!", "Selection Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.setValueAt(txtDoctorName.getText().trim(), selectedRow, 1);
        tableModel.setValueAt(cmbSpecialization.getSelectedItem().toString(), selectedRow, 2);
        tableModel.setValueAt(txtPhone.getText().trim(), selectedRow, 3);
        tableModel.setValueAt(txtRoomNo.getText().trim(), selectedRow, 4);
        tableModel.setValueAt(cmbAvailableDays.getSelectedItem().toString() + " (" + txtTimeSlot.getText().trim() + ")", selectedRow, 5);

        JOptionPane.showMessageDialog(this, "Doctor Details Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFormFields();
    }

    private void deleteDoctorAction() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Doctor from the table to Delete!", "Selection Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this doctor record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Doctor Record Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFormFields();
        }
    }

    private void searchDoctorAction() {
        String query = txtSearch.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            loadSampleData();
            return;
        }

        DefaultTableModel searchModel = new DefaultTableModel(new String[]{"ID", "Name", "Specialization", "Contact", "Room", "Schedule"}, 0);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = tableModel.getValueAt(i, 1).toString().toLowerCase();
            String spec = tableModel.getValueAt(i, 2).toString().toLowerCase();
            if (name.contains(query) || spec.contains(query)) {
                searchModel.addRow(new Object[]{
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        tableModel.getValueAt(i, 3),
                        tableModel.getValueAt(i, 4),
                        tableModel.getValueAt(i, 5)
                });
            }
        }
        doctorTable.setModel(searchModel);
    }

    private void clearFormFields() {
        txtDoctorId.setText("DOC-" + (100 + tableModel.getRowCount() + 1));
        txtDoctorName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtRoomNo.setText("");
        cmbSpecialization.setSelectedIndex(0);
        cmbAvailableDays.setSelectedIndex(0);
        doctorTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DoctorManagementFrame().setVisible(true);
        });
    }
}