package com.tanxiaoluo.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tanxiaoluo.core.annotation.LayoutId;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.subscriptions.CompositeSubscription;

/**
 * Dutyapp
 * Created by Malei on 16/7/22.
 */
public abstract class BaseFragment extends Fragment {

    private final int mLayoutId;
    private Unbinder mUnbinder;
    private CompositeSubscription mSubscription;

    public BaseFragment() {
        LayoutId layoutId = getClass().getAnnotation(LayoutId.class);
        mLayoutId = null == layoutId ? -1 : layoutId.value();
    }

    /**
     * 用于同一处理订阅状态
     *
     * @return 订阅合集
     */
    public CompositeSubscription getSubscription() {
        if (mSubscription == null) {
            mSubscription = new CompositeSubscription();
        }
        return mSubscription;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mLayoutId > 0) {
            View rootView = inflater.inflate(mLayoutId, container, false);
            mUnbinder = ButterKnife.bind(this, rootView);
            return rootView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 取消订阅
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }
}
