package io.oasisbloc.wallet.base;

import android.util.Patterns;

import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

import io.oasisbloc.wallet.App;
import io.oasisbloc.wallet.R;

public class PolicyUtils {

    /*
        amount
     */

    private static final double MIN_AMOUNT = 0.001;

    public static boolean isAmountValidate(double amount, double balance) {
        return MIN_AMOUNT <= amount && amount <= balance;
    }

    public static double getAmountMinValue() {
        return MIN_AMOUNT;
    }


    /*
        account
     */

    public static boolean isAccountValidate(String text) {
        int length = text.length();
        int fixLength = getAccountFixMaxLength();
        return length == fixLength;
    }

    public static String getAccountDigits() {
        return getString(R.string.policy_account_filter);
    }

    public static int getAccountFixMaxLength() {
        return getInteger(R.integer.policy_account_fix_length);
    }

    /*
        memo
     */

    public static boolean isMemoValidate(String text) {
        int length = text.length();
        int maxLength = getMemoMaxLength();
        return length <= maxLength;
    }

    public static int getMemoMaxLength() {
        return getInteger(R.integer.policy_memo_max_length);
    }


    /*
        password
     */
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";

    public static boolean isPasswordValidate(String text) {
        int length = text.length();
        int minLength = getPasswordMinLength();
        int maxLength = getPasswordMaxLength();

        boolean containsLetter = false;
        for (int i = 0; i < LETTERS.length(); i++) {
            if (text.contains(LETTERS.charAt(i) + "")) {
                containsLetter = true;
                break;
            }
        }
        boolean containsNumber = false;
        for (int i = 0; i < NUMBERS.length(); i++) {
            if (text.contains(NUMBERS.charAt(i) + "")) {
                containsNumber = true;
                break;
            }
        }
        
        return minLength <= length && length <= maxLength && containsLetter && containsNumber;
    }

    public static int getPasswordMinLength() {
        return getInteger(R.integer.policy_password_min_length);
    }

    public static int getPasswordMaxLength() {
        return getInteger(R.integer.policy_password_max_length);
    }

    /*
        private key
     */

    public static boolean isPrivateKeyValidate(String text) {
        int length = text.length();
        int fixLength = getPrivateKeyFixLength();
        return length == fixLength;
    }

    public static int getPrivateKeyFixLength() {
        return getInteger(R.integer.policy_private_key_fix_length);
    }

    public static String getPrivateKeyDigits() {
        return getString(R.string.policy_private_key_filter);
    }


    /*
        email
     */

    public static boolean isEmailValidate(String text) {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }


    /*

     */

    private static int getInteger(@IntegerRes int id) {
        return App.getInstance().getResources().getInteger(id);
    }

    private static String getString(@StringRes int id) {
        return App.getInstance().getString(id);
    }
}
