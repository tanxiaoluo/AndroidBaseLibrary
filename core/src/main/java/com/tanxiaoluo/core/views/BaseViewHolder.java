package com.tanxiaoluo.core.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tanxiaoluo.core.BaseActivity;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public abstract class BaseViewHolder<T extends RecyclerItem> extends RecyclerView.ViewHolder {

    private WeakReference<BaseActivity> mActivityRef;
    private WeakReference<RecyclerAdapter> mRecyclerAdapterRef;
    private T mData;

    public BaseViewHolder(View itemView, BaseActivity activity, RecyclerAdapter adapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mActivityRef = new WeakReference<>(activity);
        mRecyclerAdapterRef = new WeakReference<>(adapter);
    }

    protected void updateViewsByData(T data) {
        mData = data;
    }

    protected BaseActivity getActivity() {
        return mActivityRef.get();
    }

    protected RecyclerAdapter getAdapter() {
        return mRecyclerAdapterRef.get();
    }

    protected T getData() {
        return mData;
    }
}
