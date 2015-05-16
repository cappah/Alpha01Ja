package com.game.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.OBJLoader;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import camera.Camera;
import effects.WaterTile;
import entities.Entity;
import entities.Light;
import entities.Player;
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
	public static void main(String[] args) 
	{
		
        DisplayManager.createDisplay();
        
        Loader loader = new Loader();
       
        /*******************************TERRAIN**********************************/
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
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
        		float x = rand.nextFloat() * 800;
            	float z = rand.nextFloat() * 600;
            	float y = terrains[0][0].getHeightOfTerrain(x, z);
            	entities.add(new Entity(tree, new Vector3f(x, y, z), 0, rand.nextFloat() * 360, 0, 3));
        	}
        	
        	if (i % 20 == 0)
        	{
        		float x = rand.nextFloat() * 800;
            	float z = rand.nextFloat() * 600;
            	float y = terrains[0][0].getHeightOfTerrain(x, z);
            	entities.add(new Entity(grass, new Vector3f(x, y, z), 0, rand.nextFloat() * 360, 0, 1));
        	}
        	
        	if (i % 10 == 0)
        	{
        		float x = rand.nextFloat() * 800;
            	float z = rand.nextFloat() * 600;
            	float y = terrains[0][0].getHeightOfTerrain(x, z);
            	entities.add(new Entity(fern, rand.nextInt(4), new Vector3f(x, y, z), 0, rand.nextFloat() * 360, 0, 0.6f));
        	}
        	
        }
       
        /******************************LIGHTS******************************************/
        Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
        List<Light> lights = new ArrayList<Light>();
        lights.add(new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f)));
        lights.add(new Light(new Vector3f(95, 5, 91), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(238, 30, 195), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(164, 10, 262), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f)));
        /******************************************************************************/
        
        MasterRenderer renderer = new MasterRenderer(loader);
        
        /******************************PLAYER******************************************/
       RawModel playerModel = OBJLoader.loadObjModel("person", loader);
       TexturedModel personModel = new TexturedModel(playerModel, new ModelTexture(loader.loadTexture("playerTexture"))); 
       Player player = new Player(personModel, new Vector3f(0, 0, -50), 0, 0, 0, 0.4f);
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
       waters.add(new WaterTile(231, 342, -7));
       
       //WaterFrameBuffers fbos = new WaterFrameBuffers();
       
       //GUITexture frameWindow = new GUITexture(fbos.getReflectionTexture(), new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
       //guis.add(frameWindow);
       
       
        while(!Display.isCloseRequested())
        {	
        	camera.move();
        	player.move(currentTerrain(terrains, player));
        	renderer.processEntity(player);
        	
        	//fbos.bindReflectionFrameBuffer();
        	//renderer.renderScene(entities, terrainList, lights, camera);
        	//fbos.unbindCurrentFrameBuffer();
        	
        	renderer.renderScene(entities, terrainList, lights, camera);
        	waterRenderer.render(waters, camera);
        	guiRenderer.render(guis);
        	
        	DisplayManager.updateDisplay();
        }
       //fbos.cleanUp();
        waterShader.cleanup();
        renderer.cleanup();
        loader.cleanup();
        DisplayManager.closeDisplay();

	}
	
	// Test Current Terrain Player is standing on
	public static Terrain currentTerrain(Terrain[][] terrains, Player player)
	{
		Terrain currentTerrain = null;
    	for (int i = 0; i < 2; i++)
    	{
    		for (int j = 0; j < 2; j++)
    		{
    			int gridX =  (int)(player.getPosition().x / Terrain.SIZE);
            	int gridZ =  (int)(player.getPosition().z / Terrain.SIZE);
            	currentTerrain = terrains[gridX][gridZ];
    		}
    	}
		
		return currentTerrain;
	}
}
