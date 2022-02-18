## Phase1 Intro and Interfaces
### Phase1.0 两个longest方法？
&emsp;&emsp;我们已经实现了两种类型的链表：SLList和AList。尽管它们的实现方式很不一样，但是提供的接口（方法）是一模一样的。例如，SLList和ALList都可以在尾部添加节点(`addLast()`)。现在我们决定再创建一个工具类，用来提供使用链表的新方法：`public static String longest(?)`。它以一个String类型的链表实例作为参数，返回该链表中长度最大的String。注意，我们要实现两个版本：SLList和AList。首先是SLList版本。

	public static String longest(SLList<String> list){
		int maxDex = 0;
		for(int i = 0; i < list.size(); i++){
			String longestString = list.get(maxDex);
			String thisString = list.get(i);
			if(thisString.length() > longestString.length())
				maxDex = i;
		}
		return list.get(maxDex)
	}

&emsp;&emsp;然后是AList版本。

	public static String longest(AList<String> list){
		int maxDex = 0;
		for(int i = 0; i < list.size(); i++){
			String longestString = list.get(maxDex);
			String thisString = list.get(i);
			if(thisString.length() > longestString.length())
				maxDex = i;
		}
		return list.get(maxDex)
	}

&emsp;&emsp;我们注意到的第一个事情是，`WordUtils` 类有两个同名方法。这在java中是允许的，被称作方法重载(*method loading*: 拥有相同的方法名，但是接受不同的参数)。在编译的时候，java会根据参数类型选择合适的`longest`方法。如果参数是ALList类型，那么使用第一个longest方法，否则使用第二个。这是java的新语法重载。


&emsp;&emsp;第二件事情是，这是一种糟糕的处理方式：尽管重载是允许的，但并不是合适的。我们给出三条理由：

- 我们的源代码文件因此变得冗长，重复了不必要的代码
- 代码变得难以维护，因为我们需要同时看管两处代码
- 难以拓展。假如我们创建了新的链表类，例如DList，难道又要写第三个`longest`吗？
<br>
<br>

### Phase1.1 上位关系与下位关系
&emsp;&emsp;先问两个问题：把大象装进冰箱需要哪些步骤？把猫装进冰箱又需要哪些步骤？答案分别如下：

&emsp;&emsp;装大象：
		
- 打开冰箱门
- 把大象放进冰箱
- 关上冰箱门

&emsp;&emsp;装猫：
		
- 打开冰箱门
- 把猫放进冰箱
- 关上冰箱门

&emsp;&emsp;在日常生活中，我们将大象和猫称作动物的“下位关系”，而把动物称作大象和猫的“上位关系”。而上位关系和下位关系组成了“继承”。在java类当中，也存在着这样的关系。类可以被划分为父类和子类。
<br>
<br>

### Phase1.2 Keywords in java:`interface` and `implement`
&emsp;&emsp;在[Java Interface](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html)中，我们可以找到java官方文档关于`interface`的定义。

> In the Java programming language, an interface is a reference type, similar to a class, that can contain only constants, method signatures, default methods, static methods, and nested types. Method bodies exist only for default methods and static methods. Interfaces cannot be instantiated—they can only be implemented by classes or extended by other interfaces. 

- interface是一种引用类型。
- interface只能包括常量数据成员，方法签名，默认方法，静态方法，内部类。常数和方法都应该是public类型
- 只有默认方法和静态方法能够有方法体，其余方法只能有一个声明
- interface不能被实例化
- interface只能被其余类implements，或者被其余接口extend

&emsp;&emsp;在java中实现这样的上位关系和下位关系，我们需要两步：

- step1：定义一个接口，它规定了链表应该做什么（而不包括如何做）
- step2：让下位关系的类使用并实现这个接口

&emsp;&emsp;现在，我们可以创造一个名为`List61B`的接口。
	
	public interface List61B<Item> {
	    public void addFirst(Item x);
	    public void addLast(Item y);
	    public Item getFirst();
	    public Item getLast();
	    public Item removeLast();
	    public Item get(int index);
	    public void insert(Item x, int position);
	    public int size();
	}

&emsp;&emsp;这样一来，我们可以修改`WordUtils`类。我们不再需要两个`longest`方法（它们有着相同的代码块，而仅仅是接受的参数类型不同！）现在我们将参数类型修改为`List61B`类，就可以完成工作了。

	
	public static String longest(List61B<String> list){
		int maxDex = 0;
		for(int i = 0; i < list.size(); i++){
			String longestString = list.get(maxDex);
			String thisString = list.get(i);
			if(thisString.length() > longestString.length())
				maxDex = i;
		}
		return list.get(maxDex)
	}
<br>
<br>
### Phase1.3 override
&emsp;&emsp;如果子类有一个方法，这个方法的签名和上位接口类中某个方法的签名一模一样，我们就说这个子类`override`这个接口。我们也可以说，这个方法被`override`了。`override`发生在类继承过程当中。

&emsp;&emsp;如果我们尝试实现某个接口，我们需要在子类的方法前面加上`@Override`的标签。这是一个好习
惯。这个标签的作用是，如果你尝试override一个“不存在于接口当中的方法”，那么编译不会通过，帮助你迅速找到错误。

&emsp;&emsp;overload则不同，它是一个类当中有着同名的方法，但这些方法的签名不一样。
<br>
<br>

### Phase1.4 Interface Inheritance
&emsp;&emsp;当我们使用关键字`implements`的时候，我们实际上是在完成“继承接口”的操作。接口的含义是，一组方法的签名。它定义了继承者“应该做到的事情”。如果某个子类没有实现接口中全部的方法，那么编译会报错。例如，我们实现了锁的接口，它规定锁必须能够开锁和关锁。我们分别实现了“电子密码锁”、“金属钥匙锁”、“面部识别锁”，无论它们的其余细节如何花里胡哨，开锁和关锁都是必须做到的事情。如果发明了一种新的“彩虹锁”，但是它却不能开关锁，那么这把锁就没有任何意义。

&emsp;&emsp;接口只规定了**应该做什么**，没有规定**怎样去做**。我们需要在子类中完成具体实现。例如，电子密码锁依靠密码来完成开锁和关锁，这涉及到密码的存储、确认。金属钥匙锁则涉及到钥匙的保管，形状的吻合等等。接口不关注这些细节，它只强调锁应该做到什么，不在乎怎么实现的。

&emsp;&emsp;接口继承是可以多级继承的。AList是List61B的子类，而List61B则是Collection的子类。

&emsp;&emsp;此外，`GORE`法则也得到了拓展。`GORE`法则强调了两点：

- 使用x = y或者传递参数的时候，是在复制bit。如果x和y是基本类，代表着它们的值相等。如果x和y是复合类，复合类都是64bit的内存盒子，装着地址。
- 64bit的内存盒子是有类型的，不能装其余复合类的地址。

&emsp;&emsp;由于接口继承是一种**is-a**关系，意味着子类其实是（某种特殊的）父类。因此，上位关系的“内存盒子”可以容纳下位关系的地址。`List61B<String> a = new AList<String>()`是可以通过编译的。
<br>
<br>

### Phase1.5 default method
&emsp;&emsp;我们之前学习了第一种继承：“接口继承”(interface inheritance)。在这种继承当中，子类得到了父类的方法声明，但没有得到任何具体实现的代码，它需要自己完成这些代码。java提供了第二种继承方式：“实现继承”(implementation inheritance)，通过这种继承，子类可以得到父类提供的具体代码（即已经实现好了的方法）。使用实现继承需要关键字`default`。

	default public void print() {
    	for (int i = 0; i < size(); i += 1) {
        	System.out.print(get(i) + " ");
    	}
    	System.out.println();
	}

&emsp;&emsp;以上代码是我们在`List61B`中实现的打印方法，它能够打印链表中所有结点的值。关于这个打印方法我们有一些可以讨论的话题。首先是，我们并不知道具体的链表实现。比如，是否有哨兵结点呢？或者是说，是否依靠数组实现呢？这些我们都不知道。因此在实现`print()`方法时，我们只能通过`get(int index)`取到相对应的结点。因为`get`方法是接口定义了的，每一个链表都必须实现的。第二个问题是`default method`的效率问题。由于不能结合具体的链表结构，默认方法很可能是低效率的。例如，`print()`对于`SLList`来说就是低效的，我们本来只需要完整的遍历一次链表就能够打印完成，但在这里每一个结点都是get方法获得的，而每一个get方法都需要遍历链表，造成了大量的时间浪费。

&emsp;&emsp;因此，我们很多时候不希望使用接口提供的方法实现。如果接口给出的是默认方法，我们可以通过`@Override`标签在子类当中重写方法。注意，如果没有使用`@Override`标签，那么java还是会选择接口提供的默认方法！
	
	@Override
	public void print() {
    	for (Node p = sentinel.next; p != null; p = p.next) {
        	System.out.print(p.item + " ");
    	}
	}

<br>
<br>

### Phase1.6 dynamic method selection
&emsp;&emsp;我们继续探讨java是如何选择合适的方法的。我们已经知道，上位关系的内存盒子可以存储下位关系的地址，例如`List61B<int> lst = new AList<int>()`。那么，如果使用`lst.print()`，会选择哪个`print()`呢？答案是会选择`AList`类型的`print()`，而不是选择接口提供的默认方法。

&emsp;&emsp;java中的变量有两种类型：第一种是静态类型(编译类型)，在变量**声明**的时候被确定。第二种是动态类型(运行类型)，在变量**初始化**的时候被确定。变量只能被声明一次，因此静态类型是唯一的。然而初始化可以多次进行，因此动态类型是可以变化的，java会根据动态类型选择合适的方法。

&emsp;&emsp;`lst`是一个接口类型的盒子，`List61B`是它的静态类型。但是它所指向的是一个`SLList`类型的对象。由于动态类型分配（动态的意思是，`lst`可能指向不同类型的对象。只要是继承了接口的链表类型都可以被`lst`所指向），`lst`指向`AList`类型的对象，因此会使用`AList`的方法。

<br>
<br>