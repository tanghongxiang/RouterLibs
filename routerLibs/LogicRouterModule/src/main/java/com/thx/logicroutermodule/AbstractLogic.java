package com.thx.logicroutermodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thx.logicroutermodule.inner.ILogic;
import com.thx.logicroutermodule.utils.ConvertUtils;

import java.text.NumberFormat;
import java.util.Map;
import java.util.UUID;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public abstract class AbstractLogic implements ILogic {

    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    public boolean isRunning;

    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @Override
    public boolean shouldRun() {
        return true;
    }

    @Override
    public boolean ignoreException() {
        return !LogicRouter.isStrictMode;
    }

    @Override
    public void setParams(@NonNull Map params) {

    }

    @Override
    public void setParams(@NonNull String requestBody) {

    }

    @Override
    public void setPathParams(@NonNull Map params) {

    }

    @Override
    public void updateParams(@Nullable Map updateParams) {

    }

    @Override
    public String uuid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    /* ======================================================= */
    /* Protected Methods                                       */
    /* ======================================================= */

    protected double doubleOf(@NonNull Map params, @NonNull String name) {
        return ConvertUtils.doubleOf(params.get(name));
    }

    protected int intOf(@NonNull Map params, @NonNull String name) {
        return ConvertUtils.intOf(params.get(name));
    }

    @Nullable
    protected String stringOf(@NonNull Map params, @NonNull String name) {
        try {
            Object value = params.get(name);
            if (value == null) {
                return null;
            }
            return String.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    protected String stringFromDouble(@NonNull Map params, @NonNull String name) {
        Object obj = params.get(name);
        if (obj == null) {
            return null;
        }

        // 如果不是 Double 类型
        if (!(obj instanceof Double)) {

            // 如果是一个 Number，获取其 double 值
            if (obj instanceof Number) {
                obj = ((Number) obj).doubleValue();
            }

            // 如果是一个 String， 尝试转为 Double 类型
            else if (obj instanceof String) {
                try {
                    obj = Double.valueOf((String) obj);
                }
                // 转换失败返回 null
                catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            // 既不是 Double、又不是 Number、还不是 String，不处理
            else {
                return null;
            }
        }

        try {
            Double value = (Double) obj;
            NumberFormat format = NumberFormat.getInstance();
            format.setGroupingUsed(false);
            return format.format(value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    protected <T> T objOf(@NonNull Map params, @NonNull String name, Class<T> clazz) {
        try {
            //noinspection unchecked
            return (T) params.get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
