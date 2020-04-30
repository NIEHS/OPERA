package org.openscience.cdk1.renderer.elements;

import java.awt.Color;

import org.openscience.cdk1.annotations.TestClass;
import org.openscience.cdk1.annotations.TestMethod;

/**
 * A ring is just a circle - in other words, an oval whose width and height are
 * the same.
 * 
 * @cdk.module renderbasic
 * @cdk.githash
 */
@TestClass("org.openscience.cdk1.renderer.elements.RingElementTest")
public class RingElement extends OvalElement implements IRenderingElement {

    /**
     * Make a ring element centered on (x, y) with radius and color given.
     * 
     * @param x the x-coordinate of the ring center
     * @param y the y-coordinate of the ring center
     * @param radius the radius of the circle
     * @param color the color of the circle
     */
	@TestMethod("testConstructor")
	public RingElement(double x, double y, double radius, Color color) {
        super(x, y, radius, false, color);
    }

    /** {@inheritDoc} */
	@TestMethod("testAccept")
    public void accept(IRenderingVisitor v) {
        v.visit(this);
    }

}
