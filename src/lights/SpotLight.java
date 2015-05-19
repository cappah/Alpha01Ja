package lights;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Paul on 5/16/2015.
 */
public class SpotLight extends PointLight
{
    private Vector3f mDirection;
    private float cutoff;

    public SpotLight()
    {

    }


    public SpotLight(Vector3f position, Vector3f direction, Vector3f color, float intensity,
                     float range, float constant, float linear, float exp, float cutoff)
    {
        super(position, color, intensity, range, constant, linear, exp);
    }
}
