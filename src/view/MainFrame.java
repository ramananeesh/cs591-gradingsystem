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
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(new CoursePanel(this));
		setVisible(true);
	}

	private static enum Animation {
		NONE, SLIDE, SCALE;
	}

	/**
	 * Switch JPanel contained in this JFrame from one to another.
	 *
	 * @param from the JPanel that will be removed
	 * @param to   the JPanel that will be added
	 */
	public void switchPanel(JPanel from, JPanel to) {
		switchPanelWithSlideAnimation(from, to);
	}

	private void switchPanelWithoutAnimation(JPanel from, JPanel to) {
		remove(from);
		add(to);
		revalidate();
		repaint();
	}

	private void switchPanelWithSlideAnimation(JPanel from, JPanel to) {
		new Thread(() -> {
			Rectangle toBounds = to.getBounds();
			int count = 15;
			int delta = getHeight() / count;
			add(to);
			for (int i = 1; i <= count; ++i) {
				from.setLocation(0, -delta * i);
				to.setLocation(0, from.getHeight() - delta * i);
				try {
					Thread.sleep(16);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			to.setBounds(toBounds);
			remove(from);
			revalidate();
			repaint();
		}).start();
	}

	private void switchPanelWithScaleAnimation(JPanel from, JPanel to) {
		new Thread(() -> {
			Rectangle fromBounds = from.getBounds();
			Rectangle toBounds = to.getBounds();
			int count = 15;
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
}
