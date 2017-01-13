package cbozzett.com.br.primeboxes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbozzett on 12/01/2017.
 */

public class NumberBoxAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> mItems;
    private int mMaxItems;

    private static final int UNSET_ITEM = -1;
    private static float TEXT_SMALL = 10.0f;
    private static float TEXT_MEDIUM = 14.0f;
    private static float TEXT_BIG = 18.0f;

    public static final int MAXIMUM_NUMBER_OF_ITEMS = 32767;

    public NumberBoxAdapter(Context context) {
        init(context, MAXIMUM_NUMBER_OF_ITEMS);
    }

    public NumberBoxAdapter(Context context, int maxItems) {
        init(context, maxItems);
    }

    private void init(Context context, int maxItems) {
        mContext = context;
        mItems = new ArrayList<>();
        mMaxItems = maxItems;
    }

    public int addItems(int quantity) {
        int n = mMaxItems - mItems.size();

        if (n <= 0) {
            return 0;
        } else if (n > quantity) {
            n = quantity;
        }

        ArrayList<Integer> nums = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            nums.add(UNSET_ITEM);
        }
        mItems.addAll(nums);

        return n;
    }

    public int addItems(List<Integer> items) {
        int n = items.size() + mItems.size();

        if (n > mMaxItems) {
            return 0;
        }

        mItems.addAll(items);
        return items.size();
    }

    public int getAvailableSlots() {
        return mMaxItems - mItems.size();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public boolean isItemSet(int position) {
        return mItems.get(position) != UNSET_ITEM;
    }

    public void setItem(int position, int value) {
        mItems.set(position, value);
        notifyDataSetChanged();
    }

    public void setItem(View v, int position, int value) {
        mItems.set(position, value);
        setBox((TextView)v, value);
    }

    private void setBox(TextView textView, int number) {
        String text = "" + number;
        if (text.length() < 5) {
            textView.setTextSize(TEXT_BIG);
        } else if (text.length() > 9) {
            textView.setTextSize(TEXT_SMALL);
        } else {
            textView.setTextSize(TEXT_MEDIUM);
        }
        textView.setText(text);
        textView.setBackgroundResource(R.color.boxSet);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView)convertView;

        if (textView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            textView = (TextView)inflater.inflate(R.layout.number_box, parent, false);
        }

        int num = mItems.get(position);
        if (num == -1) {
            textView.setBackgroundResource(R.color.boxNotSet);
            textView.setText("");
        } else {
            setBox(textView, num);
        }

        return textView;
    }
}
