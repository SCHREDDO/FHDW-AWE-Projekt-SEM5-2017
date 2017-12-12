import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import "rxjs/add/operator/map";

@Injectable()
export class DataService {
    constructor(private http: Http) {}

    getAccouts() {
      return this.http.get('http://localhost:9998/rest/account/all').map(
          (response: Response) => response.json()
      );
    }

    getTransactions(number: string) {
      return this.http.get('http://localhost:9998/rest/transaction/all/' + number).map(
          (response: Response) => response.json()
      );
    }

    sendNewAccount(newAccountOwner: string, newAccountstartAmount: string) {
      let headers = new Headers();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      this.http.post('http://localhost:9998/rest/account/new', `ownerStartAmount=${newAccountOwner + "-" + newAccountstartAmount}`
      , {headers : headers})
      .subscribe(
          (response: Response) => console.log("OK"),
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("PostRequest sent")
      );
    }

    sendUpdateAccout(number: string, newOwner: string) {
      let headers = new Headers();
      headers.append('Content-Type', 'application/x-www-form-urlencoded');
      this.http.post('http://localhost:9998/rest/account/update', `newOwnerNumber=${newOwner + "-" + number}`
      , {headers : headers})
      .subscribe(
          (response: Response) => console.log("OK"),
          (error: Response) => console.log("Error: " + error.statusText),
          () => console.log("PostRequest sent")
      );
    }
}
