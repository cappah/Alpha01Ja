package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import com.game.core.DisplayManager;

import terrain.Terrain;



public class Player extends Entity 
{
	private static final float RUN_SPEED = 20f;
	private static final float TURN_SPEED = 160f;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	private float currentSpeed = 0f;
	private float currentTurnSpeed = 0f;
	private float upwardSpeed = 0f;
	private boolean isInAir = false;
	private Vector3f pos;
	
	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) 
	{
		super(model, position, rotX, rotY, rotZ, scale);
		
		pos = position;
	}
	
	
	public void move(Terrain terrain)
	{
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		
		super.increasePosition(dx, 0, dz);
		upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, upwardSpeed * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
		if (super.getPosition().y < terrainHeight)
		{
			upwardSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	
	private void jump()
	{
		if (!isInAir)
		{
			upwardSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	
	private void checkInputs()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			currentSpeed = RUN_SPEED;
			System.out.println("Pos x: " + pos.x + " y: " + pos.y + " z: " + pos.z);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			currentSpeed = -RUN_SPEED;
			System.out.println("Pos x: " + pos.x + " y: " + pos.y + " z: " + pos.z);
		}
		else
			currentSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			currentTurnSpeed = -TURN_SPEED;
			System.out.println("Pos x: " + pos.x + " y: " + pos.y + " z: " + pos.z);
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			currentTurnSpeed = TURN_SPEED;
			System.out.println("Pos x: " + pos.x + " y: " + pos.y + " z: " + pos.z);
		}
		else
			currentTurnSpeed = 0;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			jump();
		}
	}	
}