import java.math.BigDecimal

data class CheckoutPaymentRequestForm(

    val amount: BigDecimal,

    val currency: String,

    val orderId: String,

    val packages: List<ProductPackageForm>,

    val redirectUrls: RedirectUrls,
)
