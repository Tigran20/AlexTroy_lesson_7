package ru.tinkoff.ru.seminar.ui;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class CurrencyLoader extends AsyncTaskLoader<Currencies> {

    public CurrencyLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Currencies loadInBackground() {
        return null;
    }


}
