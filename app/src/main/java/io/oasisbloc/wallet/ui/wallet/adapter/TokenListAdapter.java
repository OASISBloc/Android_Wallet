package io.oasisbloc.wallet.ui.wallet.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;

import java.util.ArrayList;
import java.util.List;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.data.Token;
import io.oasisbloc.wallet.data.Transaction;
import io.oasisbloc.wallet.databinding.DialogGeneralOneButtonNoTitleBinding;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.ui.wallet.TokenDetailsActivity;
import io.oasisbloc.wallet.viewmodel.WalletListViewModel;

public class TokenListAdapter extends BaseAdapter {

    private List<Token> items = new ArrayList<Token>();
    private Context mContext;
    private WalletListViewModel mViewModel;
    private Callback mCallback;

    public TokenListAdapter(Context context, List<Token> list, WalletListViewModel model){
        this.mContext = context;
        items = list;
        mViewModel = model;
    }

    public void setCallback(Callback callback){
        mCallback = callback;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_wallet_token, null);
        }

        Token data = items.get(position);

        AppCompatImageView logo = convertView.findViewById(R.id.logo);
        AppCompatTextView name = convertView.findViewById(R.id.name);
        AppCompatTextView balance = convertView.findViewById(R.id.balance);

        if (data.getSymbol().equals("OSB") == true) {
            name.setText("OSB");
            balance.setText(String.valueOf(data.getBalance()));
            logo.setImageResource(R.drawable._temp_osb_ic_2);
        } else if (data.getSymbol().equals("SOSB") == true) {
            name.setText("SOSB");
            balance.setText(String.valueOf(data.getBalance()));
            logo.setImageResource(R.drawable.temp_sosb_ic_2);
        } else if (data.getSymbol().equals("CRA") == true) {
            name.setText("CRA");
            balance.setText(String.valueOf(data.getBalance()));
            logo.setImageResource(R.drawable.temp_cra_ic_2);
        } else {
            name.setText("OSB");
            balance.setText(String.valueOf(data.getBalance()));
            logo.setImageResource(R.drawable._temp_obs_ic_1);
        }

        convertView.setOnClickListener(v -> gotoSendPage(data.getSymbol()));
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCallback.onItemLongClicked(position, data.getSymbol());
                return true;
            }
        });

        return convertView;
    }

    private void gotoSendPage(String symbol){
        TokenDetailsActivity.start(mContext, symbol);
    }



    public interface Callback {

        void onItemLongClicked(int position, String symbol);
    }

}
