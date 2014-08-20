// Copyright 2014-08-18 PlanBase Inc. & Glen Peterson
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.planbase.pdf.layoutmanager;

public class XyDimension {
    public static final XyDimension ORIGIN = new XyDimension(0f, 0f);
    private final float x;
    private final float y;
    private XyDimension(float xCoord, float yCoord) { x = xCoord; y = yCoord; }
    public static XyDimension of(float x, float y) {
        if ((x == 0f) && (y == 0f)) { return ORIGIN; }
        return new XyDimension(x, y);
    }
    public float x() { return x; }
    public float y() { return y; }
    public XyDimension x(float newX) { return of(newX, y); }
    public XyDimension y(float newY) { return of(x, newY); }

    public XyDimension minus(XyDimension that) { return of(this.x - that.x(), this.y - that.y()); }
    public XyDimension plus(XyDimension that) { return of(this.x + that.x(), this.y + that.y()); }

//    public XyPair plusXMinusY(XyPair that) { return of(this.x + that.x(), this.y - that.y()); }

    public XyDimension maxXandY(XyDimension that) {
        if ((this.x >= that.x()) && (this.y >= that.y())) { return this; }
        if ((this.x <= that.x()) && (this.y <= that.y())) { return that; }
        return of((this.x > that.x()) ? this.x : that.x(),
                  (this.y > that.y()) ? this.y : that.y());
    }
//    public XyPair maxXMinY(XyPair that) {
//        if ((this.x >= that.x()) && (this.y <= that.y())) { return this; }
//        if ((this.x <= that.x()) && (this.y >= that.y())) { return that; }
//        return of((this.x > that.x()) ? this.x : that.x(),
//                  (this.y < that.y()) ? this.y : that.y());
//    }

    /** Compares dimensions */
    public boolean lte(XyDimension that) { return (this.x <= that.x()) && (this.y <= that.y()); }

    @Override
    public String toString() {
        return "XyPair(" + x + ", " + y + ")";
    }
}
