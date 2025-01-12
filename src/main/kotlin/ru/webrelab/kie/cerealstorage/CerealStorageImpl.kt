package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float,
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }

    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(
        cereal: Cereal,
        amount: Float,
    ): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }

        val currentAmount = storage.getOrDefault(cereal, 0f)

        val availableSpace = containerCapacity - currentAmount

        if ((storage.size + 1) * containerCapacity > storageCapacity) {
            throw IllegalStateException("Недостаточно места для нового контейнера")
        }

        return if (amount <= availableSpace) {
            storage[cereal] = currentAmount + amount
            0f
        } else {
            val overflow = amount - availableSpace
            storage[cereal] = containerCapacity
            overflow
        }
    }


    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Количество крупы не может быть отрицательным" }

        val currentAmount = storage.getOrDefault(cereal, 0f)

        return if (amount <= currentAmount) {
            storage[cereal] = currentAmount - amount
            0f
        } else {
            storage[cereal] = 0f
            currentAmount
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage.getOrDefault(cereal, 0.0f)
        return if (currentAmount > 0) {
            false
        } else {
            storage.remove(cereal)
            true
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal, 0f)
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage.getOrDefault(cereal, 0f)
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        return storage.entries.joinToString(", ") {
            "${it.key}: ${it.value} единиц"
        }.ifEmpty { "Хранилище пустое" }
    }

}

fun main() {
    val cerealStorageImpl = CerealStorageImpl(10f, 20f)
    println(cerealStorageImpl.getAmount(Cereal.BULGUR))
    cerealStorageImpl.addCereal(Cereal.PEAS, 7.0f)
    cerealStorageImpl.addCereal(Cereal.BUCKWHEAT, 9.0f)
    println(cerealStorageImpl.toString())
}
