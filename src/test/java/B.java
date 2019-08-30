class A {
    public void m(A a) {
        System.out.println("AA");
    }
    public void m(D d) {
        System.out.println("AD");
    }
}
class B extends A {

    @Override
    public void m(A a) {
        System.out.println("BA");
    }
    public void m(B b) {
        System.out.println("BD");
    }
    public void test(){

    }
    public static void main(String[] args) {
        A a = new B();
        //父类引用指向子类对象
        //toString打印的是什么？
        //该对象实现类的类名 + @ +hashCode值。B@2626b418

        //传引用传的是什么？
        //传值对原来的值没有影响，传引用有影响。传值直接把原来的数拷贝一份，和原来的数没有关系了。
        // 传引用，比如对象是a，a指向一个地址，这个地址里放它的各种属性，
        // 你把a传过去就相当于告诉程序要修改那个地址里的东西，不改变a的值（a本身是个地址），
        // a还是指向那个地址，不过地址里的数据会发生改变，会影响到a.属性的值。
        System.out.println(a);
        B b = new B();
        System.out.println(b);
        C c = new C();
        D d = new D();
        a.m(a);
        a.m(b);
        a.m(c);
        a.m(d);
    }
}
class C extends B{}
class D extends B{}