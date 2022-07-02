package de.nielsfalk.kotlintabulardata

import org.junit.jupiter.params.ParameterizedTest
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class MarkdownArgumentsProviderTest {
  @ParameterizedTest
  @MarkdownSource(
    """    
    | enum | int  | expected string concat |
    |------|------|------------------------|
    | null | 1    | null 1                 |  
    | Foo  | null | Foo null               |
    """
  )
  fun `use values from Markdown source`(testEnum: TestEnum?, int: Int?, expectedStringConcat: String?) {
    val concatenatedInputParameters = "$testEnum $int"

    expectThat(concatenatedInputParameters).isEqualTo(expectedStringConcat)
  }
}
