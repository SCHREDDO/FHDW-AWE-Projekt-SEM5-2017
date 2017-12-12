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

    constructor(private dataService : DataService) {
      this.dataService.getAccouts().subscribe(
          (account: Account) => this.accountList.push(account),
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );

      this.dataService.getTransactions("all").subscribe(
          (transactions: Transaction) => this.transactionList.push(transactions),
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );
    }

    newAccout() {
      this.dataService.sendNewAccount(this.newAccountOwner, this.newAccountstartAmount);

      this.dataService.getAccouts().subscribe(
          (account: Account) => this.accountList.push(account),
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );
    }

    updateAccount() {
      this.dataService.sendUpdateAccout(this.accountData.number, this.accountData.owner);
    }

    selectAccount(accoount: Account) {
      this.accountData = accoount;

      this.dataService.getTransactions(this.accountData.number).subscribe(
          (transactions: Transaction) => this.transactionList.push(transactions),
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("GetRequest sent")
      );
    }

    selectTransation(transaction: Transaction) {
      this.transactionData = transaction;
    }
}
