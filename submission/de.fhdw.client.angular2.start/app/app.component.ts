//Author: Sebastian Lühnen
import {Component} from '@angular/core';
import {DataService} from "./data.service";

@Component({
    templateUrl: 'app/app.template.html',
    selector: 'my-app',
    providers: [DataService]
})

export class AppComponent {
    accountList: Account[] = [];
    transactionList: Transaction[] = [];
    accountData: Account = {owner: "", number: ""};
    transactionData: Transaction = {sender: {owner: "", number: ""}, receiver: {owner: "", number: ""}, amount: 0.00, reference: "", transactionDate: ""};
    newAccountOwner: string = "-";
    newAccountstartAmount: string = "-";

    listAccount: ListAccount = {accounts: []};
    listTransaction: ListTransaction = {transactions: []};

    constructor(private dataService : DataService) {
      this.dataService.getAccouts().subscribe(
          (listAccount: ListAccount) => this.listAccount = listAccount,
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );
      this.accountList = this.listAccount.accounts;

      this.dataService.getTransactions("0").subscribe(
          (listTransaction: ListTransaction) => this.listTransaction = listTransaction,
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );
      this.transactionList = this.listTransaction.transactions;
    }

    newAccout() {
      this.dataService.sendNewAccount(this.newAccountOwner, this.newAccountstartAmount);

      this.dataService.getAccouts().subscribe(
          (listAccount: ListAccount) => this.listAccount = listAccount,
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );
      //this.accountList = this.listAccount.accounts;

      this.dataService.getTransactions("0").subscribe(
          (listTransaction: ListTransaction) => this.listTransaction = listTransaction,
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );
      //this.transactionList = this.listTransaction.transactions;

      this.accountData.number = "1000";
      this.accountData.owner = this.newAccountOwner;
    }

    updateAccount() {
      this.dataService.sendUpdateAccout(this.accountData.number, this.accountData.owner);
    }

    selectAccount(accoount: Account) {
      this.accountData = accoount;

      this.dataService.getTransactions(accoount.number).subscribe(
          (listTransaction: ListTransaction) => this.listTransaction = listTransaction,
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );
      //this.transactionList = this.listTransaction.transactions;
    }

    selectTransation(transaction: Transaction) {
      this.transactionData = transaction;
    }
}
