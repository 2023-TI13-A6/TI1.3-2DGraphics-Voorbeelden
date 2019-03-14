import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FlappyBird extends Application {

	private ResizableCanvas canvas;
	private BufferedImage pipeDown;
	private BufferedImage pipeUp;
	private BufferedImage background;
	private BufferedImage bird;

	private double backgroundScroll = 0;
	private double birdHeight = 100;
	private double birdSpeed = 0;
	private double pipeCount = 10;
	private double pipeGap = 200;
	private int score = 0;
	private boolean pause = true;

	private ArrayList<Point2D> pipes = new ArrayList<>();


	@Override
	public void start(Stage stage) {

		BorderPane mainPane = new BorderPane();
		canvas = new ResizableCanvas(g -> draw(g), mainPane);
		mainPane.setCenter(canvas);
		FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
		new AnimationTimer() {
			long last = -1;

			@Override
			public void handle(long now) {
				if (last == -1)
					last = now;
				update((now - last) / 1000000000.0);
				last = now;
				draw(g2d);
			}
		}.start();

		// Mouse Events
		canvas.setOnMouseClicked(e -> mouseClicked(e));
		canvas.setOnKeyPressed(e -> keyTyped(e));
		canvas.setFocusTraversable(true); // make sure we have focus for key events

		stage.setScene(new Scene(mainPane, 1000, 1000));
		stage.setTitle("Flappy Bird");
		stage.show();
		draw(g2d);
	}


	public void init() {
		// Get the pictures
		BufferedImage total = null;
		try {
			total = ImageIO.read(getClass().getResource("/spritesheet.png"));
			background = total.getSubimage(0, 0, 144, 256);
			bird = total.getSubimage(3, 491, 17, 12);
			pipeUp = total.getSubimage(56, 323, 26, 160);
			pipeDown = total.getSubimage(84, 323, 26, 160);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*for (int i = 200; i < 1920; i+= 100) {
			pipes.add(new Point2D.Double(i, 0));
		}*/
	}


	public void draw(FXGraphics2D g2d) {

		// Clear drawing
		g2d.setTransform(new AffineTransform());
		g2d.setBackground(Color.white);
		g2d.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

		g2d.setPaint(new TexturePaint(background, new Rectangle2D.Double(-backgroundScroll, 0, canvas.getWidth(), canvas.getHeight())));
		g2d.fill(new Rectangle2D.Double(0,0,canvas.getWidth(), canvas.getHeight()));

		AffineTransform birdTransform = new AffineTransform();
		birdTransform.translate(50, birdHeight);
		birdTransform.scale(2,2);
		birdTransform.rotate(birdSpeed/15);
		g2d.drawImage(bird, birdTransform, null);

		// Draw pipes
		for (Point2D pipe : pipes) {
			AffineTransform af = AffineTransform.getTranslateInstance(pipe.getX(), pipe.getY()-pipeUp.getHeight()*2-pipeGap/2);
			af.scale(2,2);
			g2d.drawImage(pipeUp, af, null);

			af = AffineTransform.getTranslateInstance(pipe.getX(), pipe.getY()+pipeGap/2);
			af.scale(2,2);
			g2d.drawImage(pipeDown, af, null);


		}

		// Score
		g2d.setColor(Color.red);
		g2d.drawString("Score: " + score, 50,50);
	}

	private void update(double deltaTime) {

		if (pause)
			return;


		backgroundScroll += 240*deltaTime;

		birdHeight += birdSpeed;
		birdSpeed += 10*deltaTime;

		if (birdHeight > canvas.getHeight())
			birdHeight = canvas.getHeight();
		else if (birdHeight < 0)
			birdHeight = 0;

		pipeCount--;
		if (pipeCount < 0) {
			pipes.add(new Point2D.Double(canvas.getWidth(), pipeUp.getHeight()*2 + 50 - Math.random()*200 + pipeGap/2));
			pipeCount = 50+Math.random()*100;
		}

		// Pipe stuff
		Iterator<Point2D> iterator = pipes.iterator();

		while (iterator.hasNext()) {
			Point2D pipe = iterator.next();

			pipe.setLocation(pipe.getX() - 240*deltaTime, pipe.getY());

			if (pipe.getX() < 50 + pipeUp.getWidth()*2 && pipe.getY() > 50-pipeUp.getWidth()*2) {
				if (birdHeight < pipe.getY()  - pipeGap/2 || birdHeight > pipe.getY()+pipeGap/2) {
					reset();
					break;
				}
			}

			if (pipe.getX() < -pipeUp.getWidth()*2) {
				iterator.remove();
				score++;
			}
		}
	}

	private void reset() {
		pause = true;
		birdHeight = 100;
		birdSpeed = 0;
		pipeCount = 10;
		pipes.clear();
		score = 0;
	}


	public void mouseClicked(MouseEvent e) {

	}

	private void keyTyped(KeyEvent e) {
		pause = false;
		birdSpeed -= 7.5;
	}


	public static void main(String[] args) {
		Application.launch(FlappyBird.class);
	}

}
