package kalbefamily.crm.kalbe.kalbefamily.BL;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral;

import java.util.List;

import kalbefamily.crm.kalbe.kalbefamily.Common.clsToken;
import kalbefamily.crm.kalbe.kalbefamily.NewMemberActivity;
import kalbefamily.crm.kalbe.kalbefamily.PickAccountActivity;

import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_ARRAY_ACCOUNT_AVAILABLE;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_ARRAY_ACCOUNT_NAME;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_ARRAY_DATA_TOKEN;


/**
 * Created by Dewi Oktaviani on 1/22/2018.
 */

public class AuthenticatorUtil {
    String[] accounts;
    List<clsToken> dataToken;
    /**
     * Add new account to the account manager
     * @param accountType
     * @param authTokenType
     */
    public void addNewAccount(final Activity context, final AccountManager mAccountManager, final String accountType, final String authTokenType) {

        Bundle bundles = new Bundle();
        bundles.putString(ARG_ARRAY_DATA_TOKEN, "Add account inside apps");

        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, bundles, context, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    boolean newAccount = bnd.getBoolean(AccountGeneral.ARG_IS_ADDING_NEW_ACCOUNT, false);
                    final Account availableAccounts[] = countingAccount(mAccountManager);

                    Intent intent = new Intent(context, NewMemberActivity.class);
                    intent.putExtra(AccountGeneral.ARG_ACCOUNT_TYPE, accountType);
                    intent.putExtra(AccountGeneral.ARG_AUTH_TYPE, authTokenType);
                    intent.putExtra(AccountGeneral.ARG_IS_ADDING_NEW_ACCOUNT, newAccount);
                    intent.putExtra(ARG_ARRAY_ACCOUNT_AVAILABLE, availableAccounts);

                    context.finish();
                    context.startActivity(intent);
////                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    /**
     * Show all the accounts registered on the account manager. Request an auth token upon user select.
     * @param authTokenType
     */
    public void showAccountPicker(final Activity context, final AccountManager mAccountManager, final String authTokenType) {
        final Account availableAccounts[] = countingAccount(mAccountManager);
        if (availableAccounts.length == 0) {
            addNewAccount(context, mAccountManager, AccountGeneral.ACCOUNT_TYPE, authTokenType);
//            Toast.makeText(this, "No accounts", Toast.LENGTH_SHORT).show();
        } else {
            String name[] = new String[availableAccounts.length];
            for (int i = 0; i < availableAccounts.length; i++) {
                name[i] = availableAccounts[i].name;
            }

            Intent myIntent = new Intent(context, PickAccountActivity.class);
            myIntent.putExtra(ARG_ARRAY_ACCOUNT_AVAILABLE, availableAccounts);
            myIntent.putExtra(ARG_ARRAY_ACCOUNT_NAME, name);
            context.finish();
            context.startActivity(myIntent);
        }
    }

    public Account[] countingAccount(final AccountManager mAccountManager){
        final Account availableAccounts[] = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
        return availableAccounts;
    }
    /**
     * Get the auth token for an existing account on the AccountManager
     * @param account
     * @param authTokenType
     */
    public void getExistingAccountAuthToken(final Activity activity, final AccountManager mAccountManager, final Account account, final String authTokenType) {
        String userName = account.name;
        String accountType = account.type;
        final String password = mAccountManager.getPassword(account);
        accounts = new String[]{userName, password, accountType, authTokenType};
        new PickAccountActivity().userMember(accounts, activity, mAccountManager);
        Log.d("kalbe", "GetToken Bundle is ");
    }

}