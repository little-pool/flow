package com.example.flow.Graph;
import java.util.Vector;

import com.example.flow.Graph.DenseModeGraph.adjIterator;
import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;
import com.sun.swing.internal.plaf.metal.resources.metal_zh_TW;

import java.util.Stack;
public class Path {
	/**
	 * 属性
	 * DenseModeGraph Graph 传入图对象
	 * int sourceNode 待遍历源节点
	 * boolean[] visited 存储节点的遍历情况
	 * int[] from 存储深度优先遍历时当前遍历节点的根节点
	 */
	private DenseModeGraph graph;
	private int sourceNode;
	private boolean[] visited;
	private int[] from;
	
	/**
	 * 深度优先遍历
	 * 从传入的源节点遍历整张图
	 * 在遍历过程中维护from数组用于之后的路径回溯
	 */
	private void DeepFirstSearch(int nodeID){
		visited[nodeID] = true;
		adjIterator adj = graph.new adjIterator(graph, nodeID);
		for(int i = adj.begin() ; !adj.end() ; i = adj.next()){
			if(!visited[i]){
				from[i] = nodeID;
				DeepFirstSearch(i);
			}
		}
	}
	/**
	 * 寻路函数
	 * 输入目标节点，输出路径
	 * 利用栈回溯起点到终点的from数组
	 * 最后出栈打印即可得到深优遍历的路径，但并非是最短路径
	 */
	private Vector<Integer> GetPath(int desNode){
		Stack<Integer> pathStack = new Stack<>();
		int i = desNode;
		while( i != -1 ){
			pathStack.push(i);
			i = from[i];
		}
		Vector<Integer> pathVector = new Vector<Integer>();
		while(!pathStack.empty()){
			pathVector.add(pathStack.peek());
			pathStack.pop();
		}
		return pathVector;
	}
	/**
	 * 构造函数
	 * 初始化visited&from数组
	 * all visited = false;
	 * all from = -1
	 */
	public Path(DenseModeGraph Graph, int node){
		this.sourceNode = node;
		this.graph = Graph;
		visited = new boolean[graph.GetNodes()];
		from = new int[graph.GetNodes()];
		for(int i = 0 ; i < graph.GetNodes() ; i ++){
			visited[i] = false;
			from[i] = -1;
		}
		DeepFirstSearch(sourceNode);
	}
	
	/**
	 * 打印路径
	 */
	public void PathPrint(int desNode){
		Vector<Integer> path = GetPath(desNode);
		for(int i = 0 ; i < path.size() ; i ++){
			if(i != desNode){
				System.out.print(path.get(i));
				System.out.print(" -> ");
			}
			else{
				System.out.print(path.get(i));
				System.out.print("\n");
			}
		}
	}
}
