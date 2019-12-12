package helper;

import java.awt.*;

import javax.swing.JFrame;

public class SizeManager {

	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	// LoginWindow

	public static int loginWindowWidth = getScaledSize(800);
	public static int loginWindowHeight = getScaledSize(450);
	public static int loginWindowX = (int) ((screenSize.getWidth() - loginWindowWidth) / 2);
	public static int loginWindowY = (int) ((screenSize.getHeight() - loginWindowHeight) / 2);
	public static Rectangle loginWindowBounds = new Rectangle(loginWindowX, loginWindowY, loginWindowWidth, loginWindowHeight);

	public static Rectangle titleBounds = new Rectangle(0, 0, loginWindowWidth, loginWindowHeight / 4);
	public static float titleFontSize = (int) (titleBounds.height * 0.6f);

	public static int buttonLoginWidth = loginWindowWidth / 3;
	public static int buttonLoginHeight = loginWindowHeight / 6;
	public static int buttonLoginX = loginWindowWidth / 2 - buttonLoginWidth / 2;
	public static int buttonLoginY = loginWindowHeight * 2 / 3 - buttonLoginHeight / 2;
	public static Rectangle buttonLoginBounds = new Rectangle(buttonLoginX, buttonLoginY, buttonLoginWidth, buttonLoginHeight);
	public static float buttonLoginFontSize = (int) (buttonLoginHeight * 0.6f);

	// CourseWindow

	public static int contentPaneWidth;
	public static int contentPaneHeight;
	public static Rectangle contentPaneBounds;

	public static int tableRowHeight;
	public static float fontTableSize;

	public static int tableCourseWidth;
	public static int tableCourseHeight;
	public static int tableCourseX;
	public static int tableCourseY;
	public static Rectangle tableCourseBounds;
	public static int[] courseTableColumnWidth;

	public static int searchFilterCourseWidth;
	public static int searchFilterCourseHeight;
	public static int searchCourseX;
	public static int searchFilterCourseY;
	public static Rectangle searchCourseBounds;
	public static float fontSearchSize;

	public static int filterCourseX;
	public static Rectangle filterCourseBounds;
	public static float fontFilterSize;

	public static int comboWidth;

	public static int categoryX;
	public static int categoryY;
	public static Rectangle categoryBounds;
	public static float categorySize;

	public static int itemX;
	public static Rectangle itemBounds;
	public static float fontItemSize;


	public static int comboX;
	public static Rectangle comboBounds;
	public static float comboFilterSize;


	public static int labelHeight;
	public static float fontLabelSize;

	public static Rectangle labelFilterBounds;
	public static Rectangle labelSearchBounds;

	public static Rectangle labelCategoryBounds;
	public static Rectangle labelItemBounds;
	public static Rectangle labelComboBounds;


	public static int buttonWidth;
	public static int buttonHeight;
	public static float fontButtonSize;

	public static int midButtonCourse;
	public static int buttonAddX;
	public static int buttonViewX;
	public static int buttonAddViewY;
	public static Rectangle buttonAddBounds;
	public static Rectangle buttonViewBounds;

	// MenuWindow

	public static int menuBarHeight;
	public static Rectangle menuBarBounds;
	public static float fontMenuSize;
	public static int lineThickness;

	public static int panelWidth;
	public static int panelHeight;

	public static Rectangle textInfoBounds;
	public static float fontTextSize;

	public static Rectangle tableStudentBounds;
	public static Rectangle tableCategoryBounds;
	public static Rectangle tableItemBounds;
	public static int[] tableCategoryItemColumnWidth;
	public static int menuTableRowHeight;
	public static float fontMenuTableSize;

	public static Rectangle finalizeTitleLabelBounds;
	public static Rectangle labelGradeStatisticsBounds;
	public static Rectangle finalizeButtonBackBounds;
	public static Rectangle finalizeButtonCurveBounds;
	public static Rectangle finalizeButtonSaveBounds;
	public static Rectangle finalizeButtonFinalizeBounds;


	public static Dimension optionPaneDimension = new Dimension(getScaledSize(600), getScaledSize(600));


	public static void update(JFrame frame) {

		contentPaneBounds = frame.getContentPane().getBounds();
		contentPaneWidth = frame.getContentPane().getWidth();
		contentPaneHeight = frame.getContentPane().getHeight();

		// CourseWindow

		tableRowHeight = contentPaneHeight / 20;
		fontTableSize = (int) (tableRowHeight * 0.6f);

		tableCourseWidth = contentPaneWidth * 7 / 8;
		tableCourseHeight = tableRowHeight * 15;
		tableCourseX = contentPaneWidth / 2 - tableCourseWidth / 2;
		tableCourseY = contentPaneHeight / 2 - tableCourseHeight / 2;
		tableCourseBounds = new Rectangle(tableCourseX, tableCourseY, tableCourseWidth, tableCourseHeight);
		courseTableColumnWidth = new int[]{tableCourseWidth / 4, tableCourseWidth / 2, tableCourseWidth / 4};

		int midSearchFilter = contentPaneWidth / 15;

		searchFilterCourseWidth = contentPaneWidth / 6;
		searchFilterCourseHeight = contentPaneHeight / 20;
		searchCourseX = contentPaneWidth / 2 + midSearchFilter;
		searchFilterCourseY = tableCourseY / 2 - searchFilterCourseHeight / 2;
		searchCourseBounds = new Rectangle(searchCourseX, searchFilterCourseY, searchFilterCourseWidth, searchFilterCourseHeight);
		fontSearchSize = (int) (searchFilterCourseHeight * 0.6f);

		filterCourseX = contentPaneWidth / 2 - searchFilterCourseWidth - midSearchFilter;
		filterCourseBounds = new Rectangle(filterCourseX, searchFilterCourseY, searchFilterCourseWidth, searchFilterCourseHeight);
		fontFilterSize = (int) (searchFilterCourseHeight * 0.6f);

		comboWidth = contentPaneWidth / 8;

		categoryX = filterCourseX + midSearchFilter / 5;//windowWidth / 2 - 2*searchFilterCourseWidth;
		categoryY = tableCourseY / 2 - searchFilterCourseHeight / 2;
		categoryBounds = new Rectangle(categoryX, categoryY, comboWidth, searchFilterCourseHeight);
		categorySize = (int) (searchFilterCourseHeight * 0.6f);

		itemX = categoryX + 3 * midSearchFilter;
		itemBounds = new Rectangle(itemX, searchFilterCourseY, comboWidth, searchFilterCourseHeight);
		fontItemSize = (int) (searchFilterCourseHeight * 0.6f);


		comboX = itemX + 3 * midSearchFilter + midSearchFilter / 2;
		comboBounds = new Rectangle(comboX, searchFilterCourseY, comboWidth, searchFilterCourseHeight);
		comboFilterSize = (int) (searchFilterCourseHeight * 0.6f);


		labelHeight = contentPaneHeight / 20;
		fontLabelSize = (int) (labelHeight * 0.6f);

		labelFilterBounds = new Rectangle(0, searchFilterCourseY, filterCourseX, labelHeight);
		labelSearchBounds = new Rectangle(filterCourseX + searchFilterCourseWidth, searchFilterCourseY, searchCourseX - filterCourseX - searchFilterCourseWidth, labelHeight);

		labelCategoryBounds = new Rectangle(0, searchFilterCourseY, filterCourseX, labelHeight);
		labelItemBounds = new Rectangle(categoryX + 2 * midSearchFilter / 3, searchFilterCourseY, searchCourseX - filterCourseX - searchFilterCourseWidth, labelHeight);
		labelComboBounds = new Rectangle(categoryX + 4 * midSearchFilter, searchFilterCourseY, searchCourseX - filterCourseX - searchFilterCourseWidth, labelHeight);


		buttonWidth = contentPaneWidth / 15;
		buttonHeight = contentPaneHeight / 20;
		fontButtonSize = (int) (buttonHeight * 0.6f);

		int midButtonCourse = contentPaneWidth / 40;
		buttonAddX = contentPaneWidth / 2 - buttonWidth - midButtonCourse;
		buttonViewX = contentPaneWidth / 2 + midButtonCourse;
		buttonAddViewY = contentPaneHeight * 15 / 16 - buttonHeight / 2;// contentPaneHeight * 179 / 200;
		buttonAddBounds = new Rectangle(buttonAddX, buttonAddViewY, buttonWidth, buttonHeight);
		buttonViewBounds = new Rectangle(buttonViewX, buttonAddViewY, buttonWidth, buttonHeight);

		// MenuWindow

		menuBarHeight = contentPaneHeight / 20;
		menuBarBounds = new Rectangle(0, 0, contentPaneWidth, menuBarHeight);
		fontMenuSize = (int) (menuBarHeight * 0.6f);
		lineThickness = contentPaneWidth / 500;

		panelWidth = (int) (contentPaneWidth * 0.3332);
		panelHeight = (int) (contentPaneHeight - menuBarHeight);

		textInfoBounds = new Rectangle(panelWidth, menuBarHeight, panelWidth, panelHeight);
		fontTextSize = (int) fontMenuSize;

		tableStudentBounds = new Rectangle(0, menuBarHeight, panelWidth, panelHeight);
		tableCategoryBounds = new Rectangle(2 * panelWidth, menuBarHeight, panelWidth, panelHeight / 2);
		tableItemBounds = new Rectangle(2 * panelWidth, menuBarHeight + panelHeight / 2, panelWidth, panelHeight / 2);
		tableCategoryItemColumnWidth = new int[]{panelWidth * 3 / 4, panelWidth / 4};
		menuTableRowHeight = (int) (panelHeight * 0.051);
		fontMenuTableSize = (int) (menuTableRowHeight * 0.6f);

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

		FontManager.update();
	}

	public static int getScaledSize(double size) {
		return (int) (size / 2000 * Math.max(screenSize.getWidth(), screenSize.getHeight()));
	}
}