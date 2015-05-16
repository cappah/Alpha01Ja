package com.game.core;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;

public class DisplayManager 
{
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS = 60;
	
	public static final int GRID_DIMENSION = 2;
	
	private static long lastFrameTime;
	private static float delta;
	
	// Constructor
	public DisplayManager()
	{
		
	}
	
	
	public static void createDisplay()
	{
		ContextAttribs attribs = new ContextAttribs(3, 2)
		.withForwardCompatible(true)
		.withProfileCore(true);
		
		try 
		{
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Alpha Game");
        } 
		catch (LWJGLException e) 
		{
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
		
		glViewport(0, 0, WIDTH, HEIGHT);
		lastFrameTime = getCurrentTime();
	}
	
	
	public static void updateDisplay()
	{
            Display.update();         
            Display.sync(FPS);
            
            long currentFrameTime = getCurrentTime();
            delta = (currentFrameTime - lastFrameTime) / 1000f;
            lastFrameTime = currentFrameTime;
	}
	
	
	public static void closeDisplay()
	{
        Display.destroy();
        System.exit(0);
	}
	
	
	private static long getCurrentTime()
	{
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	
	public static float getFrameTimeSeconds()
	{
		return delta;
	}
}
