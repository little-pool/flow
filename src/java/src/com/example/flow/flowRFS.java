package com.example.flow;
import com.example.flow.namespaces.*;
import com.example.flow.Graph.*;
import com.example.flow.Graph.DenseModeGraph.adjIterator;
import com.example.flow.Graph.NodeUtil;
import com.example.flow.Graph.Flow;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.omg.CORBA.PUBLIC_MEMBER;

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

    @ServiceCallback(servicePoint="flow-servicepoint",
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
    	//graphImport part
    	NavuContainer graphImport = service.container("graphImport");
    	NavuList graph = graphImport.list("graph");
    	/**
    	 * it is only support to add one graph per service point now
    	 */
    	//the next three lines are use for translate between deviceID and deviceName
    	NodeUtil nu_tmp = new NodeUtil();
		//current graph container
    	NavuContainer ncUtility = new NavuContainer();
		//current DencemodeGraph
    	DenseModeGraph graphTmp = null;
    	
    	for(NavuContainer graphInfo : graph){
    		ncUtility = graphInfo;
    		int nodeNum = Integer.valueOf(graphInfo.leaf("nodeNum").valueAsString());
    		DenseModeGraph g1 = new DenseModeGraph(nodeNum, false);
        	for (NavuContainer edge_info : graphInfo.list("edge")) {
        		g1.addEdge(Integer.valueOf(edge_info.leaf("souDevice").valueAsString()), Integer.valueOf(edge_info.leaf("desDevice").valueAsString()));
    		}
        	g1.printGraph();
        	graphTmp = g1;
        	Vector<Integer> v1 =  new Vector<Integer>();
        	Path p1 = new Path(g1, 0);
        	v1 = p1.GetPath(3);
        	NavuList nodeInfo = graphInfo.list("nodeInfo");
        	for(int i = 0 ; i < v1.size() ; i ++){
        		if(v1.get(i) != 5){
        			System.out.print(nu_tmp.Get_nodeName(graphInfo, v1.get(i)));
        			System.out.print(" -> ");
        		}
        		else{
        			System.out.print(nu_tmp.Get_nodeName(graphInfo, v1.get(i)));
        			System.out.print("\n");
        		}
        	}
    	}   	
    	//flowTable part
		Template Temp_flowPath = new Template(context, "flow-template");
		TemplateVariables TempVar_flowPath = new TemplateVariables();
    	NavuContainer flowTable = service.container("flowTable");
    	NavuList flow = flowTable.list("flow");
    	for(NavuContainer flowInfo : flow){
    		NavuContainer source_node = flowInfo.container("source_node");
    		NavuLeaf sourceDevice = source_node.leaf("Device");
    		NavuLeaf sourcePrefix = source_node.leaf("Prefix");
    		NavuLeaf sourceNetmask = source_node.leaf("Netmask");
    		
    		NavuContainer des_node = flowInfo.container("des_node");
    		NavuLeaf desDevice = des_node.leaf("Device");
    		NavuLeaf desPrefix = des_node.leaf("Prefix");
    		NavuLeaf desNetmask = des_node.leaf("Netmask");
    		Flow flowTemp = new Flow(sourcePrefix.valueAsString(), nu_tmp.Get_nodeID(ncUtility, sourceDevice.valueAsString()), Integer.valueOf(sourceNetmask.valueAsString()), desPrefix.valueAsString(), nu_tmp.Get_nodeID(ncUtility, desDevice.valueAsString()), Integer.valueOf(desNetmask.valueAsString()));	
    		if(flowInfo.leaf("pathType").valueAsString().equals("auto")){
				//Get two vectors that store the flowPath
    			Vector<Integer> flowVector_forward = flowTemp.forwardPath(graphTmp);
    			Vector<Integer> flowVector_reverse = flowTemp.reversePath();
    			//forward template apply part
				for(int i = 0 ; i < flowVector_forward.size() - 1 ; i ++){
					TempVar_flowPath.putQuoted("DEVICE_NAME", nu_tmp.Get_nodeName(ncUtility, flowVector_forward.get(i)));
					TempVar_flowPath.putQuoted("PREFIX", flowTemp.getDesPrefix());
					TempVar_flowPath.putQuoted("NETMASK", String.valueOf(flowTemp.getSourceNetmask()));
					String nextHop_IP = nu_tmp.Get_nodeLoop0IP(nu_tmp.Get_deviceInfo(ncsRoot, nu_tmp.Get_nodeName(ncUtility, flowVector_forward.get(i+1))));
					TempVar_flowPath.putQuoted("NEXTHOP", nextHop_IP);
					Temp_flowPath.apply(service, TempVar_flowPath);
				}
				//reverse template apply part
				for(int i = 0 ; i < flowVector_reverse.size() - 1 ; i ++){
					TempVar_flowPath.putQuoted("DEVICE_NAME", nu_tmp.Get_nodeName(ncUtility, flowVector_reverse.get(i)));
					TempVar_flowPath.putQuoted("PREFIX", flowTemp.getSourcePrefix());
					TempVar_flowPath.putQuoted("NETMASK", String.valueOf(flowTemp.getDesNetmask()));
					String nextHop_IP = nu_tmp.Get_nodeLoop0IP(nu_tmp.Get_deviceInfo(ncsRoot, nu_tmp.Get_nodeName(ncUtility, flowVector_reverse.get(i+1))));
					TempVar_flowPath.putQuoted("NEXTHOP", nextHop_IP);
					Temp_flowPath.apply(service, TempVar_flowPath);
				}
    			System.out.print("over");
    		}
    	}
        return opaque;
    }
}
