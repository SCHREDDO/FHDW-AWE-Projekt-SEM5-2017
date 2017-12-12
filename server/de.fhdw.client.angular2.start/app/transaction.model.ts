interface Transaction {
    sender: Account;
    receiver: Account;
    amount: string;
    reference: string;
    transactionDate: string;
}
