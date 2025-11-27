package attendance;

public class Employee {
    private String employeeId;
    private String fullName;
    private String contactNumber;
    private String address;
    private String position;
    
    public Employee(String employeeId, String fullName, String contactNumber, String address, String position) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.position = position;
    }
    
    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    
    @Override
    public String toString() {
        return employeeId + "," + fullName + "," + contactNumber + "," + address + "," + position;
    }
}
