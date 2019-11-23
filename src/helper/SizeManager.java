package helper;

import javax.swing.*;
import java.awt.*;

public class SizeManager {

	public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	// LoginWindow

	public static final int loginWindowWidth = getScaledSize(800);
	public static final int loginWindowHeight = getScaledSize(450);
	public static final int loginWindowX = (int) ((screenSize.getWidth() - loginWindowWidth) / 2);
	public static final int loginWindowY = (int) ((screenSize.getHeight() - loginWindowHeight) / 2);
	public static final Rectangle loginWindowBounds = new Rectangle(loginWindowX, loginWindowY, loginWindowWidth, loginWindowHeight);

	public static final Rectangle titleBounds = new Rectangle(0, 0, loginWindowWidth, loginWindowHeight / 4);
	public static final float titleFontSize = titleBounds.height * 0.6f;

	public static final int buttonLoginWidth = loginWindowWidth / 3;
	public static final int buttonLoginHeight = loginWindowHeight / 6;
	public static final int buttonLoginX = loginWindowWidth / 2 - buttonLoginWidth / 2;
	public static final int buttonLoginY = loginWindowHeight * 2 / 3 - buttonLoginHeight / 2;
	public static final Rectangle buttonLoginBounds = new Rectangle(buttonLoginX, buttonLoginY, buttonLoginWidth, buttonLoginHeight);
	public static final float buttonLoginFontSize = buttonLoginHeight * 0.6f;

	// Course Window

	public static int windowWidth = getScaledSize(1900);
	public static int windowHeight = getScaledSize(1000);
	public static int windowX = (int) ((screenSize.getWidth() - windowWidth) / 2);
	public static int windowY = (int) ((screenSize.getHeight() - windowHeight) / 2);
	public static Rectangle windowBounds = new Rectangle(windowX, windowY, windowWidth, windowHeight);


	public static int tableRowHeight = windowHeight / 20;
	public static final float fontTableSize = tableRowHeight * 0.6f;

	public static int tableCourseWidth = windowWidth * 7 / 8;
	public static int tableCourseHeight = tableRowHeight * 15;
	public static int tableCourseX = windowWidth / 2 - tableCourseWidth / 2;
	public static int tableCourseY = windowHeight / 2 - tableCourseHeight / 2;
	public static Rectangle tableCourseBounds = new Rectangle(tableCourseX, tableCourseY, tableCourseWidth, tableCourseHeight);
	public static int tableCourseColumnNumberWidth = tableCourseWidth / 4;
	public static int tableCourseColumnNameWidth = tableCourseWidth / 2;
	public static int tableCourseColumnSemesterWidth = tableCourseWidth / 4;

	public static int midSearchFilter = windowWidth / 15;

	public static int searchFilterCourseWidth = windowWidth / 6;
	public static int searchFilterCourseHeight = windowHeight / 20;
	public static int searchCourseX = windowWidth / 2 + midSearchFilter;
	public static int searchFilterCourseY = tableCourseY / 2 - searchFilterCourseHeight / 2;
	public static Rectangle searchCourseBounds = new Rectangle(searchCourseX, searchFilterCourseY, searchFilterCourseWidth, searchFilterCourseHeight);
	public static float fontSearchSize = searchFilterCourseHeight * 0.6f;

	public static int filterCourseX = windowWidth / 2 - searchFilterCourseWidth - midSearchFilter;
	public static Rectangle filterCourseBounds = new Rectangle(filterCourseX, searchFilterCourseY, searchFilterCourseWidth, searchFilterCourseHeight);
	public static float fontFilterSize = searchFilterCourseHeight * 0.6f;

	public static int labelHeight = windowHeight / 20;
	public static float fontLabelSize = labelHeight * 0.6f;

	public static Rectangle labelFilterBounds = new Rectangle(0, searchFilterCourseY, filterCourseX, labelHeight);
	public static Rectangle labelSearchBounds = new Rectangle(filterCourseX + searchFilterCourseWidth, searchFilterCourseY, searchCourseX - filterCourseX - searchFilterCourseWidth, labelHeight);

	public static int buttonWidth = windowWidth / 15;
	public static int buttonHeight = windowHeight / 20;
	public static float fontButtonSize = buttonHeight * 0.6f;

	public static int midButtonCourse = windowWidth / 40;
	public static int buttonAddX = windowWidth / 2 - buttonWidth - midButtonCourse;
	public static int buttonViewX = windowWidth / 2 + midButtonCourse;
	public static int buttonAddViewY = windowHeight * 179 / 200;
	public static Rectangle buttonAddBounds = new Rectangle(buttonAddX, buttonAddViewY, buttonWidth, buttonHeight);
	public static Rectangle buttonViewBounds = new Rectangle(buttonViewX, buttonAddViewY, buttonWidth, buttonHeight);

	// Menu Window

	public static final int menuBarHeight = windowHeight / 20;
	public static Rectangle menuBarBounds = new Rectangle(0, 0, windowWidth, menuBarHeight);
	public static final float fontMenuSize = menuBarHeight * 0.6f;
	public static final int lineThickness = windowWidth / 500;

	public static int panelWidth = (int) (windowWidth * 0.3332);
	public static int panelHeight = (int) (windowHeight * 0.915);

	public static Rectangle textInfoBounds = new Rectangle(panelWidth, menuBarHeight, panelWidth, panelHeight);
	public static float fontTextSize = fontMenuSize;

	public static Rectangle tableStudentBounds = new Rectangle(0, menuBarHeight, panelWidth, panelHeight);
	public static Rectangle tableCategoryBounds = new Rectangle(2 * panelWidth, menuBarHeight, panelWidth, panelHeight / 2);
	public static Rectangle tableItemBounds = new Rectangle(2 * panelWidth, menuBarHeight + panelHeight / 2, panelWidth, panelHeight / 2);
	public static int menuTableRowHeight = (int)(panelHeight *0.051);
	public static float fontMenuTableSize = menuTableRowHeight * 0.6f;

	public static int getScaledSize(double size) {
		return (int) (size / 2000 * Math.max(screenSize.getWidth(), screenSize.getHeight()));
	}
}
