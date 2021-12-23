package ru.popov.firstproductvalidation

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

class FirstProductCustomAdapter {

    @FromJson
    fun fromJson(customFirstProduct: CustomFirstProduct): CustomFirstProduct {
        return customFirstProduct
    }

    @ToJson
    fun toJson(customFirstProduct: CustomFirstProduct): CustomFirstProduct {
        return customFirstProduct
    }

    @JsonClass(generateAdapter = true)
    data class CustomFirstProduct(
        val product: String?,
        val execution: String?,
        val numberBatch: String?,
        val firmwareVersion: String?,
        val redumProcess: String?,
        val serNum: String?,
        val preparationBoard: Boolean?,
        val notePreparationBoard: String?,
        val preparationBody: Boolean?,
        val notePreparationBody: String?,
        val boardInstallationCase: Boolean?,
        val noteBoardInstallationCase: String?,
        val installationAKB: Boolean?,
        val noteInstallationAKB: String?,
        val programming: Boolean?,
        val noteProgramming: String?,
        val check: Boolean?,
        val noteCheck: String?,
        val topCoverInstallation: Boolean?,
        val noteTopCoverInstallation: String?,
        val packing: Boolean?,
        val notePacking: String?,
        val titleResultFlag: Boolean?,
        val releaseFlag: Boolean?,
        val date: String?,
        val sign: String?,
    )
}