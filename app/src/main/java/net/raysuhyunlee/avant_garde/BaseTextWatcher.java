package net.raysuhyunlee.avant_garde;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by SuhyunLee on 2015. 12. 19..
 */
public abstract class BaseTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void afterTextChanged(Editable s) {}
}
