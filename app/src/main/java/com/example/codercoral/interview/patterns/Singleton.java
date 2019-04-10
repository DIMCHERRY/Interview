package com.example.codercoral.interview.patterns;

/**
 * 单例模式:确保单例类只有一个实例，并且这个单例类提供一个函数接口让其他类获取到这个唯一的实例。
 * 使用场景：如果某个类，创建时需要消耗很多资源，即new出这个类的代价很大；或者是这个类占用很多内存，如果创建太多这个类实例会导致内存占用太多。
 */
public class Singleton {
    private static Singleton instance;
    private volatile static Singleton instance3;
    //将默认的构造函数私有化，防止其它类手动view
    private Singleton(){}

    /**
     * 如果是单线程下的系统，这么写肯定没问题。可是如果是多线程环境呢？
     * 这代码明显不是线程安全的，存在隐患：
     * 某个线程拿到的instance可能是null，可能你会想，这有什么难得，直接在getInstance()函数上加sychronized关键字不就好了。
     * 可是你想过没有，每次调用getInstance()时都要执行同步，这带来没必要的性能上的消耗。
     * 注意，在方法上加sychronized关键字时，一个线程访问这个方法时，其他线程无法同时访问这个类其他sychronized方法
     *
     * @return 实例
     */
    public static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * 为什么需要2次判断是否为空呢？第一次判断是为了避免不必要的同步，第二次判断是确保在此之前没有其他线程进入到sychronized块创建了新实例。
     * 这段代码看上去非常完美，但是，，，却有隐患！问题出现在哪呢？主要是在instance=new Singleton();这段代码上。这段代码会编译成多条指令，大致上做了3件事:
     *
     * （1）给Singleton实例分配内存
     * （2）调用Singleton()构造函数，初始化成员字段
     * （3）将instance对象指向分配的内存（此时instance就不是null啦~）
     *
     * 上面的（2）和（3）的顺序无法得到保证的，也就是说，JVM可能先初始化实例字段再把instance指向具体的内存实例，也可能先把instance指向内存实例再对实例进行初始化成员字段。
     * 考虑这种情况：一开始，第一个线程执行instance=new Singleton();这句时，JVM先指向一个堆地址，
     * 而此时，又来了一个线程2，它发现instance不是null，就直接拿去用了，但是堆里面对单例对象的初始化并没有完成，最终出现错误~
     *
     * @return
     */
    public static Singleton getInstance2(){
        if(instance == null){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    /**
     * 关键字volatile关键字的作用是：线程每次使用到被volatile关键字修饰的变量时，都会去堆里拿最新的数据。
     * 换句话说，就是每次使用instance时，保证了instance是最新的。这里解决了getInstance2的问题
     * 注意：volatile关键字并不能解决并发的问题
     * @return
     */
    public static Singleton getInstance3(){
        if(instance3 == null){
            synchronized (Singleton.class){
                if(instance3 == null){
                    instance3 = new Singleton();
                }
            }
        }
        return instance3;
    }
}
