package Search;

import Object.Customer;
import Factory.ServiceFactory;
import Repository.ICustomerRepository;

public class SearchCustomer {
	private final ICustomerRepository customerRepository = ServiceFactory.getCustomerRepository();

	public Customer findByAccNo(String accNo) {
		return customerRepository.findByAccNo(accNo).orElse(null);
	}

	public java.util.List<Customer> findByName(String namePattern) {
		java.util.List<Customer> all = customerRepository.findAll();
		java.util.List<Customer> results = new java.util.ArrayList<>();
		if (namePattern == null || namePattern.trim().isEmpty()) return all;
		String p = namePattern.toLowerCase();
		for (Customer c : all) {
			if (c.getName() != null && c.getName().toLowerCase().contains(p)) {
				results.add(c);
			}
		}
		return results;
	}

	public java.util.List<Customer> filter(CustomerSearchCriteria criteria) {
		java.util.List<Customer> all = customerRepository.findAll();
		java.util.List<Customer> filtered = new java.util.ArrayList<>();
		for (Customer c : all) {
			if (matchesCustomerSearchCriteria(c, criteria) && matchesCustomerFilterCriteria(c, criteria)) {
				filtered.add(c);
			}
		}
		return filtered;
	}

	// reuse existing matching helpers from old SearchService logic
	private boolean matchesCustomerSearchCriteria(Customer customer, CustomerSearchCriteria criteria) {
		if (!criteria.hasSearchTerms()) return true;

		boolean matches = true;

		if (criteria.getAccNo() != null && !criteria.getAccNo().isEmpty()) {
			matches = matches && customer.getAccNo() != null && customer.getAccNo().toLowerCase().contains(criteria.getAccNo().toLowerCase());
		}
		if (criteria.getId() != null && !criteria.getId().isEmpty()) {
			matches = matches && customer.getID() != null && customer.getID().toLowerCase().contains(criteria.getId().toLowerCase());
		}
		if (criteria.getName() != null && !criteria.getName().isEmpty()) {
			matches = matches && customer.getName() != null && customer.getName().toLowerCase().contains(criteria.getName().toLowerCase());
		}
		if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isEmpty()) {
			matches = matches && String.valueOf(customer.getPhoneNumber()).contains(criteria.getPhoneNumber());
		}

		return matches;
	}

	private boolean matchesCustomerFilterCriteria(Customer customer, CustomerSearchCriteria criteria) {
		if (!criteria.hasFilters()) return true;

		if (criteria.getNameFilter() != null && !criteria.getNameFilter().isEmpty()) {
			if (customer.getName() == null || !customer.getName().toLowerCase().contains(criteria.getNameFilter().toLowerCase())) return false;
		}

		if (criteria.getCreateDateFrom() != null && !criteria.getCreateDateFrom().isEmpty()) {
			if (customer.getCreateDate() == null || customer.getCreateDate().compareTo(criteria.getCreateDateFrom()) < 0) return false;
		}

		if (criteria.getCreateDateTo() != null && !criteria.getCreateDateTo().isEmpty()) {
			if (customer.getCreateDate() == null || customer.getCreateDate().compareTo(criteria.getCreateDateTo()) > 0) return false;
		}

		if (criteria.getMinBalance() != null) {
			if (customer.getBalance() < criteria.getMinBalance()) return false;
		}

		if (criteria.getMaxBalance() != null) {
			if (customer.getBalance() > criteria.getMaxBalance()) return false;
		}

		if (criteria.getActive() != null) {
			if (customer.isActive() != criteria.getActive()) return false;
		}

		if (criteria.getAddressFilter() != null && !criteria.getAddressFilter().isEmpty()) {
			if (customer.getAddress() == null || !customer.getAddress().toLowerCase().contains(criteria.getAddressFilter().toLowerCase())) return false;
		}

		return true;
	}
}
