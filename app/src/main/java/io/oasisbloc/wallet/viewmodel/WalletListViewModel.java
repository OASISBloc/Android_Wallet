package io.oasisbloc.wallet.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.DecimalUtils;
import io.oasisbloc.wallet.data.Account;
import io.oasisbloc.wallet.data.Token;
import io.oasisbloc.wallet.model.AccountModel;
import io.oasisbloc.wallet.model.WalletModel;

public class WalletListViewModel extends BaseViewModel {

    private Context mContext;

    private MutableLiveData<String> account = new MutableLiveData<>();
    private MutableLiveData<String> balance = new MutableLiveData<>();
    private MutableLiveData<String> symbol = new MutableLiveData<>();

    private MutableLiveData<Double> cpuPercent = new MutableLiveData<>();
    private MutableLiveData<Double> netPercent = new MutableLiveData<>();
    private MutableLiveData<Double> ramPercent = new MutableLiveData<>();

    private MutableLiveData<List<Token>> tokens = new MutableLiveData<>();

    private MutableLiveData<List<Token>> balances = new MutableLiveData<>();
    private MutableLiveData<List<String>> symbols = new MutableLiveData<>();

    public static WalletListViewModel get(FragmentActivity activity, Lifecycle lifecycle) {
        WalletListViewModel vm = new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new WalletListViewModel(activity);
            }
        }).get(WalletListViewModel.class);
        lifecycle.addObserver(vm);
        return vm;
    }

    private WalletListViewModel(Context context) {
        mContext = context;
        account.setValue(AccountModel.getAccount());
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    /*
        getter
     */

    public MutableLiveData<String> getAccount() {
        return account;
    }

    public MutableLiveData<String> getBalance() {
        return balance;
    }

    public MutableLiveData<String> getSymbol() {
        return symbol;
    }

    public MutableLiveData<Double> getCpuPercent() {
        return cpuPercent;
    }

    public MutableLiveData<Double> getNetPercent() {
        return netPercent;
    }

    public MutableLiveData<Double> getRamPercent() {
        return ramPercent;
    }

    public MutableLiveData<List<Token>> getTokens() {
        return tokens;
    }

    public MutableLiveData<List<Token>> getBalances() {
        return balances;
    }

    public MutableLiveData<List<String>> getSymbols() {
        return symbols;
    }

    public String getMyTokenList(){
        return AccountModel.getMyTokenList();
    }

    public void setMyTokenList(String list){
        AccountModel.setMyTokenList(list);
    }

    public List<String> getTokenList(){
        return AccountModel.getTokenList();
    }


    /*
        action
     */

    private void fetchBalance() {
        disposable = WalletModel.getBalance().subscribe(
                result -> {

//                    JsonParser parser = new JsonParser();

                    try {
//                        JsonObject json = parser.parse(result).getAsJsonObject();

                        String mylist = AccountModel.getMyTokenList();

                        JsonParser parser = new JsonParser();
                        JsonArray arr = parser.parse(mylist).getAsJsonArray();

                        List<Token> list = new ArrayList<>();

                        for(int i = 0; i < arr.size(); i++){
                              String name = arr.get(i).getAsString();

                              if(name.equals("OSB") == true){
                                  String osbBalance = result.get("osbBalance").getAsString();
                                  Token osbToken = new Token("OSB", Double.parseDouble(osbBalance));

                                  list.add(osbToken);
                              }else if(name.equals("SOSB") == true){
                                  String sOsbBalance = result.get("sOsbBalance").getAsString();
                                  Token sOsbToken = new Token("SOSB", Double.parseDouble(sOsbBalance));

                                  list.add(sOsbToken);
                              }else if(name.equals("CRA") == true){
                                  String craBalance = result.get("craBalance").getAsString();
                                  Token craToken = new Token("CRA", Double.parseDouble(craBalance));

                                  list.add(craToken);
                              }
                        }

                        balances.postValue(list);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

//                    balance.postValue(DecimalUtils.to(result));
//                    symbol.postValue("OSB");
                },
                throwable -> {}
        );
    }

    private void fetchLimit() {
        disposable = WalletModel.getAccount().subscribe(
                result -> {
                    cpuPercent.postValue(result.getCpuLimit().getPercent());
                    netPercent.postValue(result.getNetLimit().getPercent());
                    ramPercent.postValue(result.getRamLimit().getPercent());
                },
                throwable -> {}
        );
    }

    private void fetchTokenList() {
//        disposable = WalletModel.getTokenList().subscribe(
//                result -> {
//                    if (result.isEmpty()) {
//                        String msg = mContext.getString(R.string.msg_error_empty);
//                        balance.postValue(msg);
//                    } else {
//                        tokens.postValue(result);
//                    }
//                },
//                throwable ->{}
//        );
        disposable = WalletModel.getSearchTokenList().subscribe(
                result -> {
                    if (result.isEmpty()) {
//                        String msg = mContext.getString(R.string.msg_error_empty);
//                        balance.postValue(msg);
                    } else {
                        symbols.postValue(result);
                    }
                },
                throwable ->{}
        );
    }

    private void refresh() {
        fetchBalance();
        fetchLimit();
        fetchTokenList();
    }
}
