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
    accountData: Account = {owner: "Test 1", number: "0000"};
    transactionData: Transaction = {sender: {owner: "Test 1", number: "0000"}, receiver: {owner: "Test 2", number: "0001"}, amount: "0.00", reference: "TEST", transactionDate: "NIE"};
    newAccountOwner: string = "-";
    newAccountstartAmount: string = "-";

    constructor(private dataService : DataService) {}

    newAccout() {}

    updateAccount() {}

    selectAccount(accoount: Account) {}

    selectTransation(transaction: Transaction) {}
}
