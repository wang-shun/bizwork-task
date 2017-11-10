package com.sogou.bizwork.task.api.zkclient.service.impl;


import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sogou.bizwork.task.api.zkclient.service.ServiceTreeZKClient;

@Component
public class ServiceTreeZKClientImpl implements InitializingBean, DisposableBean,
        ServiceTreeZKClient {
	private final static Logger logger = Logger.getLogger("ServiceTreeZKClient");

	// 初始ZK结构
	private final static String LOCK_PATH = "/bizworkapilock";

	private static final int SESSION_TIMEOUT = 30 * 1000;
	private static final int CONNECT_TIMEOUT = 10 * 1000;

	private ZkClient zkClient;

	// 与SOA使用相同的ZooKeeper地址
	@Value("${quorumString}")
	private String quorumString;

//	private List<ACL> zkACL;


	private static String convertBytes2Str(Object data) {
		if (data != null && data instanceof byte[]) {
			return new String((byte[]) data);
		}
		return null;
	}

	@Override
	public boolean getTaskLock(String ip) {  // 该系统是在zookeeper上新建节点表示远程调用的ip地址
		try {
			if (zkClient.exists(LOCK_PATH)) {  //检测节点是否存在是否有锁 节点是否存在
				String data = convertBytes2Str(zkClient.readData(LOCK_PATH));  // 存在之后先判断节点内容，不是那种直接创建
				if (StringUtils.equalsIgnoreCase(ip, data)) {
					return true;
				}
			} else {
				zkClient.createEphemeral(LOCK_PATH, ip.getBytes());  //创建临时节点  path   data
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public void destroy() throws Exception {

		if (this.zkClient != null) {
			this.zkClient.close();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 初始化ZKClient
		createZKClient();
	}

	/**
	 * 创建ZKClient
	 * 
	 * @return
	 */
	private void createZKClient() {
		try {
//			zkACL = new ArrayList<ACL>(3);
//			// 任何人都可以读数据
//			zkACL.add(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));

			this.zkClient = new ZkClient(this.quorumString, SESSION_TIMEOUT, CONNECT_TIMEOUT,
			        new BytesPushThroughSerializer());
			
			logger.info("ZKClient initialize succeed");

			// 初始化ZK结构
//			try {
//				// 创建资源池,写入数据
//				if (!zkClient.exists(RESOURCE_POOL_PATH)) {
//					ZNodeDataResult resourcePoolData = new ZNodeDataResult();
//					ServiceNodeTypeEnum resourcePoolType = ServiceNodeTypeEnum.RESOURCE_POOL;
//					resourcePoolData.setServiceNodeId(resourcePoolType.getServiceNodeTypeCode());
//					resourcePoolData.setServiceNodeName(resourcePoolType.getServiceNodeTypeName());
//					resourcePoolData.setServiceNodeType(ServiceNodeTypeEnum.RESOURCE_POOL
//					        .getServiceNodeTypeCode());
//					resourcePoolData.setUpdateTime(new Timestamp(System.currentTimeMillis())
//					        .toString());
//				}
//
//				logger.info("ZK structure initialize succeed");
//			} catch (Exception e) {
//				logger.error("ZK structure initialize failed", e);
//			}
		} catch (Exception e) {
			// 初始化失败
			logger.warn("ZKClient initialize failed", e);
		}
	}
}