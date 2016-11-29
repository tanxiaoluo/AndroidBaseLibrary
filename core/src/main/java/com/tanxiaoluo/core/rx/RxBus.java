package com.tanxiaoluo.core.rx;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    private static volatile RxBus defaultInstance;

    private final Subject<Object, Object> bus;

    private RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        if (null == defaultInstance) {
            synchronized (RxBus.class) {
                if (null == defaultInstance) {
                    defaultInstance = new RxBus();
                }
            }
        }
        return defaultInstance;
    }

    /**
     * 发送一个新的事件
     *
     * @param obj 变化的内容
     */
    public void post(Object obj) {
        bus.onNext(obj);
    }

    /**
     * 发送一个新的事件
     * @param code 变化的code
     * @param obj 变化的内容
     */
    public void post(int code, Object obj) {
        bus.onNext(new RxMessage(code, obj));
    }

    /**
     * 返回一个被观察对象
     *
     * @param eventType 被观察的类型
     * @param <T>       T
     * @return 返回一个被观察对象
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * 根据code和类型 返回一个被观察对象
     * @param code code
     * @param eventType 被观察的类型
     * @param <T> T
     * @return 返回一个被观察对象
     */
    public <T> Observable<T> toObservable(final int code, final Class<T> eventType) {
        return bus
                .ofType(RxMessage.class)
                .filter(new Func1<RxMessage, Boolean>() {
                    @Override
                    public Boolean call(RxMessage rxMessage) {
                        return code == rxMessage.code  && eventType.isInstance(rxMessage.object);
                    }
                })
                .map(new Func1<RxMessage, Object>() {
                    @Override
                    public Object call(RxMessage rxMessage) {
                        return rxMessage.object;
                    }
                })
                .cast(eventType);
    }

    private static class RxMessage{
        final int code;
        final Object object;

        RxMessage(int code, Object object) {
            this.code = code;
            this.object = object;
        }
    }
}
