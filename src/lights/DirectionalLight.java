package lights;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by Paul on 5/16/2015.
 */
public class DirectionalLight extends BaseLight
{

    private Vector3f mDirection;


    public DirectionalLight()
    {

    }


    public DirectionalLight(Vector3f direction, Vector3f color, float intensity)
    {
        super(color, intensity);

        mDirection = direction;
    }





    public Vector3f getDirection()
    {
        return mDirection;
    }


    public void setDirection(Vector3f direction)
    {
        mDirection = direction;
    }

}
