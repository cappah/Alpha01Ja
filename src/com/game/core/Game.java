package com.game.core;

import entities.Entity;
import models.OBJLoader;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Paul on 5/16/2015.
 */
public class Game
{
    private Loader loader = new Loader();
    private TerrainTexture backgroundTexture;
    private TerrainTexture rTexture;
    private TerrainTexture gTexture;
    private TerrainTexture bTexture;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private Terrain[][] terrains = new Terrain[2][2];
    private List<Terrain> terrainList = new ArrayList<Terrain>();

    public Game()
    {

    }

    public void start()
    {
        init();
        render();
    }


    private void init()
    {
        initTerrain();
    }

    private void render()
    {

    }


    private void initTerrain()
    {
        backgroundTexture = new TerrainTexture(loader.loadTexture("beach_sand001s"));
        rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        bTexture = new TerrainTexture(loader.loadTexture("path"));

        texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        blendMap = new TerrainTexture(loader.loadTexture("blendMap"));

        terrains[0][0] = new Terrain(0, 0, loader, texturePack, blendMap, "heightmap");
        terrains[0][1] = new Terrain(0, 1, loader, texturePack, blendMap, "heightmap");
        terrains[1][0] = new Terrain(1, 0, loader, texturePack, blendMap, "heightmap");
        terrains[1][1] = new Terrain(1, 1, loader, texturePack, blendMap, "heightmap");

        terrainList.add(terrains[0][0]);
        terrainList.add(terrains[0][1]);
        terrainList.add(terrains[1][0]);
        terrainList.add(terrains[1][1]);
    }


    private void initEntities()
    {
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
    }
}
