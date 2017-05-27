package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StockChartActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int STOCK_HISTORY_LOADER = 1;

    public final int NUMBER_OF_HISTORY_POINTS = 20; //Increase this for more history in the chart.

    public String symbolClicked;

    @BindView(R.id.line_chart)
    public LineChart mLineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_chart);

        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) symbolClicked = bundle.getString(MainActivity.EXTRA_SYMBOL);

        getSupportLoaderManager().initLoader(STOCK_HISTORY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                Contract.StockHistory.URI,
                null,
                Contract.Quote.COLUMN_SYMBOL + "=?",
                new String[]{symbolClicked},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        LineData lineData = prepareDataForChart(data);
        drawDataOnChart(lineData);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLineChart.setData(null);
        mLineChart.invalidate();
    }

    public LineData prepareDataForChart(Cursor data){
        List<Entry> valsComp1 = new ArrayList<Entry>();
        List<String> xLabels = new ArrayList<String>();

        long i = 0;
        while (data.moveToNext() && i<NUMBER_OF_HISTORY_POINTS){
            Entry entry = new Entry(i, data.getFloat(Contract.StockHistory.POSITION_PRICE));
            Timber.d(data.getString(Contract.StockHistory.POSITION_SYMBOL));
            valsComp1.add(entry);
            xLabels.add(convertMillisToStringDate(data.getLong(Contract.StockHistory.POSITION_TIMESTAMP)));
            i++;
        }

        // the labels that should be drawn on the XAxis
        final String[] arrLabels = xLabels.toArray(new String[xLabels.size()]);
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return arrLabels[(int) value];
            }
        };

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setLabelRotationAngle(45.0f);
        xAxis.setValueFormatter(formatter);
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        LineDataSet setComp1 = new LineDataSet(valsComp1, "Stock Price");
        dataSets.add(setComp1);
        LineData lineData = new LineData(dataSets);
        lineData.setValueTextColor(Color.WHITE);
        return lineData;
    }

    public void drawDataOnChart(LineData lineData){
        mLineChart.setData(lineData);
        mLineChart.getXAxis().setTextSize(10f);
        mLineChart.getXAxis().setTextColor(Color.WHITE);
        mLineChart.getAxisLeft().setTextColor(Color.WHITE);
        mLineChart.getAxisRight().setTextColor(Color.WHITE);
        mLineChart.setDescription(null);
        mLineChart.setVisibleYRangeMinimum(0, YAxis.AxisDependency.LEFT);
        mLineChart.setVisibleYRangeMinimum(0, YAxis.AxisDependency.RIGHT);
        mLineChart.invalidate();
    }

    public String convertMillisToStringDate(long millis){
        Date date = new Date(millis);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
