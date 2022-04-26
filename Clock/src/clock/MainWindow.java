package clock;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;

public class MainWindow {
	private JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
					GraphicsDevice gd = ge.getDefaultScreenDevice();
					boolean isPerPixelTranslucencySupported = gd
							.isWindowTranslucencySupported(WindowTranslucency.PERPIXEL_TRANSLUCENT);
					if (isPerPixelTranslucencySupported) {
						MainWindow window = new MainWindow();
						window.frame.pack();
						window.frame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainWindow() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setType(JFrame.Type.UTILITY);
		frame.setUndecorated(true);
		frame.setBackground(new Color(0, 0, 0, 0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(450, 150);
		frame.setAlwaysOnTop(true);
		ClockPanel clockPanel = new ClockPanel(250, 250);
		clockPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent me) {
				if (me.isPopupTrigger())
					System.exit(0);
			}
		});
		clockPanel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent me) {
				Point curPoint = me.getLocationOnScreen();
				frame.setLocation(curPoint.x-125, curPoint.y-125);
			}
		});
		frame.setContentPane(clockPanel);
		clockPanel.start();
	}
}