package com.example.codercoral.interview.patterns;

import android.content.Intent;
import android.net.Uri;

/**
 * 建造者模式：将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。
 * 试用场景：对象的构造复杂，链式调用
 */
public class Builder {
    /**
     * 原型模式:原型设计模式非常简单，就是将一个对象进行拷贝。对于类A实例a，要对a进行拷贝，就是创建一个跟a一样的类型A的实例b，然后将a的属性全部复制到b。
     * 另外要注意的是，还有深拷贝和浅拷贝。深拷贝就是把对象里面的引用的对象也要拷贝一份新的对象，并将这个新的引用对象作为拷贝的对象引用。说的比较绕哈~，举个例子，假设A类中有B类的引用b，现在需要对A类实例进行拷贝，那么深拷贝就是，先对b进行一次拷贝得到nb，然后把nb作为A类拷贝的对象的引用，如此一层一层迭代拷贝，把所有的引用都拷贝结束。浅拷贝则不是。
     *试用场景：类的属性特别多，但是又要经常对类进行拷贝的时候可以用原型模式，这样代码比较简洁，而且比较方便。
     */
    class yuanxin{
        Uri uri=Uri.parse("smsto:10086");
        Intent shareIntent=new Intent(Intent.ACTION_SENDTO,uri);

        //克隆副本
        Intent intent=(Intent)shareIntent.clone();
    }
}
