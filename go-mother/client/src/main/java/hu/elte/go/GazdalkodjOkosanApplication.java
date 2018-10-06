package hu.elte.go;

import hu.elte.go.view.FxmlView;
import hu.elte.go.view.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GazdalkodjOkosanApplication extends Application {
    private ConfigurableApplicationContext context;
    private StageManager stageManager;
    
    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(GazdalkodjOkosanApplication.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));
    
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stageManager = context.getBean(StageManager.class, primaryStage);        
//        double width = 230;
//        double height = 150;
//
//        primaryStage.setScene(new Scene(rootNode, width, height));
//        primaryStage.centerOnScreen();
//        primaryStage.setTitle("Gazdálkodj okosan");
//        primaryStage.show();
        stageManager.switchScene(FxmlView.STARTER);
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
