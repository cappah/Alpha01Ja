package utils;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import org.lwjgl.input.Keyboard;

public class ShortKeyUtil 
{
	public static void polygonModeSwitch()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_P))
		{
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		}			
		else if (Keyboard.isKeyDown(Keyboard.KEY_O))
		{
			glPolygonMode(GL_FRONT, GL_FILL);
		}
		
	}
}
