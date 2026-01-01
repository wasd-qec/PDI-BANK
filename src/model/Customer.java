package model;

/**
 * Customer entity class representing a bank customer
 * Single Responsibility: Only holds customer data and basic account operations
 */
public class Customer extends User {
    private String id;
    private String accNo;
    private double balance;
    private long phoneNumber;
    private String address;
    private String birthDate;
    private String createDate;
    private boolean active;

    public Customer(String name, String password) {
        super(name, password);
    }

    public Customer(String accNo, String id, String name, String password, double balance, 
                    long phoneNumber, String address, String birthDate, String createDate, boolean active) {
        super(name, password);
        this.accNo = accNo;
        this.id = id;
        this.balance = balance;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.birthDate = birthDate;
        this.createDate = createDate;
        this.active = active;
    }

    // Getters and Setters
    public String getAccNo() { return accNo; }
    public void setAccNo(String accNo) { this.accNo = accNo; }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    
    public long getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(long phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    
    public String getCreateDate() { return createDate; }
    public void setCreateDate(String createDate) { this.createDate = createDate; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String getRole() {
        return "Customer";
    }

    /**
     * Withdraw money from account
     * @param amount the amount to withdraw
     * @return true if successful, false if insufficient funds or invalid amount
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    /**
     * Deposit money into account
     * @param amount the amount to deposit
     * @return true if successful, false if invalid amount
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "\nID: " + id + "\nAccount: " + accNo;
    }
}
