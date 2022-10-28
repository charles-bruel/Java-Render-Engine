package net.chazzvader.core.generic.engine;

import java.util.ArrayList;

/**
 * An EngineItem, is, quite simply, anything that you make a instance of that
 * interacts with the engine and or rendering system. Gives access to methods to
 * determine basic things about the current instance.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public abstract class EngineItem implements IDeletable {

	private Application application;

	private static ArrayList<EngineItem> items = new ArrayList<EngineItem>();

	/**
	 * Creates a new engine item. Assigns the instance of the application to the
	 * item, so if you are creating one of these before the Application, you mave
	 * have an issue.
	 */
	public EngineItem() {
		items.add(this);
		this.application = Application.getInstance();
	}

	/**
	 * In certain cases an EngineItem is initialized before the application starts.
	 * This should be minimized, however, as of now, it is still necessary.
	 * Initialized all EngineItems to the application.
	 */
	public static void initAll() {
		for (int i = 0; i < items.size(); i++) {
			items.get(i).application = Application.getInstance();
		}
	}

	/**
	 * The current aspect ratio of the window.
	 * 
	 * @return The current aspect ratio of of the window.
	 */
	protected final float getWindowAspectRatio() {
		return (float) getWindowWidth() / (float) getWindowHeight();
	}

	/**
	 * The current height of the window
	 * 
	 * @return The current height of the window
	 */
	protected int getWindowHeight() {
		return application.getWindow().getHeight();
	}

	/**
	 * The current width of the window.
	 * 
	 * @return The current width of the window.
	 */
	protected int getWindowWidth() {
		return application.getWindow().getWidth();
	}

	/**
	 * Gets the active scene.
	 * 
	 * @return The active scene.
	 */
	protected final Scene getActiveScene() {
		return application.getActiveScene();
	}

	/**
	 * Gets the window for the Application.
	 * 
	 * @return The window.
	 */
	protected final Window getWindow() {
		return application.getWindow();
	}

	@Override
	public void delete() {
		items.remove(this);
		application = null;
	}

}