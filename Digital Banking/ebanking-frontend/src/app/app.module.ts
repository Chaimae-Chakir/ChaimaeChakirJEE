import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import { AddCustomerComponent } from './components/add-customer/add-customer.component';
import { CustomerAccountsComponent } from './components/customer-accounts/customer-accounts.component';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { ManageAccountsComponent } from './components/manage-accounts/manage-accounts.component';
import { ManageCustomersComponent } from './components/manage-customers/manage-customers.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { OneAccountComponent } from './components/one-account/one-account.component';
import { UpdateCustomerComponent } from './components/update-customer/update-customer.component';

@NgModule({
  declarations: [
    AppComponent,
    AddCustomerComponent,
    CustomerAccountsComponent,
    LoginPageComponent,
    ManageAccountsComponent,
    ManageCustomersComponent,
    NavbarComponent,
    OneAccountComponent,
    UpdateCustomerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
