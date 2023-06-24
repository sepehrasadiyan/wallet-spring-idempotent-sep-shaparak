# Spring-boot-keycloak-wallet-idempotent

This project works with Spring Boot and PostgreSQL as the database. For security, Keycloak is utilized.

I handle idempotency by injecting filters and preventing client execution until they declare the state of a bill.

For the IPG (Internet Payment Gateway), I use Pardakht Sepehr (SEP), which is an Iranian IPG.

If anyone else encounters an error in the code, I would appreciate it if they could fix it accordingly.
Thank you for clarifying your intention to address and correct any potential issues.
## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)

## Overview

The application is a wallet system that leverages the power of Spring Boot,
PostgreSQL as the database, Keycloak for security, and implements idempotency handling.
With this application, users can create bills and make payments using an Internet Payment Gateway (IPG).
The payment amount will be deducted from the user's wallet balance after a successful transaction.
ALL users unique key is bid that already defined in keyclock.
## Prerequisites
Implemented keyclock server that have REALM SEPEHR, and client Scope api-wallet.

## Usage
1- Bill Creation: Users can create bills, specifying the amount to be charged and the associated details.
2- IPG Payment: Users initiate payment using the integrated Internet Payment Gateway, securely completing the transaction.
3- Wallet Deduction: Upon successful payment, the wallet balance is updated, deducting the payment amount.
4- Transaction History(registry entity in application): Users can view their transaction history, including details of bills, payments, and wallet deductions.
## ENV Variable

| Name           |
|----------------|
| DB_URL         |
| KC_URL         |
| KC_SECRET      |
|SEP_VERIFY_TRANSACTION|
|SEP_TERMINAL_ID|
|SEP_SHAPARAK_ADDRESS|
|SEP_REVERSE_TRANSACTION|
|SEP_REDIRECT_UR|



