package helper;

import java.awt.Color;

/**
 * Color manager.
 */
public class ColorManager {

	/** Primary color. */
	private static final Color primaryColor = new Color(0x1976d2); // Material Blue 700

	/** Light color. */
	private static final Color lightColor = Color.WHITE;

	/** Dark color. */
	private static final Color darkColor = Color.BLACK;

	/** Opaque color. */
	private static final Color opaqueColor = new Color(0, 0, 0, 0);

	/**
	 * Get primary color.
	 *
	 * @return Primary color.
	 */
	public static Color getPrimaryColor() {
		return primaryColor;
	}

	/**
	 * Get light color.
	 *
	 * @return Light color.
	 */
	public static Color getLightColor() {
		return lightColor;
	}

	/**
	 * Get dark color.
	 *
	 * @return Dark color.
	 */
	public static Color getDarkColor() {
		return darkColor;
	}

	/**
	 * Get opaque color.
	 *
	 * @return Opaque color.
	 */
	public static Color getOpaqueColor() {
		return opaqueColor;
	}

}