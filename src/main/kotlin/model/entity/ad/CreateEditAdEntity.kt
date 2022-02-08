package model.entity.ad

data class CreateEditAdEntity(
    val ad: Ad,
    private val images: List<ByteArray>?
)