import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
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

/**
 * Created by johan on 2017-03-08.
 */
public class HelloJoints extends Application implements Resizable {

	private Camera camera;
	private World world;
	private MousePicker mousePicker;
	private ResizableCanvas canvas;

	@Override
	public void start(Stage stage) throws Exception {
		BorderPane borderPane = new BorderPane();

		canvas = new ResizableCanvas(e -> draw(e), borderPane);
		FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());

		borderPane.setCenter(canvas);

		camera = new Camera(canvas, this, g2d);
		mousePicker = new MousePicker(canvas);

		stage.setScene(new Scene(borderPane,1920, 1080));
		stage.setTitle("Hello Joints");
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
		floor.addFixture(new Rectangle(20, 1));
		floor.getTransform().setTranslation(0, -3.5);
		floor.setMass(MassType.INFINITE);
		world.addBody(floor);


		// the ragdoll

		// Head
		Body head = new Body();
		{// Fixture2
			Convex c = Geometry.createCircle(0.25);
			BodyFixture bf = new BodyFixture(c);
			head.addFixture(bf);
		}
		head.setMass(MassType.NORMAL);
		world.addBody(head);

		// Torso
		Body torso = new Body();
		{// Fixture4
			Convex c = Geometry.createRectangle(0.5, 1.0);
			BodyFixture bf = new BodyFixture(c);
			torso.addFixture(bf);
		}
		{// Fixture16
			Convex c = Geometry.createRectangle(1.0, 0.25);
			c.translate(new Vector2(0.00390625, 0.375));
			BodyFixture bf = new BodyFixture(c);
			torso.addFixture(bf);
		}
		torso.translate(new Vector2(0.0234375, -0.8125));
		torso.setMass(MassType.NORMAL);
		world.addBody(torso);

		// Right Humerus
		Body rightHumerus = new Body();
		{// Fixture5
			Convex c = Geometry.createRectangle(0.25, 0.5);
			BodyFixture bf = new BodyFixture(c);
			rightHumerus.addFixture(bf);
		}
		rightHumerus.translate(new Vector2(0.4375, -0.609375));
		rightHumerus.setMass(MassType.NORMAL);
		world.addBody(rightHumerus);

		// Right Ulna
		Body rightUlna = new Body();
		{// Fixture6
			Convex c = Geometry.createRectangle(0.25, 0.4);
			BodyFixture bf = new BodyFixture(c);
			rightUlna.addFixture(bf);
		}
		rightUlna.translate(new Vector2(0.44140625, -0.98828125));
		rightUlna.setMass(MassType.NORMAL);
		world.addBody(rightUlna);

		// Neck
		Body neck = new Body();
		{// Fixture7
			Convex c = Geometry.createRectangle(0.15, 0.2);
			BodyFixture bf = new BodyFixture(c);
			neck.addFixture(bf);
		}
		neck.translate(new Vector2(0.015625, -0.2734375));
		neck.setMass(MassType.NORMAL);
		world.addBody(neck);

		// Left Humerus
		Body leftHumerus = new Body();
		{// Fixture9
			Convex c = Geometry.createRectangle(0.25, 0.5);
			BodyFixture bf = new BodyFixture(c);
			leftHumerus.addFixture(bf);
		}
		leftHumerus.translate(new Vector2(-0.3828125, -0.609375));
		leftHumerus.setMass(MassType.NORMAL);
		world.addBody(leftHumerus);

		// Left Ulna
		Body leftUlna = new Body();
		{// Fixture11
			Convex c = Geometry.createRectangle(0.25, 0.4);
			BodyFixture bf = new BodyFixture(c);
			leftUlna.addFixture(bf);
		}
		leftUlna.translate(new Vector2(-0.3828125, -0.9765625));
		leftUlna.setMass(MassType.NORMAL);
		world.addBody(leftUlna);

		// Right Femur
		Body rightFemur = new Body();
		{// Fixture12
			Convex c = Geometry.createRectangle(0.25, 0.75);
			BodyFixture bf = new BodyFixture(c);
			rightFemur.addFixture(bf);
		}
		rightFemur.translate(new Vector2(0.1796875, -1.5703125));
		rightFemur.setMass(MassType.NORMAL);
		world.addBody(rightFemur);

		// Left Femur
		Body leftFemur = new Body();
		{// Fixture13
			Convex c = Geometry.createRectangle(0.25, 0.75);
			BodyFixture bf = new BodyFixture(c);
			leftFemur.addFixture(bf);
		}
		leftFemur.translate(new Vector2(-0.1328125, -1.5703125));
		leftFemur.setMass(MassType.NORMAL);
		world.addBody(leftFemur);

		// Right Tibia
		Body rightTibia = new Body();
		{// Fixture14
			Convex c = Geometry.createRectangle(0.25, 0.5);
			BodyFixture bf = new BodyFixture(c);
			rightTibia.addFixture(bf);
		}
		rightTibia.translate(new Vector2(0.18359375, -2.11328125));
		rightTibia.setMass(MassType.NORMAL);
		world.addBody(rightTibia);

		// Left Tibia
		Body leftTibia = new Body();
		{// Fixture15
			Convex c = Geometry.createRectangle(0.25, 0.5);
			BodyFixture bf = new BodyFixture(c);
			leftTibia.addFixture(bf);
		}
		leftTibia.translate(new Vector2(-0.1328125, -2.1171875));
		leftTibia.setMass(MassType.NORMAL);
		world.addBody(leftTibia);

		// Head to Neck
		RevoluteJoint headToNeck = new RevoluteJoint(head, neck, new Vector2(0.01, -0.2));
		headToNeck.setLimitEnabled(false);
		headToNeck.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		headToNeck.setReferenceAngle(Math.toRadians(0.0));
		headToNeck.setMotorEnabled(false);
		headToNeck.setMotorSpeed(Math.toRadians(0.0));
		headToNeck.setMaximumMotorTorque(0.0);
		headToNeck.setCollisionAllowed(false);
		world.addJoint(headToNeck);

		// Neck to Torso
		RevoluteJoint neckToTorso = new RevoluteJoint(neck, torso, new Vector2(0.01, -0.35));
		neckToTorso.setLimitEnabled(false);
		neckToTorso.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		neckToTorso.setReferenceAngle(Math.toRadians(0.0));
		neckToTorso.setMotorEnabled(false);
		neckToTorso.setMotorSpeed(Math.toRadians(0.0));
		neckToTorso.setMaximumMotorTorque(0.0);
		neckToTorso.setCollisionAllowed(false);
		world.addJoint(neckToTorso);

		// Torso to Left Humerus
		RevoluteJoint torsoToLeftHumerus = new RevoluteJoint(torso, leftHumerus, new Vector2(-0.4, -0.4));
		torsoToLeftHumerus.setLimitEnabled(false);
		torsoToLeftHumerus.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		torsoToLeftHumerus.setReferenceAngle(Math.toRadians(0.0));
		torsoToLeftHumerus.setMotorEnabled(false);
		torsoToLeftHumerus.setMotorSpeed(Math.toRadians(0.0));
		torsoToLeftHumerus.setMaximumMotorTorque(0.0);
		torsoToLeftHumerus.setCollisionAllowed(false);
		world.addJoint(torsoToLeftHumerus);

		// Torso to Right Humerus
		RevoluteJoint torsoToRightHumerus = new RevoluteJoint(torso, rightHumerus, new Vector2(0.4, -0.4));
		torsoToRightHumerus.setLimitEnabled(false);
		torsoToRightHumerus.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		torsoToRightHumerus.setReferenceAngle(Math.toRadians(0.0));
		torsoToRightHumerus.setMotorEnabled(false);
		torsoToRightHumerus.setMotorSpeed(Math.toRadians(0.0));
		torsoToRightHumerus.setMaximumMotorTorque(0.0);
		torsoToRightHumerus.setCollisionAllowed(false);
		world.addJoint(torsoToRightHumerus);

		// Right Humerus to Right Ulna
		RevoluteJoint rightHumerusToRightUlna = new RevoluteJoint(rightHumerus, rightUlna, new Vector2(0.43, -0.82));
		rightHumerusToRightUlna.setLimitEnabled(false);
		rightHumerusToRightUlna.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		rightHumerusToRightUlna.setReferenceAngle(Math.toRadians(0.0));
		rightHumerusToRightUlna.setMotorEnabled(false);
		rightHumerusToRightUlna.setMotorSpeed(Math.toRadians(0.0));
		rightHumerusToRightUlna.setMaximumMotorTorque(0.0);
		rightHumerusToRightUlna.setCollisionAllowed(false);
		world.addJoint(rightHumerusToRightUlna);

		// Left Humerus to Left Ulna
		RevoluteJoint leftHumerusToLeftUlna = new RevoluteJoint(leftHumerus, leftUlna, new Vector2(-0.4, -0.81));
		leftHumerusToLeftUlna.setLimitEnabled(false);
		leftHumerusToLeftUlna.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		leftHumerusToLeftUlna.setReferenceAngle(Math.toRadians(0.0));
		leftHumerusToLeftUlna.setMotorEnabled(false);
		leftHumerusToLeftUlna.setMotorSpeed(Math.toRadians(0.0));
		leftHumerusToLeftUlna.setMaximumMotorTorque(0.0);
		leftHumerusToLeftUlna.setCollisionAllowed(false);
		world.addJoint(leftHumerusToLeftUlna);

		// Torso to Right Femur
		RevoluteJoint torsoToRightFemur = new RevoluteJoint(torso, rightFemur, new Vector2(0.16, -1.25));
		torsoToRightFemur.setLimitEnabled(false);
		torsoToRightFemur.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		torsoToRightFemur.setReferenceAngle(Math.toRadians(0.0));
		torsoToRightFemur.setMotorEnabled(false);
		torsoToRightFemur.setMotorSpeed(Math.toRadians(0.0));
		torsoToRightFemur.setMaximumMotorTorque(0.0);
		torsoToRightFemur.setCollisionAllowed(false);
		world.addJoint(torsoToRightFemur);

		// Torso to Left Femur
		RevoluteJoint torsoToLeftFemur = new RevoluteJoint(torso, leftFemur, new Vector2(-0.13, -1.25));
		torsoToLeftFemur.setLimitEnabled(false);
		torsoToLeftFemur.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		torsoToLeftFemur.setReferenceAngle(Math.toRadians(0.0));
		torsoToLeftFemur.setMotorEnabled(false);
		torsoToLeftFemur.setMotorSpeed(Math.toRadians(0.0));
		torsoToLeftFemur.setMaximumMotorTorque(0.0);
		torsoToLeftFemur.setCollisionAllowed(false);
		world.addJoint(torsoToLeftFemur);

		// Right Femur to Right Tibia
		RevoluteJoint rightFemurToRightTibia = new RevoluteJoint(rightFemur, rightTibia, new Vector2(0.17, -1.9));
		rightFemurToRightTibia.setLimitEnabled(false);
		rightFemurToRightTibia.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		rightFemurToRightTibia.setReferenceAngle(Math.toRadians(0.0));
		rightFemurToRightTibia.setMotorEnabled(false);
		rightFemurToRightTibia.setMotorSpeed(Math.toRadians(0.0));
		rightFemurToRightTibia.setMaximumMotorTorque(0.0);
		rightFemurToRightTibia.setCollisionAllowed(false);
		world.addJoint(rightFemurToRightTibia);

		// Left Femur to Left Tibia
		RevoluteJoint leftFemurToLeftTibia = new RevoluteJoint(leftFemur, leftTibia, new Vector2(-0.14, -1.9));
		leftFemurToLeftTibia.setLimitEnabled(false);
		leftFemurToLeftTibia.setLimits(Math.toRadians(0.0), Math.toRadians(0.0));
		leftFemurToLeftTibia.setReferenceAngle(Math.toRadians(0.0));
		leftFemurToLeftTibia.setMotorEnabled(false);
		leftFemurToLeftTibia.setMotorSpeed(Math.toRadians(0.0));
		leftFemurToLeftTibia.setMaximumMotorTorque(0.0);
		leftFemurToLeftTibia.setCollisionAllowed(false);
		world.addJoint(leftFemurToLeftTibia);
	}


	public void update(double deltaTime) {
		mousePicker.update(world, camera.getTransform((int)canvas.getWidth(), (int)canvas.getHeight()), 100);
		world.update(deltaTime);
	}

	public void draw(FXGraphics2D g2d) {
		g2d.setTransform(new AffineTransform());
		g2d.setColor(Color.white);
		g2d.clearRect(0,0, 1920, 1080);

		g2d.setTransform(camera.getTransform((int)canvas.getWidth(), (int)canvas.getHeight()));
		g2d.scale(1,-1);

		DebugDraw.draw(g2d, world, 100);
	}


	public static void main(String[] args) {
		Application.launch(HelloJoints.class);
	}
}
