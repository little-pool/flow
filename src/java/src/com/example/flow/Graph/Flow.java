package com.example.flow.Graph;

import java.util.Vector;
import java.util.Stack;

public class Flow {
	private String sourcePrefix;
	private int sourceDevice;
	private int sourceNetmask;
	private String desPrefix;
	private int desDevice;
	private int desNetmask;
	private Vector<Integer> flowPath;
	
	public Flow(String s_Prefix, int s_Device, int s_Netmask, String d_Prefix, int d_Device, int d_Netmask){
		this.sourcePrefix = s_Prefix;
		this.sourceDevice = s_Device;
		this.sourceNetmask = s_Netmask;
		this.desPrefix = d_Prefix;
		this.desDevice = d_Device;
		this.desNetmask = d_Netmask;
	}

	public Vector<Integer> forwardPath(DenseModeGraph graph){
		Path f_path = new Path(graph, sourceDevice);
		flowPath = f_path.GetPath(desDevice);
		return flowPath;
	}
	public Vector<Integer> reversePath(){
		Vector<Integer> reversePath = new Vector<>();
		Stack<Integer> stack = new Stack<>();
		for(int i = 0 ; i < flowPath.size() ; i ++){
			stack.push(flowPath.get(i));
		}
		while(!stack.empty()){
			reversePath.add(stack.peek());
			stack.pop();
		}
		return reversePath;
	}
}
