package com.ihypnus.ihypnuscare.utils;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
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

    private void initBarChart(boolean showLegend, int type, int endDay, int maxDay) {
        initBarChart(showLegend, type, endDay, maxDay, null, null);
    }

    private void initBarChart(boolean showLegend, int type, int endDay, int maxDay, List<Double> averageInp, List<Double> averageExp) {
        //背景颜色
        mBarChart.setBackgroundColor(Color.TRANSPARENT);
        //网格
        mBarChart.setDrawGridBackground(false);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);
        //右下角的描述文本
        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);
        //显示边界
        mBarChart.setDrawBorders(false);
        //设置动画效果
        mBarChart.animateY(3000, Easing.Linear);

        //折线图例 标签 设置
        Legend legend = mBarChart.getLegend();
        legend.setEnabled(false);


        //XY轴的设置
        //X轴设置显示位置在底部
        rightAxis.setEnabled(false);
        rightAxis.setMinWidth(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);   // 是否绘制网格线，默认true
        xAxis.setTextColor(Color.WHITE);
        yAxis.setDrawAxisLine(true);//有Y轴线
        yAxis.setDrawZeroLine(false);    // false 不绘制值为0的轴，默认false,其实比较有用的就是在柱形图，当有负数时，显示在0轴以下，其他的图这个可能会看到一些奇葩的效果
//        yAxis.setZeroLineWidth(1);  // 0轴宽度
        yAxis.setZeroLineColor(Color.WHITE);   // Y坐标轴颜色
        yAxis.setGridColor(Color.TRANSPARENT);    // 网格线颜色，默认GRAY
//        yAxis.setAxisMinimum(0);
//        yAxis.resetAxisMaximum();
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
        yAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);//设置间隔刻度

        //处理X轴坐标显示刻度
        xAxis.setValueFormatter(new XAxisValueFormatter(endDay, maxDay));

        if (averageInp != null && averageExp != null) {
            yAxis.setValueFormatter(new MyAxisValueFormatter(type, averageInp, averageExp));
        } else {
            yAxis.setValueFormatter(new MyAxisValueFormatter(type, maxDay));
        }
    }

    /**
     * 初始化LineChart
     */
    private void initLineChart(int type) {
        //背景颜色
        mBarChart.setBackgroundColor(Color.TRANSPARENT);
        //网格
        mBarChart.setDrawGridBackground(false);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);
        //右下角的描述文本
        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);
        //显示边界
        mBarChart.setDrawBorders(false);
        //设置动画效果
        mBarChart.animateY(3000, Easing.EasingOption.Linear);

        //折线图例 标签 设置
        Legend legend = mBarChart.getLegend();
        legend.setEnabled(false);


        //XY轴的设置
        //X轴设置显示位置在底部
        rightAxis.setEnabled(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);   // 是否绘制网格线，默认true
        xAxis.setTextColor(Color.WHITE);
        yAxis.setDrawZeroLine(false);    // 绘制值为0的轴，默认false,其实比较有用的就是在柱形图，当有负数时，显示在0轴以下，其他的图这个可能会看到一些奇葩的效果
        yAxis.setZeroLineWidth(10);  // 0轴宽度
        yAxis.setZeroLineColor(Color.WHITE);   // Y坐标轴颜色
        yAxis.setGridColor(Color.TRANSPARENT);    // 网格线颜色，默认GRAY
        yAxis.setAxisMinimum(0);
        yAxis.resetAxisMaximum();
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
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        yAxis.setAxisMinimum(0f);
    }

    /**
     * 展示柱状图(一条)
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param label
     */
    public void showBarChart(List<Float> xAxisValues, List<Float> yAxisValues, String label, boolean showLegend, int type, int endDay, int maxDay, int modelType) {
        initBarChart(showLegend, type, endDay, maxDay);
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
        yAxis.setAxisMinimum(0f);
        mBarChart.setFitBars(true);
        mBarChart.setData(data);
        mBarChart.invalidate();
        //设置动画效果
        mBarChart.animateY(3000, Easing.Linear);
        float axisMinimum = yAxis.getAxisMinimum();
        boolean axisMinCustom = yAxis.isAxisMinCustom();
        LogOut.d("llw", "barchart最小Y值:" + axisMinimum + ",最小值是否生效:" + axisMinCustom);
    }

    /**
     * 展示柱状图(多条)
     *
     * @param xAxisValues
     * @param yAxisValues
     */
    public void showStackedBarChart(List<Float> xAxisValues, ArrayList<BarEntry> yAxisValues, String labels, int type, int endDay, int maxDay, List<Double> averageInp, List<Double> averageExp) {
        initBarChart(false, type, endDay, maxDay, averageInp, averageExp);
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

//        mBarChart.setFitBars(true);
//        mBarChart.invalidate();

        //设置动画效果
//        mBarChart.animateY(3000, Easing.Linear);

    }

    /**
     * 并列显示多条柱状图
     *
     * @param xAxisValues
     * @param yAxisValues
     * @param type
     * @param averageInp
     * @param averageExp
     * @param labels
     * @param colours
     */
    public void showBarChart(List<Float> xAxisValues, List<List<Float>> yAxisValues, int type, int endDay, int maxDay, List<Double> averageInp, List<Double> averageExp, List<String> labels, List<Integer> colours) {
        initBarChart(false, type, endDay, maxDay, averageInp, averageExp);
//        initLineChart(type);
        BarData data = new BarData();
        for (int i = 0; i < yAxisValues.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yAxisValues.get(i).size(); j++) {

                entries.add(new BarEntry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));

            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(Color.WHITE);
            barDataSet.setValueTextSize(8f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            data.addDataSet(barDataSet);
        }

        int amount = yAxisValues.size(); //需要显示柱状图的类别 数量
        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet

//        float groupSpace = 0.12f; //柱状图组之间的间距
//        float barSpace =  0.02f; // x4 DataSet
//        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.02) * 4 + 0.12 = 1.00 即100% 按照百分百布局
        xAxis.setLabelCount(amount - 1, false);
//        柱状图宽度
        data.setBarWidth(barWidth);
        //(起始点、柱状图组间距、柱状图之间间距)
        data.groupBars(0, groupSpace, barSpace);
        mBarChart.setFitBars(true);
        mBarChart.setData(data);
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
