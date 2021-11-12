package com.hyl.mq.helper.util;


import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * @author huayuanlin
 * @date 2021/06/10 20:18
 * @desc manual tx params
 */
public class ManualTxParams<R> {

    /*** tag */
    private String tag;
    /***  spread type*/
    private Propagation propagation;
    /***  isolation level*/
    private Isolation isolation;
    private String txName;
    /*** timeout */
    private Integer timeout;
    /*** readOnly*/
    private Boolean readOnly;
    /*** transactionManager */
    private PlatformTransactionManager txManager;
    /***  transaction action */
    private Function<ManualTxBizParam, R> func;
    /*** transaction after action */
    private Consumer<ManualTxBizParam> afterCommitHook;
    /*** transaction rollback action*/
    private BiConsumer<ManualTxBizParam, Exception> afterRollbackHook;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Propagation getPropagation() {
        return propagation;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }

    public Isolation getIsolation() {
        return isolation;
    }

    public void setIsolation(Isolation isolation) {
        this.isolation = isolation;
    }

    public String getTxName() {
        return txName;
    }

    public void setTxName(String txName) {
        this.txName = txName;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public PlatformTransactionManager getTxManager() {
        return txManager;
    }

    public void setTxManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    public Function<ManualTxBizParam, R> getFunc() {
        return func;
    }

    public void setFunc(Function<ManualTxBizParam, R> func) {
        this.func = func;
    }

    public Consumer<ManualTxBizParam> getAfterCommitHook() {
        return afterCommitHook;
    }

    public void setAfterCommitHook(Consumer<ManualTxBizParam> afterCommitHook) {
        this.afterCommitHook = afterCommitHook;
    }

    public BiConsumer<ManualTxBizParam, Exception> getAfterRollbackHook() {
        return afterRollbackHook;
    }

    public void setAfterRollbackHook(BiConsumer<ManualTxBizParam, Exception> afterRollbackHook) {
        this.afterRollbackHook = afterRollbackHook;
    }

}
