import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class HelloDropBall extends Application {
	Stage stage;
	Random rnd = new Random();
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		javafx.scene.canvas.Canvas canvas = new Canvas(1920, 1080);
		FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
		draw(g2d);
		primaryStage.setScene(new Scene(new Group(canvas)));
		primaryStage.setTitle("Hello Ball");
		primaryStage.show();

		new AnimationTimer() {
			long last = -1;
			@Override
			public void handle(long now) {
				if(last == -1)
					last = now;
				update((now - last) / 1.0e9);
				last = now;
				draw(g2d);
			}
		}.start();



		canvas.setOnMouseDragEntered(e -> {
			startDrag = new Point2D.Double(e.getX(), e.getY());
		});

		canvas.setOnMouseDragExited(e -> {
			//balls.add(new Ball(new Point2D.Double(e.getX(), e.getY()),
			//		  new Point2D.Double(10,2), 10));
		});

		canvas.setOnMouseClicked(e -> {
			balls.add(new Ball(new Point2D.Double(e.getX(), e.getY()),
					new Point2D.Double(50-rnd.nextInt(100),50- rnd.nextInt(100)), 0));
		});
	}

	ArrayList<Ball> balls = new ArrayList<>();
	private Point2D startDrag;
	private Point2D endDrag;


	public void update(double deltaTime) {
		for(Ball ball : balls)
			ball.update(new Point2D.Double(this.stage.getWidth(), this.stage.getHeight()));
	}



	public void draw(FXGraphics2D g2d) {
		g2d.setColor(Color.white);
		g2d.clearRect(0,0, 1920, 1080);
		for(Ball ball : balls)
			ball.draw(g2d);

	}
}
