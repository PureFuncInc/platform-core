import java.math.BigDecimal

data class ProductPackageForm(

    val id: String,

    val amount: BigDecimal,

    val products: List<ProductForm>,
)
