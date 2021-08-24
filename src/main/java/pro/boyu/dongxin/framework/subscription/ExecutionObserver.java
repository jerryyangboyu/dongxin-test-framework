package pro.boyu.dongxin.framework.subscription;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import pro.boyu.dongxin.utils.iface.Observer;
import pro.boyu.dongxin.utils.logger.Logger;
import pro.boyu.dongxin.utils.logger.LoggerFactory;

import pro.boyu.dongxin.framework.OrderedPackageLoader;
import pro.boyu.dongxin.framework.constenum.TestCaseState;
import pro.boyu.dongxin.framework.infobean.ExecutionInfo;
import pro.boyu.dongxin.utils.jucservice.Unsubscribtion;

import static pro.boyu.dongxin.utils.logger.TestCaseInfoUtil.getLocalDate;

public class ExecutionObserver implements Observer<ExecutionInfo> {
    final Logger logger = LoggerFactory.getLogger(ExecutionObserver.class);
    List<ExecutionInfo> runningInfo = new LinkedList<>();
    ExecutionInfo endInfo;
    Unsubscribtion unsubscribtion;
    String className;
    String methodName;

    public void subscribe(ExecutorSubject observable) {
        this.className = observable.getClassName();
        this.methodName = observable.getMethodName();
        this.unsubscribtion = observable.subscribe(this);
    }

    @Override
    public void next(ExecutionInfo data) {
        switch (data.getState()) {
            case START:
                this.runningInfo.add(data);
                break;
            case RUNNING:
                this.runningInfo.add(data);
                break;
            case ERROR:
                this.runningInfo.add(data);
                break;
            case SUCCESSFIN:
                this.runningInfo.add(data);
                this.endInfo = data;
                break;
            case ERRORFIN:
                this.runningInfo.add(data);
                this.endInfo = data;
                break;
            default:
                break;
        }
    }

    @Override
    public void error(Exception e) {
        this.runningInfo.add(new ExecutionInfo(System.currentTimeMillis(), TestCaseState.RUNNING, e.getMessage()));
    }

    @Override
    public void completed() {
        this.unsubscribtion.unsubscribe();
    }

    public void exportInfos() {
        synchronized (OrderedPackageLoader.loggerLockObject) {
            this.runningInfo.forEach(ri -> {
                logger.info("testcase {} log in {} ,message : {}", this.methodName, getLocalDate(ri.getTime()), ri.getMessage());
            });
        }
    }

    public List<ExecutionInfo> showErrors() {
        return this.runningInfo.stream().filter((value) -> {
            return value.getState() == TestCaseState.ERROR;
        }).collect(Collectors.toList());
    }

}
