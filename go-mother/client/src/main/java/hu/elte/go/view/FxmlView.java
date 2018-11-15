package hu.elte.go.view;

import java.util.ResourceBundle;

public enum FxmlView {

    STARTER {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("app.title");
        }

        @Override
        String getFxmlFile() {
            return "/fxml/IPPrompt.fxml";
        }
    }, BOARD {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("app.title");
        }

        @Override
        String getFxmlFile() {
            return "/fxml/GameBoard.fxml";
        }
    };
    
    abstract String getTitle();
    abstract String getFxmlFile();
    
    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
