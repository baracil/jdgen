module jdgen.core {
    requires static lombok;
    requires java.desktop;

    requires jdgen.generator;

    exports perococco.jdgen.core;
}
