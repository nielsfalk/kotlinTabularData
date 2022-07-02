package de.nielsfalk.kotlintabulardata

import de.nielsfalk.givenwhenthen.readTabularData
import de.nielsfalk.givenwhenthen.readTestArguments
import de.nielsfalk.kotlintabulardata.TestEnum.Foo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class KotlinTabularDataParserKtTest {
  @Test
  fun `read list of DataClassInstances`() {
    val data: List<Triple<TestEnum?, Int?, String?>> = readTabularData {
      // @formatter:off
      null ǀ null ǀ null
      Foo  ǀ 42   ǀ "Bar"
      // @formatter:on
    }

    expectThat(data).isEqualTo(
      listOf(
        Triple(null, null, null),
        Triple(Foo, 42, "Bar")
      )
    )
  }

  @ParameterizedTest
  @MethodSource("readTestArguments")
  fun `use tabular data as test parameters`(
    testEnum: TestEnum?,
    int: Int?,
    expectedStringConcat: String?
  ) {
    val concatenatedInputParameters = "$testEnum $int"

    expectThat(concatenatedInputParameters).isEqualTo(expectedStringConcat)
  }

  @ParameterizedTest
  @MethodSource("readTabularData")
  fun `use tabular data in DataClassInstances as test parameters`(testData: TestData) = testData.run {
    val concatenatedInputParameters = "$testEnum $int"

    expect {
      that(concatenatedInputParameters).isEqualTo(expectedStringConcat)
    }
  }

  companion object {
    @JvmStatic
    fun readTestArguments(): List<Arguments> =
      readTestArguments {
        // @formatter:off
        // first ǀ second ǀǀ expected string concat
           null  ǀ null   ǀǀ "null null"
           Foo   ǀ 42     ǀǀ "Foo 42"
        // @formatter:on
      }

    @JvmStatic
    fun readTabularData(): List<TestData> =
      readTabularData {
        // @formatter:off
        // first ǀ second ǀǀ expected string concat
           null  ǀ null   ǀǀ "null null"
           Foo   ǀ 42     ǀǀ "Foo 42"
        // @formatter:on
      }
  }
}

data class TestData(
  val testEnum: TestEnum?,
  val int: Int?,
  val expectedStringConcat: String
)

enum class TestEnum {
  Foo
}
