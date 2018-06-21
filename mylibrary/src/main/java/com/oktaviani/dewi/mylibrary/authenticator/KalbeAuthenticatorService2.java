package com.oktaviani.dewi.mylibrary.authenticator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dewi Oktaviani on 1/22/2018.
 */

public class KalbeAuthenticatorService2 extends Service {
    private AccountManager _accountManager;
    private Account[] _currentAccount;

    private OnAccountsUpdateListener _onAccountUpdateListener = new OnAccountsUpdateListener() {
        @Override
        public void onAccountsUpdated(Account[] accounts) {

            if (_currentAccount == null) {
                _currentAccount = accounts;
                return;
            }


            ArrayList<Account> accountKalbe = null;
            ArrayList<Account> accountKalbeDelete = null;
//            if (dataLogin.size()>0){
//                accountKalbe = new ArrayList<>();
//                for (Account account : _currentAccount){
//                    if (account.type.equals(AccountGeneral.ACCOUNT_TYPE)&&dataLogin.get(0).getTxtUsername().equals(account.name)){
//                        accountKalbe.add(account);
//                    }
//                }
//
//                accountKalbeDelete = new ArrayList<>();
//                for (Account account : accounts){
//                    if (account.type.equals(AccountGeneral.ACCOUNT_TYPE)&&dataLogin.get(0).getTxtUsername().equals(account.name)){
//                        accountKalbeDelete.add(account);
//                    }
//                }
//
//                for (Account currentAccount : accountKalbe) {
//                    boolean accountExist = false;
//                    for (Account account : accountKalbeDelete) {
//                        if (account.name.equals(currentAccount.name)) {
//                            accountExist = true;
//                            break;
//                        }
//                    }
//                    if (!accountExist) {
//                        logout();
//                    }
//                }
//            } else {
//                accountKalbe = new ArrayList<>();
//                for (Account account : _currentAccount){
//                    if (account.type.equals(AccountGeneral.ACCOUNT_TYPE)){
//                        accountKalbe.add(account);
//                    }
//                }
//
//                accountKalbeDelete = new ArrayList<>();
//                for (Account account : accounts){
//                    if (account.type.equals(AccountGeneral.ACCOUNT_TYPE)){
//                        accountKalbeDelete.add(account);
//                    }
//                }
//
//                for (Account currentAccount : accountKalbe) {
//                    boolean accountExist = false;
//                    for (Account account : accountKalbeDelete) {
//                        if (account.name.equals(currentAccount.name)) {
//                            accountExist = true;
//                            break;
//                        }
//                    }
//                    if (!accountExist) {
//                        logouts();
//                    }
//                }
//            }
        }
    };

//    private void logout() {
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
//        helper.clearDataAfterLogout();
//        startActivity(intent);
//    }

//    private void logouts() {
//        Intent intent = new Intent(this, SplashActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        _accountManager = AccountManager.get(this);
////        DatabaseManager.init(this);
////        _accountManager.addOnAccountsUpdatedListener(_onAccountUpdateListener, new Handler(), true);
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        Intent broadcastIntent = new Intent("com.oktaviani.dewi.myapplication.RestartSensor");
////        sendBroadcast(broadcastIntent);
////        _accountManager.removeOnAccountsUpdatedListener(_onAccountUpdateListener);
//    }

    @Override
    public IBinder onBind(Intent intent) {
        KalbeAuthenticator2 authenticator = new KalbeAuthenticator2(this);
        return authenticator.getIBinder();
    }

}
