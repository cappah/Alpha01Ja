package lights;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Paul on 5/16/2015.
 */
public class BaseLight
{
    private Vector3f mColor;
    private float mAmbientIntensity;
    private float mDiffuseIntensity;
    private float mSpecularIntensity;
    private float mSpecularPower;
    public BaseLight()
    {

    }


    public BaseLight(Vector3f color, float ambientIntensity, float diffuseIntensity, float specularIntensity, float specularPower)
    {
        mColor = color;
        mAmbientIntensity = ambientIntensity;
        mDiffuseIntensity = diffuseIntensity;
        mSpecularIntensity = specularIntensity;
        mSpecularPower = specularPower;
    }


    public Vector3f getColor()
    {
        return mColor;
    }


    public void setColor(Vector3f color)
    {
        mColor = color;
    }


    public float getAmbientIntensity()
    {
        return mAmbientIntensity;
    }


    public void setAmbientIntensity(float intensity)
    {
        mAmbientIntensity = intensity;
    }

    public float getDiffuseIntensity()
    {
        return mDiffuseIntensity;
    }


    public void setDiffuseIntensity(float intensity)
    {
        mDiffuseIntensity = intensity;
    }


    public float getSpecularIntensity()
    {
        return mSpecularIntensity;
    }


    public void setSpecularIntensity(float intensity)
    {
        mSpecularIntensity = intensity;
    }


    public float getSpecularPower()
    {
        return mSpecularPower;
    }


    public void setSpecularPower(float power)
    {
        mSpecularPower = power;
    }
}
