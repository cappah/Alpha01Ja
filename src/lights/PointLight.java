package lights;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Paul on 5/16/2015.
 */
public class PointLight extends BaseLight
{
    private Vector3f mPosition;
    private float mRange;
    private float mAttenConstant;
    private float mAttenLinear;
    private float mAttenEXP;

    public PointLight()
    {

    }

    public PointLight(Vector3f position, Vector3f color, float intensity, float range,
                      float constant, float linear, float exp)
    {
        super(color, intensity);

        mPosition = position;
        mRange = range;
        mAttenConstant = constant;
        mAttenLinear = linear;
        mAttenEXP = exp;
    }


    public Vector3f getPosition()
    {
        return mPosition;
    }

    public void setPosition(Vector3f Position)
    {
        this.mPosition = Position;
    }

    public float getRange()
    {
        return mRange;
    }

    public void setRange(float range)
    {
        this.mRange = range;
    }

    public float getAttenConstant()
    {
        return mAttenConstant;
    }

    public void setAttenConstant(float constant)
    {
        this.mAttenConstant = constant;
    }

    public float getAttenLinear()
    {
        return mAttenLinear;
    }

    public void setAttenLinear(float linear)
    {
        this.mAttenLinear = linear;
    }

    public float getAttenEXP()
    {
        return mAttenEXP;
    }

    public void setAttenEXP(float exp)
    {
        this.mAttenEXP = exp;
    }
}
