
#ifndef CALC_ICE
#define CALC_ICE

module BankClient
{
  enum AccountType { STANDARD , PREMIUM};
  enum CurrencyType {USD = 0,EUR = 1,PLN = 2,CHF = 3};

  class CreditInfo
  {
  CurrencyType baseCurrency;
  double baseCost;
  CurrencyType foreignCurrency;
  double foreignCost;
  };

   class Date
   {
    short day;
    short month;
    short year;
   };

  exception NotAuthrorizedException{
  string reason;
  };
  exception NoIncomeException{};
  exception DateRangeError{
  string reason;
  };



  interface Account
  {
  double accountBalance();
  void deposit(double value);
  };

  interface PremiumAccount extends Account
  {
        CreditInfo applyForCredit(double value, CurrencyType currency, Date from, Date to ) throws NotAuthrorizedException,DateRangeError;
  };
  interface AccountFactory
    {
    Account* create(string firstName, string lastName, string pesel, double income) throws NoIncomeException;
    };



};

#endif
