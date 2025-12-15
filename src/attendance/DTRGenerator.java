package attendance;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.util.List;
import java.sql.Time;

/**
 * DTR (Daily Time Record) Generator
 * Generates and prints DTR in the official format
 */
public class DTRGenerator {
    
    /**
     * Generate and print DTR for an employee
     */
    public static void generateDTR(String employeeId, String employeeName, int year, int month) {
        // Get attendance logs for the specified month
        List<AttendanceLog> logs = DatabaseOperations.getAttendanceLogsByMonth(employeeId, year, month);
        
        // Create print job
        PrinterJob job = PrinterJob.getPrinterJob();
        
        // Set A4 page format
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        
        // A4 size in points (72 points = 1 inch)
        double paperWidth = 8.27 * 72;   // A4 width in points
        double paperHeight = 11.69 * 72;  // A4 height in points
        
        paper.setSize(paperWidth, paperHeight);
        paper.setImageableArea(0.5 * 72, 0.5 * 72, paperWidth - (1 * 72), paperHeight - (1 * 72));
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        
        // Create printable DTR
        DTRPrintable printable = new DTRPrintable(employeeName, year, month, logs);
        job.setPrintable(printable, pageFormat);
        
        // Show print dialog
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(null, 
                    "✅ DTR printed successfully!", 
                    "Print Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(null, 
                    "❌ Error printing DTR:\n" + e.getMessage(), 
                    "Print Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Show DTR preview
     */
    public static void showDTRPreview(String employeeId, String employeeName, int year, int month) {
        List<AttendanceLog> logs = DatabaseOperations.getAttendanceLogsByMonth(employeeId, year, month);
        
        JFrame previewFrame = new JFrame("DTR Preview - " + employeeName);
        previewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        previewFrame.setSize(800, 1000);
        previewFrame.setLocationRelativeTo(null);
        
        DTRPreviewPanel previewPanel = new DTRPreviewPanel(employeeName, year, month, logs);
        JScrollPane scrollPane = new JScrollPane(previewPanel);
        previewFrame.add(scrollPane);
        
        // Add print button
        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton("🖨️ Print DTR");
        printButton.addActionListener(e -> {
            generateDTR(employeeId, employeeName, year, month);
        });
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> previewFrame.dispose());
        
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);
        
        previewFrame.add(buttonPanel, BorderLayout.SOUTH);
        previewFrame.setVisible(true);
    }
    
    /**
     * Printable class for DTR
     */
    private static class DTRPrintable implements Printable {
        private String employeeName;
        private int year;
        private int month;
        private List<AttendanceLog> logs;
        
        public DTRPrintable(String employeeName, int year, int month, List<AttendanceLog> logs) {
            this.employeeName = employeeName;
            this.year = year;
            this.month = month;
            this.logs = logs;
        }
        
        @Override
        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            
            double pageWidth = pf.getImageableWidth();
            double pageHeight = pf.getImageableHeight();
            
            // Margins
            double margin = 20;
            double x = margin;
            double y = margin;
            
            // Title
            Font titleFont = new Font("Arial", Font.BOLD, 18);
            g2d.setFont(titleFont);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            String title = "TIME RECORD";
            int titleWidth = titleMetrics.stringWidth(title);
            g2d.drawString(title, (float)((pageWidth - titleWidth) / 2), (float)y);
            y += titleMetrics.getHeight() + 10;
            
            // Employee Name
            Font nameFont = new Font("Arial", Font.PLAIN, 14);
            g2d.setFont(nameFont);
            FontMetrics nameMetrics = g2d.getFontMetrics();
            int nameWidth = nameMetrics.stringWidth(employeeName);
            g2d.drawString(employeeName, (float)((pageWidth - nameWidth) / 2), (float)y);
            y += nameMetrics.getHeight() + 15;
            
            // Period
            String monthName = LocalDate.of(year, month, 1).format(DateTimeFormatter.ofPattern("MMMM"));
            String period = "For the month of " + monthName.toUpperCase() + " " + year;
            g2d.drawString(period, (float)x, (float)y);
            y += nameMetrics.getHeight() + 5;
            
            g2d.drawString("Official hours for and departure", (float)x, (float)y);
            y += nameMetrics.getHeight() + 5;
            g2d.drawString("Regular days...", (float)x, (float)y);
            y += nameMetrics.getHeight() + 5;
            g2d.drawString("Saturdays...", (float)x, (float)y);
            y += nameMetrics.getHeight() + 15;
            
            // Table header
            Font tableFont = new Font("Arial", Font.PLAIN, 9);
            g2d.setFont(tableFont);
            FontMetrics tableMetrics = g2d.getFontMetrics();
            double rowHeight = tableMetrics.getHeight() + 2;
            
            // Draw table lines
            double tableX = x;
            double tableY = y;
            double col1Width = 30;  // DAY
            double col2Width = 50;  // A.M. Arrival
            double col3Width = 50;  // A.M. Departure
            double col4Width = 50;  // P.M. Arrival
            double col5Width = 50;  // P.M. Departure
            double col6Width = 40;  // UNDERTIME Hours
            double col7Width = 40;  // UNDERTIME Minutes
            
            // Header row
            g2d.drawString("DAY", (float)tableX, (float)tableY);
            g2d.drawString("A.M.", (float)(tableX + col1Width), (float)tableY);
            g2d.drawString("P.M.", (float)(tableX + col1Width + col2Width + col3Width), (float)tableY);
            g2d.drawString("UNDERTIME", (float)(tableX + col1Width + col2Width + col3Width + col4Width + col5Width), (float)tableY);
            
            tableY += rowHeight;
            g2d.drawString("", (float)tableX, (float)tableY);
            g2d.drawString("Arrival", (float)(tableX + col1Width), (float)tableY);
            g2d.drawString("Departure", (float)(tableX + col1Width + col2Width), (float)tableY);
            g2d.drawString("Arrival", (float)(tableX + col1Width + col2Width + col3Width), (float)tableY);
            g2d.drawString("Departure", (float)(tableX + col1Width + col2Width + col3Width + col4Width), (float)tableY);
            g2d.drawString("Hours", (float)(tableX + col1Width + col2Width + col3Width + col4Width + col5Width), (float)tableY);
            g2d.drawString("Minutes", (float)(tableX + col1Width + col2Width + col3Width + col4Width + col5Width + col6Width), (float)tableY);
            
            tableY += rowHeight;
            
            // Draw table data for each day of the month
            LocalDate startDate = LocalDate.of(year, month, 1);
            int daysInMonth = startDate.lengthOfMonth();
            
            // Create a map of logs by date
            java.util.Map<Integer, AttendanceLog> logMap = new java.util.HashMap<>();
            for (AttendanceLog log : logs) {
                LocalDate logDate = log.getLogDate().toLocalDate();
                if (logDate.getYear() == year && logDate.getMonthValue() == month) {
                    logMap.put(logDate.getDayOfMonth(), log);
                }
            }
            
            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate currentDate = LocalDate.of(year, month, day);
                DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                
                // Day number
                g2d.drawString(String.valueOf(day), (float)tableX, (float)tableY);
                
                AttendanceLog log = logMap.get(day);
                
                if (dayOfWeek == DayOfWeek.SATURDAY) {
                    g2d.drawString("SATURDAY", (float)(tableX + col1Width), (float)tableY);
                } else if (dayOfWeek == DayOfWeek.SUNDAY) {
                    g2d.drawString("SUNDAY", (float)(tableX + col1Width), (float)tableY);
                } else if (log != null) {
                    // A.M. Arrival
                    if (log.getMorningClockIn() != null) {
                        String time = formatTime(log.getMorningClockIn());
                        g2d.drawString(time, (float)(tableX + col1Width), (float)tableY);
                    }
                    
                    // A.M. Departure
                    if (log.getMorningClockOut() != null) {
                        String time = formatTime(log.getMorningClockOut());
                        g2d.drawString(time, (float)(tableX + col1Width + col2Width), (float)tableY);
                    }
                    
                    // P.M. Arrival
                    if (log.getAfternoonClockIn() != null) {
                        String time = formatTime(log.getAfternoonClockIn());
                        g2d.drawString(time, (float)(tableX + col1Width + col2Width + col3Width), (float)tableY);
                    }
                    
                    // P.M. Departure
                    if (log.getAfternoonClockOut() != null) {
                        String time = formatTime(log.getAfternoonClockOut());
                        g2d.drawString(time, (float)(tableX + col1Width + col2Width + col3Width + col4Width), (float)tableY);
                    }
                    
                    // Calculate undertime (if any)
                    // TODO: Calculate undertime based on expected hours
                }
                
                tableY += rowHeight;
                
                // Check if we need a new page
                if (tableY > pageHeight - 100) {
                    // Would need to handle page breaks for long months
                    break;
                }
            }
            
            // Total line
            tableY += rowHeight;
            g2d.drawString("TOTAL", (float)tableX, (float)tableY);
            
            // Certification
            tableY += rowHeight * 2;
            String certText = "I CERTIFY on my honor that the above is a true and correct report of the hours of work performed, record of which was made daily at the time of arrival at the departure from the office.";
            drawWrappedText(g2d, certText, (float)x, (float)tableY, (float)(pageWidth - 2 * margin), tableFont);
            tableY += rowHeight * 3;
            
            g2d.drawString("Verified as prescribed office hours", (float)x, (float)tableY);
            tableY += rowHeight * 2;
            
            // Signature line
            g2d.drawString("_________________________", (float)x, (float)tableY);
            tableY += rowHeight;
            g2d.drawString("Punong Barangay", (float)x, (float)tableY);
            
            return PAGE_EXISTS;
        }
        
        private String formatTime(Time time) {
            if (time == null) return "";
            return time.toString().substring(0, 5); // HH:mm format
        }
        
        private void drawWrappedText(Graphics2D g2d, String text, float x, float y, float maxWidth, Font font) {
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            float currentY = y;
            
            for (String word : words) {
                String testLine = line.length() == 0 ? word : line + " " + word;
                float width = g2d.getFontMetrics(font).stringWidth(testLine);
                
                if (width > maxWidth && line.length() > 0) {
                    g2d.drawString(line.toString(), x, currentY);
                    line = new StringBuilder(word);
                    currentY += g2d.getFontMetrics(font).getHeight();
                } else {
                    line = new StringBuilder(testLine);
                }
            }
            if (line.length() > 0) {
                g2d.drawString(line.toString(), x, currentY);
            }
        }
    }
    
    /**
     * Preview panel for DTR
     */
    private static class DTRPreviewPanel extends JPanel {
        private String employeeName;
        private int year;
        private int month;
        private List<AttendanceLog> logs;
        
        public DTRPreviewPanel(String employeeName, int year, int month, List<AttendanceLog> logs) {
            this.employeeName = employeeName;
            this.year = year;
            this.month = month;
            this.logs = logs;
            setPreferredSize(new Dimension(800, 1200));
            setBackground(Color.WHITE);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            
            // Similar drawing logic as DTRPrintable but for preview
            // This is a simplified version - you can enhance it
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("TIME RECORD", 350, 30);
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.drawString(employeeName, 350, 60);
            
            String monthName = LocalDate.of(year, month, 1).format(DateTimeFormatter.ofPattern("MMMM"));
            g2d.drawString("For the month of " + monthName.toUpperCase() + " " + year, 20, 90);
        }
    }
}

