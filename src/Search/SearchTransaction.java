package Search;

import Object.Transaction;
import Factory.ServiceFactory;
import Repository.ITransactionRepository;

public class SearchTransaction {
    private final ITransactionRepository transactionRepository = ServiceFactory.getTransactionRepository();

    public Transaction findById(String transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }

    public java.util.List<Transaction> filterByAmountRange(double minAmount, double maxAmount) {
        java.util.List<Transaction> all = transactionRepository.findAll();
        java.util.List<Transaction> out = new java.util.ArrayList<>();
        for (Transaction t : all) {
            if (t.getAmount() >= minAmount && t.getAmount() <= maxAmount) out.add(t);
        }
        return out;
    }

    public java.util.List<Transaction> filterBySender(String senderId) {
        java.util.List<Transaction> all = transactionRepository.findAll();
        java.util.List<Transaction> out = new java.util.ArrayList<>();
        for (Transaction t : all) {
            if (senderId.equals(t.getSenderID())) out.add(t);
        }
        return out;
    }

    public java.util.List<Transaction> filterByReceiver(String receiverId) {
        java.util.List<Transaction> all = transactionRepository.findAll();
        java.util.List<Transaction> out = new java.util.ArrayList<>();
        for (Transaction t : all) {
            if (receiverId.equals(t.getReceiverID())) out.add(t);
        }
        return out;
    }

    public java.util.List<Transaction> filter(TransactionSearchCriteria criteria) {
        java.util.List<Transaction> all = transactionRepository.findAll();
        java.util.List<Transaction> filtered = new java.util.ArrayList<>();
        for (Transaction t : all) {
            if (matchesTransactionSearchCriteria(t, criteria) && matchesTransactionFilterCriteria(t, criteria)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    private boolean matchesTransactionSearchCriteria(Transaction t, TransactionSearchCriteria criteria) {
        if (criteria.getTransactionId() == null || criteria.getTransactionId().isEmpty()) return true;
        return criteria.getTransactionId().equals(t.getTransactionID());
    }

    private boolean matchesTransactionFilterCriteria(Transaction t, TransactionSearchCriteria criteria) {
        if (criteria.getSenderId() != null && !criteria.getSenderId().isEmpty()) {
            if (!criteria.getSenderId().equals(t.getSenderID())) return false;
        }
        if (criteria.getReceiverId() != null && !criteria.getReceiverId().isEmpty()) {
            if (!criteria.getReceiverId().equals(t.getReceiverID())) return false;
        }
        if (criteria.getType() != null && !criteria.getType().isEmpty()) {
            if (!criteria.getType().equals(t.getType())) return false;
        }
        if (criteria.getMinAmount() != null) {
            if (t.getAmount() < criteria.getMinAmount()) return false;
        }
        if (criteria.getMaxAmount() != null) {
            if (t.getAmount() > criteria.getMaxAmount()) return false;
        }
        if (criteria.getDateFrom() != null && !criteria.getDateFrom().isEmpty()) {
            if (t.getTimestamp() == null || t.getTimestamp().compareTo(criteria.getDateFrom()) < 0) return false;
        }
        if (criteria.getDateTo() != null && !criteria.getDateTo().isEmpty()) {
            if (t.getTimestamp() == null || t.getTimestamp().compareTo(criteria.getDateTo()) > 0) return false;
        }
        return true;
    }
}
