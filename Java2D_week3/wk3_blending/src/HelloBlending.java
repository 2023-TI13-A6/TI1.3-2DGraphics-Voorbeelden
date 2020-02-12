import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import sun.awt.image.BufferedImageDevice;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HelloBlending extends Application {
	Stage stage;
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		javafx.scene.canvas.Canvas canvas = new Canvas(1920, 1080);

		try {
			image1 = ImageIO.read(getClass().getResource("/images/test.png"));
			image2 = ImageIO.read(getClass().getResource("/images/bg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}


		FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
		draw(g2d);
		primaryStage.setScene(new Scene(new Group(canvas)));
		primaryStage.setTitle("Hello Blending");
		primaryStage.show();
	}

	Image image1;
	Image image2;

	public void draw(FXGraphics2D g2d)
	{
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		for(float f = 0; f < 1; f+=0.1) {
			g2d.drawImage(image2, AffineTransform.getTranslateInstance(f * 1500, 0), null);
		}
		for(float f = 0; f <= 1; f+=0.1) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
			g2d.drawImage(image1, AffineTransform.getTranslateInstance(f*1500, 0), null);
			g2d.drawImage(image1, AffineTransform.getTranslateInstance(f*1500, 150), null);
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));


	}
}
