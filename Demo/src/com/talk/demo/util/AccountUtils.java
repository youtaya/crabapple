package com.talk.demo.util;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.talk.demo.account.AccountConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AccountUtils {
    private static final String TAG = "AccountUtils";
    
    private static class AuthenticatorConflictException extends IOException {

        private static final long serialVersionUID = 641279204734869183L;
    }
    /**
     * Get default account where password can be retrieved
     *
     * @param context
     * @return password accessible account or null if none
     */
    public static Account getPasswordAccessibleAccount(final Context context) {
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(AccountConstants.ACCOUNT_TYPE);
        if (accounts == null || accounts.length == 0)
            return null;

        try {
            accounts = getPasswordAccessibleAccounts(manager, accounts);
        } catch (AuthenticatorConflictException e) {
            return null;
        }
        return accounts != null && accounts.length > 0 ? accounts[0] : null;
    }
    
    private static Account[] getPasswordAccessibleAccounts(
            final AccountManager manager, final Account[] candidates)
            throws AuthenticatorConflictException {
        final List<Account> accessible = new ArrayList<Account>(
                candidates.length);
        boolean exceptionThrown = false;
        for (Account account : candidates)
            try {
                manager.getPassword(account);
                accessible.add(account);
            } catch (SecurityException ignored) {
                exceptionThrown = true;
            }
        if (accessible.isEmpty() && exceptionThrown)
            throw new AuthenticatorConflictException();
        return accessible.toArray(new Account[accessible.size()]);
    }

}
