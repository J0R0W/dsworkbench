/* 
 * Copyright 2015 Torridity.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tor.tribes.ui.models;

import java.awt.Point;
import javax.swing.AbstractSpinnerModel;

/**
 *
 * @author Torridity
 */
public class CoordinateSpinnerModel extends AbstractSpinnerModel {

    public static final int FIELD_X = 0;
    public static final int FIELD_Y = 1;
    private Point point;
    private int field = FIELD_X;

    /**
     * Create a default
     * <code>SpinnerPointModel</code>
     */
    public CoordinateSpinnerModel() {
        this(null);
    }

    /**
     * Create a
     * <code>SpinnerPointModel</code> with a specified
     * <code>Point</code>
     *
     * @param point this specified<code>Point</code>
     */
    public CoordinateSpinnerModel(Point point) {
        this.point = point == null ? new Point() : point;
    }

    /**
     * The <i>current element</i> of the sequence. This element is usually displayed by the
     * <code>editor</code> part of a
     * <code>JSpinner</code>.
     *
     * @return the current spinner value.
     *
     * @see #setValue
     */
    @Override
    public Object getValue() {
        return point;
    }

    /**
     * Changes current value of the model, typically this value is displayed by the
     * <code>editor</code> part of a
     * <code>JSpinner</code>. If the
     * <code>SpinnerModel</code> implementation doesn't support the specified value then an
     * <code>IllegalArgumentException</code> is thrown. For example a
     * <code>SpinnerModel</code> for numbers might only support values that are integer multiples of ten. In that case,
     * <code>model.setValue(new Number(11))</code> would throw an exception.
     *
     * @param value new value
     * @throws IllegalArgumentException if
     * <code>value</code> isn't allowed
     * @see #getValue
     */
    @Override
    public void setValue(Object value) {
        if (value instanceof Point) {
            setPoint((Point) value);
        }
    }

    /**
     * The <i>current element</i> of the sequence. This element is usually displayed by the
     * <code>editor</code> part of a
     * <code>JSpinner</code>.
     *
     * @return the current spinner value.
     *
     * @see #setPoint(Point)
     * @see #getValue()
     */
    public Point getPoint() {
        return point;
    }

    /**
     * @param point the new point
     */
    public void setPoint(Point point) {
        if (!this.point.equals(point)) {
            this.point = point;
            fireStateChanged();
        }
    }

    /**
     * @return the field.
     */
    public int getField() {
        return field;
    }

    /**
     * @param field the new field.
     */
    public void setField(int field) {
        this.field = field;
    }

    /**
     * Return the object in the sequence that comes after the object returned by
     * <code>getValue()</code>. If the end of the sequence has been reached then return null. Calling this method does not effect
     * <code>value</code>.
     *
     * @return the next legal value or null if one doesn't exist
     *
     * @see #getValue
     * @see #getPreviousValue
     */
    @Override
    public Object getNextValue() {
        Point p = (Point) point.clone();
        if (field == FIELD_X) {
            p.x++;
        } else {
            p.y++;
        }
        return p;
    }

    /**
     * Return the object in the sequence that comes before the object returned by
     * <code>getValue()</code>. If the end of the sequence has been reached then return null. Calling this method does not effect
     * <code>value</code>.
     *
     * @return the previous legal value or null if one doesn't exist
     *
     * @see #getValue
     * @see #getNextValue
     */
    @Override
    public Object getPreviousValue() {
        Point p = (Point) point.clone();
        if (field == FIELD_X) {
            p.x--;
        } else {
            p.y--;
        }
        return p;
    }
}
