package co.turtlegames.engine.world.virtual;

import net.minecraft.server.v1_8_R3.ChunkRegionLoader;
import net.minecraft.server.v1_8_R3.World;

import java.io.File;
import java.io.IOException;

public class VChunkLoader extends ChunkRegionLoader {

    public VChunkLoader(File file) {
        super(null);
    }

    @Override
    public boolean chunkExists(World world, int i, int j) {
        return false;
    }

    @Override
    public Object[] loadChunk(World world, int i, int j) throws IOException {
        return null;
    }

}
