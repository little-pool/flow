package com.example.flow.Graph;

import java.net.InterfaceAddress;
import java.util.List;

import com.sun.xml.internal.ws.api.server.Container;
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
	//Get nodeName from nodeID
	public String Get_nodeName(NavuContainer graphInfo, int nodeID) throws NavuException{
		NavuList nodeInfo = graphInfo.list("nodeInfo");
		return nodeInfo.elem(String.valueOf(nodeID)).leaf("deviceName").valueAsString();
	}
	//Get nodeID from nodeName
	public int Get_nodeID(NavuContainer graphInfo, String nodeName) throws NavuException{
		NavuList nodeInfo = graphInfo.list("nodeInfo");
		for(NavuContainer nodeInfo_detail : nodeInfo){
			if(nodeInfo_detail.leaf("deviceName").valueAsString().equals(nodeName))
				return Integer.valueOf(nodeInfo_detail.leaf("deviceID").valueAsString());
		}
		return -1;
	}
	//Get nodeLoopback0IP from nodeName
	public String Get_nodeLoop0IP(NavuContainer deviceInfo) throws NavuException{
		NavuContainer deviceConfig = deviceInfo.container("config");
		NavuContainer interfaces = deviceConfig.container("cisco-ios-xr:interface");
		NavuList Loopback = interfaces.list("Loopback");
		NavuContainer Loopback0 = Loopback.elem("0");
		NavuContainer ipv4 = Loopback0.container("ipv4");
		NavuContainer address = ipv4.container("address");
		NavuLeaf ip = address.leaf("ip");
		String nodeLoopback0IP = ip.valueAsString();
		return nodeLoopback0IP;
	}
	
	//Get container deviceInfo from deviceName
	public NavuContainer Get_deviceInfo(NavuNode root, String deviceName) throws NavuException{
		NavuContainer devices = root.container("devices");
		NavuList device = devices.list("device");
		NavuContainer deviceInfo = device.elem(deviceName);
		return deviceInfo;
	}
}
