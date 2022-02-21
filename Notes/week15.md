# Lecture 15: Packages, Javadocs, Access Control, Objects
## Phase1 Packages 
&emsp;&emsp;Packages的出现是基于这样一个事实：可能会有许多重名的类。为了更好的管理这些类（加以区分，选择所需要的，等等），我们可以将同名的类放到不同的Packages当中。

&emsp;&emsp;通常情况下Packages的名字以网址开始，当然对于不需要发行的代码而言，不必遵守这一规定。

&emsp;&emsp;Intellij提供了很多使用Package的方法。

&emsp;&emsp;需要注意的一点是，如果一个类没有在任何一个包中，那么它默认在`default`包中。在`default`包中的文件不能被其余文件所导入。
<br>
<br>
## Phase2 JAR Files

&emsp;&emsp;JAR文件可以被简单理解为压缩文件，它包含了class文件。
<br>
<br>
## Phase3 Access Control(Ⅰ)

&emsp;&emsp;`public`和`private`是用于访问控制的关键字。现在我们讨论在继承和包两种场景下的案例。

- 子类可以访问父类的私有成员吗？
- 同包下的类可以访问另一个类的私有成员吗？

&emsp;&emsp;答案都是否定的，因为私有成员只能被同一个文件当中的代码所访问。如果我们希望某个成员可以被子类或者同包下的类访问，我们可以使用关键字`protected`：

- public:允许class subclass package world访问
- protected:允许class subclass package访问，但不允许world访问
- private:允许class访问，不允许其余任何情况下的访问
- 无访问控制的关键字，这个时候被称为package private:允许class和package访问，不允许subclass和world访问

&emsp;&emsp;我们已经介绍了所有的访问控制类型。从中应该发现一个事实：package比subclass关系更加亲密。package不能访问的，subclass一定也不能访问。subclass可以访问的，package一定也能访问。这是符合java工业界工作原理的。package往往更被看作一个整体，而不是subclass。
<br>
<br>
## Phase4 Access Control(Ⅱ)
&emsp;&emsp;接下来讨论访问控制的一些特殊情况。第一种是默认包情况，如果没有给java文件指定包，那么它会在一个`default package`当中。

- 所有没有指定包的Java文件，都在同一个默认包当中
- 如果默认包当中的文件没有访问控制符，那么处于包私有状态。



## Phase5 Object:toString
&emsp;&emsp;所有类都继承自Object类。从Object类继承到的方法有许多，我们主要讨论`String toString();`和`boolean equals(Object obj);`俩个方法。

&emsp;&emsp;`toString()`方法提供了对象的字符串表示。当我们使用`System.out.println()`的时候，打印方法其实隐式地调用了`toString()`方法，并且打印出字符串。然而，`Object`类当中的默认方法其实只是打印对象的地址，十六进制的字符串。而java当中内置的数据结构例如`ArrayList`实际上重写了这个方法，可以漂亮地打印出一系列链表当中的元素。当然，我们也可以为自己创造出来的类重写`toString()`。

&emsp;&emsp;下面是我们自己创造的类`ArraySet`的字符串转换方法：

	public String toString() {
	    String returnString = "{";
	    for (int i = 0; i < size; i += 1) {
	        returnString += keys[i];
	        returnString += ", ";
	    }
	    returnString += "}";
	    return returnString;
	}

&emsp;&emsp;上面的方法看起来非常优美简洁，但实际上并不高效。要记得我们在不变量一节当中所学到的知识，字符串是一种不变量，因此我们每一次使用`+`号其实都是在创造等一个新的字符串。这太糟糕了，因为你无法想象我们创造了如此多的新字符串！

&emsp;&emsp;当然java提供了一种补偿方法：名为`StringBuilder`的类。它的实例对象是可变的。我们利用这个类完成重写。

	public String toString() {
        StringBuilder returnSB = new StringBuilder("{");
        for (int i = 0; i < size - 1; i += 1) {
            returnSB.append(items[i].toString());
            returnSB.append(", ");
        }
        returnSB.append(items[size - 1]);
        returnSB.append("}");
        return returnSB.toString();
    }

<br>
<br>
## Phase6 Object:equals
&emsp;&emsp;`==`和`.equals()`在java当中具有不同的表现行为。前者检查两个对象在内存当中是否为同一个对象。记住，是比较两个对象的每一个bit！对于基本类型，`==`比较它们的值是否相等。而对于引用类型，`==`比较它们的内存地址是否相等（两个引用型变量是否指向同一个对象）。

&emsp;&emsp;在判断引用类型是否相等的时候，`==`非常不好用。例如，比较两个链表是否相等，比较两个集合是否相等，我们不希望是比较地址是否相等。例如，对于集合相等的定义是，A中的每一个元素都在B中，且B中的每一个元素都在A中。因此我们需要重写`equals()`方法。这个方法在未被重写之前，有着和`==`一样的行为。通过重写，使得不再根据是否指向同一个对象来判定两个集合是否相等。

&emsp;&emsp;下面是ArraySet的equals方法。注意，比较集合是否相等是很麻烦的，因为集合没有顺序。

	public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        ArraySet<T> o = (ArraySet<T>) other;
        if (o.size() != this.size()) {
            return false;
        }
        for (T item : this) {
            if (!o.contains(item)) {
                return false;
            }
        }
        return true;
    }


<br>
<br>