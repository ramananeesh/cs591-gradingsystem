package view;

import helper.SizeManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

	/** The background image file */
	private static final String BACKGROUND_PICTURE_FILE_NAME = "src/resource/background.jpg";

	/**
	 * Initializes a newly created {@code MainFrame} object
	 */
	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setContentPane(new JLabel(new ImageIcon(BACKGROUND_PICTURE_FILE_NAME)));
		setBounds(SizeManager.windowBounds);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//			System.setProperty("awt.useSystemAAFontSettings","on");
//			System.setProperty("swing.aatext", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(new CoursePanel(this));
		setVisible(true);
	}

	/**
	 * Change JPanel contained in this JFrame from one to another.
	 *
	 * @param from the JPanel that will be removed
	 * @param to   the JPanel that will be added
	 */
	public void changePanel(JPanel from, JPanel to) {
		changePanel(from, to, AnimationType.NONLINEAR_SLIDE);
	}

	/**
	 * Change JPanel contained in this JFrame from one to another with specified animation
	 *
	 * @param from          the JPanel that will be removed
	 * @param to            the JPanel that will be added
	 * @param animationType type of animation
	 */
	public void changePanel(JPanel from, JPanel to, AnimationType animationType) {
		switch (animationType) {
			case NONE:
				changePanelWithoutAnimation(from, to);
				break;
			case NONLINEAR_SLIDE:
				changePanelWithNonlinearSlideAnimation(from, to);
				break;
			case LINEAR_SLIDE:
				changePanelWithLinearSlideAnimation(from, to);
				break;
			case SCALE:
				changePanelWithScaleAnimation(from, to);
				break;
		}
	}

	/**
	 * Change JPanel contained in this JFrame from one to another without animation
	 *
	 * @param from the JPanel that will be removed
	 * @param to   the JPanel that will be added
	 */
	private void changePanelWithoutAnimation(JPanel from, JPanel to) {
		remove(from);
		add(to);
		revalidate();
		repaint();
	}

	/**
	 * Change JPanel contained in this JFrame from one to another with slide animation
	 *
	 * @param from the JPanel that will be removed
	 * @param to   the JPanel that will be added
	 */
	private void changePanelWithSlideAnimation(JPanel from, JPanel to, double exponent) {
		new Thread(() -> { // coefficient * pow(time - i, exponent) = height
			int time = 60;
			int distance = getHeight();
			double coefficient = distance / Math.pow(time, exponent);
			add(to);
			for (int i = 0; i <= time; ++i) {
				int height = (int) (coefficient * Math.pow(time - i, exponent)) - distance;
				from.setLocation(0, height);
				to.setLocation(0, from.getHeight() + height);
				try {
					Thread.sleep(16);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			to.setLocation(0, 0);
			remove(from);
			revalidate();
			repaint();
		}).start();
	}

	/**
	 * Change JPanel contained in this JFrame from one to another with nonlinear animation
	 *
	 * @param from the JPanel that will be removed
	 * @param to   the JPanel that will be added
	 */
	private void changePanelWithNonlinearSlideAnimation(JPanel from, JPanel to) {
		changePanelWithSlideAnimation(from, to, 7);
	}

	private void changePanelWithLinearSlideAnimation(JPanel from, JPanel to) {
		changePanelWithSlideAnimation(from, to, 1);
	}

	private void changePanelWithScaleAnimation(JPanel from, JPanel to) {
		new Thread(() -> {
			Rectangle fromBounds = from.getBounds();
			Rectangle toBounds = to.getBounds();
			int count = 60;
			for (int i = 1; i <= count; ++i) {
				int newFromWidth = (int) (fromBounds.getWidth() * (count - i) / count);
				int newFromHeight = (int) (fromBounds.getWidth() * (count - i) / count);
				from.setBounds(getWidth() / 2 - newFromWidth / 2, getHeight() / 2 - newFromHeight / 2, newFromWidth, newFromHeight);
				try {
					Thread.sleep(16);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			remove(from);
			add(to);
			for (int i = 1; i <= count; ++i) {
				int newToWidth = (int) (toBounds.getWidth() * i / count);
				int newToHeight = (int) (toBounds.getHeight() * i / count);
				to.setBounds(getWidth() / 2 - newToWidth / 2, getHeight() / 2 - newToHeight / 2, newToWidth, newToHeight);
				try {
					Thread.sleep(16);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			to.setBounds(toBounds);
			revalidate();
			repaint();
		}).start();
	}

	public enum AnimationType {
		NONE, NONLINEAR_SLIDE, LINEAR_SLIDE, SCALE
	}

	public enum AnimationDirection {
		UP_TO_DOWN(0, +1),
		DOWN_TO_UP(0, -1),
		LEFT_TO_RIGHT(+1, 0),
		RIGHT_TO_LEFT(-1, 0);
		int x, y;

		AnimationDirection(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
