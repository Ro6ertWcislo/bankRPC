import Ice
import sys

Ice.loadSlice('slice/bankClient.ice')
import BankClient

currencies = {
    'CHF': BankClient.CurrencyType.CHF,
    'USD': BankClient.CurrencyType.USD,
    'EUR': BankClient.CurrencyType.EUR,
    'PLN': BankClient.CurrencyType.PLN
}

with Ice.initialize(sys.argv) as communicator:
    bankName = input("type your bank name")
    port = input("type bank port")
    base = communicator.stringToProxy("bank/{0}:tcp -h localhost -p {1}:udp -h localhost -p {1}".format(bankName,port))
    factory = BankClient.AccountFactoryPrx.checkedCast(base)
    if not factory:
        raise RuntimeError("Invalid proxy")
    c_input = input("type create to create new account or anything to log in")
    account = None
    if c_input == 'create':
        c_input = input("firstName lastName pesel monthly_income")
        f, l, p, i = c_input.split(' ')
        i = float(i)
        account = factory.create(f, l, p, i)
    else:
        pesel = input("gimmie you pesel")
        obj = communicator.stringToProxy("account/" + pesel + ":tcp -h localhost -p "+port+":udp -h localhost -p "+port)
        account = BankClient.AccountPrx.checkedCast(obj)

    while c_input != 'end':
        try:
            c_input = input("create/log/credit/balance/deposit")
            if c_input == 'create':
                c_input = input("firstName lastName pesel monthly_income")
                f, l, p, i = c_input.split(' ')
                i = float(i)
                account = factory.create(f, l, p, i)
            elif c_input == 'log':
                pesel = input("gimmie you pesel")
                obj = communicator.stringToProxy(
                    "account/" + pesel + ":tcp -h localhost -p 10000:udp -h localhost -p 10000")
                account = BankClient.AccountPrx.checkedCast(obj)
            elif c_input == 'balance':
                print("you have " + str(account.accountBalance()))
            elif c_input == 'deposit':
                val = float(input('how much you want to pay?'))
                account.deposit(val)
                print('money are on your account. Your balance is now ' + str(account.accountBalance()))
            elif c_input == 'credit':

                premium_account = BankClient.PremiumAccountPrx.uncheckedCast(account)

                val, currency, date_from, date_to = input("value currency from to\n").split(' ')
                val = float(val)
                currency = currencies[currency]
                y, m, d = date_from.split('-')
                date_from = BankClient.Date(int(d), int(m), int(y))

                y, m, d = date_to.split('-')
                date_to = BankClient.Date(int(d), int(m), int(y))

                ret = premium_account.applyForCredit(val, currency, date_from, date_to)

                print(ret)
        except Ice.OperationNotExistException:
            print("sorry, you're not allowed to take credit. You do not have a premium account")
        except BankClient.DateRangeError:
            print("you passed incorrect date. Try again")
        except ValueError:
            print("you passed wrong parameters. Try again")
        except:
            print("Unexpected error:", sys.exc_info()[0])
