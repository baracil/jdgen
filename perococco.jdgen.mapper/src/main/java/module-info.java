module jdgen.mapper {
    requires static lombok;

    requires com.google.common;
    requires jdgen.core;
    requires jdgen.api;

    exports perococco.jdgen.mapper;
}
