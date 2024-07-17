package com.thx.logicroutermodule.inner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thx.logicroutermodule.AbstractLogic;
import com.thx.logicroutermodule.BaseAsynLogic;
import com.thx.logicroutermodule.BaseSyncLogic;
import com.thx.logicroutermodule.ILogicController;
import com.thx.logicroutermodule.ILogicHandler;
import com.thx.logicroutermodule.IRecyclable;
import com.thx.logicroutermodule.LogicResult;
import com.thx.logicroutermodule.LogicRouter;
import com.thx.logicroutermodule.logic.LogicUseKt;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public class LogicRouterImpl implements ILogicRouter {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */


    /**
     * Key: 名称; Value: 实现类
     */
    private Map<String, Class<? extends AbstractLogic>> mName2Impl;

    /**
     * 所有可重用的 Logic 创建后都保存到这里。
     * <br/> 执行完成后，从这里移除，添加到 {@link #mUuid2Recyclable} 中。
     */
    private LinkedHashMap<Class<? extends AbstractLogic>, AbstractLogic> mRecyclerPool;

    /**
     * <uuid, logic>
     */
    private Map<String, IRecyclable> mUuid2Recyclable;

    /**
     * 对 {@link #mUuid2Recyclable} 和 {@link #mRecyclerPool} 进行线程安全保护的锁。
     */
    private ReentrantLock mLock;



    /* ======================================================= */
    /* Constructors                                            */
    /* ======================================================= */

    public LogicRouterImpl() {
        mRecyclerPool = new LruCacheMap<>(16);
        mUuid2Recyclable = new HashMap<>();
        mLock = new ReentrantLock();
    }



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @Override
    public synchronized void addLogicMap(@NonNull Map<String, Class<? extends AbstractLogic>> map) {
        if (mName2Impl == null) {
            mName2Impl = new HashMap<>(map.size());
        }

        mName2Impl.putAll(map);
    }

    @Nullable
    @Override
    public ILogicController asynExecute(@NonNull String name, @Nullable Map params, @Nullable ILogicHandler handler) {
        return prepareLogic(false, name, params, null,null, handler);
    }

    @Nullable
    @Override
    public ILogicController asynExecuteWithObject(@NonNull String name, @Nullable String body, @Nullable ILogicHandler handler) {
        return prepareLogic(true, name, null, body, null,handler);
    }

    @Nullable
    @Override
    public ILogicController asynExecute(@NonNull String name, @Nullable Map params, @Nullable Map pathParams, @Nullable ILogicHandler handler) {
        return  prepareLogic(false, name, params, null,pathParams, handler);
    }

    @Nullable
    @Override
    public ILogicController asynExecuteWithObject(@NonNull String name, @Nullable String body, @Nullable Map pathParams, @Nullable ILogicHandler handler) {
        return prepareLogic(true, name, null, body, pathParams,handler);
    }

    @Override
    public LogicResult syncExecute(String name, Map params) {
        // 实例化 Logic
        AbstractLogic logic = createLogic(name);

        // 无法实例化 Logic
        if (logic == null) {
            return new LogicResult(ILogicHandler.CODE_NO_SUCH_LOGIC, null, null);
        }

        // 不是一个同步任务
        boolean isSyncLogic = logic instanceof BaseSyncLogic;
        if (!isSyncLogic) {
            return new LogicResult(ILogicHandler.CODE_NOT_VALID_SYNC_LOGIC, null, null);
        }

        // 设置参数
        if (params != null) {
            logic.setParams(params);
        }

        // 检查是否可以运行
        if (!logic.shouldRun()) {
            String res = LogicUseKt.processUnPassShouldRunErrMsg(false);
            return new LogicResult(ILogicHandler.CODE_PARAMS_INVALID, res, null);
        }

        // 运行
        return ((BaseSyncLogic) logic).run();
    }



    /* ======================================================= */
    /* Private Methods                                         */
    /* ======================================================= */

    private AbstractLogic createLogic(String name) {
        try {
            Class<? extends AbstractLogic> logicClass = mName2Impl.get(name);

            if (logicClass == null) {
                String msg = "找不到 " + name + " 对应的实现类！";
                LogicRouter.sLog.e("LogicRouter", msg);
                if (LogicRouter.isStrictMode) {
                    throw new IllegalArgumentException(msg);
                }
                return null;
            }

            // 如果是可重用的，从重用池中取
            boolean isRecyclable = IRecyclable.class.isAssignableFrom(logicClass);
            if (isRecyclable) {
                AbstractLogic logic = mRecyclerPool.get(logicClass);
                // 如果重用池中存在，清空后返回
                if (logic != null) {
                    ((IRecyclable) logic).clearBeforeReuse();
                    return logic;
                }
                // 如果重用池中不存在，创建一个新的
                else {
                    return logicClass.newInstance();
                }
            }

            // 如果是不可重用的，创建一个新的
            return logicClass.newInstance();

        } catch (Exception e) {
            System.out.println("错误信息" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 当一个Logic类不属于异步任务，也不属于同步任务时，执行该方法。
     */
    private void notifyLogicNotExtendsSpecialException(String name, ILogicHandler handler) {
        Class<? extends AbstractLogic> logicClass = mName2Impl.get(name);

        String msg = logicClass.getSimpleName() + " 既没有继承 AsynLogic， 也没有继承 SyncLogic类。"
                + "请继承 AsynLogic 或者 SyncLogic 实现自己的业务逻辑，"
                + "不要直接继承 AbstractLogic。";

        if (LogicRouter.isStrictMode) {
            throw new RuntimeException(msg);
        }

        if (handler != null) {
            LogicResult result = new LogicResult(ILogicHandler.CODE_OTHER, null, null);
            handler.onResponse(result);
        }

        LogicRouter.sLog.e("LogicRouter", msg);
    }

    private ILogicController prepareLogic(boolean hasBody, @NonNull String name, @Nullable Map params, @Nullable String body,@Nullable Map pathParams, @Nullable ILogicHandler handler) {
        // 实例化 Logic
        AbstractLogic logic = createLogic(name);

        // 无法实例化 Logic
        if (logic == null) {
            if (handler != null) {
                LogicResult result = new LogicResult(ILogicHandler.CODE_NO_SUCH_LOGIC, null, null);
                handler.onResponse(result);
            }
            return null;
        }

        // 判断是否为一个异步任务
        boolean isAsynLogic = logic instanceof BaseAsynLogic;
        boolean isSyncLogic = logic instanceof BaseSyncLogic;
        if (!isAsynLogic && !isSyncLogic) {
            notifyLogicNotExtendsSpecialException(name, handler);
            return null;
        }

        // 如果是同步的任务，则封装为一个异步任务
        if (isSyncLogic) {
            SyncLogicWrapper logicWrapper = new SyncLogicWrapper((BaseSyncLogic) logic);
            logicWrapper.isRunning = true;
            if (hasBody) {
                logicWrapper.startCompat(body, handler);
            } else {
                logicWrapper.startCompat(params, handler);
            }
            return logicWrapper;
        }

        // 如果是可重用的 Logic， 包装 Handler， 使得结束后加入到缓存池
        if (logic instanceof IRecyclable) {
            mUuid2Recyclable.put(logic.uuid(), (IRecyclable) logic);
            handler = new ReuseLogicHandler(handler);
        }

        // 如果是异步任务，准备执行
        logic.isRunning = true;
        if (hasBody) {
            ((BaseAsynLogic) logic).startWithBody(body,pathParams, handler);
        } else {
            ((BaseAsynLogic) logic).start(params,pathParams, handler);
        }
        return logic;
    }


    /* ======================================================= */
    /* Inner Class                                             */
    /* ======================================================= */

    private class ReuseLogicHandler implements ILogicHandler {

        ILogicHandler proxy;

        ReuseLogicHandler(ILogicHandler proxy) {
            this.proxy = proxy;
        }

        @Override
        public void onResponse(@NonNull LogicResult result) {
            if (proxy != null) {
                proxy.onResponse(result);
            }

            // 如果 logic 已经执行成功或者失败了，说明已经执行结束了
            if (result.isSuccess() || result.isFailure()) {
                // 根据 uuid 从缓存中取出来，加入到可重用池中。
                IRecyclable recyclable = mUuid2Recyclable.get(result.uuid);
                if (recyclable instanceof AbstractLogic) {
                    AbstractLogic logic = (AbstractLogic) recyclable;
                    mLock.lock();
                    // 加入到可重用列表
                    mRecyclerPool.put(logic.getClass(), logic);
                    // 从未完成列表中移除
                    mUuid2Recyclable.remove(result.uuid);
                    mLock.unlock();
                }
            }
        }
    }


    private class LruCacheMap<K, V> extends LinkedHashMap<K, V> {

        /**
         * 最大容量，超过这个大小时，最旧的数据会被移除
         */
        private int mMaxCount;

        LruCacheMap(int maxCount) {
            super(16, 0.75f, true);
            mMaxCount = maxCount;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > mMaxCount;
        }
    }
}
