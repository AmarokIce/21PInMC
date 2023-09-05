package club.someoneice.points

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ChatComponentTranslation
import java.util.Random
import kotlin.random.asKotlinRandom

class CommandPoints: CommandBase() {
    override fun getCommandName(): String = "21points"


    override fun getCommandUsage(sender: ICommandSender): String = "/21points"

    override fun processCommand(sender: ICommandSender, tree: Array<out String>) {
        val player = getCommandSenderAsPlayer(sender)
        when(tree[0]) {
            "join"      -> join(player)
            "start"     -> start(player)
            "get"       -> get(player)
            "stop"      -> stop(player)
            "giveup"    -> fail(player)
        }
    }

    private fun join(player: EntityPlayer) {
        if (!Room.isRunning) {
            if (!Room.open) {
                Room.open = true
                Room.name = player.displayName
                Room.players.add(player.displayName)
                Main.sendAllPlayer(player, "${player.displayName} 开启了一场21点游戏！")
            } else {
                Room.players.add(player.displayName)
                Main.sendAllPlayer(player, "${player.displayName} 加入了一场21点游戏！当前已有 ${Room.players.size} 位玩家！")
            }
        }
    }

    private fun start(player: EntityPlayer) {
        val random = Random().asKotlinRandom()
        if (Room.open && !Room.isRunning && player.displayName == Room.name) {
            Main.sendAllPlayer(player, "${Room.name} 发起的一场21点游戏准备开始！当前已有 ${Room.players.size} 位玩家！")
            Room.isRunning = true
            Room.players.forEach {
                Room.playerCard[it] = random.nextInt(1, 22)
                getPlayer(player, it).addChatMessage(ChatComponentTranslation("$it ,你的初始牌和是 ${Room.playerCard[it]}"))
            }

        } else player.addChatMessage(ChatComponentTranslation("未存在需要启动的游戏！"))
    }

    private fun get(player: EntityPlayer) {
        val random = Random().asKotlinRandom()
        if (Room.isRunning) {
            if (Room.playerCard.contains(player.displayName) && !Room.stopPlayers.contains(player.displayName)) {
                val card = random.nextInt(1, 13)
                Room.playerCard[player.displayName] = Room.playerCard[player.displayName]!! + card
                Main.sendAllPlayer(player, "${player.displayName} 抽到了 $card ！现在他的牌和是 ${Room.playerCard[player.displayName]}")
                if (Room.playerCard[player.displayName]!! > 21) {
                    val damage = (Room.playerCard[player.displayName]!! - 21) * 2
                    Main.sendAllPlayer(player, "${player.displayName} 爆牌！将会受到 ${damage} 的伤害！")
                    player.attackEntityFrom(PointDamage.DAMAGE, damage.toFloat())
                    Room.playerCard.remove(player.displayName)
                    Room.players.remove(player.displayName)
                    check(player)
                }

            } else player.addChatMessage(ChatComponentTranslation("${player.displayName} ,你没有加入游戏或你已经停牌了！"))
        } else player.addChatMessage(ChatComponentTranslation("未存在启动的游戏！"))
    }

    private fun stop(player: EntityPlayer) {
        if (Room.isRunning) {
            Main.sendAllPlayer(player, "${player.displayName} 以点数 ${Room.playerCard[player.displayName]} 停牌！")
            Room.stopPlayers.add(player.displayName)
            check(player)
        }
    }

    private fun fail(player: EntityPlayer) {
        if (Room.isRunning) {
            val pot = Room.playerCard[player.displayName]!!
            Main.sendAllPlayer(player, "${player.displayName} 以点数 $pot 投降！")
            Room.playerCard.remove(player.displayName)
            Room.players.remove(player.displayName)
            Room.stopPlayers.remove(player.displayName)

            player.attackEntityFrom(PointDamage.DAMAGE, pot.toFloat())
            check(player)
        }
    }

    private fun check(player: EntityPlayer) {
        if (Room.players.size == Room.stopPlayers.size) {
            Main.sendAllPlayer(player, "全部玩家停牌！正在结算...")
            var MaxPlayer = ""
            var MaxSize = 0

            Room.playerCard.forEach {(key, value) ->
                if (value > MaxSize) {
                    MaxPlayer = key
                    MaxSize = value
                }
            }

            Main.sendAllPlayer(player, "$MaxPlayer 是最后的赢家！他的点数： $MaxSize")

            Room.playerCard.forEach {(key, value) ->
                if (key != MaxPlayer) {
                    getPlayer(player, key).attackEntityFrom(PointDamage.DAMAGE, (21 - value).toFloat())
                }
            }

            Room.init()
        }
    }

    override fun canCommandSenderUseCommand(p_71519_1_: ICommandSender?): Boolean {
        return true
    }
}