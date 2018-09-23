package com.merlin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.merlin.entity.Person;

/**
 * 
使用方式
方法引用主要有如下三种使用情况:（1）. 类：：实例方法;（2）. 类：：静态方法;（3）. 对象：：实例方法
其中后两种情况等同于提供方法参数的lambda表达式，
如:System.out::println 等同于(x)->System.out.println(x),
  Math::pow 等同于(x,y)->Math.pow(x,y).
第一种中，第一个参数会成为执行方法的对象，String::compareToIgnoreCase)等同于(x,y)->x.compareToIgnoreCase(y)
此外，方法引用还可以使用this::methodName及super::methodName表示该对象或者其父类对象中的方法


构造器引用
构造器引用同方法引用类似，同样作用于函数式接口
构造器引用的语法为 ClassName::new
啥也不说，线上代码
List<String> labels = Arrays.asList("aaa","bbb","ccc","ddd");
Stream<Button> buttonStream = labels.stream().map(Button::new);
如上代码所示，map方法内需要一个Function对象
<R> Stream<R> map(Function<? super T, ? extends R> mapper);

 * @author aspire
 *
 */
public class MainTest {
	
	private static List<Person> javaProgrammers = new ArrayList<Person>() {
		  {  
		    add(new Person("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));  
		    add(new Person("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));  
		    add(new Person("Floyd", "Donny", "Java programmer", "male", 33, 1800));  
		    add(new Person("Sindy", "Jonie", "Java programmer", "female", 32, 1600));  
		    add(new Person("Vere", "Hervey", "Java programmer", "male", 22, 1200));  
		    add(new Person("Maude", "Jaimie", "Java programmer", "female", 27, 1900));  
		    add(new Person("Shawn", "Randall", "Java programmer", "male", 30, 2300));  
		    add(new Person("Jayden", "Corrina", "Java programmer", "female", 35, 1700));  
		    add(new Person("Palmer", "Dene", "Java programmer", "male", 33, 2000));  
		    add(new Person("Addison", "Pam", "Java programmer", "female", 34, 1300));  
		  }  
	};

	private static List<Person> phpProgrammers = new ArrayList<Person>() {

		{  
		    add(new Person("Jarrod", "Pace", "PHP programmer", "male", 34, 1550));  
		    add(new Person("Clarette", "Cicely", "PHP programmer", "female", 23, 1200));  
		    add(new Person("Victor", "Channing", "PHP programmer", "male", 32, 1600));  
		    add(new Person("Tori", "Sheryl", "PHP programmer", "female", 21, 1000));  
		    add(new Person("Osborne", "Shad", "PHP programmer", "male", 32, 1100));  
		    add(new Person("Rosalind", "Layla", "PHP programmer", "female", 25, 1300));  
		    add(new Person("Fraser", "Hewie", "PHP programmer", "male", 36, 1100));  
		    add(new Person("Quinn", "Tamara", "PHP programmer", "female", 21, 1000));  
		    add(new Person("Alvin", "Lance", "PHP programmer", "male", 38, 1600));  
		    add(new Person("Evonne", "Shari", "PHP programmer", "female", 40, 1800));  
		  }  
	};  

	public static void main0(String[] args) {
		
		String[] atp = {"Rafael Nadal", "Novak Djokovic",  
			       "Stanislas Wawrinka",  
			       "David Ferrer","Roger Federer",  
			       "Andy Murray","Tomas Berdych",  
			       "Juan Martin Del Potro"};  
			List<String> players =  Arrays.asList(atp);  
			  
			// 以前的循环方式  
			for (String player : players) {  
			     System.out.print(player + "; ");  
			}  
			  
			// 使用 lambda 表达式以及函数操作(functional operation)  
			players.forEach((player) -> System.out.print(player + "; "));  
			   
			// 在 Java 8 中使用双冒号操作符(double colon operator)  
			players.forEach(System.out::println);  
	}
	
	/**
	 * 使用lambdas 来实现 Runnable接口 
	 * @param args
	 */
	public static void main2(String[] args) {
		// 1.1使用匿名内部类  
		new Thread(new Runnable() {  
		    @Override  
		    public void run() {  
		        System.out.println("Hello world !");  
		    }  
		}).start();  
		  
		// 1.2使用 lambda expression  
		new Thread(() -> System.out.println("Hello world !")).start();  
		  
		// 2.1使用匿名内部类  
		Runnable race1 = new Runnable() {  
		    @Override  
		    public void run() {  
		        System.out.println("Hello world !");  
		    }  
		};  
		  
		// 2.2使用 lambda expression  
		Runnable race2 = () -> System.out.println("Hello world !");
		   
		// 直接调用 run 方法(没开新线程哦!)  
		race1.run();  
		race2.run();  
	}
	
	/**
	 * 使用Lambdas排序集合
	 * @param args
	 */
	public static void main3(String[] args) {
		
		String[] players = {"Rafael Nadal", "Novak Djokovic",   
			    "Stanislas Wawrinka", "David Ferrer",  
			    "Roger Federer", "Andy Murray",  
			    "Tomas Berdych", "Juan Martin Del Potro",  
			    "Richard Gasquet", "John Isner"};  
			   
		// 1.1 使用匿名内部类根据 name 排序 players  
		Arrays.sort(players, new Comparator<String>() {  
		    @Override  
		    public int compare(String s1, String s2) {  
		        return (s1.compareTo(s2));  
		    }  
		}); 
		
		// 1.2 使用 lambda expression 排序 players  
		Comparator<String> sortByName = (String s1, String s2) -> (s1.compareTo(s2));  
		Arrays.sort(players, sortByName);  
		  
		// 1.3 也可以采用如下形式:  
		Arrays.sort(players, (String s1, String s2) -> (s1.compareTo(s2))); 
	}
	
	public static void main4(String[] args) {
		
		
		System.out.println("所有程序员的姓名:");  
		javaProgrammers.forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));
		System.out.println("");
		phpProgrammers.forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));
		
		/*
		 * forEach方法,增加程序员的工资5%
		 */
		System.out.println("给程序员加薪 5% :");  
		Consumer<Person> giveRaise = e -> e.setSalary(e.getSalary() / 100 * 5 + e.getSalary());  
		  
		javaProgrammers.forEach(giveRaise);  
		phpProgrammers.forEach(giveRaise); 
		
		/*
		 * 用的方法是过滤器filter() ,让我们显示月薪超过1400美元的PHP程序员
		 */
		System.out.println("下面是月薪超过 $1,400 的PHP程序员:");
		phpProgrammers.stream()  
		          .filter((p) -> (p.getSalary() > 1400))  
		          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName())); 
		System.out.println("");
		
		/*
		 * 也可以定义过滤器,然后重用它们来执行其他操作
		 */
		Predicate<Person> ageFilter = (p) -> (p.getAge() > 25);  
		Predicate<Person> salaryFilter = (p) -> (p.getSalary() > 1400);  
		Predicate<Person> genderFilter = (p) -> ("female".equals(p.getGender()));  
		  
		System.out.println("下面是年龄大于 24岁且月薪在$1,400以上的女PHP程序员:");  
		phpProgrammers.stream()  
		          .filter(ageFilter)  
		          .filter(salaryFilter)  
		          .filter(genderFilter)  
		          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));  
		System.out.println("");
		// 重用filters  
		System.out.println("年龄大于 24岁的女性 Java programmers:");  
		javaProgrammers.stream()  
		          .filter(ageFilter)  
		          .filter(genderFilter)  
		          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName())); 
		
		System.out.println("");
		
		/*
		 * 使用limit方法,可以限制结果集的个数:
		 */
		System.out.println("最前面的3个 Java programmers:");  
		javaProgrammers.stream()  
		          .limit(3)  
		          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));  
		  
		System.out.println("");
		System.out.println("最前面的3个女性 Java programmers:");  
		javaProgrammers.stream()  
		          .filter(genderFilter)  
		          .limit(3)  
		          .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName())); 	
	}
	
	/**
	 * 在stream中排序
	 * @param args
	 */
	public static void main5(String[] args) {
		System.out.println("根据 name 排序,并显示前5个 Java programmers:");  
		List<Person> sortedJavaProgrammers = javaProgrammers  
		          .stream()
		          .sorted((p, p2) -> (p.getFirstName().compareTo(p2.getFirstName())))  
		          .limit(5)
		          .collect(Collectors.toList());  
		  
		sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));  
		   
		System.out.println("根据 salary 排序 Java programmers:");  
		sortedJavaProgrammers = javaProgrammers  
		          .stream()  
		          .sorted((p, p2) -> (p.getSalary() - p2.getSalary()))  
		          .collect(Collectors.toList());  
		  
		sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));  
		
		/*
		 * 最低和最高的薪水感兴趣,比排序后选择第一个/最后一个 更快的是min和max方法
		 */
		System.out.println("工资最低的 Java programmer:");  
		Person pers = javaProgrammers  
		          .stream()  
		          .min((p1, p2) -> (p1.getSalary() - p2.getSalary()))  
		          .get(); 
		  
		System.out.printf("Name: %s %s; Salary: $%,d.", pers.getFirstName(), pers.getLastName(), pers.getSalary());
		System.out.println("工资最高的 Java programmer:");  
		Person person = javaProgrammers  
		          .stream()  
		          .max((p, p2) -> (p.getSalary() - p2.getSalary()))  
		          .get(); 
		  
		System.out.printf("Name: %s %s; Salary: $%,d.", person.getFirstName(), person.getLastName(), person.getSalary()); 
		System.out.println("");
	}
	
	/**
	 * 结合 map 方法,我们可以使用 collect 方法来将我们的结果集放到一个字符串,一个 Set 或一个TreeSet中
	 * @param args
	 */
	public static void main6(String[] args) {
		System.out.println("将 PHP programmers 的 first name 拼接成字符串:");  
		String phpDevelopers = phpProgrammers  
		          .stream()  
		          .map(Person::getFirstName)  
		          .collect(Collectors.joining(";")); // 在进一步的操作中可以作为标记(token)     
		System.out.printf("%s",phpDevelopers);  
		System.out.println("\\n");
		System.out.println("将 Java programmers 的 first name 存放到 Set:");  
		Set<String> javaDevFirstName = javaProgrammers  
		          .stream()  
		          .map(Person::getFirstName)  
		          .collect(Collectors.toSet());  
		 
		System.out.println("将 Java programmers 的 first name 存放到 TreeSet:");  
		TreeSet<String> javaDevLastName = javaProgrammers  
		          .stream()  
		          .map(Person::getLastName)  
		          .collect(Collectors.toCollection(TreeSet::new));  
		
		
		/*
		 * Streams 还可以是并行的(parallel)
		 */
		System.out.println("计算付给 Java programmers 的所有money:");  
		int totalSalary = javaProgrammers  
//		          .parallelStream() 
				  .stream()
		          .mapToInt(p -> p.getSalary())  
		          .sum();
		System.out.println(totalSalary);
	}
	
	
	/**
	 * 使用summaryStatistics方法获得stream 中元素的各种汇总数据
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);  
		IntSummaryStatistics stats = numbers  
		          .stream()  
		          .mapToInt((x) -> x)  
		          .summaryStatistics();  
		
		System.out.println("List的个数 : " + stats.getCount());
		System.out.println("List中最大的数字 : " + stats.getMax());  
		System.out.println("List中最小的数字 : " + stats.getMin());  
		System.out.println("所有数字的总和   : " + stats.getSum());  
		System.out.println("所有数字的平均值 : " + stats.getAverage());  
	}
}
