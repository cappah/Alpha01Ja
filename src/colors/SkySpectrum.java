package colors;

import utils.FPSTimer;

/**
 * Calculates the color of the sky based on a specific period
 * of time that represents a day cycle.
 *
 * Created by Paul on 5/17/2015.
 */
public class SkySpectrum
{
    private FPSTimer mTimer;

    public SkySpectrum()
    {
        mTimer = new FPSTimer();

    }

    public void init()
    {
        mTimer.init();
    }

    public void update()
    {
        mTimer.update();
        System.out.println("FPS:" + mTimer.getFPS());
    }



}
