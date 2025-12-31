public class Customer extends User {
    private String id;
    private double balance;
    private long PhoneNumber;
    private String accNo;
    private String address;
    private String BirthDate;
    private String CreateDate;

    
    public Customer(String name, String password) {
        super(name, password);
    }

    //still within testing didnt put all the variables yet
    public Customer(String name, String password, String id, String accNo) {
        super(name, password);
        this.id = id;
        this.accNo = accNo;
    }

    public Customer(String name, String password, int age, String id) {
        super(name, password, age);
        this.id = id;
    }
    
    // Constructor with all 10 parameters
    public Customer(String accNo, String id, String name, String password, double balance, 
                    long PhoneNumber, String address, String BirthDate, String CreateDate, boolean active) {
        super(name, password);
        this.accNo = accNo;
        this.id = id;
        this.balance = balance;
        this.PhoneNumber = PhoneNumber;
        this.address = address;
        this.BirthDate = BirthDate;
        this.CreateDate = CreateDate;
    }

    // Getters and Setters
    public String getAccNo() { return accNo; }
    public void setAccNo(String accNo) { this.accNo = accNo; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public long getPhoneNumber() { return PhoneNumber; }
    public void setPhoneNumber(long phoneNumber) { PhoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getBirthDate() { return BirthDate; }
    public void setBirthDate(String birthDate) { BirthDate = birthDate; }
    public String getCreateDate() { return CreateDate; }
    public void setCreateDate(String createDate) { CreateDate = createDate; }
    @Override
    public String getRole() {
        return "Customer";
    }
    @Override
    public String toString() {
        return super.toString() + "\nID: " + id;
    }
    
    // Transaction methods
    /**
     * Withdraw money from account
     * @param amount the amount to withdraw
     * @return true if successful, false if insufficient funds
     */
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be positive");
            return false;
        }
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        System.out.println("Insufficient funds");
        return false;
    }
    
    /**
     * Deposit money into account
     * @param amount the amount to deposit
     * @return true if successful
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be positive");
            return false;
        }
        balance += amount;
        return true;
    }
}
