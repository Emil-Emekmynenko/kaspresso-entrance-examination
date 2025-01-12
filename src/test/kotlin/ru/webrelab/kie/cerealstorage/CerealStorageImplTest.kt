package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10.0f, 20.0f)

    // tests group for containerCapacity
    @Test
    // ассерт, если емкость отрицательна
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4.0f, 10.0f)
        }
    }

    @Test
    // значение ёмкости положительное
    fun `containerCapacity is positive`() {
        val testClass = CerealStorageImpl(1.3f, 10.0f)
        assertEquals(1.3f, testClass.containerCapacity)
    }

    // tests group for storageCapacity
    @Test
    //ассерт, если значение хранилища отрицательное
    fun `should throw if storageCapacity is negative`() {
        assertThrows<IllegalArgumentException>("Невалидное значение storageCapacity") {
            CerealStorageImpl(10.0f, -8.0f)
        }
    }

    @Test
    // ассерт, если значение хранилища меньше чем значение одного контейнера
    fun `should throw if storageCapacity less containerCapacity`() {
        assertThrows<IllegalArgumentException>("Невалидное значение storageCapacity") {
            CerealStorageImpl(10.0f, 8.0f)
        }
    }

    // tests group for method addCereal
    @Test
    // добавляет крупу в существующий контейнер
    fun `adds cereal to an existing container`() {
        repeat(2) { storage.addCereal(Cereal.RICE, 5.0f) }
        val actual = storage.getAmount(Cereal.RICE)
        assertEquals(10.0f, actual)
    }

    @Test
    // добавляет новый контейнер
    fun `adds a new container`() {
        storage.addCereal(Cereal.RICE, 5.0f)
        val actual = storage.getAmount(Cereal.RICE)
        assertEquals(5.0f, actual)
    }

    @Test
    // возврат оставшегося места в контейнере, если добавить крупу не удалось
    fun `returning the remaining space in the container if adding cereal failed`() {
        storage.addCereal(Cereal.RICE, 9.7f)
        val actual = storage.addCereal(Cereal.RICE, 2.7f)
        assertEquals(2.4f, actual, 0.01f)

    }

    @Test
    // добавление нескольких разных контейнеров
    fun `added several different containers`() {
        with(storage) {
            addCereal(Cereal.PEAS, 7.3f)
            addCereal(Cereal.BULGUR, 9.4f)
        }
        assertAll(
            {
                assertEquals(7.3f, storage.getAmount(Cereal.PEAS))
                assertEquals(9.4f, storage.getAmount(Cereal.BULGUR))
            })
    }

    @Test
    // ассерт, если значение amount отрицательное
    fun `should throw if amount is negative (addCereal)`() {
        val exception = assertThrows<IllegalArgumentException> {
            storage.addCereal(Cereal.RICE, -6f)
        }
        assertEquals("Количество крупы не может быть отрицательным", exception.message)
    }

    @Test
    // ассерт, если хранилище заполнено
    fun `should throw if the storage is full`() {
        with(storage) {
            addCereal(Cereal.PEAS, 10.0f)
            addCereal(Cereal.BULGUR, 10.0f)
        }
        val exception = assertThrows<IllegalStateException> {
            storage.addCereal(Cereal.BUCKWHEAT, 0.1f)
        }
        assertEquals("Недостаточно места для нового контейнера", exception.message)

    }

    // tests group for method getCereal
    @Test
    // Крупы в контейнере становиться меньше, при изъятии
    fun `cereal in the container become smaller when removed`() {
        with(storage) {
            addCereal(Cereal.PEAS, 10.0f)
            getCereal(Cereal.PEAS, 7.0f)
        }
        val actual = storage.getAmount(Cereal.PEAS)
        assertEquals(3.0f, actual, 0.01f)
    }

    @Test
    // возвращает остаток крупы, если в контейнере недостаточно запрашиваемой крупы
    fun `returns the remaining cereal if there is not enough requested cereal in the container`() {
        storage.addCereal(Cereal.PEAS, 7.0f)
        val actual = storage.getCereal(Cereal.PEAS, 8.0f)

        assertEquals(7.0f, actual, 0.01f)

    }

    @Test
    // ассерт, если cereal нет в хранилище
    fun `should throw if cereal is not in storage`() {
        val actual = storage.getCereal(Cereal.PEAS, 8.0f)
        assertEquals(0.0f, actual)
    }

    @Test
    // ассерт, если значение amount отрицательное
    fun `should throw if amount is negative (getCereal)`() {
        val exception = assertThrows<IllegalArgumentException> {
            storage.getCereal(Cereal.RICE, -6f)
        }
        assertEquals("Количество крупы не может быть отрицательным", exception.message)
    }

    // tests group for method removeContainer
    @Test
    // удаление контейнера успешно
    fun `container removal successful - true`() {
        with(storage) {
            addCereal(Cereal.BULGUR, 0.1f)
            getCereal(Cereal.BULGUR, 0.1f)
        }
        val actual = storage.removeContainer(Cereal.BULGUR)
        assertEquals(true, actual)
    }

    @Test
    // удаление контейнера не успешно
    fun `container removal successful - false`() {
        storage.addCereal(Cereal.BULGUR, 0.1f)
        val actual = storage.removeContainer(Cereal.BULGUR)
        assertEquals(false, actual)
    }


    // tests group for method removeContainer
    @Test
    // возвращает значение крупы в контейнере
    fun `returns the value of the cereal in the container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 1.0f)
        val actual = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(1.0f, actual)
    }

    @Test
    // возвращает значение крупы в контейнере если в контейнере нет ничего
    fun `returns the value of the cereal in the container is empty`() {
        val actual = storage.getAmount(Cereal.BUCKWHEAT)
        assertEquals(0.0f, actual)
    }


    // tests group for method getSpace
    @Test
    // Возвращает значение свободного места в контейнере
    fun `returns the value of free space in the container`() {
        storage.addCereal(Cereal.BUCKWHEAT, 1.0f)
        val actual = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(9.0f, actual)
    }

    @Test
    // Возвращает значение свободного места в контейнере если в контейнере нет ничего
    fun `returns the value of free space in the container is empty`() {
        val actual = storage.getSpace(Cereal.BUCKWHEAT)
        assertEquals(10.0f, actual)
    }


    // tests group for method toString
    @Test
    // Возвращает текстовое сообщение наполненности хранилища
    fun `return storage full text message`() {
        with(storage) {
            addCereal(Cereal.BULGUR, 7.0f)
            addCereal(Cereal.MILLET, 9.9f)
        }
        val actual = storage.toString()

        assertEquals("BULGUR: 7.0 единиц, MILLET: 9.9 единиц", actual)
    }

    @Test
    // Возвращает текстовое сообщение если хранилище пустое
    fun `return storage full text message if storage is empty`() {
        val actual = storage.toString()
        assertEquals("Хранилище пустое", actual)
    }


}