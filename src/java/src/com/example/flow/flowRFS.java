package com.example.flow;

import com.example.flow.namespaces.*;
import com.sun.xml.internal.ws.api.server.Container;
import com.example.flow.Graph.*;
import com.example.flow.Graph.DenseModeGraph.adjIterator;

import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.tailf.conf.*;
import com.tailf.navu.*;
import com.tailf.ncs.ns.Ncs;
import com.tailf.dp.*;
import com.tailf.dp.annotations.*;
import com.tailf.dp.proto.*;
import com.tailf.dp.services.*;
import com.tailf.ncs.template.Template;
import com.tailf.ncs.template.TemplateVariables;


public class flowRFS {


    /**
     * Create callback method.
     * This method is called when a service instance committed due to a create
     * or update event.
     *
     * This method returns a opaque as a Properties object that can be null.
     * If not null it is stored persistently by Ncs.
     * This object is then delivered as argument to new calls of the create
     * method for this service (fastmap algorithm).
     * This way the user can store and later modify persistent data outside
     * the service model that might be needed.
     *
     * @param context - The current ServiceContext object
     * @param service - The NavuNode references the service node.
     * @param ncsRoot - This NavuNode references the ncs root.
     * @param opaque  - Parameter contains a Properties object.
     *                  This object may be used to transfer
     *                  additional information between consecutive
     *                  calls to the create callback.  It is always
     *                  null in the first call. I.e. when the service
     *                  is first created.
     * @return Properties the returning opaque instance
     * @throws ConfException
     */

    @ServiceCallback(servicePoint="graphImport-servicepoint",
        callType=ServiceCBType.CREATE)
    public Properties graphImport(ServiceContext context,
             NavuNode service,
             NavuNode ncsRoot,
             Properties opaque)
             throws ConfException {
    	/**
    	 * 加载graph对象
    	 * 设备名的最后两个字符为设备ID，作为graph节点ID
    	 */
    	//测试输出当前所有连接设备ID
    	/*NavuContainer devices = ncsRoot.container("devices");
    	NavuList device = devices.list("device");
    	for(NavuContainer device_info : device){
    		System.out.print(device_info.leaf("name").valueAsString());
    		System.out.println("\t");
    	}*/
    	//测试输出所有yang数据
    	/*NavuList nodeInfo = service.list("nodeInfo");
    	for(NavuContainer node_info : nodeInfo){
    		System.out.print(node_info.leaf("deviceID").valueAsString());
    		System.out.print(":");
    		System.out.print(node_info.leaf("deviceName").valueAsString());
    		System.out.print("\n");
    	}*/
    	
    	int nodeNum = Integer.valueOf(service.leaf("nodeNum").valueAsString());
    	DenseModeGraph g1 = new DenseModeGraph(nodeNum, false);
    	for (NavuContainer edge_info : service.list("edge")) {
    		g1.addEdge(Integer.valueOf(edge_info.leaf("souDevice").valueAsString()), Integer.valueOf(edge_info.leaf("desDevice").valueAsString()));
		}
    	g1.printGraph();
//    	adjIterator adj1 = g1.new adjIterator(g1, 1);
//    	for(int i = adj1.begin() ; !adj1.end() ; i = adj1.next()){
//    		System.out.print(i);
//    	}
//    	System.out.print("\n");
//    	Path p1 = new Path(g1, 3);
//    	p1.PathPrint(5);
    	Vector<Integer> v1 =  new Vector<Integer>();
    	Path p1 = new Path(g1, 0);
    	v1 = p1.GetPath(5);
    	NavuList nodeInfo = service.list("nodeInfo");
    	for(int i = 0 ; i < v1.size() ; i ++){
    		if(v1.get(i) != 5){
    			System.out.print(nodeInfo.elem(v1.get(i).toString()).leaf("deviceName").valueAsString());
    			System.out.print(" -> ");
    		}
    		else{
    			System.out.print(nodeInfo.elem(v1.get(i).toString()).leaf("deviceName").valueAsString());
    			System.out.print("\n");
    		}
    	}
        return opaque;
    }
    
    @ServiceCallback(servicePoint="flowTable-servicepoint",
		callType=ServiceCBType.CREATE)
    public Properties flowTable(ServiceContext context,
    		NavuNode service,
    		NavuNode ncsRoot,
    		Properties opaque)
			throws ConfException{
    	return opaque;
    }
}
