package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import timber.log.Timber;

/**
 * Created by andreas on 27/05/17.
 */

public class StockWidgetViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockWidgetDataProvider(this.getApplicationContext(), intent);
    }

}
