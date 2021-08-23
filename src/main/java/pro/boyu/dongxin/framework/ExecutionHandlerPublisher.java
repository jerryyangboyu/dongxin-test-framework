package pro.boyu.dongxin.framework;

import java.util.HashMap;
import java.util.Map;

public class ExecutionHandlerPublisher {
    private Map<String, ExecutionHandlerSubscriber> subscribers = new HashMap<>();
    private String lastSubscriberName;

    public void register(ExecutionHandlerSubscriber subscriber) {
        subscribers.put(subscriber.getName(), subscriber);
        this.lastSubscriberName = subscriber.getName();
    }

    public void registerNotExist(ExecutionHandlerSubscriber subscriber) {
        if (!this.subscribers.containsKey(subscriber.getName())) {
            register(subscriber);
        }
    }

    public boolean isSubscriberPending(String lastSubscriberName) {
        return this.subscribers.get(lastSubscriberName).isFinished();
    }

    public void setSubscriberPendingStatus(String subscriberName, boolean state) {
        this.subscribers.get(subscriberName).setFinished(state);
    }

    public void invoke(String subscriberName, boolean success, String message) {
        ExecutionHandlerSubscriber subscriber = subscribers.get(subscriberName);
        subscriber.setFinished(true);
        subscriber.process(false, success, message);
    }

    public boolean subscriberExist(String subscriberName) {
        return this.subscribers.containsKey(subscriberName);
    }

    public String getLastSubscriberName() {
        return lastSubscriberName;
    }

}

