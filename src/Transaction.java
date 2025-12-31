/**
 * Transaction entity class
 * Follows encapsulation principles with private fields and getters
 */
public class Transaction {
    private final String TransactionId;
    private final String ReceiverId;
    private final String SenderId;
    private final double Amount;
    private final String Timestamp;
    private final String Type;

    public Transaction(String TransactionId, String ReceiverId, String SenderId, double Amount, String Timestamp, String Type) {
        this.TransactionId = TransactionId;
        this.ReceiverId = ReceiverId;
        this.SenderId = SenderId;
        this.Amount = Amount;
        this.Timestamp = Timestamp;
        this.Type = Type;
    }

    @Override
    public String toString() {
        return String.format("Transaction[%s: %s -> %s, $%.2f, %s, %s]",
            TransactionId, SenderId, ReceiverId, Amount, Timestamp, Type);
    }
}
