package com.github.abhrp.pixabaysearchdemo.adapters.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created by abhrp on 5/1/17.
 */

public abstract class BaseRecyclerAdapter<T, E extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<E> {

    protected List<T> mList;
    protected Context mContext = null;
    protected LayoutInflater mInflater;

    public BaseRecyclerAdapter(final Context context) {
        if (context != null) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }
    }

    public void setList(final List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addSubList(final List<T> list) {
        int startIndex = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(startIndex, list.size());
    }

    public void addSubList(int pos, final  List<T> list) {
        mList.addAll(pos, list);
        notifyItemRangeInserted(pos, list.size());
    }

    @Override
    public int getItemCount() { return mList != null ? mList.size() : 0; }

    public T getItem(int position) {
        return mList.get(position);
    }


    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position,T t){
        mList.add(position,t);
        notifyItemInserted(position);
    }

    public void addItem(T t) {
        mList.add(t);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mList.clear();
        notifyDataSetChanged();
    }
}
