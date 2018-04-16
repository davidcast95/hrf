package bobzone.hrf.humanresource.Helper

import bobzone.hrf.humanresource.Core.Model.CreationResponse
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlaceDetailsResponse
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Places.GoogleMapsPlacesResponse
import bobzone.hrf.humanresource.Core.Model.GoogleMap.Roads.GoogleMapsRoadsResponse
import bobzone.hrf.humanresource.Model.Address.AddressData
import bobzone.hrf.humanresource.Model.Address.AddressFetchData
import bobzone.hrf.humanresource.Model.BaseMessage
import bobzone.hrf.humanresource.Model.BaseResponse
import bobzone.hrf.humanresource.Model.BaseSingleMessage
import bobzone.hrf.humanresource.Model.BaseSingleResponse
import bobzone.hrf.humanresource.Model.Company.CompanyData
import bobzone.hrf.humanresource.Model.Currency.CurrencyData
import bobzone.hrf.humanresource.Model.Customer.CustomerData
import bobzone.hrf.humanresource.Model.Customer.CustomerGroupData
import bobzone.hrf.humanresource.Model.Customer.EmployeeData
import bobzone.hrf.humanresource.Model.Customer.EmployeeSingleData
import bobzone.hrf.humanresource.Model.InvoiceItem.InvoiceItemData
import bobzone.hrf.humanresource.Model.Leave.LeaveApplicationData
import bobzone.hrf.humanresource.Model.Leave.LeaveTypeData
import bobzone.hrf.humanresource.Model.Leave.RequestLeaveApplicationData
import bobzone.hrf.humanresource.Model.MetaData.MetaData
import bobzone.hrf.humanresource.Model.Offer.OfferData
import bobzone.hrf.humanresource.Model.OfferItem.OfferItemData
import bobzone.hrf.humanresource.Model.PriceList.PriceListData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceSingleData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderData
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderSingleData
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemData
import bobzone.hrf.humanresource.Model.SalesOrderItem.ItemDetailsData
import bobzone.hrf.humanresource.Model.SalesOrderItem.SalesOrderItemData
import bobzone.hrf.humanresource.Model.SalesTaxesAndCharge.SalesTaxesAndChargeData
import bobzone.hrf.humanresource.Model.Territory.TerritoryData
import bobzone.hrf.humanresource.Model.User.UserData
import bobzone.hrf.humanresource.Model.User.UserResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by davidwibisono on 12/24/17.
 */

interface APIHelper {
    //GOOGLE MAPS
    @GET("/maps/api/place/nearbysearch/json")
    fun googleMapsNearbySearch(@Query("location") location:String,@Query("radius") radius:String,@Query("type") type:String,@Query("keyword") keyword:String,@Query("key") key:String) : Call<GoogleMapsPlacesResponse>

    @GET("/maps/api/place/details/json")
    fun googgleMapsPlaceDetails(@Query("placeid") placeID:String,@Query("key") key:String): Call<GoogleMapsPlaceDetailsResponse>

    //GOOGLE MAPS ROADS
    @GET("/v1/nearestRoads")
    fun googgleMapsNearestRoads(@Query("points") points:String,@Query("key") key:String): Call<GoogleMapsRoadsResponse>

    @POST("/api/method/login")
    fun loginUser(@Query("usr") usr: String, @Query("pwd") pwd: String, @Query("device") device: String): Call<UserResponse>

    //METADATA
    @GET("api/method/salesforce.api.get_metadata")
    fun getMetaData(): Call<BaseSingleMessage<MetaData>>

    //SALES ORDER
    @POST("/api/resource/Sales Order")
    fun submitSalesOrder(@Body body:JsonObject):Call<CreationResponse>

    @PUT("/api/resource/Sales Order")
    fun updateSalesOrder(@Body body:JsonObject):Call<CreationResponse>

    @GET("/api/method/salesforce.api.get_sales_order")
    fun getSalesOrder(@Query("status") status:String, @Query("query") query:String, @Query("page") page:String): Call<BaseMessage<SalesOrderData>>

    @GET("/api/resource/Sales Order/{name}")
    fun getSpecifiedSalesOrder(@Path("name") name:String):Call<BaseSingleResponse<SalesOrderSingleData>>

    @GET("/api/resource/Sales Invoice Item?fields=[\"parent\"]")
    fun getSalesInvoiceFromItem(@Query("filters") filters:String):Call<BaseResponse<InvoiceItemData>>

    //INVOICE
    @POST("/api/resource/Sales Invoice")
    fun submitInvoice(@Body body:JsonObject):Call<CreationResponse>

    @PUT("/api/resource/Sales Invoice")
    fun updateInvoice(@Body body:JsonObject):Call<CreationResponse>

    @GET("/api/method/salesforce.api.get_sales_invoice")
    fun getSalesInvoice(@Query("status") status:String, @Query("query") query:String, @Query("page") page:String): Call<BaseMessage<InvoiceData>>

    @GET("/api/resource/Sales Invoice/{name}")
    fun getSpecifiedSalesInvoice(@Path("name") name:String):Call<BaseSingleResponse<InvoiceSingleData>>


    //LEAVE APPLICATION
    @GET("/api/resource/Leave Type?limit_page_length=100")
    fun getLeaveType(): Call<BaseResponse<LeaveTypeData>>
    @GET("/api/method/salesforce.api.get_leave_application")
    fun getLeaveApplication(@Query("status") status:String, @Query("query") query:String, @Query("page") page:String): Call<BaseMessage<LeaveApplicationData>>
    @GET("/api/method/salesforce.api.request_leave_application")
    //employee='',company='',leave_type='', from_date='', to_date='', status='Open', half_day=0, half_day_date='',docstatus=0,leave_approver=None):
    fun requestLeaveApplication(@Query("employee") employee:String,@Query("company") company:String,@Query("leave_type") leave_type:String, @Query("from_date") from_date:String,@Query("to_date") to_date:String,@Query("status") status:String,@Query("half_day") half_day:String,@Query("half_day_date") half_day_date:String,@Query("docstatus") docstatus:String,@Query("leave_approver") leave_approver:String):Call<BaseSingleMessage<RequestLeaveApplicationData>>
    @POST("/api/resource/Leave Application")
    fun submitLeaveApplication(@Body body:JsonObject):Call<CreationResponse>
    @PUT("/api/resource/Leave Application/{name}")
    fun updateLeaveApplication(@Path("name") name:String, @Body update:JsonObject):Call<CreationResponse>

    //USER
    @GET("/api/resource/User?limit_page_length=100")
    fun getUser():Call<BaseResponse<UserData>>

    //ADDRESS
    @POST("/api/resource/Address")
    fun submitAddress(@Body address:AddressData):Call<CreationResponse>
    @GET("/api/resource/Address?fields=[\"*\"]")
    fun getAddress(@Query("filters") filters:String):Call<BaseResponse<AddressFetchData>>

    //COMPANY
    @GET("/api/resource/Company?limit_page_length=100")
    fun getCompany(): Call<BaseResponse<CompanyData>>

    @GET("/api/resource/Currency?limit_page_length=100")
    fun getCurrency(): Call<BaseResponse<CurrencyData>>

    //EMPLOYEE
    @GET("/api/resource/Employee?fields=[\"*\"]")
    fun getEmployee(@Query("filters") filters:String, @Query("limit_start") start:String): Call<BaseResponse<EmployeeData>>

    @GET("/api/resource/Employee/{name}")
    fun getSpecifiedEmployee(@Path("name") name:String):Call<BaseSingleResponse<EmployeeSingleData>>


    //CUSTOMER
    @POST("/api/resource/Customer")
    fun submitCustomer(@Body body:JsonObject):Call<CreationResponse>

    @PUT("/api/resource/Customer")
    fun updateCustomer(@Body body:JsonObject):Call<CreationResponse>

    @GET("/api/resource/Customer?fields=[\"*\"]")
    fun getCustomer(@Query("filters") filters:String, @Query("limit_start") start:String): Call<BaseResponse<CustomerData>>

    @GET("/api/resource/Customer Group?limit_page_length=100")
    fun getCustomerGroup(): Call<BaseResponse<CustomerGroupData>>

    @GET("/api/resource/Territory?limit_page_length=100")
    fun getTerritory(): Call<BaseResponse<TerritoryData>>

    @GET("/api/resource/Customer/{name}")
    fun getSpecifiedCustomer(@Path("name") name:String):Call<BaseSingleResponse<CustomerData>>

    //ITEM
    @GET("/api/method/salesforce.api.get_item")
    fun getItem(@Query("is_sales_item") is_sales_item:String, @Query("is_stock_item") is_stock_item:String,@Query("ref") ref:String,@Query("page") page:String): Call<BaseMessage<ItemData>>
    @GET("/api/method/erpnext.stock.get_item_details.get_item_details")
    fun getItemDetails(@Query("args") args:String):Call<BaseSingleMessage<ItemDetailsData>>


    //SALES ORDER ITEM
    @GET("/api/resource/Sales Order Item?fields=[\"*\"]&limit_page_length=1000")
    fun getSalesOrderItem(@Query("filters") filters:String): Call<BaseResponse<SalesOrderItemData>>


    //SALES INVOICE ITEM
    @GET("/api/resource/Sales Invoice Item?fields=[\"*\"]&limit_page_length=1000")
    fun getSalesInvoiceItem(@Query("filters") filters:String): Call<BaseResponse<InvoiceItemData>>

    //TAX AND CHARGE
    @GET("/api/resource/Sales Taxes and Charges?fields=[\"*\"]&limit_page_length=1000")
    fun getSalesTaxesAndCharge(@Query("filters") filters:String): Call<BaseResponse<SalesTaxesAndChargeData>>

    //LEAD
    @GET("/api/method/salesforce.api.get_lead")
    fun getOffer(@Query("status") status:String, @Query("query") query:String, @Query("page") page:String): Call<BaseSingleMessage<OfferData>>
    @GET("/api/method/salesforce.api.get_lead_item")
    fun getLeadItem(@Query("lead_no") lead_no:String): Call<BaseSingleMessage<OfferItemData>>

    //PRICE LIST
    @GET("/api/resource/Price List?filters=[[\"Price List\",\"buying\",\"=\",\"0\"]]")
    fun getPriceList():Call<BaseResponse<PriceListData>>
}