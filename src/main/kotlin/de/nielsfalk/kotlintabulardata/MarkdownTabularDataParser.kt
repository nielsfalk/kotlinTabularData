@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package de.nielsfalk.kotlintabulardata

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.intellij.lang.annotations.Language

inline fun <reified T> readMarkdown(@Language("Markdown") markdown: String): List<T> {
  val parametersNames = T::class.constructors.first().parameters.map { it.name }
  return readMarkdownAsStrings(markdown)
    .map { line ->
      mapper.readValue(toJson(line, parametersNames))
    }
}

fun readMarkdownAsStrings(markdown: String): List<List<String>> =
  markdown
    .trim()
    .split('\n')
    .drop(2) // drop Headers
    .map { line ->
      line
        .split('|')
        .drop(1)
        .dropLast(1)
        .map { it.trim() }
    }

fun toJson(
  line: List<String>,
  parametersNames: List<String?>
): String =
  line.mapIndexed { index, cell ->
    """"${parametersNames[index]}":${cellToJsonValue(cell)}"""
  }.joinToString(prefix = "{", postfix = "}")

fun cellToJsonValue(cell: String): String {
  val trimmed = cell.trim()
  return if (trimmed == "null") trimmed
  else """"$trimmed""""
}

val mapper: ObjectMapper = ObjectMapper().apply {
  enable(SerializationFeature.INDENT_OUTPUT)
  setSerializationInclusion(JsonInclude.Include.NON_NULL)
  configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  registerModule(JavaTimeModule())
  registerModule(KotlinModule.Builder().build())
}
