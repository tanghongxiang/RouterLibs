package com.thx.logicroutermodule.inner;

import androidx.annotation.NonNull;

import com.thx.logicroutermodule.ILogicController;

import java.util.Map;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface ILogic extends ILogicController {

    /**
     * run 方法执行之前的检查。
     * <p>
     * <br/> 实现方可以在这个方法判断参数是否满足要求。
     * <br/> 不满足时返回 false，框架将不再执行 run 方法。
     * <br/> AbstractLogic 默认返回 true。
     * <br/>
     *
     * @return true: 继续执行, false: 不执行。
     */
    boolean shouldRun();

    /**
     * 外部传入的参数，将通过这个方法传递给实现类。
     *
     * @param params Logic需要的参数
     */
    void setParams(@NonNull Map params);

    /**
     * 外部传入的参数(requestBody)，将通过这个方法传递给实现类。
     * @param requestBody
     */
    void setParams(@NonNull String requestBody);

    /**
     * 设置Request Path上的动态参数
     */
    void setPathParams(@NonNull Map params);

    /**
     * 是否忽略执行过程中检测到的异常。
     * <p>
     * <br/> 开启严格模式时，默认为NO。关闭严格模式时，默认YES。
     *
     * @return true: 忽略    false: 不忽略
     */
    boolean ignoreException();

}

