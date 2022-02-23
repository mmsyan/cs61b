# HW2: Percolation

## Phase1 基本介绍
&emsp;&emsp;HW2主要考察并查集的用法。普林斯顿库已经提供了并查集的实现和接口，无需我们自己实现。

&emsp;&emsp;HW2介绍了并查集在物理当中的应用——渗透问题。[HW2](https://sp18.datastructur.es/materials/hw/hw2/hw2)中有关于渗透问题的相关说明。本次作业分成两部分，第一部分是根据并查集实现一个渗透类，完成一系列接口；第二部分是数学问题，根据概率公式计算相关系数。
<br>
<br>
## Phase2 第一阶段：实现渗透类
### Phase2.1 理解渗透系统
&emsp;&emsp;在实现渗透类之前，先理解整个渗透系统。整个系统是一个N行N列的数组，每个元素都可被看作是一个格子。N是构造函数唯一参数。

- open/blocked：每个格子有两种状态，开启或者关闭。`open()`接口可以打开指定格子，`isOpen()`接口询问指定格子是否被打开。
- full：若一个格子是full状态的，则：它是开启状态的，且它可以通过上下左右四个格子与某个**最顶层已经开启**的格子连接。
- percolates：一个系统是渗透状态的，当且仅当最底层有格子是full状态的。

&emsp;&emsp;渗透类的关键有两点：格子只会被打开不会被关闭，且已经打开的格子之间会通过上下左右四个方向的格子与其它格子相连，这种情况下非常适合并查集结构。

&emsp;&emsp;要注意，渗透与否是对于整个系统而言，而open/full是针对格子而言。
<br>
### Phase2.2 思考渗透结构
&emsp;&emsp;我们的渗透类需要完成以下接口。

	public class Percolation {
	   public Percolation(int N)                // create N-by-N grid, with all sites initially blocked
	   public void open(int row, int col)       // open the site (row, col) if it is not open already
	   public boolean isOpen(int row, int col)  // is the site (row, col) open?
	   public boolean isFull(int row, int col)  // is the site (row, col) full?
	   public int numberOfOpenSites()           // number of open sites
	   public boolean percolates()              // does the system percolate?
	   public static void main(String[] args)   // use for unit testing (not required)
	}

&emsp;&emsp;对于格子是否处于开启态，我们设置一个二维的boolean数组`items`来表示。同时为了表示连接关系，设置第一个并查集结构`wuf`。

- `open()`接口修改boolean数组，将状态改为true。同时观察上下左右四个格子是否处于开启态，如果是则在并查集wuf当中进行连接。
- `isOpen()`接口直接返回boolean数组的指定值。
- `isFull()`接口：首先检查是否处于开启状态，若不处于直接返回false。即使处于开启状态，也需要判定是否与最顶层相连接。这时候问题来了，难道需要遍历最顶层的N个格子，判断是否有开启状态的格子与自己处于连接状态吗？

&emsp;&emsp;如果遍历最顶层所有格子，则时间复杂度为O(N)。这里有更加巧妙的方法:虚拟格子。在最顶层制造一个虚拟格子，与最顶层所有**处于开启状态**的格子相连通。这样询问一个开启状态的格子是否处于Full状态，只需要在并查集当中比较是否与最顶层的虚拟格子相连通即可。

- `numberOfOpenSites()`：需要我们额外设置一个Int变量
- `percolates() `：需要询问**最底层是否有格子处于full状态**。正常情况下需要遍历最底层所有格子。这样做时间复杂度来到了O(N)。但是我们也可以在最顶层设置一个虚拟格子，与最底层所有**处于开启状态**的格子相连通。则**系统处于渗透状态**，等价于**最底层的虚拟格子是否与最顶层的虚拟格子相连接**。
<br>
### Phase2.3 回流问题
&emsp;&emsp;双虚拟节点有效降低了时间复杂度，但是带来了回流问题。假如x表示关闭，o表示开启，我们来看下面的反例：

x x x x o

x x x x o

o x x x o

o x x x o

&emsp;&emsp;最右边一列都是o，代表处于打开状态，此时系统处于渗透状态。但是最左边一列底下有两个o，它们都不处于full状态。在我们的双虚拟节点渗透类当中，系统处于渗透态等价于最顶层和最底层的虚拟节点相连接了，而最左边的节点会通过最底层的虚拟节点和最顶层的虚拟节点相连接，从而造成处于full状态的假象。

&emsp;&emsp;一个解决办法是，设置两个并查集实例变量。其中一个变量不需要最底层虚拟节点。对于`open()`接口，如果打开的是最底层的节点，那么在`wuf`并查集当中与最底层虚拟节点相连接，但是在`wufWithoutBW`并查集当中不做处理。对于`isFull()`接口，只使用`wufWithoutBW`。对于`percolates() `接口，只使用`wuf`。
<br>
<br>
## Phase3 第二阶段：模拟实验
&emsp;&emsp;这是一个概率论实验，总共进行T次，每次实验不断随机地打开一个节点，直到系统发生渗透。记录下每次发生渗透时的临界值（已打开的节点数/总结点数）。然后求出T次实验的样本均值、标准差、置信区间。
