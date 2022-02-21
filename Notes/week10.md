#  Lecture 10: Subtype Polymorphism vs. HoFs


## Phase1 静态类型与动态类型复习
&emsp;&emsp;下面总结一下，静态类型和动态类型的几条规则：

- 父类变量可以装载子类变量地址
- 编译器根据静态类型检查 方法调用 是否合理
- 被重写的方法根据动态类型来选择具体调用哪一个
- 类型转换的效果是暂时的，过了这个表达式还是会恢复原来的静态类型

## Phase2 子类多态
&emsp;&emsp;继承可以让我们使用父类已完成的代码，并且可以让我们在子类中重写方法或者增添新方法来实现修改。继承还使得利用多态去设计通用的数据结构和方法变得可能。

&emsp;&emsp;多态，顾名思义，意味着“多种形态”。其定义是为多种类型的实体提供单一的接口。在java，多态指的是对象可以拥有多种形式的类型。在面向对象程序设计中，多态指的是一个对象如何可以被看作它自己类的一个实例，被看作父类的一个实例，以及如何被看作父类的父类的一个实例。

&emsp;&emsp;考虑一个静态类型为`Deque`的变量deque，当我们使用deque.addFirst()的时候，调用会被推迟到执行时刻，根据deque的动态类型决定调用的方法。正如我们在上一章看到的，java根据动态选择机制决定调用哪个方法。

&emsp;&emsp;假如我们需要完成一个python程序：以字符串形式打印两个对象中较大的那一个。我们有两种方式：HoF(Higher-order-Function)以及多态。

&emsp;&emsp;下面是显示的HoF方式，它传入两个对象，并且传入两个函数（用于对象比较的compare函数，用于字符串形式打印的stringify函数）

	def print_larger(x, y, compare, stringify):
	    if compare(x, y):
	        return stringify(x)
	    return stringify(y)

&emsp;&emsp;还有一种则是子类型多态。在这种方式中，由对象做出比较以及打印的选择。largerThan()和str()都是根据x和y具体情况来实现的。

	def print_larger(x, y):
	    if x.largerThan(y):
	        return x.str()
	    return y.str()



<br>
<br>

## Phase3 Max方法
&emsp;&emsp;现在我们完成Max方法：它接受一个引用型数组（任何引用类型的数组都可以），并返回数组中最大的元素。

	public static Object max(Object[] items) {
	    int maxDex = 0;
	    for (int i = 0; i < items.length; i += 1) {
	        if (items[i] > items[maxDex]) {
	            maxDex = i;
	        }
	    }
	    return items[maxDex];
	}

&emsp;&emsp;上述程序最后会报错。因为程序无法理解`items[i] > items[maxDex]`。大于号对于两个引用类型来说意味着什么呢？这是不确定的。例如，下面的程序试图调用max函数，它接受了“一个狗的数组”。然而，它无法理解两只狗如何比较大小。

	public static void main(String[] args) {
	    Dog[] dogs = {new Dog("Elyse", 3), new Dog("Sture", 9), new Dog("Benjamin", 15)};
	    Dog maxDog = (Dog) max(dogs);
	    maxDog.bark();
	}

&emsp;&emsp;一种修改方式就是，专门为上述任务写一个`maxDog`函数。现在这个函数根据`Dog`类中的`size`成员来比较大小。

	public static Dog maxDog(Dog[] dogs) {
	    if (dogs == null || dogs.length == 0) {
	        return null;
	    }
	    Dog maxDog = dogs[0];
	    for (Dog d : dogs) {
	        if (d.size > maxDog.size) {
	            maxDog = d;
	        }
	    }
	    return maxDog;
	}

&emsp;&emsp;尽管现在我们可以顺利工作，但是我们放弃了创造通用的max函数的梦想，而是让`Dog`类自行定义用于比较的max函数。没有一个通用的函数的话，我们必须为每一个自己定义的类完成类似的工作。我们还要完成`maxCat`、`maxWhale`这样的函数。这将导致无意义的重复代码。
<br>
<br>
## Phase4 利用接口
&emsp;&emsp;导致这一现象的根本问题是`>`不能用于两个对象之间的比较。这是合理的，因为java怎么知道如何进行两个对象的比较呢？按照它们的字符串形式，还是按照size成员变量，还是按照其它的度量标准呢？在c++和python中，我们可以重载`>`来适应不同类型的比较。然而java不提供操作符的重载。不过，我们依旧可以选择让接口继承帮助我们完成工作。我们现在创造一个接口，所有实现了该接口的类，都有着一个用于比较的方法：

	public interface OurComparable {
	    public int compareTo(Object o);
	}

&emsp;&emsp;现在我们利用`Dog`类当中的size变量完成接口的代码实现。现在`Dog`类继承了`OurComparable`类。

	public class Dog implements OurComparable {
	    private String name;
	    private int size;
	
	    public Dog(String n, int s) {
	        name = n;
	        size = s;
	    }

	    public int compareTo(Object o) {
	        Dog uddaDog = (Dog) o;
	        if (this.size < uddaDog.size) {
	            return -1;
	        } else if (this.size == uddaDog.size) {
	            return 0;
	        }
	        return 1;
	    }
	}

&emsp;&emsp;接下来的操作是关键，我们利用继承来完成通用方法max(通用方法max是定义在工具类当中的)：

	public static OurComparable max(OurComparable[] items) {
	    int maxDex = 0;
	    for (int i = 0; i < items.length; i += 1) {
	        int cmp = items[i].compareTo(items[maxDex]);
	        if (cmp > 0) {
	            maxDex = i;
	        }
	    }
	    return items[maxDex];
	}

&emsp;&emsp;仔细阅读这段代码。返回类型是OurComparable，参数类型是OurComparable的数组，它们都是接口类型。接口类型作为上位关系，是可以容纳子类对象的地址的。而所有的子类，都实现了OurComparable接口，因此所有子类对象必然已经实现了`compareTo`方法。如果要进行猫和狗的比较，那么compareTo接口当中的类型转换就会失败。

<br>
<br>

## Phase5 更好的选择：Comparable接口
&emsp;&emsp;我们刚刚通过创建`OurComparable`接口，让所有需要被比较的类继承该接口，实现`compareTo`方法，这样定义在`OurComparable`接口当中的`max`方法就可以完成工作了。（通过继承，max知道哪些类是可以比较的——所有继承了接口的类才可以比较；以及如何比较——继承了接口的类都实现了compareTo方法）。但是OurComparable接口有两大问题：

- 需要进行糟糕的类型转换
- 这个接口是我们创建的，不具备通用性。不能保证别人创建的类也使用了这一接口

&emsp;&emsp;当然，我们有一个更好的选择，使用java提供的Comparable接口。这一接口采用泛型，使用起来非常方便。而且这个接口早已被各大工程项目采纳，具有统一性。

	public interface Comparable<T> {
		public int compareTo(T obj);
	}

<br>
<br>
## Phase6 Comparator
&emsp;&emsp;狗有大小，也有名字。之前我们都根据狗的大小来进行比较。假如我们需要两种排序方式，一种根据狗的大小，一种根据狗的名字。这个时候，Comparable接口就不一定能很好表现了，因为我们不能重写compareTo方法两次。我们意识到还有一种方法，就是传递函数指针。

&emsp;&emsp;Java没有函数指针，但是可以把函数放到类当中，传递类进去。于是出现了Comparator类。

	public class SizeComparator implements Comparator<Dog> {
        
        @Override
        public int compare(Dog o1, Dog o2) {
            return o1.size - o2.size;
        }
    }


    public class NameComparator implements Comparator<Dog> {

        // String的比较方式由compareTo定义
        @Override
        public int compare(Dog o1, Dog o2) {
            return o1.compareTo(o2);
        }
    }

&emsp;&emsp; 一般来说，会将Comparator类设置为私有属性，提供公共接口`getXXXComparator()`来返回一个Comparator类的实例。

	Comparator<Dog> c = Dog.getNameComparator();
	c.compareTo(d1, d2);

