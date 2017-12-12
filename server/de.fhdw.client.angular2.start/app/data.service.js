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
var http_1 = require("@angular/http");
require("rxjs/add/operator/map");
var DataService = (function () {
    function DataService(http) {
        this.http = http;
    }
    DataService.prototype.getAccouts = function () {
        return this.http.get('http://localhost:9998/rest/account/all').map(function (response) { return response.json(); });
    };
    DataService.prototype.getTransactions = function (number) {
        return this.http.get('http://localhost:9998/rest/transaction/all/' + number).map(function (response) { return response.json(); });
    };
    DataService.prototype.sendNewAccount = function (newAccountOwner, newAccountstartAmount) {
        var headers = new http_1.Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.http.post('http://localhost:9998/rest/account/new', "ownerStartAmount=" + (newAccountOwner + "-" + newAccountstartAmount), { headers: headers })
            .subscribe(function (response) { return console.log("OK"); }, function (error) { return console.log("Error: " + error.statusText); }, function () { return console.log("PostRequest sent"); });
    };
    DataService.prototype.sendUpdateAccout = function (number, newOwner) {
        var headers = new http_1.Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.http.post('http://localhost:9998/rest/account/update', "newOwnerNumber=" + (newOwner + "-" + number), { headers: headers })
            .subscribe(function (response) { return console.log("OK"); }, function (error) { return console.log("Error: " + error.statusText); }, function () { return console.log("PostRequest sent"); });
    };
    DataService = __decorate([
        core_1.Injectable(),
        __metadata("design:paramtypes", [http_1.Http])
    ], DataService);
    return DataService;
}());
exports.DataService = DataService;
//# sourceMappingURL=data.service.js.map