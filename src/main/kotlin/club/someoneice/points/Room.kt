package club.someoneice.points

import com.google.common.collect.Maps
import com.google.common.collect.Sets
import java.util.HashMap
import java.util.HashSet

object Room {
    var open = false
    var isRunning = false
    var name = ""
    val players: HashSet<String> = Sets.newHashSet()
    val stopPlayers: HashSet<String> = Sets.newHashSet()
    val playerCard: HashMap<String, Int> = Maps.newHashMap()

    fun init() {
        this.open = false
        this.isRunning = false
        this.name = ""
        this.players.clear()
        this.stopPlayers.clear()
        this.playerCard.clear()
    }
}