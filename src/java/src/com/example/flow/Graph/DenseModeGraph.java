package com.example.flow.Graph;
import java.util.Vector;
import javax.management.loading.PrivateClassLoader;
import org.omg.PortableServer.ServantActivator;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.xpath.internal.operations.Bool;
import java.util.Stack;

public class DenseModeGraph {
	/**
	 * 属性
	 * 节点个数，边个数，是否为有向图，Vector对象啊存储图的邻接矩阵。
	 */
	private int nodes;
	private int edges;
	private boolean directed;
	private Vector<Vector> graph = new Vector<Vector>();
	/**
	 * 构造函数
	 * @param nodes		-节点个数
	 * @param directed 	-是否为有向图
	 */
	public DenseModeGraph(int nodes, boolean directed){
		this.nodes = nodes;
		this.directed = directed;
		graph = new Vector<Vector>(nodes);
		for(int i = 0 ; i < nodes ; i ++){
			Vector<Integer> v_tmp = new Vector<Integer>();
			for(int j = 0 ; j < nodes ; j ++){
				v_tmp.add(0);
			}
			graph.add(v_tmp);
		}
	}
	/**
	 * Gets函数
	 * 提供节点数与边数的查询
	 */
	public int GetNodes(){
		return this.nodes;
	}
	public int GetEdges(){
		return this.edges;
	}
	/**
	 * 查询函数
	 * 查询途中是够含有指定边
	 * @param x -one edge
	 * @param y	-the other edge
	 * @return	-true or false
	 */
	boolean hasEdge(int x, int y){
		//这里只考虑无相图，因此只需要判断一个方向的变即可
		if(graph.get(x).get(y).equals(1))
			return true;
		return false;
	}
	/**
	 * addEdge函数
	 * 针对无向图，需要双向添加。
	 * @param x	-one edge
	 * @param y	-the other edge
	 */
	public void addEdge(int x, int y){
		graph.get(x).set(y, 1);
		if(this.directed == false)
			graph.get(y).set(x, 1);
		this.edges ++;
	}
	/**
	 * show函数
	 * 打印图的邻接矩阵
	 */
	public void printGraph(){
		System.out.print("\t");
		for(int i = 0 ; i < this.nodes ; i ++){
			System.out.print(i);
			System.out.print("\t");
		}
		System.out.print("\n");
		for(int i = 0 ; i < this.nodes ; i ++){
			System.out.print(i);
			System.out.print("\t");
			for(int j = 0 ; j < this.nodes ; j ++){
				System.out.print(this.graph.get(i).get(j));
				System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
	/**
	 * adjIterator
	 * 指定节点的临边迭代器
	 */
	public class adjIterator{
		//指定图象
		private DenseModeGraph Graph;
		//目标节点
		private int node;
		//临边索引值
		private int index;
		//construction
		public adjIterator(DenseModeGraph graph, int node){
			this.Graph = graph;
			this.node = node;
		}
		//three main methods
		public int begin(){
				index = -1;
				return next();
			}
		public int next(){
			for(index += 1 ; index < Graph.GetNodes() ; index ++){
				if(Graph.graph.get(node).get(index).equals(1))
					return index;
			}
			return -1;
		}
		public	boolean end(){
			return index >= Graph.GetNodes();
		}
	}
	
}
