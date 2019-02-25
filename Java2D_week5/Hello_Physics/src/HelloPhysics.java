import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.*;
import org.dyn4j.geometry.Rectangle;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.Resizable;
import org.jfree.fx.ResizableCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

/**
 * Created by johan on 2017-03-08.
 */
public class HelloPhysics extends Application implements Resizable {

	private World world;
	private ResizableCanvas canvas;

	public void init() {
		world = new World();
		world.setGravity(new Vector2(0,-9.8));

		Body floor = new Body();
		floor.addFixture(Geometry.createRectangle(10, 1));
		floor.setMass(MassType.INFINITE);
		floor.getTransform().setRotation(Math.toRadians(10.0));
		world.addBody(floor);

		Body ball = new Body();

		BodyFixture fixture = new BodyFixture(Geometry.createCircle(1));
		fixture.setRestitution(.25);
		ball.addFixture(fixture);
		ball.getTransform().setTranslation(new Vector2(0,10));
		ball.setMass(MassType.NORMAL);
//		ball.getFixture(0).setRestitution(.25);
		world.addBody(ball);

	}

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane borderPane = new BorderPane();

		canvas = new ResizableCanvas(e -> draw(e), borderPane);
		FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

		borderPane.setCenter(canvas);

		stage.setScene(new Scene(borderPane,1920, 1080));
		stage.setTitle("Hello Physics");
		stage.show();

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

	}


	public void update(double deltaTime) {
		world.update(deltaTime);
	}

	public void draw(FXGraphics2D g2d) {
		g2d.setTransform(new AffineTransform());
		g2d.setColor(Color.white);
		g2d.clearRect(0,0, 1920, 1080);

		g2d.translate(canvas.getWidth()/2, canvas.getHeight()/2);
		g2d.scale(1,-1);

		DebugDraw.draw(g2d, world, 50);
	}

	public static void main(String[] args) {
		Application.launch(HelloPhysics.class);
	}


}
