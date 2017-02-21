package com.example.flow.Graph;

import com.tailf.navu.NavuContainer;
import com.tailf.navu.NavuException;
import com.tailf.navu.NavuLeaf;
import com.tailf.navu.NavuList;
import com.tailf.navu.NavuNode;
/**
 * 转换设备ID&设备NAME
 * @author pool_little
 *
 */
public class NodeUtil {
	
	public static String Get_nodeName(NavuContainer graphInfo, int nodeID) throws NavuException{
		NavuList nodeInfo = graphInfo.list("nodeInfo");
		return nodeInfo.elem(String.valueOf(nodeID)).leaf("deviceName").valueAsString();
	}
	
	public static int Get_nodeID(NavuContainer graphInfo, String nodeName) throws NavuException{
		NavuList nodeInfo = graphInfo.list("nodeInfo");
		for(NavuContainer nodeInfo_detail : nodeInfo){
			if(nodeInfo_detail.leaf("deviceName").valueAsString().equals(nodeName))
				return Integer.valueOf(nodeInfo_detail.leaf("deviceID").valueAsString());
		}
		return -1;
	}
}
