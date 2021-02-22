package io.oasisbloc.wallet.ui.addtoken.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.databinding.DialogGeneralOneButtonNoTitleBinding;
import io.oasisbloc.wallet.ui.modal.CenterModal;
import io.oasisbloc.wallet.ui.wallet.TokenDetailsActivity;
import io.oasisbloc.wallet.viewmodel.WalletListViewModel;

public class AddTokenListAdater extends BaseAdapter {
    private WalletListViewModel mViewModel;

    private List<String> items = new ArrayList<String>();
    private Context mContext;

    public AddTokenListAdater(Context context, List<String> list, WalletListViewModel model) {
        this.mContext = context;
        items = list;
        mViewModel = model;
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
            convertView = inflater.inflate(R.layout.row_add_token_list, null);
        }

        String data = items.get(position);

        AppCompatImageView logo = convertView.findViewById(R.id.logo);
        AppCompatTextView name = convertView.findViewById(R.id.name);

        if (data.equals("SOSB") == true) {
            name.setText("SOSB");
            logo.setImageResource(R.drawable.temp_sosb_ic_2);
        } else if (data.equals("CRA") == true) {
            name.setText("CRA");
            logo.setImageResource(R.drawable.temp_cra_ic_2);
        } else {
            name.setText("SOSB");
            logo.setImageResource(R.drawable.temp_sosb_ic_2);
        }

        convertView.setOnClickListener(v -> setMyToken(data));
        return convertView;
    }

    private void setMyToken(String symbol) {
        boolean isFind = false;
        String myTokens = mViewModel.getMyTokenList();

        JsonParser parser = new JsonParser();
        JsonArray arr = parser.parse(myTokens).getAsJsonArray();

        for (int i = 0; i < arr.size(); i++) {
            if (symbol.equals(arr.get(i).getAsString()) == true) {
                //중복.
                isFind = true;
                break;
            }
        }

        if (!isFind) {
            arr.add(symbol);
            mViewModel.setMyTokenList(arr.toString());
            showAlreadyAdded(true);
        } else {
            showAlreadyAdded(false);
        }
    }

    private void showAlreadyAdded(boolean isAdd) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DialogGeneralOneButtonNoTitleBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.dialog_general_one_button_no_title,
                null,
                false
        );
        Dialog dialog = CenterModal.showCenterModal(binding.getRoot());

        if(isAdd){
            binding.message.setText("Added.");
            binding.positive.setText("Confirm");
        }else {
            binding.message.setText("Already Added.");
            binding.positive.setText("Confirm");
        }

        binding.positive.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }


}
