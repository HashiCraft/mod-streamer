package io.hashicraft.stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModBlock extends Block {
  static final String blockName = "stream_block";
  static final Material blockMaterial = Material.ROCK;
  static final Properties props = Properties.create(blockMaterial).hardnessAndResistance(3.0F, 3.0F);

  private static final Logger LOGGER = LogManager.getLogger();

  public ModBlock() {
      super(props);
      this.setRegistryName(blockName);
  }

  @Override
  boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

    LOGGER.info("Clicked on block", worldIn.isRemote);
    if (worldIn.isRemote) { 
      return true;
    }
      
    YouTubePlayer mp = new YouTubePlayer();
    mp.Play(); 

    return false;
  }
  public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
    super.onBlockClicked(state, worldIn, pos, player);

  }

}