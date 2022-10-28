package net.chazzvader.core.generic.engine.object;

import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.event.EventManager;
import net.chazzvader.core.generic.engine.event.type.IEventHandlerWindowResized;
import net.chazzvader.core.generic.math.Matrix4f;
import net.chazzvader.core.generic.math.Quaternion;
import net.chazzvader.core.generic.math.Vector3f;

/**
 * Camera class, a game object that can be set as a scenes camera, making all
 * rendering happen from that position and rotation with a certain
 * configuration. Deals with creating the relevant matrices
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class Camera extends EngineObject {

	/**
	 * Represents a camera configuration, a configuration contains enough details
	 * about the camera to construct the projection matrix
	 * 
	 * @author csbru
	 * @since 1.0
	 * @version 1
	 */
	public static abstract class CameraConfiguration {
		protected Camera parent;

		/**
		 * Gets the camera type of this configuration
		 * 
		 * @return The camera type
		 */
		public abstract CameraType cameraType();

		/**
		 * Creates a projection matrix for the configuration (this will be a new matrix)
		 * 
		 * @return The projection matrix
		 */
		public abstract Matrix4f createProjectionMatrix();
	}

	/**
	 * Represents an orthographic camera (no perspective)
	 * 
	 * @author csbru
	 * @since 1.0
	 * @version 1
	 */
	@SuppressWarnings("javadoc")
	public static class OrthographicCamera extends CameraConfiguration {

		private class OrthographicCameraEventHandler implements IEventHandlerWindowResized {

			@Override
			public boolean windowResized(int width, int height) {
				float nWidth = 9 * width / height;
				left = -nWidth / 10;
				right = nWidth / 10;
				parent.projectionMatrix = createProjectionMatrix();
				return false;
			}

		}

		/**
		 * Bounds for the camera
		 */
		public float right, left, top, bottom, near, far;
		private IEventHandlerWindowResized eventHandler;

		/**
		 * Creates a orthographic camera configuration with the following bounds
		 * 
		 * @param right  The right bound
		 * @param left   The left bound
		 * @param top    The top bound
		 * @param bottom The bottom bound
		 * @param near   The near bound
		 * @param far    The far bound
		 */
		public OrthographicCamera(float right, float left, float top, float bottom, float near, float far) {
			this.right = right;
			this.left = left;
			this.top = top;
			this.bottom = bottom;
			this.near = near;
			this.far = far;
			eventHandler = new OrthographicCameraEventHandler();
			EventManager.registerEventListener(eventHandler, 0);
		}

		/**
		 * Creates an orthographic camera with default bounds
		 */
		public OrthographicCamera() {
			this(16 / 10f, -16 / 10f, 9 / 10f, -9 / 10f, -5, 5);
		}

		@Override
		public CameraType cameraType() {
			return CameraType.ORTHOGRAPHIC;
		}

		@Override
		public Matrix4f createProjectionMatrix() {
			return Matrix4f.orthographic(left, right, bottom, top, near, far);
		}

	}

	/**
	 * Represents a perspective camera (has perspective, duh)
	 * 
	 * @author csbru
	 * @since 1.0
	 * @version 1
	 */
	public static class PerspectiveCamera extends CameraConfiguration {

		private class PerspectiveCameraEventHandler implements IEventHandlerWindowResized {

			@Override
			public boolean windowResized(int width, int height) {
				aspectRatio = (float) width / (float) height;
				parent.projectionMatrix = createProjectionMatrix();
				return false;
			}

		}

		/**
		 * The field of view of the camera in degrees
		 */
		public float fov;
		/**
		 * The Aspect ratio of the screen, width/height
		 */
		public float aspectRatio;
		/**
		 * The near clipping plane
		 */
		public float near;
		/**
		 * The far clipping plane
		 */
		public float far;

		private IEventHandlerWindowResized eventHandler;

		/**
		 * 
		 * @param fov         The field of view of the camera in degrees
		 * @param aspectRatio The Aspect ratio of the screen, width/height
		 * @param near        The near clipping plane
		 * @param far         The far clipping plane
		 */
		public PerspectiveCamera(float fov, float aspectRatio, float near, float far) {
			this.fov = fov;
			this.aspectRatio = aspectRatio;
			this.near = near;
			this.far = far;
			eventHandler = new PerspectiveCameraEventHandler();
			EventManager.registerEventListener(eventHandler, 0);
		}

		/**
		 * Creates a new perspective camera configuration
		 * 
		 * @param fov    The field of view of the camera in degrees
		 * @param width  The width of the screen, used for aspect ratio calculations
		 * @param height The height of the screen, used for aspect ratio calculations
		 * @param near   The near clipping plane
		 * @param far    The far clipping plane
		 */
		public PerspectiveCamera(float fov, float width, float height, float near, float far) {
			this(fov, width / height, near, far);
		}

		/**
		 * Creates a new perspective camera configuration with default settings
		 */
		public PerspectiveCamera() {
			this(45, (float) Application.getInstance().getWindow().getWidth()
					/ (float) Application.getInstance().getWindow().getHeight(), 0.1f, 100);
		}

		@Override
		public CameraType cameraType() {
			return CameraType.PERSPECTIVE;
		}

		@Override
		public Matrix4f createProjectionMatrix() {
			return Matrix4f.perspective(fov, aspectRatio, near, far);
		}
	}

	/**
	 * Camera projection type
	 * 
	 * @author csbru
	 * @since 1.0
	 * @version 1
	 */
	public enum CameraType {
		/**
		 * A perspective projection, objects further away look smaller. Good for first
		 * and third person games, and most 3d games.
		 */
		PERSPECTIVE, 
		/**
		 * An orthographic projection, objection always appear the same size, good for 2d games.
		 */
		ORTHOGRAPHIC;
	}

	private Matrix4f projectionMatrix;
	private CameraConfiguration cameraType;

	/**
	 * Creates a camera with the given configuration, position and rotation
	 * 
	 * @param cameraType A camera configuration details specs about the camera
	 * @param pos        The position of the camera in world space
	 * @param rot        The rotaion of the camera in Euler angles
	 */
	public Camera(CameraConfiguration cameraType, Vector3f pos, Quaternion rot) {
		super(pos, rot, new Vector3f(1, 1, 1));
		this.cameraType = cameraType;
		cameraType.parent = this;
		projectionMatrix = cameraType.createProjectionMatrix();
		
		provider = getWindow().getRenderPipeline().get3dProvider();
	}

	private Quaternion _rot = null;
	private Vector3f _scale = null;
	private Vector3f _pos = null;

	private Matrix4f cachedMatrix;

	/**
	 * The view matrix, from world space to clip space
	 * 
	 * @return The view matrix, from world space to clip space
	 */
	public Matrix4f getViewMatrix() {
		if (pos().equals(_pos) && _rot.equals(rot()) && relativeScale.equals(_scale)) {
			return cachedMatrix;
		} else {
			cachedMatrix = _getViewMatrix();
			_rot = Quaternion.copyOf(rot());
			_pos = Vector3f.copyOf(pos());
			_scale = Vector3f.copyOf(relativeScale);
			return cachedMatrix;
		}
	}

	private Matrix4f _getViewMatrix() {
		// TODO: I dont think euler angles are ideal here.
		
		Vector3f euler = rot().toEuler();
		
		euler.toDegrees();
				
		return Matrix4f.rotationZ(-euler.x).applyLookAtQuaternion(pos(), rot());
	}

	/**
	 * Just the projection matrix, from view space to clip space
	 * 
	 * @return The projection matrix, from view space to clip space
	 */
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	/**
	 * The camera configuration for this camera
	 * 
	 * @return The camera configuration
	 */
	public CameraConfiguration getCameraType() {
		return cameraType;
	}

	/**
	 * Creates a new camera with the given camera configuration. Sets the position
	 * to 0, 0, 0 and no rotation.
	 * 
	 * @param cameraType A camera configuration details specs about the camera
	 */
	public Camera(CameraConfiguration cameraType) {
		this(cameraType, new Vector3f(), new Quaternion());
	}

	/**
	 * Creates a new camera with the given the default perspective configuration.
	 * Sets the position to 0, 0, 0 and rotation to 0, 0, 0
	 */
	public Camera() {
		this(new PerspectiveCamera());
	}

//	@Override
//	protected void fixRot() {
//	}

}
