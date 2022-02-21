# Lecture 11: Libraries

## Phase1 ADT(抽象数据类)

&emsp;&emsp;到现在为止我们构造了两种抽象数据结构：链表类，双端队列类。抽象数据类型是指只关注数据结构的行为，而不关注具体实现。Java当中的接口就是抽象数据类型。[Java Deuqe](https://docs.oracle.com/javase/7/docs/api/java/util/Deque.html)就是一种接口，Java标准库当中提供了几种具体的实现。
<br>
<br>

## Phase2 List Set Map

&emsp;&emsp;Java库当中完成了许多数据结构的实现。其中List Set Map是最重要的几种之一。

&emsp;&emsp;[List](https://docs.oracle.com/javase/8/docs/api/java/util/List.html) : 链表可以被理解为一组元素的有序‘集合’

&emsp;&emsp;[Set](https://docs.oracle.com/javase/7/docs/api/java/util/Set.html) ： 集合是一组无序、不重复的元素

&emsp;&emsp;[Map](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html) ： 映射是一组键值对。每一个键值对都由一个键元素和一个值元素组成。
<br>
<br>
## Phase3 Abstract Class
&emsp;&emsp;到现在为止，我们学习了两种继承：关键字`implements`的接口继承，和关键字`extends`的实现继承。

&emsp;&emsp;对于接口继承而言：

- 默认方法是抽象方法，不包含实现

- 使用关键字`default`的方法才不是抽象方法
<br>
<br>


## Phase4 Package
&emsp;&emsp;Package是用于管理类和接口的名字空间。它的出现基于这样一个事实：类的名字可能因为相同而产生冲突。因此需要Package名字来进行区分。
