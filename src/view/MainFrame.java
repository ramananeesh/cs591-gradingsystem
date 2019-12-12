package view;

import helper.SizeManager;

import javax.swing.*;

import controller.Master;

import java.awt.*;

/**
 * The {@code LoginFrame} class represents the main frame which contains many kinds of panels
 */
public class MainFrame extends JFrame {

	/** The background image file */
	private static final String BACKGROUND_PICTURE_FILE_NAME = "src/resource/background.jpg";

	/** The total frames of animation */
	private static final int ANIMATION_FRAMES = 60;

	/** The total time (milliseconds) of animation */
	private static final int ANIMATION_TIME = 1000;

	/** The interval (milliseconds) of animation. */
	private static final int ANIMATION_INTERVAL = ANIMATION_TIME / ANIMATION_FRAMES;


	/**
	 * Initializes a newly created {@code MainFrame} object
	 */
	public MainFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setContentPane(new JLabel(new ImageIcon(BACKGROUND_PICTURE_FILE_NAME)));
		setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setVisible(true);
		SizeManager.update(this);
		add(new CoursePanel(this, new Master()));
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
			int time = ANIMATION_FRAMES; // the total frames of the animation
			int distance = getContentPane().getHeight();
			double coefficient = distance / Math.pow(time, exponent);
			add(to);
			for (int i = 0; i <= time; ++i) {
				int height = (int) (coefficient * Math.pow(time - i, exponent)) - distance;
				from.setLocation(0, height);
				to.setLocation(0, from.getHeight() + height);
				try {
					Thread.sleep(ANIMATION_INTERVAL);
				} catch (InterruptedException e) {
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
		changePanelWithSlideAnimation(from, to, 7); // When set exponent as a number not equal to 1, the slide animation is nonlinear. It looks good when being set as 7.
	}

	/**
	 * Change JPanel contained in this JFrame from one to another with nonlinear animation
	 *
	 * @param from the JPanel that will be removed
	 * @param to   the JPanel that will be added
	 */
	private void changePanelWithLinearSlideAnimation(JPanel from, JPanel to) {
		changePanelWithSlideAnimation(from, to, 1); // when set exponent as 1, the slide animation is linear
	}

	/** Types of animation */
	public enum AnimationType {
		NONE, NONLINEAR_SLIDE, LINEAR_SLIDE
	}

}
