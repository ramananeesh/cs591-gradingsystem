package helper;

import java.awt.Font;
import java.io.File;

import javax.swing.JLabel;

/**
 * Font manager.
 */
public class FontManager {

	/** Font : Product Sans. */
	private static Font fontProductSans = setFont("Product Sans");

	/** Font : Bauhaus 93. */
	private static Font fontBauhaus = setFont("Bauhaus 93");

	/** Font : Cascadia Code. */
	private static Font fontCascadia = setFont("Cascadia");

	/** Font : Consolas. */
	private static Font fontConsolas = setFont("Consolas");

	/** Default font. */
	private static Font fontDefault = fontProductSans;

	/** Font for title. */
	private static Font fontTitle = fontDefault.deriveFont(SizeManager.getTitleFontSize());

	/** Font for login button. */
	private static Font fontLogin = fontDefault.deriveFont(SizeManager.getButtonLoginFontSize());

	/** Font for table. */
	private static Font fontTable = fontDefault.deriveFont(SizeManager.getFontTableSize());

	/** Font for search bar. */
	private static Font fontSearch = fontDefault.deriveFont(SizeManager.getFontSearchSize());

	/** Font for filter. */
	private static Font fontFilter = fontDefault.deriveFont(SizeManager.getFontFilterSize());

	/** Font for button. */
	private static Font fontButton = fontDefault.deriveFont(SizeManager.getFontButtonSize());

	/** Font for label. */
	private static Font fontLabel = fontDefault.deriveFont(SizeManager.getFontLabelSize());

	/** Font for menu bar. */
	private static Font fontMenu = fontDefault.deriveFont(SizeManager.getFontMenuSize());

	/** Font for text. */
	private static Font fontText = fontDefault.deriveFont(SizeManager.getFontTextSize());

	/** Font for menu table. */
	private static Font fontMenuTable = fontDefault.deriveFont(SizeManager.getFontMenuTableSize());

	/** Font for menu button. */
	private static Font fontMenuButton;

	/** Font for password. */
	private static Font fontPassword = fontCascadia.deriveFont(Font.BOLD, 35);

	/**
	 * Load font file.
	 *
	 * @param fileName Font file name.
	 * @return Loaded font.
	 */
	private static Font setFont(String fileName) {
		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("src/resource/" + fileName + ".ttf"));
		} catch (Exception e) {
			System.out.println("load font fail. use default font.");
			font = new JLabel().getFont();
		}
		return font;
	}

	/**
	 * Update font size.
	 */
	public static void update() {
		fontTitle = fontDefault.deriveFont(Font.PLAIN, SizeManager.getTitleFontSize());
		fontLogin = fontDefault.deriveFont(Font.PLAIN, SizeManager.getButtonLoginFontSize());
		fontTable = fontDefault.deriveFont(SizeManager.getFontTableSize());
		fontSearch = fontDefault.deriveFont(SizeManager.getFontSearchSize());
		fontFilter = fontDefault.deriveFont(SizeManager.getFontFilterSize());
		fontButton = fontDefault.deriveFont(SizeManager.getFontButtonSize());
		fontLabel = fontDefault.deriveFont(SizeManager.getFontLabelSize());
		fontMenu = fontDefault.deriveFont(SizeManager.getFontMenuSize());
		fontText = fontDefault.deriveFont(SizeManager.getFontTextSize());
		fontMenuTable = fontDefault.deriveFont(SizeManager.getFontMenuTableSize());
		fontMenuButton = fontDefault.deriveFont(SizeManager.getFontMenuButtonSize());
	}

	/**
	 * Get font for title.
	 *
	 * @return Font for title.
	 */
	public static Font getFontTitle() {
		return fontTitle;
	}

	/**
	 * Get font for login button.
	 *
	 * @return Font for login button.
	 */
	public static Font getFontLogin() {
		return fontLogin;
	}

	/**
	 * Get font for table.
	 *
	 * @return Font for table.
	 */
	public static Font getFontTable() {
		return fontTable;
	}

	/**
	 * Get font for search bar.
	 *
	 * @return Font for search bar.
	 */
	public static Font getFontSearch() {
		return fontSearch;
	}

	/**
	 * Get Font for filter.
	 *
	 * @return Font for filter.
	 */
	public static Font getFontFilter() {
		return fontFilter;
	}

	/**
	 * Get font for button.
	 *
	 * @return Font for button.
	 */
	public static Font getFontButton() {
		return fontButton;
	}

	/**
	 * Get font for label.
	 *
	 * @return Font for label.
	 */
	public static Font getFontLabel() {
		return fontLabel;
	}

	/**
	 * Get font for menu bar.
	 *
	 * @return Font for menu bar.
	 */
	public static Font getFontMenu() {
		return fontMenu;
	}

	/**
	 * Get font for text.
	 *
	 * @return Font for text.
	 */
	public static Font getFontText() {
		return fontText;
	}

	/**
	 * Get font for menu table.
	 *
	 * @return Font for menu table.
	 */
	public static Font getFontMenuTable() {
		return fontMenuTable;
	}

	/**
	 * Get font for menu button.
	 *
	 * @return Font for menu button.
	 */
	public static Font getFontMenuButton() {
		return fontMenuButton;
	}

	/**
	 * Get font for password.
	 *
	 * @return Font for password.
	 */
	public static Font getFontPassword() {
		return fontPassword;
	}

}