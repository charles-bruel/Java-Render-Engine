package net.chazzvader.core.generic.engine.render.material;

import net.chazzvader.core.generic.Configuration;
import net.chazzvader.core.generic.Configuration.Renderer;
import net.chazzvader.core.generic.engine.Application;
import net.chazzvader.core.generic.engine.creator.ShaderCreator;
import net.chazzvader.core.generic.engine.object.EngineObject;
import net.chazzvader.core.generic.math.Vector3f;
import net.chazzvader.core.opengl.engine.render.OpenGLTexture;
import net.chazzvader.core.opengl.engine.util.OpenGLStateMachine;
import net.chazzvader.core.opengl.shader.OpenGLShaderLocations;

/**
 * Basic material. Blinn-Phong shading, shadows, normal mapping. Used for pretty
 * much everything.
 * 
 * @author csbru
 * @since 1.0
 * @version 1
 */
public class MaterialBasic extends Material {

	private Shader shader;

	/**
	 * A color multiplier, applies as a tint. White (default) means no tint and
	 * black just causes the entire thing to go black.
	 */
	public Vector3f colorMultiplier = new Vector3f(1, 1, 1);

	/**
	 * A flat specular strength multiplier. Specular are those shiny highlights you
	 * get when viewing the object at an angle.
	 */
	public float specularMultiplier = 1f;

	/**
	 * A flat normal strength multiplier. Normals create the illusion of a bumpy
	 * surface.
	 */
	public float normalStrength = 0.65f;// TODO: Implement this

	/**
	 * The diffuse texture, essentially just the color.
	 */
	public Texture diffuse = Texture.BLANK;

	/**
	 * A specular multiplier texture, works similarly to
	 * <code>specularMultiplier</code> except applies non-uniformly. White is shiny
	 * and black is not.
	 * 
	 * @see MaterialBasic#specularMultiplier
	 */
	public Texture specular = Texture.BLANK;

	/**
	 * A normal map. Not to be confused with normal strength, a normal map contains
	 * at each point a vector point away from the surface. This creates the illusion
	 * of bumps. Defaults to a blue texture instead of a white one because blue is
	 * (0, 0, 1), pointing straight up, or smooth.
	 */
	public Texture normal = Texture.BLANK_NORMALS;

	/**
	 * An exponent used in the specular calculation. More advanced. Should be a
	 * power of 2.
	 */
	public int specularPower = 64;

	/**
	 * Essentially turns this shader into MaterialTextureCoordinatesTest.
	 * 
	 * @see MaterialTextureCoordinatesTest
	 */
	public boolean textureCoordinatesDebug = false;

	/**
	 * Essentially turns this shader into MaterialNormalsTest but with normal
	 * mapping applied.
	 * 
	 * @see MaterialNormalsTest
	 */
	public boolean normalDebug = false;

	/**
	 * Constructor that uses initializes all the values.
	 * 
	 * @param colorMultiplier    A tint. Defaults to (1, 1, 1).
	 * @param specularMultiplier Roughly speaking, shininess. Defaults to 1.
	 * @param normalStrength     Bumpiness, relies on normal map. Defaults to 0.65.
	 * @param diffuse            The color texture. Defaults to a blank white
	 *                           texture.
	 * @param specular           The shininess texture. Defaults to a blank white
	 *                           texture.
	 * @param normal             The normal map. Defaults to a blank blue texture.
	 * @param specularPower      The specular power, a value used in the
	 *                           calculation. Defaults to 64.
	 * 
	 * @see MaterialBasic#colorMultiplier
	 * @see MaterialBasic#specularMultiplier
	 * @see MaterialBasic#normalStrength
	 * @see MaterialBasic#diffuse
	 * @see MaterialBasic#specular
	 * @see MaterialBasic#normal
	 * @see MaterialBasic#specularPower
	 */
	public MaterialBasic(Vector3f colorMultiplier, float specularMultiplier, float normalStrength, Texture diffuse,
			Texture specular, Texture normal, int specularPower) {
		this();
		this.colorMultiplier = colorMultiplier;
		this.specularMultiplier = specularMultiplier;
		this.normalStrength = normalStrength;
		this.diffuse = diffuse;
		this.specular = specular;
		this.normal = normal;
		this.specularPower = specularPower;
	}

	/**
	 * Stripped down version of the all values constructor
	 * 
	 * @param specularMultiplier Roughly speaking, shininess. Defaults to 1.
	 * @param diffuse            The color texture. Defaults to a blank white
	 *                           texture.
	 * @param normal             The normal map. Defaults to a blank blue texture.
	 * 
	 * @see MaterialBasic#specularMultiplier
	 * @see MaterialBasic#diffuse
	 * @see MaterialBasic#normal
	 */
	public MaterialBasic(float specularMultiplier, Texture diffuse, Texture normal) {
		this();
		this.specularMultiplier = specularMultiplier;
		this.diffuse = diffuse;
		this.normal = normal;
	}

	/**
	 * Most basic constructor, creates a new blank material with default values.
	 * Does the heavy lifting initial creation stuff,
	 */
	public MaterialBasic() {
		super(false);
		Configuration.assertRendererFinalized();
		this.shader = ShaderCreator.basicShader();
		shader.bind();
		shader.setUniform1i("diffuse", OpenGLShaderLocations.TEXTURE_DIFFUSE);
		shader.setUniform1i("specular", OpenGLShaderLocations.TEXTURE_SPECULAR);
		shader.setUniform1i("normals_tex", OpenGLShaderLocations.TEXTURE_NORMAL);
		shader.unbind();
	}

	@Override
	protected void _bind(EngineObject object) {
		shader.bind();
		diffuse.bind();
		shader.setUniform1i("tc_debug_flag", textureCoordinatesDebug ? 1 : 0);
		shader.setUniform1i("normal_debug_flag", normalDebug ? 1 : 0);
		shader.setUniform1f("ambient_strength", Application.getInstance().getActiveScene().getAmbientLightStrength());
		shader.setUniform1f("specular_strength", specularMultiplier);
		shader.setUniform3f("ambient_color", Application.getInstance().getActiveScene().getAmbientLightColor());
		shader.setUniform3f("view_pos", Application.getInstance().getActiveScene().getActiveCamera().relativePos);
		shader.setUniform3f("color_multiplier", colorMultiplier);
		shader.setUniform1i("specular_power", specularPower);
		shader.setUniformMat4f("world_matrix", object.getWorldMatrix());
		Renderer renderer = Configuration.getRendererVerifyFinalized();
		switch (renderer) {
		case GENERIC:
			break;
		case OPEN_GL:
			OpenGLStateMachine.setActiveTextureSlot(OpenGLShaderLocations.TEXTURE_DIFFUSE);
			((OpenGLTexture) diffuse).bind();

			OpenGLStateMachine.setActiveTextureSlot(OpenGLShaderLocations.TEXTURE_SPECULAR);
			((OpenGLTexture) specular).bind();

			OpenGLStateMachine.setActiveTextureSlot(OpenGLShaderLocations.TEXTURE_NORMAL);
			((OpenGLTexture) normal).bind();
			break;
		}
	}

	@Override
	public Shader getShader() {
		return shader;
	}

}
