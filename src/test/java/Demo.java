
public class Demo{
    public static void main(String[] args) {
        A a=new A("j");
        System.out.println(a.name);
        change(a);
        System.out.println(a.name);
    }
    public static void change(A a){
        a.name="jiang";
    }
    static class A{
        String name;
        A(String name){
            this.name=name;
        }
    }

}
