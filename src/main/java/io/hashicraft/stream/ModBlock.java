package io.hashicraft.stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModBlock extends Block {
  static final String blockName = "stream_block";
  static final Material blockMaterial = Material.ROCK;
  static final Properties props = Properties.create(blockMaterial).hardnessAndResistance(3.0F, 3.0F);

  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    if (!world.isRemote) {
      LOGGER.info("Block placed, client call");
      YouTubePlayer player = new YouTubePlayer();
      player.Play();
    }
  }

  public ModBlock() {
      super(props);
      this.setRegistryName(blockName);
  }

}