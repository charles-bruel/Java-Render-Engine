package net.chazzvader.core.generic.engine.ui;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;

import com.sun.management.OperatingSystemMXBean;

import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.EngineItemRenderable;
import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.event.EventManager;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerKey;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerMouseButton;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerMouseMoved;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerScroll;
import net.chazzvader.core.generic.engine.render.MeshRendererImplementation;
import net.chazzvader.core.generic.engine.render.material.MaterialUI;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.math.Vector4f;

/**
 * Immediate mode UIRenderer.
 * 
 * @author csbru
 * @version 1
 * @since 1.0
 */
public class UIRenderer extends EngineItemRenderable {

	/**
	 * Creates a new renderer. The only real difference between renderer instance is
	 * they will have different {@link #uiPointer} and internally different meshes.
	 */
	public UIRenderer() {
		provider = Application.getInstance().getWindow().getRenderPipeline().getUIProvider();

		standardMaterial = new MaterialUI();
		standardRenderer = MeshRendererImplementation.getMeshImplementation(ObjectCreator.quadUI(), standardMaterial);

		eventStateInstance = new EventState();

		renderTextureInstance = new RenderTexture();
		renderTextCachedInstance = new RenderTextCached();
		renderTextQuickInstance = new RenderTextQuick();
		renderButtonInstance = new RenderButton();

		stackInstance = new Stack();
		diagnosticInstance = new Diagnostic();
	}

	private EventState eventStateInstance;

	private RenderTexture renderTextureInstance;
	private RenderTextQuick renderTextQuickInstance;
	private RenderTextCached renderTextCachedInstance;
	private RenderButton renderButtonInstance;

	private Stack stackInstance;
	private Diagnostic diagnosticInstance;

	private MaterialUI standardMaterial;
	private MeshRendererImplementation standardRenderer;

	/**
	 * Renders the standard quad with the settings currently applied to
	 * {@link #standardMaterial}. Not recommended to call directly.
	 */
	public void render() {
		standardMaterial.bind(null);
		standardRenderer.render();
	}

	/**
	 * Does any frame starting procedure.
	 */
	public void startFrame() {

	}

	/**
	 * Does any frame ending procedure.
	 */
	public void endFrame() {

	}

	/**
	 * Checks if the mouse is hovering over the area of the screen described by a
	 * <code>size</code> rectangle centered at <code>position</code>.
	 * 
	 * @param position The position to check.
	 * @param size     The size to check.
	 * @return True if the area is being hovered over.
	 */
	public boolean isHover(Vector2f position, Vector2f size) {
		Vector2f halfSize = size.mulCopy(0.5f);
		float mx = event().mousePosX;
		float my = event().mousePosY;
		return (mx > position.x - halfSize.x && mx < position.x + halfSize.x && my > position.y - halfSize.y
				&& my < position.y + halfSize.y);
	}

	private Vector2f pos = new Vector2f();
	private Matrix4f mat = new Matrix4f();

	private Matrix4f temp = new Matrix4f();
	private Matrix4f mulHelp = new Matrix4f();
	
	/**
	 * Generates a world matrix for position, size, and alignment.
	 * 
	 * @param position  The position to translate to.
	 * @param size      The size to scale to.
	 * @param alignment The alignment to translate with.
	 * @return The world matrix
	 */
	public Matrix4f generateWorldMatrix(Vector2f position, Vector2f size, Alignment alignment) {
		pos.x = position.x;
		pos.y = position.y;

		pos.mul(2);
		pos.x--;
		pos.y--;

		pos.x += alignment.x * size.x;// HORIZONTAL ALIGNMENT
		pos.y += alignment.y * size.y;// VERTICAL ALIGNMENT

		mat.reset();

//		Matrix4f toReturn = new Matrix4f();

		mat.applyTranslation(pos.x, pos.y, 0, temp, mulHelp);
		mat.applyScale(size.x, size.y, 0, temp, mulHelp);

		return mat;
	}

	/**
	 * Adjusts a size vector for the aspect ratio of the screen. I.e. a
	 * <code>0.1, 0.1</code> sized thing should be square, so it tweaks the size so
	 * it is, against the current aspect ratio.
	 * 
	 * @param size         The size.
	 * @param scalingBasis The scaling basis, what dimension to base against.
	 * @return The scaled size.
	 */
	public Vector2f adjustForAspectRatio(Vector2f size, ScalingBasis scalingBasis) {
		switch (scalingBasis) {
		case HEIGHT:
			size.x /= getWindowAspectRatio();
			break;
		case WIDTH:
			size.y *= getWindowAspectRatio();
			size.mul(9f / 16f);
			break;
		}
		return size;
	}

	/**
	 * The UI pointer is a position that is automatically updated to move, allowing
	 * you to put many elements in a column, or a row without having to worry about
	 * position.<br>
	 * <br>
	 * <em>May not work if the elements have different sizes!</em>
	 * 
	 * @see #uiPointerMode
	 * @see #uiPointerPadding
	 * @see #bumpUIPointer(Vector2f, Vector2f)
	 */
	public Vector2f uiPointer = new Vector2f(0.5f, 0.5f);

	/**
	 * The direction to bump the pointer after rendering.
	 * 
	 * @see #uiPointer
	 */
	public Direction uiPointerMode = Direction.NONE;

	/**
	 * The distance between elements.
	 * 
	 * @see #uiPointer
	 */
	public float uiPointerPadding = 0.01f;

	private boolean uiPointerLock = false;

	/**
	 * Locks the pointer
	 */
	public void lockUIPointer() {
		uiPointerLock = true;
	}

	/**
	 * Unlocks the pointer
	 */
	public void unlockUIPointer() {
		uiPointerLock = false;
	}

	/**
	 * Is the pointer locked in place.
	 * 
	 * @return True if the pointer is locked in place.
	 */
	public boolean isUIPointerLocked() {
		return uiPointerLock;
	}

	/**
	 * The method that actually move the pointer after rendering.
	 * 
	 * @param position The position of the just rendered element.
	 * @param size     The size of the just rendered element.
	 * @see #uiPointer
	 */
	public void bumpUIPointer(Vector2f size) {
		if (uiPointerLock) {
			return;
		}
		uiPointer.x += uiPointerMode.xMul * (size.x + uiPointerPadding);
		uiPointer.y += uiPointerMode.yMul * (size.y + uiPointerPadding);
	}

	/**
	 * Access to all diagnostic methods.
	 * 
	 * @return Access to all diagnostic methods.
	 */
	public Diagnostic diagnostic() {
		return diagnosticInstance;
	}

	/**
	 * Access to all stack methods. This contains the method for drawing a general
	 * diagnostic and all the supporting methods.
	 * 
	 * @author csbru
	 * @version 1
	 * @since 1.0
	 */
	public class Diagnostic {

		/**
		 * The height of diagnostic text.
		 */
		public static final float DIAGNOSTIC_TEXT_HEIGHT = 0.025f;

		private OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();

		private RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

		private long committedVirtualMemorySize, freePhysicalMemorySize, freeSwapSpaceSize, processCPUTime,
				totalPhysicalMemorySize, totalSwapSpaceSize, PID, upTime;
		private double processCPULoad, systemCPULoad;
		private String name, vmName, vmVendor, vmVersion, specName, specVendor, specVersion;

		{
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						committedVirtualMemorySize = operatingSystemMXBean.getCommittedVirtualMemorySize();
						freePhysicalMemorySize = operatingSystemMXBean.getFreeMemorySize();
						freeSwapSpaceSize = operatingSystemMXBean.getFreeSwapSpaceSize();
						processCPULoad = operatingSystemMXBean.getProcessCpuLoad();
						processCPUTime = operatingSystemMXBean.getProcessCpuTime();
						systemCPULoad = operatingSystemMXBean.getCpuLoad();
						totalPhysicalMemorySize = operatingSystemMXBean.getTotalMemorySize();
						totalSwapSpaceSize = operatingSystemMXBean.getTotalSwapSpaceSize();

						PID = runtimeMXBean.getPid();
						upTime = runtimeMXBean.getUptime();
						name = runtimeMXBean.getName();
						vmName = runtimeMXBean.getVmName();
						vmVendor = runtimeMXBean.getVmVendor();
						vmVersion = runtimeMXBean.getVmVersion();
						specName = runtimeMXBean.getSpecName();
						specVendor = runtimeMXBean.getSpecVendor();
						specVersion = runtimeMXBean.getSpecVersion();
					}
				}
			});
			thread.setName("Process Info Grabber");
			thread.setDaemon(true);
			thread.start();
		}

		/**
		 * Draws debug information. Uses a bunch of booleans to determine what parts to
		 * draw. String[] version.
		 * 
		 * @param position   The position to render the information. 0 for upper left, 1
		 *                   for lower left, 2 for lower right, 3 for upper right. <br>
		 *                   This has modulo applied to it so it should always be in
		 *                   bounds.
		 * @param fps        True if you want to render the FPS diagnostic.
		 * @param ram        True if you want to render the RAM diagnostic, with used
		 *                   memory and total memory.
		 * @param ramPercent True if you want to render the RAM diagnostic, with used
		 *                   memory and total percents.
		 * @param cpu        True if you want to render the CPU diagnostic.
		 * @param process    True if you want to render the process diagnostic, PID,
		 *                   name, etc.
		 * @param vm         True if you want to render the virtual machine diagnostic,
		 *                   like version and vendor.
		 * @param vmSpec     True if you want to render the specification diagnostic,
		 *                   like version and vendor.
		 * @param extras     Any extra data you wish to be drawn with the diagnostic.
		 *                   Each item will have a line. <br>
		 *                   Null is an allowed input.
		 * @param color      What color the text is.
		 */
		public void drawDiagnostic(int position, boolean fps, boolean ram, boolean ramPercent, boolean cpu,
				boolean process, boolean vm, boolean vmSpec, String[] extras, Vector3f color) {
			stack().UIPointerPush();

			Alignment alignment = Alignment.CENTER;

			position %= 4;
			switch (position) {
			case 0:
				uiPointer = new Vector2f(1, 1);
				uiPointerMode = Direction.DOWN;
				alignment = Alignment.UPPER_RIGHT;
				break;
			case 1:
				uiPointer = new Vector2f(1, 0);
				uiPointerMode = Direction.UP;
				alignment = Alignment.LOWER_RIGHT;
				break;
			case 2:
				uiPointer = new Vector2f(0, 0);
				uiPointerMode = Direction.UP;
				alignment = Alignment.LOWER_LEFT;
				break;
			case 3:
				uiPointer = new Vector2f(1, 1);
				uiPointerMode = Direction.DOWN;
				alignment = Alignment.UPPER_LEFT;
				break;
			}

			if (fps) {
				textQuick().drawText("FPS: " + Application.getInstance().getCurrentFPS(), uiPointer,
						DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
			}

			if (ram || ramPercent) {
				textQuick().drawText("Committed VRAM: " + committedVirtualMemorySize / 1048576 + "MB", uiPointer,
						DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
			}

			if (ram) {
				textQuick().drawText("Free RAM: " + freePhysicalMemorySize / 1048576 + "MB", uiPointer,
						DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				textQuick().drawText("Free Swap Space: " + freeSwapSpaceSize / 1048576 + "MB", uiPointer,
						DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				textQuick().drawText("Total RAM: " + totalPhysicalMemorySize / 1048576 + "MB", uiPointer,
						DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				textQuick().drawText("Total Swap Space: " + totalSwapSpaceSize / 1048576 + "MB", uiPointer,
						DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
			}

			if (ramPercent) {
				double ramFraction = ((double) freePhysicalMemorySize / totalPhysicalMemorySize);
				double swapSpaceFraction = ((double) freeSwapSpaceSize / totalSwapSpaceSize);
				textQuick().drawText("Free RAM: " + percent(ramFraction, 6), uiPointer, DIAGNOSTIC_TEXT_HEIGHT,
						alignment, color);
				textQuick().drawText("Free Swap Space: " + percent(swapSpaceFraction, 6), uiPointer,
						DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
			}

			if (cpu) {
				textQuick().drawText("CPU Time: " + processCPUTime, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment,
						color);
				textQuick().drawText("Process CPU Usage: " + percent(processCPULoad, 6), uiPointer, DIAGNOSTIC_TEXT_HEIGHT,
						alignment, color);
				textQuick().drawText("Total CPU Usage: " + percent(systemCPULoad, 6), uiPointer, DIAGNOSTIC_TEXT_HEIGHT,
						alignment, color);
			}

			if (process) {
				textQuick().drawText("Process PID: " + PID, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				textQuick().drawText("Process Name: " + name, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				textQuick().drawText("Process Uptime: " + upTime, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
			}

			if (vm) {
				textQuick().drawText("VM Name: " + vmName, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				textQuick().drawText("VM Version: " + vmVersion, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				textQuick().drawText("VM Vendor: " + vmVendor, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
			}

			if (vmSpec) {
				textQuick().drawText("Spec Name: " + specName, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				textQuick().drawText("Spec Version: " + specVersion, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment,
						color);
				textQuick().drawText("Spec Vendor: " + specVendor, uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
			}

			if (extras != null) {
				for (int i = 0; i < extras.length; i++) {
					textQuick().drawText(extras[i], uiPointer, DIAGNOSTIC_TEXT_HEIGHT, alignment, color);
				}
			}

			stack().UIPointerPop();
		}
		
		/**
		 * Draws debug information. Uses a bunch of booleans to determine what parts to
		 * draw. Varargs extra info version.
		 * 
		 * @param position   The position to render the information. 0 for upper left, 1
		 *                   for lower left, 2 for lower right, 3 for upper right. <br>
		 *                   This has modulo applied to it so it should always be in
		 *                   bounds.
		 * @param fps        True if you want to render the FPS diagnostic.
		 * @param ram        True if you want to render the RAM diagnostic, with used
		 *                   memory and total memory.
		 * @param ramPercent True if you want to render the RAM diagnostic, with used
		 *                   memory and total percents.
		 * @param cpu        True if you want to render the CPU diagnostic.
		 * @param process    True if you want to render the process diagnostic, PID,
		 *                   name, etc.
		 * @param vm         True if you want to render the virtual machine diagnostic,
		 *                   like version and vendor.
		 * @param vmSpec     True if you want to render the specification diagnostic,
		 *                   like version and vendor.
		 * @param color      What color the text is.
		 * @param extras     Any extra data you wish to be drawn with the diagnostic.
		 *                   Each item will have a line. <br>
		 *                   Null is an allowed input. 
		 */
		public void drawDiagnostic(int position, boolean fps, boolean ram, boolean ramPercent, boolean cpu,
				boolean process, boolean vm, boolean vmSpec, Vector3f color, String... extras) {
			this.drawDiagnostic(position, fps, ram, ramPercent, cpu, process, vm, vmSpec, extras, color);
		}

		private String percent(double value, int length) {
			String string = (value * 100) + "";
			return string.substring(0, Math.min(string.length(), length)) + "%";
		}
	}

	/**
	 * Access to all stack methods.
	 * 
	 * @return Access to all stack methods.
	 */
	public Stack stack() {
		return stackInstance;
	}

	/**
	 * Access to all stack methods. The methods allow you to store state for use
	 * later. Useful for doing things in subroutines, for example.
	 * 
	 * @author csbru
	 * @version 1
	 * @since 1.0
	 */
	public class Stack {

		private ArrayList<Vector2f> uiPointerStack = new ArrayList<>();
		private ArrayList<Direction> uiPointerModeStack = new ArrayList<>();
		private ArrayList<Float> uiPointerPaddingStack = new ArrayList<>();
		private ArrayList<Boolean> uiPointerLockStack = new ArrayList<>();

		/**
		 * Saves the state of the UI pointer and then resets it.
		 */
		public void UIPointerPush() {
			uiPointerStack.add(uiPointer);
			uiPointerModeStack.add(uiPointerMode);
			uiPointerPaddingStack.add(uiPointerPadding);
			uiPointerLockStack.add(uiPointerLock);

			uiPointer = new Vector2f(0.5f, 0.5f);
			uiPointerMode = Direction.NONE;
			uiPointerPadding = 0.01f;
			uiPointerLock = false;
		}

		/**
		 * Restores the state of the UI pointer to the last saved state.
		 * 
		 * @return True if the operation succeeded, false if the operation failed.
		 */
		public boolean UIPointerPop() {
			int index = uiPointerStack.size() - 1;
			if (index < 0) {
				Logging.log("Tried to pop UI pointer configuration off the stack but there was nothing!", "UI System",
						LoggingLevel.WARN);
				return false;
			}

			uiPointer = uiPointerStack.remove(index);
			uiPointerMode = uiPointerModeStack.remove(index);
			uiPointerPadding = uiPointerPaddingStack.remove(index);
			uiPointerLock = uiPointerLockStack.remove(index);

			return true;
		}

	}

	/**
	 * Access to all texture methods.
	 * 
	 * @return Access to all texture methods.
	 */
	public RenderTexture texture() {
		return renderTextureInstance;
	}

	/**
	 * Contains all texture rendering methods.
	 * 
	 * @author csbru
	 * @version 1
	 * @since 1.0
	 */
	public class RenderTexture {

		/**
		 * Renders a texture without some of the extra settings. Useful if you want a
		 * custom background, or other nonstandard options.
		 * 
		 * @param texture   The texture to draw.
		 * @param position  The position of the texture.
		 * @param size      The size of the texture.
		 * @param alignment The alignment of the texture
		 */
		public void drawTextureFinal(Texture texture, Vector2f position, Vector2f size, Alignment alignment) {
			standardMaterial.texture = texture;
			standardMaterial.world_matrix = generateWorldMatrix(position, size, alignment);
			render();
		}

		/**
		 * Renders a texture.
		 * 
		 * @param texture   The texture to draw.
		 * @param position  The position of the texture.
		 * @param size      The size of the texture.
		 * @param alignment The alignment of the texture
		 */
		public void drawTexture(Texture texture, Vector2f position, Vector2f size, Alignment alignment) {
			standardMaterial.background = Texture.TRANSPARENT;
			standardMaterial.border_size = 0;
			standardMaterial.color_multiplier = new Vector3f(1, 1, 1);
			drawTextureFinal(texture, position, size, alignment);
			bumpUIPointer(size);
		}

		/**
		 * Renders a texture.
		 * 
		 * @param texture   The texture to draw.
		 * @param x         The x position of the texture.
		 * @param y         The y position of the texture.
		 * @param w         The width of the texture.
		 * @param h         The height of the texture.
		 * @param alignment The alignment of the texture
		 */
		public void drawTexture(Texture texture, float x, float y, float w, float h, Alignment alignment) {
			drawTexture(texture, new Vector2f(x, y), new Vector2f(w, h), alignment);
		}

		/**
		 * Renders a texture.
		 * 
		 * @param texture  The texture to draw.
		 * @param position The position of the texture.
		 * @param size     The size of the texture.
		 */
		public void drawTexture(Texture texture, Vector2f position, Vector2f size) {
			drawTexture(texture, position, size, Alignment.CENTER);
		}

		/**
		 * Renders a texture.
		 * 
		 * @param texture The texture to draw.
		 * @param x       The x position of the texture.
		 * @param y       The y position of the texture.
		 * @param w       The width of the texture.
		 * @param h       The height of the texture.
		 */
		public void drawTexture(Texture texture, float x, float y, float w, float h) {
			drawTexture(texture, x, y, w, h, Alignment.CENTER);
		}

		/**
		 * Renders a texture.
		 * 
		 * @param texture The texture to draw.
		 * @param x       The x position of the texture.
		 * @param y       The y position of the texture.
		 * @param size    The size of the texture.
		 * @param basis   What dimension is the basis for sizing.
		 */
		public void drawTexture(Texture texture, float x, float y, float size, ScalingBasis basis) {
			drawTexture(texture, new Vector2f(x, y), autoSize(texture, size, basis), Alignment.CENTER);
		}

		/**
		 * Renders a texture.
		 * 
		 * @param texture  The texture to draw.
		 * @param position The position of the texture.
		 * @param size     The size of the texture.
		 * @param basis    What dimension is the basis for sizing.
		 */
		public void drawTexture(Texture texture, Vector2f position, float size, ScalingBasis basis) {
			drawTexture(texture, position, autoSize(texture, size, basis), Alignment.CENTER);
		}

		/**
		 * Renders a texture.
		 * 
		 * @param texture   The texture to draw.
		 * @param x         The x position of the texture.
		 * @param y         The y position of the texture.
		 * @param size      The size of the texture.
		 * @param basis     What dimension is the basis for sizing.
		 * @param alignment The alignment of the texture.
		 */
		public void drawTexture(Texture texture, float x, float y, float size, ScalingBasis basis,
				Alignment alignment) {
			drawTexture(texture, new Vector2f(x, y), autoSize(texture, size, basis), alignment);
		}

		/**
		 * Renders a texture.
		 * 
		 * @param texture   The texture to draw.
		 * @param position  The position of the texture.
		 * @param size      The size of the texture.
		 * @param basis     What dimension is the basis for sizing.
		 * @param alignment The alignment of the texture.
		 */
		public void drawTexture(Texture texture, Vector2f position, float size, ScalingBasis basis,
				Alignment alignment) {
			drawTexture(texture, position, autoSize(texture, size, basis), alignment);
		}

		/**
		 * Auto size based on the texture size and the given size.
		 * 
		 * @param texture The texture
		 * @param size    The size of the texture
		 * @param basis   What dimension is the basis for sizing.
		 * @return The correct size.
		 */
		public Vector2f autoSize(Texture texture, float size, ScalingBasis basis) {
			Vector2f adjust = new Vector2f(size * texture.getWidth() / texture.getHeight(), size);
			return adjustForAspectRatio(adjust, basis);
		}
	}

	/**
	 * Access to all quick text methods. These methods are fast but support nothing
	 * beyond ASCII and have no styling options. Use for quickly updating text, such
	 * as fps, distance to objective and health.
	 * 
	 * @return Access to all text methods.
	 */
	public RenderTextQuick textQuick() {
		return renderTextQuickInstance;
	}

	/**
	 * Contains all quick text rendering methods. These methods are fast but support
	 * nothing beyond ASCII and have no styling options beyond color changing. Use
	 * for quickly updating text, such as fps, distance to objective and health.
	 * 
	 * @author csbru
	 * @version 1
	 * @since 1.0
	 */
	public class RenderTextQuick {

		Texture atlas = TextureCreator.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\atlas.png");
		float[] width = new float[] { 0.359375f, 0.5f, 0.41796875f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
				0.5f, 0.4921875f, 0.07421875f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
				0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.265625f, 0.21875f, 0.296875f, 0.45703125f, 0.4140625f,
				0.62890625f, 0.6171875f, 0.17578125f, 0.23046875f, 0.23046875f, 0.3203125f, 0.52734375f, 0.1640625f,
				0.30859375f, 0.1640625f, 0.296875f, 0.4140625f, 0.4140625f, 0.4140625f, 0.4140625f, 0.4140625f,
				0.4140625f, 0.4140625f, 0.4140625f, 0.4140625f, 0.4140625f, 0.1640625f, 0.1640625f, 0.52734375f,
				0.52734375f, 0.52734375f, 0.34765625f, 0.73828125f, 0.49609375f, 0.44140625f, 0.48046875f, 0.5390625f,
				0.390625f, 0.375f, 0.52734375f, 0.546875f, 0.203125f, 0.26953125f, 0.4453125f, 0.36328125f, 0.69140625f,
				0.578125f, 0.58203125f, 0.4296875f, 0.58203125f, 0.4609375f, 0.41015625f, 0.40234375f, 0.53125f,
				0.4765625f, 0.71875f, 0.453125f, 0.42578125f, 0.4375f, 0.23046875f, 0.57421875f, 0.23046875f,
				0.52734375f, 0.3203125f, 0.203125f, 0.390625f, 0.453125f, 0.35546875f, 0.453125f, 0.40234375f,
				0.23828125f, 0.453125f, 0.4375f, 0.18359375f, 0.18359375f, 0.3828125f, 0.18359375f, 0.6640625f, 0.4375f,
				0.453125f, 0.453125f, 0.453125f, 0.265625f, 0.328125f, 0.26171875f, 0.4375f, 0.3671875f, 0.5546875f,
				0.3515625f, 0.37109375f, 0.34765625f, 0.23046875f, 0.1796875f, 0.23046875f, 0.52734375f, 0.23046875f,
				0.4140625f, 0.5f, 0.19921875f, 0.4140625f, 0.19921875f, 0.48046875f, 0.4765625f, 0.4765625f,
				0.28515625f, 0.5859375f, 0.41015625f, 0.28515625f, 0.71875f, 0.5f, 0.4375f, 0.5f, 0.5f, 0.234375f,
				0.234375f, 0.234375f, 0.234375f, 0.2265625f, 0.07421875f, 0.07421875f, 0.2578125f, 0.28125f, 0.328125f,
				0.28515625f, 0.71875f, 0.5f, 0.34765625f, 0.42578125f, 0.265625f, 0.21875f, 0.4140625f, 0.4140625f,
				0.4296875f, 0.4140625f, 0.1796875f, 0.34375f, 0.31640625f, 0.6875f, 0.30078125f, 0.38671875f,
				0.52734375f, 0.30859375f, 0.6875f, 0.33203125f, 0.29296875f, 0.52734375f, 0.28125f, 0.28125f, 0.21875f,
				0.4453125f, 0.3515625f, 0.1640625f, 0.16015625f, 0.26953125f, 0.33203125f, 0.38671875f, 0.69140625f,
				0.71875f, 0.71484375f, 0.34765625f, 0.49609375f, 0.49609375f, 0.49609375f, 0.49609375f, 0.49609375f,
				0.49609375f, 0.66015625f, 0.48046875f, 0.390625f, 0.390625f, 0.390625f, 0.390625f, 0.203125f, 0.203125f,
				0.203125f, 0.203125f, 0.5390625f, 0.578125f, 0.58203125f, 0.58203125f, 0.58203125f, 0.58203125f,
				0.58203125f, 0.52734375f, 0.58203125f, 0.53125f, 0.53125f, 0.53125f, 0.53125f, 0.42578125f, 0.4296875f,
				0.41796875f, 0.390625f, 0.390625f, 0.390625f, 0.390625f, 0.390625f, 0.390625f, 0.64453125f, 0.35546875f,
				0.40234375f, 0.40234375f, 0.40234375f, 0.40234375f, 0.18359375f, 0.18359375f, 0.18359375f, 0.18359375f,
				0.4296875f, 0.4375f, 0.453125f, 0.453125f, 0.453125f, 0.453125f, 0.453125f, 0.52734375f, 0.453125f,
				0.4375f, 0.4375f, 0.4375f, 0.4375f, 0.37109375f, 0.453125f, 0.37109375f };

		private Vector2f size = new Vector2f();
		private Vector4f texCoord = new Vector4f();
		private Vector4f defaultTexCoord = new Vector4f(0, 0, 1, 1);
		private Vector3f defaultColorMul = new Vector3f(1, 1, 1);

		/**
		 * Draws a character for use with text rendering. This method is not intended to
		 * be called directly, although it can.
		 * 
		 * @param character The character to render.
		 * @param pos       The position of the left center of the character.
		 * @param height    The height of the character.
		 */
		public void drawCharacter(int character, Vector2f pos, float height) {
			float texX = character % 16;
			float texY = character / 16;
			texCoord.x = texX / 16f;
			texCoord.y = texY / 16f;
			texCoord.z = (texX + width[character]) / 16f;
			texCoord.w = (texY + 1) / 16f;
			standardMaterial.overrideTexCoords = texCoord;
			size.x = height * width[character] / getWindowAspectRatio();
			size.y = height;
			texture().drawTextureFinal(atlas, pos, size, Alignment.LEFT);
			standardMaterial.overrideTexCoords = defaultTexCoord;
		}

		/**
		 * The is the core draw method, read parameters carefully.<br>
		 * Draws a text string, assumed scaling basis to be <code>HEIGHT</code> and
		 * alignment to be <code>LEFT</code>.
		 * 
		 * @param text   The text to draw.
		 * @param pos    The position of the text.
		 * @param height The height a standard character.
		 * @param color  The color of the text.
		 */
		public void drawTextFinal(String text, Vector2f pos, float height, Vector3f color) {
			lockUIPointer();
			pos = pos.copy();
			char[] chars = text.toCharArray();
			standardMaterial.color_multiplier = color;
			for (int i = 0; i < chars.length; i++) {
				int currentIndex = chars[i];
				if (currentIndex > 255) {
					currentIndex = 2;/* Turn unknown into a blank character. */
				}
				drawCharacter(currentIndex, pos, height);
				pos.x += width[currentIndex] * height / getWindowAspectRatio();
			}
			standardMaterial.color_multiplier = defaultColorMul;
			unlockUIPointer();
			bumpUIPointer(getTextSize(text, height / 2f));
		}

		/**
		 * Draws some text.
		 * 
		 * @param text      The text to draw.
		 * @param pos       The position of the text.
		 * @param height    The height a standard character.
		 * @param basis     The scaling basis of the text.
		 * @param alignment The alignment of the text.
		 * @param color     The color of the text.
		 */
		public void drawText(String text, Vector2f pos, float height, ScalingBasis basis, Alignment alignment,
				Vector3f color) {
			switch (basis) {
			case HEIGHT:
				break;
			case WIDTH:
				height = (9f * height * getWindowAspectRatio()) / 16f;
				break;
			}
			Vector2f finalSize = getTextSize(text, height);
			pos = pos.copy();
			pos.x += (alignment.x - 1) * finalSize.x / 2;/* HORIZONTAL ALIGNMENT TO LEFT */
			pos.y += alignment.y * finalSize.y / 2;/* VERTICAL ALIGNMENT */
			drawTextFinal(text, pos, height, color);
		}

		/**
		 * Draws some text. The alignment is assumed to be <code>CENTER</code>.
		 * 
		 * @param text   The text to draw.
		 * @param pos    The position of the text.
		 * @param height The height a standard character.
		 * @param basis  The scaling basis of the text.
		 * @param color  The color of the text.
		 */
		public void drawText(String text, Vector2f pos, float height, ScalingBasis basis, Vector3f color) {
			drawText(text, pos, height, basis, Alignment.CENTER, color);
		}

		/**
		 * Draws some text. The scaling basis is assumed to be <code>HEIGHT</code>.
		 * 
		 * @param text      The text to draw.
		 * @param pos       The position of the text.
		 * @param height    The height a standard character.
		 * @param alignment The alignment of the text.
		 * @param color     The color of the text.
		 */
		public void drawText(String text, Vector2f pos, float height, Alignment alignment, Vector3f color) {
			drawText(text, pos, height, ScalingBasis.HEIGHT, alignment, color);
		}

		/**
		 * Draws some text. The scaling basis is assumed to be <code>HEIGHT</code> and
		 * the alignment is assumed to be <code>CENTER</code>.
		 * 
		 * @param text   The text to draw.
		 * @param pos    The position of the text.
		 * @param height The height a standard character.
		 * @param color  The color of the text.
		 */
		public void drawText(String text, Vector2f pos, float height, Vector3f color) {
			drawText(text, pos, height, ScalingBasis.HEIGHT, Alignment.CENTER, color);
		}

		/**
		 * Draws some text.
		 * 
		 * @param text      The text to draw.
		 * @param x         The x position of the text.
		 * @param y         The y position of the text.
		 * @param height    The height a standard character.
		 * @param basis     The scaling basis of the text.
		 * @param alignment The alignment of the text.
		 * @param color     The color of the text.
		 */
		public void drawText(String text, float x, float y, float height, ScalingBasis basis, Alignment alignment,
				Vector3f color) {
			drawText(text, new Vector2f(x, y), height, basis, alignment, color);
		}

		/**
		 * Draws some text. The alignment is assumed to be <code>CENTER</code>.
		 * 
		 * @param text   The text to draw.
		 * @param x      The x position of the text.
		 * @param y      The y position of the text.
		 * @param height The height a standard character.
		 * @param basis  The scaling basis of the text.
		 * @param color  The color of the text.
		 */
		public void drawText(String text, float x, float y, float height, ScalingBasis basis, Vector3f color) {
			drawText(text, new Vector2f(x, y), height, basis, Alignment.CENTER, color);
		}

		/**
		 * Draws some text. The scaling basis is assumed to be <code>HEIGHT</code>.
		 * 
		 * @param text      The text to draw.
		 * @param x         The x position of the text.
		 * @param y         The y position of the text.
		 * @param height    The height a standard character.
		 * @param alignment The alignment of the text.
		 * @param color     The color of the text.
		 */
		public void drawText(String text, float x, float y, float height, Alignment alignment, Vector3f color) {
			drawText(text, new Vector2f(x, y), height, ScalingBasis.HEIGHT, alignment, color);
		}

		/**
		 * Draws some text. The scaling basis is assumed to be <code>HEIGHT</code> and
		 * the alignment is assumed to be <code>CENTER</code>.
		 * 
		 * @param text   The text to draw.
		 * @param x      The x position of the text.
		 * @param y      The y position of the text.
		 * @param height The height a standard character.
		 * @param color  The color of the text.
		 */
		public void drawText(String text, float x, float y, float height, Vector3f color) {
			drawText(text, new Vector2f(x, y), height, ScalingBasis.HEIGHT, Alignment.CENTER, color);
		}

		/**
		 * Draws some text.
		 * 
		 * @param text      The text to draw.
		 * @param pos       The position of the text.
		 * @param height    The height a standard character.
		 * @param basis     The scaling basis of the text.
		 * @param alignment The alignment of the text.
		 */
		public void drawText(String text, Vector2f pos, float height, ScalingBasis basis, Alignment alignment) {
			drawText(text, pos, height, basis, alignment, new Vector3f(0, 0, 0));
		}

		/**
		 * Draws some text.
		 * 
		 * @param text   The text to draw.
		 * @param pos    The position of the text.
		 * @param height The height a standard character.
		 * @param basis  The scaling basis of the text.
		 */
		public void drawText(String text, Vector2f pos, float height, ScalingBasis basis) {
			drawText(text, pos, height, basis, new Vector3f(0, 0, 0));
		}

		/**
		 * Draws some text.
		 * 
		 * @param text      The text to draw.
		 * @param pos       The position of the text.
		 * @param height    The height a standard character.
		 * @param alignment The alignment of the text.
		 */
		public void drawText(String text, Vector2f pos, float height, Alignment alignment) {
			drawText(text, pos, height, alignment, new Vector3f(0, 0, 0));
		}

		/**
		 * Draws some text.
		 * 
		 * @param text   The text to draw.
		 * @param pos    The position of the text.
		 * @param height The height a standard character.
		 */
		public void drawText(String text, Vector2f pos, float height) {
			drawText(text, pos, height, new Vector3f(0, 0, 0));
		}

		/**
		 * Draws some text.
		 * 
		 * @param text      The text to draw.
		 * @param x         The x position of the text.
		 * @param y         The y position of the text.
		 * @param height    The height a standard character.
		 * @param basis     The scaling basis of the text.
		 * @param alignment The alignment of the text.
		 */
		public void drawText(String text, float x, float y, float height, ScalingBasis basis, Alignment alignment) {
			drawText(text, x, y, height, basis, alignment, new Vector3f(0, 0, 0));
		}

		/**
		 * Draws some text.
		 * 
		 * @param text   The text to draw.
		 * @param x      The x position of the text.
		 * @param y      The y position of the text.
		 * @param height The height a standard character.
		 * @param basis  The scaling basis of the text.
		 */
		public void drawText(String text, float x, float y, float height, ScalingBasis basis) {
			drawText(text, x, y, height, basis, new Vector3f(0, 0, 0));
		}

		/**
		 * Draws some text.
		 * 
		 * @param text      The text to draw.
		 * @param x         The x position of the text.
		 * @param y         The y position of the text.
		 * @param height    The height a standard character.
		 * @param alignment The alignment of the text.
		 */
		public void drawText(String text, float x, float y, float height, Alignment alignment) {
			drawText(text, x, y, height, alignment, new Vector3f(0, 0, 0));
		}

		/**
		 * Draws some text.
		 * 
		 * @param text   The text to draw.
		 * @param x      The x position of the text.
		 * @param y      The y position of the text.
		 * @param height The height a standard character.
		 */
		public void drawText(String text, float x, float y, float height) {
			drawText(text, x, y, height, new Vector3f(0, 0, 0));
		}

		/**
		 * Returns the size of the text with a given height.
		 * 
		 * @param text   The text to check.
		 * @param height The height of the text.
		 * @return The size of the text if drawn.
		 */
		public Vector2f getTextSize(String text, float height) {
			return new Vector2f(getTextSize(text) * height, height);
		}

		/**
		 * Returns the width of the text <em>given a height of one</em>. This means this
		 * functions more as an aspect ratio.
		 * 
		 * @param text The text to check.
		 * @return The width of the text.
		 */
		public float getTextSize(String text) {
			float textWidth = 0;
			char[] chars = text.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				int currentIndex = chars[i];
				if (currentIndex > 255) {
					currentIndex = 2;/* Turn unknown into a blank character. */
				}
				textWidth += width[currentIndex] / getWindowAspectRatio();
			}
			return textWidth;
		}
	}

	/**
	 * Access to all caching text methods. These methods are slow but support many
	 * more characters, better formatting and are also planned to support italics,
	 * bold, and more fonts. For the most performance, avoid using these unless
	 * absolutely necessary, and avoid updating them every frame.
	 * 
	 * @return Access to all text methods.
	 */
	public RenderTextCached textCached() {
		return renderTextCachedInstance;
	}

	/**
	 * Contains all cached text rendering methods. These methods are slow but
	 * support many more characters, better formatting and are also planned to
	 * support italics, bold, and more fonts. For the most performance, avoid using
	 * these unless absolutely necessary, and avoid updating them every frame.
	 * 
	 * @author csbru
	 * @version 1
	 * @since 1.0
	 */
	public class RenderTextCached {

		/**
		 * Renders text.
		 * 
		 * @param context      The text context is used to store the text to avoid
		 *                     recreating the texture frequently for performance
		 *                     reasons.
		 * @param text         The text to draw.
		 * @param position     The position to draw the text.
		 * @param size         The size of the texture.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @param alignment    The alignment of the texture.
		 * @return The texture that was used to render.
		 * @see TextRendererContext
		 */
		public Texture drawText(TextRendererContext context, String text, Vector2f position, float size,
				ScalingBasis scalingBasis, Alignment alignment) {
			float finalHeight = size;
			switch (scalingBasis) {
			case HEIGHT:
				break;
			case WIDTH:
				finalHeight = (9f * finalHeight * getWindowAspectRatio()) / 16f;
				break;
			}
			Texture tempTexture = context.get(UIRenderer.this, finalHeight, scalingBasis, text);
			texture().drawTexture(tempTexture, position.x, position.y, size, scalingBasis, alignment);
			return tempTexture;
		}

		/**
		 * Renders text.
		 * 
		 * @param context      The text context is used to store the text to avoid
		 *                     recreating the texture frequently for performance
		 *                     reasons.
		 * @param text         The text to draw.
		 * @param x            The x position of the text.
		 * @param y            The y position of the text.
		 * @param size         The size of the texture.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @param alignment    The alignment of the texture.
		 * @return The texture that was used to render.
		 * @see TextRendererContext
		 */
		public Texture drawText(TextRendererContext context, String text, float x, float y, float size,
				ScalingBasis scalingBasis, Alignment alignment) {
			return drawText(context, text, new Vector2f(x, y), size, scalingBasis, alignment);
		}

		/**
		 * Renders text. The TextRendererContext is missing, <em>no texture caching will
		 * occur</em>. Avoid using this outside of testing purpose, or where the text
		 * will change every frame anyway.
		 * 
		 * @param text         The text to draw.
		 * @param position     The position to draw the text.
		 * @param size         The size of the texture.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @param alignment    The alignment of the texture.
		 * @return The texture that was used to render.
		 */
		public Texture drawText(String text, Vector2f position, float size, ScalingBasis scalingBasis,
				Alignment alignment) {
			TextRendererContext temp = new TextRendererContext();
			Texture tempTexture = drawText(temp, text, position, size, scalingBasis, alignment);
			temp.delete();
			return tempTexture;
		}

		/**
		 * Renders text. The TextRendererContext is missing, <em>no texture caching will
		 * occur</em>. Avoid using this outside of testing purpose, or where the text
		 * will change every frame anyway.
		 * 
		 * @param text         The text to draw.
		 * @param x            The x position of the text.
		 * @param y            The y position of the text.
		 * @param size         The size of the texture.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @param alignment    The alignment of the texture.
		 * @return The texture that was used to render.
		 */
		public Texture drawText(String text, float x, float y, float size, ScalingBasis scalingBasis,
				Alignment alignment) {
			TextRendererContext temp = new TextRendererContext();
			Texture tempTexture = drawText(temp, text, new Vector2f(x, y), size, scalingBasis, alignment);
			temp.delete();
			return tempTexture;
		}

		/**
		 * Renders text.
		 * 
		 * @param context      The text context is used to store the text to avoid
		 *                     recreating the texture frequently for performance
		 *                     reasons.
		 * @param text         The text to draw.
		 * @param position     The position to draw the text.
		 * @param size         The size of the texture.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @see TextRendererContext
		 * @return The texture that was used to render.
		 */
		public Texture drawText(TextRendererContext context, String text, Vector2f position, float size,
				ScalingBasis scalingBasis) {
			return drawText(context, text, position, size, scalingBasis, Alignment.CENTER);
		}

		/**
		 * Renders text.
		 * 
		 * @param context      The text context is used to store the text to avoid
		 *                     recreating the texture frequently for performance
		 *                     reasons.
		 * @param text         The text to draw.
		 * @param x            The x position of the text.
		 * @param y            The y position of the text.
		 * @param size         The size of the texture.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @see TextRendererContext
		 * @return The texture that was used to render.
		 */
		public Texture drawText(TextRendererContext context, String text, float x, float y, float size,
				ScalingBasis scalingBasis) {
			return drawText(context, text, x, y, size, scalingBasis, Alignment.CENTER);
		}

		/**
		 * Renders text. The TextRendererContext is missing, <em>no texture caching will
		 * occur</em>. Avoid using this outside of testing purpose, or where the text
		 * will change every frame anyway.
		 * 
		 * @param text         The text to draw.
		 * @param position     The position to draw the text.
		 * @param size         The size of the texture.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @return The texture that was used to render.
		 */
		public Texture drawText(String text, Vector2f position, float size, ScalingBasis scalingBasis) {
			return drawText(text, position, size, scalingBasis, Alignment.CENTER);
		}

		/**
		 * Renders text. The TextRendererContext is missing, <em>no texture caching will
		 * occur</em>. Avoid using this outside of testing purpose, or where the text
		 * will change every frame anyway.
		 * 
		 * @param text         The text to draw.
		 * @param x            The x position of the text.
		 * @param y            The y position of the text.
		 * @param size         The size of the texture.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @return The texture that was used to render.
		 */
		public Texture drawText(String text, float x, float y, float size, ScalingBasis scalingBasis) {
			return drawText(text, x, y, size, scalingBasis, Alignment.CENTER);
		}

	}

	/**
	 * Access to all button methods.
	 * 
	 * @return Access to all button methods.
	 */
	public RenderButton button() {
		return renderButtonInstance;
	}

	/**
	 * Contains all button rendering methods.
	 * 
	 * @author csbru
	 * @version 1
	 * @since 1.0
	 */
	public class RenderButton {

		/**
		 * The standard center non hovered texture for a button.
		 */
		public Texture CENTER = TextureCreator
				.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\button_center.png");
		/**
		 * The standard left non hovered texture for a button.
		 */
		public Texture LEFT = TextureCreator
				.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\button_left.png");
		/**
		 * The standard right non hovered texture for a button.
		 */
		public Texture RIGHT = TextureCreator
				.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\button_right.png");

		/**
		 * The standard center hovered texture for a button.
		 */
		public Texture CENTER_HOVER = TextureCreator
				.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\button_hover_center.png");
		/**
		 * The standard left hovered texture for a button.
		 */
		public Texture LEFT_HOVER = TextureCreator
				.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\button_hover_left.png");
		/**
		 * The standard right hovered texture for a button.
		 */
		public Texture RIGHT_HOVER = TextureCreator
				.fromFile("net\\chazzvader\\core\\generic\\engine\\ui\\res\\button_hover_right.png");

		/**
		 * The standard array of textures.
		 */
		public Texture[] TEXTURES = new Texture[] { CENTER, LEFT, RIGHT, CENTER_HOVER, LEFT_HOVER, RIGHT_HOVER };

		/**
		 * Draws a button.
		 * 
		 * @param textContext  The text context to use to render the button.
		 * @param text         The text to display on the button.
		 * @param position     The position of the button.
		 * @param size         The size of the button.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @param alignment    The alignment of the button.
		 * @param textures     The set of textures used to remember. Must be 6 elements
		 *                     and the pattern is as follows:<br>
		 *                     <code>{ CENTER, LEFT, RIGHT, CENTER_HOVER, LEFT_HOVER, RIGHT_HOVER };</code>
		 * @param color        The color of the text.
		 * @return True if the button was pressed.
		 */
		@SuppressWarnings("incomplete-switch")
		public boolean drawButton(String text, Vector2f position, float size, ScalingBasis scalingBasis,
				Alignment alignment, Texture[] textures, Vector3f color) {

			/* 1: ELIMATE SCALING BASIS */

			if (scalingBasis == ScalingBasis.WIDTH) {
				size = (9f * size * getWindowAspectRatio()) / 16f;
			}

			/* 2: ELEMINATE VERTICAL ALIGNMENT */

			position = position.copy();

			position.y += alignment.y * size / 2;

			alignment = Alignment.eliminateVertical(alignment);

			/* 3: DETERMINE HORIZONTAL OFFSET */

			switch (alignment) {
			case CENTER:
				break;
			case LEFT:
				position.x += ((float) textures[1].getWidth() / (getWindowAspectRatio() * textures[1].getHeight()))
						* size;
				break;
			case RIGHT:
				position.x -= ((float) textures[2].getWidth() / (getWindowAspectRatio() * textures[2].getHeight()))
						* size;
				break;
			}

			/* 4: RENDER TEXT */

			textQuick().drawText(text, position, size, ScalingBasis.HEIGHT, alignment, color);
			Vector2f textSize = textQuick().getTextSize(text, size);
			textSize.x *= 2;

			lockUIPointer();

			/* 5: READJUST TO CENTER */

			float offsetWidth = (size * textSize.x) / (2 * textSize.y * getWindowAspectRatio());

			position.x += alignment.x * offsetWidth;

			/*
			 * 6: DETERMINE HOVER
			 */

			float aspectRatio = (float) textSize.x / textSize.y;

			Texture tempLeft = textures[1];
			Texture tempRight = textures[2];

			Vector2f fullSize = new Vector2f(0, size);
			fullSize.x += (aspectRatio * size) / getWindowAspectRatio();// CENTER
			fullSize.x += (tempLeft.getWidth() * size) / (getWindowAspectRatio() * tempLeft.getHeight());// LEFT
			fullSize.x += (tempRight.getWidth() * size) / (getWindowAspectRatio() * tempRight.getHeight());// RIGHT

			boolean isHover = isHover(position, fullSize);// Finds the button textures
			int index = isHover ? 3 : 0;
			Texture center = textures[index++];
			Texture left = textures[index++];
			Texture right = textures[index++];

			/* 7: DRAW BUTTON */

			texture().drawTexture(center, position.x, position.y, (aspectRatio * size) / getWindowAspectRatio(), size);
			float leftAdjAspectRatio = (left.getWidth() * size) / (getWindowAspectRatio() * left.getHeight());
			texture().drawTexture(left, position.x - offsetWidth - leftAdjAspectRatio / 2, position.y,
					leftAdjAspectRatio, size);
			float rightAdjAspectRatio = (right.getWidth() * size) / (getWindowAspectRatio() * right.getHeight());
			texture().drawTexture(right, position.x + offsetWidth + rightAdjAspectRatio / 2, position.y,
					rightAdjAspectRatio, size);

			unlockUIPointer();

			bumpUIPointer(fullSize);

			return false;
		}

		/**
		 * Draws a button.<br>
		 * The default texture set is used.
		 * 
		 * @param textContext  The text context to use to render the button.
		 * @param text         The text to display on the button.
		 * @param position     The position of the button.
		 * @param size         The size of the button.
		 * @param scalingBasis What dimension is the basis for sizing.
		 * @param alignment    The alignment of the button.
		 * @return True if the button was pressed.
		 */
		public boolean drawButton(String text, Vector2f position, float size, ScalingBasis scalingBasis,
				Alignment alignment) {
			return drawButton(text, position, size, scalingBasis, alignment, TEXTURES, new Vector3f(0, 0, 0));
		}

	}

	/**
	 * Access to all event state methods.
	 * 
	 * @return Access to all event state methods.
	 */
	public EventState event() {
		return eventStateInstance;
	}

	@SuppressWarnings("javadoc")
	public class EventState
			implements IEventHandlerKey, IEventHandlerMouseButton, IEventHandlerMouseMoved, IEventHandlerScroll {

		public EventState() {
			EventManager.registerEventListener(this, 2);
		}

		private float mousePosX, mousePosY, scrollWheelOffsetX, scrollWheelOfssetY, mouseOffsetRelX, mouseOffsetRelY,
				mouseOffsetAbsX, mouseOffsetAbsY;

		@Override
		public boolean scrollWheel(float xoffset, float yoffset) {
			scrollWheelOffsetX = xoffset;
			scrollWheelOfssetY = yoffset;
			return false;
		}

		@Override
		public boolean updateMouseAbsolutePosition(int x, int y) {
			mousePosX = x / (float) getWindowWidth();
			mousePosY = y / (float) getWindowHeight();
			return false;
		}

		@Override
		public boolean updateMouseOffset(int x, int y) {
			mouseOffsetAbsX = x;
			mouseOffsetAbsY = y;
			mouseOffsetRelX = x / (float) getWindowWidth();
			mouseOffsetRelY = y / (float) getWindowHeight();
			return false;
		}

		@Override
		public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock, int clickCount) {
			return false;
		}

		@Override
		public boolean mouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			return false;
		}

		@Override
		public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			return false;
		}

		@Override
		public boolean keyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			return false;
		}

		public float getMousePosX() {
			return this.mousePosX;
		}

		public float getMousePosY() {
			return this.mousePosY;
		}

		public float getScrollWheelOffsetX() {
			return this.scrollWheelOffsetX;
		}

		public float getScrollWheelOfssetY() {
			return this.scrollWheelOfssetY;
		}

		public float getMouseOffsetRelX() {
			return this.mouseOffsetRelX;
		}

		public float getMouseOffsetRelY() {
			return this.mouseOffsetRelY;
		}

		public float getMouseOffsetAbsX() {
			return this.mouseOffsetAbsX;
		}

		public float getMouseOffsetAbsY() {
			return this.mouseOffsetAbsY;
		}

	}

}