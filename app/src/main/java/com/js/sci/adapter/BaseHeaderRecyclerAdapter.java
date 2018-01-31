package com.js.sci.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.js.sci.util.SCILog;

/**
 * Created by a80099709 on 2017. 9. 12..
 */

public abstract class BaseHeaderRecyclerAdapter<T, H, F> extends BaseRecyclerAdapter<T> {

    private static final int TYPE_HEADER = Integer.MIN_VALUE;
    private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;

    private boolean showHeader = false;
    private boolean showFooter = false;

    private H header;
    private F footer;

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(getHeaderResource(), parent, false);
            return getHeaderViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(getFooterResource(), parent, false);
            return getFooterViewHolder(view);

        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (isHeaderPosition(position)) {
            holder.bind(header, position);
        } else if (isFooterPosition(position)) {
            holder.bind(footer, position);
        } else {
            super.onBindViewHolder(holder, position - (hasHeader() ? 1 : 0));
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();

        if (hasHeader()) {
            itemCount += 1;
        }

        if (hasFooter()) {
            itemCount += 1;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return TYPE_HEADER;
        }
        if (isFooterPosition(position)) {
            return TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    protected abstract int getHeaderResource();

    protected abstract BaseViewHolder getHeaderViewHolder(View view);

    protected abstract int getFooterResource();

    protected abstract BaseViewHolder getFooterViewHolder(View view);

    public int getBaseItemCount() {
        return super.getItemCount();
    }

    public boolean hasHeader() {
        return getHeaderResource() > 0 && showHeader;
    }

    public boolean hasFooter() {
        return getFooterResource() > 0 && showFooter;
    }

    public boolean isHeaderPosition(int position) {
        return hasHeader() && position == 0;
    }

    public boolean isFooterPosition(int position) {
        return hasFooter() && position == getItemCount() - 1;
    }

    public void showHeader(H header) {
        showHeader = true;
        this.header = header;
        notifyDataSetChanged();
    }

    public void hideHeader() {
        showHeader = false;
        this.header = null;
        notifyDataSetChanged();
    }
    public void showFooter(F footer) {
        showFooter = true;
        this.footer = footer;
        notifyDataSetChanged();
    }

    public void hideFooter() {
        showFooter = false;
        this.footer = null;
        notifyDataSetChanged();
    }

}
