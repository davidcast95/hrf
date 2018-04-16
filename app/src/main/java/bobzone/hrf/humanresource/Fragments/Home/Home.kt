package bobzone.hrf.humanresource.Fragments.Home

import android.app.ActionBar
import android.graphics.Color
import android.graphics.DashPathEffect
import android.util.Log
import bobzone.hrf.humanresource.Core.Base.Callback.BaseSingleMessageCallBack
import bobzone.hrf.humanresource.Core.Base.SideMenu.MenuFragment
import bobzone.hrf.humanresource.Core.Helper.Helper
import bobzone.hrf.humanresource.Model.MetaData.MetaData
import bobzone.hrf.humanresource.Model.MetaData.NetSalesData
import bobzone.hrf.humanresource.Model.SalesOrder.InvoiceStatus
import bobzone.hrf.humanresource.Model.SalesOrder.SalesOrderStatus
import bobzone.hrf.humanresource.Model.User.UserModel
import bobzone.hrf.humanresource.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate.rgb
import java.util.ArrayList
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import bobzone.hrf.humanresource.Core.Base.Loading.LoadingDelegate


/**
 * Created by davidwibisono on 12/24/17.
 */

class Home : MenuFragment() {

    var pieChartSO: PieChart? = null
    var pieChartINV: PieChart? = null
    var lineChart:LineChart? = null
    var loading:RelativeLayout? = null

    override fun getView(inflater: LayoutInflater, container: ViewGroup): View {
        val v = inflater.inflate(R.layout.fragment_home,container,false)
        pieChartSO = v.findViewById(R.id.pieChartSalesOrder)
        pieChartINV = v.findViewById(R.id.pieChartInvoice)
        lineChart = v.findViewById(R.id.lineChart)
        loading = v.findViewById(R.id.loading)
        val l = loading
        if (l is RelativeLayout) l.visibility = View.VISIBLE

        fetchMetaData()
        return v
    }



    override fun getTitleMenu(): String {
        return getString(R.string.home)
    }

    fun setupPieChart() {
        val pcSO = pieChartSO
        if (pcSO is PieChart) {
            pcSO.description.isEnabled = false
            pcSO.isDrawHoleEnabled = true
            pcSO.setHoleColor(Color.WHITE)
            setupDataPieChatSO(pcSO)
        }

        val pcINV = pieChartINV
        if (pcINV is PieChart) {
            pcINV.description.isEnabled = false
            pcINV.isDrawHoleEnabled = true
            pcINV.setHoleColor(Color.WHITE)
            setupDataPieChatINV(pcINV)
        }
    }



    fun setupDataPieChatSO(chart:PieChart) {

        val pieEntries = ArrayList<PieEntry>()
        val metaData = MetaData.instance
        pieEntries.add(PieEntry(metaData.sales_order.count.to_deliver_and_bill.toFloat(), ""))
        pieEntries.add(PieEntry(metaData.sales_order.count.to_bill.toFloat(),""))
        pieEntries.add(PieEntry(metaData.sales_order.count.to_deliver.toFloat(), ""))

        Helper.instance.setTextView(activity,R.id.to_deliver_and_bill_count,"${metaData.sales_order.count.to_deliver_and_bill} orders")
        Helper.instance.setTextView(activity,R.id.to_bill_count,"${metaData.sales_order.count.to_bill} orders")
        Helper.instance.setTextView(activity,R.id.to_deliver_count,"${metaData.sales_order.count.to_deliver} orders")

        var COLOR = mutableListOf<Int>(rgb("#B5463A"), rgb("#E8A63A"), rgb("#21826B"))
        val dataSet = PieDataSet(pieEntries, "")
        dataSet.colors = COLOR
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextColor(R.color.White)
        data.setValueTextSize(6f)

        data.setValueTextSize(0f)
        chart.legend.isEnabled = false
        chart.data = data
        chart.contentDescription
        chart.description.isEnabled = false
        chart.setEntryLabelTextSize(20f)
        chart.setEntryLabelColor(Color.BLACK)
        chart.invalidate()
    }

    fun setupDataPieChatINV(chart:PieChart) {

        val metaData = MetaData.instance
        val pieEntries = ArrayList<PieEntry>()
        pieEntries.add(PieEntry(metaData.invoice.count.overdue.toFloat(), ""))
        pieEntries.add(PieEntry(metaData.invoice.count.unpaid.toFloat(), ""))
        pieEntries.add(PieEntry(metaData.invoice.count.paid.toFloat(), ""))

        Helper.instance.setTextView(activity,R.id.overdue_count,"${metaData.invoice.count.overdue} invoices")
        Helper.instance.setTextView(activity,R.id.unpaid_count,"${metaData.invoice.count.unpaid} invoices")
        Helper.instance.setTextView(activity,R.id.paid_count,"${metaData.invoice.count.paid} invoices")

        var COLOR = mutableListOf<Int>(rgb("#B5463A"), rgb("#E8A63A"), rgb("#21826B"))
        val dataSet = PieDataSet(pieEntries, "")
        dataSet.colors = COLOR
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextColor(R.color.White)
        data.setValueTextSize(11f)

        data.setValueTextSize(0f)
        chart.legend.isEnabled = false
        chart.data = data
        chart.contentDescription
        chart.description.isEnabled = false
        chart.setEntryLabelTextSize(20f)
        chart.setEntryLabelColor(Color.BLACK)
        chart.invalidate()
    }

    fun setupLineChart() {
        val metaData = MetaData.instance
        val mChart = lineChart
        if (mChart is LineChart) {
            mChart.setDrawGridBackground(false)

            // no description text
            mChart.getDescription().setEnabled(false)

            // enable touch gestures
            mChart.setTouchEnabled(true)

            // enable scaling and dragging
            mChart.setDragEnabled(true)
            mChart.setScaleEnabled(true)
            // mChart.setScaleXEnabled(true);
            // mChart.setScaleYEnabled(true);

            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(false)


            // x-axis limit line
            val llXAxis = LimitLine(10f, "")
            llXAxis.lineWidth = 4f
            llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            llXAxis.textSize = 10f

            val xAxis = mChart.getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            val xLabel = ArrayList<String>()
            for (i in 0 until metaData.daily_net_sales.size) {
                xLabel.add(metaData.daily_net_sales[i].posting_date)
            }
            xAxis.valueFormatter = IAxisValueFormatter { value, axis ->
                if (((value * 10) % 10) == 5f) {
                    ""
                } else {
                    xLabel.get(value.toInt())
                }
            }

            setData(mChart)

        }
    }

    private fun setData(mChart: LineChart) {
        val metaData = MetaData.instance
        val values = ArrayList<Entry>()

        for (i in 0 until metaData.daily_net_sales.size) {
            values.add(Entry(i.toFloat(), metaData.daily_net_sales[i].net_sales.toFloat()))
        }

        val set1: LineDataSet

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = mChart.data.dataSets[0] as LineDataSet
            set1.values = values
            mChart.getData().notifyDataChanged()
            mChart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "Net Sales")

            set1.setDrawIcons(false)

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f)
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.color = R.color.colorPrimary
            set1.setCircleColor(R.color.colorPrimary)
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.valueTextSize = 9f
            set1.setDrawFilled(true)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f


            set1.fillColor = R.color.colorPrimary2

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the datasets

            // create a data object with the datasets
            val data = LineData(dataSets)

            // set data
            mChart.setData(data)
        }
    }

    //API
    fun fetchMetaData() {
        val cookieJar = UserModel.instance.loadCookieJar(context)
        val api = Helper.instance.getAPIWithCookie(context, cookieJar)
        val callMetaData = api.getMetaData()
        callMetaData.enqueue(object: BaseSingleMessageCallBack<MetaData>(context) {
            override fun responseData(data: MetaData) {
                MetaData.instance = data

                setupPieChart()
                if (data.daily_net_sales.size > 0)
                    setupLineChart()
                val l = loading
                if (l is RelativeLayout) l.visibility = View.GONE
            }

            override fun failed(error: Throwable) {

            }
        })
    }

    fun refresh() {
        setupPieChart()
        setupLineChart()
    }
}