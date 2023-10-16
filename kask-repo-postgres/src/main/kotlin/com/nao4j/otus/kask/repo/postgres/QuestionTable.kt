package com.nao4j.otus.kask.repo.postgres

import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.UserId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object QuestionTable : Table("questions") {

    val id = varchar("id", 128)
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val owner = varchar("owner", 128)

    override val primaryKey = PrimaryKey(id)

    fun from(res: InsertStatement<Number>) = Question(
        id = Question.QuestionId(res[id].toString()),
        title = res[title],
        description = res[description],
        ownerId = UserId(res[owner].toString()),
    )

    fun from(res: ResultRow) = Question(
        id = Question.QuestionId(res[id].toString()),
        title = res[title],
        description = res[description],
        ownerId = UserId(res[owner].toString()),
    )

    fun to(it: UpdateBuilder<*>, question: Question, randomUuid: () -> String) {
        it[id] = question.id.takeIf { it != Question.QuestionId.NONE }?.asString() ?: randomUuid()
        it[title] = question.title
        it[description] = question.description
        it[owner] = question.ownerId.asString()
    }
}
