package helper;

import java.awt.*;

public class SizeManager {

	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	// LoginWindow

	public static final int loginWindowWidth = getScaledSize(800);
	public static final int loginWindowHeight = getScaledSize(450);
	public static final int loginWindowX = (int) ((screenSize.getWidth() - loginWindowWidth) / 2);
	public static final int loginWindowY = (int) ((screenSize.getHeight() - loginWindowHeight) / 2);
	public static final Rectangle loginWindowBounds = new Rectangle(loginWindowX, loginWindowY, loginWindowWidth, loginWindowHeight);

	public static final Rectangle titleBounds = new Rectangle(0, 0, loginWindowWidth, loginWindowHeight / 4);
	public static final float titleFontSize = (int) (titleBounds.height * 0.6f);

	public static final int buttonLoginWidth = loginWindowWidth / 3;
	public static final int buttonLoginHeight = loginWindowHeight / 6;
	public static final int buttonLoginX = loginWindowWidth / 2 - buttonLoginWidth / 2;
	public static final int buttonLoginY = loginWindowHeight * 2 / 3 - buttonLoginHeight / 2;
	public static final Rectangle buttonLoginBounds = new Rectangle(buttonLoginX, buttonLoginY, buttonLoginWidth, buttonLoginHeight);
	public static final float buttonLoginFontSize = (int) (buttonLoginHeight * 0.6f);

	// CourseWindow

	public static final int windowWidth = getScaledSize(1900);
	public static final int windowHeight = getScaledSize(1000);
	public static final int windowX = (int) ((screenSize.getWidth() - windowWidth) / 2);
	public static final int windowY = (int) ((screenSize.getHeight() - windowHeight) / 2);
	public static final Rectangle windowBounds = new Rectangle(windowX, windowY, windowWidth, windowHeight);
	public static final Rectangle panelBounds = new Rectangle(0, 0, windowWidth, windowHeight);

	public static final int tableRowHeight = windowHeight / 20;
	public static final float fontTableSize = (int) (tableRowHeight * 0.6f);

	public static final int tableCourseWidth = windowWidth * 7 / 8;
	public static final int tableCourseHeight = tableRowHeight * 15;
	public static final int tableCourseX = windowWidth / 2 - tableCourseWidth / 2;
	public static final int tableCourseY = windowHeight / 2 - tableCourseHeight / 2;
	public static final Rectangle tableCourseBounds = new Rectangle(tableCourseX, tableCourseY, tableCourseWidth, tableCourseHeight);
	public static final int[] courseTableColumnWidth = {tableCourseWidth / 4, tableCourseWidth / 2, tableCourseWidth / 4};

	public static final int midSearchFilter = windowWidth / 15;

	public static final int searchFilterCourseWidth = windowWidth / 6;
	public static final int searchFilterCourseHeight = windowHeight / 20;
	public static final int searchCourseX = windowWidth / 2 + midSearchFilter;
	public static final int searchFilterCourseY = tableCourseY / 2 - searchFilterCourseHeight / 2;
	public static final Rectangle searchCourseBounds = new Rectangle(searchCourseX, searchFilterCourseY, searchFilterCourseWidth, searchFilterCourseHeight);
	public static final float fontSearchSize = (int) (searchFilterCourseHeight * 0.6f);

	public static final int filterCourseX = windowWidth / 2 - searchFilterCourseWidth - midSearchFilter;
	public static final Rectangle filterCourseBounds = new Rectangle(filterCourseX, searchFilterCourseY, searchFilterCourseWidth, searchFilterCourseHeight);
	public static final float fontFilterSize = (int) (searchFilterCourseHeight * 0.6f);

	public static final int labelHeight = windowHeight / 20;
	public static final float fontLabelSize = (int) (labelHeight * 0.6f);

	public static final Rectangle labelFilterBounds = new Rectangle(0, searchFilterCourseY, filterCourseX, labelHeight);
	public static final Rectangle labelSearchBounds = new Rectangle(filterCourseX + searchFilterCourseWidth, searchFilterCourseY, searchCourseX - filterCourseX - searchFilterCourseWidth, labelHeight);

	public static final int buttonWidth = windowWidth / 15;
	public static final int buttonHeight = windowHeight / 20;
	public static final float fontButtonSize = (int) (buttonHeight * 0.6f);

	public static final int midButtonCourse = windowWidth / 40;
	public static final int buttonAddX = windowWidth / 2 - buttonWidth - midButtonCourse;
	public static final int buttonViewX = windowWidth / 2 + midButtonCourse;
	public static final int buttonAddViewY = windowHeight * 179 / 200;
	public static final Rectangle buttonAddBounds = new Rectangle(buttonAddX, buttonAddViewY, buttonWidth, buttonHeight);
	public static final Rectangle buttonViewBounds = new Rectangle(buttonViewX, buttonAddViewY, buttonWidth, buttonHeight);

	// MenuWindow

	public static final int menuBarHeight = windowHeight / 20;
	public static final Rectangle menuBarBounds = new Rectangle(0, 0, windowWidth, menuBarHeight);
	public static final float fontMenuSize = (int) (menuBarHeight * 0.6f);
	public static final int lineThickness = windowWidth / 500;

	public static final int panelWidth = (int) (windowWidth * 0.3332);
	public static final int panelHeight = (int) (windowHeight * 0.915);

	public static final Rectangle textInfoBounds = new Rectangle(panelWidth, menuBarHeight, panelWidth, panelHeight);
	public static final float fontTextSize = (int) fontMenuSize;

	public static final Rectangle tableStudentBounds = new Rectangle(0, menuBarHeight, panelWidth, panelHeight);
	public static final Rectangle tableCategoryBounds = new Rectangle(2 * panelWidth, menuBarHeight, panelWidth, panelHeight / 2);
	public static final Rectangle tableItemBounds = new Rectangle(2 * panelWidth, menuBarHeight + panelHeight / 2, panelWidth, panelHeight / 2);
	public static final int[] tableCategoryItemColumnWidth = {panelWidth * 3 / 4, panelWidth / 4};
	public static final int menuTableRowHeight = (int) (panelHeight * 0.051);
	public static final float fontMenuTableSize = (int) (menuTableRowHeight * 0.6f);

	public static final Dimension optionPaneDimension = new Dimension(getScaledSize(600), getScaledSize(600));

	public static int getScaledSize(double size) {
		return (int) (size / 2000 * Math.max(screenSize.getWidth(), screenSize.getHeight()));
	}
}