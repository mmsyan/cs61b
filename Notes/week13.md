# Lecture 13: Generics, Conversion, Promotion

## Phase1 Java变量的类型转换(上)
&emsp;&emsp;主要介绍java的两种类型转换：一种是基本类与包裹类的互相转换，另一种是基础类中“窄类”向宽类的转换。本节介绍第一种转换。

&emsp;&emsp;首先复习一下，java当中一共有8种基本数据类型：。byte,short,int,long;float,double;boolean;char。除此之外的所有类型都是引用类。现在加入新知识，每一种基本数据类型都有其对应的包裹类型：Byte,Short,Integer,Long,Float,Double,Boolean,Character。在泛型编程中，我们可以采用`<T>`这样的符号来指定不同的**引用**类型。然而，T必须是引用类型，因此8种基本类型是不能使用的。

&emsp;&emsp;例如，`LinkedListDeque<int>`会引发编译错误，因此我们需要使用基本类型所对应的包裹类型，例如`LinkedListDeque<Integer>`。现在的问题是，如何进行基本类型和包裹类型的转换呢？我们有显示和隐式两种方案。

- 显式转换
	- primitive -> wrapper( eg:`Integer X = new Integer(10);`)
	- wrapper -> primitive( eg:`int x = Integer.valueOf(X)`;)
	
- 隐式转换
	- primitive -> wrapper(Auto-boxing)
	- wrapper -> primitive(Auto-unboxing)

&emsp;&emsp;在绝大多数情况下，机器会自动完成基本类型与包裹类型的相互转换。例如`l.add(5)`并不需要你写成`l.add(new Integer(5))`。但是要注意三点：
 
1. 数组不会完成这种自动转换。如果在包裹类型的数组使用基本类型，或者在基本类型的数组使用包裹类型，会报错
2. 自动转换会影响代码效率
3. 包裹类型非常占用内存
<br>
<br>

## Phase2 Java变量的类型转换(下)
&emsp;&emsp;如果是窄类型转换为宽类型，可以自动完成。

&emsp;&emsp;如果是宽类型转换为窄类型，需要显式转换。

&emsp;&emsp;[我们可以获取更多关于类型转换的知识。](https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html)

<br>
<br>



## Phase3 Immutability
&emsp;&emsp;有关不变性的概念可能是我们永远也不会知道的事物，但是如果你意识到它的存在，它能帮助你简化很多问题。

&emsp;&emsp;不可改变的数据类型是指这样一种类型：当它的实例被实例化之后，不能够以任何可见的方式被改变。

&emsp;&emsp;例如，java当中的`String`类就是不可改变的。无论怎样，当你拥有了一个String类型的实例变量，你可以调用任何有关它的方法，但是不会改变它。事实上，任何有关String变量的方法调用都不会修改原来的变量，只是返回一个新的String对象。
<br>
<br>


## Phase4 创造另一个泛型类：map
&emsp;&emsp;现在我们已经创造了泛型链表，例如`DLList`和`AList`。我们转向另外一种数据结构：映射。映射能让我们将“键”和“值”联系起来。例如，语句“josh的考试分数是30”可以用一个联系学生姓名和分数的映射存储。java当中的映射与python当中的字典是一样的。

&emsp;&emsp;接下来我们基于数组实现`ArrayMap`类，它继承了`Map61B`接口——java内置的Map接口的删减版本。`Map61B`接口定义了下面的方法：

	public interface Map61B<K, V> {

	    /** Associate key with value. */
	    void put(K key, V value);
	
	    /** Checks if map contains the key. */
	    boolean containsKey(K key);
	
	    /** Returns value, assuming key exists. */
	    V get(K key);
	
	    /** Returns a list of all keys. */
	    List<K> keys();
	
	    /** Returns number of keys. */
	    int size();
	}

&emsp;&emsp;我们暂时忽略数组的扩容问题。关于映射有一件要注意的事情：一个键只能对应一个值。例如，如果我们发现josh其实是考了100分，那么我们会修改"josh--0"为"josh--100"，但不能让josh对应0和100两个值。

&emsp;&emsp;最后注意一点，泛型类型的名字我们将选用`K`和`V`。这两个名字是很随意的（与此同时也很直观，K意味着Key而V意味着Value）。我们也可以用其余名字代替`K`和`V`，但是在java中，用一个大写字母来表示泛型类型是非常常见的。现在我们开始实现映射。键和值都被一个数组存储着，并且还有一个size变量，这是实现数组映射所需要的全部成员。

	
	public class ArrayMap<K, V> implements Map61B<K, V>{
	    private K[] keys;
	    private V[] values;
	    private int size;
	
	    public ArrayMap(){
	        keys = (K[]) new Object[100];
	        values = (V[]) new Object[100];
	        size = 0;
	    }
	……

&emsp;&emsp;接着我们实现数组映射的方法。第一个方法是`keyIndex`，它负责在键数组中寻找是否有我们想要的键。如果键数组中包含有给定的键则返回其下标，否则返回-1。下面是第一个版本的代码，它有两个问题：

	/** some errors */
	private int keyIndex(K key) {
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] == key) {
                return i;
            }
        }
        return -1;
    }

&emsp;&emsp;第一个问题是，我们不应当使用`keys.length`这样的代码。构造函数会为我们分配一个包含100个值的数组，但很多时候我们并没有那么多键值对。如果size只有20，而keys.length却有100，那么我们多做了80次毫无意义的比较，甚至有可能出错（size之外的数组值是禁止被使用的！）另外一方面，我们使用了`keys[i] == key`。这也是错误的代码，因为泛型类型都是引用类型，keys是一个引用类型的数组，它的每个成员keys[i]都是引用类型。引用类型用`==`的意思是判断它们是否指向同一个对象，而我们不关系它们是否指向同一个对象，只关心它们所指向对象的值是否完全相等。因此我们应当使用`equals`。下面是修改之后的代码：

	private int keyIndex(K key) {
        for (int i = 0; i < size; i++) {
            if (keys[i].equals(key)) {
                return i;
            }
        }
        return -1;
    }


&emsp;&emsp;接下来我们完成五个方法，它们都建立在`keyIndex()`的基础上。这里我们忽略数组扩容问题，以及index越界问题。

	public boolean containsKey(K key) {
        int index = keyIndex(key);
        return index > -1;
    }

    public void put(K key, V value){
        int index = keyIndex(key);
        if(index == -1){
            keys[size] = key;
            values[size] = value;
            size += 1;
            return;
        }
        else{
            values[index] = value;
            return;
        }
    }

    public V get(K key) {
        int index = keyIndex(key);
        return values[index];
    }

	public int size() {
        return size;
    }

    public List<K> keys() {
        List<K> keyList = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            keyList.add(keys[i]);
        }
        return keyList;
    }

<br>
<br>

## Phase5 泛型当中的类型转换问题
&emsp;&emsp;我们为ArrayMap写一个简单的test函数（需要用到junit模块）。

	@Test
	public void test() { 
	    ArrayMap<Integer, Integer> am = new ArrayMap<Integer, Integer>();
	    am.put(2, 5);
	    int expected = 5;
	    assertEquals(expected, am.get(2));
	}

&emsp;&emsp;运行这个模块，我们会得到……编译报错。下面是报错的具体内容
>java: 对assertEquals的引用不明确
>
>org.junit.Assert 中的方法 assertEquals(long,long) 和 org.junit.Assert 中的方法 assertEquals(java.lang.Object,java.lang.Object) 都匹配

&emsp;&emsp;`expected`变量是int类型，am.get(2)是Integer类型。assertEquals不知道该选择哪一种重载方式。这是我们需要注意的。

&emsp;&emsp;将expected转换为Integer类型或者将am.get(2)解包都可以顺利运行程序。
<br>
<br>

## Phase6 泛型方法
&emsp;&emsp;接下来的目标是编写一个`MapHelper`类。它拥有两个方法：

- `get(Map61B, key):`:参数是一个映射和一个键。该方法会在给定的映射中寻找这个键是否存在，如果不存在返回null。如果存在则返回相对应的值。注意，我们之前实现的get方法没有考虑数组越界问题，之前的版本当中由于`keyIndex(key)`可能会返回-1，而数组下标-1的元素不存在。所以在这里我们需要重写一个。
- `maxKey(Map61B)`:返回给定映射中最大的那个键。前提条件是键可以被比较。

&emsp;&emsp;我们首先完成get方法。最有趣的部分不是具体实现，而是方法签名：`public static Integer get(Map61B<String, Integer> map, String key);`这个签名局限于String--Integer的映射，而我们希望它能够用于任何类型的映射！因此我们要怎样修改其签名呢？

&emsp;&emsp;想一下我们之前是怎样完成泛型编程的：我们都是在类签名处声明泛型类型，然后实例化类的时候传递参数。例如`public class ArrayMap<K, V>:`，我们会使用`ArrayMap<String, Integer> sim = new ArrayMap<String, Integer>();`。但是没有人会实例化`MapHelper`这个类。我们仅仅希望调用它提供的静态方法。

&emsp;&emsp;我们现在的任务是，不要让整个类变成泛型，而仅仅让类中的方法变成泛型。**方法是在函数返回类型前补上<>，声明这是一个泛型方法**

	public static <K,V> V get(Map61B<K,V> map, K key) {
        if(map.containsKey(key)){
            return map.get(key);
        }
        return null;
    }

&emsp;&emsp;这样的实现方式非常友好，它的调用格式如下。对于参数isMap不需要额外强调类型，Java会自动帮我们识别。

	ArrayMap<Integer, String> isMap = new ArrayMap<Integer, String>();
	System.out.println(mapHelper.get(isMap, 5));

&emsp;&emsp;接下来完成maxKey方法。

	public static <K, V> K maxKey(Map61B<K, V> map) {
	    List<K> keylist = map.keys();
	    K largest = map.get(0);
	    for (K k: keylist) {
	        if (k > largest) {
	            largest = k;
	        }
	    }
	    return largest;
	}

&emsp;&emsp;这段代码的问题在于，大于号`>`仅能用于基本类型，而K是引用类型。我们考虑重写if语句：`  if (k.compareTo(largest))`。这依旧不行。因为不是所有的引用类型都继承了`Comparable`类并完成了`compareTo`方法——特别是我们自己定义的那些奇怪的类。编译器会感到困惑，它并不知道你给它的类是不是完成了继承。所以它拒绝通过编译。
<br>
<br>

## Phase7 Type upper bounds
&emsp;&emsp;我们之前就强调过，maxKey方法的运行前提是映射的键类型可以被比较。因此我们可以用一个奇怪的签名表示这一点：

	public static <K extends Comparable<K>, V> K maxKey(Map61B<K, V> map) {...}

&emsp;&emsp;`K extends Comparable<K>`意味着K继承了Comparable接口，因此是可比较的。此外，Comparable本身也是泛型接口，我们需要指定可比较的类型。这里我们需要K与K进行比较。

&emsp;&emsp;更加有趣的问题是，为什么我们使用`extends`而不是`implements`来继承Comparable？毕竟，我们是在继承一个接口。事实上，用`implements`会导致报错。这涉及到extends在不同情境下的不同含义。

- 在继承情况下，例如，`Dog extends Animal`，我们强调的是Animal能做的事情Dog都能做，并且Dog还能做更多其余的事情！对于继承而言，`extends`关键字将父类所有属性都赋予给了子类。
- 在泛型情况下，例如，`K extends Comparable<K>`，我们不是在说K获得了Comparable<K>的所有属性，相反，我们的意思是，K必须是Comparable<K>的子类。这个时候的`extends`意味着限制而不是赋予。

&emsp;&emsp;这一规定被称之为`type upper bounding`。本节内容令人困惑，但却并不是重点。可以暂时忽略。