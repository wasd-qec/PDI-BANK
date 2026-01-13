package Search;

/**
 * Search and filter criteria for Transaction entities
 * Supports searching by: Transaction ID
 * Supports filtering by: Amount range, Type, Receiver ID, Sender ID, Date range
 */
public class TransactionSearchCriteria {
    
    // Search field
    private String transactionId;
    
    // Filter fields
    private Double minAmount;
    private Double maxAmount;
    private String type;           // DEPOSIT, WITHDRAWAL, TRANSFER
    private String receiverId;
    private String senderId;
    private String dateFrom;       // Format: yyyy-MM-dd or yyyy-MM-dd HH:mm:ss
    private String dateTo;
    
    // Constructors
    public TransactionSearchCriteria() {
    }
    
    // Builder pattern for easy construction
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public Double getMinAmount() { return minAmount; }
    public void setMinAmount(Double minAmount) { this.minAmount = minAmount; }
    
    public Double getMaxAmount() { return maxAmount; }
    public void setMaxAmount(Double maxAmount) { this.maxAmount = maxAmount; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    
    public String getDateFrom() { return dateFrom; }
    public void setDateFrom(String dateFrom) { this.dateFrom = dateFrom; }
    
    public String getDateTo() { return dateTo; }
    public void setDateTo(String dateTo) { this.dateTo = dateTo; }
    
    /**
     * Check if this criteria has a search term set
     */
    public boolean hasSearchTerm() {
        return transactionId != null && !transactionId.isEmpty();
    }
    
    /**
     * Check if this criteria has any filters set
     */
    public boolean hasFilters() {
        return minAmount != null ||
               maxAmount != null ||
               (type != null && !type.isEmpty()) ||
               (receiverId != null && !receiverId.isEmpty()) ||
               (senderId != null && !senderId.isEmpty()) ||
               (dateFrom != null && !dateFrom.isEmpty()) ||
               (dateTo != null && !dateTo.isEmpty());
    }
    
    /**
     * Builder class for TransactionSearchCriteria
     */
    public static class Builder {
        private final TransactionSearchCriteria criteria = new TransactionSearchCriteria();
        
        public Builder transactionId(String transactionId) {
            criteria.setTransactionId(transactionId);
            return this;
        }
        
        public Builder minAmount(Double minAmount) {
            criteria.setMinAmount(minAmount);
            return this;
        }
        
        public Builder maxAmount(Double maxAmount) {
            criteria.setMaxAmount(maxAmount);
            return this;
        }
        
        public Builder type(String type) {
            criteria.setType(type);
            return this;
        }
        
        public Builder receiverId(String receiverId) {
            criteria.setReceiverId(receiverId);
            return this;
        }
        
        public Builder senderId(String senderId) {
            criteria.setSenderId(senderId);
            return this;
        }
        
        public Builder dateFrom(String dateFrom) {
            criteria.setDateFrom(dateFrom);
            return this;
        }
        
        public Builder dateTo(String dateTo) {
            criteria.setDateTo(dateTo);
            return this;
        }
        
        public TransactionSearchCriteria build() {
            return criteria;
        }
    }
}
