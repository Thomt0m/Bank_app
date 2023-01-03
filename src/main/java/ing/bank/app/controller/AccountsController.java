package ing.bank.app.controller;

import ing.bank.app.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path="/Accounts")
public class AccountsController {


    @Autowired
    AccountRepository accountRepo;


    /*@GetMapping(path = "/GetByUserId")
    public @ResponseBody Iterable<Account> getByUserId(@PathVariable Long userId) {
        // TODO figure out if there is a way to have spring generate a matching statement for a query
        ArrayList<Account> accounts = new ArrayList<>();
        for (Account account : accountRepo.findAll())
            if (account.getOwner().getId().equals(userId))
                accounts.add(account);
        return accounts;
    }*/


}
