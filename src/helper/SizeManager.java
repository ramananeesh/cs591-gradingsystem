package helper;

import java.awt.*;

import javax.swing.JFrame;

public class SizeManager {

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	// LoginWindow

	private static int loginWindowWidth = getScaledSize(800);
	private static int loginWindowHeight = getScaledSize(450);
	private static int loginWindowX = (int) ((screenSize.getWidth() - loginWindowWidth) / 2);
	private static int loginWindowY = (int) ((screenSize.getHeight() - loginWindowHeight) / 2);
	public static Rectangle loginWindowBounds = new Rectangle(loginWindowX, loginWindowY, loginWindowWidth, loginWindowHeight);

	public static Rectangle titleBounds = new Rectangle(0, 0, loginWindowWidth, loginWindowHeight / 4);
	public static float titleFontSize = (int) (titleBounds.height * 0.6f);

	private static int buttonLoginWidth = loginWindowWidth / 3;
	private static int buttonLoginHeight = loginWindowHeight / 6;
	private static int buttonLoginX = loginWindowWidth / 2 - buttonLoginWidth / 2;
	private static int buttonLoginY = loginWindowHeight * 2 / 3 - buttonLoginHeight / 2;
	public static Rectangle buttonLoginBounds = new Rectangle(buttonLoginX, buttonLoginY, buttonLoginWidth, buttonLoginHeight);
	public static float buttonLoginFontSize = (int) (buttonLoginHeight * 0.6f);

	// CourseWindow

	public static Rectangle contentPaneBounds;

	public static int tableRowHeight;
	public static float fontTableSize;

	public static Rectangle tableCourseBounds;
	public static int[] courseTableColumnWidth;

	public static Rectangle searchCourseBounds;
	public static float fontSearchSize;

	public static Rectangle filterCourseBounds;
	public static float fontFilterSize;

	public static Rectangle categoryBounds;

	public static Rectangle itemBounds;

	public static Rectangle comboBounds;


	public static float fontLabelSize;

	public static Rectangle labelFilterBounds;
	public static Rectangle labelSearchBounds;

	public static Rectangle labelCategoryBounds;
	public static Rectangle labelItemBounds;
	public static Rectangle labelComboBounds;

	public static float fontButtonSize;

	public static Rectangle buttonAddBounds;
	public static Rectangle buttonViewBounds;

	// MenuWindow

	public static Rectangle menuBarBounds;
	public static float fontMenuSize;
	public static Rectangle[] menuButtonsBounds;

	public static Rectangle textInfoBounds;
	public static float fontTextSize;

	public static Rectangle tableStudentBounds;
	public static Rectangle tableCategoryBounds;
	public static Rectangle tableItemBounds;
	public static int[] tableCategoryItemColumnWidth;
	public static int menuTableRowHeight;
	public static float fontMenuTableSize;
	public static float fontMenuButtonSize;

	public static Rectangle finalizeTitleLabelBounds;
	public static Rectangle labelGradeStatisticsBounds;
	public static Rectangle finalizeButtonBackBounds;
	public static Rectangle finalizeButtonCurveBounds;
	public static Rectangle finalizeButtonSaveBounds;
	public static Rectangle finalizeButtonFinalizeBounds;

	public static int optionPaneWidth = getScaledSize(600);
	public static int optionPaneRowHeight = getScaledSize(70);
	public static Rectangle viewGradeStatBounds;
	public static Rectangle viewGradeTitleBounds;
	public static Rectangle[] viewGradeComboBounds;
	public static Rectangle viewGradeLockedButtonBounds;
	public static Rectangle testLockButtonBounds;

	public static void update(JFrame frame) {

		contentPaneBounds = frame.getContentPane().getBounds();
		int contentPaneWidth = frame.getContentPane().getWidth();
		int contentPaneHeight = frame.getContentPane().getHeight();

		// CourseWindow

		tableRowHeight = contentPaneHeight / 20;
		fontTableSize = (int) (tableRowHeight * 0.6f);

		int tableCourseWidth = contentPaneWidth * 7 / 8;
		int tableCourseHeight = tableRowHeight * 15;
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
		int buttonAddViewY = contentPaneHeight * 15 / 16 - buttonHeight / 2;// contentPaneHeight * 179 / 200;
		buttonAddBounds = new Rectangle(buttonAddX, buttonAddViewY, buttonWidth, buttonHeight);
		buttonViewBounds = new Rectangle(buttonViewX, buttonAddViewY, buttonWidth, buttonHeight);

		// MenuWindow

		int menuBarHeight = contentPaneHeight / 20;
		menuBarBounds = new Rectangle(0, 0, contentPaneWidth, menuBarHeight);
		fontMenuSize = (int) (menuBarHeight * 0.6f);

		int panelWidth = contentPaneWidth / 3;
		int panelHeight = contentPaneHeight - menuBarHeight;

		textInfoBounds = new Rectangle(panelWidth, menuBarHeight, panelWidth, panelHeight - 5 * buttonHeight);
		fontTextSize = fontMenuSize;

		tableStudentBounds = new Rectangle(0, menuBarHeight, panelWidth, panelHeight);
		tableCategoryBounds = new Rectangle(2 * panelWidth, menuBarHeight, panelWidth, panelHeight / 2);
		tableItemBounds = new Rectangle(2 * panelWidth, menuBarHeight + panelHeight / 2, panelWidth, panelHeight / 2);
		tableCategoryItemColumnWidth = new int[]{panelWidth * 3 / 4, panelWidth / 4};
		menuTableRowHeight = panelHeight / 20;
		fontMenuTableSize = (int) (menuTableRowHeight * 0.6f);
		fontMenuButtonSize = (int) (menuTableRowHeight * 0.5f);
		int menuButtonsWidth = (panelWidth - buttonHeight * 3) / 2;//panelWidth * 4 / 10;
		int menuButtonMid = (panelWidth - menuButtonsWidth * 2) / 6;
		int menuButtonCenterY = menuBarHeight + (panelHeight - 5 * buttonHeight) + 5 * buttonHeight / 2;
		menuButtonsBounds = new Rectangle[]{
				new Rectangle(contentPaneWidth / 2 - menuButtonMid - menuButtonsWidth, menuButtonCenterY - buttonHeight - menuButtonMid, menuButtonsWidth, buttonHeight),
				new Rectangle(contentPaneWidth / 2 + menuButtonMid, menuButtonCenterY - buttonHeight - menuButtonMid, menuButtonsWidth, buttonHeight),
				new Rectangle(contentPaneWidth / 2 - menuButtonMid - menuButtonsWidth, menuButtonCenterY + menuButtonMid, menuButtonsWidth, buttonHeight),
				new Rectangle(contentPaneWidth / 2 + menuButtonMid, menuButtonCenterY + menuButtonMid, menuButtonsWidth, buttonHeight),
		};
		viewGradeLockedButtonBounds = new Rectangle(contentPaneWidth / 2 - menuButtonsWidth / 2, menuButtonCenterY - buttonHeight / 2, menuButtonsWidth, buttonHeight);
		testLockButtonBounds = new Rectangle(contentPaneWidth / 2 - menuButtonsWidth / 2, contentPaneHeight - buttonHeight, menuButtonsWidth, buttonHeight);

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

	public static int getScaledSize(double size) {
		return (int) (size / 2000 * Math.max(screenSize.getWidth(), screenSize.getHeight()));
	}
}