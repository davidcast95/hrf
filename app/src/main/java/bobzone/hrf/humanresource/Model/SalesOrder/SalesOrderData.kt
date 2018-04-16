package bobzone.hrf.humanresource.Model.SalesOrder

import bobzone.hrf.humanresource.Model.Company.CompanyData
import bobzone.hrf.humanresource.Model.Currency.CurrencyData
import bobzone.hrf.humanresource.Model.Customer.CustomerGroupData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import com.google.gson.annotations.SerializedName

/**
 * Created by davidwibisono on 3/6/18.
 */
open class SalesOrderData {
    @SerializedName("name")
    var name = ""
    @SerializedName("company")
    var company = ""
    @SerializedName("transaction_date")
    var transaction_date = ""
    @SerializedName("delivery_date")
    var delivery_date = ""
    @SerializedName("customer_group")
    var customer_group = ""
    @SerializedName("currency")
    var currency = ""
    @SerializedName("status")
    var status = ""
    @SerializedName("customer")
    var customer = ""
    @SerializedName("selling_price_list")
    var selling_price_list = ""
    @SerializedName("grand_total")
    var grand_total = 0.0
    @SerializedName("rounded_total")
    var rounded_total = 0.0


    @SerializedName("total_taxes_and_charges")
    var total_taxes_and_charges = 0.0

    @SerializedName("additional_discount_percentage")
    var additional_discount_percentage = 0.0
    @SerializedName("apply_discount_on")
    var apply_discount_on = ""

    @SerializedName("discount_amount")
    var discount_amount = 0.0
}