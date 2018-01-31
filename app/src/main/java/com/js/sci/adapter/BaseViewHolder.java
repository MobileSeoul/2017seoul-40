package com.js.sci.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by a80099709 on 2017. 9. 12..
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        init();
    }

    protected abstract void init();
    protected abstract void bind(T item, int position);

}
