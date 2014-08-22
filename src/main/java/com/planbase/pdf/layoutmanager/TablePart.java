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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TablePart {
    private final TableBuilder tableBuilder;
    private List<Float> cellWidths;
    private CellStyle cellStyle;
    private TextStyle textStyle;
    private final List<TableRowBuilder> rows = new ArrayList<TableRowBuilder>(1);

    private TablePart(TableBuilder t) {
        tableBuilder = t; cellWidths.addAll(t.cellWidths()); cellStyle = t.cellStyle();
        textStyle = t.textStyle();
    }

    public static TablePart of(TableBuilder t) { return new TablePart(t); }

//    private TablePart(Table t, float[] a, CellStyle b, TextStyle c) {
//        table = t; cellWidths = a; cellStyle = b; textStyle = c;
//    }

//    public static TablePart of(float[] cellWidths, CellStyle cellStyle, TextStyle textStyle) {
//        return new TablePart(cellWidths, cellStyle, textStyle);
//    }

    public List<Float> cellWidths() { return Collections.unmodifiableList(cellWidths); }
    public TablePart cellWidths(List<Float> x) { cellWidths = x; return this; }

//    public TablePart cellWidths(float[] x) { return new Builder().cellWidths(cellWidths).build(); }

    public CellStyle cellStyle() { return cellStyle; }
    public TablePart cellStyle(CellStyle x) { cellStyle = x; return this; }
//    public TablePart cellStyle(CellStyle x) { return new Builder().cellStyle(cellStyle).build(); }

    public TextStyle textStyle() { return textStyle; }
    public TablePart textStyle(TextStyle x) { textStyle = x; return this; }
//    public TablePart textStyle(TextStyle x) { return new Builder().textStyle(textStyle).build(); }

    public TableRowBuilder rowBuilder() { return TableRowBuilder.of(this); }

    public TablePart addRow(TableRowBuilder trb) { rows.add(trb); return this; }

    public TableBuilder buildPart() { return tableBuilder.addPart(this); }

    public XyDim calcDimensions() {
        XyDim maxDim = XyDim.ZERO;
        for (TableRowBuilder row : rows) {
            XyDim wh = row.calcDimensions();
            maxDim = XyDim.of(Float.max(wh.x(), maxDim.x()),
                              maxDim.y() + wh.y());
        }
        return maxDim;
    }

    public XyOffset render(LogicalPage lp, XyOffset outerTopLeft, boolean allPages) {
        XyOffset rightmostLowest = outerTopLeft;
        for (TableRowBuilder row : rows) {
            XyOffset rl = row.render(lp, XyOffset.of(outerTopLeft.x(), rightmostLowest.y()),
                                     allPages);
            rightmostLowest = XyOffset.of(Float.max(rl.x(), rightmostLowest.x()),
                                          Float.min(rl.y(), rightmostLowest.y()));
        }
        return rightmostLowest;
    }

//    public static Builder builder(TableBuilder t) { return new Builder(t); }
//
//    public static class Builder {
//        private final TableBuilder tableBuilder;
//        private float[] cellWidths;
//        private CellStyle cellStyle;
//        private TextStyle textStyle;
//
//        private Builder(TableBuilder t) { tableBuilder = t; }
//
//        public Builder cellWidths(float[] x) { cellWidths = x; return this; }
//        public Builder cellStyle(CellStyle x) { cellStyle = x; return this; }
//        public Builder textStyle(TextStyle x) { textStyle = x; return this; }
//
//        public TablePart build() { return new TablePart(tableBuilder, cellWidths, cellStyle, textStyle); }
//    } // end of class Builder
}