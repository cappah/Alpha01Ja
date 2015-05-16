package shaderPrograms;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;



/**
 * This class represents a shader program.
 *
 * @author Paul Lewis
 */
public abstract class ShaderProgram
{
	// #include "file.glh" or any other extension.
	// I prefer "glvh" for vertex shader includes and "glfh"
	// for fragment shader includes
	private static final String INCLUDE_DIRECTIVE = "#include";

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private static List<String> headerFiles = new ArrayList<String>();
	private static List<String> sourceBuilders;
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	public ShaderProgram()
	{

	}
	public ShaderProgram(String vertFile, String fragFile)
	{
		vertexShaderID = loadShader(vertFile, GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragFile, GL_FRAGMENT_SHADER);
		loadProgram();
	}


	protected abstract void bindAttributes();

	protected abstract void getAllUniformLocations();

	protected int getUniformLocation(String uniformName)
	{
		return glGetUniformLocation(programID, uniformName);
	}


	public void start()
	{
		glUseProgram(programID);
	}


	public void stop()
	{
		glUseProgram(0);
	}


	public void cleanup()
	{
		stop();
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		glDeleteProgram(programID);
	}


	protected void bindAttribute(int attribute, String variableName)
	{
		glBindAttribLocation(programID, attribute, variableName);
	}


	protected void loadFloat(int location, float value)
	{
		glUniform1f(location, value);
	}


	protected void loadInt(int location, int value)
	{
		glUniform1i(location, value);
	}


	protected void loadVector(int location, Vector3f vec)
	{
		glUniform3f(location, vec.x, vec.y, vec.z);
	}


	protected void load2DVector(int location, Vector2f vec)
	{
		glUniform2f(location, vec.x, vec.y);
	}


	protected void loadBoolean(int location, boolean value)
	{
		float toLoad = 0f;
		if (value)
			toLoad = 1f;
		glUniform1f(location, toLoad);
	}


	protected void loadMatrix(int location, Matrix4f matrix)
	{
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4(location, false, matrixBuffer);

	}


	private static int loadShader(String file, int shaderType)
	{
		StringBuilder shaderSource = new StringBuilder();

		extractGLHeaderFilesPaths(file);
		// Read include headers
		readHeaders(shaderSource);

		// Re-read shader and check for includes again. But this time add extracted shader header code
		shaderSource = loadShaderWithIncludes(file);

		int shaderID = glCreateShader(shaderType);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);

		int status = glGetShaderi(shaderID, GL_COMPILE_STATUS);
		if (status == GL_FALSE)
		{
			System.out.println(glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile " + file);
			System.exit(-1);
		}

		headerFiles.clear();
		sourceBuilders.clear();

		return shaderID;
	}

	private static void extractGLHeaderFilesPaths(String file)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null)
			{
				// save file path add include header file name or just read in
				if (line.startsWith(INCLUDE_DIRECTIVE))
					headerFiles.add("src/shaders/" + line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1));
			}
			reader.close();
		}
		catch (IOException e)
		{
			System.err.println("Could not read " + file);
			e.printStackTrace();
			System.exit(-1);
		}
		sourceBuilders = new ArrayList<String>(headerFiles.size());
	}

	// Read GL header files and extract their data into a list
	private static void readHeaders(StringBuilder shaderSource)
	{
		// For each header file stored, read file and extract data into list
		for (int i = 0; i < headerFiles.size(); i++)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(headerFiles.get(i)));

				String line;
				while ((line = reader.readLine()) != null)
				{
					shaderSource.append(line).append("\n");
				}
				// Store extracted data into list
				sourceBuilders.add(shaderSource.toString());

				// Clear string buffer for next iteration
				shaderSource.setLength(0);
				// Close current file
				reader.close();
			}
			catch (IOException e)
			{
				System.err.println("Could not read header " + headerFiles.get(i));
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}


	private static StringBuilder loadShaderWithIncludes(String file)
	{
		// Clear stringbuilder for reuse
		StringBuilder shaderSource = new StringBuilder();
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null)
			{

				if (line.startsWith(INCLUDE_DIRECTIVE))
				{
					// get index of header path that matches directive
					// if a match, insert header's source in it's place
					// then break out of inner loop
					for (int i = 0; i < headerFiles.size(); i++)
					{
						String headerFromLine = line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1);
						String headerFromList = headerFiles.get(i).substring(headerFiles.get(i).lastIndexOf('/')
								+ 1, headerFiles.get(i).length()).toString();

						if (headerFromLine.equals(headerFromList))
						{
							String[] lines = sourceBuilders.get(i).split("\n");
							for (String sbLine : lines)
							{
								shaderSource.append(sbLine).append("\n");
							}
						}
					}
				}
				else
					shaderSource.append(line).append("\n");
			}
			reader.close();
		}
		catch (IOException e)
		{
			System.err.println("Could not read " + file);
			e.printStackTrace();
			System.exit(-1);
		}
		return shaderSource;
	}




	public void loadProgram()
	{
		programID = glCreateProgram();
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		glLinkProgram(programID);
		glValidateProgram(programID);
		getAllUniformLocations();

		int status = glGetProgrami(programID, GL_LINK_STATUS);
		if (status == GL_FALSE)
		{
			System.out.println(glGetProgramInfoLog(programID, 500));
			System.err.println("Could not link program");
			System.exit(-1);
		}
	}

}