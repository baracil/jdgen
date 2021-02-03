module jdgen.viewer {
    requires static lombok;
    requires java.desktop;

    requires jdgen.core;

    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    requires com.google.common;
    requires jdgen.rooms;

    exports perococco.jdgen.viewer;
}
