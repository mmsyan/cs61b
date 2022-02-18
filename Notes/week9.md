## Extends casting and higer order function
### Phase1 类的基本继承
&emsp;&emsp;`implements`关键字完成了接口继承，接口继承强调应该做到什么，而不关注如何实现的。而实现继承则关注具体实现。例如类和类之间的继承关系。

&emsp;&emsp;我们用`extends`关键字来完成类与类之间的继承关系。例如，我们想要实现一个翻转的链表：`RotatingSLList`，我们并不需要复制`SLList`的全部代码再添加翻转方法。相反，我们直接使用类继承的关键字：`extends`

	public class RotatingSLList<Item> extends SLList<Item>{

	    public void rotateRight(){
	        Item remove = this.removeLast();
	        this.addFirst(remove);
	    }

	    public static void main(String[] args){
	        RotatingSLList<Integer> rst = new RotatingSLList<Integer>();
	        rst.addLast(10);rst.addLast(12);rst.addLast(15);rst.addLast(17);
	 
	        /** rst:10 -> 12 -> 15 -> 17 **/
	        rst.print();
	
	        rst.rotateRight();
	        /** rst:17 -> 10 -> 12 -> 15 **/
	        rst.print();
	    }
	}

&emsp;&emsp;当我们使用`extends`的时候，我们究竟继承了哪些东西呢？

- 所有的实例和静态变量
- 所有的方法(除了构造函数)
- 所有的内部类

&emsp;&emsp;请注意，我们没有继承构造函数！此外，`private`类型的成员和方法也不会被继承。
<br>
<br>

### Phase2 重写方法：`super`关键字
&emsp;&emsp;现在我们想要实现一个名为`vengefulSLList`的链表。它要保留那些被删除的结点。新增一个SLList类型的成员记录被删除的结点，准备打印这些结点的方法。现在我们需要重写`removeLast()`。

	public class VengefulSLList<Item> extends SLList<Item> {
    
	    SLList<Item> deletedItems;
	    
	    public void printLostItems(){
	        deletedItems.print();
	    }
	    
	    @Override
	    public Item removeLast(){
	        // to be finished
	    }
	}

&emsp;&emsp;先构思一下，重写后的`removeLast()`是什么样子的呢？首先它依旧会删除最后一个结点，不过它会将被删掉的结点存储在`deletedItems`当中。我们可以拷贝`SLList`当中的`removeLast()`的代码。但问题是，在父类当中这个方法使用了哨兵结点，修改了size，而这些都是`private`类型的成员变量，它们是不可以被子类访问的。并且。我们在使用子类的时候，也不希望还需要理解父类的行为才能重写代码。


&emsp;&emsp;我们可以使用`super`关键字。这个关键字帮助我们调用父类的方法。`super.removeLast()`意味着调用了父类的removeLast()方法，即使我们完全不知道父类是如何实现这个操作的。

	@Override
    public Item removeLast() {
        Item x = super.removeLast();
        deletedItems.addLast(x);
        return x;
    }

&emsp;&emsp;[更多关于super关键字的内容，戳这里。](https://docs.oracle.com/javase/tutorial/java/IandI/super.html)

<br>
<br>

### Phase3 构造函数的继承
&emsp;&emsp;正如我们之前所提到的，子类继承了父类的所有成员，包括实例和静态变量，方法，以及内部类。但是这种继承不包括构造函数。尽管构造函数不被继承，但是java规定，所有子类的构造函数中必须首先调用父类的某一个构造函数。

&emsp;&emsp;为了对java的这一特性获得某种直观理解，我们回忆一下`extends`关键字定义的是子类和父类之间的**is-a**关系。即，子类是某种特殊的父类。例如，`vengefulSLList`和`RotatingSLList`都是某种特殊的`SLList`。这意味着，子类首先得被设置为父类的某种形态，再设置子类独有的属性。例如，语文老师是老师的子类，那么一个语文老师在初始化的时候首先设置好老师的属性，然后补充上语文老师的属性。

&emsp;&emsp;我们可以显示地调用父类的构造函数。使用`super`关键字即可。例如，`super()`意味着调用父类的无参数构造函数。如果我们不做任何显示地调用（即在我们的代码中不使用`super`关键字），java会自动调用父类的无参数构造函数（而不会选择父类其它的带参构造函数）。子类的构造函数一定会先调用父类的构造函数，这种调用或许是显式的(由我们自己选择super关键字)，或许是隐式的(自动调用无参构造函数)。

	VengefulSLList(){
        super();
        deletedItems = new SLList<Item>();
    }

&emsp;&emsp;在刚刚那种情况下，无论我们是否加上了`super()`，效果都是一样的，都是调用父类无参数的构造函数。然而，有些时候我们需要使用父类其余的构造函数。例如，我们需要为`VengefulSLList()`加上第一个结点，那我们需要调用`SLList`的另一个构造函数。这个时候需要给`super()`带上参数。

    VengefulSLList(Item x){
        super(x);
        deletedItems = new SLList<Item>();
    }

&emsp;&emsp;总结一下，`super`关键字有两大功能：

- 访问父类被覆盖的成员/方法（成员的覆盖是不理智的操作，应当避免使用）
- 调用父类的构造函数

<br>
<br>

### Phase4 Object Class
&emsp;&emsp;[Object类](https://docs.oracle.com/javase/9/docs/api/java/lang/Object.html)

&emsp;&emsp;java中所有的类都是Object类的后代，或者说，java中所有的类都`extends`了Object类。即使有些类没有显示地使用`extends`关键字，它们仍然隐式地继承了Object类。例如，VengefulSLList显式地继承了SLList类，而SLList隐式地继承了Object类。这样做的好处是，所有的类都得到了某种基本保障。

&emsp;&emsp;我们从Object类当中继承了哪些成员呢？Object提供了每一个类都应当有的操作：`.equals(Object obj)`, `.hashCode()`, 以及 `.toString()`，还有一些在数据结构当中不重要的方法。以上提到的三个方法是所有类都有的，它们保障了一些重要功能。

- `.hashCode()`:用一个int来表示对象。如果equals方法下两个对象相等，那么必须返回相同的Int值。当然，如果equals方法下两个对象不等，也可以返回相同int值。hash本身是一种损耗信息的编码方式，不要求做到一一对应。

- `.equals(Object obj)`:比较两个对象是否相等。最初始的实现方式是，比较两个对象所装的地址是否相同。这个方法也可以被重构，从而进行新的比较。例如，可以根据类中某个特定值来比较二者是否相等，而忽略其余数据成员。

- `.toString()`：返回对象的某种字符型表示。最好将所有的子类都重写这一方法，以便调试方便。

<br>
<br>

### Phase5 封装机制
&emsp;&emsp;**封装**是面向对象程序设计的一项基本准则，也是我们作为程序员去对抗我们最大的敌人——复杂性的一种手段。在编写大型程序的时候，最难的不是编程语言的细节，也不是某些bug，而是整个程序在宏观层面的处理。当我们编写大型项目的时候，管理复杂性是我们所面临的一项主要工作。我们用来对抗复杂性的方法有很多，包括层级抽象，以及一个名为“为改变而设计”的概念。这个概念的中心思想是程序应当被设计为可替换的模块，更换其中某些模块不会影响其余模块和整个系统的稳定。另外，当管理大型项目的时候，**隐藏对方不需要知道的信息**也是非常重要的。

&emsp;&emsp;在计算机科学中，模块被定义为一系列方法的集合，这些方法被看作一个整体，去完成某项特定的任务。我们之前设计的链表类就符合这一定义。如果方法的具体实现在模块内部被封装好，唯一与模块打交道的方式是通过被记录了的接口，那么这个模块就是被封装好了的。

&emsp;&emsp;以我们在proj1a当中完成的`ArrayDeque`类为例。唯一与它打交道的方式是通过定义好了的接口，例如add和remove操作。然而，我们不需要知道有关数据结构如何实现的复杂细节（例如，我们在内部定义了一个数组，还用两个int型变量作为指针确定队列的范围）。同样的，在使用者的层面，不关注`LinkedDeque`的具体实现(例如究竟设置了几个sentinel节点)，只要这个队列能按照接口标准使用相关功能即可。

&emsp;&emsp;理想情况下，用户不应当观察到他们正在使用的数据结构的内部结构。幸运的是，java确实提供了这类机制。例如，使用`private`关键字。这些机制确保内部的复杂性不被外部世界观察到。

<br>
<br>

### Phase6 破坏封装
&emsp;&emsp;实现继承有可能会破坏封装。下面是父类`bark()`和`barkMany()`两个接口的两种实现方式。
	
	public void bark() {
	   System.out.println("bark");
	}
	public void barkMany(int N) {
	   	for (int i = 0; i < N; i += 1) {
	      	bark();  
	   	}
	}

&emsp;&emsp;第二种实现方式是先完成barkMany，再完成bark()

	public void bark() {
		barkMany(1);
	}
	public void barkMany(int N) {
		for (int i = 0; i < N; i += 1) {
		   System.out.println("bark");  
		}
	}

&emsp;&emsp;我们完成了父类Dog的实现继承，创建新的子类并重写barkMany方法。这个时候两种实现会造成截然不同的效果。所有的bark()和barkMany()都是实例方法，因此前面会有一个被忽略的`this.`。这造成了不同的结果。

	
	@Override
	public void barkMany(int N) {
	    System.out.println("As a dog, I say: ");
		for (int i = 0; i < N; i += 1) {
	       	bark();
		}
	}

</br>



### Phase7 类型检查和转换

&emsp;&emsp;接下来学习类型检查和转换。下面的代码是一个很好的案例。line1和line2都会通过编译，因为它们的静态类型是SLList，包含了addLast()和removeLast()两个接口。在运行过程当中，根据sl的动态类型为VengefulSLList，去寻找是否重写了代码，选择合适的执行。

&emsp;&emsp;然而line3和line4没有这么好运，它们在编译过程当中就会报错。由于**编译只关注静态类型**，而SLList没有printLostItems()方法，因此会报错。在line4的初始化过程当中，记住“子类一定是特殊的父类，所以父类变量可以装子类地址”，相反的，不能用父类给子类初始化。

	public static void main(String[] args) {
	   	VengefulSLList<Integer> vsl = 
	          new VengefulSLList<Integer>(9);
	   	SLList<Integer> sl = vsl;   
	
		sl.addLast(50);     //line1
	   	sl.removeLast();	 //line2	
	
	    sl.printLostItems();  //line3
	    VengefulSLList<Integer> vsl2 = sl; //line4
	}


&emsp;&emsp;下面两行代码帮助我们更好认知。sl是SLList类型的地址盒子，`new VengefulSLList<Integer>()`返回VengefulSLList类型的地址。子类是特殊的父类，因此VengefulSLList是特殊的SLList，第一行可以通过编译。第二行则不行。

	SLList<Integer> sl = new VengefulSLList<Integer>();  
	VengefulSLList<Integer> vsl = new SLList<Integer>();


&emsp;&emsp;接下来要介绍类型转换。类型转换使用`(类型)`的格式，欺骗编译器。例如第一个案例当中的sl。你确定在运行过程当中它的动态类型是VengefulSLList，那么你就使用类型转换，欺骗编译器不要报错。这种欺骗某种意义上是危险的，因为如果它的动态类型不是VengefulSLList，那么会报出运行时错误。
</br>



### Phase8 HOF预览

&emsp;&emsp;java当中有没有函数指针？很遗憾，没有，java只能指向对象。当我们想要把函数作为另外一个函数的参数的时候，例如，有一个函数`int apply(int x)`，另外一个函数`int do(Function ?, int y)`。我们不能把apply作为do的参数。因此我们创建一个类（一般是一个接口），让这个接口包含apply，再创建一个继承该接口的类实现apply，把类作为对象传递给do。


<br>
<br>
<br>