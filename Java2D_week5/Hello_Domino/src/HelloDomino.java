import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
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
public class HelloDomino extends Application implements Resizable {

	private Camera camera;
	private World world;
	private ResizableCanvas canvas;

	@Override
	public void start(Stage stage) {
		BorderPane borderPane = new BorderPane();

		canvas = new ResizableCanvas(e -> draw(e), borderPane);
		FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

		borderPane.setCenter(canvas);

		camera = new Camera(canvas, this, g2d);

		stage.setScene(new Scene(borderPane, 1920, 1080));
		stage.setTitle("Hello Domino");
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

	public void init() {
		world = new World();
		world.setGravity(new Vector2(0,-9.8));

		Body floor = new Body();
		floor.addFixture(Geometry.createRectangle(20, 1));
		floor.getTransform().setTranslation(0, -.5);
		floor.setMass(MassType.INFINITE);
		world.addBody(floor);

		for(int y = 0; y < 10; y++) {
			for (int x = 0; x < 10-y; x++) {
				Body box = new Body();
				box.addFixture(Geometry.createRectangle(.25, .25));
				box.setMass(MassType.NORMAL);
				box.getTransform().setTranslation(5 + x * 0.25 + 0.125 * y, y*0.25);
				world.addBody(box);

			}
		}

		Body ball = new Body();
		ball.addFixture(Geometry.createCircle(1));
		ball.getTransform().setTranslation(new Vector2(0,2));
		ball.setMass(MassType.NORMAL);
		ball.getFixture(0).setRestitution(.250);
		ball.applyImpulse(-20);
		world.addBody(ball);

	}


	public void update(double deltaTime) {
		world.update(deltaTime);
	}

	public void draw(FXGraphics2D g2d) {
		g2d.setTransform(new AffineTransform());
		g2d.clearRect(0,0, 1920, 1080);
		g2d.setColor(Color.white);

		g2d.setTransform(camera.getTransform((int)canvas.getWidth(), (int)canvas.getHeight()));
		g2d.scale(1,-1);

		DebugDraw.draw(g2d, world, 100);
	}

	public static void main(String[] args) {
		Application.launch(HelloDomino.class);
	}


}
