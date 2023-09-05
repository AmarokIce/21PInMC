package club.someoneice.points

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLServerStartingEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ChatComponentTranslation

@Mod(modid = Main.MODID)
class Main {
    companion object {
        const val MODID = "21points"

        fun sendAllPlayer(player: EntityPlayer, str: String) {
            (player.worldObj.playerEntities as List<EntityPlayer>).forEach {
                it.addChatMessage(ChatComponentTranslation(str))
            }
        }
    }

    @EventHandler
    fun serverStartingEvent(event: FMLServerStartingEvent) {
        event.registerServerCommand(CommandPoints())
    }
}