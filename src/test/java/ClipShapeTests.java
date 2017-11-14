/*
 * Copyright (c) 2017, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * @test
 * @bug 8184429
 * @summary Verifies that Marlin rendering generates the same
 * images with and without clipping optimization with all possible
 * stroke (cap/join) and fill modes (EO rules)
 * Use the following setting to use Float or Double variant:
 * -Dsun.java2d.renderer=org.marlin.pisces.MarlinRenderingEngine
 * -Dsun.java2d.renderer=org.marlin.pisces.DMarlinRenderingEngine
 * @run main ClipShapeTests
 * @ignore tests that take a long time (huge number of polygons)
 * @run main/othervm -ms1g -mx1g ClipShapeTests -slow
 */
public final class ClipShapeTests {

    static final boolean TEST_STROKER = true;
    static final boolean TEST_FILLER = true;

    static final boolean USE_DASHES = false; // really slower

    static int NUM_TESTS = 500;
    static final int TESTW = 100;
    static final int TESTH = 100;
    static final ShapeMode SHAPE_MODE = ShapeMode.NINE_LINE_POLYS;
    static final boolean SHAPE_REPEAT = true;

    // dump path on console:
    static final boolean DUMP_SHAPE = true;

    static final boolean SHOW_DETAILS = true;
    static final boolean SHOW_OUTLINE = true;
    static final boolean SHOW_POINTS = true;
    static final boolean SHOW_INFO = false;

    static final int MAX_SHOW_FRAMES = 10;

    static final boolean FIXED_SEED = false;
    static final double RAND_SCALE = 3.0;
    static final double RANDW = TESTW * RAND_SCALE;
    static final double OFFW = (TESTW - RANDW) / 2.0;
    static final double RANDH = TESTH * RAND_SCALE;
    static final double OFFH = (TESTH - RANDH) / 2.0;

    static enum ShapeMode {
        TWO_CUBICS,
        FOUR_QUADS,
        FIVE_LINE_POLYS,
        NINE_LINE_POLYS,
        FIFTY_LINE_POLYS,
        MIXED
    }

    static final long SEED = 1666133789L;
    static final Random RANDOM;

    static {
        // disable static clipping setting:
        System.setProperty("sun.java2d.renderer.clip", "false");
        System.setProperty("sun.java2d.renderer.clip.runtime.enable", "true");

        // Fixed seed to avoid any difference between runs:
        RANDOM = new Random(SEED);
    }

    static final File OUTPUT_DIR = new File(".");

    /**
     * Test
     * @param args
     */
    public static void main(String[] args) {
        boolean runSlowTests = (args.length != 0 && "-slow".equals(args[0]));

        if (runSlowTests) {
            NUM_TESTS = 10000; // 10000 or 100000 (very slow)
        }

        // First display which renderer is tested:
        // JDK9 only:
        System.setProperty("sun.java2d.renderer.verbose", "true");
        System.out.println("Testing renderer: ");
        // Other JDK:
        String renderer = "undefined";
        try {
            renderer = sun.java2d.pipe.RenderingEngine.getInstance().getClass().getName();
            System.out.println(renderer);
        } catch (Throwable th) {
            // may fail with JDK9 jigsaw (jake)
            if (false) {
                System.err.println("Unable to get RenderingEngine.getInstance()");
                th.printStackTrace();
            }
        }

        System.out.println("ClipShapeTests: image = " + TESTW + " x " + TESTH);

        int failures = 0;
        final long start = System.nanoTime();
        try {
            // TODO: test affine transforms ?

            if (TEST_STROKER) {
                final float[][] dashArrays = (USE_DASHES)
                        ? new float[][]{null, new float[]{1f, 2f}}
                        : new float[][]{null};

                // Stroker tests:
                for (float width = 0.1f; width < 110f; width *= 5f) {
                    for (int cap = 0; cap <= 2; cap++) {
                        for (int join = 0; join <= 2; join++) {
                            for (float[] dashes : dashArrays) {
                                failures += paintPaths(new TestSetup(SHAPE_MODE, false, width, cap, join, dashes));
                                failures += paintPaths(new TestSetup(SHAPE_MODE, true, width, cap, join, dashes));
                            }
                        }
                    }
                }
            }

            if (TEST_FILLER) {
                // Filler tests:
                failures += paintPaths(new TestSetup(SHAPE_MODE, false, Path2D.WIND_NON_ZERO));
                failures += paintPaths(new TestSetup(SHAPE_MODE, true, Path2D.WIND_NON_ZERO));

                failures += paintPaths(new TestSetup(SHAPE_MODE, false, Path2D.WIND_EVEN_ODD));
                failures += paintPaths(new TestSetup(SHAPE_MODE, true, Path2D.WIND_EVEN_ODD));
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        System.out.println("main: duration= " + (1e-6 * (System.nanoTime() - start)) + " ms.");
        if (failures != 0) {
            throw new RuntimeException("Clip test failures : " + failures);
        }
    }

    public static int paintPaths(final TestSetup ts) throws IOException {
        final long start = System.nanoTime();

        if (FIXED_SEED) {
            // Reset seed for random numbers:
            RANDOM.setSeed(SEED);
        }

        System.out.println("paintPaths: " + NUM_TESTS
                + " paths (" + SHAPE_MODE + ") - setup: " + ts);

        final boolean fill = !ts.isStroke();
        final Path2D p2d = new Path2D.Double(ts.windingRule);

        final BufferedImage imgOn = newImage(TESTW, TESTH);
        final Graphics2D g2dOn = initialize(imgOn, ts);

        final BufferedImage imgOff = newImage(TESTW, TESTH);
        final Graphics2D g2dOff = initialize(imgOff, ts);

        final BufferedImage imgDiff = newImage(TESTW, TESTH);

        final DiffContext globalCtx = new DiffContext("All tests");

        int nd = 0;
        try {
            final DiffContext testCtx = new DiffContext("Test");
            BufferedImage diffImage;

            for (int n = 0; n < NUM_TESTS; n++) {
                genShape(p2d, ts);

                // Runtime clip setting OFF:
                paintShape(p2d, g2dOff, fill, false);

                // Runtime clip setting ON:
                paintShape(p2d, g2dOn, fill, true);

                /* compute image difference if possible */
                diffImage = computeDiffImage(testCtx, imgOn, imgOff, imgDiff, globalCtx);

                final String testName = "Setup_" + ts.id + "_test_" + n;

                if (diffImage != null) {
                    nd++;

                    final double ratio = (100.0 * testCtx.histPix.count) / testCtx.histAll.count;
                    System.out.println("Diff ratio: " + testName + " = " + trimTo3Digits(ratio) + " %");

                    if (false) {
                        saveImage(diffImage, OUTPUT_DIR, testName + "-diff.png");
                    }

                    if (DUMP_SHAPE) {
                        dumpShape(p2d);
                    }
                    if (nd < MAX_SHOW_FRAMES) {
                        if (SHOW_DETAILS) {
                            paintShapeDetails(g2dOff, p2d);
                            paintShapeDetails(g2dOn, p2d);
                        }

                        saveImage(imgOff, OUTPUT_DIR, testName + "-off.png");
                        saveImage(imgOn, OUTPUT_DIR, testName + "-on.png");
                        saveImage(diffImage, OUTPUT_DIR, testName + "-diff.png");
                    }
                }
            }
        } finally {
            g2dOff.dispose();
            g2dOn.dispose();

            if (nd != 0) {
                System.out.println("paintPaths: " + NUM_TESTS + " paths - "
                        + "Number of differences = " + nd
                        + " ratio = " + (100f * nd) / NUM_TESTS + " %");
            }

            globalCtx.dump();
        }
        System.out.println("paintPaths: duration= " + (1e-6 * (System.nanoTime() - start)) + " ms.");
        return nd;
    }

    private static void paintShape(final Path2D p2d, final Graphics2D g2d,
                                   final boolean fill, final boolean clip) {
        reset(g2d);

        setClip(g2d, clip);

        if (fill) {
            g2d.fill(p2d);
        } else {
            g2d.draw(p2d);
        }
    }

    private static Graphics2D initialize(final BufferedImage img,
                                         final TestSetup ts) {
        final Graphics2D g2d = (Graphics2D) img.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);

        if (ts.isStroke()) {
            g2d.setStroke(createStroke(ts));
        }
        g2d.setColor(Color.GRAY);

        return g2d;
    }

    private static void reset(final Graphics2D g2d) {
        // Disable antialiasing:
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, TESTW, TESTH);
    }

    private static void setClip(final Graphics2D g2d, final boolean clip) {
        // Enable antialiasing:
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Enable or Disable clipping:
        System.setProperty("sun.java2d.renderer.clip.runtime", (clip) ? "true" : "false");
    }

    static void genShape(final Path2D p2d, final TestSetup ts) {
        p2d.reset();

        final int end = (SHAPE_REPEAT) ? 2 : 1;

        for (int p = 0; p < end; p++) {
            p2d.moveTo(randX(), randY());

            switch (ts.shapeMode) {
                case MIXED:
                case FIFTY_LINE_POLYS:
                case NINE_LINE_POLYS:
                case FIVE_LINE_POLYS:
                    p2d.lineTo(randX(), randY());
                    p2d.lineTo(randX(), randY());
                    p2d.lineTo(randX(), randY());
                    p2d.lineTo(randX(), randY());
                    if (ts.shapeMode == ShapeMode.FIVE_LINE_POLYS) {
                        // And an implicit close makes 5 lines
                        break;
                    }
                    p2d.lineTo(randX(), randY());
                    p2d.lineTo(randX(), randY());
                    p2d.lineTo(randX(), randY());
                    p2d.lineTo(randX(), randY());
                    if (ts.shapeMode == ShapeMode.NINE_LINE_POLYS) {
                        // And an implicit close makes 9 lines
                        break;
                    }
                    if (ts.shapeMode == ShapeMode.FIFTY_LINE_POLYS) {
                        for (int i = 0; i < 41; i++) {
                            p2d.lineTo(randX(), randY());
                        }
                        // And an implicit close makes 50 lines
                        break;
                    }
                case TWO_CUBICS:
                    p2d.curveTo(randX(), randY(), randX(), randY(), randX(), randY());
                    p2d.curveTo(randX(), randY(), randX(), randY(), randX(), randY());
                    if (ts.shapeMode == ShapeMode.TWO_CUBICS) {
                        break;
                    }
                case FOUR_QUADS:
                    p2d.quadTo(randX(), randY(), randX(), randY());
                    p2d.quadTo(randX(), randY(), randX(), randY());
                    p2d.quadTo(randX(), randY(), randX(), randY());
                    p2d.quadTo(randX(), randY(), randX(), randY());
                    if (ts.shapeMode == ShapeMode.FOUR_QUADS) {
                        break;
                    }
                default:
            }

            if (ts.closed) {
                p2d.closePath();
            }
        }
    }

    static final float POINT_RADIUS = 2f;
    static final float LINE_WIDTH = 1f;

    static final Stroke OUTLINE_STROKE = new BasicStroke(LINE_WIDTH);
    static final int COLOR_ALPHA = 128;
    static final Color COLOR_MOVETO = new Color(255, 0, 0, COLOR_ALPHA);
    static final Color COLOR_LINETO_ODD = new Color(0, 0, 255, COLOR_ALPHA);
    static final Color COLOR_LINETO_EVEN = new Color(0, 255, 0, COLOR_ALPHA);

    static final Ellipse2D.Float ELL_POINT = new Ellipse2D.Float();

    private static void paintShapeDetails(final Graphics2D g2d, final Shape shape) {

        final Stroke oldStroke = g2d.getStroke();
        final Color oldColor = g2d.getColor();

        setClip(g2d, false);

        if (SHOW_OUTLINE) {
            g2d.setStroke(OUTLINE_STROKE);
            g2d.setColor(COLOR_LINETO_ODD);
            g2d.draw(shape);
        }

        final float[] coords = new float[6];
        float px, py;

        int nMove = 0;
        int nLine = 0;
        int n = 0;

        for (final PathIterator it = shape.getPathIterator(null); !it.isDone(); it.next()) {
            int type = it.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    if (SHOW_POINTS) {
                        g2d.setColor(COLOR_MOVETO);
                    }
                    break;
                case PathIterator.SEG_LINETO:
                    if (SHOW_POINTS) {
                        g2d.setColor((nLine % 2 == 0) ? COLOR_LINETO_ODD : COLOR_LINETO_EVEN);
                    }
                    nLine++;
                    break;
                case PathIterator.SEG_CLOSE:
                    continue;
                default:
                    System.out.println("unsupported segment type= " + type);
                    continue;
            }
            px = coords[0];
            py = coords[1];

            if (SHOW_INFO) {
                System.out.println("point[" + (n++) + "|seg=" + type + "]: " + px + " " + py);
            }

            if (SHOW_POINTS) {
                ELL_POINT.setFrame(px - POINT_RADIUS, py - POINT_RADIUS,
                        POINT_RADIUS * 2f, POINT_RADIUS * 2f);
                g2d.fill(ELL_POINT);
            }
        }
        if (SHOW_INFO) {
            System.out.println("Path moveTo=" + nMove + ", lineTo=" + nLine);
            System.out.println("--------------------------------------------------");
        }

        g2d.setStroke(oldStroke);
        g2d.setColor(oldColor);
    }

    private static void dumpShape(final Shape shape) {
        final float[] coords = new float[6];

        for (final PathIterator it = shape.getPathIterator(null); !it.isDone(); it.next()) {
            final int type = it.currentSegment(coords);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    System.out.println("p2d.moveTo(" + coords[0] + ", " + coords[1] + ");");
                    break;
                case PathIterator.SEG_LINETO:
                    System.out.println("p2d.lineTo(" + coords[0] + ", " + coords[1] + ");");
                    break;
                case PathIterator.SEG_CLOSE:
                    System.out.println("p2d.closePath();");
                    break;
                default:
                    System.out.println("// Unsupported segment type= " + type);
            }
        }
        System.out.println("--------------------------------------------------");
    }

    public static double randX() {
        return RANDOM.nextDouble() * RANDW + OFFW;
    }

    public static double randY() {
        return RANDOM.nextDouble() * RANDH + OFFH;
    }

    private static BasicStroke createStroke(final TestSetup ts) {
        return new BasicStroke(ts.strokeWidth, ts.strokeCap, ts.strokeJoin, 10.0f, ts.dashes, 0.0f);
    }

    private final static class TestSetup {

        static final AtomicInteger COUNT = new AtomicInteger();

        final int id;
        final ShapeMode shapeMode;
        final boolean closed;
        // stroke
        final float strokeWidth;
        final int strokeCap;
        final int strokeJoin;
        final float[] dashes;
        // fill
        final int windingRule;

        TestSetup(ShapeMode shapeMode, final boolean closed,
                  final float strokeWidth, final int strokeCap, final int strokeJoin, final float[] dashes) {
            this.id = COUNT.incrementAndGet();
            this.shapeMode = shapeMode;
            this.closed = closed;
            this.strokeWidth = strokeWidth;
            this.strokeCap = strokeCap;
            this.strokeJoin = strokeJoin;
            this.dashes = dashes;
            this.windingRule = Path2D.WIND_NON_ZERO;
        }

        TestSetup(ShapeMode shapeMode, final boolean closed, final int windingRule) {
            this.id = COUNT.incrementAndGet();
            this.shapeMode = shapeMode;
            this.closed = closed;
            this.strokeWidth = 0f;
            this.strokeCap = this.strokeJoin = -1; // invalid
            this.dashes = null;
            this.windingRule = windingRule;
        }

        boolean isStroke() {
            return this.strokeWidth > 0f;
        }

        @Override
        public String toString() {
            return "TestSetup{id=" + id + ", shapeMode=" + shapeMode + ", closed=" + closed
                    + ", strokeWidth=" + strokeWidth + ", strokeCap=" + strokeCap + ", strokeJoin=" + strokeJoin
                    + ((dashes != null) ? ", dashes: " + Arrays.toString(dashes) : "")
                    + ", windingRule=" + windingRule + '}';
        }
    }

    // --- utilities ---
    private static final boolean USE_GRAPHICS_ACCELERATION = false;
    private static final boolean NORMALIZE_DIFF = true;
    private static final int DCM_ALPHA_MASK = 0xff000000;

    private final static GraphicsConfiguration gc = (USE_GRAPHICS_ACCELERATION)
            ? GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration() : null;

    public static BufferedImage newImage(final int w, final int h) {
        if (USE_GRAPHICS_ACCELERATION) {
            return gc.createCompatibleImage(w, h);
        }
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
    }

    public static BufferedImage computeDiffImage(final DiffContext localCtx, final BufferedImage tstImage, final BufferedImage refImage,
                                                 final BufferedImage diffImage, final DiffContext globalCtx) {
        final int width = tstImage.getWidth();
        final int height = tstImage.getHeight();

        final DataBuffer refDataBuffer = refImage.getRaster().getDataBuffer();
        final DataBuffer tstDataBuffer = tstImage.getRaster().getDataBuffer();

        if (refDataBuffer instanceof DataBufferInt) {
            final BufferedImage dImage = (diffImage != null) ? diffImage : newImage(width, height);

            final int aRefPix[] = ((DataBufferInt) refDataBuffer).getData();
            final int aTstPix[] = ((DataBufferInt) tstDataBuffer).getData();
            final int aDifPix[] = ((DataBufferInt) dImage.getRaster().getDataBuffer()).getData();

            // reset local diff context:
            localCtx.reset();

            int dr, dg, db, v, max = 0;
            for (int i = 0, len = aRefPix.length; i < len; i++) {

                /* put condition out of loop */
                // grayscale diff:
                dg = (r(aRefPix[i]) + g(aRefPix[i]) + b(aRefPix[i]))
                        - (r(aTstPix[i]) + g(aTstPix[i]) + b(aTstPix[i]));

                // max difference on grayscale values:
                v = (int) Math.ceil(Math.abs(dg / 3.0));

                if (v > max) {
                    max = v;
                }
                aDifPix[i] = toInt(v, v, v);

                localCtx.add(v);
                globalCtx.add(v);
            }

            if (!localCtx.isDiff()) {
                return null;
            }

            if (NORMALIZE_DIFF) {
                /* normalize diff image vs mean(diff) */
                if ((max > 0) && (max < 255)) {
                    final float factor = 255f / max;
                    for (int i = 0, len = aDifPix.length; i < len; i++) {
                        v = (int) Math.ceil(factor * b(aDifPix[i]));
                        aDifPix[i] = toInt(v, v, v);
                    }
                }
            }

            return dImage;
        }
        return null;
    }

    public static void saveImage(final BufferedImage image, final File resDirectory, final String imageFileName) throws IOException {
        final Iterator<ImageWriter> itWriters = ImageIO.getImageWritersByFormatName("PNG");
        if (itWriters.hasNext()) {
            final ImageWriter writer = itWriters.next();

            final ImageWriteParam writerParams = writer.getDefaultWriteParam();
            writerParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

            final File imgFile = new File(resDirectory, imageFileName);

            if (!imgFile.exists() || imgFile.canWrite()) {
                System.out.println("saveImage: saving image as PNG [" + imgFile + "]...");
                imgFile.delete();

                // disable cache in temporary files:
                ImageIO.setUseCache(false);

                final long start = System.nanoTime();

                // PNG uses already buffering:
                final ImageOutputStream imgOutStream = ImageIO.createImageOutputStream(new FileOutputStream(imgFile));

                writer.setOutput(imgOutStream);
                try {
                    writer.write(null, new IIOImage(image, null, null), writerParams);
                } finally {
                    imgOutStream.close();

                    final long time = System.nanoTime() - start;
                    System.out.println("saveImage: duration= " + (time / 1000000l) + " ms.");
                }
            }
        }
    }

    public static int r(final int v) {
        return (v >> 16 & 0xff);
    }

    public static int g(final int v) {
        return (v >> 8 & 0xff);
    }

    public static int b(final int v) {
        return (v & 0xff);
    }

    public static int clamp127(final int v) {
        return (v < 128) ? (v > -127 ? (v + 127) : 0) : 255;
    }

    public static int toInt(final int r, final int g, final int b) {
        return DCM_ALPHA_MASK | (r << 16) | (g << 8) | b;
    }

    /* stats */
    static class StatInteger {

        public final String name;
        public long count = 0l;
        public long sum = 0l;
        public long min = Integer.MAX_VALUE;
        public long max = Integer.MIN_VALUE;

        StatInteger(String name) {
            this.name = name;
        }

        void reset() {
            count = 0l;
            sum = 0l;
            min = Integer.MAX_VALUE;
            max = Integer.MIN_VALUE;
        }

        void add(int val) {
            count++;
            sum += val;
            if (val < min) {
                min = val;
            }
            if (val > max) {
                max = val;
            }
        }

        void add(long val) {
            count++;
            sum += val;
            if (val < min) {
                min = val;
            }
            if (val > max) {
                max = val;
            }
        }

        public final double average() {
            return ((double) sum) / count;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(128);
            toString(sb);
            return sb.toString();
        }

        public final StringBuilder toString(final StringBuilder sb) {
            sb.append(name).append("[n: ").append(count);
            sb.append("] sum: ").append(sum).append(" avg: ").append(trimTo3Digits(average()));
            sb.append(" [").append(min).append(" | ").append(max).append("]");
            return sb;
        }

    }

    public final static class Histogram extends StatInteger {

        static final int BUCKET = 2;
        static final int MAX = 20;
        static final int LAST = MAX - 1;
        static final int[] STEPS = new int[MAX];

        static {
            STEPS[0] = 0;
            STEPS[1] = 1;

            for (int i = 2; i < MAX; i++) {
                STEPS[i] = STEPS[i - 1] * BUCKET;
            }
//            System.out.println("Histogram.STEPS = " + Arrays.toString(STEPS));
        }

        static int bucket(int val) {
            for (int i = 1; i < MAX; i++) {
                if (val < STEPS[i]) {
                    return i - 1;
                }
            }
            return LAST;
        }

        private final StatInteger[] stats = new StatInteger[MAX];

        public Histogram(String name) {
            super(name);
            for (int i = 0; i < MAX; i++) {
                stats[i] = new StatInteger(String.format("%5s .. %5s", STEPS[i], ((i + 1 < MAX) ? STEPS[i + 1] : "~")));
            }
        }

        @Override
        final void reset() {
            super.reset();
            for (int i = 0; i < MAX; i++) {
                stats[i].reset();
            }
        }

        @Override
        final void add(int val) {
            super.add(val);
            stats[bucket(val)].add(val);
        }

        @Override
        final void add(long val) {
            add((int) val);
        }

        @Override
        public final String toString() {
            final StringBuilder sb = new StringBuilder(2048);
            super.toString(sb).append(" { ");

            for (int i = 0; i < MAX; i++) {
                if (stats[i].count != 0l) {
                    sb.append("\n        ").append(stats[i].toString());
                }
            }

            return sb.append(" }").toString();
        }
    }

    /**
     * Adjust the given double value to keep only 3 decimal digits
     * @param value value to adjust
     * @return double value with only 3 decimal digits
     */
    public static double trimTo3Digits(final double value) {
        return ((long) (1e3d * value)) / 1e3d;
    }

    public static final class DiffContext {

        public final Histogram histAll;
        public final Histogram histPix;

        public DiffContext(String name) {
            histAll = new Histogram("All  Pixels [" + name + "]");
            histPix = new Histogram("Diff Pixels [" + name + "]");
        }

        public void reset() {
            histAll.reset();
            histPix.reset();
        }

        public void dump() {
            if (isDiff()) {
                System.out.println("Differences [" + histAll.name + "]:");
                System.out.println("Total [all pixels]:\n" + histAll.toString());
                System.out.println("Total [different pixels]:\n" + histPix.toString());
            } else {
                System.out.println("No difference for [" + histAll.name + "].");
            }
        }

        void add(int val) {
            histAll.add(val);
            if (val != 0) {
                histPix.add(val);
            }
        }

        boolean isDiff() {
            return histAll.sum != 0l;
        }
    }
}
