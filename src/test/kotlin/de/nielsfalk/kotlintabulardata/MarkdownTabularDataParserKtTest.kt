package de.nielsfalk.kotlintabulardata

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

class MarkdownTabularDataParserKtTest {
  @Test
  fun `read list of DataClassInstances`() {
    val data = readMarkdown<MarkdownRow>(
      """    
      | enum | int  | string | datetime             | boolean |
      |------|------|--------|----------------------|---------|
      | null | null | null   | null                 | null    |
      | Foo  | 42   | Bar    | 2022-03-09T14:24:59Z | true    |
      """
    )

    val expectedDateTime = LocalDate.of(2022, 3, 9)
      .atTime(14, 24, 59)
      .atOffset(ZoneOffset.UTC)
    expectThat(data).isEqualTo(
      listOf(
        MarkdownRow(null, null, null, null, null),
        MarkdownRow(TestEnum.Foo, 42, "Bar", expectedDateTime, true)
      )
    )
  }
}

data class MarkdownRow(
  val anEnumValue: TestEnum?,
  val anInt: Int?,
  val aString: String?,
  val aDateTime: OffsetDateTime?,
  val aBoolean: Boolean?
)
