package cn.test;

/**
 * @author wangyangkun
 * @date 2019/8/6 0006 13:52
 */
public class Singleton_02 {
    private static volatile Singleton_02 singleton_02;

    private Singleton_02(){}

    public static Singleton_02 getInstance(){
        if (singleton_02==null){
            synchronized (Singleton_02.class){
                if (singleton_02==null){
                    singleton_02 = new Singleton_02();
                }
            }
        }
        return singleton_02;
    }
}
