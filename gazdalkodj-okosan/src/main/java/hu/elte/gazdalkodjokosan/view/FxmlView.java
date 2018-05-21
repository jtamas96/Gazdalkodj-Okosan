package hu.elte.gazdalkodjokosan.view;

import java.util.ResourceBundle;

public enum FxmlView {

    STARTER {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("app.title");
        }

        @Override
        String getFxmlFile() {
            return "/fxml/Starter.fxml";
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
