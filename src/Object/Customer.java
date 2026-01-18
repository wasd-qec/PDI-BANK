package Object;

public class Customer {
    private String ID;
    private double Balance;
    private int PhoneNumber;
    private String AccNo;
    private String Address;
    private String BirthDate;
    private String CreateDate;
    private boolean Active;
    private String Name;
    private String HashedPassword;
    
    public Customer(String name, String HashedPassword, String ID, 
                    String accNo, double balance, int PhoneNumber, 
                    String address, String BirthDate, String CreateDate, 
                    boolean Active) {
        this.Name = name;
        this.HashedPassword = HashedPassword;
        this.ID = ID;
        this.AccNo = accNo;
        this.Balance = balance;
        this.PhoneNumber = PhoneNumber;
        this.Address = address;
        this.BirthDate = BirthDate;
        this.CreateDate = CreateDate;
        this.Active = Active;
    }
    
    public String getID() { return ID;}
    public void setID(String iD) { this.ID = iD;}
    public double getBalance() { return Balance;}
    public void setBalance(double balance) { this.Balance = balance;}
    public int getPhoneNumber() { return PhoneNumber;}
    public void setPhoneNumber(int phoneNumber) { this.PhoneNumber = phoneNumber;}
    public String getAccNo() { return AccNo;}
    public void setAccNo(String accNo) {this.AccNo = accNo;}
    public String getAddress() {return Address;}
    public void setAddress(String address) {this.Address = address;}
    public String getBirthDate() {return BirthDate;}
    public void setBirthDate(String birthDate) {this.BirthDate = birthDate;}
    public String getCreateDate() {return CreateDate;}
    public void setCreateDate(String createDate) { this.CreateDate = createDate;}
    public boolean isActive() {return Active;}
    public void setActive(boolean active) {this.Active = active;}
    public String getName() {return Name;}
    public void setName(String name) {this.Name = name;}
    public String getPassword() {return HashedPassword;}
    public void setPassword(String password) {this.HashedPassword = password;}

    public Boolean withdraw(double amount) {
        if (amount > 0 && amount <= Balance) {
            Balance -= amount;
            return true;
        } else {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }
    }
    public Boolean deposit(double amount) {
        if (amount > 0) {
            Balance += amount;
            return true;
        } else {
            throw new IllegalArgumentException("Invalid deposit amount");
        }
    }
    public String toString() {
        return "Account number:" + AccNo + ", Name: " + Name + ", Balance: " + Balance;
}
}


    