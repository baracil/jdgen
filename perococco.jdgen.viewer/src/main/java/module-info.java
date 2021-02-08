module jdgen.viewer {
    requires static lombok;
    requires java.desktop;


    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    requires com.google.common;
    requires jdgen.api;
    requires jdgen.core;
    requires jdgen.rooms;
    requires jdgen.graph;
    requires jdgen.mapper;

    exports perococco.jdgen.viewer;
}
