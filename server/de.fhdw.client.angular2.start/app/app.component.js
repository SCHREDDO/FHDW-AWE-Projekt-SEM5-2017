"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
Object.defineProperty(exports, "__esModule", { value: true });
var core_1 = require("@angular/core");
var data_service_1 = require("./data.service");
var AppComponent = (function () {
    function AppComponent(dataService) {
        var _this = this;
        this.dataService = dataService;
        this.accountList = [];
        this.transactionList = [];
        this.accountData = { owner: "", number: "" };
        this.transactionData = { sender: { owner: "", number: "" }, receiver: { owner: "", number: "" }, amount: 0.00, reference: "", transactionDate: "" };
        this.newAccountOwner = "-";
        this.newAccountstartAmount = "-";
        this.listAccount = { accounts: [] };
        this.listTransaction = { transactions: [] };
        this.dataService.getAccouts().subscribe(function (listAccount) { return _this.listAccount = listAccount; }, function (error) { return console.log("Error: " + error.statusText); }, function () { return console.log("GetRequest sent"); });
        this.accountList = this.listAccount.accounts;
        this.dataService.getTransactions("0").subscribe(function (listTransaction) { return _this.listTransaction = listTransaction; }, function (error) { return console.log("Error: " + error.statusText); }, function () { return console.log("GetRequest sent"); });
        this.transactionList = this.listTransaction.transactions;
    }
    AppComponent.prototype.newAccout = function () {
        var _this = this;
        this.dataService.sendNewAccount(this.newAccountOwner, this.newAccountstartAmount);
        this.dataService.getAccouts().subscribe(function (listAccount) { return _this.listAccount = listAccount; }, function (error) { return console.log("Error: " + error.statusText); }, function () { return console.log("GetRequest sent"); });
        //this.accountList = this.listAccount.accounts;
        this.dataService.getTransactions("0").subscribe(function (listTransaction) { return _this.listTransaction = listTransaction; }, function (error) { return console.log("Error: " + error.statusText); }, function () { return console.log("GetRequest sent"); });
        //this.transactionList = this.listTransaction.transactions;
    };
    AppComponent.prototype.updateAccount = function () {
        this.dataService.sendUpdateAccout(this.accountData.number, this.accountData.owner);
    };
    AppComponent.prototype.selectAccount = function (accoount) {
        var _this = this;
        this.accountData = accoount;
        this.dataService.getTransactions(accoount.number).subscribe(function (listTransaction) { return _this.listTransaction = listTransaction; }, function (error) { return console.log("Error: " + error.statusText); }, function () { return console.log("GetRequest sent"); });
        //this.transactionList = this.listTransaction.transactions;
    };
    AppComponent.prototype.selectTransation = function (transaction) {
        this.transactionData = transaction;
    };
    AppComponent = __decorate([
        core_1.Component({
            templateUrl: 'app/app.template.html',
            selector: 'my-app',
            providers: [data_service_1.DataService]
        }),
        __metadata("design:paramtypes", [data_service_1.DataService])
    ], AppComponent);
    return AppComponent;
}());
exports.AppComponent = AppComponent;
//# sourceMappingURL=app.component.js.map