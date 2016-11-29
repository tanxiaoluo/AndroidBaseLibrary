package com.tanxiaoluo.core;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tanxiaoluo.core.annotation.LayoutId;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

    private final int mLayoutId;
    private CompositeSubscription mSubscription;
    private boolean isDestroyed;
    protected BaseActivity mContext;
    private boolean isContainFragment = false;

    public BaseActivity() {
        mLayoutId = getClass().getAnnotation(LayoutId.class).value();
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView(savedInstanceState);
        setContentView(mLayoutId);
        ButterKnife.bind(this);
        mContext = this;
    }

    protected void beforeSetContentView(Bundle savedInstanceState){};

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 取消订阅
        if (mSubscription != null) {
            mSubscription.unsubscribe();
            mSubscription = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }

    public boolean isDestroyed() {
        return Build.VERSION.SDK_INT >= 17 ? super.isDestroyed() : isDestroyed;
    }

    public void closeInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    protected void snakeBar(View v, int stringId) {
        Snackbar.make(v, stringId, Snackbar.LENGTH_LONG).show();
    }

    protected void snakeBar(View v, String content) {
        Snackbar.make(v, content, Snackbar.LENGTH_LONG).show();
    }

    protected void toast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
    }
    protected void toast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        isContainFragment = true;
        return super.getSupportFragmentManager();
    }

    public boolean isContainFragment() {
        return isContainFragment;
    }
}
