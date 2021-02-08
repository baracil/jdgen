module jdgen.mapper {
    requires static lombok;

    requires com.google.common;
    requires jdgen.core;
    requires jdgen.generator;

    exports perococco.jdgen.mapper;
}
