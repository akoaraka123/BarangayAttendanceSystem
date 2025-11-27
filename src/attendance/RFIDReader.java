package attendance;

import java.util.function.Consumer;
import javax.swing.SwingUtilities;

/**
 * RFID Reader class para sa serial port communication
 * Supports automatic RFID card reading via serial port
 */
public class RFIDReader {
    @SuppressWarnings("unused")
    private String portName;
    @SuppressWarnings("unused")
    private int baudRate;
    private Consumer<String> onCardDetected;
    private boolean isReading;
    private Thread readingThread;
    @SuppressWarnings("unused")
    private Object serialPort; // Will use jSerialComm library
    
    // Fallback: Simulated RFID reader para sa testing kung walang hardware
    private boolean useSimulation;
    
    /**
     * Check if using simulation mode
     */
    public boolean isSimulationMode() {
        return useSimulation;
    }
    
    public RFIDReader(String portName, int baudRate) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.isReading = false;
        this.useSimulation = false;
    }
    
    /**
     * Constructor para sa simulation mode (testing without hardware)
     */
    public RFIDReader() {
        this.useSimulation = true;
        this.isReading = false;
    }
    
    /**
     * Set callback function na tatawagin kapag may RFID card na na-detect
     */
    public void setOnCardDetected(Consumer<String> callback) {
        this.onCardDetected = callback;
    }
    
    /**
     * Start reading RFID cards
     */
    public boolean startReading() {
        if (isReading) {
            return false; // Already reading
        }
        
        isReading = true;
        
        if (useSimulation) {
            startSimulation();
        } else {
            startSerialReading();
        }
        
        return true;
    }
    
    /**
     * Stop reading RFID cards
     */
    public void stopReading() {
        isReading = false;
        if (readingThread != null) {
            readingThread.interrupt();
        }
    }
    
    /**
     * Check kung currently reading
     */
    public boolean isReading() {
        return isReading;
    }
    
    /**
     * Start serial port reading (actual hardware)
     * Note: Kailangan ng jSerialComm library para dito
     */
    private void startSerialReading() {
        readingThread = new Thread(() -> {
            try {
                // Using jSerialComm library
                // Uncomment and configure kapag may actual RFID hardware na
                /*
                com.fazecast.jSerialComm.SerialPort serialPort = 
                    com.fazecast.jSerialComm.SerialPort.getCommPort(portName);
                serialPort.setBaudRate(baudRate);
                serialPort.setNumDataBits(8);
                serialPort.setNumStopBits(1);
                serialPort.setParity(com.fazecast.jSerialComm.SerialPort.NO_PARITY);
                
                if (!serialPort.openPort()) {
                    System.err.println("Failed to open serial port: " + portName);
                    isReading = false;
                    return;
                }
                
                StringBuilder cardData = new StringBuilder();
                while (isReading && !Thread.currentThread().isInterrupted()) {
                    if (serialPort.bytesAvailable() > 0) {
                        byte[] buffer = new byte[serialPort.bytesAvailable()];
                        int numRead = serialPort.readBytes(buffer, buffer.length);
                        
                        for (int i = 0; i < numRead; i++) {
                            char c = (char) buffer[i];
                            if (c == '\n' || c == '\r') {
                                if (cardData.length() > 0) {
                                    String cardId = cardData.toString().trim();
                                    if (!cardId.isEmpty() && onCardDetected != null) {
                                        SwingUtilities.invokeLater(() -> 
                                            onCardDetected.accept(cardId));
                                    }
                                    cardData.setLength(0);
                                }
                            } else if (c >= 32 && c <= 126) { // Printable characters
                                cardData.append(c);
                            }
                        }
                    }
                    Thread.sleep(10); // Small delay
                }
                
                serialPort.closePort();
                */
                
                // Check if jSerialComm library is available
                try {
                    Class<?> serialPortClass = Class.forName("com.fazecast.jSerialComm.SerialPort");
                    Object serialPort = serialPortClass.getMethod("getCommPort", String.class)
                        .invoke(null, portName);
                    
                    // Set baud rate
                    serialPortClass.getMethod("setBaudRate", int.class).invoke(serialPort, baudRate);
                    serialPortClass.getMethod("setNumDataBits", int.class).invoke(serialPort, 8);
                    serialPortClass.getMethod("setNumStopBits", int.class).invoke(serialPort, 1);
                    
                    // Open port
                    boolean opened = (Boolean) serialPortClass.getMethod("openPort").invoke(serialPort);
                    
                    if (!opened) {
                        System.err.println("❌ Failed to open serial port: " + portName);
                        System.err.println("Check if:");
                        System.err.println("  1. RFID reader is connected");
                        System.err.println("  2. COM port is correct");
                        System.err.println("  3. No other application is using the port");
                        isReading = false;
                        return;
                    }
                    
                    System.out.println("✅ RFID Reader connected: " + portName + " @ " + baudRate);
                    
                    StringBuilder cardData = new StringBuilder();
                    while (isReading && !Thread.currentThread().isInterrupted()) {
                        int bytesAvailable = (Integer) serialPortClass.getMethod("bytesAvailable").invoke(serialPort);
                        
                        if (bytesAvailable > 0) {
                            byte[] buffer = new byte[bytesAvailable];
                            int numRead = (Integer) serialPortClass.getMethod("readBytes", byte[].class, int.class)
                                .invoke(serialPort, buffer, bytesAvailable);
                            
                            for (int i = 0; i < numRead; i++) {
                                char c = (char) buffer[i];
                                if (c == '\n' || c == '\r') {
                                    if (cardData.length() > 0) {
                                        String cardId = cardData.toString().trim();
                                        if (!cardId.isEmpty() && onCardDetected != null) {
                                            SwingUtilities.invokeLater(() -> 
                                                onCardDetected.accept(cardId));
                                        }
                                        cardData.setLength(0);
                                    }
                                } else if (c >= 32 && c <= 126) { // Printable characters
                                    cardData.append(c);
                                }
                            }
                        }
                        Thread.sleep(10); // Small delay
                    }
                    
                    serialPortClass.getMethod("closePort").invoke(serialPort);
                    System.out.println("RFID Reader disconnected.");
                    
                } catch (ClassNotFoundException e) {
                    System.out.println("ℹ️ INFO: Walang jSerialComm library.");
                    System.out.println("Gamitin ang 'Enter RFID Card ID' button para sa manual entry.");
                    System.out.println("O i-click ang '🔖 Quick RFID' button sa header.");
                    // Don't stop reading - allow manual entry
                    // isReading = false;
                } catch (Exception e) {
                    System.err.println("❌ Error reading from RFID: " + e.getMessage());
                    e.printStackTrace();
                    isReading = false;
                }
                
            } catch (Exception e) {
                System.err.println("Error reading from RFID: " + e.getMessage());
                e.printStackTrace();
                isReading = false;
            }
        });
        
        readingThread.setDaemon(true);
        readingThread.start();
    }
    
    /**
     * Simulation mode para sa testing without hardware
     */
    private void startSimulation() {
        readingThread = new Thread(() -> {
            System.out.println("RFID Reader: Simulation mode active.");
            System.out.println("Para mag-test, i-type ang Employee ID sa dialog box.");
            
            // Simulation mode - hindi automatic reading
            // User can still use manual entry
            while (isReading && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        readingThread.setDaemon(true);
        readingThread.start();
    }
    
    /**
     * Manually trigger card detection (useful for simulation/testing)
     */
    public void simulateCardDetection(String cardId) {
        if (onCardDetected != null) {
            SwingUtilities.invokeLater(() -> onCardDetected.accept(cardId));
        }
    }
    
    /**
     * Get available serial ports (for configuration)
     */
    public static String[] getAvailablePorts() {
        // Using jSerialComm library
        // Uncomment kapag may library na
        /*
        com.fazecast.jSerialComm.SerialPort[] ports = 
            com.fazecast.jSerialComm.SerialPort.getCommPorts();
        String[] portNames = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            portNames[i] = ports[i].getSystemPortName();
        }
        return portNames;
        */
        
        // Default ports para sa Windows
        return new String[]{"COM1", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8"};
    }
}

