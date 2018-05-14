package hu.elte.gazdalkodjokosan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URL;

@SpringBootApplication
public class GazdalkodjOkosanApplication extends Application {
    public static ConfigurableApplicationContext context;
    public static SpringApplicationBuilder builder;
    public Parent rootNode;


    @Override
    public void init() throws Exception {
        builder = new SpringApplicationBuilder(GazdalkodjOkosanApplication.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));

        URL url = getClass().getResource("/fxml/Starter.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(context::getBean);
        rootNode = loader.load();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        double width = 230;
        double height = 150;

        primaryStage.setScene(new Scene(rootNode, width, height));
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Gazdálkodj okosan");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
