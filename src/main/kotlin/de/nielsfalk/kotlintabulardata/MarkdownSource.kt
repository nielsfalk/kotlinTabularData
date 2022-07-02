package de.nielsfalk.kotlintabulardata

import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.junit.jupiter.params.support.AnnotationConsumer
import java.util.stream.Stream
import kotlin.annotation.AnnotationTarget.FUNCTION

@Target(FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ArgumentsSource(MarkdownArgumentsProvider::class)
annotation class MarkdownSource(@Language("Markdown") val value: String)

class MarkdownArgumentsProvider : ArgumentsProvider, AnnotationConsumer<MarkdownSource> {
  private var markdown: MarkdownSource? = null

  override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> =
    readMarkdownAsStrings(checkNotNull(this.markdown).value)
      .map { line ->
        Arguments.of(
          *line
            .map { cell -> if (cell == "null") null else cell }
            .toTypedArray()
        )
      }.stream()

  override fun accept(markdownSource: MarkdownSource?) {
    this.markdown = markdownSource
  }
}
