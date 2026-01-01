package model;

/**
 * Transaction entity class (immutable)
 * Single Responsibility: Only holds transaction data
 */
public class Transaction {
    private final String transactionId;
    private final String receiverId;
    private final String senderId;
    private final double amount;
    private final String timestamp;
    private final String type;

    public Transaction(String transactionId, String receiverId, String senderId, 
                       double amount, String timestamp, String type) {
        this.transactionId = transactionId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
    }

    public String getTransactionId() { return transactionId; }
    public String getReceiverId() { return receiverId; }
    public String getSenderId() { return senderId; }
    public double getAmount() { return amount; }
    public String getTimestamp() { return timestamp; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return String.format("Transaction[%s: %s -> %s, $%.2f, %s, %s]",
            transactionId, senderId, receiverId, amount, timestamp, type);
    }
}
