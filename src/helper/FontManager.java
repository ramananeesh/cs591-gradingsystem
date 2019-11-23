package helper;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FontManager {
	private static final Font fontBauhaus = setFont("Bauhaus 93");
	private static final Font fontCascadia = setFont("Cascadia");
	private static final Font fontConsolas = setFont("Consolas");
	private static final Font fontProductSans = setFont("Product Sans");

	public static final Font fontDefault = fontProductSans;

	public static final Font fontTitle = fontDefault.deriveFont(Font.PLAIN, SizeManager.titleFontSize);
	public static final Font fontLogin = fontDefault.deriveFont(Font.PLAIN, SizeManager.buttonLoginFontSize);

	public static final Font fontTable = fontDefault.deriveFont(SizeManager.fontTableSize);
	public static final Font fontSearch = fontDefault.deriveFont(SizeManager.fontSearchSize);
	public static final Font fontFilter = fontDefault.deriveFont(SizeManager.fontFilterSize);
	public static final Font fontButton = fontDefault.deriveFont(SizeManager.fontButtonSize);
	public static final Font fontLabel = fontDefault.deriveFont(SizeManager.fontLabelSize);
	public static final Font fontMenu = fontDefault.deriveFont(SizeManager.fontMenuSize);
	public static final Font fontText = fontDefault.deriveFont(SizeManager.fontTextSize);
	public static final Font fontMenuTable = fontDefault.deriveFont(SizeManager.fontMenuTableSize);

	public static Font setFont(String name) {
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("src/resource/" + name + ".ttf"));
		} catch (Exception e) {
			System.out.println("load font fail. use default font.");
			font = new JLabel().getFont();
		}
		return font;
	}
}