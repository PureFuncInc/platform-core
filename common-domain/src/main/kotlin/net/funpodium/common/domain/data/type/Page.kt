package net.funpodium.common.domain.data.type

data class Page(

    val index: Int,

    val size: Int
) {

    companion object {
        val default = Page(0, 10)
    }
}
