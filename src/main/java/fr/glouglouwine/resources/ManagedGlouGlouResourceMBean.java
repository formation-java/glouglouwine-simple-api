package fr.glouglouwine.resources;

public interface ManagedGlouGlouResourceMBean {
	public void enableAddBottle();

	public void disableAddBottle();

	void makeListBottleSlow(boolean isListBottleSlow);
}
