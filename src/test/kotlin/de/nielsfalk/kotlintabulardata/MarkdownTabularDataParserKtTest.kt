package de.nielsfalk.kotlintabulardata

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class MarkdownTabularDataParserKtTest {
  @Test
  fun `read list of DataClassInstances`() {
    val data = readMarkdown<Triple<TestEnum?, Int?, String?>>(
      """    
      | enum | int  | string |
      |------|------|--------|
      | null | null | null   |  
      | Foo  | 42   | Bar    |
      """
    )

    expectThat(data).isEqualTo(
      listOf(
        Triple(null, null, null),
        Triple(TestEnum.Foo, 42, "Bar")
      )
    )
  }
}
