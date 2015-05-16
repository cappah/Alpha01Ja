package colors;

import org.lwjgl.util.vector.Vector3f;

public class GColor 
{
	// LIGHTING COLORS ///////////////////////////////////////////////////////////////////

	/*
	These are materials that produce light when they are heated. The sun is a black body
	illuminant, as is a candle flame. The color of light of these types of sources can be
	characterized by their Kelvin temperature. Note that this temperature has nothing to
	do with how "hot" a light source is - just with the color of its light. A light source
	with a low Kelvin temperature is very red. One with a high Kelvin temperature is very
	blue. More accurately, when we see two light sources side by side in a scene, the
	higher Kelvin light appears more blue, and the lower Kelvin light appears more red.
	Its all relative.
	*/
	private static Vector3f COLOR_ILLUM_CANDLE = new Vector3f(0.831f, 0.921f, 1.0f); // 255, 147, 41
	private static Vector3f COLOR_ILLUM_40WTUNGSTEN = new Vector3f(0.956f, 1.0f, 0.980f); // 255, 197, 143
	private static Vector3f COLOR_ILLUM_100WTUNGSTEN = new Vector3f(1.0f, 0.956f, 0.898f); // 255, 214, 170
	private static Vector3f COLOR_ILLUM_HALOGEN = new Vector3f(0.831f, 0.921f, 1.0f); // 255, 241, 224
	private static Vector3f COLOR_ILLUM_CARBONARC = new Vector3f(0.956f, 1.0f, 0.980f); // 255, 250, 244
	private static Vector3f COLOR_DAY_NOON = new Vector3f(1.0f, 0.956f, 0.898f); // 255, 255, 251
	private static Vector3f COLOR_DAY_SUNLIGHT = new Vector3f(0.831f, 0.921f, 1.0f); // 255, 255, 255
	private static Vector3f COLOR_DAY_OVERCAST = new Vector3f(0.956f, 1.0f, 0.980f); // 201, 226, 255
	private static Vector3f COLOR_DAY_CLEARBLUE = new Vector3f(1.0f, 0.956f, 0.898f); // 64, 156, 255
	private static Vector3f COLOR_SKY = new Vector3f(0.529f, 0.807f, 0.921f);
	
	/*
	light sources produce light by creating a large amount of UV light via high voltage
	electrical discharge through a tube filled with rare gasses. The UV light excites
	materials coating the tube to produce light through fluorescence. These lights have
	broad but sometimes disjointed spectra.
	*/
	private static Vector3f COLOR_FLOURESCENT_BLUE = new Vector3f(0.831f, 0.921f, 1.0f); // 212, 235, 255
	private static Vector3f COLOR_FLOURESCENT = new Vector3f(0.956f, 1.0f, 0.980f); // 244, 255, 250
	private static Vector3f COLOR_FLOURESCENT_WARM = new Vector3f(1.0f, 0.956f, 0.898f); // 255, 244, 229

	/*
	light source usually involves a metallic gas under pressure being excited by a
	high voltage coil. They do not produce a continuous spectrum at all, but instead
	produce a series of monochromatic lines of light energy. This confounds our
	ability to accurately reproduce the full effect of how these lights look and
	interact with colors in a scene.
	*/
	private static Vector3f COLOR_GAS_MERCURY = new Vector3f(1.0f, 0.956f, 0.898f); // 216, 247, 255
	private static Vector3f COLOR_GAS_SODIUM = new Vector3f(1.0f, 0.956f, 0.898f); // 255, 209, 178
	private static Vector3f COLOR_GAS_HALIDE = new Vector3f(1.0f, 0.956f, 0.898f); // 242, 252, 255
	private static Vector3f COLOR_GAS_SODIUM_HP = new Vector3f(1.0f, 0.956f, 0.898f); //255, 183, 76

	
	
	private static Vector3f[] mDay = new Vector3f[]
	{
		COLOR_DAY_NOON,
		COLOR_DAY_SUNLIGHT,
		COLOR_DAY_OVERCAST,
		COLOR_DAY_CLEARBLUE,
		COLOR_SKY
	};
	
	
	private static Vector3f[] mIllum = new Vector3f[]
	{
		COLOR_ILLUM_CANDLE,
		COLOR_ILLUM_40WTUNGSTEN,
		COLOR_ILLUM_100WTUNGSTEN,
		COLOR_ILLUM_HALOGEN,
		COLOR_ILLUM_CARBONARC
	};
	
	
	private static Vector3f[] mFlour = new Vector3f[]
	{
		COLOR_FLOURESCENT_BLUE,
		COLOR_FLOURESCENT,
		COLOR_FLOURESCENT_WARM
	};
	
	
	private static Vector3f[] mGas = new Vector3f[]
	{
		COLOR_GAS_MERCURY,
		COLOR_GAS_SODIUM,
		COLOR_GAS_HALIDE,
		COLOR_GAS_SODIUM_HP
	};
	
	
	public GColor()
	{
		
	}
	
	
	public static Vector3f[] day()
	{
		return mDay;
	}
	
	
	public static Vector3f[] illum()
	{
		return mIllum;
	}
	
	
	public static Vector3f[] flourecent()
	{
		return mFlour;
	}
	
	
	public static Vector3f[] gas()
	{
		return mGas;
	}
}
