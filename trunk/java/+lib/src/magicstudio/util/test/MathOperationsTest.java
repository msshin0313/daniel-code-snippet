package magicstudio.util.test;
import org.junit.Test;
import static junit.framework.Assert.*;
import static magicstudio.util.MathOperation.*;

public class MathOperationsTest {
    @Test public void testCorrelation() {
        double[][] matrix = {{57.1, 16.8, 86.1, 40.8, 90.3}, {2.42, .07, .26, 1.42, 3.11}, {39, 32, 69, 49, 74}};
        double[][] corMatrix = correlation(matrix);
        assertTrue(approxEquals(1d, corMatrix[0][0]));
        assertTrue(approxEquals(1d, corMatrix[1][1]));
        assertTrue(approxEquals(1d, corMatrix[2][2]));
        assertTrue(approxEquals(0.4679, corMatrix[0][1]));
        assertTrue(approxEquals(0.4679, corMatrix[1][0]));
        assertTrue(approxEquals(0.9132, corMatrix[0][2]));
        assertTrue(approxEquals(0.9132, corMatrix[2][0]));
        assertTrue(approxEquals(0.3222, corMatrix[1][2]));
        assertTrue(approxEquals(0.3222, corMatrix[2][1]));
    }

    @Test public void testApproxEquals() {
        assertTrue(approxEquals(38.5678, 385678d/10000));
        assertFalse(approxEquals(38.5678, 38));
        assertTrue(approxEquals(38.5678, 38.567801));
        assertTrue(approxEquals(38.5678, 38.567800000000000000001));
    }
}
