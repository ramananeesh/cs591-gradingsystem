package helper;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Size manager. Set component size according.
 */
public class SizeManager {

	private static Rectangle contentPaneBounds;
	private static int tableRowHeight;
	private static float fontTableSize;
	private static Rectangle tableCourseBounds;
	private static int[] courseTableColumnWidth;
	private static Rectangle searchCourseBounds;
	private static float fontSearchSize;
	private static Rectangle filterCourseBounds;
	private static float fontFilterSize;
	private static Rectangle categoryBounds;
	private static Rectangle itemBounds;
	private static Rectangle comboBounds;
	private static float fontLabelSize;
	private static Rectangle labelFilterBounds;
	private static Rectangle labelSearchBounds;
	private static Rectangle labelCategoryBounds;
	private static Rectangle labelItemBounds;
	private static Rectangle labelComboBounds;
	private static float fontButtonSize;
	private static Rectangle buttonAddBounds;
	private static Rectangle buttonViewBounds;
	private static Rectangle menuBarBounds;
	private static float fontMenuSize;
	private static Rectangle[] menuButtonsBounds;
	private static Rectangle textInfoBounds;
	private static float fontTextSize;
	private static Rectangle tableStudentBounds;
	private static Rectangle tableCategoryBounds;
	private static Rectangle tableItemBounds;
	private static int[] tableCategoryItemColumnWidth;
	private static int menuTableRowHeight;
	private static float fontMenuTableSize;
	private static float fontMenuButtonSize;
	private static Rectangle finalizeTitleLabelBounds;
	private static Rectangle labelGradeStatisticsBounds;
	private static Rectangle finalizeButtonBackBounds;
	private static Rectangle finalizeButtonCurveBounds;
	private static Rectangle finalizeButtonSaveBounds;
	private static Rectangle finalizeButtonFinalizeBounds;
	private static Rectangle viewGradeStatBounds;
	private static Rectangle viewGradeTitleBounds;
	private static Rectangle[] viewGradeComboBounds;
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static int optionPaneWidth = getScaledSize(600);
	private static int optionPaneRowHeight = getScaledSize(70);
	private static int loginWindowWidth = getScaledSize(800);
	private static int loginWindowHeight = getScaledSize(450);
	private static Rectangle titleBounds = new Rectangle(0, 0, loginWindowWidth, loginWindowHeight / 4);
	private static int loginWindowX = (int) ((screenSize.getWidth() - loginWindowWidth) / 2);
	private static int loginWindowY = (int) ((screenSize.getHeight() - loginWindowHeight) / 2);
	private static Rectangle loginWindowBounds = new Rectangle(loginWindowX, loginWindowY, loginWindowWidth, loginWindowHeight);
	private static int buttonLoginWidth = loginWindowWidth / 3;
	private static int buttonLoginHeight = loginWindowHeight / 6;
	private static float buttonLoginFontSize = (int) (buttonLoginHeight * 0.6f);
	private static int buttonLoginX = loginWindowWidth / 2 - buttonLoginWidth / 2;
	private static int buttonLoginY = loginWindowHeight * 2 / 3 - buttonLoginHeight / 2;
	private static Rectangle buttonLoginBounds = new Rectangle(buttonLoginX, buttonLoginY, buttonLoginWidth, buttonLoginHeight);
	private static float titleFontSize = (int) (titleBounds.height * 0.6f);
	
	/**
	 * Update component size according to frame size.
	 *
	 * @param frame Frame.
	 */
	public static void update(JFrame frame) {

		contentPaneBounds = frame.getContentPane().getBounds();
		int contentPaneWidth = frame.getContentPane().getWidth();
		int contentPaneHeight = frame.getContentPane().getHeight();

		// CourseWindow

		tableRowHeight = contentPaneHeight / 20;
		fontTableSize = (int) (getTableRowHeight() * 0.6f);

		int tableCourseWidth = contentPaneWidth * 7 / 8;
		int tableCourseHeight = getTableRowHeight() * 15;
		int tableCourseX = contentPaneWidth / 2 - tableCourseWidth / 2;
		int tableCourseY = contentPaneHeight / 2 - tableCourseHeight / 2;
		tableCourseBounds = new Rectangle(tableCourseX, tableCourseY, tableCourseWidth, tableCourseHeight);
		courseTableColumnWidth = new int[]{tableCourseWidth / 4, tableCourseWidth / 2, tableCourseWidth / 4};

		int midSearchFilter = contentPaneWidth / 15;

		int searchFilterCourseWidth = contentPaneWidth / 6;
		int searchFilterCourseHeight = contentPaneHeight / 20;
		int searchCourseX = contentPaneWidth / 2 + midSearchFilter;
		int searchFilterCourseY = tableCourseY / 2 - searchFilterCourseHeight / 2;
		searchCourseBounds = new Rectangle(searchCourseX, searchFilterCourseY, searchFilterCourseWidth, searchFilterCourseHeight);
		fontSearchSize = (int) (searchFilterCourseHeight * 0.6f);

		int filterCourseX = contentPaneWidth / 2 - searchFilterCourseWidth - midSearchFilter;
		filterCourseBounds = new Rectangle(filterCourseX, searchFilterCourseY, searchFilterCourseWidth, searchFilterCourseHeight);
		fontFilterSize = (int) (searchFilterCourseHeight * 0.6f);

		int comboWidth = contentPaneWidth / 8;

		int categoryX = tableCourseX + tableCourseWidth * 2 / 8 - comboWidth / 2;
		int categoryY = tableCourseY / 2 - searchFilterCourseHeight / 2;
		categoryBounds = new Rectangle(categoryX, categoryY, comboWidth, searchFilterCourseHeight);

		int itemX = tableCourseX + tableCourseWidth * 4 / 8 - comboWidth / 2;
		itemBounds = new Rectangle(itemX, searchFilterCourseY, comboWidth, searchFilterCourseHeight);

		int comboX = tableCourseX + tableCourseWidth * 6 / 8 - comboWidth / 2;
		comboBounds = new Rectangle(comboX, searchFilterCourseY, comboWidth, searchFilterCourseHeight);

		int labelHeight = contentPaneHeight / 20;
		fontLabelSize = (int) (labelHeight * 0.6f);

		labelFilterBounds = new Rectangle(0, searchFilterCourseY, filterCourseX, labelHeight);
		labelSearchBounds = new Rectangle(filterCourseX + searchFilterCourseWidth, searchFilterCourseY, searchCourseX - filterCourseX - searchFilterCourseWidth, labelHeight);
		labelCategoryBounds = new Rectangle(categoryX - comboWidth, searchFilterCourseY, comboWidth, labelHeight);
		labelItemBounds = new Rectangle(itemX - comboWidth, searchFilterCourseY, comboWidth, labelHeight);
		labelComboBounds = new Rectangle(comboX - comboWidth, searchFilterCourseY, comboWidth, labelHeight);


		int buttonWidth = contentPaneWidth / 15;
		int buttonHeight = contentPaneHeight / 20;
		fontButtonSize = (int) (buttonHeight * 0.6f);

		int midButtonCourse = contentPaneWidth / 40;
		int buttonAddX = contentPaneWidth / 2 - buttonWidth - midButtonCourse;
		int buttonViewX = contentPaneWidth / 2 + midButtonCourse;
		int buttonAddViewY = contentPaneHeight * 15 / 16 - buttonHeight / 2;
		buttonAddBounds = new Rectangle(buttonAddX, buttonAddViewY, buttonWidth, buttonHeight);
		buttonViewBounds = new Rectangle(buttonViewX, buttonAddViewY, buttonWidth, buttonHeight);

		// MenuWindow

		int menuBarHeight = contentPaneHeight / 20;
		menuBarBounds = new Rectangle(0, 0, contentPaneWidth, menuBarHeight);
		fontMenuSize = (int) (menuBarHeight * 0.6f);

		int panelWidth = contentPaneWidth / 3;
		int panelHeight = contentPaneHeight - menuBarHeight;

		textInfoBounds = new Rectangle(panelWidth, menuBarHeight, panelWidth, panelHeight - 5 * buttonHeight);
		fontTextSize = getFontMenuSize() * 0.8f;

		tableStudentBounds = new Rectangle(0, menuBarHeight, panelWidth, panelHeight);
		tableCategoryBounds = new Rectangle(2 * panelWidth, menuBarHeight, panelWidth, panelHeight / 2);
		tableItemBounds = new Rectangle(2 * panelWidth, menuBarHeight + panelHeight / 2, panelWidth, panelHeight / 2);
		tableCategoryItemColumnWidth = new int[]{panelWidth * 3 / 4, panelWidth / 4};
		menuTableRowHeight = panelHeight / 20;
		fontMenuTableSize = (int) (getMenuTableRowHeight() * 0.6f);
		fontMenuButtonSize = (int) (getMenuTableRowHeight() * 0.5f);
		int menuButtonsWidth = (panelWidth - buttonHeight * 3) / 2;//panelWidth * 4 / 10;
		int menuButtonMid = (panelWidth - menuButtonsWidth * 2) / 6;
		int menuButtonCenterY = menuBarHeight + (panelHeight - 5 * buttonHeight) + 5 * buttonHeight / 2;
		menuButtonsBounds = new Rectangle[]{
				new Rectangle(contentPaneWidth / 2 - menuButtonMid - menuButtonsWidth, menuButtonCenterY - buttonHeight - menuButtonMid, menuButtonsWidth, buttonHeight),
				new Rectangle(contentPaneWidth / 2 + menuButtonMid, menuButtonCenterY - buttonHeight - menuButtonMid, menuButtonsWidth, buttonHeight),
				new Rectangle(contentPaneWidth / 2 - menuButtonMid - menuButtonsWidth, menuButtonCenterY + menuButtonMid, menuButtonsWidth, buttonHeight),
				new Rectangle(contentPaneWidth / 2 + menuButtonMid, menuButtonCenterY + menuButtonMid, menuButtonsWidth, buttonHeight),
		};

		finalizeTitleLabelBounds = new Rectangle(0, 0, contentPaneWidth, contentPaneHeight / 20);
		labelGradeStatisticsBounds = new Rectangle(0, contentPaneHeight / 20, contentPaneWidth, contentPaneHeight / 20);
		int midFinalize = contentPaneWidth / 16;
		int[] buttonBackCurveFinalizeX = new int[]{
				contentPaneWidth / 2 - buttonWidth * 2 - midFinalize * 3 / 2,
				contentPaneWidth / 2 - buttonWidth - midFinalize / 2,
				contentPaneWidth / 2 + midFinalize / 2,
				contentPaneWidth / 2 + buttonWidth + midFinalize * 3 / 2
		};
		int buttonBackCurveFinalizeY = contentPaneHeight * 15 / 16 - buttonHeight / 2;
		int buttonBackCurveFinalizeWidth = contentPaneWidth / 10;
		finalizeButtonBackBounds = new Rectangle(buttonBackCurveFinalizeX[0], buttonBackCurveFinalizeY, buttonBackCurveFinalizeWidth, buttonHeight);
		finalizeButtonCurveBounds = new Rectangle(buttonBackCurveFinalizeX[1], buttonBackCurveFinalizeY, buttonBackCurveFinalizeWidth, buttonHeight);
		finalizeButtonSaveBounds = new Rectangle(buttonBackCurveFinalizeX[2], buttonBackCurveFinalizeY, buttonBackCurveFinalizeWidth, buttonHeight);
		finalizeButtonFinalizeBounds = new Rectangle(buttonBackCurveFinalizeX[3], buttonBackCurveFinalizeY, buttonBackCurveFinalizeWidth, buttonHeight);

		viewGradeTitleBounds = new Rectangle(0, 0, contentPaneWidth, contentPaneHeight / 8 / 3);
		viewGradeStatBounds = new Rectangle(0, contentPaneHeight / 8 / 3, contentPaneWidth, contentPaneHeight / 8 / 3);
		viewGradeComboBounds = new Rectangle[]{
				new Rectangle(tableCourseX + tableCourseWidth / 4 - comboWidth, contentPaneHeight / 8 / 3 * 2, comboWidth, contentPaneHeight / 8 / 3),
				new Rectangle(tableCourseX + tableCourseWidth / 4, contentPaneHeight / 8 / 3 * 2, comboWidth, contentPaneHeight / 8 / 3),
				new Rectangle(tableCourseX + tableCourseWidth * 3 / 4 - comboWidth * 2, contentPaneHeight / 8 / 3 * 2, comboWidth, contentPaneHeight / 8 / 3),
				new Rectangle(tableCourseX + tableCourseWidth * 3 / 4 - comboWidth, contentPaneHeight / 8 / 3 * 2, comboWidth, contentPaneHeight / 8 / 3),
		};
		FontManager.update();
	}

	private static int getScaledSize(double size) {
		return (int) (size / 2000 * Math.max(screenSize.getWidth(), screenSize.getHeight()));
	}

	public static float getTitleFontSize() {
		return titleFontSize;
	}

	public static Rectangle getContentPaneBounds() {
		return contentPaneBounds;
	}

	public static int getTableRowHeight() {
		return tableRowHeight;
	}

	public static float getFontTableSize() {
		return fontTableSize;
	}

	public static Rectangle getTableCourseBounds() {
		return tableCourseBounds;
	}

	public static int[] getCourseTableColumnWidth() {
		return courseTableColumnWidth;
	}

	public static Rectangle getSearchCourseBounds() {
		return searchCourseBounds;
	}

	public static float getFontSearchSize() {
		return fontSearchSize;
	}

	public static Rectangle getFilterCourseBounds() {
		return filterCourseBounds;
	}

	public static float getFontFilterSize() {
		return fontFilterSize;
	}

	public static Rectangle getCategoryBounds() {
		return categoryBounds;
	}

	public static Rectangle getItemBounds() {
		return itemBounds;
	}

	public static Rectangle getComboBounds() {
		return comboBounds;
	}

	public static float getFontLabelSize() {
		return fontLabelSize;
	}

	public static Rectangle getLabelFilterBounds() {
		return labelFilterBounds;
	}

	public static Rectangle getLabelSearchBounds() {
		return labelSearchBounds;
	}

	public static Rectangle getLabelCategoryBounds() {
		return labelCategoryBounds;
	}

	public static Rectangle getLabelItemBounds() {
		return labelItemBounds;
	}

	public static Rectangle getLabelComboBounds() {
		return labelComboBounds;
	}

	public static float getFontButtonSize() {
		return fontButtonSize;
	}

	public static Rectangle getButtonAddBounds() {
		return buttonAddBounds;
	}

	public static Rectangle getButtonViewBounds() {
		return buttonViewBounds;
	}

	public static Rectangle getMenuBarBounds() {
		return menuBarBounds;
	}

	public static float getFontMenuSize() {
		return fontMenuSize;
	}

	public static Rectangle[] getMenuButtonsBounds() {
		return menuButtonsBounds;
	}

	public static Rectangle getTextInfoBounds() {
		return textInfoBounds;
	}

	public static float getFontTextSize() {
		return fontTextSize;
	}

	public static Rectangle getTableStudentBounds() {
		return tableStudentBounds;
	}

	public static Rectangle getTableCategoryBounds() {
		return tableCategoryBounds;
	}

	public static Rectangle getTableItemBounds() {
		return tableItemBounds;
	}

	public static int[] getTableCategoryItemColumnWidth() {
		return tableCategoryItemColumnWidth;
	}

	public static int getMenuTableRowHeight() {
		return menuTableRowHeight;
	}

	public static float getFontMenuTableSize() {
		return fontMenuTableSize;
	}

	public static float getFontMenuButtonSize() {
		return fontMenuButtonSize;
	}

	public static Rectangle getFinalizeTitleLabelBounds() {
		return finalizeTitleLabelBounds;
	}

	public static Rectangle getLabelGradeStatisticsBounds() {
		return labelGradeStatisticsBounds;
	}

	public static Rectangle getFinalizeButtonBackBounds() {
		return finalizeButtonBackBounds;
	}

	public static Rectangle getFinalizeButtonCurveBounds() {
		return finalizeButtonCurveBounds;
	}

	public static Rectangle getFinalizeButtonSaveBounds() {
		return finalizeButtonSaveBounds;
	}

	public static Rectangle getFinalizeButtonFinalizeBounds() {
		return finalizeButtonFinalizeBounds;
	}

	public static Rectangle getViewGradeStatBounds() {
		return viewGradeStatBounds;
	}

	public static Rectangle getViewGradeTitleBounds() {
		return viewGradeTitleBounds;
	}

	public static Rectangle[] getViewGradeComboBounds() {
		return viewGradeComboBounds;
	}

	public static int getOptionPaneWidth() {
		return optionPaneWidth;
	}

	public static int getOptionPaneRowHeight() {
		return optionPaneRowHeight;
	}

	public static Rectangle getTitleBounds() {
		return titleBounds;
	}

	public static Rectangle getLoginWindowBounds() {
		return loginWindowBounds;
	}

	public static float getButtonLoginFontSize() {
		return buttonLoginFontSize;
	}

	public static Rectangle getButtonLoginBounds() {
		return buttonLoginBounds;
	}

}