package lights;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Paul on 5/16/2015.
 */
public class BaseLight
{
    private Vector3f mColor;
    private float mIntensity;

    public BaseLight()
    {

    }


    public BaseLight(Vector3f color, float intensity)
    {
        mColor = color;
        mIntensity = intensity;
    }


    public Vector3f getColor()
    {
        return mColor;
    }


    public void setColor(Vector3f color)
    {
        mColor = color;
    }


    public float getIntensity()
    {
        return mIntensity;
    }


    public void setIntensity(float intensity)
    {
        mIntensity = intensity;
    }
}
