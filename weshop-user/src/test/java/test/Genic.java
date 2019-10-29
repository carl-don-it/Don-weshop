package test;

import java.util.ArrayList;

/**
 * 无泛型的类赋给有泛型的类，可能有cast异常
 *
 * @author Walker_Don
 * @version V1.0
 * @date 2019年10月29日 上午 10:57
 */
//@SuppressWarnings("unchecked")
public class Genic {
    public static void main(String[] args) {
        //无泛型
        ArrayList arrayList = new ArrayList();
        arrayList.add("sf");
        arrayList.add(new Object());
        arrayList.add("sd");

        //赋给有泛型
        ArrayList<String> strings ;  //编译器可通过，但是运行转化就会有问题
        strings = arrayList;
        for (String s : strings) {
            System.out.println(s);//打印到第二个有异常
        }
    }
}
