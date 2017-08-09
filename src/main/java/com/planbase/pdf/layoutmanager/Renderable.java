// Copyright 2014-08-14 PlanBase Inc. & Glen Peterson
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

/**
 * Implementing Renderable means being suitable for use with a two-pass layout manager whose first pass says, "given
 * this width, what is your height?" and second pass says, "Given these dimensions, draw yourself as best you can."
 */
// TODO: Split into Layoutable and Renderable where Layoutable just has calcDimensions() which returns a Renderable which just has render() (but without maxWidth) and getXyDim()
public interface Renderable {
    enum Constants implements Renderable {
        EOL {
            @Override
            public XyDim calcDimensions(float maxWidth) {
                return null;
            }

            @Override
            public XyOffset render(RenderTarget lp, XyOffset outerTopLeft, XyDim outerDimensions) {
                return null;
            }
        };

    }

    /**
    Given a width, returns the height and actual width after line wrapping.  If line wrapping is
    not needed, just returns the static width and height.  If calculations are done, the results
    should be cached because render() will likely be called with the same width (or at least one
    previously given widths).
     */
    XyDim calcDimensions(float maxWidth);

    /**
     Only call this with a maxWidth that you have previously passed to calcDimensions.
     Renders item and all child-items with given width and returns the x-y pair of the
     lower-right-hand corner of the last line (e.g. of text).
     @param lp the place to render to (either a single page, or logical collection of pages)
     @param outerTopLeft the top-left position to render to
     @param outerDimensions the width and height of the thing to render.
     @return the bottom-right corner of the rendered result.  This is not necessarily
     the same as topLeft + outerDimensions (it could be on a different page).
    */
    XyOffset render(RenderTarget lp, XyOffset outerTopLeft, XyDim outerDimensions);

    default Renderator renderator() {
        return new Renderator.SingleItemRenderator(this);
    }

}
