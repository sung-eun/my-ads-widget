package com.essie.myads.domain.entity

data class AdAccount(
    val id: String,
    val displayName: String,
    val supplier: AdSupplier
)