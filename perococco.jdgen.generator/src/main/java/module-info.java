module jdgen.generator {
    uses perococco.gen.generator.Generator;
    requires static lombok;
    requires jdgen.core;
    requires jdgen.graph;
    requires jdgen.mapper;
    requires jdgen.rooms;


    exports perococco.gen.generator;
}
