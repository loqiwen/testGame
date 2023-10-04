package com.example.testgame

abstract class Creature(
    val attack: Int,
    val defense: Int,
    var health: Int,
) {
    val maxHealth = 100
    fun takeDamage(damage: Int) {
        health -= damage
        if (health < 0) health = 0
    }

    fun isDead(health: Int): Boolean = (health <= 0)
    fun damageRange(): IntRange = 1..6
}

class Player(attack: Int, deffence: Int, health: Int) : Creature(attack, deffence, health) {
    var countOfHeals: Int = 0
    val dices = mutableListOf<Int>()
    fun attack(monster: Monster) {
        val attackModifier = attack - monster.defense + 1
        val diceRolls = if (attackModifier > 0) attackModifier else 1
        dices.clear()
        var attackSuccessful = false
        repeat(diceRolls) {
            val diceRoll = (1..6).random()
            dices.add(diceRoll)
            if (diceRoll >= 5) {
                attackSuccessful = true
            }
        }

        if (attackSuccessful) {
            val damage = damageRange().random()
            monster.takeDamage(damage)
        }
    }

    fun heal() {
        if (countOfHeals < 4) {
            countOfHeals += 1
            val maxHealAmount = maxHealth * 0.3
            val healAmount = maxHealAmount.coerceAtMost((maxHealth - health).toDouble())
            health += healAmount.toInt()
        }
    }
}

class Monster(attack: Int, defence: Int, health: Int) : Creature(attack, defence, health) {
    val dices = mutableListOf<Int>()
    fun attack(player: Player) {

        val attackModifier = attack - player.defense + 1
        val diceRolls = if (attackModifier > 0) attackModifier else 1
        dices.clear()
        var attackSuccessful = false
        repeat(diceRolls) {
            val diceRoll = (1..6).random()
            dices.add(diceRoll)
            if (diceRoll >= 5) {
                attackSuccessful = true
            }
        }
        if (attackSuccessful) {
            val damage = damageRange().random()
            player.takeDamage(damage)
        }
    }
}