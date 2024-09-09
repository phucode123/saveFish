package com.example.save_my_friend.view.level2.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class MapManager {
    private Bitmap tileBitmap;
    private int tileSize;
    private int mapWidth, mapHeight;
    private int[][] mapData; // Mảng chứa các chỉ số của các tile

    public MapManager(Context context, int tileResource, int tileSize, int[][] mapData) {
        this.tileSize = tileSize;
        this.mapData = mapData;
        tileBitmap = BitmapFactory.decodeResource(context.getResources(), tileResource);
        mapWidth = mapData[0].length * tileSize;
        mapHeight = mapData.length * tileSize;
    }

    public void draw(Canvas canvas) {
        for (int y = 0; y < mapData.length; y++) {
            for (int x = 0; x < mapData[y].length; x++) {
                int tileIndex = mapData[y][x];
                int srcX = (tileIndex % (tileBitmap.getWidth() / tileSize)) * tileSize;
                int srcY = (tileIndex / (tileBitmap.getWidth() / tileSize)) * tileSize;
                Rect srcRect = new Rect(srcX, srcY, srcX + tileSize, srcY + tileSize);
                Rect destRect = new Rect(x * tileSize, y * tileSize, (x + 1) * tileSize, (y + 1) * tileSize);
                canvas.drawBitmap(tileBitmap, srcRect, destRect, null);
            }
        }
    }
}

