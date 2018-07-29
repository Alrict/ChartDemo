package com.ihypnus.ihypnuscare.utils;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.utils
 * @author: llw
 * @Description: 柱状图管理类
 * @date: 2018/5/28 20:42
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class BarChartManager {
    private BarChart mBarChart;
    private YAxis yAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    public BarChartManager(BarChart barChart) {
        this.mBarChart = barChart;
        yAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
    }

    /**
     * 初始化BarChart
     */

    private void initBarChart(boolean showLegend, int type) {
        initBarChart(showLegend, type, null, null);
    }

    private void initBarChart(boolean showLegend, int type, List<Double> averageInp, List<Double> averageExp) {
        //背景颜色
        mBarChart.setBackgroundColor(Color.TRANSPARENT);
        //网格
        mBarChart.setDrawGridBackground(false);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);
        //最多显示的柱状图数量
        mBarChart.setMaxVisibleValueCount(31);
        //显示边界
        mBarChart.setDrawBorders(false);

//        mBarChart.animateX(3000, Easing.EasingOption.Linear);


        xAxis.setEnabled(true); // 轴线是否可编辑,默认true
        xAxis.setDrawLabels(true);  // 是否绘制标签,默认true
        xAxis.setDrawAxisLine(true);    // 是否绘制坐标轴,默认true
        xAxis.setDrawGridLines(false);   // 是否绘制网格线，默认true
        xAxis.setTextColor(Color.WHITE);
        //右下角的描述文本
        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);

//        yAxis.setSpaceTop(34);   // 设置最大值到图标顶部的距离占所有数据范围的比例。默认10，y轴独有
        yAxis.setLabelCount(8, false);
//        yAxis.setSpaceTop(15f);
        yAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);  // 标签绘制位置。默认再坐标轴外面

        rightAxis.setEnabled(false);

        // 轴颜色
        yAxis.setTextColor(Color.WHITE);  // Y轴标签字体颜色
        yAxis.setTextSize(10);    // 标签字体大小，dp，6-24之间，默认为10dp
        yAxis.setTypeface(null);    // 标签字体
        yAxis.setGridColor(Color.TRANSPARENT);    // 网格线颜色，默认GRAY
//        yAxis.setGridLineWidth(1);    // 网格线宽度，dp，默认1dp
        yAxis.setAxisLineColor(Color.WHITE);  // 坐标轴颜色，默认GRAY.测试到一个bug，假如左侧线只有1dp，
        // 那么如果x轴有线且有网格线，当刻度拉的正好位置时会覆盖到y轴的轴线，变为X轴网格线颜色，
        // 解决办法是，要么不画轴线，要么就是坐标轴稍微宽点
        xAxis.setAxisLineColor(Color.WHITE);
        if (averageInp != null && averageExp != null) {
            yAxis.setValueFormatter(new MyAxisValueFormatter(type, averageInp, averageExp));
        } else {
            yAxis.setValueFormatter(new MyAxisValueFormatter(type));
        }


        // X轴更多属性
//        xAxis.setLabelRotationAngle(90);   // 标签倾斜
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // X轴绘制位置，默认是顶部

        yAxis.setDrawZeroLine(false);    // 绘制值为0的轴，默认false,其实比较有用的就是在柱形图，当有负数时，显示在0轴以下，其他的图这个可能会看到一些奇葩的效果
        yAxis.setZeroLineWidth(10);  // 0轴宽度
        yAxis.setZeroLineColor(Color.WHITE);   // Y坐标轴颜色
        yAxis.setAxisMinimum(0);
        yAxis.resetAxisMaximum();
        if (type == 1) {
            //睡眠分数范围
            yAxis.setAxisMaximum(100);
        } else if (type == 3) {
            //使用时长范围
            yAxis.setAxisMaximum(24);
        } else if (type == 5) {
            //90%吸气压范围
            yAxis.setAxisMaximum(120);
        } else if (type == 7) {
            //ahi范围
            yAxis.setAxisMaximum(40);
        }

        //图例 标签 设置
        Legend legend = mBarChart.getLegend();
        legend.setEnabled(showLegend);    // 是否绘制图例
        legend.setTextColor(Color.WHITE);    // 图例标签字体颜色，默认BLACK
        legend.setTextSize(12); // 图例标签字体大小[6,24]dp,默认10dp
        legend.setTypeface(null);   // 图例标签字体
        legend.setWordWrapEnabled(false);    // 当图例超出时是否换行适配，这个配置会降低性能，且只有图例在底部时才可以适配。默认false
        legend.setMaxSizePercent(1f); // 设置，默认0.95f,图例最大尺寸区域占图表区域之外的比例
//        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);    // 图例显示位置，已弃用
        legend.setForm(Legend.LegendForm.CIRCLE);   // 设置图例的形状，SQUARE, CIRCLE 或者 LINE
        legend.setFormSize(8); // 图例图形尺寸，dp，默认8dp
        legend.setXEntrySpace(6);  // 设置水平图例间间距，默认6dp
        legend.setYEntrySpace(0);  // 设置垂直图例间间距，默认0
        legend.setFormToTextSpace(5);    // 设置图例的标签与图形之间的距离，默认5dp
        legend.setWordWrapEnabled(true);   // 图标单词是否适配。只有在底部才会有效，
        legend.setCustom(new LegendEntry[]{
                new LegendEntry("不太好", Legend.LegendForm.CIRCLE, 10, 5, null, Color.parseColor("#0093dd")),
                new LegendEntry("还不错", Legend.LegendForm.CIRCLE, 10, 5, null, Color.parseColor("#e67817")),
                new LegendEntry("非常好", Legend.LegendForm.CIRCLE, 10, 5, null, Color.parseColor("#85c226"))}); // 这个应该是之前的setCustom(int[] colors, String[] labels)方法
        // 这个方法会把前面设置的图例都去掉，重置为指定的图例。
//        legend.resetCustom();   // 去掉上面方法设置的图例，然后之前dataSet中设置的会重新显示。
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);


        //XY轴的设置
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setLabelRotationAngle(90);   // 标签倾斜
        xAxis.setGranularity(1f);

        // 轴值转换显示
    /*    xAxis.setValueFormatter(new IAxisValueFormatter() { // 与上面值转换一样，这里就是转换出轴上label的显示。也有几个默认的，不多说了。
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value + "℃";
            }
        });*/

        //保证Y轴从0开始，不然会上移一点
        yAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
    }

    /**
     * 展示柱状图(一条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param label
     */
    public void showBarChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, boolean showLegend, int type, int modelType) {
        initBarChart(showLegend, type);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < xAxisValues.size(); i++) {
            entries.add(new BarEntry(xAxisValues.get(i), yAxisValues.get(i)));
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, label);
        barDataSet.setValueTextSize(9f);
        barDataSet.setFormSize(15.f);
        barDataSet.setValueTextColor(Color.WHITE);
        ArrayList<Integer> colors = new ArrayList();
        for (int i = 0; i < yAxisValues.size(); i++) {
            Float aFloat = yAxisValues.get(i);
            switch (modelType) {
                case 0:
                    //睡眠分数
                    if (aFloat >= 80) {
                        colors.add(Color.parseColor("#85c226"));
                    } else if (aFloat >= 60) {
                        colors.add(Color.parseColor("#e67817"));
                    } else {
                        colors.add(Color.parseColor("#e67817"));
                    }
                    break;

                case 1:
                    //使用时长
                    if (aFloat >= 4) {
                        colors.add(Color.parseColor("#3bb3c3"));
                    } else {
                        colors.add(Color.parseColor("#e67817"));
                    }
                    break;
                case 3:
                    //AHI
                    colors.add(Color.parseColor("#85c226"));
                    break;
                default:
                    colors.add(Color.parseColor("#85c226"));
                    break;
            }

        }
        barDataSet.setColors(colors);
        // Y轴更多属性
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);  // 设置dataSet应绘制在Y轴的左轴还是右轴，默认LEFT
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet);
        BarData data = new BarData(dataSets);
        //设置X轴的刻度数
        xAxis.setLabelCount(xAxisValues.size() - 1, false);

        mBarChart.setData(data);
        mBarChart.invalidate();
        //设置动画效果
        mBarChart.animateY(3000, Easing.EasingOption.Linear);
    }

    /**
     * 展示柱状图(多条)
     *
     * @param xAxisValues
     * @param yAxisValues
     */
    public void showStackedBarChart(List<Float> xAxisValues, ArrayList<BarEntry> yAxisValues, String labels, int type, List<Double> averageInp, List<Double> averageExp) {
        initBarChart(false, type, averageInp, averageExp);
        //设置X轴的刻度数
        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        BarDataSet set1;
        if (mBarChart.getData() != null &&
                mBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yAxisValues);
            //设置是否显示柱子上面的数值
            set1.setDrawValues(true);
            mBarChart.getData().notifyDataChanged();
            mBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yAxisValues, "90%压力");
            set1.setDrawIcons(false);
            set1.setLabel(labels);
            set1.setColors(getColors());
            //设置是否显示柱子上面的数值
            set1.setDrawValues(true);
            set1.setStackLabels(new String[]{"吸气压力", "呼气压力"});
            set1.setValueTextColor(Color.WHITE);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            mBarChart.setData(data);
        }

        mBarChart.setFitBars(true);
        mBarChart.invalidate();
    /*    // Y轴更多属性
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);  // 设置dataSet应绘制在Y轴的左轴还是右轴，默认LEFT
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);

        mBarChart.setData(data);
        mBarChart.invalidate();*/
        //设置动画效果
        mBarChart.animateY(3000, Easing.EasingOption.Linear);

    }

    private int[] getColors() {

        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];
        colors[0] = ColorTemplate.MATERIAL_COLORS[1];
        colors[1] = ColorTemplate.MATERIAL_COLORS[0];

        return colors;
    }


    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        yAxis.setAxisMaximum(max);
        yAxis.setAxisMinimum(min);
        yAxis.setLabelCount(labelCount, false);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        mBarChart.invalidate();
    }

    /**
     * 设置X轴的值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setXAxis(float max, float min, int labelCount) {
        xAxis.setAxisMaximum(max);
        xAxis.setAxisMinimum(min);
        xAxis.setLabelCount(labelCount, false);

        mBarChart.invalidate();
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHightLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        yAxis.addLimitLine(hightLimit);
        mBarChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        yAxis.addLimitLine(hightLimit);
        mBarChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }
}
