//Author: Sebastian Lühnen
interface Transaction {
    sender: Account;
    receiver: Account;
    amount: number;
    reference: string;
    transactionDate: string;
}
