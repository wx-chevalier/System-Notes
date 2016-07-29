package wx.algorithm.numbertheory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by apple on 16/7/29.
 */
public class SquareRootsTest {

    //用于测试类
    SquareRoots squareRoots;

    @Before
    public void setUp() {

        squareRoots = new SquareRoots(4.695);

    }

    @Test
    public void testBabylonian() {

        for (int i = 0; i < 10000; i++) {
            Assert.assertEquals(2.166795861438391, squareRoots.Babylonian(), 0.000001);

        }

    }

    @Test
    public void testTSqrt() {

        for (int i = 0; i < 10000; i++) {

            Assert.assertEquals(2.166795861438391, squareRoots.TSqrt(), 0.000001);

        }
    }

    @Test
    public void testFastInverseSquareRoot() {

        for (int i = 0; i < 10000; i++) {

            Assert.assertEquals(2.1667948388864198, squareRoots.FastInverseSquareRoot(), 0.000001);

        }
    }

    @Test
    public void benchMark() {

        //巴比伦算法计时器
        long babylonianTimer = 0;

        //级数逼近算法计时器
        long tSqrtTimer = 0;

        //平方根倒数速算法计时器
        long fastInverseSquareRootTimer = 0;

        //随机数生成器
        Random r = new Random();

        for (int i = 0; i < 100000; i++) {

            double value = r.nextDouble() * 1000;

            SquareRoots squareRoots = new SquareRoots(value);

            long start, stop;

            start = System.currentTimeMillis();

            squareRoots.Babylonian();

            babylonianTimer += (System.currentTimeMillis() - start);

            start = System.currentTimeMillis();

            squareRoots.TSqrt();

            tSqrtTimer += (System.currentTimeMillis() - start);

            start = System.currentTimeMillis();

            squareRoots.FastInverseSquareRoot();

            fastInverseSquareRootTimer += (System.currentTimeMillis() - start);

        }


        System.out.println("巴比伦算法:" + babylonianTimer);

        System.out.println("级数逼近算法:" + tSqrtTimer);

        System.out.println("平方根倒数速算法:" + fastInverseSquareRootTimer);


    }
}
