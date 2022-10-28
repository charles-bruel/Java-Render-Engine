package net.chazzvader.core.generic.engine;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.render.RenderPipeline;

/**
 * Represents an application, your main class will extend this
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public abstract class Application {

	Window window = null;
	private boolean started;
	private static Application instance;
	private Scene activeScene = new Scene();

	/**
	 * Gets the active scene
	 * 
	 * @return The active scene
	 */
	public Scene getActiveScene() {
		return activeScene;
	}

	/**
	 * Sets the active scene of this application
	 * 
	 * @param scene The scene to set as active
	 */
	public void setActiveScene(Scene scene) {
		scene.active = true;
		if (activeScene != null)
			activeScene.active = false;
		window.setBackgroundColor(scene.getBackgroundColor());
		activeScene = scene;
	}

	/**
	 * This will only return when the application is closed or if the application
	 * fails to start.<br>
	 * Runs the application.
	 */
	public void start() {
		if (started) {
			Logging.log("Application already started", "Application", LoggingLevel.WARN);
			return;
		}
		if (instance != null) {
			Logging.log("An application has already been started!", "Application", LoggingLevel.WARN);
			return;
		}
		if (window == null) {
			Logging.log("No window", "Application", LoggingLevel.WARN);
			return;
		}
		instance = this;
		EngineItem.initAll();
		Logging.log("Application started", "Application", LoggingLevel.INFO);
		preInit();
		loop();
		Logging.log("Min FPS: " + min_fps, "Application", LoggingLevel.INFO);
		Logging.log("Max FPS: " + max_fps, "Application", LoggingLevel.INFO);
		Logging.log("Mean FPS: " + frames / seconds, "Application", LoggingLevel.INFO);
	}

	/**
	 * Gets and returns the current frames per second. This is measured by counting
	 * frames until the time reaches 1 sec, and then resetting. Thus, this value
	 * only refreshes once per second.
	 * 
	 * @return The current frames per second.
	 */
	public int getCurrentFPS() {
		return currentFPS;
	}

	/**
	 * Returns the application instance of the program
	 * 
	 * @return The application instance of the program
	 */
	public static Application getInstance() {
		return instance;
	}

	/**
	 * Returns the applications window
	 * 
	 * @return The applications window
	 */
	public Window getWindow() {
		return window;
	}

	/**
	 * Has the program been started?
	 * 
	 * @return If the program has been started
	 */
	public boolean isStarted() {
		return started;
	}

	protected abstract void preInit();

	protected abstract void init();

	/**
	 * Update function, to do a lot
	 * 
	 * @param delta How long is seconds since the last update
	 */
	protected void update(double delta) {

	}

	/**
	 * Update function, to do a lot <em>ONLY CALLED WHEN RENDER OVERRIDE IS
	 * TRUE</em>
	 */
	public void render() {

	}

	/**
	 * Renders everything
	 */
	private void _render() {
		RenderPipeline pipeline = window.getRenderPipeline();
		pipeline.preRender(window, this);
		renderUI();
		pipeline.render(window, this, activeScene);
		pipeline.postRender(window, this);
	}

	/**
	 * Update wrapper function, to poll events and stuff
	 * 
	 * @param delta How long is seconds since the last update
	 */
	private void _update(double delta) {
		window.preUpdate(delta);
		update(delta);
		fps(delta);
		activeScene.update(delta);
		window.postUpdate(delta);
	}

	private int framesThisSecond, frames, currentFPS, seconds, min_fps = Integer.MAX_VALUE, max_fps = 0;
	private double currentCount;

	private void fps(double delta) {
		currentCount += delta;
		framesThisSecond++;
		if (currentCount >= 1) {
			currentFPS = framesThisSecond;
			frames += currentFPS;
			seconds++;
			if (currentFPS < min_fps) {
				min_fps = currentFPS;
			}
			if (currentFPS > max_fps) {
				max_fps = currentFPS;
			}

			currentCount -= 1;
			framesThisSecond = 0;
		}
	}

	private boolean firstFrame = true;

	private void loop() {
		long time = System.nanoTime();
		while (window.shouldRun()) {
			if (firstFrame) {
				init();
				firstFrame = false;
			}
			_render();
			long temp = System.nanoTime();
			_update((temp - time) / (double) (1E+9));
			time = temp;
		}
	}

	/**
	 * Custom UI rendering implementation. Called every frame as part of the
	 * rendering process.
	 */
	public abstract void renderUI();

}
