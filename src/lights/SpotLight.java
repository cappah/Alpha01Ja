package lights;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Paul on 5/16/2015.
 */
public class SpotLight extends PointLight
{
    private Vector3f mDirection;
    private float mCutoff;

    public SpotLight()
    {

    }


    public SpotLight(Vector3f position, Vector3f direction, Vector3f color, float ambientIntensity,
                     float diffuseIntensity, float range, float constant, float linear, float exp,
                     float cutoff, float specIntensity, float specPower)
    {
        super(position, color, ambientIntensity, diffuseIntensity, range, constant, linear, exp, specIntensity, specPower);

        mDirection = direction;
        mCutoff = cutoff;
    }


    public Vector3f getDirection()
    {
        return mDirection;
    }


    public void setmDirection(Vector3f direction)
    {
        mDirection = direction;
    }


    public float getmCutoff()
    {
        return mCutoff;
    }


    public void setCutoff(float cutoff)
    {
        mCutoff = cutoff;
    }
}
