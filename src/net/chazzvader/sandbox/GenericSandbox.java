package net.chazzvader.sandbox;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.glfw.GLFW;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.Logging;
import net.chazzvader.core.generic.Logging.LoggingLevel;
import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.Scene;
import net.chazzvader.core.generic.engine.Window;
import net.chazzvader.core.generic.engine.creator.OBJLoader;
import net.chazzvader.core.generic.engine.creator.ObjectCreator;
import net.chazzvader.core.generic.engine.creator.TextureCreator;
import net.chazzvader.core.generic.engine.event.EventManager;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerKey;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerMouseButton;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerMouseMoved;
import net.chazzvader.core.generic.engine.mesh.Mesh;
import net.chazzvader.core.generic.engine.object.Camera;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.engine.object.EngineObjectMesh;
import net.chazzvader.core.generic.engine.object.IScript;
import net.chazzvader.core.generic.engine.object.Light;
import net.chazzvader.core.generic.engine.object.text.EngineObjectTextNonUI;
import net.chazzvader.core.generic.engine.render.material.MaterialBasic;
import net.chazzvader.core.generic.engine.render.material.Texture;
import net.chazzvader.core.generic.engine.ui.Alignment;
import net.chazzvader.core.generic.engine.ui.Direction;
import net.chazzvader.core.generic.engine.ui.ScalingBasis;
import net.chazzvader.core.generic.engine.ui.TextRendererContext;
import net.chazzvader.core.generic.engine.ui.UIRenderer;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Quaternion;
import net.chazzvader.core.generic.math.Vector2f;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.generic.util.Utils;

@SuppressWarnings("javadoc")
public class GenericSandbox extends Application {

	private static GenericSandbox instance;

	public static final float FWD_SPEED = 2;
	public static final float BWD_SPEED = 1;
	public static final float SIDEWAYS_SPEED = 1.5f;
	public static final float MOUSE_SENSITIVITY = 0.04f;

	public static void main(String[] args) {
		instance = new GenericSandbox();
	}

	public GenericSandbox() {
		Logging.log("Starting up!", "Main", LoggingLevel.INFO);
		Configuration.setApplicationName("Sandbox App");
		Configuration.setDebugMode(true);
		Configuration.setRenderer(Renderer.OPEN_GL);
		Configuration.rendererFinalize();
		Configuration.assertRenderer(Renderer.OPEN_GL);
		Window.create(1280, 720, this);
		EventManager.registerEventListener(new CustomEventHandler(), 14);
		start();
	}

	public static GenericSandbox getInstance() {
		return instance;
	}
	
	@Override
	protected void preInit() {
		getWindow().setWindowIcon(Texture.MISSING);
		Scene s = new Scene();
		Camera c = new Camera();
		ObjectCreator.newObjectStatic().append(ObjectCreator.cube(0.5f));
		c.relativePos = new Vector3f(0, 0, 2);
		s.setActiveCamera(c);
		s.setAmbientLightColor(new Color(0x8EBBDE));
		s.setBackgroundColor(new Color(0x8EBBDE));
		MaterialBasic globe = new MaterialBasic();
		globe.diffuse = TextureCreator.fromFile("net\\chazzvader\\sandbox\\kerbin.png");
		globe.specular = TextureCreator.fromFile("net\\chazzvader\\sandbox\\kerbin_spec.png");
		globe.normal = TextureCreator.fromFile("net\\chazzvader\\sandbox\\kerbin_normal.png");
		globe.specularPower = 32;
		EngineObjectMesh kerbin = ObjectCreator.newObjectStatic().append(ObjectCreator.sphere(0.5f, 32)).getObject(globe, true);
		EngineObjectMesh cube = ObjectCreator.instaCube(0.1f, new MaterialBasic(), true);
		cube.setParent(kerbin);
		kerbin.relativePos = new Vector3f(1, 0, 0);
		cube.relativePos = new Vector3f(1, 0, 0);
		kerbin.addScript(new IScript() {
			
			private float time = 0;
			
			@Override
			public void run(EngineObject object, double delta) {
				object.relativeRot.mul(Quaternion.rotation((float) delta, new Vector3f(0, 1, 0)));
				object.relativeRot.normalize();
				time += delta;
				float val = 0.95f + (float) (Math.sin(time) * 0.1);
				object.relativeScale = new Vector3f(val, val, val);
			}
		});
		ObjectCreator.newObjectStatic().append(ObjectCreator.quadTextured(new Vector3f(-5, 0, -5), new Vector3f(-5, 0, 5), new Vector3f(5, 0, -5), new Vector3f(5, 0, 5)));
		MaterialBasic groundMaterial = new MaterialBasic();
		groundMaterial.diffuse = TextureCreator.fromFile("net\\chazzvader\\sandbox\\brick.jpg");
		groundMaterial.normal = TextureCreator.fromFile("net\\chazzvader\\sandbox\\brick_normal.png");
		EngineObjectMesh ground = ObjectCreator.getObjectStatic(groundMaterial, true);
		ground.relativePos = new Vector3f(0, -1.5f, 0);
		s.add(ground);
		s.add(kerbin);
		s.add(cube);
		Light l = new Light(new Color(0xFFFFFA), 5000, new Vector3f(0, 7, 0));
		s.add(l);
		l.createPhysicalRepresentation();
		l.addScript(new IScript() {
			
			private double time = 0;
			
			@Override
			public void run(EngineObject object, double delta) {
				time += delta;
				object.relativePos = new Vector3f(5*(float) Math.sin(time), 2, 5*(float) Math.cos(time));
			}
		});
		Light l1 = new Light(new Color(0xFFFFFA), 100, new Vector3f(0, 5, 0));
		s.add(l1);
		EngineObjectTextNonUI text = new EngineObjectTextNonUI(new Font("Arial", 0, 200), "Not UI System "+Utils.loadAsString("net\\chazzvader\\sandbox\\hello.txt",  "UTF-8"));
		text.relativeScale = new Vector3f(0.1f, 0.1f, 0.1f);
		text.relativePos = new Vector3f(-1, 1, 0);
		s.add(text);
		
		EngineObjectMesh spinny = ObjectCreator.instaCube(0.5f, groundMaterial, true);
		spinny.relativePos = new Vector3f(0, 2, 3);
		spinny.relativeRot = Quaternion.rotation(2f, new Vector3f(3, 1, 2).normalize());
		s.add(spinny);
		spinny.addScript(new IScript() {
			
			@Override
			public void run(EngineObject object, double delta) {
				object.relativeRot.mul(Quaternion.rotationGlobal((float) delta, new Vector3f(0, 1, 0), object.relativeRot));
			}
			
		});
		
		Mesh teapotMesh = OBJLoader.loadFromFilePath("net\\chazzvader\\sandbox\\teapot.obj");
		teapotMesh = teapotMesh.calculateNormals();
		EngineObject teapot = new EngineObjectMesh(teapotMesh, groundMaterial);
		teapot.relativePos = new Vector3f(0, 0, 3);
		s.add(teapot);
		
		//Finalize
		l1.createPhysicalRepresentation();
		setActiveScene(s);
		getWindow().debugDrawMode();
		getWindow().lockMouse();
	}

	@Override
	protected void update(double delta) {
		if (w) {
			Vector3f dir = Application.getInstance().getActiveScene().getActiveCamera().rot().toEuler().toDegrees().toDirection();
			Application.getInstance().getActiveScene().getActiveCamera().relativePos.add(dir.mul((float) (delta * -FWD_SPEED)));
		}
		if (s) {
			Vector3f dir = Application.getInstance().getActiveScene().getActiveCamera().rot().toEuler().toDegrees().toDirection();
			Application.getInstance().getActiveScene().getActiveCamera().relativePos.add(dir.mul((float) (delta * BWD_SPEED)));
		}
		if (d) {
			Vector3f dir = Application.getInstance().getActiveScene().getActiveCamera().rot().toEuler().toDegrees().toDirection();
			Vector3f up = Vector3f.up();
			Vector3f right = dir.crossProduct(up).normalize();
			Application.getInstance().getActiveScene().getActiveCamera().relativePos
					.add(right.mul((float) (delta * -SIDEWAYS_SPEED)));
		}
		if (a) {
			Vector3f dir = Application.getInstance().getActiveScene().getActiveCamera().rot().toEuler().toDegrees().toDirection();
			Vector3f up = Vector3f.up();
			Vector3f right = dir.crossProduct(up).normalize();
			Application.getInstance().getActiveScene().getActiveCamera().relativePos
					.add(right.mul((float) (delta * SIDEWAYS_SPEED)));
		}
	}

	private boolean w, a, s, d;

	private class CustomEventHandler implements IEventHandlerMouseMoved, IEventHandlerKey, IEventHandlerMouseButton {

		@Override
		public boolean keyPressed(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			if(!getWindow().isMouseLocked()) return false;
			switch (key) {
			case GLFW.GLFW_KEY_W:
				w = true;
				break;
			case GLFW.GLFW_KEY_A:
				a = true;
				break;
			case GLFW.GLFW_KEY_S:
				s = true;
				break;
			case GLFW.GLFW_KEY_D:
				d = true;
				break;
			case GLFW.GLFW_KEY_ESCAPE:
				w = false;
				a = false;
				s = false;
				d = false;
				getWindow().unlockMouse();
				break;
			}
			return false;
		}

		@Override
		public boolean keyReleased(int key, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			switch (key) {
			case GLFW.GLFW_KEY_W:
				w = false;
				break;
			case GLFW.GLFW_KEY_A:
				a = false;
				break;
			case GLFW.GLFW_KEY_S:
				s = false;
				break;
			case GLFW.GLFW_KEY_D:
				d = false;
				break;
			case GLFW.GLFW_KEY_ESCAPE:
				break;
			}
			return false;
		}

		@Override
		public boolean updateMouseAbsolutePosition(int x, int y) {
			return false;
		}

		@Override
		public boolean updateMouseOffset(int x, int y) {
			if(!getWindow().isMouseLocked()) return false;
			
			Vector3f cameraEuler = getActiveScene().getActiveCamera().relativeRot.toEuler();

			cameraEuler.toDegrees();
						
			cameraEuler.y += x * -MOUSE_SENSITIVITY;
			cameraEuler.z += y * MOUSE_SENSITIVITY;
			
			cameraEuler.toRadians();
			
			getActiveScene().getActiveCamera().relativeRot = Quaternion.fromEuler(cameraEuler);
			
			return false;
		}

		@Override
		public boolean mouseButtonPressed(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock, int clickCount) {
			if (button == GLFW.GLFW_MOUSE_BUTTON_1) {
				getWindow().lockMouse();
			}
			return false;
		}

		@Override
		public boolean mouseButtonReleased(int button, int mods, boolean ctrl, boolean shift, boolean alt, boolean os,
				boolean capLock, boolean numLock) {
			return false;
		}
	}

	@Override
	protected void init() {
		
	}

	UIRenderer ui = null;
	
	TextRendererContext textRendererContextA = new TextRendererContext();

	Texture t = null;
	
	int frame = 0;
	
	@Override
	public void renderUI() {
		if(t == null) {
			t = TextureCreator.fromFile("net\\chazzvader\\sandbox\\pos.png");
		}
		if(ui == null) {
			ui = new UIRenderer();
		}
		
//		ui.textCached().drawText(textRendererContextA, "Hello, World!", 0.4f, 0.85f, 0.1f, ScalingBasis.HEIGHT, Alignment.LEFT);
		
		ui.uiPointer = new Vector2f(0.25f, 1f);
		ui.uiPointerMode = Direction.RIGHT;
		
		for(int i = 0;i < 5;i ++) {
			ui.texture().drawTexture(Texture.MISSING, ui.uiPointer, 0.1f, ScalingBasis.HEIGHT, Alignment.UPPER);
		}
		
		frame++;
//		ui.textQuick().drawText(""+frame, new Vector2f(0.0f, 1.0f), 0.1f, ScalingBasis.HEIGHT, Alignment.UPPER_LEFT);

//		ui.texture().drawTexture(t, new Vector2f(0.5f, 0.5f), 0.3f, ScalingBasis.HEIGHT);

//		ui.button().drawButton("Button Push Here", new Vector2f(0.5f, 0.5f), 0.1f, ScalingBasis.HEIGHT, Alignment.CENTER);
		
//		ui.textQuick().drawText(" !\"+#$%&'()*+,-./", new Vector2f(0.3f, 0.3f), 0.05f);
		
		ui.diagnostic().drawDiagnostic(0, true, true, true, true, true, true, true, new String[] {"Hi"}, new Vector3f(1, 0, 0));
	}
}
