import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.dynamics.joint.RevoluteJoint;
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
import java.util.ArrayList;

/**
 * Created by johan on 2017-03-08.
 */
public class HelloBasketball extends Application implements Resizable {

	private Camera camera;
	private World world;
	private MousePicker mousePicker;
	private ResizableCanvas canvas;
	private boolean debugSelected = true;
	private ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();

	public void init() {
		world = new World();
		//world.setGravity(new Vector2(0,-9.8));
		world.setGravity(new Vector2(0,-9.8/4));

		Body floor = new Body();
		floor.addFixture(Geometry.createRectangle(20, 1));
		floor.getTransform().setTranslation(0, -0.5);
		floor.setMass(MassType.INFINITE);
		world.addBody(floor);
		gameObjects.add(new GameObject("/images/floor.png", floor, new Vector2(0,0), 1));

		Body wall1 = new Body();
		wall1.addFixture(Geometry.createRectangle(0.15, 10));
		wall1.getTransform().setTranslation(10,5);
		wall1.setMass(MassType.INFINITE);
		world.addBody(wall1);
		gameObjects.add(new GameObject("/images/wall.png", wall1, new Vector2(0,0), 1));

		Body wall2 = new Body();
		wall2.addFixture(Geometry.createRectangle(0.15, 10));
		wall2.getTransform().setTranslation(-10,5);
		wall2.setMass(MassType.INFINITE);
		world.addBody(wall2);
		gameObjects.add(new GameObject("/images/wall.png", wall2, new Vector2(0,0), 1));

		Body pole = new Body();
		pole.addFixture(Geometry.createRectangle(0.1, 2.5));
		pole.getTransform().setTranslation(4,1.25);
		pole.setMass(MassType.INFINITE);
		world.addBody(pole);
		gameObjects.add(new GameObject("/images/basket.png", pole, new Vector2(15,0), 1));

		Body net = new Body();
		net.addFixture(Geometry.createRectangle(0.02, 0.2));
		net.getTransform().setTranslation(3.6,2.0);
		net.setMass(MassType.INFINITE);
		world.addBody(net);

		Body ball = new Body();
		ball.addFixture(Geometry.createCircle(0.15));
		ball.getTransform().setTranslation(0,2.4);
		ball.setMass(MassType.NORMAL);
		ball.getFixture(0).setRestitution(0.75);
		world.addBody(ball);
		gameObjects.add(new GameObject("/images/basketball.png", ball, new Vector2(0,0), 0.05));


	}

	@Override
	public void start(Stage stage) throws Exception {

		BorderPane borderPane = new BorderPane();

		javafx.scene.control.CheckBox showDebug = new CheckBox("Show debug");
		showDebug.setOnAction(e-> {
			debugSelected = showDebug.isSelected();
		});
		showDebug.setSelected(true);
		canvas = new ResizableCanvas(e -> draw(e), borderPane);
		FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

		borderPane.setTop(showDebug);
		borderPane.setCenter(canvas);

		camera = new Camera(canvas, this, g2d);
		mousePicker = new MousePicker(canvas);

		stage.setScene(new Scene(borderPane, 1920,1080));
		stage.setTitle("Hello Basketball");
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
		mousePicker.update(world, camera.getTransform((int)canvas.getWidth(), (int)canvas.getHeight()), 100);

		world.update(deltaTime);
	}

	public void draw(FXGraphics2D g2d) {
		g2d.setTransform(new AffineTransform());
		g2d.setColor(Color.white);
		g2d.clearRect(0,0, 1920, 1080);

		AffineTransform originalTransform = g2d.getTransform();

		g2d.setTransform(camera.getTransform((int)canvas.getWidth(), (int)canvas.getHeight()));
		g2d.scale(1,-1);


		for(GameObject go : gameObjects) {
			go.draw(g2d);
		}

		if(debugSelected) {
			g2d.setColor(Color.blue);
			DebugDraw.draw(g2d, world, 100);
		}
		g2d.setTransform(originalTransform);
	}


	public static void main(String[] args) {
		Application.launch(HelloBasketball.class);
	}

}
