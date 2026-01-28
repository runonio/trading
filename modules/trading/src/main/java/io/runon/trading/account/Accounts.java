package io.runon.trading.account;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 게좌들
 * @author macle
 */
public class Accounts implements Account{

    protected final Map<String, Account> accountMap = new HashMap<>();

    protected final Object lock = new Object();

    private final String id;
    public Accounts(String id){
        this.id = id;
    }

    public void add(Account account){
        synchronized (lock){
            accountMap.put(account.getId(), account);
        }
    }

    public void remove(Account account){
        remove(account.getId());
    }

    public void remove(String id){
        synchronized (lock){
            accountMap.remove(id);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public BigDecimal getAssets() {
        synchronized (lock) {
            BigDecimal assets = BigDecimal.ZERO;

            Collection<Account> accounts = accountMap.values();
            for(Account account : accounts){
                assets = assets.add(account.getAssets());
            }

            return assets;
        }
    }

    @Override
    public BigDecimal getCash(int nextDay) {
        synchronized (lock){
            BigDecimal cash = BigDecimal.ZERO;
            Collection<Account> accounts = accountMap.values();
            for(Account account : accounts){
                cash = cash.add(account.getCash(nextDay));
            }
            return cash;
        }

    }


}
