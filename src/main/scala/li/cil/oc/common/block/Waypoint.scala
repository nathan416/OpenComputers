package li.cil.oc.common.block

import li.cil.oc.OpenComputers
import li.cil.oc.common.GuiType
import li.cil.oc.common.tileentity
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.world.World

class Waypoint extends RedstoneAware with traits.Rotatable {
  override protected def setDefaultExtendedState(state: IBlockState) = setDefaultState(state)

  override def createNewTileEntity(world: World, metadata: Int) = new tileentity.Waypoint()

  // ----------------------------------------------------------------------- //

  override def localOnBlockActivated(world: World, pos: BlockPos, player: EntityPlayer, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    if (!player.isSneaking) {
      if (world.isRemote) {
        player.openGui(OpenComputers, GuiType.Waypoint.id, world, pos.getX, pos.getY, pos.getZ)
      }
      true
    }
    else super.localOnBlockActivated(world, pos, player, side, hitX, hitY, hitZ)
  }

  override def getValidRotations(world: World, pos: BlockPos): Array[EnumFacing] =
    world.getTileEntity(pos) match {
      case waypoint: tileentity.Waypoint =>
        EnumFacing.values.filter {
          d => d != waypoint.facing && d != waypoint.facing.getOpposite
        }
      case _ => super.getValidRotations(world, pos)
    }
}