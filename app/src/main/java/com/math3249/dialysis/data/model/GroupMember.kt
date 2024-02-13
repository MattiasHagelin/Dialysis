package com.math3249.dialysis.data.model

data class GroupMember(
    val groupId: String = "",
    val canRead: Boolean = true,
    val canWrite: Boolean = true,
    val isOwner: Boolean = true
)
