package com.js.sci.adapter;

import android.support.v7.widget.GridLayoutManager;

/**
 * Created by a80099709 on 2017. 9. 13..
 */

public class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private final BaseHeaderRecyclerAdapter adapter;
    private final GridLayoutManager layoutManager;

    public HeaderSpanSizeLookup(BaseHeaderRecyclerAdapter adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        boolean isHeaderOrFooter = adapter.isHeaderPosition(position) || adapter.isFooterPosition(position);
        return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
    }
}
