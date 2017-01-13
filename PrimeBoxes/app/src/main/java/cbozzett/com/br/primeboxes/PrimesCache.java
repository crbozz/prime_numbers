package cbozzett.com.br.primeboxes;

import android.util.Log;

/**
 * Created by cbozzett on 12/01/2017.
 */

public class PrimesCache {
    private int mCache[];
    private int mDone;

    public PrimesCache(int size) {
        mCache = new int[size];
        mDone = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                makeCache();
            }
        }).start();
    }

    int size() {
        return mCache.length;
    }

    public boolean isReady() {
        return mDone == mCache.length;
    }

    public int getCloser(int number) {
        if (mCache.length != mDone) {
            return -1;
        }

        int start = 0;
        int end = mCache.length - 1;
        while (end > start) {
            int mid = (end + start) / 2;
            if (mCache[mid] < number) {
                start = mid + 1;
            } else if (mCache[mid] > number) {
                end = mid - 1;
            } else {
                return number;
            }
        }

        start++;
        if (start >= mCache.length) {
            start = mCache.length - 1;
        }

        return mCache[start];
    }

    private void makeCache() {
        int table[] = new int[1024];

        for (int i = 0; i < table.length; i++) {
            table[i] = 0;
        }

        int start = 2;
        int end = table.length;
        for (int i = 2; i < end; i++) {
            if (table[i] == 0) {
                mCache[mDone] = i;
                mDone++;
                if (mDone >= mCache.length) {
                    break;
                }
                table[i] = 1;
                for (int j = i * i; j < end; j += i) {
                    table[j] = 1;
                }
            }
        }

        while (mDone < mCache.length) {
            for (int i = 0; i < table.length; i++) {
                table[i] = 0;
            }

            start = end;
            end += table.length;
            for (int i = 0; i < table.length; i++) {
                if (table[i] == 0) {
                    int candidate = i + start;
                    int mid = candidate / 2;
                    int j;

                    for (j = 0; j < mDone; j++) {
                        if (mCache[j] > mid) {
                            j = mDone;
                        } else if ((candidate % mCache[j]) == 0){
                            break;
                        }
                    }

                    if (j < mDone) {
                        for (int k = i; k < table.length; k += mCache[j]) {
                            table[k] = 1;
                        }
                    } else {
                        mCache[mDone] = candidate;
                        ++mDone;
                        if (mDone >= mCache.length) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
