package com.thx.logicroutermodule;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
public interface ILog {

    void i(String tag, String msg);

    void w(String tag, String msg);

    void e(String tag, String msg);


    class Log implements ILog {

        @Override
        public void i(String tag, String msg) {
            System.out.println("[Info] [" + tag + "] " + msg);
        }

        @Override
        public void w(String tag, String msg) {
            System.out.println("[Warn] [" + tag + "] " + msg);
        }

        @Override
        public void e(String tag, String msg) {
            System.out.println("[Error] [" + tag + "] " + msg);
        }
    }
}