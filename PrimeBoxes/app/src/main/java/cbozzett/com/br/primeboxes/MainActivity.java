package cbozzett.com.br.primeboxes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements PrimesGenerator.PrimesDeliver {
    private NumberBoxAdapter mAdapter;
    private PrimesGenerator mPrimesGenerator;

    private static final int BOXES_GROWTH_FACTOR = 100;
    private static final int MAXIMUM_NUMBER_OF_BOXES = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new NumberBoxAdapter(this);
        mAdapter.addItems(BOXES_GROWTH_FACTOR);

        mPrimesGenerator = new PrimesGenerator(2, this);
        mPrimesGenerator.makePrimes(BOXES_GROWTH_FACTOR);

        final GridView theGrid = (GridView)findViewById(R.id.the_grid);
        theGrid.setAdapter(mAdapter);

        theGrid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int first, int visible, int total) {
                if (first + visible >= total) {
                    int added = mAdapter.addItems(BOXES_GROWTH_FACTOR);
                    if (added > 0) {
                        mAdapter.notifyDataSetChanged();
                        mPrimesGenerator.makePrimes(added);
                    }

                    Log.d("MAIN", "Bottom! Remaining " + mAdapter.getAvailableSlots());
                }
            }
        });
    }

    @Override
    public void primeDone(final int prime, final int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setItem(index, prime);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mPrimesGenerator.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mPrimesGenerator.stop();
    }
}
