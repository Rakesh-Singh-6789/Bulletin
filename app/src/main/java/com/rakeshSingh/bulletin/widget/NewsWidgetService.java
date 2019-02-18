package com.rakeshSingh.bulletin.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class NewsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NewsWidgetDataProvider(NewsWidgetService.this, intent);
    }
}
