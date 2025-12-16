package attendance;

import javax.swing.*;
import javax.swing.text.*;

/**
 * Utility class for filtering input to prevent special characters
 */
public class InputFilter {
    
    /**
     * Create a DocumentFilter that only allows letters, numbers, and spaces
     */
    public static DocumentFilter createAlphanumericFilter() {
        return new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String filtered = string.replaceAll("[^A-Za-z0-9\\s]", "");
                if (!filtered.isEmpty()) {
                    super.insertString(fb, offset, filtered, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                String filtered = text.replaceAll("[^A-Za-z0-9\\s]", "");
                if (!filtered.isEmpty() || length > 0) {
                    super.replace(fb, offset, length, filtered, attrs);
                }
            }
        };
    }
    
    /**
     * Create a DocumentFilter that only allows letters (including ñ) and spaces (for names, positions)
     */
    public static DocumentFilter createLettersOnlyFilter() {
        return new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                // Allow: letters (including ñ), spaces, hyphens, apostrophes, commas
                String filtered = string.replaceAll("[^A-Za-zñÑ\\s\\-',]", "");
                if (!filtered.isEmpty()) {
                    super.insertString(fb, offset, filtered, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                // Allow: letters (including ñ), spaces, hyphens, apostrophes, commas
                String filtered = text.replaceAll("[^A-Za-zñÑ\\s\\-',]", "");
                if (!filtered.isEmpty() || length > 0) {
                    super.replace(fb, offset, length, filtered, attrs);
                }
            }
        };
    }
    
    /**
     * Create a DocumentFilter for addresses (letters including ñ, numbers, spaces, commas, periods, hyphens)
     */
    public static DocumentFilter createAddressFilter() {
        return new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                // Allow: letters (including ñ), numbers, spaces, commas, periods, hyphens, apostrophes
                String filtered = string.replaceAll("[^A-Za-zñÑ0-9\\s,\\.\\-']", "");
                if (!filtered.isEmpty()) {
                    super.insertString(fb, offset, filtered, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                // Allow: letters (including ñ), numbers, spaces, commas, periods, hyphens, apostrophes
                String filtered = text.replaceAll("[^A-Za-zñÑ0-9\\s,\\.\\-']", "");
                if (!filtered.isEmpty() || length > 0) {
                    super.replace(fb, offset, length, filtered, attrs);
                }
            }
        };
    }
    
    /**
     * Create a DocumentFilter for department names (letters including ñ, numbers, spaces)
     */
    public static DocumentFilter createDepartmentNameFilter() {
        return new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                // Allow: letters (including ñ), numbers, spaces
                String filtered = string.replaceAll("[^A-Za-zñÑ0-9\\s]", "");
                if (!filtered.isEmpty()) {
                    super.insertString(fb, offset, filtered, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                // Allow: letters (including ñ), numbers, spaces
                String filtered = text.replaceAll("[^A-Za-zñÑ0-9\\s]", "");
                if (!filtered.isEmpty() || length > 0) {
                    super.replace(fb, offset, length, filtered, attrs);
                }
            }
        };
    }
    
    /**
     * Create a DocumentFilter for descriptions (letters including ñ, numbers, spaces, basic punctuation)
     */
    public static DocumentFilter createDescriptionFilter() {
        return new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                // Allow: letters (including ñ), numbers, spaces, commas, periods, hyphens, apostrophes
                String filtered = string.replaceAll("[^A-Za-zñÑ0-9\\s,\\.\\-']", "");
                if (!filtered.isEmpty()) {
                    super.insertString(fb, offset, filtered, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                // Allow: letters (including ñ), numbers, spaces, commas, periods, hyphens, apostrophes
                String filtered = text.replaceAll("[^A-Za-zñÑ0-9\\s,\\.\\-']", "");
                if (!filtered.isEmpty() || length > 0) {
                    super.replace(fb, offset, length, filtered, attrs);
                }
            }
        };
    }
    
    /**
     * Apply filter to a JTextField
     */
    public static void applyFilter(JTextField textField, DocumentFilter filter) {
        if (textField.getDocument() instanceof AbstractDocument) {
            ((AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
        }
    }
    
    /**
     * Apply filter to a JTextArea
     */
    public static void applyFilter(JTextArea textArea, DocumentFilter filter) {
        if (textArea.getDocument() instanceof AbstractDocument) {
            ((AbstractDocument) textArea.getDocument()).setDocumentFilter(filter);
        }
    }
}

