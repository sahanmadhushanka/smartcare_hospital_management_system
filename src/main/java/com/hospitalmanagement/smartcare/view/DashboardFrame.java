package com.hospitalmanagement.smartcare.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardFrame extends JFrame {

    private JPanel contentArea;
    private JPanel headerPanel;
    private JPanel sidebarPanel;
    private JPanel homeMainPanel;

    // Dynamic Labels for Stat Cards
    private JLabel lblDoctorCount;
    private JLabel lblPatientCount;
    private JLabel lblTodayIncome;
    private JLabel lblAppointmentCount;

    // Search Table Components
    private JTable searchResultTable;
    private DefaultTableModel tableModel;
    private String highlightedName = "";

    // Real-time Clock Label
    private JLabel lblClock;

    // Dark Mode Toggle State
    private boolean isDarkMode = false;
    private JButton btnThemeToggle;

    public DashboardFrame() {
        setTitle("SmartCare Hospital Management System");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1000, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Top Header
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 10, 26));
        headerPanel.setPreferredSize(new Dimension(1280, 45));

        JLabel titleLabel = new JLabel("  SmartCare HMS", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        headerRightPanel.setOpaque(false);

        lblClock = new JLabel();
        lblClock.setFont(new Font("Arial", Font.BOLD, 12));
        lblClock.setForeground(new Color(241, 196, 15));
        startLiveClock();

        JButton btnBell = new JButton("Notification 2");
        btnBell.setFont(new Font("Arial", Font.BOLD, 12));
        btnBell.setBackground(new Color(255, 22, 0));
        btnBell.setForeground(Color.WHITE);
        btnBell.setFocusPainted(false);
        btnBell.addActionListener(e -> JOptionPane.showMessageDialog(this,
                " Emergency Cases:\n1. Patient #1042 ICU Admission\n2. Low Amoxicillin Stock in Pharmacy",
                "Notifications Panel", JOptionPane.WARNING_MESSAGE));

//        btnThemeToggle = new JButton("Dark mode");
//        btnThemeToggle.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
//        btnThemeToggle.setPreferredSize(new Dimension(38, 30));
//        btnThemeToggle.setBackground(new Color(255, 255, 255));
//        btnThemeToggle.setForeground(Color.WHITE);
//        btnThemeToggle.setFocusPainted(false);
//        btnThemeToggle.addActionListener(e -> toggleDarkMode());

        JLabel userLabel = new JLabel("Logged in as: Admin ");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(Color.WHITE);

        headerRightPanel.add(lblClock);
        headerRightPanel.add(btnBell);
//        headerRightPanel.add(btnThemeToggle);
        headerRightPanel.add(userLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(headerRightPanel, BorderLayout.EAST);

        // 2. Sidebar Navigation
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new GridLayout(12, 1, 3, 3));
        sidebarPanel.setBackground(new Color(0, 10, 26));
        sidebarPanel.setPreferredSize(new Dimension(220, 600));

        JButton btnDashboard = createSidebarButton("Dashboard");
        JButton btnDoctor = createSidebarButton("Doctor Management");
        JButton btnPatient = createSidebarButton("Patient Management");
        JButton btnAppointment = createSidebarButton("Appointments");
        JButton btnBilling = createSidebarButton("Billing & Payments");
        JButton btnBedWard = createSidebarButton("Bed & Ward Management");
        JButton btnPharmacy = createSidebarButton("Pharmacy Inventory");
        JButton btnLabReports = createSidebarButton("Lab & Test Reports");
        JButton btnSchedule = createSidebarButton("Doctor Schedule");
        JButton btnAuditLog = createSidebarButton("System Audit Log");
        JButton btnSettings = createSidebarButton("User Settings");
        JButton btnLogout = createSidebarButton("Logout");
        btnLogout.setBackground(new Color(168, 0, 0));

        sidebarPanel.add(btnDashboard);
        sidebarPanel.add(btnDoctor);
        sidebarPanel.add(btnPatient);
        sidebarPanel.add(btnAppointment);
        sidebarPanel.add(btnBilling);
        sidebarPanel.add(btnBedWard);
        sidebarPanel.add(btnPharmacy);
        sidebarPanel.add(btnLabReports);
        sidebarPanel.add(btnSchedule);
        sidebarPanel.add(btnAuditLog);
        sidebarPanel.add(btnSettings);
        sidebarPanel.add(btnLogout);

        // 3. Main Dynamic Content Area
        contentArea = new JPanel(new CardLayout());
        contentArea.setBackground(new Color(245, 246, 250));

        JPanel homeView = createCustomHomeDashboardView();
        JPanel bedWardView = createBedWardView();
        JPanel pharmacyView = createPharmacyView();
        JPanel labView = createLabReportsView();
        JPanel scheduleView = createScheduleView();
        JPanel auditView = createAuditLogView();
        JPanel settingsView = createSettingsView();

        contentArea.add(homeView, "Home");
        contentArea.add(bedWardView, "BedWard");
        contentArea.add(pharmacyView, "Pharmacy");
        contentArea.add(labView, "LabReports");
        contentArea.add(scheduleView, "Schedule");
        contentArea.add(auditView, "AuditLog");
        contentArea.add(settingsView, "Settings");

        add(headerPanel, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);

        btnDashboard.addActionListener(e -> switchView(homeView));
        btnBedWard.addActionListener(e -> switchView(bedWardView));
        btnPharmacy.addActionListener(e -> switchView(pharmacyView));
        btnLabReports.addActionListener(e -> switchView(labView));
        btnSchedule.addActionListener(e -> switchView(scheduleView));
        btnAuditLog.addActionListener(e -> switchView(auditView));
        btnSettings.addActionListener(e -> switchView(settingsView));
    }

    private void startLiveClock() {
        Timer timer = new Timer(1000, e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss a");
            lblClock.setText("🕒 " + sdf.format(new Date()));
        });
        timer.start();
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(36, 37, 58));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private JPanel createCustomHomeDashboardView() {
        homeMainPanel = new JPanel();
        homeMainPanel.setLayout(new BoxLayout(homeMainPanel, BoxLayout.Y_AXIS));
        homeMainPanel.setBackground(Color.WHITE);
        homeMainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Welcome Text
        JPanel welcomeContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        welcomeContainer.setOpaque(false);
        welcomeContainer.setPreferredSize(new Dimension(950, 25));
        welcomeContainer.setMaximumSize(new Dimension(2000, 25));

        JLabel lblWelcome = new JLabel(" Welcome Back, Admin! Here is today's hospital system overview.");
        lblWelcome.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 20));
        lblWelcome.setForeground(new Color(0, 1, 67));
        welcomeContainer.add(lblWelcome);

        // Stat Cards 4
        JPanel cardsContainer = new JPanel(new GridLayout(1, 4, 15, 0));
        cardsContainer.setPreferredSize(new Dimension(950, 65));
        cardsContainer.setMaximumSize(new Dimension(2000, 65));
        cardsContainer.setOpaque(false);

        lblDoctorCount = new JLabel("50", JLabel.CENTER);
        lblPatientCount = new JLabel("50", JLabel.CENTER);
        lblTodayIncome = new JLabel("50000", JLabel.CENTER);
        lblAppointmentCount = new JLabel("50000", JLabel.CENTER);

        cardsContainer.add(createMiniCard("Total Doctors", lblDoctorCount, new Color(41, 128, 185)));    // Blue
        cardsContainer.add(createMiniCard("Total Patients", lblPatientCount, new Color(39, 174, 96)));    // Green
        cardsContainer.add(createMiniCard("Today Income", lblTodayIncome, new Color(230, 126, 34)));      // Orange
        cardsContainer.add(createMiniCard("Today Appointments", lblAppointmentCount, new Color(142, 68, 173))); // Purple


        // Live Widgets
        JPanel liveWidgetsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        liveWidgetsPanel.setOpaque(false);
        liveWidgetsPanel.setPreferredSize(new Dimension(950, 55));
        liveWidgetsPanel.setMaximumSize(new Dimension(2000, 55));

        JPanel pnlDoctorBadges = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 4));
        pnlDoctorBadges.setBorder(BorderFactory.createTitledBorder("🟢 Live Doctor Status"));
        pnlDoctorBadges.setBackground(Color.WHITE);

        JLabel lblDocAvailable = new JLabel("🟢 Available: 18");
        lblDocAvailable.setFont(new Font("Arial", Font.BOLD, 12));
        lblDocAvailable.setForeground(new Color(39, 174, 96));

        JLabel lblDocConsulting = new JLabel("🔴 In-Consultation: 24");
        lblDocConsulting.setFont(new Font("Arial", Font.BOLD, 12));
        lblDocConsulting.setForeground(new Color(192, 57, 43));

        JLabel lblDocLeave = new JLabel("⚪ On-Leave: 8");
        lblDocLeave.setFont(new Font("Arial", Font.BOLD, 12));
        lblDocLeave.setForeground(Color.GRAY);

        pnlDoctorBadges.add(lblDocAvailable);
        pnlDoctorBadges.add(lblDocConsulting);
        pnlDoctorBadges.add(lblDocLeave);

        JPanel pnlQueueTracker = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 4));
        pnlQueueTracker.setBorder(BorderFactory.createTitledBorder(" Channeling Queue Live Tracker"));
        pnlQueueTracker.setBackground(Color.WHITE);

        JLabel lblQueue1 = new JLabel("Room 01 (Dr. Nimal): Token #14");
        lblQueue1.setFont(new Font("Arial", Font.BOLD, 12));
        lblQueue1.setForeground(new Color(41, 128, 185));

        JLabel lblQueue2 = new JLabel("Room 02 (Dr. Sunethra): Token #08");
        lblQueue2.setFont(new Font("Arial", Font.BOLD, 12));
        lblQueue2.setForeground(new Color(142, 68, 173));

        pnlQueueTracker.add(lblQueue1);
        pnlQueueTracker.add(lblQueue2);

        liveWidgetsPanel.add(pnlDoctorBadges);
        liveWidgetsPanel.add(pnlQueueTracker);

        // Search Section + Export Buttons
        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        searchContainer.setPreferredSize(new Dimension(950, 35));
        searchContainer.setMaximumSize(new Dimension(2000, 35));
        searchContainer.setOpaque(false);

        JLabel lblSearch = new JLabel("Search Doctor or Patient :");
        lblSearch.setFont(new Font("Arial", Font.BOLD, 13));

        String[] searchOptions = {"Doctor", "Patient"};
        JComboBox<String> cmbSearchOption = new JComboBox<>(searchOptions);
        cmbSearchOption.setPreferredSize(new Dimension(130, 30));

        JTextField txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(200, 30));

        JButton btnSearch = new JButton("Search");
        btnSearch.setPreferredSize(new Dimension(85, 30));
        btnSearch.setBackground(new Color(41, 128, 185));
        btnSearch.setForeground(Color.WHITE);

        JButton btnExportCSV = new JButton("📄 Export CSV");
        btnExportCSV.setPreferredSize(new Dimension(110, 30));
        btnExportCSV.setBackground(new Color(39, 174, 96));
        btnExportCSV.setForeground(Color.WHITE);

        JButton btnExportPDF = new JButton("🖨️ Print / PDF");
        btnExportPDF.setPreferredSize(new Dimension(110, 30));
        btnExportPDF.setBackground(new Color(142, 68, 173));
        btnExportPDF.setForeground(Color.WHITE);

        searchContainer.add(lblSearch);
        searchContainer.add(cmbSearchOption);
        searchContainer.add(txtSearch);
        searchContainer.add(btnSearch);
        searchContainer.add(btnExportCSV);
        searchContainer.add(btnExportPDF);

        // Table Section
        tableModel = new DefaultTableModel();
        searchResultTable = new JTable(tableModel);
        searchResultTable.setRowHeight(32);
        searchResultTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        searchResultTable.getTableHeader().setPreferredSize(new Dimension(0, 30));
        searchResultTable.getTableHeader().setBackground(new Color(220, 221, 225));
        searchResultTable.setFont(new Font("Arial", Font.PLAIN, 12));

        searchResultTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!highlightedName.isEmpty() && row == 0) {
                    c.setBackground(new Color(255, 243, 205));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setForeground(new Color(133, 100, 4));
                } else {
                    c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(searchResultTable);
        tableScrollPane.setPreferredSize(new Dimension(950, 300));
        tableScrollPane.setMaximumSize(new Dimension(2000, 300));

        btnSearch.addActionListener(e -> {
            String selectedOption = (String) cmbSearchOption.getSelectedItem();
            String query = txtSearch.getText().trim();
            performSearch(selectedOption, query);
        });

        btnExportCSV.addActionListener(e -> exportToCSV());
        btnExportPDF.addActionListener(e -> printTableToPDF());

        // Emergency Alerts Panel
        JPanel notificationPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        notificationPanel.setBackground(new Color(253, 237, 236));
        notificationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(245, 183, 177), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        notificationPanel.setPreferredSize(new Dimension(950, 65));
        notificationPanel.setMaximumSize(new Dimension(2000, 65));

        JLabel lblAlert1 = new JLabel(" [EMERGENCY] Patient ID #1042 admitted to ICU Ward 02 (Critical condition)");
        lblAlert1.setFont(new Font("Arial", Font.BOLD, 12));
        lblAlert1.setForeground(new Color(192, 57, 43));

        JLabel lblAlert2 = new JLabel(" [SYSTEM ALERT] Amoxicillin stock is running low in Pharmacy (Only 15 packs left)");
        lblAlert2.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAlert2.setForeground(new Color(211, 84, 0));

        notificationPanel.add(lblAlert1);
        notificationPanel.add(lblAlert2);

        // Speed Dial Panel
        JPanel speedDialPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 6));
        speedDialPanel.setBackground(new Color(245, 247, 250));
        speedDialPanel.setBorder(BorderFactory.createTitledBorder("⚡ Speed Dial - Quick Actions"));
        speedDialPanel.setPreferredSize(new Dimension(950, 65));
        speedDialPanel.setMaximumSize(new Dimension(2000, 65));

        JButton btnQuickReceipt = new JButton(" Quick Print Receipt");
        btnQuickReceipt.setFont(new Font("Arial", Font.BOLD, 13));
        btnQuickReceipt.setPreferredSize(new Dimension(190, 34));
        btnQuickReceipt.setBackground(new Color(52, 73, 94));
        btnQuickReceipt.setForeground(Color.WHITE);

        JButton btnQuickEmergency = new JButton(" New Emergency Case");
        btnQuickEmergency.setFont(new Font("Arial", Font.BOLD, 13));
        btnQuickEmergency.setPreferredSize(new Dimension(200, 34));
        btnQuickEmergency.setBackground(new Color(192, 57, 43));
        btnQuickEmergency.setForeground(Color.WHITE);

        JButton btnQuickDoc = new JButton("️ Add Doctor");
        btnQuickDoc.setFont(new Font("Arial", Font.BOLD, 13));
        btnQuickDoc.setPreferredSize(new Dimension(150, 34));
        btnQuickDoc.setBackground(new Color(39, 174, 96));
        btnQuickDoc.setForeground(Color.WHITE);

        speedDialPanel.add(btnQuickReceipt);
        speedDialPanel.add(btnQuickEmergency);
        speedDialPanel.add(btnQuickDoc);

        // Tabbed Analytics Section
        JTabbedPane analyticsTabPane = new JTabbedPane();
        analyticsTabPane.setFont(new Font("Arial", Font.BOLD, 12));
        analyticsTabPane.setPreferredSize(new Dimension(950, 180));
        analyticsTabPane.setMaximumSize(new Dimension(2000, 180));

        JPanel pnlTab1 = new JPanel(new GridLayout(3, 1, 6, 6));
        pnlTab1.setBackground(Color.WHITE);
        pnlTab1.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JProgressBar pbDoc1 = new JProgressBar(0, 100);
        pbDoc1.setValue(92);
        pbDoc1.setString("1st Place: Dr. Nimal Perera (145 Patients Served)");
        pbDoc1.setStringPainted(true);
        pbDoc1.setForeground(new Color(41, 128, 185));

        JProgressBar pbDoc2 = new JProgressBar(0, 100);
        pbDoc2.setValue(78);
        pbDoc2.setString("2nd Place: Dr. Sunethra Silva (110 Patients Served)");
        pbDoc2.setStringPainted(true);
        pbDoc2.setForeground(new Color(39, 174, 96));

        JProgressBar pbDoc3 = new JProgressBar(0, 100);
        pbDoc3.setValue(65);
        pbDoc3.setString("3rd Place: Dr. Kasun Fernando (85 Patients Served)");
        pbDoc3.setStringPainted(true);
        pbDoc3.setForeground(new Color(155, 89, 182));

        pnlTab1.add(pbDoc1);
        pnlTab1.add(pbDoc2);
        pnlTab1.add(pbDoc3);

        JPanel pnlTab2 = new JPanel(new GridLayout(2, 1, 6, 6));
        pnlTab2.setBackground(Color.WHITE);
        pnlTab2.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JProgressBar pbReg = new JProgressBar(0, 500);
        pbReg.setValue(345);
        pbReg.setString("Monthly Target Progress: 345 / 500 Patients Registered");
        pbReg.setStringPainted(true);
        pbReg.setForeground(new Color(230, 126, 34));

        JProgressBar pbM3 = new JProgressBar(0, 400);
        pbM3.setValue(345);
        pbM3.setString("3-Month Average Growth: August (345) vs July (310)");
        pbM3.setStringPainted(true);
        pbM3.setForeground(new Color(52, 152, 219));

        pnlTab2.add(pbReg);
        pnlTab2.add(pbM3);

        JPanel pnlTab3 = new JPanel(new GridLayout(2, 1, 6, 6));
        pnlTab3.setBackground(Color.WHITE);
        pnlTab3.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JProgressBar pbCancel = new JProgressBar(0, 100);
        pbCancel.setValue(8);
        pbCancel.setString("Cancelled Appointments: 8% (24 / 300 Appointments)");
        pbCancel.setStringPainted(true);
        pbCancel.setForeground(new Color(192, 57, 43));

        JLabel lblSuccessRate = new JLabel("  ✅ Successful Completion Rate: 92% (Low Cancellation Risk overall)");
        lblSuccessRate.setFont(new Font("Arial", Font.BOLD, 12));
        lblSuccessRate.setForeground(new Color(39, 174, 96));

        pnlTab3.add(pbCancel);
        pnlTab3.add(lblSuccessRate);

        analyticsTabPane.addTab(" Top Doctors", pnlTab1);
        analyticsTabPane.addTab(" Patient Trends", pnlTab2);
        analyticsTabPane.addTab(" Cancellations", pnlTab3);

        // Assembly with Increased Vertical Spaces (20px - 25px)
        homeMainPanel.add(welcomeContainer);
        homeMainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing Increased
        homeMainPanel.add(cardsContainer);
        homeMainPanel.add(Box.createRigidArea(new Dimension(0, 22))); // Spacing Increased
        homeMainPanel.add(liveWidgetsPanel);
        homeMainPanel.add(Box.createRigidArea(new Dimension(0, 22))); // Spacing Increased
        homeMainPanel.add(searchContainer);
        homeMainPanel.add(Box.createRigidArea(new Dimension(0, 18))); // Spacing Increased
        homeMainPanel.add(tableScrollPane);
        homeMainPanel.add(Box.createRigidArea(new Dimension(0, 25))); // Spacing Increased
        homeMainPanel.add(notificationPanel);
        homeMainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacing Increased
        homeMainPanel.add(speedDialPanel);
        homeMainPanel.add(Box.createRigidArea(new Dimension(0, 25))); // Spacing Increased
        homeMainPanel.add(analyticsTabPane);

        performSearch("Doctor", "");

        JScrollPane mainScrollPane = new JScrollPane(homeMainPanel);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setBorder(null);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(mainScrollPane, BorderLayout.CENTER);
        return wrapper;
    }

//    private void toggleDarkMode() {
//        isDarkMode = !isDarkMode;
//        Color bg = isDarkMode ? new Color(45, 52, 54) : Color.WHITE;
//
//        homeMainPanel.setBackground(bg);
//        btnThemeToggle.setText(isDarkMode ? "White mode" : "Dark mode");
//        btnThemeToggle.setBackground(isDarkMode ? new Color(241, 196, 15) : new Color(52, 73, 94));
//        btnThemeToggle.setForeground(isDarkMode ? Color.BLACK : Color.WHITE);
//
//        homeMainPanel.revalidate();
//        homeMainPanel.repaint();
//    }

    private JPanel createLabReportsView() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Lab & Diagnostic Test Reports");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        String[] cols = {"Report ID", "Patient Name", "Test Type", "Date", "Status", "Action"};
        Object[][] data = {
                {"LAB-101", "Kamal Gunaratne", "Full Blood Count (FBC)", "2026-08-06", "COMPLETED", "Download PDF"},
                {"LAB-102", "Saman Kumara", "Chest X-Ray", "2026-08-06", "PENDING", "In Progress"},
                {"LAB-103", "Anula Jayasinghe", "Lipid Profile", "2026-08-05", "COMPLETED", "Download PDF"}
        };

        JTable labTable = new JTable(data, cols);
        labTable.setRowHeight(30);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(labTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createScheduleView() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Doctor Consultation Timetable & Duty Roster");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        String[] cols = {"Doctor Name", "Specialization", "Available Days", "Time Slot", "Room No."};
        Object[][] data = {
                {"Dr. Nimal Perera", "Cardiologist", "Monday / Wednesday", "09:00 AM - 01:00 PM", "Room 01"},
                {"Dr. Sunethra Silva", "Pediatrician", "Tuesday / Thursday", "04:00 PM - 08:00 PM", "Room 02"},
                {"Dr. Kasun Fernando", "General Physician", "Daily", "08:00 AM - 12:00 PM", "Room 05"}
        };

        JTable scheduleTable = new JTable(data, cols);
        scheduleTable.setRowHeight(30);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAuditLogView() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("System Audit Log & Security Trail");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        String[] cols = {"Log ID", "User Role", "Username", "Action Performed", "Timestamp"};
        Object[][] data = {
                {"LOG-8901", "ADMIN", "admin", "User logged into System Dashboard", "2026-08-06 08:30:12"},
                {"LOG-8902", "RECEPTIONIST", "reception1", "Registered Patient ID #1042", "2026-08-06 09:15:45"},
                {"LOG-8903", "DOCTOR", "dr_nimal", "Updated Consultation Notes #P102", "2026-08-06 10:05:00"}
        };

        JTable auditTable = new JTable(data, cols);
        auditTable.setRowHeight(30);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(auditTable), BorderLayout.CENTER);
        return panel;
    }

    private void exportToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Table Data as CSV");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".csv")) {
                filePath += ".csv";
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.write(tableModel.getColumnName(i) + (i == tableModel.getColumnCount() - 1 ? "" : ","));
                }
                writer.write("\n");

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        writer.write(tableModel.getValueAt(i, j).toString() + (j == tableModel.getColumnCount() - 1 ? "" : ","));
                    }
                    writer.write("\n");
                }
                JOptionPane.showMessageDialog(this, "CSV File saved successfully!\nLocation: " + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving CSV file!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printTableToPDF() {
        try {
            boolean complete = searchResultTable.print(JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("SmartCare HMS - Search Report"),
                    new java.text.MessageFormat("Page - {0}"));
            if (complete) {
                JOptionPane.showMessageDialog(this, "Printing / PDF Export Completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception pe) {
            JOptionPane.showMessageDialog(this, "Error during printing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createBedWardView() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Bed & Ward Management");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        String[] cols = {"Ward Name", "Total Beds", "Occupied Beds", "Available Beds", "Status"};
        Object[][] data = {
                {"ICU Ward", "10", "8", "2", "Almost Full"},
                {"Emergency Ward", "15", "12", "3", "High Demand"},
                {"General Male Ward", "30", "18", "12", "Available"},
                {"General Female Ward", "30", "22", "8", "Available"},
                {"Pediatric Ward", "12", "5", "7", "Available"}
        };

        JTable bedTable = new JTable(data, cols);
        bedTable.setRowHeight(30);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(bedTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPharmacyView() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Pharmacy & Medicine Inventory");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        String[] cols = {"Medicine Name", "Category", "Stock Qty", "Expiry Date", "Stock Alert"};
        Object[][] data = {
                {"Paracetamol 500mg", "Painkiller", "450 Packs", "2027-12-01", "In Stock"},
                {"Amoxicillin 250mg", "Antibiotic", "15 Packs", "2026-09-15", "LOW STOCK"},
                {"Omeprazole 20mg", "Antacid", "120 Packs", "2026-08-30", "EXPIRING SOON"},
                {"Metformin 500mg", "Diabetes", "300 Packs", "2028-04-10", "In Stock"}
        };

        JTable pharmTable = new JTable(data, cols);
        pharmTable.setRowHeight(30);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(pharmTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSettingsView() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 12));
        panel.setBorder(BorderFactory.createTitledBorder("User Management & System Settings"));
        panel.setBackground(Color.WHITE);

        panel.add(new JLabel("Username:"));
        panel.add(new JTextField("admin"));

        panel.add(new JLabel("Current Password:"));
        panel.add(new JPasswordField());

        panel.add(new JLabel("New Password:"));
        panel.add(new JPasswordField());

        panel.add(new JLabel("User Role:"));
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"ADMIN", "DOCTOR", "RECEPTIONIST"});
        panel.add(cmbRole);

        JButton btnSave = new JButton("Update Password");
        btnSave.setBackground(new Color(41, 128, 185));
        btnSave.setForeground(Color.WHITE);

        panel.add(new JLabel(""));
        panel.add(btnSave);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setBackground(Color.WHITE);
        wrapper.add(panel);
        return wrapper;
    }

    private JPanel createMiniCard(String title, JLabel valueLabel, Color bgColor) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 2));
        card.setBackground(bgColor); // Dynamic Color එක apply වේ
        card.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JLabel lblTitle = new JLabel(title, JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitle.setForeground(Color.WHITE);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(Color.WHITE);

        card.add(lblTitle);
        card.add(valueLabel);
        return card;
    }

    private void performSearch(String option, String query) {
        tableModel.setRowCount(0);
        highlightedName = query;

        if ("Doctor".equalsIgnoreCase(option)) {
            String[] doctorColumns = {"#", "Doctor Name", "Available Days & Times", "Total Appointments Today"};
            tableModel.setColumnIdentifiers(doctorColumns);

            Object[][] masterData = {
                    {"Dr. Nimal Perera", "Mon / Wed (09:00 AM - 01:00 PM)", "12 Patients"},
                    {"Dr. Sunethra Silva", "Tue / Thu (04:00 PM - 08:00 PM)", "8 Patients"},
                    {"Dr. Kasun Fernando", "Daily (08:00 AM - 12:00 PM)", "15 Patients"},
                    {"Dr. Ruwan Fernando", "Fri / Sat (02:00 PM - 06:00 PM)", "10 Patients"},
                    {"Dr. Mahesh Senanayake", "Mon / Tue (10:00 AM - 02:00 PM)", "5 Patients"},
                    {"Dr. Chathuri Wickramasinghe", "Wed / Fri (05:00 PM - 09:00 PM)", "14 Patients"},
                    {"Dr. Amara Gunasekara", "Thu / Sat (09:00 AM - 01:00 PM)", "7 Patients"},
                    {"Dr. Kamal Herath", "Sun (08:00 AM - 12:00 PM)", "9 Patients"},
                    {"Dr. Dilani Jayawardena", "Daily (03:00 PM - 07:00 PM)", "11 Patients"},
                    {"Dr. Nuwan Bandara", "Mon / Thu (01:00 PM - 05:00 PM)", "6 Patients"}
            };

            populateDataWithSearchTop(masterData, query, "Doctor");

        } else if ("Patient".equalsIgnoreCase(option)) {
            String[] patientColumns = {"#", "Patient Name", "Appointment Date", "Assigned Doctor", "Payment Status"};
            tableModel.setColumnIdentifiers(patientColumns);

            Object[][] masterData = {
                    {"Kamal Gunaratne", "2026-08-06", "Dr. Nimal Perera", "PAID"},
                    {"Saman Kumara", "2026-08-06", "Dr. Sunethra Silva", "PENDING"},
                    {"Anula Jayasinghe", "2026-08-07", "Dr. Kasun Fernando", "PAID"},
                    {"Pathum Nisanka", "2026-08-07", "Dr. Ruwan Fernando", "PAID"},
                    {"Kusal Mendis", "2026-08-07", "Dr. Mahesh Senanayake", "PENDING"},
                    {"Charith Asalanka", "2026-08-08", "Dr. Chathuri Wickramasinghe", "PAID"},
                    {"Wanindu Hasaranga", "2026-08-08", "Dr. Amara Gunasekara", "PAID"},
                    {"Maheesh Theekshana", "2026-08-08", "Dr. Kamal Herath", "PENDING"},
                    {"Matheesha Pathirana", "2026-08-09", "Dr. Dilani Jayawardena", "PAID"},
                    {"Dusun Shanaka", "2026-08-09", "Dr. Nuwan Bandara", "PAID"}
            };

            populateDataWithSearchTop(masterData, query, "Patient");
        }

        if (searchResultTable.getColumnCount() > 0) {
            searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(45);
            searchResultTable.getColumnModel().getColumn(0).setMaxWidth(50);
        }
    }

    private void populateDataWithSearchTop(Object[][] masterData, String query, String type) {
        int matchIndex = -1;

        if (!query.isEmpty()) {
            for (int i = 0; i < masterData.length; i++) {
                String name = masterData[i][0].toString().toLowerCase();
                if (name.contains(query.toLowerCase())) {
                    matchIndex = i;
                    break;
                }
            }

            if (matchIndex == -1) {
                JOptionPane.showMessageDialog(this,
                        "No matching " + type + " found for: \"" + query + "\"",
                        "Search Result",
                        JOptionPane.WARNING_MESSAGE);
                highlightedName = "";
                return;
            }
        }

        int rowNum = 1;

        if (matchIndex != -1) {
            Object[] matchRow = masterData[matchIndex];
            Object[] newRow = new Object[matchRow.length + 1];
            newRow[0] = rowNum++;
            System.arraycopy(matchRow, 0, newRow, 1, matchRow.length);
            tableModel.addRow(newRow);
        }

        for (int i = 0; i < masterData.length && tableModel.getRowCount() < 10; i++) {
            if (i == matchIndex) continue;

            Object[] row = masterData[i];
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = rowNum++;
            System.arraycopy(row, 0, newRow, 1, row.length);
            tableModel.addRow(newRow);
        }

        searchResultTable.repaint();
    }

    private void switchView(JPanel newView) {
        contentArea.removeAll();
        contentArea.add(newView);
        contentArea.revalidate();
        contentArea.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DashboardFrame().setVisible(true);
        });
    }
}