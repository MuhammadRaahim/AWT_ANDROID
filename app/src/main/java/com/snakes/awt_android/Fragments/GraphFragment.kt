package com.snakes.awt_android.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;


import java.util.ArrayList;
import java.util.List;

import com.snakes.awt_android.databinding.FragmentGraphBinding
import com.anychart.AnyChart.cartesian





class GraphFragment : Fragment() {

    private lateinit var binding: FragmentGraphBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(layoutInflater)

        val pie = AnyChart.line()
        pie.animation(true)
        pie.padding(10.0, 20.0, 5.0, 20.0)

        pie.crosshair().enabled(true)
        pie.crosshair()
            .yLabel(true) // TODO ystroke
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        pie.tooltip().positionMode(TooltipPositionMode.POINT)

        pie.title("Trend of Donation of the Most Popular Services of Allah wallay Trust.")

        pie.yAxis(0).title("Amount of Donation per month")
        pie.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData: MutableList<DataEntry> = ArrayList()
        seriesData.add(CustomDataEntry("Jan", 300, 20023, 242))
        seriesData.add(CustomDataEntry("Feb", 3242, 32444, 4424))
        seriesData.add(CustomDataEntry("Mar", 85, 6244, 51))
        seriesData.add(CustomDataEntry("Apr", 94444, 11844, 6445))
        seriesData.add(CustomDataEntry("May", 1044, 13.0, 125))
        seriesData.add(CustomDataEntry("Jun", 11444, 139, 104))
        seriesData.add(CustomDataEntry("Jul", 1444, 1850, 21044))
        seriesData.add(CustomDataEntry("Aug", 1044, 23, 203))
        seriesData.add(CustomDataEntry("Sep", 132, 24557, 1932))
        seriesData.add(CustomDataEntry("Oct", 12444, 1850, 1444))
        seriesData.add(CustomDataEntry("Nov", 32554, 1555, 9200))
        seriesData.add(CustomDataEntry("Dec", 455, 1135, 5900))


        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
        val series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }")

        val series1 = pie.line(series1Mapping)
        series1.name("DasterKhawan")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val series2 = pie.line(series2Mapping)
        series2.name("School")
        series2.hovered().markers().enabled(true)
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series2.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val series3 = pie.line(series3Mapping)
        series3.name("Services")
        series3.hovered().markers().enabled(true)
        series3.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series3.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        pie.legend().enabled(true)
        pie.legend().fontSize(13.0)
        pie.legend().padding(0.0, 0.0, 10.0, 0.0)

        binding.anyChartView.setChart(pie)
        return binding.root
    }

    public class CustomDataEntry internal constructor(
        x: String?,
        value: Number?,
        value2: Number?,
        value3: Number?
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }
    }

}