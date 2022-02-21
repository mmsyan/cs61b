# Lecture 14: Exceptions, Iteration

## Phase1 List总结
&emsp;&emsp;在本节我们学习使用java的内置数据结构：`List`和`Set`，并且去构造我们自己的数据结构`ArraySet`。

&emsp;&emsp;在cs61b，我们已经创建了两种类型的链表：`AList`和`SLList`。我们也创建了链表类接口`List61B`，它用于指定链表需要实现的操作。

&emsp;&emsp;我们相当于从头开始创造了链表。然而，java提供了内置的`List`接口以及该接口的若干实现，例如`ArrayList`。记住，`List`是一个接口，因此我们不能实例化它。我们只能实例化它的具体实现。如果要使用java提供的链表，记得使用`import`导入相关库文件。
<br>
<br>

## Phase2 set(集合)
&emsp;&emsp;集合是一系列的无重复无顺序元素。无重复的意思是，每一个元素只能有一个副本。java也内置了`Set`接口以及若干实现，例如`HashSet`。如果要使用它们记得导入库文件。

&emsp;&emsp;我们的目标是以数组为核心数据结构创造自己的集合`ArraySet`。它要实现三个方法：

- `add(value)`：如果value不在集合当中则将value加入集合当中。集合的增添操作比映射和链表都要复杂，因为要保证元素的唯一性。
- `contains(value)`：检查集合中是否包含该元素。
- `size()`：返回集合当中的元素个数。

&emsp;&emsp;下面是给出的实现。

	import java.util.Iterator;

	public class ArraySet<T> implements Iterable<T> {
	    private T[] items;
	    private int size; // the next item to be added will be at position size
	
	    public ArraySet() {
	        items = (T[]) new Object[100];
	        size = 0;
	    }
	
	    /* Returns true if this map contains a mapping for the specified key.
	     */
	    public boolean contains(T x) {
	        for (int i = 0; i < size; i += 1) {
	            if (items[i].equals(x)) {
	                return true;
	            }
	        }
	        return false;
	    }
	
	    /* Associates the specified value with the specified key in this map. */
	    public void add(T x) {
	        if (contains(x)) {
	            return;
	        }
	        items[size] = x;
	        size += 1;
	    }
	
	    /* Returns the number of key-value mappings in this map. */
	    public int size() {
	        return size;
	    }
	}
<br>
<br>

## Phase3 抛出异常
&emsp;&emsp;上一节当中我们对于`ArraySet`的实现有一个问题。如果我们往集合当中添加`null`，我们会得到一个`NullPointerException`。问题出在contains语句，对于待检查的元素，它会让集合中的每一个元素都调用.equals方法。如果集合中某个元素为`null`，那么这个元素是无法调用equals方法的，这造成了无指针类型的异常。

&emsp;&emsp;异常会导致正常执行流终止。事实上我们也可以选择抛出异常。在python当中我们使用关键字`raise`来抛出异常。在java当中，异常也是对象，因此我们使用下列格式抛出：
>throw new ExceptionObject(parameter1, ...)

&emsp;&emsp;我们的处理方式是禁止用户向集合当中添加null元素。如果用户有这样的行为，我们会抛出`IllegalArgumentException`异常，该异常有一个字符串类型参数（用于详细描述异常信息，例如，我们要提醒用户不要添加null，可以加上"can't add null!"）

	/* Associates the specified value with the specified key in this map.
	   Throws an IllegalArgumentException if the key is null. */
	public void add(T x) {
	    if (x == null) {
	        throw new IllegalArgumentException("can't add null");
	    }
	    if (contains(x)) {
	        return;
	    }
	    items[size] = x;
	    size += 1;
	}

&emsp;&emsp;不管怎样我们会得到一个异常。为什么抛出异常是好的行为呢？

- 我们可以控制代码：我们准确地知道程序何时停止运行
- 异常类型和描述错误的字符串可以帮助使用者知道问题出在哪里

&emsp;&emsp;当然，程序不崩溃的话会更好。我们有两个选择：让add方法忽视添加null元素；或者在contains方法中分类讨论，对于null另外处理。无论我们做什么选择，关键是让用户知道会发生什么。这就是为什么程序的文档描述（包括你写的关于方法的注释！）非常重要。
<br>
<br>

## Phase4 强化循环
&emsp;&emsp;对于java内置的set，我们可以使用更加简洁的强化循环。但如果想让我们自己的ArraySet使用强化循环则会报错。我们如何启用该功能呢？

	Set<String> s = new HashSet<>();
	s.add("Tokyo");
	s.add("Lagos");
	for (String city : s) {
	    System.out.println(city);
	}

&emsp;&emsp;首先要理解，当使用强化循环的时候，究竟发生了什么。上面的代码可以翻译成另外一种形式：

	Set<String> s = new HashSet<>();
	...
	Iterator<String> seer = s.iterator();
	while (seer.hasNext()) {
	    String city = seer.next();
	    ...
	}

&emsp;&emsp;我们揭开魔法外壳，深入理解强化循环的本质。这里的关键是一个叫迭代器的对象。我们通过迭代器方法`iterator()`来获得迭代器。这个方法没有任何参数，返回一个迭代器类型的实例对象。这个方法会被具体的类重载，因此返回类型是各自具体类自己实现的迭代器类型的子类。而迭代器类又包含了两个重要的方法：`hasNext()`和`next()`。前者判断迭代是否继续，返回布尔值；后者返回下一个迭代值。

<br>
<br>

## Phase5 迭代器
&emsp;&emsp;在这一节，我们讨论如何让一个类具有迭代功能。我们首先考察下面的代码，看看编译器需要知道哪些东西才能让这段代码通过编译。

	List<Integer> friends = new ArrayList<Integer>();
	Iterator<Integer> seer = friends.iterator();
	
	while(seer.hasNext()) {
	    System.out.println(seer.next());
	}

- friends变量的静态类型是List。如果能调用friends.iterator()，那么List接口必须实现iterator()方法。
- seer变量的静态类型是Iterator，如果调用了seer.haxNext()和seer.next()，那么Iterator必须实现这两个方法。

&emsp;&emsp;任何具备迭代功能的具体类，都需要继承“可迭代接口”：`iterable`接口。java内置的数据结构类，如ArrayList、HashSet都已经继承好了该接口。`iterable`接口只有一个签名类型为` Iterator<T> iterator();`的方法声明。也就是说，如果想让我们自己写得类具有迭代功能，只需要两个操作：继承iterable接口，重写iterator()方法。

&emsp;&emsp;继承接口非常容易，在类声明的后面补上`implements Iterable<T>`即可。关键是如何重写iterator()方法？这个方法不接受参数，返回一个迭代器接口类型的对象。由于iterator只是接口类型，因此我们需要根据具体数据结构的实现，继承iterator接口写一个MyIterator类。这个类通常需要三个方法：构造函数；hasNext()和next()。并且根据数据结构的具体实现，需要若干数据成员来完成迭代。

	private class ArraySetIterator implements Iterator<T> {
	    private int wizPos;
	
	    public ArraySetIterator() {
	        wizPos = 0;
	    }
	
	    public boolean hasNext() {
	        return wizPos < size;
	    }
	
	    public T next() {
	        T returnItem = items[wizPos];
	        wizPos += 1;
	        return returnItem;
	    }
	}

<br>
<br>