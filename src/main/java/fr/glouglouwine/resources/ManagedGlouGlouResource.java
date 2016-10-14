package fr.glouglouwine.resources;

public class ManagedGlouGlouResource implements ManagedGlouGlouResourceMBean {

	public static ManagedGlouGlouResource INSTANCE = new ManagedGlouGlouResource();

	private boolean addBottleEnabled = true;

	private boolean isListBottleSlow = false;

	@Override
	public void enableAddBottle() {
		addBottleEnabled = true;
	}

	@Override
	public void disableAddBottle() {
		addBottleEnabled = false;
	}

	@Override
	public void makeListBottleSlow(boolean isListBottleSlow) {
		this.isListBottleSlow = isListBottleSlow;
	}

	public boolean isAddBottleEnabled() {
		return addBottleEnabled;
	}
	
	public boolean isListBottleSlow() {
		return isListBottleSlow;
	}


}
