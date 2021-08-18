package com.essie.myads.domain.entity

data class Account(
    val id: String,
    val displayName: String,
    val supplier: AdSupplier
)