module jdgen.viewer {
    requires static lombok;
    requires java.desktop;

    requires jdgen.core;

    requires javafx.graphics;
    requires javafx.controls;

    requires com.google.common;

    exports perococco.jdgen.viewer;
}
