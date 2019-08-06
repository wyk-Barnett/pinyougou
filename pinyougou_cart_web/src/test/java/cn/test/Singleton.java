package cn.test;

/**
 * @author wangyangkun
 * @date 2019/8/6 0006 11:28
 */
public class Singleton {
    private Singleton(){}

    private static class SingletonInstance{
        private static final Singleton instance = new Singleton();
    }

    public static Singleton getInstance(){
        return SingletonInstance.instance;
    }
}
