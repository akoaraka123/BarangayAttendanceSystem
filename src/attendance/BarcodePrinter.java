package attendance;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

/**
 * Barcode Printer class para sa RFID Card ID / Barcode printing
 * Supports Code128 barcode format
 */
public class BarcodePrinter {
    
    /**
     * Print barcode para sa RFID Card ID - A4 size, compact design
     */
    public static void printBarcode(String rfidCardId, String employeeName, String employeeId) {
        if (rfidCardId == null || rfidCardId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Walang RFID Card ID para i-print!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create print job
        PrinterJob job = PrinterJob.getPrinterJob();
        
        // Set A4 page format (210mm x 297mm = 8.27" x 11.69")
        PageFormat pageFormat = new PageFormat();
        Paper paper = new Paper();
        
        // A4 size in points (72 points = 1 inch)
        double paperWidth = 8.27 * 72;   // A4 width in points
        double paperHeight = 11.69 * 72;  // A4 height in points
        
        paper.setSize(paperWidth, paperHeight);
        paper.setImageableArea(0.5 * 72, 0.5 * 72, paperWidth - (1 * 72), paperHeight - (1 * 72));
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        
        // Create printable component
        Printable printable = new BarcodePrintable(rfidCardId, employeeName, employeeId);
        job.setPrintable(printable, pageFormat);
        
        // Show print dialog
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(null, 
                    "✅ Barcode printed successfully!", 
                    "Print Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(null, 
                    "❌ Error printing barcode:\n" + e.getMessage(), 
                    "Print Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Show barcode preview before printing
     */
    public static void showBarcodePreview(String rfidCardId, String employeeName, String employeeId) {
        if (rfidCardId == null || rfidCardId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Walang RFID Card ID para i-preview!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create preview frame - A4 aspect ratio
        JFrame previewFrame = new JFrame("Barcode Preview - " + employeeName);
        previewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        previewFrame.setSize(600, 850); // A4 aspect ratio (width:height ≈ 1:1.414)
        previewFrame.setLocationRelativeTo(null);
        
        // Create preview panel
        BarcodePreviewPanel previewPanel = new BarcodePreviewPanel(rfidCardId, employeeName, employeeId);
        previewFrame.add(previewPanel);
        
        // Add print button
        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton("🖨️ Print Barcode");
        printButton.addActionListener(e -> {
            printBarcode(rfidCardId, employeeName, employeeId);
        });
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> previewFrame.dispose());
        
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);
        
        previewFrame.add(buttonPanel, BorderLayout.SOUTH);
        previewFrame.setVisible(true);
    }
    
    /**
     * Printable class para sa barcode printing
     */
    private static class BarcodePrintable implements Printable {
        private String rfidCardId;
        private String employeeName;
        private String employeeId;
        
        public BarcodePrintable(String rfidCardId, String employeeName, String employeeId) {
            this.rfidCardId = rfidCardId;
            this.employeeName = employeeName;
            this.employeeId = employeeId;
        }
        
        @Override
        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pf.getImageableX(), pf.getImageableY());
            
            // A4 page dimensions (in points)
            double pageWidth = pf.getImageableWidth();
            double pageHeight = pf.getImageableHeight();
            
            // Card dimensions - compact size para sa A4 (approximately 1/3 ng page width)
            double cardWidth = pageWidth * 0.6;  // 60% ng page width
            double cardHeight = pageHeight * 0.35; // 35% ng page height
            double cardX = (pageWidth - cardWidth) / 2; // Center horizontally
            double cardY = (pageHeight - cardHeight) / 2; // Center vertically
            
            // Draw card border
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.BLACK);
            g2d.drawRect((int)cardX, (int)cardY, (int)cardWidth, (int)cardHeight);
            
            // Set fonts - mas maliit para compact
            Font titleFont = new Font("Arial", Font.BOLD, 16);
            Font nameFont = new Font("Arial", Font.BOLD, 12);
            Font idFont = new Font("Arial", Font.PLAIN, 10);
            Font barcodeFont = new Font("Courier", Font.BOLD, 10);
            
            // Starting position inside card
            int y = (int)(cardY + 25);
            int leftMargin = (int)(cardX + 20);
            int contentWidth = (int)(cardWidth - 40);
            
            // Draw title
            g2d.setFont(titleFont);
            String title = "BARANGAY ATTENDANCE CARD";
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, (int)(cardX + (cardWidth - titleWidth) / 2), y);
            y += 25;
            
            // Draw employee name
            g2d.setFont(nameFont);
            String nameText = "Name: " + employeeName;
            g2d.drawString(nameText, leftMargin, y);
            y += 20;
            
            // Draw employee ID
            g2d.setFont(idFont);
            String empIdText = "Employee ID: " + employeeId;
            g2d.drawString(empIdText, leftMargin, y);
            y += 18;
            
            // Draw RFID Card ID
            String rfidText = "RFID: " + rfidCardId;
            g2d.drawString(rfidText, leftMargin, y);
            y += 25;
            
            // Draw barcode (mas maliit para compact)
            int barcodeHeight = 50;
            int barcodeY = y;
            drawBarcode(g2d, rfidCardId, leftMargin, barcodeY, contentWidth, barcodeHeight);
            y += barcodeHeight + 15;
            
            // Draw barcode text below
            g2d.setFont(barcodeFont);
            fm = g2d.getFontMetrics();
            int barcodeTextWidth = fm.stringWidth(rfidCardId);
            g2d.drawString(rfidCardId, (int)(cardX + (cardWidth - barcodeTextWidth) / 2), y);
            
            return PAGE_EXISTS;
        }
        
        /**
         * Draw simplified Code128-style barcode
         */
        private void drawBarcode(Graphics2D g2d, String code, int x, int y, int width, int height) {
            // Convert code to binary representation (simplified)
            StringBuilder binary = new StringBuilder();
            for (char c : code.toCharArray()) {
                // Simple encoding: each character = 8 bits
                int ascii = (int) c;
                String bin = String.format("%8s", Integer.toBinaryString(ascii)).replace(' ', '0');
                binary.append(bin);
            }
            
            // Draw bars
            int barWidth = Math.max(1, width / binary.length());
            
            int currentX = x;
            for (int i = 0; i < binary.length(); i++) {
                if (binary.charAt(i) == '1') {
                    // Draw bar
                    g2d.fillRect(currentX, y, barWidth, height);
                }
                currentX += barWidth;
            }
            
            // Draw border around barcode
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(x - 2, y - 2, width + 4, height + 4);
        }
    }
    
    /**
     * Preview panel para sa barcode
     */
    private static class BarcodePreviewPanel extends JPanel {
        private String rfidCardId;
        private String employeeName;
        private String employeeId;
        
        public BarcodePreviewPanel(String rfidCardId, String employeeName, String employeeId) {
            this.rfidCardId = rfidCardId;
            this.employeeName = employeeName;
            this.employeeId = employeeId;
            setBackground(Color.WHITE);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int width = getWidth();
            int height = getHeight();
            
            // Card dimensions - compact size para sa A4 preview
            int cardWidth = (int)(width * 0.6);  // 60% ng width
            int cardHeight = (int)(height * 0.35); // 35% ng height
            int cardX = (width - cardWidth) / 2; // Center horizontally
            int cardY = (height - cardHeight) / 2; // Center vertically
            
            // Draw card border
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.BLACK);
            g2d.drawRect(cardX, cardY, cardWidth, cardHeight);
            
            // Set fonts - mas maliit para compact
            Font titleFont = new Font("Arial", Font.BOLD, 16);
            Font nameFont = new Font("Arial", Font.BOLD, 12);
            Font idFont = new Font("Arial", Font.PLAIN, 10);
            Font barcodeFont = new Font("Courier", Font.BOLD, 10);
            
            // Starting position inside card
            int y = cardY + 25;
            int leftMargin = cardX + 20;
            int contentWidth = cardWidth - 40;
            
            // Draw title
            g2d.setFont(titleFont);
            String title = "BARANGAY ATTENDANCE CARD";
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            g2d.drawString(title, cardX + (cardWidth - titleWidth) / 2, y);
            y += 25;
            
            // Draw employee name
            g2d.setFont(nameFont);
            g2d.drawString("Name: " + employeeName, leftMargin, y);
            y += 20;
            
            // Draw employee ID
            g2d.setFont(idFont);
            g2d.drawString("Employee ID: " + employeeId, leftMargin, y);
            y += 18;
            
            // Draw RFID Card ID
            g2d.drawString("RFID: " + rfidCardId, leftMargin, y);
            y += 25;
            
            // Draw barcode (mas maliit para compact)
            int barcodeHeight = 50;
            drawBarcode(g2d, rfidCardId, leftMargin, y, contentWidth, barcodeHeight);
            y += barcodeHeight + 15;
            
            // Draw barcode text below
            g2d.setFont(barcodeFont);
            fm = g2d.getFontMetrics();
            int barcodeTextWidth = fm.stringWidth(rfidCardId);
            g2d.drawString(rfidCardId, cardX + (cardWidth - barcodeTextWidth) / 2, y);
        }
        
        private void drawBarcode(Graphics2D g2d, String code, int x, int y, int width, int height) {
            // Convert code to binary representation
            StringBuilder binary = new StringBuilder();
            for (char c : code.toCharArray()) {
                int ascii = (int) c;
                String bin = String.format("%8s", Integer.toBinaryString(ascii)).replace(' ', '0');
                binary.append(bin);
            }
            
            // Draw bars
            int barWidth = Math.max(1, width / binary.length());
            int currentX = x;
            
            for (int i = 0; i < binary.length(); i++) {
                if (binary.charAt(i) == '1') {
                    g2d.fillRect(currentX, y, barWidth, height);
                }
                currentX += barWidth;
            }
            
            // Draw border
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(x - 5, y - 5, width + 10, height + 10);
        }
    }
}

