package com.hyl.mq.helper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author huayuanlin
 * @date 2021/06/10 20:30
 * @desc the manual tx util
 */
public class ManualTxUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManualTxUtil.class);

    private ManualTxUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the default transaction manager
     */
    private static PlatformTransactionManager defaultTransactionManager() {
        return SpringBeanUtil.getBeanByType(PlatformTransactionManager.class);
    }

    /**
     * cur exist tx
     *
     * @return boolean
     */
    public static boolean curExistTx() {
        return TransactionSynchronizationManager.isActualTransactionActive();
    }

    /**
     * with out param context start manual tx
     *
     * @param params tx action param
     */
    public static <R> R startTxWithOutParams(ManualTxParams<R> params) {
        return startTx(params, null);
    }

    /**
     * start manual tx
     *
     * @param params  tx action param
     * @param context manual tx biz param
     * @return result
     */
    public static <R> R startTx(ManualTxParams<R> params, ManualTxBizParam context) {
        String tag = Optional.ofNullable(params.getTag()).orElse("");
        DefaultTransactionDefinition def = buildDefinition(params);
        PlatformTransactionManager tx = Optional.ofNullable(params.getTxManager()).orElse(defaultTransactionManager());
        Assert.notNull(tx, "transaction manager is null,tag:" + tag);
        TransactionStatus status = tx.getTransaction(def);
        R result;
        try {
            result = params.getFunc().apply(context);
            tx.commit(status);
        } catch (Exception ex) {
            LOGGER.error("manual tx execute fail,tag:{}", tag, ex);
            tx.rollback(status);
            Optional.ofNullable(params.getAfterRollbackHook()).ifPresent(hook -> hook.accept(context, ex));
            throw ex;
        }
        Optional.ofNullable(params.getAfterCommitHook()).ifPresent(hook -> hook.accept(context));
        return result;
    }


    /**
     * build tx definition info
     *
     * @param params tx action param
     * @return tx definition
     */
    public static <R> DefaultTransactionDefinition buildDefinition(ManualTxParams<R> params) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        Optional.ofNullable(params.getPropagation()).map(Propagation::value).ifPresent(def::setPropagationBehavior);
        Optional.ofNullable(params.getIsolation()).map(Isolation::value).ifPresent(def::setIsolationLevel);
        Assert.notNull(params.getTimeout(), "must set tx time out");
        Optional.ofNullable(params.getTimeout()).ifPresent(def::setTimeout);
        Optional.ofNullable(params.getTxName()).ifPresent(def::setName);
        Optional.ofNullable(params.getReadOnly()).ifPresent(def::setReadOnly);
        return def;
    }

}
