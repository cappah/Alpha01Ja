package textures;

public class ModelTexture 
{
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean hasFakeLighting = false;
	
	private int numberOfRows = 1;
	
	// CONSTRUCTOR
	public ModelTexture(int id)
	{
		textureID = id;
	}
	
	
	public int getNumberOfRows()
	{
		return numberOfRows;
	}
	
	
	public void setNumberOfRows(int num)
	{
		numberOfRows = num;
	}
	
	public int getID() { return textureID;}


	public float getShineDamper() {
		return shineDamper;
	}


	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}


	public float getReflectivity() {
		return reflectivity;
	}


	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}


	public boolean hasTransparency() {
		return hasTransparency;
	}


	public void setTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}


	public boolean hasFakeLighting() {
		return hasFakeLighting;
	}


	public void setFakeLighting(boolean hasFakeLighting) {
		this.hasFakeLighting = hasFakeLighting;
	}
	
	
	
}
