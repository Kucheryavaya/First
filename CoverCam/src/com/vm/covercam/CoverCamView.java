package com.vm.covercam;

import java.io.IOException;
import java.util.List;

import com.vm.covercam.constants.CFConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Kucheryavaya Lyudmila
 */
public class CoverCamView extends SurfaceView implements
                SurfaceHolder.Callback, Camera.PreviewCallback {
    static {
        System.loadLibrary("livecamera");
    }

    public native void decode(Bitmap pTarget, byte[] pSource, int pCheck);
    
    private static Camera mCamera;
    private byte[] mVideoSource;
    private Bitmap mBackBuffer;
    private Paint mPaint;
    private int mCameraId = 0;
    private static int FPS;
    private Canvas pCanvas;
    private int w, h;
    
    private int w_canvas = CFConstants.getSizeCanvas("width");
    private int h_canvas = CFConstants.getSizeCanvas("height");
    
    public CoverCamView(Context context) {
        super(context);

        getHolder().addCallback(this);
        setWillNotDraw(false);
    }
    
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            
            mCamera = Camera.open();
            mCamera.autoFocus(myAutoFocusCallback);
            mCamera.setPreviewDisplay(null);
            mCamera.setPreviewCallbackWithBuffer(this);
            
        } catch (IOException eIOException) {
            mCamera.release();
            mCamera = null;
            throw new IllegalStateException();
        }
    	
    }
    
    public void surfaceChanged(SurfaceHolder pHolder, int pFormat,
                    int pWidth, int pHeight) {
        mCamera.stopPreview();
        pCanvas = getHolder().lockCanvas();
        Size lSize = findBestResolution(w_canvas, h_canvas);

        PixelFormat lPixelFormat = new PixelFormat();
        PixelFormat.getPixelFormatInfo(mCamera.getParameters()
                        .getPreviewFormat(), lPixelFormat);
        int widthSource = lSize.width;
        int heightSource = lSize.height;
        int lSourceSize = widthSource * heightSource
                        * lPixelFormat.bitsPerPixel / 8;
        mVideoSource = new byte[lSourceSize];
        mBackBuffer = Bitmap.createBitmap(widthSource, heightSource,
                        Bitmap.Config.ARGB_8888);

        Camera.Parameters lParameters = mCamera.getParameters();
        
        lParameters.setPreviewSize(widthSource, heightSource);
        lParameters.setPreviewFormat(PixelFormat.YCbCr_420_SP);
        lParameters.setZoom(8);
        mCamera.autoFocus(myAutoFocusCallback);
        mCamera.setParameters(lParameters);
        
        mCamera.addCallbackBuffer(mVideoSource);
        
        mCamera.startPreview();
        FPS = lParameters.getPreviewFrameRate();
    }
    
    private Size findBestResolution(int pWidth, int pHeight) {
        List<Size> lSizes = mCamera.getParameters()
                        .getSupportedPreviewSizes();
        
        Size lSelectedSize = mCamera.new Size(0, 0);
        for (Size lSize : lSizes) {
            if ((lSize.width <= pWidth)
                            && (lSize.height <= pHeight)
                            && (lSize.width <= lSelectedSize.width)
                            && (lSize.height <= lSelectedSize.height)) {
                lSelectedSize = lSize;
            }
        }
        
        if ((lSelectedSize.width == 0)
                        || (lSelectedSize.height == 0)) {
            lSelectedSize = lSizes.get(0);
        }
        return lSelectedSize;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mVideoSource = null;
            mBackBuffer = null;
        }
    }

    public void onPreviewFrame(byte[] pData, Camera pCamera) {
        decode(mBackBuffer, pData, CFConstants.getColorFilter());
    	invalidate();
    }
    
    @SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas pCanvas) {
        
    	
    	if (mCamera != null) {
            
    		super.onDraw(pCanvas);
    		pCanvas.rotate(90, w_canvas, 0);
    		Log.i("density", Integer.toString(w_canvas));
    		Bitmap mbp = getResizedBitmap(mBackBuffer, w_canvas, h_canvas);
    		pCanvas.drawBitmap(mbp, w_canvas, 0, null);
            mCamera.addCallbackBuffer(mVideoSource);
            
        }
    }
    
    public static int getFPS() {
    	return FPS;
    }
    
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight, width, height);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }

    public static Camera getCamera() {
    	return mCamera;
    }
    
    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback(){

		@Override
		public void onAutoFocus(boolean arg0, Camera arg1) {
			// TODO Auto-generated method stub
			
		}};
    
}
