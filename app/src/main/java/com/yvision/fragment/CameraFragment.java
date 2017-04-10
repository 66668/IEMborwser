package com.yvision.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yvision.R;
import com.yvision.widget.SquareSurfacePreview;

import java.io.IOException;
import java.util.List;

import static android.hardware.Camera.CameraInfo;
import static android.hardware.Camera.PictureCallback;
import static android.hardware.Camera.ShutterCallback;
import static android.hardware.Camera.getCameraInfo;
import static android.hardware.Camera.open;

/**
 * 相机详细操作
 * <p>
 * 预览界面适应公司需求，为方形图
 * 可以切换摄像头
 * 拍照后 图片自动旋转处理，
 * <p>
 * Created by sjy on 2016/11/17.
 */

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, PictureCallback {

    //常量
    public static final String TAG = "CameraFragment";//CameraFragment.class.getSimpleName()
    public static final String CAMERA_ID_KEY = "camera_id";
    public static final String CAMERA_FLASH_KEY = "flash_mode";
    public static final String PREVIEW_HEIGHT_KEY = "preview_height";
    private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
    private static final int PREVIEW_SIZE_MAX_WIDTH = 640;


    /*
     * 控件
     */
    private SquareSurfacePreview mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private View topCoverView;//顶部view
    private View btnCoverView;//底部view
    //拍照三个按钮
    private LinearLayout camera_tools_view;
    private ImageView takePhotoBtn;//拍照按钮
    private View changeCameraFlashModeBtn;
    private TextView autoFlashIcon;
    private ImageView swapCameraBtn;//切换按钮

    //变量
    private int mCoverHeight;
    private int mPreviewHeight;
    private int mCameraID = 0;
    private String mFlashMode;
    private Camera mCamera = null;
    private CameraOrientationListener mOrientationListener;
    private int mDisplayOrientation;
    private int mLayoutOrientation;

    //屏幕宽高
    private int screenHeight;
    private int screenWidth;

    public static Fragment newInstance() {
        return new CameraFragment();
    }

    public CameraFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOrientationListener = new CameraOrientationListener(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            Log.d(TAG, "savedInstanceState == null");
            mCameraID = getBackCameraID();//获取后置摄像头的mCameraID
            mFlashMode = Camera.Parameters.FLASH_MODE_AUTO;//auto
        } else {
            Log.d(TAG, "savedInstanceState != null");
            mCameraID = savedInstanceState.getInt(CAMERA_ID_KEY);
            mFlashMode = savedInstanceState.getString(CAMERA_FLASH_KEY);
            mPreviewHeight = savedInstanceState.getInt(PREVIEW_HEIGHT_KEY);
        }

        initMyView(view);

        if (mCoverHeight == 0) {
            //观察者模式
            ViewTreeObserver observer = mSurfaceView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int width = mSurfaceView.getWidth();
                    mPreviewHeight = mSurfaceView.getHeight();
                    mCoverHeight = (mPreviewHeight - width) / 2;

                    Log.d(TAG, "preview width " + width + " height " + mPreviewHeight + "mCoverHeight=" + mCoverHeight);

                    topCoverView.getLayoutParams().height = mCoverHeight;
                    btnCoverView.getLayoutParams().height = mCoverHeight;

                    //sdK版本设置（可忽略选项）
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mSurfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        } else {
            topCoverView.getLayoutParams().height = mCoverHeight;
            btnCoverView.getLayoutParams().height = mCoverHeight;
        }
        initListener();
    }

    private void initMyView(View view) {
        btnCoverView = view.findViewById(R.id.cover_bottom_view);//底部view
        topCoverView = view.findViewById(R.id.cover_top_view);//顶部view
        camera_tools_view = (LinearLayout) view.findViewById(R.id.camera_tools_view);
        mSurfaceView = (SquareSurfacePreview) view.findViewById(R.id.camera_preview_view);
        mSurfaceView.getHolder().addCallback(CameraFragment.this);

        //拍照三个按钮
        takePhotoBtn = (ImageView) view.findViewById(R.id.capture_image_button);//拍照按钮
        changeCameraFlashModeBtn = view.findViewById(R.id.flash);
        autoFlashIcon = (TextView) view.findViewById(R.id.auto_flash_icon);
        swapCameraBtn = (ImageView) view.findViewById(R.id.change_camera);
        changeCameraFlashModeBtn = getView().findViewById(R.id.flash);

        mOrientationListener.enable();//激活监听

        //获取屏幕高宽
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        screenHeight = windowManager.getDefaultDisplay().getHeight();
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        Log.d(TAG, "屏幕宽高：screenHeight=" + screenHeight + "-->screenWidth=" + screenWidth);
    }

    private void initListener() {
        //拍照按钮
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        //切换摄像头
        swapCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCameraID == CameraInfo.CAMERA_FACING_FRONT) {
                    mCameraID = getBackCameraID();
                } else {
                    mCameraID = getFrontCameraID();
                }
                Log.d(TAG, "切换摄像头mCamera=" + (mCamera == null));
                restartPreview();
            }
        });

        //拍照类型按钮(可无选项)

        changeCameraFlashModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlashMode.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_AUTO)) {
                    mFlashMode = Camera.Parameters.FLASH_MODE_ON;
                    autoFlashIcon.setText("On");
                } else if (mFlashMode.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_ON)) {
                    mFlashMode = Camera.Parameters.FLASH_MODE_OFF;
                    autoFlashIcon.setText("Off");
                } else if (mFlashMode.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_OFF)) {
                    mFlashMode = Camera.Parameters.FLASH_MODE_AUTO;
                    autoFlashIcon.setText("Auto");
                }
                //设置相机参数
                setupCamera();
            }
        });
    }

    /**
     * SurfaceHolder.Callback复写方法
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        mSurfaceHolder = holder;
        getCamera(mCameraID);
        Log.d(TAG, "surfaceCreated mCamera == null =" + (mCamera == null));
        startCameraPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case 1:
                Uri imageUri = data.getData();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * Start the camera preview
     */
    private void startCameraPreview() {
        determineDisplayOrientation();
        setupCamera();

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Can't start camera preview due to IOException " + e);
            e.printStackTrace();
        }
    }

    /**
     * Restart
     */
    private void restartPreview() {
        stopCameraPreview();
        mCamera.release();
        getCamera(mCameraID);
        startCameraPreview();
    }

    /**
     * Stop the camera preview
     */
    private void stopCameraPreview() {
        mCamera.stopPreview();

        mCamera.setPreviewCallback(null);//

        mSurfaceView.setCamera(null);
    }

    //初始化Camera
    private void getCamera(int cameraID) {
        Log.d(TAG, "get camera with id " + cameraID);

        try {
            mCamera = open(cameraID);
            mSurfaceView.setCamera(mCamera);
        } catch (Exception e) {
            Log.d(TAG, "Can't open camera with id " + cameraID + "error:" + e.getMessage());
        }
    }

    /**
     * 设置相机参数
     */
    private void setupCamera() {
        Log.d(TAG, "setupCamera: ");
        // Never keep a global parameters
        Camera.Parameters parameters = mCamera.getParameters();

        Camera.Size bestPreviewSize = determineBestPreviewSize(parameters);
        Log.d(TAG, "CameraFragment--setupCamera--PreviewSize---" + "width=" + bestPreviewSize.width + "--height=" + bestPreviewSize.height);
        //        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);


        // Set continuous picture focus, if it's supported
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }


        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes != null && flashModes.contains(mFlashMode)) {
            parameters.setFlashMode(mFlashMode);
            changeCameraFlashModeBtn.setVisibility(View.VISIBLE);
        } else {
            changeCameraFlashModeBtn.setVisibility(View.INVISIBLE);
        }

        // Lock in the changes
        mCamera.setParameters(parameters);
    }

    /**
     * Determine the current display orientation and rotate the camera preview
     * accordingly
     */
    private void determineDisplayOrientation() {
        CameraInfo cameraInfo = new CameraInfo();
        getCameraInfo(mCameraID, cameraInfo);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: {
                Log.d(TAG, "degrees = 0");
                degrees = 0;
                break;
            }
            case Surface.ROTATION_90: {
                Log.d(TAG, "degrees = 90");
                degrees = 90;
                break;
            }
            case Surface.ROTATION_180: {
                Log.d(TAG, "degrees = 180");

                degrees = 180;
                break;
            }
            case Surface.ROTATION_270: {
                Log.d(TAG, "degrees = 270");
                degrees = 270;
                break;
            }
        }
        int displayOrientation;
        // Camera direction
        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
            // Orientation is angle of rotation when facing the camera for
            // the camera image to match the natural orientation of the device
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }
        mDisplayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        mLayoutOrientation = degrees;

        Log.d(TAG, "调整相机角度--determineDisplayOrientation--displayOrientation = " + displayOrientation);
        mCamera.setDisplayOrientation(displayOrientation);//调整相机角度
    }


    //相机预览尺寸
    private Camera.Size determineBestPreviewSize(Camera.Parameters parameters) {
        return determineBestSize(parameters.getSupportedPreviewSizes(), PREVIEW_SIZE_MAX_WIDTH);
    }

    //拍照后预览尺寸
    private Camera.Size determineBestPictureSize(Camera.Parameters parameters) {
        return determineBestSize(parameters.getSupportedPictureSizes(), PICTURE_SIZE_MAX_WIDTH);
    }

    //设置最佳宽高比尺寸
    private Camera.Size determineBestSize(List<Camera.Size> sizes, int widthThreshold) {
        Camera.Size bestSize = null;
        Camera.Size size;
        int numOfSizes = sizes.size();
        for (int i = 0; i < numOfSizes; i++) {
            size = sizes.get(i);

            boolean isDesireRatio = (size.width / 4) == (size.height / 3);//最佳4：3尺寸选择 (size.width / 4) == (size.height / 3)
            boolean isBetterSize = (bestSize == null) || size.width > bestSize.width;//选择最佳尺寸中最大的尺寸

            if (isDesireRatio && isBetterSize) {
                bestSize = size;
                //选中的最佳尺寸
                Log.d(TAG, "determineBestSize: bestSize.width" + (bestSize.width) + "\nbestSize.height:" + bestSize.height);
            }
        }

        if (bestSize == null) {
            Log.d(TAG, "cannot find the best camera size");
            return sizes.get(sizes.size() - 1);
        }

        return bestSize;
    }

    //获取前置cameraid
    private int getFrontCameraID() {
        return getCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    //获取后置cameraid
    private int getBackCameraID() {
        return getCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * @param tagInfo
     * @return 得到特定camera info的id
     */
    private int getCameraId(int tagInfo) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        // 开始遍历摄像头，得到camera info
        int cameraId, cameraCount;
        for (cameraId = 0, cameraCount = Camera.getNumberOfCameras(); cameraId < cameraCount; cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == tagInfo) {
                break;
            }
        }
        return cameraId;
    }

    /**
     * Take a picture
     */
    private void takePicture() {
        mOrientationListener.rememberOrientation();

        // Shutter callback occurs after the image is captured. This can
        // be used to trigger a sound to let the user know that image is taken
        ShutterCallback shutterCallback = null;

        // Raw callback occurs when the raw image data is available
        PictureCallback raw = null;

        // postView callback occurs when a scaled, fully processed
        // postView image is available.
        PictureCallback postView = null;

        // jpeg callback occurs when the compressed image is available
        mCamera.takePicture(shutterCallback, raw, postView, this);
    }

    /**
     * 拍照回调函数
     *
     * @param data
     * @param camera
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        int rotation = (mDisplayOrientation
                + mOrientationListener.getRememberedNormalOrientation()
                + mLayoutOrientation) % 360;
        Log.d(TAG, "onPictureTaken--rotation=" + rotation);

        //        Bitmap bitmap = ImageUtility.rotatePicture(getActivity(), rotation, data);
        //        Uri uri = ImageUtility.savePicture(getActivity(), bitmap);
        Log.d(TAG, "mCoverHeight=" + mCoverHeight + "\nmPreviewHeight=" + mPreviewHeight);

//        if(rotation == 180 || mCameraID == CameraInfo.CAMERA_FACING_FRONT){//对一种情况图做处理
//            rotation = 0;
//        }
        //拍照后图片预览和处理EditSavePhotoFragment
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                EditSavePhotoFragment.newInstance(data, rotation, mCoverHeight, mPreviewHeight),
                EditSavePhotoFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    /**
     * 方向监听
     * When orientation changes, onOrientationChanged(int) of the listener will be called
     */
    private static class CameraOrientationListener extends OrientationEventListener {

        private int mCurrentNormalizedOrientation;
        private int mRememberedNormalOrientation;

        public CameraOrientationListener(Context context) {
            super(context, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation != ORIENTATION_UNKNOWN) {
                mCurrentNormalizedOrientation = normalize(orientation);
            }
        }

        private int normalize(int degrees) {
            if (degrees > 315 || degrees <= 45) {

                return 0;
            }

            if (degrees > 45 && degrees <= 135) {
                return 90;
            }

            if (degrees > 135 && degrees <= 225) {
                return 180;
            }

            if (degrees > 225 && degrees <= 315) {
                return 270;
            }

            throw new RuntimeException("The physics as we know them are no more. Watch out for anomalies.");
        }

        public void rememberOrientation() {
            mRememberedNormalOrientation = mCurrentNormalizedOrientation;
            Log.d(TAG, "CameraOrientationListener--rememberOrientation--mRememberedNormalOrientation=" + mRememberedNormalOrientation);
        }

        public int getRememberedNormalOrientation() {
            Log.d(TAG, "CameraOrientationListener--getRememberedNormalOrientation--mRememberedNormalOrientation=" + mRememberedNormalOrientation);
            return mRememberedNormalOrientation;
        }
    }

    @Override
    public void onStop() {
        mOrientationListener.disable();
        // stop the preview
        stopCameraPreview();
        mCamera.release();
        super.onStop();
    }

}
