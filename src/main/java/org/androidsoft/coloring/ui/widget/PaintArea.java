/*
 * Copyright (C) 2010 Peter Dornbach.
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
package org.androidsoft.coloring.ui.widget;

import org.androidsoft.coloring.util.FloodFill;
import org.androidsoft.coloring.util.images.BitmapImage;
import org.androidsoft.coloring.util.images.ImageDB;
import org.androidsoft.coloring.util.images.NullImage;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ImageView;

public class PaintArea {

    private Bitmap bitmap = null;
    private int paintColor;
    private ImageView view;

    public PaintArea(ImageView view) {
        this.view = view;
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return onTouchEvent(motionEvent);
            }
        });
    }


    public boolean canPaint() {
        return bitmap != null;
    }

    public ImageDB.Image getImage() {
        if (canPaint()) {
            return new BitmapImage(bitmap);
        }
        return new NullImage();
    }

    public void setImageBitmap(Bitmap bm) {
        view.setImageBitmap(bm);
        bitmap = bm;
    }

    public void setPaintColor(int color)
    {
        paintColor = color;
        if (paintColor == FloodFill.BORDER_COLOR) {
            paintColor ++;
        }
    }

    public boolean onTouchEvent(MotionEvent e)
    {
        if (e.getAction() == MotionEvent.ACTION_DOWN)
        {
            int x = (int)(e.getX() * bitmap.getWidth() / view.getWidth());
            int y = (int)(e.getY() * bitmap.getHeight() / view.getHeight());
            Log.d("touch", "(" + e.getRawX() + ") " + e.getX() + " -> " + x);
            Log.d("touch", "(" + e.getRawY() + ") " + e.getY() + " -> " + y);
            Bitmap newBitmap = FloodFill.fill(bitmap, x, y, paintColor);
            setImageBitmap(newBitmap);
            // play default click sound
            // see https://stackoverflow.com/a/10987791/1320237
            view.playSoundEffect(SoundEffectConstants.CLICK);
        }
        return true;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getPaintColor() {
        return paintColor;
    }
}
