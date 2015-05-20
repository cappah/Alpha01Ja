package renderEngine;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import colors.SkySpectrum;
import lights.DirectionalLight;
import lights.PointLight;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import camera.Camera;

import com.game.core.Loader;

import entities.Entity;
import lights.Light;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shaderPrograms.StaticShader;
import shaderPrograms.TerrainShader;
import terrain.Terrain;


public class MasterRenderer 
{
	private static final float FOV = 70f;
	private static final float NEAR = 0.1f;
	private static final float FAR = 1600f;
	
	//private SkySpectrum mSkySpectrum;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader mShader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	//private SkyboxRenderer skyboxRenderer;
	
	// CONSTRUCTOR
	public MasterRenderer(Loader loader)
	{
		//mSkySpectrum = new SkySpectrum();

		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(mShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		//skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
	}
	
	
	public void prepare()
	{
		//mSkySpectrum.init();
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.4f, 0.5f, 0.5f, 1);
		
	}
	
	public void renderScene(List<Entity> entities, List<Terrain> terrains, List<Light> lights,
							DirectionalLight directionalLight, PointLight pointLight,
							Camera camera, Vector4f clippingPlane)
	{
		for (Terrain terrain : terrains)
			processTerrain(terrain);
		for (Entity entity : entities)
			processEntity(entity);
		render(lights, directionalLight, pointLight, camera, clippingPlane);
	}
	
	
	public void render(List<Light> lights, DirectionalLight directionalLight, PointLight pointLight, Camera camera, Vector4f clippingPlane)
	{
		prepare();
		//mSkySpectrum.update();
		mShader.start();
		//mShader.loadClippingPlane(clippingPlane); // water
		mShader.loadSkyColor(0.4f, 0.5f, 0.5f);
		mShader.loadLights(lights);
		mShader.loadDirectionalLight(directionalLight);
		mShader.loadPointLight(pointLight);
		mShader.loadViewMatrix(camera);
		renderer.render(entities);
		mShader.stop();
		
		terrainShader.start();
		//terrainShader.loadClippingPlane(clippingPlane); // water
		terrainShader.loadSkyColor(0.4f, 0.5f, 0.5f);
		terrainShader.loadLights(lights);
		terrainShader.loadDirectionalLight(directionalLight);
		terrainShader.loadPointLight(pointLight);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		//skyboxRenderer.render(camera, 0.4f, 0.5f, 0.5f);
		
		terrains.clear();
		entities.clear();
	}
	
	
	public void processEntity(Entity entity)
	{
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		
		if (batch != null)
			batch.add(entity);
		else
		{
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	
	public void processTerrain(Terrain terrain)
	{
		terrains.add(terrain);
	}
	
	
	public static void enableCulling()
	{
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	
	public static void disableCulling()
	{
		glDisable(GL_CULL_FACE);
	}
	
	
	private void createProjectionMatrix()
	{
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR - NEAR;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR + NEAR) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR * FAR) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	
	public void cleanup()
	{
		mShader.cleanup();
		terrainShader.cleanup();
	}
	
	
	public Matrix4f getProjectionMatrix()
	{
		return projectionMatrix;
	}
}
