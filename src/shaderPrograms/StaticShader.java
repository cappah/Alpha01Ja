package shaderPrograms;

import java.util.List;

import lights.*;
import math.Maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import org.lwjgl.util.vector.Vector4f;


public class StaticShader extends ShaderProgram
{
	private static final int MAX_LIGHTS = 4;
	private static final int MAX_POINT_LIGHTS = 16;
	private static final int MAX_SPOT_LIGHTS = 16;
	private static final String VERTEX_FILE = "src/shaders/basicV.glsl";
	private static final String FRAG_FILE = "src/shaders/basicF.glsl";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_transformationMatrix;
	
	private int[] location_lightPosition;
	private int[] location_lightColor;
	private int[] location_atten;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	private int location_skyColor;
	private int location_numberOfRows;
	private int location_offset;
	private int location_plane;

	// Base Light
	private int location_specularIntensity;
	private int location_specularPower;
	private int location_baseLightColor;
	private int location_baseLightAmbientIntensity;
	private int location_baseLightDiffuseIntensity;
	// Directional Light
	private int location_directionalLightColor;
	private int location_directionalLightAmbientIntensity;
	private int location_directionalLightDiffuseIntensity;
	private int location_directionalLightDirection;
	// Point Light
	private int[] location_pointLightColor;
	private int[] location_pointLightAmbientIntensity;
	private int[] location_pointLightDiffuseIntensity;
	private int[] location_pointLightAttenConst;
	private int[] location_pointLightAttenLinear;
	private int[] location_pointLightAttenExp;
	private int[] location_pointLightPosition;
	private int[] location_pointLightRange;
	private int location_pointLightCount;
	// Point Light
	private int location_spotLightColor;
	private int location_spotLightAmbientIntensity;
	private int location_spotLightDiffuseIntensity;
	private int location_spotLightAttenConst;
	private int location_spotLightAttenLinear;
	private int location_spotLightAttenExp;
	private int location_spotLightPosition;
	private int location_spotLightRange;
	private int location_spotLightDirection;
	private int location_spotLightCutoff;

	public StaticShader()
	{
		super(VERTEX_FILE, FRAG_FILE);
	}
	
	
	public StaticShader(String vertFile, String fragFile) 
	{
		super(vertFile, fragFile);
		
	}

	
	@Override
	protected void bindAttributes() 
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
		super.bindAttribute(2, "normal");
	}


	@Override
	protected void getAllUniformLocations() 
	{
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_atten = new int[MAX_LIGHTS];
		
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_atten[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		
		location_skyColor = super.getUniformLocation("skyColor");
		
		location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");

		location_plane = super.getUniformLocation("plane");

		// Base Light
		location_specularIntensity = super.getUniformLocation("specularIntensity");
		location_specularPower = super.getUniformLocation("specularPower");
		location_baseLightColor = super.getUniformLocation("gBaseLight.color");
		location_baseLightAmbientIntensity = super.getUniformLocation("gBaseLight.ambientIntensity");
		location_baseLightAmbientIntensity = super.getUniformLocation("gBaseLight.diffuseIntensity");

		// Directional Light
		location_directionalLightColor = super.getUniformLocation("gDirectionalLight.base.color");
		location_directionalLightAmbientIntensity = super.getUniformLocation("gDirectionalLight.base.ambientIntensity");
		location_directionalLightDiffuseIntensity = super.getUniformLocation("gDirectionalLight.base.diffuseIntensity");
		location_directionalLightDirection = super.getUniformLocation("gDirectionalLight.direction");

		// Point Light
		location_pointLightColor = new int[MAX_POINT_LIGHTS];
		location_pointLightAmbientIntensity = new int[MAX_POINT_LIGHTS];
		location_pointLightDiffuseIntensity = new int[MAX_POINT_LIGHTS];
		location_pointLightAttenConst = new int[MAX_POINT_LIGHTS];
		location_pointLightAttenLinear = new int[MAX_POINT_LIGHTS];
		location_pointLightAttenExp = new int[MAX_POINT_LIGHTS];
		location_pointLightPosition = new int[MAX_POINT_LIGHTS];
		location_pointLightRange = new int[MAX_POINT_LIGHTS];

		for (int i = 0; i < MAX_POINT_LIGHTS; i++)
		{
			location_pointLightColor[i] = super.getUniformLocation("gPointLight[" + i + "].base.color");
			location_pointLightAmbientIntensity[i] = super.getUniformLocation("gPointLight[" + i + "].base.ambientIntensity");
			location_pointLightDiffuseIntensity[i] = super.getUniformLocation("gPointLight[" + i + "].base.diffuseIntensity");
			location_pointLightAttenConst[i] = super.getUniformLocation("gPointLight[" + i + "].atten.constant");
			location_pointLightAttenLinear[i] = super.getUniformLocation("gPointLight[" + i + "].atten.linear");
			location_pointLightAttenExp[i] = super.getUniformLocation("gPointLight[" + i + "].atten.exponent");
			location_pointLightPosition[i] = super.getUniformLocation("gPointLight[" + i + "].position");
			location_pointLightRange[i] = super.getUniformLocation("gPointLight[" + i + "].range");
		}
		location_pointLightCount = super.getUniformLocation("gNumPointLights");

	}

	
	public void loadProjectionMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	
	public void loadViewMatrix(Camera camera)
	{
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	
	public void loadTransformationMatrix(Matrix4f matrix)
	{
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	
	public void loadLights(List<Light> lights)
	{
		for (int i = 0; i < MAX_LIGHTS; i++)
		{
			if (i < lights.size())
			{
				super.loadVector(location_lightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_lightColor[i], lights.get(i).getColor());
				super.loadVector(location_atten[i], lights.get(i).getAtten());
			}
			else
			{
				super.loadVector(location_lightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(location_atten[i], new Vector3f(1, 0, 0));
			}
		}	
	}
	
	
	public void loadShineVariables(float damper, float reflectivity)
	{
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	
	public void loadFakeLighting(boolean useFake)
	{
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	
	public void loadSkyColor(float r, float g, float b)
	{
		super.loadVector(location_skyColor, new Vector3f(r, g, b));
	}
	
	
	public void loadNumberOfRows(int numberOfRows)
	{
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	
	public void loadOffset(float x, float y)
	{
		super.load2DVector(location_offset, new Vector2f(x, y));
	}


	public void loadClippingPlane(Vector4f plane)
	{
		super.load4DVector(location_plane, plane);
	}


	/**************************************************
	 LIGHTING
	 **************************************************/

	public void loadBaseLight(BaseLight light)
	{
		super.loadVector(location_baseLightColor, light.getColor());
		super.loadFloat(location_baseLightAmbientIntensity, light.getAmbientIntensity());
		super.loadFloat(location_baseLightDiffuseIntensity, light.getDiffuseIntensity());
		super.loadFloat(location_specularIntensity, light.getSpecularIntensity());
		super.loadFloat(location_specularPower, light.getSpecularPower());
	}


	public void loadDirectionalLight(DirectionalLight light)
	{

		super.loadVector(location_directionalLightDirection, light.getDirection());
		super.loadVector(location_directionalLightColor, light.getColor());
		super.loadFloat(location_directionalLightAmbientIntensity, light.getAmbientIntensity());
		super.loadFloat(location_baseLightDiffuseIntensity, light.getDiffuseIntensity());
	}


	public void loadPointLights(List<PointLight> lights)
	{
		super.loadInt(location_pointLightCount, lights.size());
		for (int i = 0; i < MAX_POINT_LIGHTS; i++)
		{
			if (i < lights.size())
			{
				super.loadVector(location_pointLightPosition[i], lights.get(i).getPosition());
				super.loadVector(location_pointLightColor[i], lights.get(i).getColor());
				super.loadFloat(location_pointLightAmbientIntensity[i], lights.get(i).getAmbientIntensity());
				super.loadFloat(location_pointLightDiffuseIntensity[i], lights.get(i).getDiffuseIntensity());
				super.loadFloat(location_pointLightRange[i], lights.get(i).getRange());
				super.loadFloat(location_pointLightAttenConst[i], lights.get(i).getAttenConstant());
				super.loadFloat(location_pointLightAttenLinear[i], lights.get(i).getAttenLinear());
				super.loadFloat(location_pointLightAttenExp[i], lights.get(i).getAttenEXP());
			}
			else
			{
				super.loadVector(location_pointLightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(location_pointLightColor[i], new Vector3f(0, 0, 0));
				super.loadFloat(location_pointLightAmbientIntensity[i], 0);
				super.loadFloat(location_pointLightDiffuseIntensity[i], 0);
				super.loadFloat(location_pointLightRange[i], 0);
				super.loadFloat(location_pointLightAttenConst[i], 0);
				super.loadFloat(location_pointLightAttenLinear[i], 0);
				super.loadFloat(location_pointLightAttenExp[i], 0);
			}
		}
	}


	public void loadSpotLight(SpotLight light)
	{
		super.loadVector(location_spotLightPosition, light.getPosition());
		super.loadVector(location_spotLightDirection, light.getDirection());
		super.loadVector(location_spotLightColor, light.getColor());
		super.loadFloat(location_spotLightAmbientIntensity, light.getAmbientIntensity());
		super.loadFloat(location_spotLightDiffuseIntensity, light.getDiffuseIntensity());
		super.loadFloat(location_spotLightRange, light.getRange());
		super.loadFloat(location_spotLightAttenConst, light.getAttenConstant());
		super.loadFloat(location_spotLightAttenLinear, light.getAttenLinear());
		super.loadFloat(location_spotLightAttenExp, light.getAttenEXP());
		super.loadFloat(location_spotLightCutoff, light.getmCutoff());
	}
}
