package helper;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FontManager {

	private static  Font fontBauhaus = setFont("Bauhaus 93");
	private static  Font fontCascadia = setFont("Cascadia");
	private static  Font fontConsolas = setFont("Consolas");
	private static  Font fontProductSans = setFont("Product Sans");

	public static  Font fontDefault = fontProductSans;

	public static  Font fontTitle = fontDefault.deriveFont(Font.PLAIN, SizeManager.titleFontSize);
	public static  Font fontLogin = fontDefault.deriveFont(Font.PLAIN, SizeManager.buttonLoginFontSize);

	public static  Font fontTable = fontDefault.deriveFont(SizeManager.fontTableSize);
	public static  Font fontSearch = fontDefault.deriveFont(SizeManager.fontSearchSize);
	public static  Font fontFilter = fontDefault.deriveFont(SizeManager.fontFilterSize);
	public static  Font fontButton = fontDefault.deriveFont(SizeManager.fontButtonSize);
	public static  Font fontLabel = fontDefault.deriveFont(SizeManager.fontLabelSize);
	public static  Font fontMenu = fontDefault.deriveFont(SizeManager.fontMenuSize);
	public static  Font fontText = fontDefault.deriveFont(SizeManager.fontTextSize);
	public static  Font fontMenuTable = fontDefault.deriveFont(SizeManager.fontMenuTableSize);
    public static Font fontMenuButton;

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

	public static void update() {
		 fontTitle = fontDefault.deriveFont(Font.PLAIN, SizeManager.titleFontSize);
		 fontLogin = fontDefault.deriveFont(Font.PLAIN, SizeManager.buttonLoginFontSize);

		 fontTable = fontDefault.deriveFont(SizeManager.fontTableSize);
		 fontSearch = fontDefault.deriveFont(SizeManager.fontSearchSize);
		 fontFilter = fontDefault.deriveFont(SizeManager.fontFilterSize);
		 fontButton = fontDefault.deriveFont(SizeManager.fontButtonSize);
		 fontLabel = fontDefault.deriveFont(SizeManager.fontLabelSize);
		 fontMenu = fontDefault.deriveFont(SizeManager.fontMenuSize);
		 fontText = fontDefault.deriveFont(SizeManager.fontTextSize);
		 fontMenuTable = fontDefault.deriveFont(SizeManager.fontMenuTableSize);
        fontMenuButton = fontDefault.deriveFont(SizeManager.fontMenuButtonSize);
	}
}