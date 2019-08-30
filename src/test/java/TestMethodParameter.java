import org.junit.Test;

public class TestMethodParameter {

    @Test
    public void test(){
        Employee employee1 = new Employee("zhangsan");
        Employee employee2 = new Employee("lisi");

        Employee temp = employee1;
        employee1 = employee2;
        employee2 = temp;
        System.out.println("employee1:" + employee1.getName() + "|" + "employee2:" + employee2.getName());
    }

    @Test
    public void testSwap(){
        Employee employee1 = new Employee("zhangsan");
        Employee employee2 = new Employee("lisi");

        swap(employee1, employee2);
        System.out.println("employee1:" + employee1.getName() + "|" + "employee2:" + employee2.getName());
        System.out.println("----------------------");
        change(employee1,employee2);
        System.out.println("employee1:" + employee1.getName() + "|" + "employee2:" + employee2.getName());
    }

    static void swap(Employee a, Employee b){
        Employee temp = a;
        a = b;
        b = temp;
        System.out.println("a:" + a.getName() + "|" + "b:" + b.getName());
    }
    static void change(Employee a, Employee b){
        a.setName("jiang");
        b.setName("yang");
        System.out.println("a:" + a.getName() + "|" + "b:" + b.getName());
    }
}
