// Copyright 2013-03-03 PlanBase Inc. & Glen Peterson
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

import java.awt.Color;

/**
 The style of a cell including: horizontal alignment, padding, background color, and border style.
 Immutable.
 */
public class CellStyle {

//    /** Horizontal allignment options for cell contents */
//    public enum HorizAlign { LEFT, CENTER, RIGHT; }
//    public enum VertAlign { TOP, MIDDLE, BOTTOM; }

    /** Horizontal and vertical alignment options for cell contents */
    public enum Align {
        TOP_LEFT {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                return Padding.of(0, outer.x() - inner.x(), outer.y() - inner.y(), 0);
            }
        },
        TOP_CENTER {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                float dx = (outer.x() - inner.x()) / 2;
                return Padding.of(0, dx, outer.y() - inner.y(), dx);
            }
        },
        TOP_RIGHT {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                return Padding.of(0, 0, outer.y() - inner.y(), outer.x() - inner.x());
            }
        },
        MIDDLE_LEFT {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                float dy = (outer.y() - inner.y()) / 2;
                return Padding.of(dy, outer.x() - inner.x(), dy, 0);
            }
        },
        MIDDLE_CENTER {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                float dx = (outer.x() - inner.x()) / 2;
                float dy = (outer.y() - inner.y()) / 2;
                return Padding.of(dy, dx, dy, dx);
            }
        },
        MIDDLE_RIGHT {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                float dy = (outer.y() - inner.y()) / 2;
                return Padding.of(dy, 0, dy, outer.x() - inner.x());
            }
        },
        BOTTOM_LEFT {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                return Padding.of(outer.y() - inner.y(), outer.x() - inner.x(), 0, 0);
            }
        },
        BOTTOM_CENTER {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                float dx = (outer.x() - inner.x()) / 2;
                return Padding.of(outer.y() - inner.y(), dx, 0, dx);
            }
        },
        BOTTOM_RIGHT {
            @Override public Padding calcPadding(XyDimension outer, XyDimension inner) {
                if (outer.lte(inner)) { return Padding.NO_PADDING; }
                return Padding.of(outer.y() - inner.y(), 0, 0, outer.x() - inner.x());
            }
        };

        /*
        Given outer dimensions (make sure to add padding as necessary), and inner dimensions,
        calculates additional padding to apply.
        */
        public abstract Padding calcPadding(XyDimension outer, XyDimension inner);
    }

    public static final Align DEFAULT_ALIGN = Align.TOP_LEFT;

    public static final CellStyle DEFAULT = new CellStyle(DEFAULT_ALIGN, null, null,
                                                          BorderStyle.NO_BORDERS);

    private final Align align;
    private final Padding padding;
    private final Color bgColor;
    private final BorderStyle borderStyle; // Only for cell-style

    private CellStyle(Align a, Padding p, Color bg, BorderStyle b) {
        align = a; padding = p; bgColor = bg; // I think it's OK if this is null.
        borderStyle = b;
    }

    public static CellStyle of(Align a, Padding p, Color bg, BorderStyle b) {
        return builder().align(a).padding(p).bgColor(bg).borderStyle(b).build();
    }

    public Align align() { return align; }
    public Padding padding() { return padding; }
    public Color bgColor() { return bgColor; }
    public BorderStyle borderStyle() { return borderStyle; }

    // NOTE: This lincluded the padding!
//    public float calcLeftX(float x, float spareRoom) {
//        return HorizAlign.LEFT == align ? x + padding.left() :
//               HorizAlign.CENTER == align ? x + (spareRoom / 2) :
//               HorizAlign.RIGHT == align ? x + spareRoom - padding.right() :
//               x;
//    }

    public CellStyle align(Align a) { return new Builder(this).align(a).build(); }
    public CellStyle padding(Padding p) { return new Builder(this).padding(p).build(); }
    public CellStyle bgColor(Color c) { return new Builder(this).bgColor(c).build(); }
    public CellStyle borderStyle(BorderStyle bs) {
        return new Builder(this).borderStyle(bs).build();
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Align align;
        private Padding padding;
        private Color bgColor;
        private BorderStyle borderStyle; // Only for cell-style

        private Builder() {}

        private Builder(CellStyle cs) {
            align = cs.align; padding = cs.padding; bgColor = cs.bgColor;
            borderStyle = cs.borderStyle;
        }

        public CellStyle build() {
            if (align == null) { align = DEFAULT_ALIGN; }
            if (padding == null) { padding = Padding.DEFAULT_TEXT_PADDING; }
            if (borderStyle == null) { borderStyle = BorderStyle.NO_BORDERS; }

            if ( (align == DEFAULT_ALIGN) &&
                 (padding == Padding.DEFAULT_TEXT_PADDING) &&
                 ((bgColor == null) || bgColor.equals(Color.WHITE)) &&
                 (borderStyle == BorderStyle.NO_BORDERS) ) {
                return DEFAULT;
            }
            return new CellStyle(align, padding, bgColor, borderStyle);
        }

        public Builder align(Align a) { align = a; return this; }

//        public Builder alignLeft() { align = HorizAlign.LEFT; return this; }
//        public Builder alignCenter() { align = HorizAlign.CENTER; return this; }
//        public Builder alignRight() { align = HorizAlign.RIGHT; return this; }

        public Builder padding(Padding p) { padding = p; return this; }
        public Builder bgColor(Color c) { bgColor = c; return this; }
        public Builder borderStyle(BorderStyle bs) { borderStyle = bs; return this; }
    }
}
