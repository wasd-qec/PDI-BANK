package Search;

/**
 * Search and filter criteria for Customer entities
 * Supports searching by: AccNo, ID, Name, Phone Number
 * Supports filtering by: Name, Create Date, Balance (amount), Active status, Address
 */
public class CustomerSearchCriteria {
    
    // Search fields (exact or partial match)
    private String accNo;
    private String id;
    private String name;
    private String phoneNumber;
    
    // Filter fields
    private String nameFilter;
    private String createDateFrom;
    private String createDateTo;
    private Double minBalance;
    private Double maxBalance;
    private Boolean active;
    private String addressFilter;
    
    // Constructors
    public CustomerSearchCriteria() {
    }
    
    // Builder pattern for easy construction
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters and Setters
    public String getAccNo() { return accNo; }
    public void setAccNo(String accNo) { this.accNo = accNo; }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getNameFilter() { return nameFilter; }
    public void setNameFilter(String nameFilter) { this.nameFilter = nameFilter; }
    
    public String getCreateDateFrom() { return createDateFrom; }
    public void setCreateDateFrom(String createDateFrom) { this.createDateFrom = createDateFrom; }
    
    public String getCreateDateTo() { return createDateTo; }
    public void setCreateDateTo(String createDateTo) { this.createDateTo = createDateTo; }
    
    public Double getMinBalance() { return minBalance; }
    public void setMinBalance(Double minBalance) { this.minBalance = minBalance; }
    
    public Double getMaxBalance() { return maxBalance; }
    public void setMaxBalance(Double maxBalance) { this.maxBalance = maxBalance; }
    
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    
    public String getAddressFilter() { return addressFilter; }
    public void setAddressFilter(String addressFilter) { this.addressFilter = addressFilter; }
    
    /**
     * Check if this criteria has any search terms set
     */
    public boolean hasSearchTerms() {
        return (accNo != null && !accNo.isEmpty()) ||
               (id != null && !id.isEmpty()) ||
               (name != null && !name.isEmpty()) ||
               (phoneNumber != null && !phoneNumber.isEmpty());
    }
    
    /**
     * Check if this criteria has any filters set
     */
    public boolean hasFilters() {
        return (nameFilter != null && !nameFilter.isEmpty()) ||
               (createDateFrom != null && !createDateFrom.isEmpty()) ||
               (createDateTo != null && !createDateTo.isEmpty()) ||
               minBalance != null ||
               maxBalance != null ||
               active != null ||
               (addressFilter != null && !addressFilter.isEmpty());
    }
    
    /**
     * Builder class for CustomerSearchCriteria
     */
    public static class Builder {
        private final CustomerSearchCriteria criteria = new CustomerSearchCriteria();
        
        public Builder accNo(String accNo) {
            criteria.setAccNo(accNo);
            return this;
        }
        
        public Builder id(String id) {
            criteria.setId(id);
            return this;
        }
        
        public Builder name(String name) {
            criteria.setName(name);
            return this;
        }
        
        public Builder phoneNumber(String phoneNumber) {
            criteria.setPhoneNumber(phoneNumber);
            return this;
        }
        
        public Builder nameFilter(String nameFilter) {
            criteria.setNameFilter(nameFilter);
            return this;
        }
        
        public Builder createDateFrom(String createDateFrom) {
            criteria.setCreateDateFrom(createDateFrom);
            return this;
        }
        
        public Builder createDateTo(String createDateTo) {
            criteria.setCreateDateTo(createDateTo);
            return this;
        }
        
        public Builder minBalance(Double minBalance) {
            criteria.setMinBalance(minBalance);
            return this;
        }
        
        public Builder maxBalance(Double maxBalance) {
            criteria.setMaxBalance(maxBalance);
            return this;
        }
        
        public Builder active(Boolean active) {
            criteria.setActive(active);
            return this;
        }
        
        public Builder addressFilter(String addressFilter) {
            criteria.setAddressFilter(addressFilter);
            return this;
        }
        
        public CustomerSearchCriteria build() {
            return criteria;
        }
    }
}
