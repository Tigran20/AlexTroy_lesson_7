package ru.tinkoff.ru.seminar.ui;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class StubTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // stub
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // stub
    }

    @Override
    public void afterTextChanged(Editable s) {
        // stub
    }
}
