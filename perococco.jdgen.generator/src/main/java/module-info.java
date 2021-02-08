import perococco.gen.generator.DungeonGenerator;

module jdgen.generator {
    uses DungeonGenerator;
    requires static lombok;
    requires jdgen.api;
    requires jdgen.core;
    requires jdgen.graph;
    requires jdgen.mapper;
    requires jdgen.rooms;


    exports perococco.gen.generator;
}
