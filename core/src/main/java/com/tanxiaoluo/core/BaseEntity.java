package com.tanxiaoluo.core;

import com.google.gson.Gson;

public abstract class BaseEntity {

    @Override
    public String toString() {
        return super.toString() + "\n" + new Gson().toJson(this);
    }
}
