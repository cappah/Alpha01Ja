package camera;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import entities.Player;



public class Camera 
{
	private Vector3f position = new Vector3f(0, 5, 20);
	private static float pitch = 20f;
	private float yaw;
	private float roll;
	
	private final float MIN_PITCH = 1.0f;
	private final float MAX_PITCH = 60.0f;
	
	private float distanceFromPlayer = 50f;
	private float angleAroundPlayer = 0;
	
	private Player player;
	
	public Camera(Player player)
	{
		this.player = player;
	}
	
	
	public void move()
	{
		calculateZoom();
		calculatePitch();
		limitPitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		
		yaw = 180.0f - (player.getRotY() + angleAroundPlayer);
	}

	
	private void calculateZoom()
	{
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
	}
	
	
	private void calculatePitch()
	{
		if (Mouse.isButtonDown(1))
		{
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
	}
	
	
	private void calculateAngleAroundPlayer()
	{
		if (Mouse.isButtonDown(0))
		{
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}
	
	
	private void calculateCameraPosition(float horizontalDistance, float verticalDistance)
	{
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticalDistance;
	}
	
	
	private float calculateHorizontalDistance()
	{
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	
	private float calculateVerticalDistance()
	{
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	
	private void limitPitch()
	{
		if (pitch < MIN_PITCH)
			pitch = MIN_PITCH;
		else if (pitch > MAX_PITCH)
			pitch = MAX_PITCH;
	}

	public void invertPitch()
	{
		pitch = -pitch;
	}
	public Vector3f getPosition() 
	{
		return position;
	}

	
	public float getPitch() 
	{
		return pitch;
	}

	
	public float getYaw() 
	{
		return yaw;
	}

	
	public float getRoll() 
	{
		return roll;
	}
	
	
	public static String pitchValue()
	{
		return "Pitch: " + pitch;
	}
}
