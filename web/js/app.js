// ABA Banking System - JavaScript Application

const API_BASE = 'http://localhost:8080/api';

// ========== State Management ==========
let currentCustomer = null;
let currentAdminTab = 'account';
let confirmCallback = null;

// ========== Screen Navigation ==========
function showScreen(screenId) {
    document.querySelectorAll('.screen').forEach(screen => {
        screen.classList.remove('active');
    });
    document.getElementById(screenId).classList.add('active');
}

// ========== Modal Management ==========
function showModal(modalId) {
    document.getElementById(modalId).classList.add('active');
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

function showAlert(title, message, type = 'info') {
    const modal = document.getElementById('alert-modal');
    const titleEl = document.getElementById('alert-title');
    const messageEl = document.getElementById('alert-message');
    
    titleEl.textContent = title;
    messageEl.textContent = message;
    
    // Remove existing alert type classes
    modal.querySelector('.modal-content').classList.remove('alert-success', 'alert-error', 'alert-warning');
    
    if (type === 'success') {
        modal.querySelector('.modal-content').classList.add('alert-success');
    } else if (type === 'error') {
        modal.querySelector('.modal-content').classList.add('alert-error');
    } else if (type === 'warning') {
        modal.querySelector('.modal-content').classList.add('alert-warning');
    }
    
    showModal('alert-modal');
}

function showConfirm(title, message, callback) {
    document.getElementById('confirm-title').textContent = title;
    document.getElementById('confirm-message').textContent = message;
    confirmCallback = callback;
    showModal('confirm-modal');
}

function confirmAction() {
    closeModal('confirm-modal');
    if (confirmCallback) {
        confirmCallback();
        confirmCallback = null;
    }
}

// ========== API Helpers ==========
async function apiCall(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        },
    };
    
    if (body) {
        options.body = JSON.stringify(body);
    }
    
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, options);
        const data = await response.json();
        
        if (!response.ok) {
            throw new Error(data.message || 'Request failed');
        }
        
        return data;
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// ========== Customer Authentication ==========
async function customerLogin() {
    const accNo = document.getElementById('customer-accno').value.trim();
    const password = document.getElementById('customer-password').value;
    
    if (!accNo || !password) {
        showAlert('Warning', 'Please enter both account number and password.', 'warning');
        return;
    }
    
    try {
        const response = await apiCall('/customer/login', 'POST', { accNo, password });
        
        if (response.success) {
            currentCustomer = response.customer;
            document.getElementById('customer-accno').value = '';
            document.getElementById('customer-password').value = '';
            showScreen('customer-dashboard');
            updateCustomerDashboard();
        } else {
            showAlert('Error', 'Invalid account number or password.', 'error');
        }
    } catch (error) {
        showAlert('Error', 'Login failed. Please try again.', 'error');
    }
}

function customerLogout() {
    showConfirm('Logout', 'Are you sure you want to log out?', () => {
        currentCustomer = null;
        showScreen('role-selection');
    });
}

// ========== Admin Authentication ==========
async function adminLogin() {
    const username = document.getElementById('admin-username').value.trim();
    const password = document.getElementById('admin-password').value;
    
    if (!username || !password) {
        showAlert('Warning', 'Please enter both username and password.', 'warning');
        return;
    }
    
    try {
        const response = await apiCall('/admin/login', 'POST', { username, password });
        
        if (response.success) {
            document.getElementById('admin-username').value = '';
            document.getElementById('admin-password').value = '';
            showScreen('admin-dashboard');
            loadAccountList();
        } else {
            showAlert('Error', 'Invalid username or password.', 'error');
        }
    } catch (error) {
        showAlert('Error', 'Login failed. Please try again.', 'error');
    }
}

function adminLogout() {
    showConfirm('Logout', 'Are you sure you want to log out?', () => {
        showScreen('role-selection');
    });
}

// ========== Customer Dashboard ==========
async function updateCustomerDashboard() {
    if (!currentCustomer) return;
    
    // Update balance
    const balanceEl = document.getElementById('customer-balance');
    balanceEl.textContent = formatCurrency(currentCustomer.balance);
    
    // Load transactions
    await loadCustomerTransactions();
}

async function loadCustomerTransactions() {
    if (!currentCustomer) return;
    
    try {
        const transactions = await apiCall(`/transactions/customer/${currentCustomer.id}`);
        const container = document.getElementById('customer-transactions');
        container.innerHTML = '';
        
        if (transactions.length === 0) {
            container.innerHTML = '<p class="no-data">No transactions found</p>';
            return;
        }
        
        transactions.forEach(t => {
            const card = createCustomerTransactionCard(t);
            container.appendChild(card);
        });
    } catch (error) {
        console.error('Failed to load transactions:', error);
    }
}

function createCustomerTransactionCard(transaction) {
    const card = document.createElement('div');
    card.className = 'transaction-card';
    
    const isCredit = transaction.receiverId === currentCustomer.id && transaction.type !== 'WITHDRAWAL';
    const amountClass = isCredit ? 'credit' : 'debit';
    const amountPrefix = isCredit ? '+' : '-';
    
    card.innerHTML = `
        <div class="transaction-info">
            <div class="transaction-type">${transaction.type}</div>
            <div class="transaction-id">${transaction.transactionId}</div>
        </div>
        <div>
            <div class="transaction-amount ${amountClass}">${amountPrefix}${formatCurrency(transaction.amount)}</div>
            <div class="transaction-date">${transaction.timestamp}</div>
        </div>
    `;
    
    return card;
}

// ========== Customer Account Detail ==========
function showAccountDetail() {
    if (!currentCustomer) return;
    
    const content = document.getElementById('account-detail-content');
    content.innerHTML = `
        <table class="detail-table">
            <tr><td>Account Number</td><td>${currentCustomer.accNo}</td></tr>
            <tr><td>Customer ID</td><td>${currentCustomer.id}</td></tr>
            <tr><td>Name</td><td>${currentCustomer.name}</td></tr>
            <tr><td>Phone Number</td><td>${currentCustomer.phoneNumber}</td></tr>
            <tr><td>Address</td><td>${currentCustomer.address}</td></tr>
            <tr><td>Birth Date</td><td>${currentCustomer.birthDate}</td></tr>
            <tr><td>Account Created</td><td>${currentCustomer.createDate}</td></tr>
            <tr><td>Balance</td><td>${formatCurrency(currentCustomer.balance)}</td></tr>
            <tr><td>Status</td><td>${currentCustomer.active ? 'Active' : 'Inactive'}</td></tr>
        </table>
    `;
    
    showModal('account-detail-modal');
}

// ========== Withdraw ==========
function showWithdrawModal() {
    document.getElementById('withdraw-amount').value = '';
    showModal('withdraw-modal');
}

async function processWithdraw() {
    const amount = parseFloat(document.getElementById('withdraw-amount').value);
    
    if (!amount || amount <= 0) {
        showAlert('Warning', 'Please enter a valid amount.', 'warning');
        return;
    }
    
    if (amount > currentCustomer.balance) {
        showAlert('Error', 'Insufficient funds.', 'error');
        return;
    }
    
    try {
        const response = await apiCall('/transactions/withdraw', 'POST', {
            customerId: currentCustomer.id,
            amount
        });
        
        if (response.success) {
            currentCustomer.balance = response.newBalance;
            closeModal('withdraw-modal');
            updateCustomerDashboard();
            showAlert('Success', `Successfully withdrew ${formatCurrency(amount)}\nNew balance: ${formatCurrency(currentCustomer.balance)}`, 'success');
        } else {
            showAlert('Error', response.message || 'Withdrawal failed.', 'error');
        }
    } catch (error) {
        showAlert('Error', 'Withdrawal failed. Please try again.', 'error');
    }
}

// ========== Deposit ==========
function showDepositModal() {
    document.getElementById('deposit-amount').value = '';
    showModal('deposit-modal');
}

async function processDeposit() {
    const amount = parseFloat(document.getElementById('deposit-amount').value);
    
    if (!amount || amount <= 0) {
        showAlert('Warning', 'Please enter a valid amount.', 'warning');
        return;
    }
    
    try {
        const response = await apiCall('/transactions/deposit', 'POST', {
            customerId: currentCustomer.id,
            amount
        });
        
        if (response.success) {
            currentCustomer.balance = response.newBalance;
            closeModal('deposit-modal');
            updateCustomerDashboard();
            showAlert('Success', `Successfully deposited ${formatCurrency(amount)}\nNew balance: ${formatCurrency(currentCustomer.balance)}`, 'success');
        } else {
            showAlert('Error', response.message || 'Deposit failed.', 'error');
        }
    } catch (error) {
        showAlert('Error', 'Deposit failed. Please try again.', 'error');
    }
}

// ========== Transfer ==========
function showTransferModal() {
    document.getElementById('transfer-recipient').value = '';
    document.getElementById('transfer-amount').value = '';
    showModal('transfer-modal');
}

async function processTransfer() {
    const recipientAccNo = document.getElementById('transfer-recipient').value.trim();
    const amount = parseFloat(document.getElementById('transfer-amount').value);
    
    if (!recipientAccNo || !amount || amount <= 0) {
        showAlert('Warning', 'Please enter recipient account and a valid amount.', 'warning');
        return;
    }
    
    if (amount > currentCustomer.balance) {
        showAlert('Error', 'Insufficient funds.', 'error');
        return;
    }
    
    try {
        // First verify recipient exists
        const recipientResponse = await apiCall(`/customer/find/${recipientAccNo}`);
        
        if (!recipientResponse.success) {
            showAlert('Error', 'Recipient account not found.', 'error');
            return;
        }
        
        const recipient = recipientResponse.customer;
        
        closeModal('transfer-modal');
        showConfirm('Confirm Transfer', 
            `Transfer ${formatCurrency(amount)} to ${recipient.name} (${recipient.accNo})?`,
            async () => {
                try {
                    const response = await apiCall('/transactions/transfer', 'POST', {
                        senderId: currentCustomer.id,
                        receiverAccNo: recipientAccNo,
                        amount
                    });
                    
                    if (response.success) {
                        currentCustomer.balance = response.newBalance;
                        updateCustomerDashboard();
                        showAlert('Success', `Successfully transferred ${formatCurrency(amount)} to ${recipient.name}\nNew balance: ${formatCurrency(currentCustomer.balance)}`, 'success');
                    } else {
                        showAlert('Error', response.message || 'Transfer failed.', 'error');
                    }
                } catch (error) {
                    showAlert('Error', 'Transfer failed. Please try again.', 'error');
                }
            }
        );
    } catch (error) {
        showAlert('Error', 'Failed to verify recipient account.', 'error');
    }
}

// ========== Deactivate ==========
function showDeactivateConfirm() {
    showConfirm('Deactivate Account', 
        'Are you sure you want to deactivate your account?\nThis action cannot be undone.',
        () => {
            showAlert('Info', 'Account deactivation request submitted.\nPlease contact customer service.');
        }
    );
}

// ========== Customer Report ==========
async function showCustomerReport() {
    if (!currentCustomer) return;
    
    try {
        const transactions = await apiCall(`/transactions/customer/${currentCustomer.id}`);
        
        let reportHtml = `
            <h4 style="color: var(--text-color); margin-bottom: 15px;">Account: ${currentCustomer.name} (${currentCustomer.accNo})</h4>
            <table class="report-table">
                <thead>
                    <tr>
                        <th>Transaction ID</th>
                        <th>Type</th>
                        <th>Amount</th>
                        <th>Date</th>
                    </tr>
                </thead>
                <tbody>
        `;
        
        if (transactions.length === 0) {
            reportHtml += '<tr><td colspan="4" style="text-align: center;">No transactions found</td></tr>';
        } else {
            transactions.forEach(t => {
                reportHtml += `
                    <tr>
                        <td>${t.transactionId}</td>
                        <td>${t.type}</td>
                        <td>${formatCurrency(t.amount)}</td>
                        <td>${t.timestamp}</td>
                    </tr>
                `;
            });
        }
        
        reportHtml += '</tbody></table>';
        
        document.getElementById('report-content').innerHTML = reportHtml;
        showModal('report-modal');
    } catch (error) {
        showAlert('Error', 'Failed to load report.', 'error');
    }
}

// ========== Admin Dashboard ==========
function switchAdminTab(tab) {
    currentAdminTab = tab;
    
    // Update tab buttons
    document.getElementById('account-tab').classList.toggle('active', tab === 'account');
    document.getElementById('transaction-tab').classList.toggle('active', tab === 'transaction');
    
    // Update content panels
    document.getElementById('account-list').classList.toggle('active', tab === 'account');
    document.getElementById('transaction-list').classList.toggle('active', tab === 'transaction');
    
    // Update sidebar menu
    const createAccountBtn = document.getElementById('create-account-btn');
    if (tab === 'account') {
        createAccountBtn.style.display = 'block';
        loadAccountList();
    } else {
        createAccountBtn.style.display = 'none';
        loadTransactionList();
    }
}

async function loadAccountList(searchTerm = null) {
    try {
        let accounts;
        if (searchTerm) {
            accounts = await apiCall(`/customer/search?term=${encodeURIComponent(searchTerm)}`);
        } else {
            accounts = await apiCall('/customer/all');
        }
        
        const container = document.getElementById('account-list');
        container.innerHTML = '';
        
        if (accounts.length === 0) {
            container.innerHTML = `<p class="no-data">${searchTerm ? `No accounts found matching: ${searchTerm}` : 'No accounts found'}</p>`;
            return;
        }
        
        accounts.forEach(account => {
            const card = createAccountCard(account);
            container.appendChild(card);
        });
    } catch (error) {
        console.error('Failed to load accounts:', error);
    }
}

function createAccountCard(account) {
    const card = document.createElement('div');
    card.className = 'account-card';
    card.onclick = () => showAccountDetailAdmin(account);
    
    card.innerHTML = `
        <div class="account-info">
            <div class="account-name">${account.name}</div>
            <div class="account-number">${account.accNo}</div>
        </div>
        <div class="account-balance">${formatCurrency(account.balance)}</div>
    `;
    
    return card;
}

function showAccountDetailAdmin(account) {
    const content = document.getElementById('account-detail-content');
    content.innerHTML = `
        <table class="detail-table">
            <tr><td>Account Number</td><td>${account.accNo}</td></tr>
            <tr><td>Customer ID</td><td>${account.id}</td></tr>
            <tr><td>Name</td><td>${account.name}</td></tr>
            <tr><td>Phone Number</td><td>${account.phoneNumber}</td></tr>
            <tr><td>Address</td><td>${account.address}</td></tr>
            <tr><td>Birth Date</td><td>${account.birthDate}</td></tr>
            <tr><td>Account Created</td><td>${account.createDate}</td></tr>
            <tr><td>Balance</td><td>${formatCurrency(account.balance)}</td></tr>
            <tr><td>Status</td><td>${account.active ? 'Active' : 'Inactive'}</td></tr>
        </table>
    `;
    
    showModal('account-detail-modal');
}

async function loadTransactionList(searchTerm = null) {
    try {
        let transactions;
        if (searchTerm) {
            transactions = await apiCall(`/transactions/search?term=${encodeURIComponent(searchTerm)}`);
        } else {
            transactions = await apiCall('/transactions/all');
        }
        
        const container = document.getElementById('transaction-list');
        container.innerHTML = '';
        
        if (transactions.length === 0) {
            container.innerHTML = `<p class="no-data">${searchTerm ? `No transactions found matching: ${searchTerm}` : 'No transactions found'}</p>`;
            return;
        }
        
        transactions.forEach(t => {
            const card = createAdminTransactionCard(t);
            container.appendChild(card);
        });
    } catch (error) {
        console.error('Failed to load transactions:', error);
    }
}

function createAdminTransactionCard(transaction) {
    const card = document.createElement('div');
    card.className = 'admin-transaction-card';
    
    card.innerHTML = `
        <div class="admin-transaction-header">
            <span class="transaction-type">${transaction.type}</span>
            <span class="transaction-amount">${formatCurrency(transaction.amount)}</span>
        </div>
        <div class="admin-transaction-row">
            <span>Transaction ID: ${transaction.transactionId}</span>
            <span>${transaction.timestamp}</span>
        </div>
        <div class="admin-transaction-row">
            <span>Sender: ${transaction.senderId}</span>
            <span>Receiver: ${transaction.receiverId}</span>
        </div>
    `;
    
    return card;
}

// ========== Admin Search ==========
function adminSearch() {
    const title = currentAdminTab === 'account' ? 'Search Account' : 'Search Transaction';
    const label = currentAdminTab === 'account' 
        ? 'Enter account number or name:' 
        : 'Enter transaction ID or user ID:';
    
    document.getElementById('search-title').textContent = title;
    document.getElementById('search-label').textContent = label;
    document.getElementById('search-input').value = '';
    
    showModal('search-modal');
}

function executeSearch() {
    const searchTerm = document.getElementById('search-input').value.trim();
    closeModal('search-modal');
    
    if (!searchTerm) {
        if (currentAdminTab === 'account') {
            loadAccountList();
        } else {
            loadTransactionList();
        }
        return;
    }
    
    if (currentAdminTab === 'account') {
        loadAccountList(searchTerm);
    } else {
        loadTransactionList(searchTerm);
    }
}

// ========== Create Account (Admin) ==========
function showCreateAccountModal() {
    // Clear form
    document.getElementById('new-name').value = '';
    document.getElementById('new-password').value = '';
    document.getElementById('new-phone').value = '';
    document.getElementById('new-address').value = '';
    document.getElementById('new-birthdate').value = '';
    document.getElementById('new-balance').value = '0';
    
    showModal('create-account-modal');
}

async function createAccount() {
    const name = document.getElementById('new-name').value.trim();
    const password = document.getElementById('new-password').value;
    const phone = document.getElementById('new-phone').value.trim();
    const address = document.getElementById('new-address').value.trim();
    const birthDate = document.getElementById('new-birthdate').value;
    const balance = parseFloat(document.getElementById('new-balance').value) || 0;
    
    if (!name || !password) {
        showAlert('Warning', 'Name and password are required.', 'warning');
        return;
    }
    
    try {
        const response = await apiCall('/customer/create', 'POST', {
            name,
            password,
            phone: parseInt(phone) || 0,
            address,
            birthDate,
            balance
        });
        
        if (response.success) {
            closeModal('create-account-modal');
            loadAccountList();
            showAlert('Success', `Account created successfully!\nAccount Number: ${response.customer.accNo}`, 'success');
        } else {
            showAlert('Error', response.message || 'Failed to create account.', 'error');
        }
    } catch (error) {
        showAlert('Error', 'Failed to create account. Please try again.', 'error');
    }
}

// ========== Utility Functions ==========
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 0,
        maximumFractionDigits: 2
    }).format(amount);
}

// ========== Event Listeners ==========
document.addEventListener('DOMContentLoaded', () => {
    // Add Enter key support for login forms
    document.getElementById('customer-password').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') customerLogin();
    });
    
    document.getElementById('admin-password').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') adminLogin();
    });
    
    document.getElementById('search-input').addEventListener('keypress', (e) => {
        if (e.key === 'Enter') executeSearch();
    });
    
    // Close modals when clicking outside
    document.querySelectorAll('.modal').forEach(modal => {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.classList.remove('active');
            }
        });
    });
});
