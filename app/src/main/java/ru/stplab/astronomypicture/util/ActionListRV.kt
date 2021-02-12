package ru.stplab.astronomypicture.util

@Suppress("DataClassPrivateConstructor")
data class ActionListRV private constructor(val status: Action, val fromPos: Int? = null, val toPos: Int? = null) {
    companion object {
        fun moved(fromPos: Int?, toPos: Int?) = ActionListRV(Action.MOVED, fromPos, toPos)
        fun removed(fromPos: Int?) = ActionListRV(Action.REMOVED, fromPos)
        fun insert(fromPos: Int?) = ActionListRV(Action.INSERT, fromPos)
    }

    enum class Action {
        MOVED,
        REMOVED,
        INSERT
    }
}