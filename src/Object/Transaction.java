package Object;

public class Transaction {
    String TransactionID;
    String ReciverID;
    String SenderID;
    double Amount;
    String Timestamp;
    String Type;

    public Transaction(String transactionID, String ReciverID, String SenderID, double Amount, String Type, String Timestamp) {
        this.TransactionID = transactionID;
        this.ReciverID = ReciverID;
        this.SenderID = SenderID;
        this.Amount = Amount;
        this.Timestamp = Timestamp;
        this.Type = Type;
    }
    public String getTransactionID() { return TransactionID; }
    public void setTransactionID(String transactionID) { this.TransactionID = transactionID; }
    public String getReciverID() { return ReciverID; }
    public void setReciverID(String reciverID) { this.ReciverID = reciverID; }
    public String getSenderID() { return SenderID; }
    public void setSenderID(String senderID) { this.SenderID = senderID; }
    public double getAmount() { return Amount; }
    public void setAmount(double amount) { this.Amount = amount; }
    public String getTimestamp() { return Timestamp; }
    public void setTimestamp(String timestamp) { this.Timestamp = timestamp; }
    public String getType() { return Type; }
    public void setType(String type) { this.Type = type; }
}
