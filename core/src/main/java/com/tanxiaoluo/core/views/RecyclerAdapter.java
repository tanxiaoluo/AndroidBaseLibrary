package com.tanxiaoluo.core.views;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanxiaoluo.core.BaseActivity;
import com.tanxiaoluo.core.annotation.LayoutId;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public final class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private WeakReference<BaseActivity> mActivityRef;
    private SparseArray<Class<? extends BaseViewHolder>> mViewHolder = new SparseArray<>();
    private ArrayList<RecyclerItem> mDatas = new ArrayList<>();

    public RecyclerAdapter(BaseActivity activity) {
        mActivityRef = new WeakReference<>(activity);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getViewType();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<? extends BaseViewHolder> clazz = mViewHolder.get(viewType);
        int layoutId = clazz.getAnnotation(LayoutId.class).value();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        BaseViewHolder holder = null;
        try {
            holder = clazz.getConstructor(View.class, BaseActivity.class, RecyclerAdapter.class)
                    .newInstance(itemView, mActivityRef.get(), this);
        } catch (Exception e) {
            Timber.e(e, "实例化ViewHolder错误 %s", e.getCause());
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        //noinspection unchecked
        holder.updateViewsByData(mDatas.get(position));
    }

    public void registerViewHolder(int viewType, Class<? extends BaseViewHolder> clazz) {
        mViewHolder.put(viewType, clazz);
    }

    public void updateDatas(boolean clearOld, List<? extends RecyclerItem> datas) {
        if (clearOld) {
            mDatas.clear();
        }
        if (datas != null) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public ArrayList<RecyclerItem> getDatas() {
        return mDatas;
    }

    public void removeItem(RecyclerItem item){
        int position = mDatas.indexOf(item);
        if ( -1 == position){
            return;
        }
        mDatas.remove(item);
        notifyItemRemoved(position);
    }
}
