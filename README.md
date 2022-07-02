# Kotlin Tabular Data
![example workflow](https://github.com/nielsfalk/kotlinTabularData/actions/workflows/gradle.yml/badge.svg)
[![](https://jitpack.io/v/nielsfalk/kotlinTabularData.svg)](https://jitpack.io/#nielsfalk/kotlinTabularData)


provides a DSL to define Data in a tabular way. It is inspired by [Spock](https://spockframework.org/)

## Examples

Tables of data can be written as in a markup-language. It results in a List of data-class-instances. 

```kotlin
val data: List<Triple<TestEnum?, Int?, String?>> = readTabularData {
    // @formatter:off
    null ǀ null ǀ null
    Foo  ǀ 42   ǀ "Bar"
    // @formatter:on
}
```

This Tabular data can be used to parameterize a test.

```kotlin
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
}
```

Data can also be defined in Markdown-Tables. In IntelliJ with language-injection the Markdown has syntax-highlighting.

```kotlin
val data = readMarkdown<Triple<TestEnum?, Int?, String?>>("""    
  | enum | int  | string |
  |------|------|--------|
  | null | null | null   |  
  | Foo  | 42   | Bar    |
  """
)
```

The @MarkdownSource annotation can be used to Parameterize a test directly with a markdown-table

```kotlin
@ParameterizedTest
@MarkdownSource("""    
  | enum | int  | string       |
  |------|------|--------------|
  | null | 1    | Hallo World! |  
  | Foo  | null | Hello World! |
  """
)
fun `use values from Markdown source`(
    testEnum: TestEnum?,
    int: Int?,
    string: String?
) {
    // Test implementation
}
```

## Getting started

Add the following to your ```build.gradle.kts```

```kotlin
repositories {
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    testImplementation("com.github.nielsfalk:kotlinTabularData:0+")
 }
```

or follow the Instructions on [jitpack](https://jitpack.io/#nielsfalk/kotlinTabularData)

[<div align="right"><img src="KotlinMascot.png" width="50%" /></div>](https://kotlinlang.org/docs/kotlin-mascot.html)

## Full Spock Dsl

to archive the full [Spock](https://spockframework.org/)-Dsl use this project bundled in [GivenWhenThen](https://github.com/nielsfalk/givenWhenThen) and write expressive tests like this:

```kotlin
class ShowcaseTest : GivenWhenThenTest(
    scenario(
        description {
            "Rock Paper Scissors expectedWinner=${data.expectedWinner}"
        },
        `when` { data.first defend data.second },
        then {
            expectActual()
                .isEqualTo(data.expectedWinner)
        },
        where<RockPaperScissorsTestCase> {
            // @formatter:off
            Rock     ǀ Rock     ǀǀ null
            Rock     ǀ Scissors ǀǀ Rock
            Rock     ǀ Paper    ǀǀ Paper
            Scissors ǀ Scissors ǀǀ null
            Scissors ǀ Paper    ǀǀ Scissors
            Scissors ǀ Rock     ǀǀ Rock
            Paper    ǀ Paper    ǀǀ null
            Paper    ǀ Rock     ǀǀ Paper
            Paper    ǀ Scissors ǀǀ Scissors
            // @formatter:on
        }
    )
)

private data class RockPaperScissorsTestCase(
    val first: RockPaperScissors,
    val second: RockPaperScissors,
    val expectedWinner: RockPaperScissors?
)
```


