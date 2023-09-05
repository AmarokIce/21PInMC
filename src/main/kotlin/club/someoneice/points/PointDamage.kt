package club.someoneice.points

import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.util.DamageSource
import net.minecraft.util.IChatComponent

class PointDamage: DamageSource("point") {
    override fun func_151519_b(entity: EntityLivingBase): IChatComponent {
        return ChatComponentTranslation("${entity.commandSenderName} 付出了代价。")
    }

    companion object {
        val DAMAGE = PointDamage()
    }
}