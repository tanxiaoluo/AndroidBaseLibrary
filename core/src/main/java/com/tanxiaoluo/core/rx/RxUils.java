package com.tanxiaoluo.core.rx;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;

public class RxUils {

    public static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                new Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                }
        );
    }
}
