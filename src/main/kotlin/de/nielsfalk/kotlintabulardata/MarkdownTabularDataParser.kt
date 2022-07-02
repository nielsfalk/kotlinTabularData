@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package de.nielsfalk.kotlintabulardata

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.lang.Boolean
import java.lang.Number
import kotlin.String
import kotlin.Suppress
import kotlin.apply

inline fun <reified T> readMarkdown(markdown: String): List<T> {
  val parameters = T::class.java.constructors.first().parameters
  val kParameters = T::class.constructors.first().parameters
  return readMarkdownAsStrings(markdown)
    .map { line ->
      mapper.readValue(
        line.mapIndexed { index, cell ->
          """"${kParameters[index].name}":${cellToJsonValue(cell, parameters[index].type)}"""
        }.joinToString(prefix = "{", postfix = "}")
      )
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

fun cellToJsonValue(cell: String, type: Class<*>): String {
  val trimmed = cell.trim()
  return when {
    trimmed == "null" ||
      Number::class.java.isAssignableFrom(type)
      || Boolean::class.java.isAssignableFrom(type) -> trimmed
    else -> """"$trimmed""""
  }
}

val mapper: ObjectMapper = ObjectMapper().apply {
  enable(SerializationFeature.INDENT_OUTPUT)
  setSerializationInclusion(JsonInclude.Include.NON_NULL)
  configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
  registerModule(JavaTimeModule())
  registerModule(KotlinModule.Builder().build())
}
