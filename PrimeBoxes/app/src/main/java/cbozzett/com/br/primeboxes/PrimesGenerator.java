package cbozzett.com.br.primeboxes;

import java.util.Objects;
import java.util.Random;

/**
 * Created by cbozzett on 12/01/2017.
 */

public class PrimesGenerator {
    public interface PrimesDeliver {
        void primeDone(int prime, int index);
    }

    private PrimesDeliver mDeliverer;
    private int mNumThreads;

    private PrimesCache mCache;

    private int mDoneOrDoing = 0;
    private int mToDo = 0;
    private final Object lock = new Object();

    private boolean mRun;

    private Random mRandom = new Random();

    private Thread[] mThreads;

    private static final int STEPS[] = {
            Integer.MAX_VALUE & 0xff,
            Integer.MAX_VALUE & 0xffff,
            Integer.MAX_VALUE & 0xffffff,
            Integer.MAX_VALUE
    };

    private static final int CACHE_SIZE = 20000;

    public PrimesGenerator(int numThreads, PrimesDeliver deliverer) {
        mNumThreads = numThreads;
        mDeliverer = deliverer;

        mCache = new PrimesCache(CACHE_SIZE);

        mThreads = new Thread[mNumThreads];
        for (int i = 0; i < mNumThreads; i++) {
            mThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mRun) {
                        int index = -1;
                        synchronized (lock) {
                            if (mToDo > 0) {
                                mToDo--;
                                index = mDoneOrDoing;
                                mDoneOrDoing++;
                            }
                        }

                        if (index == -1) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            int prime = makePrime();
                            mDeliverer.primeDone(prime, index);
                        }
                    }
                }
            });
        }
    }

    public void start() {
        mRun = true;

        for (int i = 0; i < mNumThreads; i++) {
            mThreads[i].start();
        }
    }

    public void stop() {
        mRun = false;
    }

    public void makePrimes(int quantity) {
        if (quantity == 0) {
            return;
        }

        synchronized (lock) {
            mToDo += quantity;
        }
    }

    private int remainingNumbers() {
        return 0;
    }

    private int makePrime() {
        int step = STEPS[mRandom.nextInt(STEPS.length)];
        int candidate = mRandom.nextInt(step);

        if (candidate == 2 || candidate == 1) {
            return 2;
        } else if ((candidate & 0x1) == 0) {
            candidate++;
        }

        int startingPoint = 3;
        if (mCache.isReady()) {
            int n = mCache.getCloser(candidate);
            if (n < candidate) {
                startingPoint = n;
            } else {
                return n;
            }
        }

        while (mRun) {
            int mid = (candidate >> 1) + 1;

            int i;
            for (i = startingPoint; i < mid; i += 2) {
                if ((candidate % i) == 0) {
                    candidate += 2;
                    break;
                }
            }

            if (i >= mid) {
                break;
            }
        }

        return candidate;
    }
}
