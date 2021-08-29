package com.enricog.entities

@JvmInline
value class ID private constructor(private val id: Long) {

    init {
        require(id >= ID_NEW) { "id must have a value more or equals to 0" }
    }

    val isNew: Boolean
        get() = id == ID_NEW

    operator fun compareTo(other: ID): Int {
        return id.compareTo(other.id)
    }

    fun toLong(): Long {
        return id
    }

    override fun toString(): String {
        return "ID: $id"
    }

    companion object {
        private const val ID_NEW = 0L

        fun new(): ID {
            return from(ID_NEW)
        }

        fun from(value: Long): ID {
            return ID(value)
        }
    }
}

val Long.asID: ID
    get() = ID.from(this)

val Int.asID: ID
    get() = ID.from(this.toLong())
