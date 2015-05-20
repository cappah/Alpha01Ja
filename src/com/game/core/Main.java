package com.game.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lights.DirectionalLight;
import lights.PointLight;
import lights.SpotLight;
import models.OBJLoader;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import water.WaterTile;
import entities.Entity;
import lights.Light;
import entities.Player;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.GUIRenderer;
import renderEngine.MasterRenderer;
import renderEngine.WaterRenderer;
import shaderPrograms.WaterShader;
import terrain.Terrain;
import textures.GUITexture;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;


public class Main 
{
    // How many tiles across x and z axis
    public static final int TERRAIN_ROOT_DIMENSION = 2;
    public static final int TERRAIN_SIZE_SQUARED = TERRAIN_ROOT_DIMENSION * TERRAIN_ROOT_DIMENSION;

	public static void main(String[] args) 
	{
		
        DisplayManager.createDisplay();
        
        Loader loader = new Loader();
       
        /*******************************TERRAIN**********************************/
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("beach_sand001s"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
        
        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
        
        Terrain[][] terrains = new Terrain[2][2];
        terrains[0][0] = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap");
        terrains[0][1] = new Terrain(0, 1, loader, texturePack, blendMap, "heightmap");
        terrains[1][0] = new Terrain(1, 0, loader, texturePack, blendMap, "heightmap");
        terrains[1][1] = new Terrain(1, 1, loader, texturePack, blendMap, "heightmap");
       
        List<Terrain> terrainList = new ArrayList<Terrain>();
        terrainList.add(terrains[0][0]);
        terrainList.add(terrains[0][1]);
        terrainList.add(terrains[1][0]);
        terrainList.add(terrains[1][1]);
        //------------------------------------------------------------------------------------------
        
        
        /**************************ENTITIES**********************************************/
        RawModel model = OBJLoader.loadObjModel("tree", loader);
        
        
        TexturedModel tree = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree")));
        //ModelTexture texture = staticModel.getTexture();
       /// texture.setShineDamper(10);
        //texture.setReflectivity(1);
        
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
        		new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setTransparency(true);
        grass.getTexture().setFakeLighting(true);
        
        
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
        		new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setTransparency(true);
        fern.getTexture().setFakeLighting(true);
        fern.getTexture().setNumberOfRows(2);
        
        List<Entity> entities = new ArrayList<Entity>();
        Random rand = new Random(676452);
        
        for (int i = 0; i < 400; i++)
        {
        	if (i % 5 == 0)
        	{
        		float x = rand.nextFloat() * 800.0f;
            	float z = rand.nextFloat() * 600.0f;
            	float y = terrains[0][0].getHeightOfTerrain(x, z);
            	entities.add(new Entity(tree, new Vector3f(x, y, z), 0, rand.nextFloat() * 360.0f, 0.0f, 3.0f));
        	}
        	
        	if (i % 20 == 0)
        	{
        		float x = rand.nextFloat() * 800.0f;
            	float z = rand.nextFloat() * 600.0f;
            	float y = terrains[0][0].getHeightOfTerrain(x, z);
            	entities.add(new Entity(grass, new Vector3f(x, y, z), 0, rand.nextFloat() * 360.0f, 0.0f, 1.0f));
        	}
        	
        	if (i % 10 == 0)
        	{
        		float x = rand.nextFloat() * 800.0f;
            	float z = rand.nextFloat() * 600.0f;
            	float y = terrains[0][0].getHeightOfTerrain(x, z);
            	entities.add(new Entity(fern, rand.nextInt(4), new Vector3f(x, y, z), 0, rand.nextFloat() * 360.0f, 0.0f, 0.6f));
        	}
        	
        }
       
        /******************************LIGHTS******************************************/
        Light light = new Light(new Vector3f(0.0f, 0.0f, -20.0f), new Vector3f(1.0f, 1.0f, 1.0f));
        List<Light> lights = new ArrayList<Light>();
        //lights.add(new Light(new Vector3f(0.0f, 1000.0f, -7000.0f), new Vector3f(0.4f, 0.4f, 0.4f)));
        //lights.add(new Light(new Vector3f(95.0f, 5.0f, 91.0f), new Vector3f(2.0f, 0.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
       // lights.add(new Light(new Vector3f(238.0f, 30.0f, 195.0f), new Vector3f(0.0f, 2.0f, 2.0f), new Vector3f(1.0f, 0.01f, 0.002f)));
       // lights.add(new Light(new Vector3f(164.0f, 10.0f, 262.0f), new Vector3f(2.0f, 2.0f, 0.0f), new Vector3f(1.0f, 0.01f, 0.002f)));

        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(0.0f, 1000.0f, -7000.0f), new Vector3f(0.4f, 0.5f, 0.5f),
                0.5f, 0.1f, 10.f, 2.f);
        PointLight pointLight = new PointLight(new Vector3f(316.8f, 4.f, 63.0f), new Vector3f(1.0f, 1.0f, 0.0f),
                50.f, 30.f, 25.f, 15.f, 10.f, 5.f, 41.f, 14.f);

        /******************************************************************************/
        
        MasterRenderer renderer = new MasterRenderer(loader);
        
        /******************************PLAYER******************************************/
       RawModel playerModel = OBJLoader.loadObjModel("person", loader);
       TexturedModel personModel = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture"))); 
       Player player = new Player(personModel, new Vector3f(125.0f, -9.5f, 135.5f), 0.0f, 0.0f, 0.0f, 0.2f);
       /******************************************************************************/
       
       Camera camera = new Camera(player);
       
       List<GUITexture> guis = new ArrayList<GUITexture>();
       GUITexture gui = new GUITexture(loader.loadTexture("socuwan"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));

       guis.add(gui);
       GUIRenderer guiRenderer = new GUIRenderer(loader);
       /**********************WATER***********************/
       
       WaterShader waterShader = new WaterShader();
       WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix());
       List<WaterTile> waters = new ArrayList<WaterTile>();
       waters.add(new WaterTile(231.0f, 340.0f, -16.5f));

      // WaterFrameBuffers fbos = new WaterFrameBuffers();
      //  GUITexture refraction = new GUITexture(fbos.getRefractionTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
     //  GUITexture reflection = new GUITexture(fbos.getReflectionTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
      //  guis.add(refraction);
      //  guis.add(reflection);
       //GUITexture frameWindow = new GUITexture(fbos.getReflectionTexture(), new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
       //guis.add(frameWindow);

        // Clipping plane
        Vector4f reflectionClipping = new Vector4f(0.0f, 1.0f, 0.0f, -waters.get(0).getHeight());
        Vector4f refractionClipping = new Vector4f(0.0f, -1.0f, 0.0f, waters.get(0).getHeight());
       
        while(!Display.isCloseRequested())
        {	
        	camera.move();
        	player.move(currentTerrain(terrains, player));
            renderer.processEntity(player);

            /* Water reflection and refraction. Check MasterRenderer for clipping plane
            glEnable(GL_CLIP_DISTANCE0);

            // render reflection (render everything above water
            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - waters.get(0).getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            renderer.renderScene(entities, terrainList, lights, camera, reflectionClipping);
            camera.getPosition().y += distance;
            camera.invertPitch();

            // render refraction (render everything under water
            fbos.bindRefractionFrameBuffer();
            renderer.renderScene(entities, terrainList, lights, camera, refractionClipping);

            // render window frame
        	//fbos.bindReflectionFrameBuffer();
        	//renderer.renderScene(entities, terrainList, lights, camera);
        	//fbos.unbindCurrentFrameBuffer();

            // render scene
            glDisable(GL_CLIP_DISTANCE0);
            fbos.unbindCurrentFrameBuffer();*/
        	renderer.renderScene(entities, terrainList, lights, directionalLight, pointLight, camera, new Vector4f(0, -1, 0, 100000));
        	waterRenderer.render(waters, camera);
        	guiRenderer.render(guis);
        	
        	DisplayManager.updateDisplay();
        }
       // fbos.cleanUp();
        waterShader.cleanup();
        renderer.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();

	}
	
	// Test Current Terrain Player is standing on
	public static Terrain currentTerrain(Terrain[][] terrains, Player player)
	{
		Terrain currentTerrain = null;
    	for (int i = 0; i < TERRAIN_ROOT_DIMENSION; i++)
    	{
    		for (int j = 0; j < TERRAIN_ROOT_DIMENSION; j++)
    		{
    			int gridX =  (int)(player.getPosition().x / Terrain.SIZE);
            	int gridZ =  (int)(player.getPosition().z / Terrain.SIZE);
            	currentTerrain = terrains[gridX][gridZ];
    		}
    	}
		
		return currentTerrain;
	}
}
