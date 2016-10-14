package fr.glouglouwine.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

public class JmxManager {

	public final static JmxManager INSTANCE = new JmxManager();
	private MBeanServer mbs;

	private JmxManager() {
		mbs = ManagementFactory.getPlatformMBeanServer();
	}

	public void register(Object managedBean) {
		ObjectName name;
		try {
			name = new ObjectName("fr.glouglouwine.resources:type=" + managedBean.getClass().getSimpleName());
			mbs.registerMBean(managedBean, name);
		} catch (Exception e) {
			throw new RuntimeException("Error while registering bean", e);
		}
	}
}
