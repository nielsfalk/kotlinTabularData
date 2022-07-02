package de.nielsfalk.givenwhenthen

import org.junit.jupiter.params.provider.Arguments
import kotlin.reflect.jvm.isAccessible

fun readTestArguments(function: TabularDataBuilder.() -> Unit): List<Arguments> =
  readUntypedData(function).map { Arguments.of(*it.toTypedArray()) }

inline fun <reified T> readTabularData(function: TabularDataBuilder.() -> Unit): List<T> =
  readUntypedData(function).map { row ->
    T::class.constructors.first {
      it.parameters.size == row.size
    }.run {
      isAccessible = true
      call(*row.toTypedArray())
    }
  }

inline fun readUntypedData(function: TabularDataBuilder.() -> Unit): List<List<Any?>> =
  TabularDataBuilder().run {
    function()
    rowBuilders.map { it.cells.toList() }
  }

class TabularDataBuilder {
  val rowBuilders = mutableListOf<RowBuilder>()

  @Suppress("FunctionName", "NonAsciiCharacters")
  infix fun Any?.ǀ(nextCell: Any?): RowBuilder = if (this is RowBuilder) {
    cells.add(nextCell)
    this
  } else RowBuilder(firstCell = this, nextCell).also {
    rowBuilders += it
  }

  /*
  * Input- and expectation-data can be seperated by using ǀǀ
  */
  @Suppress("FunctionName", "NonAsciiCharacters")
  infix fun Any?.ǀǀ(nextCell: Any?): RowBuilder = this ǀ nextCell
}

class RowBuilder(firstCell: Any?, secondCell: Any?) {
  val cells = mutableListOf(firstCell, secondCell)
}
