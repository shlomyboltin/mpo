package com.example.shlomyslaptop.myapplication99;

import android.content.Context;
import android.hardware.Camera;
import android.widget.Toast;

import me.dm7.barcodescanner.core.CameraWrapper;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ZXingAutofocusScannerView extends ZXingScannerView {

    private boolean callbackFocus = false ;

    public ZXingAutofocusScannerView(Context context) {
        super(context);
    }
    @Override public void setupCameraPreview(CameraWrapper cameraWrapper)
    {
        Camera.Parameters parameters= cameraWrapper.mCamera.getParameters();
        if(parameters != null)
        {
            try {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                parameters.setPictureSize(500,500);
                parameters.setZoom(2);
                cameraWrapper.mCamera.setParameters(parameters);
            }catch (Exception e)
            {
                callbackFocus = true;
            }
            // cameraWrapper.mCamera.getParameters()

        }


        super.setupCameraPreview(cameraWrapper);
    }
    @Override
    public void setAutoFocus(boolean state) {
        super.setAutoFocus(callbackFocus);

    }
}