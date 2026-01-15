package Object;

public class Transaction {
    String TransactionID;
    String ReceiverID;
    String SenderID;
    double Amount;
    String Timestamp;
    String Type;

    public Transaction(String transactionID, String ReceiverID, String SenderID, double Amount, String Type, String Timestamp) {
        this.TransactionID = transactionID;
        this.ReceiverID = ReceiverID;
        this.SenderID = SenderID;
        this.Amount = Amount;
        this.Timestamp = Timestamp;
        this.Type = Type;
    }
    public String getTransactionID() { return TransactionID; }
    public void setTransactionID(String transactionID) { this.TransactionID = transactionID; }
    public String getReceiverID() { return ReceiverID; }
    public void setReceiverID(String receiverID) { this.ReceiverID = receiverID; }
    public String getSenderID() { return SenderID; }
    public void setSenderID(String senderID) { this.SenderID = senderID; }
    public double getAmount() { return Amount; }
    public void setAmount(double amount) { this.Amount = amount; }
    public String getTimestamp() { return Timestamp; }
    public void setTimestamp(String timestamp) { this.Timestamp = timestamp; }
    public String getType() { return Type; }
    public void setType(String type) { this.Type = type; }
}
