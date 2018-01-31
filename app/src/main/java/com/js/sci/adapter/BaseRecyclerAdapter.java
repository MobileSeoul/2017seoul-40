package com.js.sci.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.js.sci.util.SCILog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by a80099709 on 2017. 9. 12..
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    public interface OnItemClickListener<T> {
        void onItemClick(T obj);
    }

    private final Object lock = new Object();
    protected List<T> list;
    protected OnItemClickListener onItemClickListener;

    public BaseRecyclerAdapter() {
        this(new ArrayList<T>());
    }

    public BaseRecyclerAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(getResource(), parent, false);
        return getItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        final T object = list.get(position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(object);
                }
            });
        }
        holder.bind(object, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected abstract int getResource();

    protected abstract BaseViewHolder getItemViewHolder(View view);

    public void add(T object) {
        synchronized (lock) {
            list.add(object);
        }
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        synchronized (lock) {
            list.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void insert(T object, int index) {
        synchronized (lock) {
            list.add(index, object);
        }
        notifyDataSetChanged();
    }

    public void remove(T object) {
        synchronized (lock) {
            list.remove(object);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (lock) {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (lock) {
            Collections.sort(list, comparator);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
