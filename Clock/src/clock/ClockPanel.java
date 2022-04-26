package clock;

import java.awt.*;
import java.awt.geom.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;

public class ClockPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private volatile boolean start;
	private LocalDateTime dateTime;

	public ClockPanel(int width, int height) {
		setPreferredSize(new Dimension(width, height));
		setOpaque(false);
		dateTime = LocalDateTime.now();
	}

	public void start() {
		this.start = true;
		new Thread(this).start();
	}

	public void run() {
		int prevSecond = -1;
		while (start) {
			dateTime = LocalDateTime.now();
			int second = dateTime.getSecond();
			if (second != prevSecond) {
				prevSecond = second;
				repaint();
			}
			try {
				TimeUnit.MILLISECONDS.sleep(200);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintClock(g2, dateTime);
	}

	private void paintClock(Graphics2D g2, LocalDateTime dateTime) {
		float lineWidth = 3.0f;
		Stroke line = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		Font font = new Font("Tahoma", Font.PLAIN, getHeight() / 10);
		Color defColour;
		if (dateTime.getHour() <= 18 && dateTime.getHour() >= 6) {
			defColour = new Color(200, 200, 255);
		} else {
			defColour = new Color(173, 110, 7);
		}
		g2.setColor(defColour);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.65f));
		g2.setStroke(line);
		g2.setFont(font);
		Point2D origin = new Point2D.Double(getWidth() / 2.0, getHeight() / 2.0);
		String time = dtFormatter.format(dateTime);
		Rectangle2D timeBounds = font.getStringBounds(time, g2.getFontRenderContext());
		g2.drawString(time, Math.round(origin.getX() - timeBounds.getWidth() / 2.0),
				Math.round(origin.getY() + getHeight() / 5.0));
		double radius = getWidth() / 2.0 - 2.0 * lineWidth;

		Point2D secondPoint = getHandPoint(dateTime.getSecond(), origin, 0.95 * radius);
		Point2D minutePoint = getHandPoint(dateTime.getMinute(), origin, 0.85 * radius);
		Point2D hourPoint = getHandPoint(dateTime.getHour(), origin, 0.65 * radius);
		g2.setColor(new Color(255, 0, 0));
		g2.draw(new Line2D.Double(origin, secondPoint));
		g2.setColor(new Color(0, 255, 0));
		g2.draw(new Line2D.Double(origin, minutePoint));
		g2.setColor(new Color(0, 0, 255));
		g2.draw(new Line2D.Double(origin, hourPoint));
		g2.setColor(defColour);
		for (int i = 1; i <= 12; i++) {
			Rectangle2D hourBounds = font.getStringBounds(String.valueOf(i), g2.getFontRenderContext());//
			Point2D hourLabel = getHandPoint(i * 5, origin, 0.8 * radius);
			g2.drawString(String.valueOf(i), Math.round(hourLabel.getX() - hourBounds.getWidth() / 2),
					Math.round(hourLabel.getY() + hourBounds.getHeight() / 2));
		}
// rysowanie kolka na srodku zegara
		double innerRadius = radius / 10;
		g2.fill(new Ellipse2D.Double(origin.getX() - innerRadius / 2.0, origin.getY() - innerRadius / 2.0, 
				innerRadius, innerRadius));
		// TODO rysowanie obrêczy
		g2.draw(new Ellipse2D.Double(origin.getX() - radius, origin.getY() - radius, radius * 2, radius * 2));// ,15,15));
	}

	private Point2D getHandPoint(double handValue, Point2D origin, double radius) {
		double angle = 2.0 * Math.PI / 60.0 * handValue - Math.PI / 2.0;
		return getTickPoint(angle, origin, radius);
	}

	private Point2D getTickPoint(double angle, Point2D origin, double radius) {
		double x = origin.getX() + radius * Math.cos(angle);
		double y = origin.getY() + radius * Math.sin(angle);
		return new Point2D.Double(x, y);
	}
}